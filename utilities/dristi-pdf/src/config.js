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

  DB_USER: process.env.DB_USER || "postgres",
  DB_PASSWORD: process.env.DB_PASSWORD || "postgres",
  DB_HOST: process.env.DB_HOST || "localhost",
  DB_NAME: process.env.DB_NAME || "postgres",
  DB_PORT: process.env.DB_PORT || 5432,

  pdf: {
    issue_of_summon: process.env.PROJECT_DETAILS_TEMPLATE || "summons-issue-qr",
  },

  app: {
    port: parseInt(process.env.APP_PORT) || 8080,
    host: HOST,
    contextPath: process.env.CONTEXT_PATH || "/egov-pdf",
  },

  host: {
    mdms: process.env.EGOV_MDMS_HOST || 'http://localhost:8081',
    pdf: process.env.EGOV_PDF_HOST || 'http://localhost:8070',
    user: process.env.EGOV_USER_HOST || HOST,
    case: process.env.DRISTI_CASE_HOST || 'http://localhost:8091',
    order: process.env.DRISTI_ORDER_HOST || 'http://localhost:8092',
    hrms: process.env.EGOV_HRMS_HOST || 'http://localhost:8082',
    individual: process.env.EGOV_INDIVIDUAL_HOST || 'http://localhost:8085',
    hearing: process.env.DRISTI_HEARING_HOST || 'http://localhost:8093',
    sunbirdrc_credential_service: process.env.EGOV_SUNBIRDRC_CREDENTIAL_HOST || 'http://localhost:8095',
  },

  paths: {
    pdf_create: "/pdf-service/v1/_createnosave",
    case_search:"/case/v1/_search",
    order_search: "/order/v1/search",
    hearing_search: "/hearing/v1/search",
    hrms_search:"/egov-hrms/employees/_search",
    individual_search:"/individual/v1/_search",
    mdms_search: "/egov-mdms-service/v2/_search",
    sunbirdrc_credential_service_search: "/sunbirdrc-credential-service/qrcode/_get",
  },

  constraints: {
    "beneficiaryIdByHeadCode": "Deduction_{tanentId}_{headcode}"
  }
};
