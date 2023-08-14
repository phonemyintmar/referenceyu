package mm.com.mytelpay.adapter.common.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SoapConverter {
    private static final Logger logger = LoggerFactory.getLogger(SoapConverter.class);

    private SoapConverter() {
        // default constructor
    }

    public static Document convertStringToSoap(String input) {
        Document document = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbFactory.setFeature(FEATURE, true);
            FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbFactory.setFeature(FEATURE, false);
            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            dbFactory.setFeature(FEATURE, false);
            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            dbFactory.setFeature(FEATURE, false);
            dbFactory.setXIncludeAware(false);
            dbFactory.setExpandEntityReferences(false);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(new InputSource(new StringReader(input)));
            document.getDocumentElement().normalize();
        } catch (Exception exception) {
            logger.error("error parse data: {}", exception.getMessage());
            exception.printStackTrace();
        }
        return document;
    }
}
