const config = require("../config/config");
const axios = require("axios");
var url = require("url");

/**
 * Extracts case section from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {String} A string object that refers to section Number.
 */
function getCaseSectionNumber(cases) {
  const statutesAndSections = cases.statutesAndSections;
  if (statutesAndSections.length === 0) {
    return "";
  }

  const firstElement = statutesAndSections[0];
  const firstSection = firstElement.sections[0];
  const firstSubsection = firstElement.subsections[0];

  if (firstSection && firstSubsection) {
    return `Section ${firstSubsection} of ${firstSection}`;
  } else {
    return "";
  }
}

/**
 * Extracts document file store id from respective object.
 *
 * @param {Object} fileUploadDocuments - The object containing file store id.
 * @param {String} fileName - File Name to search for in the document.
 * @returns {String} A string object that refers to File Store Id.
 */
function getDocumentFileStore(fileUploadDocuments, fileName) {
  const documents = fileUploadDocuments?.document;
  if (Array.isArray(documents)) {
    const document = documents.find(
      (doc) => doc?.fileName === fileName || doc?.name === fileName
    );
    return document?.fileStore ?? null;
  } else if (documents && documents?.fileName) {
    return documents.fileName === fileName
      ? documents?.fileStore ?? null
      : null;
  }
  return null;
}

function getComplaintAdditionalDocumentFileStore(fileUploadDocuments) {
  if (!Array.isArray(fileUploadDocuments)) return [];

  return fileUploadDocuments
    .flatMap((doc) => doc?.document || [])
    .map((item) => item?.fileStore)
    .filter((fileStore) => fileStore);
}

/**
 * Extracts address information from a nested object.
 *
 * @param {Object} addressObject - The object containing address details.
 * @returns {Object} An object containing extracted address details.
 */
function getAddressDetails(addressObject) {
  return {
    locality: addressObject?.locality || "",
    city: addressObject?.city || "",
    district: addressObject?.district || "",
    state: addressObject?.state || "",
    pincode: addressObject?.pincode || "",
  };
}

function getStringAddressDetails(addressObject) {
  return `${addressObject?.locality || ""}, ${addressObject?.city || ""}, ${
    addressObject?.district || ""
  },  ${addressObject?.state || ""},  ${addressObject?.pincode || ""}`;
}

function formatDate(date) {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
}

function convertDateToDDMMYYYY(dateString) {
  if (!dateString) return "";

  const [year, month, day] = dateString?.split("-");
  return `${day}-${month}-${year}`;
}

function convertToIndianCurrency(amount, locale, currency) {
  if (typeof amount !== "number" && typeof amount !== "string") return "";

  const number = Number(amount);
  if (isNaN(number)) return "";

  return new Intl.NumberFormat(locale, {
    style: "currency",
    currency: currency,
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  })
    .format(number)
    .toString();
}

/**
 * Extracts complainant information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of complainant information objects.
 */
function getComplainantsDetails(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.complainantDetails ||
    !cases.additionalDetails.complainantDetails.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.complainantDetails.formdata.map((formData) => {
    const data = formData.data;
    const complainantType = data.complainantType || "";
    const firstName = data.firstName || "";
    const middleName = data.middleName || "";
    const lastName = data.lastName || "";
    const phoneNumber =
      (data.complainantVerification &&
        data.complainantVerification.mobileNumber) ||
      "";

    if (complainantType.code === "REPRESENTATIVE") {
      const companyDetails = data.addressCompanyDetails || {};
      const companyAddress = getAddressDetails(companyDetails);

      return {
        complainantType: complainantType.name || "",
        representativeName: `${firstName} ${middleName} ${lastName}`,
        name: "",
        phoneNumber,
        companyName: data.companyName || "",
        companyDetailsFileStore:
          getDocumentFileStore(
            data.companyDetailsUpload,
            "Company documents"
          ) || "",
        companyAddress: companyAddress,
        address: "",
      };
    } else {
      const addressDetails =
        (data.complainantVerification &&
          data.complainantVerification.individualDetails &&
          data.complainantVerification.individualDetails.addressDetails) ||
        {};
      const address = getAddressDetails(addressDetails);

      return {
        complainantType: complainantType.name || "",
        name: `${firstName} ${middleName} ${lastName}`,
        representativeName: "",
        phoneNumber,
        address: address,
        companyName: "",
        companyAddress: "",
        companyDetailsFileStore: "",
      };
    }
  });
}

/**
 * Extracts respondent information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of respondent information objects.
 */
function getRespondentsDetails(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.respondentDetails ||
    !cases.additionalDetails.respondentDetails.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.respondentDetails.formdata.map((formData) => {
    const data = formData.data;

    const firstName = data.respondentFirstName || "";
    const middleName = data.respondentMiddleName || "";
    const lastName = data.respondentLastName || "";
    const addresses = data.addressDetails.map((addressDetail) => {
      return getAddressDetails(addressDetail.addressDetails);
    });

    return {
      name: `${firstName} ${middleName} ${lastName}`,
      respondentType: data.respondentType.name,
      phoneNumber:
        data.phonenumbers && data.phonenumbers.mobileNumber
          ? data.phonenumbers.mobileNumber.join(", ")
          : null,
      email:
        data.emails && data.emails.emailId
          ? data.emails.emailId.join(", ")
          : null,
      address: addresses,
      inquiryAffidavitFileStore: getDocumentFileStore(
        data.inquiryAffidavitFileUpload,
        "Affidavit documents"
      ),
    };
  });
}

/**
 * Extracts witness information from the cases object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of witness information objects.
 */
function getWitnessDetails(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.witnessDetails ||
    !cases.additionalDetails.witnessDetails.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.witnessDetails.formdata.map((formData) => {
    const data = formData.data;
    const addresses = data.addressDetails.map((addressDetail) => {
      return getAddressDetails(addressDetail.addressDetails);
    });
    const firstName = data.firstName || "";
    const middleName = data.middleName || "";
    const lastName = data.lastName || "";

    const additionalDetails =
      data &&
      data.witnessAdditionalDetails &&
      typeof data.witnessAdditionalDetails === "object" &&
      data.witnessAdditionalDetails.text
        ? data.witnessAdditionalDetails.text
        : "";

    return {
      name: `${firstName} ${middleName} ${lastName}`,
      phoneNumber:
        data &&
        data.phonenumbers &&
        Array.isArray(data.phonenumbers.mobileNumber) &&
        data.phonenumbers.mobileNumber.length > 0
          ? data.phonenumbers.mobileNumber[0]
          : null,
      email:
        data && data.emails && data.emails.textfieldValue
          ? data.emails.textfieldValue
          : null,
      address: addresses,
      additionalDetails,
    };
  });
}

/**
 * Extracts advocate information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of advocate information objects.
 */
function getAdvocateDetails(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.advocateDetails ||
    !cases.additionalDetails.advocateDetails.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.advocateDetails.formdata.map((formData) => {
    const data = formData.data;

    return {
      name: data.advocateName,
      barRegistrationNumber: data.barRegistrationNumber,
      vakalatnamaFileStore: getDocumentFileStore(
        data.vakalatnamaFileUpload,
        "UPLOAD_VAKALATNAMA"
      ),
      isRepresenting: data.isAdvocateRepresenting.name,
    };
  });
}

/**
 * Extracts cheque information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of cheque information objects.
 */
function getChequeDetails(cases) {
  if (
    !cases.caseDetails ||
    !cases.caseDetails.chequeDetails ||
    !cases.caseDetails.chequeDetails.formdata
  ) {
    return [];
  }
  const chequeDetailsList = cases.caseDetails.chequeDetails.formdata.map(
    (dataItem) => {
      const chequeDetailsData = dataItem.data || {};

      return {
        signatoryName: chequeDetailsData.chequeSignatoryName || null,
        bouncedChequeFileStore: getDocumentFileStore(
          chequeDetailsData.bouncedChequeFileUpload,
          "CS_BOUNCED_CHEQUE"
        ),
        nameOnCheque: chequeDetailsData.name || null,
        chequeNumber: chequeDetailsData.chequeNumber || null,
        dateOfIssuance: chequeDetailsData.issuanceDate || null,
        bankName: chequeDetailsData.bankName || null,
        ifscCode: chequeDetailsData.ifsc || null,
        chequeAmount: chequeDetailsData.chequeAmount || null,
        dateOfDeposit: chequeDetailsData.depositDate || null,
        depositChequeFileStore: getDocumentFileStore(
          chequeDetailsData.depositChequeFileUpload,
          "CS_PROOF_DEPOSIT_CHEQUE"
        ),
        returnMemoFileStore: getDocumentFileStore(
          chequeDetailsData.returnMemoFileUpload,
          "CS_CHEQUE_RETURN_MEMO"
        ),
        chequeAdditionalDetails:
          (chequeDetailsData.chequeAdditionalDetails &&
            chequeDetailsData.chequeAdditionalDetails.text) ||
          null,
      };
    }
  );

  return chequeDetailsList;
}

/**
 * Extracts debt liability information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of debt liability information objects.
 */
function getDebtLiabilityDetails(cases) {
  if (
    !cases.caseDetails ||
    !cases.caseDetails.debtLiabilityDetails ||
    !cases.caseDetails.debtLiabilityDetails.formdata
  ) {
    return [];
  }
  const debtLiabilityDetailsList =
    cases.caseDetails.debtLiabilityDetails.formdata.map((dataItem) => {
      const debtLiabilityData = dataItem.data || {};

      return {
        natureOfDebt:
          (debtLiabilityData.liabilityNature &&
            debtLiabilityData.liabilityNature.name) ||
          null,
        totalAmountCoveredByCheque:
          debtLiabilityData.liabilityType &&
          debtLiabilityData.liabilityType.showAmountCovered
            ? debtLiabilityData.liabilityAmountCovered || null
            : null,
        proofOfLiabilityFileStore: getDocumentFileStore(
          debtLiabilityData.debtLiabilityFileUpload,
          "CS_PROOF_DEBT"
        ),
        additionalDetails:
          (debtLiabilityData.additionalDebtLiabilityDetails &&
            debtLiabilityData.additionalDebtLiabilityDetails.text) ||
          null,
      };
    });

  return debtLiabilityDetailsList;
}

/**
 * Extracts demand notice information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of demand notice information objects.
 */
function getDemandNoticeDetails(cases) {
  if (
    !cases.caseDetails ||
    !cases.caseDetails.demandNoticeDetails ||
    !cases.caseDetails.demandNoticeDetails.formdata
  ) {
    return [];
  }
  const demandNoticeDetailsList =
    cases.caseDetails.demandNoticeDetails.formdata.map((dataItem) => {
      const demandNoticeData = dataItem.data || {};

      return {
        modeOfDispatch:
          (demandNoticeData.modeOfDispatchType &&
            demandNoticeData.modeOfDispatchType.modeOfDispatchType &&
            demandNoticeData.modeOfDispatchType.modeOfDispatchType.name) ||
          null,
        dateOfIssuance: demandNoticeData.dateOfIssuance || null,
        dateOfDispatch: demandNoticeData.dateOfDispatch || null,
        legalDemandNoticeFileStore: getDocumentFileStore(
          demandNoticeData.legalDemandNoticeFileUpload,
          "LEGAL_DEMAND_NOTICE"
        ),
        proofOfDispatchFileStore: getDocumentFileStore(
          demandNoticeData.proofOfDispatchFileUpload,
          "PROOF_OF_DISPATCH_FILE_NAME"
        ),
        proofOfService:
          (demandNoticeData.proofOfService &&
            demandNoticeData.proofOfService.code) ||
          null,
        dateOfDeemedService: demandNoticeData.dateOfService || null,
        dateOfAccrual: demandNoticeData.dateOfAccrual || null,
        proofOfAcknowledgmentFileStore: getDocumentFileStore(
          demandNoticeData.proofOfAcknowledgmentFileUpload,
          "PROOF_LEGAL_DEMAND_NOTICE_FILE_NAME"
        ),
        replyReceived:
          (demandNoticeData.proofOfReply &&
            demandNoticeData.proofOfReply.code) ||
          null,
        dateOfReply: demandNoticeData.dateOfReply || null,
        proofOfReplyFileStore: getDocumentFileStore(
          demandNoticeData.proofOfReplyFileUpload,
          "CS_PROOF_TO_REPLY_DEMAND_NOTICE_FILE_NAME"
        ),
      };
    });

  return demandNoticeDetailsList;
}

/**
 * Extracts delay condonation information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of delay condonation information objects.
 */
function getDelayCondonationDetails(cases) {
  if (
    !cases.caseDetails ||
    !cases.caseDetails.delayApplications ||
    !cases.caseDetails.delayApplications.formdata
  ) {
    return [];
  }
  const delayCondonationDetailsList =
    cases.caseDetails.delayApplications.formdata.map((dataItem) => {
      const delayData = dataItem.data || {};

      return {
        reasonForDelay:
          (delayData.delayApplicationReason &&
            delayData.delayApplicationReason.reasonForDelay) ||
          null,
        proofOfReplyFileStore: getDocumentFileStore(
          delayData.legalDemandNoticeFileUpload,
          "CS_DELAY_CONDONATION_APPLICATION"
        ),
      };
    });

  return delayCondonationDetailsList;
}

/**
 * Extracts prayer and sworn statement information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of prayer and sworn statement information objects.
 */
function getPrayerSwornStatementDetails(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.prayerSwornStatement ||
    !cases.additionalDetails.prayerSwornStatement.formdata
  ) {
    return [];
  }
  const prayerSwornStatementDetailsList =
    cases.additionalDetails.prayerSwornStatement.formdata.map((dataItem) => {
      const swornStatementData = dataItem.data || {};

      return {
        prayerAndSwornStatementType:
          (swornStatementData.prayerAndSwornStatementType &&
            swornStatementData.prayerAndSwornStatementType.name) ||
          null,
        whetherComplainantWillingToSettle:
          (swornStatementData.infoBoxData &&
            swornStatementData.infoBoxData.data) ||
          null,
        circumstancesUnderWhichComplainantWillingToSettle:
          (swornStatementData.caseSettlementCondition &&
            swornStatementData.caseSettlementCondition.text) ||
          null,
        memorandumOfComplaintText:
          (swornStatementData.memorandumOfComplaint &&
            swornStatementData.memorandumOfComplaint.text) ||
          null,
        memorandumOfComplaintFileStore: getDocumentFileStore(
          swornStatementData.memorandumOfComplaint,
          "CS_MEMORANDUM_OF_COMPLAINT_HEADER"
        ),
        prayerForReliefText:
          (swornStatementData.prayerForRelief &&
            swornStatementData.prayerForRelief.text) ||
          null,
        prayerForReliefFileStore: getDocumentFileStore(
          swornStatementData.prayerForRelief,
          "CS_PRAYER_FOR_RELIEF_HEADER"
        ),
        swornStatement:
          getDocumentFileStore(
            swornStatementData.swornStatement,
            "CS_SWORN_STATEMENT_HEADER"
          ) || "",
        additionalDetails:
          (swornStatementData.additionalDetails &&
            swornStatementData.additionalDetails.text) ||
          null,
        additionalActsSectionsToChargeWith:
          (swornStatementData.additionalActsSections &&
            swornStatementData.additionalActsSections.text) ||
          null,
        complaintAdditionalDocumentFileStore:
          getComplaintAdditionalDocumentFileStore(
            swornStatementData?.SelectUploadDocWithName
          ),
      };
    });

  return prayerSwornStatementDetailsList;
}

function getComplainantsDetailsForComplaint(cases) {
  if (
    !cases?.additionalDetails ||
    !cases?.additionalDetails?.complainantDetails ||
    !cases?.additionalDetails?.complainantDetails?.formdata
  ) {
    return [];
  }
  return cases?.additionalDetails?.complainantDetails?.formdata?.map(
    (formData) => {
      const data = formData?.data;
      const complainantType = data?.complainantType || "";
      const firstName = data?.firstName || "";
      const middleName = data?.middleName || "";
      const lastName = data?.lastName || "";
      const phoneNumber =
        (data?.complainantVerification &&
          data?.complainantVerification?.mobileNumber) ||
        "";

      if (complainantType.code === "REPRESENTATIVE") {
        const companyDetails = data.addressCompanyDetails || {};
        const companyAddress = getStringAddressDetails(companyDetails);
        const advocateList = getAdvocateDetailsForComplaint(cases);

        return {
          ifIndividual: false,
          institutionName: data?.complainantCompanyName || "",
          complainantAddress: companyAddress || "",
          nameOfAuthorizedSignatory:
            `${firstName} ${middleName} ${lastName}` || "",
          designationOfAuthorizedSignatory: data?.complainantDesignation || "",
          companyDetailsFileStore:
            getDocumentFileStore(
              data?.companyDetailsUpload,
              "Company documents"
            ) || "",
          advocateList: advocateList,
          isPartyInPerson: advocateList?.[0]?.isPartyInPerson,
        };
      } else {
        const addressDetails =
          (data?.complainantVerification &&
            data?.complainantVerification?.individualDetails &&
            data?.complainantVerification?.individualDetails?.addressDetails) ||
          {};
        const address = getStringAddressDetails(addressDetails);
        const advocateList = getAdvocateDetailsForComplaint(cases);

        return {
          ifIndividual: true,
          complainantName: `${firstName} ${middleName} ${lastName}` || "",
          complainantAge: data?.complainantAge || "",
          complainantAddress: address || "",
          phoneNumber: phoneNumber || "",
          emailId: "",
          complainantIdProofFileStore:
            getDocumentFileStore(
              data?.complainantVerification?.individualDetails
            ) || "",
          advocateList: advocateList,
          isPartyInPerson: advocateList?.[0]?.isPartyInPerson,
        };
      }
    }
  );
}

function getAdvocateDetailsForComplaint(cases) {
  if (
    !cases.additionalDetails ||
    !cases.additionalDetails.advocateDetails ||
    !cases.additionalDetails.advocateDetails.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.advocateDetails.formdata.map(
    (formData, index) => {
      const data = formData.data;

      if (data.isAdvocateRepresenting.code === "NO") {
        return {
          isPartyInPerson: true,
        };
      } else {
        return {
          index: index,
          adjustedIndex: index + 1,
          isPartyInPerson: false,
          advocateName: data.advocateName || "",
          barId: data.barRegistrationNumber || "",
          advocatePhoneNumber:
            data.AdvocateNameDetails.advocateMobileNumber || "",
          vakalatnamaFileStore:
            getDocumentFileStore(data.vakalatnamaFileUpload, "VAKALATNAMA") ||
            "",
        };
      }
    }
  );
}

function getRespondentsDetailsForComplaint(cases) {
  if (
    !cases?.additionalDetails ||
    !cases?.additionalDetails?.respondentDetails ||
    !cases?.additionalDetails?.respondentDetails?.formdata
  ) {
    return [];
  }
  return cases.additionalDetails.respondentDetails.formdata?.map((formData) => {
    const data = formData?.data;
    const respondentType = data?.respondentType || "";
    const firstName = data?.respondentFirstName || "";
    const middleName = data?.respondentMiddleName || "";
    const lastName = data?.respondentLastName || "";
    const addresses = data?.addressDetails?.map((addressDetail) => {
      return getStringAddressDetails(addressDetail?.addressDetails);
    });
    if (respondentType.code === "REPRESENTATIVE") {
      return {
        ifAccusedIndividual: false,
        accusedInstitutionName: data?.respondentCompanyName || "",
        accusedAddress: (addresses && addresses?.join(", ")) || "",
        nameOfAccusedAuthorizedSignatory:
          `${firstName} ${middleName} ${lastName}` || "",
        designationOfAccusedAuthorizedSignatory:
          data?.respondentDesignation || "",
        inquiryAffidavitFileStore:
          getDocumentFileStore(
            data?.inquiryAffidavitFileUpload,
            "Affidavit documents"
          ) || "",
        companyDetailsUpload:
          getDocumentFileStore(
            data?.companyDetailsUpload,
            "Company documents"
          ) || "",
      };
    } else {
      return {
        ifAccusedIndividual: true,
        accusedName: `${firstName} ${middleName} ${lastName}` || "",
        accusedAge: data?.respondentAge || "",
        accusedAddress: (addresses && addresses?.join(", ")) || "",
        accusedPhoneNumber: data?.phonenumbers?.mobileNumber?.join(", ") || "",
        accusedEmailId: data?.emails?.emailId?.join(", ") || "",
        inquiryAffidavitFileStore:
          getDocumentFileStore(
            data?.inquiryAffidavitFileUpload,
            "Affidavit documents"
          ) || "",
      };
    }
  });
}

function getDocumentList(cases) {
  const newDocumentList = [];

  const chequeDetails = getChequeDetails(cases);
  const demandNoticeDetails = getDemandNoticeDetails(cases);

  const bounceCheque = generateBounceChequeDescriptions(chequeDetails);
  const returnMemo = generateChequeReturnMemoDescriptions(chequeDetails);
  const statutoryNotice = generateDemandNoticeDescriptions(demandNoticeDetails);
  const proofOfDispatch =
    generateProofDispatchDescriptions(demandNoticeDetails);
  const proofOfService = generateProofServiceDescriptions(demandNoticeDetails);
  const affidavitInLieuComplaint = [
    "Digital record of proof of Affidavit under section 223 of BNSS",
  ];
  const proofOfReply = generateProofReplyDescriptions(demandNoticeDetails);
  const proofOfDeposit = generateProofDepositDescriptions(chequeDetails);
  const optionalDocs = generateOptionalDocDescriptions(cases.documents);

  newDocumentList.push(
    ...bounceCheque,
    ...returnMemo,
    ...statutoryNotice,
    ...proofOfDispatch,
    ...proofOfService,
    ...affidavitInLieuComplaint,
    ...proofOfReply,
    ...proofOfDeposit,
    ...optionalDocs
  );
  return newDocumentList;
}

function generateBounceChequeDescriptions(chequeDetailsList) {
  return chequeDetailsList.map((chequeDetails) => {
    const chequeNumber = chequeDetails.chequeNumber || "";
    const dateOfIssuance = convertDateToDDMMYYYY(chequeDetails.dateOfIssuance);
    const chequeAmount =
      convertToIndianCurrency(chequeDetails.chequeAmount, "en-IN", "INR") || "";
    return `Digital record of cheque no. ${chequeNumber} dated ${dateOfIssuance} for ${chequeAmount}`;
  });
}

function generateChequeReturnMemoDescriptions(chequeDetailsList) {
  return chequeDetailsList.map((chequeDetails) => {
    const dateOfDishonorCheque = convertDateToDDMMYYYY(
      chequeDetails.dateOfDeposit
    );
    return `Digital record of cheque return memo dated ${dateOfDishonorCheque}.`;
  });
}

function generateDemandNoticeDescriptions(demandNoticeList) {
  return demandNoticeList.map((demandNotice) => {
    const dateOfIssuance = convertDateToDDMMYYYY(demandNotice.dateOfIssuance);
    if (dateOfIssuance) {
      return `Digital record of the statutory notice dated ${dateOfIssuance}.`;
    } else {
      return `Digital record of the statutory notice.`;
    }
  });
}

function generateProofDispatchDescriptions(demandNoticeList) {
  return demandNoticeList.map((demandNotice) => {
    const dateOfDispatch = convertDateToDDMMYYYY(demandNotice.dateOfDispatch);
    return `Digital record of proof of dispatch dated ${dateOfDispatch}.`;
  });
}

function generateProofServiceDescriptions(demandNoticeList) {
  return demandNoticeList
    .filter((demandNotice) => demandNotice.proofOfAcknowledgmentFileStore)
    .map((demandNotice) => {
      const dateOfDeemedService = convertDateToDDMMYYYY(
        demandNotice.dateOfDeemedService
      );
      return `Digital record of proof of service- ${dateOfDeemedService}.`;
    });
}

function generateProofReplyDescriptions(demandNoticeList) {
  return demandNoticeList
    .filter((demandNotice) => demandNotice.proofOfReplyFileStore)
    .map((demandNotice) => {
      const dateOfReply = convertDateToDDMMYYYY(demandNotice.dateOfReply);
      return `Digital record of proof of reply dated ${dateOfReply}.`;
    });
}

function generateProofDepositDescriptions(chequeDetailsList) {
  return chequeDetailsList
    .filter((chequeDetails) => chequeDetails.depositChequeFileStore)
    .map((chequeDetails) => {
      const dateOfDeposit = convertDateToDDMMYYYY(chequeDetails.dateOfDeposit);
      return `Digital record of proof of deposit dated ${dateOfDeposit}.`;
    });
}

function generateOptionalDocDescriptions(documentList) {
  if (!documentList) {
    return [];
  }
  return documentList
    .map((document) => {
      switch (document.documentType) {
        case "AUTHORIZED_COMPLAINANT_COMPANY_REPRESENTATIVE":
          return `Digital record of proof of authorized representative of the company in complainant`;
        case "case.affidavit.225bnss":
          return `Digital record of Affidavit in-lieu-of inquiry under section 225, Bharatiya Nagarik Suraksha Sanhita, 2024`;
        case "AUTHORIZED_ACCUSED_COMPANY_REPRESENTATIVE":
          return `Digital record of proof of authorized representative of the company in accused`;
        case "case.liabilityproof":
          return `Digital record of proof of Debt/Liability`;
        case "CONDONATION_DOC":
          return `Digital record of proof of Delay Condonation`;
        case "case.docs":
          return `Digital record of proof of Complaint Additional Documents`;
        case "VAKALATNAMA_DOC":
          return `Digital record of proof of Advocate Vakalatnama`;
        default:
          return null;
      }
    })
    .filter(Boolean);
}

function getWitnessDetailsForComplaint(cases) {
  if (
    !cases?.additionalDetails ||
    !cases?.additionalDetails?.witnessDetails ||
    !cases?.additionalDetails?.witnessDetails?.formdata
  ) {
    return [];
  }
  return cases?.additionalDetails?.witnessDetails?.formdata?.map((formData) => {
    const data = formData?.data;
    const addresses = data?.addressDetails?.map((addressDetail) => {
      return getStringAddressDetails(addressDetail?.addressDetails);
    });
    const firstName = data?.firstName || "";
    const middleName = data?.middleName || "";
    const lastName = data?.lastName || "";

    return {
      witnessName: `${firstName} ${middleName} ${lastName}` || "",
      witnessOccupation: data?.witnessDesignation,
      witnessPhoneNumber: data?.phonenumbers?.mobileNumber?.join(", ") || "",
      witnessEmail: data?.emails?.emailId?.join(", ") || "",
      witnessAddress: addresses?.join(", ") || "",
      witnessAdditionalDetails: data?.witnessAdditionalDetails?.text || "",
    };
  });
}

async function searchCase(caseId, tenantId, requestinfo) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.caseUrl, config.caseSearchUrl),
      data: {
        RequestInfo: requestinfo,
        tenantId: tenantId,
        criteria: [
          {
            caseId: caseId,
          },
        ],
      },
    });
  } catch (error) {
    throw error;
  }
}

module.exports = {
  getCaseSectionNumber,
  formatDate,
  getComplainantsDetails,
  getRespondentsDetails,
  getWitnessDetails,
  getAdvocateDetails,
  getChequeDetails,
  getDebtLiabilityDetails,
  getDemandNoticeDetails,
  getDelayCondonationDetails,
  getPrayerSwornStatementDetails,
  getComplainantsDetailsForComplaint,
  getAdvocateDetailsForComplaint,
  getRespondentsDetailsForComplaint,
  getDocumentList,
  generateBounceChequeDescriptions,
  generateChequeReturnMemoDescriptions,
  generateDemandNoticeDescriptions,
  generateProofDispatchDescriptions,
  generateProofServiceDescriptions,
  generateProofReplyDescriptions,
  generateProofDepositDescriptions,
  generateOptionalDocDescriptions,
  getWitnessDetailsForComplaint,
  searchCase,
};
