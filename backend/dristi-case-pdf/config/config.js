module.exports = {
    pdfServiceUrl: process.env.PDF_SERVICE_URL || 'http://localhost:8087',
    fileStoreHost: process.env.FILE_STORE_HOST || 'http://localhost:9000',
    courtName: process.env.COURT_NAME || 'DISTRICT COURT KOLLAM',
    courtPlace: process.env.COURT_PLACE || 'Kollam',
    caseUrl: process.env.DRISTI_CASE_HOST || "http://localhost:8091",
    caseSearchUrl: "/case/v1/_search",
};