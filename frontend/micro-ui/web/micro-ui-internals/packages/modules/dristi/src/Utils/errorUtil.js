export class DocumentUploadError extends Error {
  constructor(message, documentType) {
    super(message);
    this.documentType = documentType;
    this.name = "DocumentUploadError";
  }
}
