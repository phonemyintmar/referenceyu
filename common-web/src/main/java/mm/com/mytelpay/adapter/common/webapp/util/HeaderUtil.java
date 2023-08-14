package mm.com.mytelpay.adapter.common.webapp.util;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.util.StrUtils;
import mm.com.mytelpay.adapter.common.util.StringPool;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HeaderUtil {
    private HeaderUtil() {}

    /**
     * createAlert.
     *
     * @param applicationName a {@link String} object.
     * @param message a {@link String} object.
     * @param param a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(ApiConstants.HttpHeaders.X_ACTION_MESSAGE, message);

        try {
            headers.add(
                    ApiConstants.HttpHeaders.X_ACTION_PARAMS,
                    URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            // StandardCharsets are supported by every Java implementation so this exception
            // will never happen
        }

        return headers;
    }

    /**
     * createEntityCreationAlert.
     *
     * @param applicationName a {@link String} object.
     * @param enableTranslation a boolean.
     * @param errorCode a {@link String} object.
     * @param param a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityCreationAlert(
            String applicationName, boolean enableTranslation, String errorCode, String param) {
        String message =
                enableTranslation
                        ? applicationName + "." + errorCode + ".created"
                        : "A new " + errorCode + " is created with identifier " + param;

        return createAlert(message, param);
    }

    /**
     * createEntityDeletionAlert.
     *
     * @param applicationName a {@link String} object.
     * @param enableTranslation a boolean.
     * @param errorCode a {@link String} object.
     * @param param a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityDeletionAlert(
            String applicationName, boolean enableTranslation, String errorCode, String param) {
        String message =
                enableTranslation
                        ? applicationName + "." + errorCode + ".deleted"
                        : "A " + errorCode + " is deleted with identifier " + param;

        return createAlert(message, param);
    }

    /**
     * createEntityUpdateAlert.
     *
     * @param applicationName a {@link String} object.
     * @param enableTranslation a boolean.
     * @param errorCode a {@link String} object.
     * @param param a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityUpdateAlert(
            String applicationName, boolean enableTranslation, String errorCode, String param) {
        String message =
                enableTranslation
                        ? applicationName + "." + errorCode + ".updated"
                        : "A " + errorCode + " is updated with identifier " + param;

        return createAlert(message, param);
    }

    /**
     * createFailureAlert.
     *
     * @param applicationName a {@link String} object.
     * @param enableTranslation a boolean.
     * @param errorCode a {@link String} object.
     * @param errorKey a {@link String} object.
     * @param defaultMessage a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createFailureAlert(
            boolean enableTranslation, String errorKey, Object[] params, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);

        String message = enableTranslation ? errorKey : defaultMessage;

        HttpHeaders headers = new HttpHeaders();

        headers.add(ApiConstants.HttpHeaders.X_ACTION_MESSAGE, message);
        headers.add(ApiConstants.HttpHeaders.X_ACTION_MESSAGE_KEY, errorKey);

        if (params != null && params.length > 0) {
            headers.add(
                    ApiConstants.HttpHeaders.X_ACTION_PARAMS,
                    StrUtils.join(params, StringPool.COMMA));
        }

        return headers;
    }

    public static String getBasicAuthorization(String username, String password) {
        StringBuilder sb = new StringBuilder(3);

        sb.append(username);
        sb.append(StringPool.COLON);
        sb.append(password);

        byte[] encodedAuth =
                org.apache.tomcat.util.codec.binary.Base64.encodeBase64(
                        sb.toString().getBytes(Charset.forName(StringPool.UTF8)));

        return ApiConstants.Header.BASIC_START + new String(encodedAuth);
    }

    public static String getBasicAuthorization(String token) {
        return ApiConstants.Header.BASIC_START + token;
    }

    public static String getBearerAuthorization(String token) {
        return ApiConstants.Header.BEARER_START + token;
    }

    public static String getAuthorization(String tokenType, String token) {
        StringBuilder sb = new StringBuilder(3);

        if (!StrUtils.isEmpty(tokenType)) {
            sb.append(tokenType);
            sb.append(StringPool.SPACE);
        } else {
            sb.append(ApiConstants.Header.BEARER_START);
        }

        sb.append(token);

        return sb.toString();
    }

    public static HttpHeaders getTypeJsonHeaders() {
        HttpHeaders headers = getHeaders();

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public static HttpHeaders getTypeUrlEncodeHeaders() {
        HttpHeaders headers = getHeaders();

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        headers.add(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        return headers;
    }
}
