export const efilingDocumentKeyAndTypeMapping = {
  returnMemoFileUpload: "CHEQUE_RETURN_MEMO",
  legalDemandNoticeFileUpload: "LEGAL_NOTICE",
  vakalatnamaFileUpload: "VAKALATNAMA",
  inquiryAffidavitFileUpload: "AFFIDAVIT_UNDER_225",
};

export const efilingDocumentTypeAndKeyMapping = {
  CHEQUE_RETURN_MEMO: "returnMemoFileUpload",
  LEGAL_NOTICE: "legalDemandNoticeFileUpload",
  VAKALATNAMA: "vakalatnamaFileUpload",
  AFFIDAVIT_UNDER_225: "inquiryAffidavitFileUpload",
};

export const ocrErrorLocations = {
  inquiryAffidavitFileUpload: {
    name: "respondentDetails",
    index: 0,
    fieldName: "image",
    configKey: "litigentDetails",
    inputlist: ["inquiryAffidavitFileUpload.document", "image"],
    fileName: null,
  },
  legalDemandNoticeFileUpload: {
    name: "demandNoticeDetails",
    index: 0,
    fieldName: "image",
    configKey: "caseSpecificDetails",
    inputlist: ["legalDemandNoticeFileUpload.document"],
    fileName: null,
  },
  returnMemoFileUpload: {
    name: "chequeDetails",
    index: 0,
    fieldName: "image",
    configKey: "caseSpecificDetails",
    inputlist: ["returnMemoFileUpload.document"],
    fileName: null,
  },
};
