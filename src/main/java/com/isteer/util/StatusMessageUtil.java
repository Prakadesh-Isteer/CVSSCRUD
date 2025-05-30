package com.isteer.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.isteer.enums.CVSSEnum;

@Component
public class StatusMessageUtil {

	 private static MessageSource messageSource;

	    public StatusMessageUtil(MessageSource messageSource) {
	        StatusMessageUtil.messageSource = messageSource;
	    }

	    public static String getMessage(CVSSEnum enumVal) {
	        return messageSource.getMessage(enumVal.getMessageKey(), null, LocaleContextHolder.getLocale());
	    }
}
