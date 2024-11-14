class DocumentError extends Error {
  constructor(documentType) {
    super(`Error with document type: ${documentType}`);
    this.documentType = documentType;
  }
}

module.exports = { DocumentError };
