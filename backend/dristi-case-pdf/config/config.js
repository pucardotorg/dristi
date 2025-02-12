module.exports = {
  pdfServiceUrl: process.env.PDF_SERVICE_URL || "http://localhost:8087",
  fileStoreHost: process.env.FILE_STORE_HOST || "http://localhost:9000",
  courtName: process.env.COURT_NAME || "IN THE 24X7 ON COURT",
  courtPlace: process.env.COURT_PLACE || "KOLLAM",
  caseUrl: process.env.DRISTI_CASE_HOST || "http://localhost:8091",
  caseSearchUrl: "/case/v1/_search",
  payloadSize: process.env.PAYLOADSIZE || "5mb",
};