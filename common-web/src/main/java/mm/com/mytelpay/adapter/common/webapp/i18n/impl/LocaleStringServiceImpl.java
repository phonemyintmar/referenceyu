package mm.com.mytelpay.adapter.common.webapp.i18n.impl;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.webapp.i18n.LocaleStringService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class LocaleStringServiceImpl implements LocaleStringService {

    private final MessageSource messageSource;

    @Autowired
    public LocaleStringServiceImpl(@Qualifier("messageResourceMytel") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Locale getCurrentLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return Locale.getDefault();
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request.getLocale();
    }

    @Override
    public String getMessage(String messageCode, String defaultMessage, Object... params) {
        Locale currentLocale = getCurrentLocale();
        try {
            return messageSource.getMessage(messageCode, params, currentLocale);
        } catch (Exception e) {
            log.warn(
                    "Could not find message {} for locale {}. Using default message",
                    messageCode,
                    currentLocale);
            try {
                return MessageFormat.format(defaultMessage, params);
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
                return defaultMessage;
            }
        }
    }
}
