const config = require('../config/config'); 


/**
 * Extracts case section from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {String} A string object that refers to section Number.
 */
exports.getCaseSectionNumber = async (cases) => {
    const statutesAndSections = cases.statutesAndSections;
    if (statutesAndSections.length === 0) {
        return '';
    }

    const firstElement = statutesAndSections[0];
    const firstSection = firstElement.sections[0];
    const firstSubsection = firstElement.subsections[0];

    if (firstSection && firstSubsection) {
        return `Section ${firstSubsection} of ${firstSection}`;
    } else {
        return '';
    }
};

/**
 * Extracts document file store id from respective object.
 *
 * @param {Object} fileUploadDocuments - The object containing file store id.
 * @param {String} fileName - File Name to search for in the document.
 * @returns {String} A string object that refers to File Store Id.
 */
const getDocumentFileStore = (fileUploadDocuments, fileName) => {
    const documents = fileUploadDocuments?.document;
    if (Array.isArray(documents)) {
      const document = documents.find(doc => doc?.fileName === fileName);
      return document?.fileStore ?? null;
    } else if (documents && documents?.fileName) {
      return documents.fileName === fileName ? documents?.fileStore ?? null : null;
    }
    return null;
};

/**
 * Extracts address information from a nested object.
 *
 * @param {Object} addressObject - The object containing address details.
 * @returns {Object} An object containing extracted address details.
 */
const getAddressDetails = (addressObject) => {
    return {
      locality: addressObject?.locality || '',
      city: addressObject?.city || '',
      district: addressObject?.district || '',
      state: addressObject?.state || '',
      pincode: addressObject?.pincode || ''
    };
  };


/**
 * Extracts complainant information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of complainant information objects.
 */
exports.getComplainantsDetails = async (cases) => {
    if (!cases.additionalDetails || !cases.additionalDetails.complainantDetails || !cases.additionalDetails.complainantDetails.formdata) {
        return [];
    }
    return cases.additionalDetails.complainantDetails.formdata.map((formData) => {
        const data = formData.data;
        const complainantType = data.complainantType || '';
        const firstName = data.firstName || '';
        const middleName = data.middleName || '';
        const lastName = data.lastName || '';
        const phoneNumber = (data.complainantVerification && data.complainantVerification.mobileNumber) || '';

        if (complainantType.code === 'REPRESENTATIVE') {
            const companyDetails = data.addressCompanyDetails || {};
            const companyAddress = getAddressDetails(companyDetails);

            return {
                complainantType: complainantType.name || '',
                representativeName: `${firstName} ${middleName} ${lastName}`,
                name: '',
                phoneNumber,
                companyName: data.companyName || '',
                companyDetailsFileStore: getDocumentFileStore(data.companyDetailsUpload, 'Company documents') || '',
                companyAddress: companyAddress,
                address: ''
            };
        } else {
            const addressDetails = data.complainantVerification && data.complainantVerification.individualDetails && data.complainantVerification.individualDetails.addressDetails || {};
            const address = getAddressDetails(addressDetails);

            return {
                complainantType: complainantType.name || '',
                name: `${firstName} ${middleName} ${lastName}`,
                representativeName: '',
                phoneNumber,
                address: address,
                companyName: '',
                companyAddress: '',
                companyDetailsFileStore: ''
            };
        }
    });
};

/**
 * Extracts respondent information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of respondent information objects.
 */
exports.getRespondentsDetails = async (cases) => {
    if (!cases.additionalDetails || !cases.additionalDetails.respondentDetails || !cases.additionalDetails.respondentDetails.formdata) {
        return [];
    }
    return cases.additionalDetails.respondentDetails.formdata.map((formData) => {
        const data = formData.data;

        const firstName = data.respondentFirstName || '';
        const middleName = data.respondentMiddleName || '';
        const lastName = data.respondentLastName || '';
        const addresses = data.addressDetails.map((addressDetail) => {
            return getAddressDetails(addressDetail.addressDetails);
        });

        return {
            name: `${firstName} ${middleName} ${lastName}`,
            respondentType: data.respondentType.name,
            phoneNumber: data.phonenumbers && data.phonenumbers.mobileNumber ? data.phonenumbers.mobileNumber.join(', ') : null,
            email: data.emails && data.emails.emailId ? data.emails.emailId.join(', ') : null,
            address: addresses,
            inquiryAffidavitFileStore: getDocumentFileStore(data.inquiryAffidavitFileUpload, 'Affidavit documents')
        };
    });
};

/**
 * Extracts witness information from the cases object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of witness information objects.
 */
exports.getWitnessDetails = async (cases) => {
    if (!cases.additionalDetails || !cases.additionalDetails.witnessDetails || !cases.additionalDetails.witnessDetails.formdata) {
        return [];
    }
    return cases.additionalDetails.witnessDetails.formdata.map((formData) => {
        const data = formData.data;
        const addresses = data.addressDetails.map((addressDetail) => {
            return getAddressDetails(addressDetail.addressDetails);
        });
        const firstName = data.firstName || '';
        const middleName = data.middleName || '';
        const lastName = data.lastName || '';

        const additionalDetails = data && data.witnessAdditionalDetails && typeof data.witnessAdditionalDetails === 'object' && data.witnessAdditionalDetails.text ? data.witnessAdditionalDetails.text : '';

        return {
            name: `${firstName} ${middleName} ${lastName}`,
            phoneNumber: data && data.phonenumbers && Array.isArray(data.phonenumbers.mobileNumber) && data.phonenumbers.mobileNumber.length > 0 ? data.phonenumbers.mobileNumber[0] : null,
            email: data && data.emails && data.emails.textfieldValue ? data.emails.textfieldValue : null,
            address: addresses,
            additionalDetails
        };
    });
};

/**
 * Extracts advocate information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of advocate information objects.
 */
exports.getAdvocateDetails = async (cases) => {
    if (!cases.additionalDetails || !cases.additionalDetails.advocateDetails || !cases.additionalDetails.advocateDetails.formdata) {
        return [];
    }
    return cases.additionalDetails.advocateDetails.formdata.map((formData) => {
        const data = formData.data;

        return {
            name: data.advocateName,
            barRegistrationNumber: data.barRegistrationNumber,
            vakalatnamaFileStore: getDocumentFileStore(data.vakalatnamaFileUpload, 'UPLOAD_VAKALATNAMA'),
            isRepresenting: data.isAdvocateRepresenting.name
        };
    });
};

/**
 * Extracts cheque information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of cheque information objects.
 */
exports.getChequeDetails = (cases) => {
    if (!cases.caseDetails || !cases.caseDetails.chequeDetails || !cases.caseDetails.chequeDetails.formdata) {
        return [];
    }
    const chequeDetailsList = cases.caseDetails.chequeDetails.formdata.map(dataItem => {
        const chequeDetailsData = dataItem.data || {};

        return {
            signatoryName: chequeDetailsData.chequeSignatoryName || null,
            bouncedChequeFileStore: getDocumentFileStore(chequeDetailsData.bouncedChequeFileUpload, 'CS_BOUNCED_CHEQUE'),
            nameOnCheque: chequeDetailsData.name || null,
            chequeNumber: chequeDetailsData.chequeNumber || null,
            dateOfIssuance: chequeDetailsData.issuanceDate || null,
            bankName: chequeDetailsData.bankName || null,
            ifscCode: chequeDetailsData.ifsc || null,
            chequeAmount: chequeDetailsData.chequeAmount || null,
            dateOfDeposit: chequeDetailsData.depositDate || null,
            depositChequeFileStore: getDocumentFileStore(chequeDetailsData.depositChequeFileUpload, 'CS_PROOF_DEPOSIT_CHEQUE'),
            returnMemoFileStore: getDocumentFileStore(chequeDetailsData.returnMemoFileUpload, 'CS_CHEQUE_RETURN_MEMO'),
            chequeAdditionalDetails: chequeDetailsData.chequeAdditionalDetails && chequeDetailsData.chequeAdditionalDetails.text || null
        };
    });

    return chequeDetailsList;
};

/**
 * Extracts debt liability information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of debt liability information objects.
 */
exports.getDebtLiabilityDetails = (cases) => {
    if (!cases.caseDetails || !cases.caseDetails.debtLiabilityDetails || !cases.caseDetails.debtLiabilityDetails.formdata) {
        return [];
    }
    const debtLiabilityDetailsList = cases.caseDetails.debtLiabilityDetails.formdata.map(dataItem => {
        const debtLiabilityData = dataItem.data || {};

        return {
            natureOfDebt: debtLiabilityData.liabilityNature && debtLiabilityData.liabilityNature.name || null,
            totalAmountCoveredByCheque: debtLiabilityData.liabilityType && debtLiabilityData.liabilityType.showAmountCovered ? debtLiabilityData.liabilityAmountCovered || null : null,
            proofOfLiabilityFileStore: getDocumentFileStore(debtLiabilityData.debtLiabilityFileUpload, 'CS_PROOF_DEBT'),
            additionalDetails: debtLiabilityData.additionalDebtLiabilityDetails && debtLiabilityData.additionalDebtLiabilityDetails.text || null
        };
    });

    return debtLiabilityDetailsList;
};

/**
 * Extracts demand notice information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of demand notice information objects.
 */
exports.getDemandNoticeDetails = (cases) => {
    if (!cases.caseDetails || !cases.caseDetails.demandNoticeDetails || !cases.caseDetails.demandNoticeDetails.formdata) {
        return [];
    }
    const demandNoticeDetailsList = cases.caseDetails.demandNoticeDetails.formdata.map(dataItem => {
        const demandNoticeData = dataItem.data || {};

        return {
            modeOfDispatch: demandNoticeData.modeOfDispatchType && demandNoticeData.modeOfDispatchType.modeOfDispatchType && demandNoticeData.modeOfDispatchType.modeOfDispatchType.name || null,
            dateOfIssuance: demandNoticeData.dateOfIssuance || null,
            dateOfDispatch: demandNoticeData.dateOfDispatch || null,
            legalDemandNoticeFileStore: getDocumentFileStore(demandNoticeData.legalDemandNoticeFileUpload, 'LEGAL_DEMAND_NOTICE'),
            proofOfDispatchFileStore: getDocumentFileStore(demandNoticeData.proofOfDispatchFileUpload, 'PROOF_OF_DISPATCH_FILE_NAME'),
            proofOfService: demandNoticeData.proofOfService && demandNoticeData.proofOfService.code || null,
            dateOfDeemedService: demandNoticeData.dateOfDeemedService || null,
            dateOfAccrual: demandNoticeData.dateOfAccrual || null,
            proofOfAcknowledgmentFileStore: getDocumentFileStore(demandNoticeData.proofOfAcknowledgmentFileUpload, 'PROOF_LEGAL_DEMAND_NOTICE_FILE_NAME'),
            replyReceived: demandNoticeData.proofOfReply && demandNoticeData.proofOfReply.code || null,
            dateOfReply: demandNoticeData.dateOfReply || null,
            proofOfReplyFileStore: getDocumentFileStore(demandNoticeData.proofOfReplyFileUpload, 'CS_PROOF_TO_REPLY_DEMAND_NOTICE_FILE_NAME')
        };
    });

    return demandNoticeDetailsList;
};

/**
 * Extracts delay condonation information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of delay condonation information objects.
 */
exports.getDelayCondonationDetails = (cases) => {
    if (!cases.caseDetails || !cases.caseDetails.delayApplications || !cases.caseDetails.delayApplications.formdata) {
        return [];
    }
    const delayCondonationDetailsList = cases.caseDetails.delayApplications.formdata.map(dataItem => {
        const delayData = dataItem.data || {};

        return {
            reasonForDelay: delayData.delayApplicationReason && delayData.delayApplicationReason.reasonForDelay || null,
            proofOfReplyFileStore: getDocumentFileStore(delayData.legalDemandNoticeFileUpload, 'CS_DELAY_CONDONATION_APPLICATION')
        };
    });

    return delayCondonationDetailsList;
};

/**
 * Extracts prayer and sworn statement information from the case object.
 *
 * @param {Object} cases - The cases object containing court case details.
 * @returns {Array} An array of prayer and sworn statement information objects.
 */
exports.getPrayerSwornStatementDetails = (cases) => {
    if (!cases.additionalDetails || !cases.additionalDetails.prayerSwornStatement || !cases.additionalDetails.prayerSwornStatement.formdata) {
        return [];
    }
    const prayerSwornStatementDetailsList = cases.additionalDetails.prayerSwornStatement.formdata.map(dataItem => {
        const swornStatementData = dataItem.data || {};

        return {
            prayerAndSwornStatementType: swornStatementData.prayerAndSwornStatementType && swornStatementData.prayerAndSwornStatementType.name || null,
            whetherComplainantWillingToSettle: swornStatementData.infoBoxData && swornStatementData.infoBoxData.data || null,
            circumstancesUnderWhichComplainantWillingToSettle: swornStatementData.caseSettlementCondition && swornStatementData.caseSettlementCondition.text || null,
            memorandumOfComplaintText: swornStatementData.memorandumOfComplaint && swornStatementData.memorandumOfComplaint.text || null,
            memorandumOfComplaintFileStore: getDocumentFileStore(swornStatementData.memorandumOfComplaint, 'CS_MEMORANDUM_OF_COMPLAINT_HEADER'),
            prayerForReliefText: swornStatementData.prayerForRelief && swornStatementData.prayerForRelief.text || null,
            prayerForReliefFileStore: getDocumentFileStore(swornStatementData.prayerForRelief, 'CS_PRAYER_FOR_RELIEF_HEADER'),
            swornStatement: getDocumentFileStore(swornStatementData.swornStatement, 'CS_SWORN_STATEMENT_HEADER') || '',
            additionalDetails: swornStatementData.additionalDetails && swornStatementData.additionalDetails.text || null,
            additionalActsSectionsToChargeWith: swornStatementData.additionalActsSections && swornStatementData.additionalActsSections.text || null
        };
    });

    return prayerSwornStatementDetailsList;
};
