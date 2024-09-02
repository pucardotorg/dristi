package org.egov.web.notification.mail.utils;

public class Constants {

	public static final String SMS_REQ_MOBILE_NO_KEY_NAME = "mobileNumber";
	public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

	public static final String SMS_REQ_MSG_KEY_NAME = "message";
	public static final String EMAIL_TEMPLATE_MODULE_NAME = "mail-template";
	public static final String EMAIL_TEMPLATE_MASTER_NAME = "email-template";
	public static  final String ERROR_WHILE_FETCHING_MESSAGES = "Error while fetching messages from localization: ";
	public static final String MESSAGE_LOCALE = "en_IN";
	public static final String LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";
	public static final String LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";

	public static final String LOCALIZATION_MODULE = "notification";
	public static final String GREETING = "NOTIFICATION_GREETING";
	public static final String FOOTER = "NOTIFICATION_FOOTER";
	public static final String MESSAGE_CONSTRUCTION_ERROR = "Error while constructing message: ";

}
