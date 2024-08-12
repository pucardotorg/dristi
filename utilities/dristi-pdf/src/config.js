// config.js
// const env = process.env.NODE_ENV; // 'dev' or 'test'

HOST = process.env.EGOV_HOST || "localhost";


if (!HOST) {
  console.log("You need to set the HOST variable");
  process.exit(1);
}

module.exports = {
  auth_token: process.env.AUTH_TOKEN,

  KAFKA_BROKER_HOST: process.env.KAFKA_BROKER_HOST || "localhost:9092",
  KAFKA_RECEIVE_CREATE_JOB_TOPIC: process.env.KAFKA_RECEIVE_CREATE_JOB_TOPIC || "PDF_GEN_RECEIVE",
  KAFKA_BULK_PDF_TOPIC: process.env.KAFKA_BULK_PDF_TOPIC || "BULK_PDF_GEN",
  KAFKA_PAYMENT_EXCEL_GEN_TOPIC: process.env.KAFKA_PAYMENT_EXCEL_GEN_TOPIC || "PAYMENT_EXCEL_GEN",
  KAFKA_EXPENSE_PAYMENT_CREATE_TOPIC: process.env.KAFKA_EXPENSE_PAYMENT_CREATE_TOPIC || "expense-payment-create",

  PDF_BATCH_SIZE: process.env.PDF_BATCH_SIZE || 40,

  DB_USER: process.env.DB_USER,
  DB_PASSWORD: process.env.DB_PASSWORD,
  DB_HOST: process.env.DB_HOST || "localhost",
  DB_NAME: process.env.DB_NAME || "postgres",
  DB_PORT: process.env.DB_PORT || 5432,

  pdf: {
    summons_issue: process.env.SUMMONS_ISSUE || "summons-issue",
    summons_issue_qr: process.env.SUMMONS_ISSUE_QR || "summons-issue-qr",
    order_generic: process.env.ORDER_GENERIC || "order-generic",
    order_generic_qr: process.env.ORDER_GENERIC_QR || "order-generic-qr",
    case_transfer: process.env.CASE_TRANSFER || "case-transfer",
    case_transfer_qr: process.env.CASE_TRANSFER_QR || "case-transfer-qr",
    case_settlement_acceptance: process.env.CASE_SETTLEMENT_ACCEPTANCE || "case-settlement-acceptance",
    case_settlement_acceptance_qr: process.env.CASE_SETTLEMENT_ACCEPTANCE_QR || "case-settlement-acceptance-qr",
    case_settlement_rejection: process.env.CASE_SETTLEMENT_REJECTION || "case-settlement-rejection",
    case_settlement_rejection_qr: process.env.CASE_SETTLEMENT_REJECTION_QR || "case-settlement-rejection-qr",
    adr_case_referral: process.env.ADR_CASE_REFERRAL || "adr-case-referral",
    adr_case_referral_qr: process.env.ADR_CASE_REFERRAL_QR || "adr-case-referral-qr",
    mandatory_async_submissions_responses: process.env.MANDATORY_ASYNC_SUBMISSIONS_RESPONSES || "mandatory-async-submissions-responses",
    mandatory_async_submissions_responses_qr: process.env.MANDATORY_ASYNC_SUBMISSIONS_RESPONSES_QR || "mandatory-async-submissions-responses-qr",
    reschedule_request_judge: process.env.RESCHEDULE_REQUEST_JUDGE || "reschedule-request-judge",
    reschedule_request_judge_qr: process.env.RESCHEDULE_REQUEST_JUDGE_QR || "reschedule-request-judge-qr",
    new_hearing_date_after_reschedule: process.env.NEW_HEARING_DATE_AFTER_RESCHEDULE || "new-hearing-date-after-rescheduling",
    new_hearing_date_after_reschedule_qr: process.env.NEW_HEARING_DATE_AFTER_RESCHEDULE_QR || "new-hearing-date-after-rescheduling-qr",
    schedule_hearing_date: process.env.SCHEDULE_HEARING_DATE || "schedule-hearing-date",
    schedule_hearing_date_qr: process.env.SCHEDULE_HEARING_DATE_QR || "schedule-hearing-date-qr",
    accept_rescheduling_request: process.env.ACCEPT_RESCHEDULING_REQUEST || "accept-reschedule-request",
    accept_rescheduling_request_qr: process.env.ACCEPT_RESCHEDULING_REQUEST_QR || "accept-reschedule-request-qr",
    reject_rescheduling_request: process.env.REJECT_RESCHEDULING_REQUEST || "reject-reschedule-request",
    reject_rescheduling_request_qr: process.env.REJECT_RESCHEDULING_REQUEST_QR || "reject-reschedule-request-qr",
    accept_adr_application : process.env.ACCEPT_ADR_APPLICATION || "accept-adr-application",
    accept_adr_application_qr : process.env.ACCEPT_ADR_APPLICATION_QR || "accept-adr-application-qr",
    reject_adr_application: process.env.REJECT_ADR_APPLICATION || "reject-adr-application",
    reject_adr_application_qr: process.env.REJECT_ADR_APPLICATION_QR || "reject-adr-application-qr",
  },

  app: {
    port: parseInt(process.env.APP_PORT) || 8080,
    host: HOST,
    contextPath: process.env.CONTEXT_PATH || "/egov-pdf",
  },

  host: {
    mdms: process.env.EGOV_MDMS_HOST || 'http://localhost:8081',
    pdf: process.env.EGOV_PDF_HOST || 'http://localhost:8070',
    case: process.env.DRISTI_CASE_HOST || 'http://localhost:8091',
    order: process.env.DRISTI_ORDER_HOST || 'http://localhost:8092',
    hrms: process.env.EGOV_HRMS_HOST || 'http://localhost:8082',
    individual: process.env.EGOV_INDIVIDUAL_HOST || 'http://localhost:8085',
    hearing: process.env.DRISTI_HEARING_HOST || 'http://localhost:8093',
    sunbirdrc_credential_service: process.env.EGOV_SUNBIRDRC_CREDENTIAL_HOST || 'http://localhost:8095',
    application: process.env.DRISTI_APPLICATION_HOST || 'http://localhost:8094',
    localization: process.env.EGOV_LOCALIZATION_HOST || 'http://localhost:8083',
    filestore: process.env.EGOV_FILESTORE_SERVICE_HOST || 'http://localhost:8084',
  },

  paths: {
    pdf_create: "/pdf-service/v1/_create",
    case_search:"/case/v1/_search",
    order_search: "/order/v1/search",
    hearing_search: "/hearing/v1/search",
    application_search: "/application/v1/search",
    hrms_search:"/egov-hrms/employees/_search",
    individual_search:"/individual/v1/_search",
    mdms_search: "/egov-mdms-service/v2/_search",
    sunbirdrc_credential_service_search: "/sunbirdrc-credential-service/qrcode/_get",
  },

  constraints: {
    "beneficiaryIdByHeadCode": "Deduction_{tanentId}_{headcode}"
  }
};
