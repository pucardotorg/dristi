const config = require('../config/config'); 


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

const getDocumentFileStore = (documents, fileName) => {
    if (Array.isArray(documents)) {
        // If documents is an array, find the document by fileName
        const document = documents.find(doc => doc.fileName === fileName);
        return document ? document.fileStore : null;
    } else if (documents && documents.fileName) {
        // If documents is a single object and has a fileName property
        return documents.fileName === fileName ? documents.fileStore : null;
    }
    // Return null if no match is found or if documents is null/undefined
    return null;
};

exports.getComplainantsDetails = async (cases) => {
    return cases.additionalDetails.complainantDetails.formdata.map((formData) => {
        const data = formData.data;
        const complainantType = data.complainantType || '';
        const firstName = data.firstName || '';
        const middleName = data.middleName || '';
        const lastName = data.lastName || '';
        const phoneNumber = (data.complainantVerification && data.complainantVerification.mobileNumber) || '';

        if (complainantType.code === 'REPRESENTATIVE') {
            const companyDetails = data.addressCompanyDetails || {};
            const companyAddress = {
                locality: companyDetails.locality || '',
                city: companyDetails.city || '',
                district: companyDetails.district || '',
                state: companyDetails.state || '',
                pincode: companyDetails.pincode || ''
            };

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
            const address = {
                locality: addressDetails.locality || '',
                city: addressDetails.city || '',
                district: addressDetails.district || '',
                state: addressDetails.state || '',
                pincode: addressDetails.pincode || ''
            };

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


exports.getRespondentsDetails = async (cases) => {
    return cases.additionalDetails.respondentDetails.formdata.map((formData) => {
        const data = formData.data;

        const firstName = data.respondentFirstName || '';
        const middleName = data.respondentMiddleName || '';
        const lastName = data.respondentLastName || '';
        const addresses = data.addressDetails.map((addressDetail) => {
            return {
                locality: addressDetail.addressDetails.locality,
                city: addressDetail.addressDetails.city,
                district: addressDetail.addressDetails.district,
                state: addressDetail.addressDetails.state,
                pincode: addressDetail.addressDetails.pincode
            };
        });
        const affidavitDocument = data.inquiryAffidavitFileUpload && data.inquiryAffidavitFileUpload.document.find(doc => doc.fileName === 'Affidavit documents');

        return {
            name: `${firstName} ${middleName} ${lastName}`,
            respondentType: data.respondentType.name,
            phoneNumber: data.mobileNumber || null,
            email: data.email || null,
            address: addresses,
            inquiryAffidavitFileStore: affidavitDocument ? affidavitDocument.fileStore : null
        };
    });
};

exports.getWitnessDetails = async (cases) => {
    return cases.additionalDetails.witnessDetails.formdata.map((formData) => {
        const data = formData.data;
        const addresses = data.addressDetails.map((addressDetail) => {
            return {
                locality: addressDetail.addressDetails.locality,
                city: addressDetail.addressDetails.city,
                district: addressDetail.addressDetails.district,
                state: addressDetail.addressDetails.state,
                pincode: addressDetail.addressDetails.pincode
            };
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

exports.getAdvocateDetails = async (cases) => {
    return cases.additionalDetails.advocateDetails.formdata.map((formData) => {
        const data = formData.data;
        
        const vakalatnamaDocument = data.vakalatnamaFileUpload && data.vakalatnamaFileUpload.document.find(doc => doc.fileName === 'UPLOAD_VAKALATNAMA');
        
        return {
            name: data.advocateName,
            barRegistrationNumber: data.barRegistrationNumber,
            vakalatnamaFileStore: vakalatnamaDocument ? vakalatnamaDocument.fileStore : null,
            isRepresenting: data.isAdvocateRepresenting.name
        };
    });
};

exports.getChequeDetails = (cases) => {
    const chequeDetailsList = cases.caseDetails.chequeDetails.formdata.map(dataItem => {
        const chequeDetailsData = dataItem.data || {};

        const bouncedChequeDocument = chequeDetailsData.bouncedChequeFileUpload && chequeDetailsData.bouncedChequeFileUpload.document.find(doc => doc.fileName === 'CS_BOUNCED_CHEQUE');
        const depositChequeDocument = chequeDetailsData.depositChequeFileUpload && chequeDetailsData.depositChequeFileUpload.document.find(doc => doc.fileName === 'CS_PROOF_DEPOSIT_CHEQUE');
        const returnMemoDocument = chequeDetailsData.returnMemoFileUpload && chequeDetailsData.returnMemoFileUpload.document.find(doc => doc.fileName === 'CS_CHEQUE_RETURN_MEMO');

        return {
            signatoryName: chequeDetailsData.chequeSignatoryName || null,
            bouncedChequeFileStore: bouncedChequeDocument ? bouncedChequeDocument.fileStore : null,
            nameOnCheque: chequeDetailsData.name || null,
            chequeNumber: chequeDetailsData.chequeNumber || null,
            dateOfIssuance: chequeDetailsData.issuanceDate || null,
            bankName: chequeDetailsData.bankName || null,
            ifscCode: chequeDetailsData.ifsc || null,
            chequeAmount: chequeDetailsData.chequeAmount || null,
            dateOfDeposit: chequeDetailsData.depositDate || null,
            depositChequeFileStore: depositChequeDocument ? depositChequeDocument.fileStore : null,
            returnMemoFileStore: returnMemoDocument ? returnMemoDocument.fileStore : null,
            chequeAdditionalDetails: chequeDetailsData.chequeAdditionalDetails && chequeDetailsData.chequeAdditionalDetails.text || null
        };
    });

    return chequeDetailsList;
};

exports.getDebtLiabilityDetails = (cases) => {
    const debtLiabilityDetailsList = cases.caseDetails.debtLiabilityDetails.formdata.map(dataItem => {
        const debtLiabilityData = dataItem.data || {};

        const proofOfLiabilityDocument = debtLiabilityData.debtLiabilityFileUpload && debtLiabilityData.debtLiabilityFileUpload.document.find(doc => doc.fileName === 'CS_PROOF_DEBT');

        return {
            natureOfDebt: debtLiabilityData.liabilityNature && debtLiabilityData.liabilityNature.name || null,
            totalAmountCoveredByCheque: debtLiabilityData.liabilityType && debtLiabilityData.liabilityType.showAmountCovered ? debtLiabilityData.liabilityAmountCovered || null : null,
            proofOfLiabilityFileStore: proofOfLiabilityDocument ? proofOfLiabilityDocument.fileStore : null,
            additionalDetails: debtLiabilityData.additionalDebtLiabilityDetails && debtLiabilityData.additionalDebtLiabilityDetails.text || null
        };
    });

    return debtLiabilityDetailsList;
};

exports.getDemandNoticeDetails = (cases) => {
    const demandNoticeDetailsList = cases.caseDetails.demandNoticeDetails.formdata.map(dataItem => {
        const demandNoticeData = dataItem.data || {};

        const legalDemandNoticeDocument = demandNoticeData.legalDemandNoticeFileUpload && demandNoticeData.legalDemandNoticeFileUpload.document.find(doc => doc.fileName === 'LEGAL_DEMAND_NOTICE');
        const proofOfServiceDocument = demandNoticeData.proofOfDispatchFileUpload && demandNoticeData.proofOfDispatchFileUpload.document.find(doc => doc.fileName === 'PROOF_OF_DISPATCH_FILE_NAME');
        const proofOfAcknowledgmentDocument = demandNoticeData.proofOfAcknowledgmentFileUpload && demandNoticeData.proofOfAcknowledgmentFileUpload.document.find(doc => doc.fileName === 'PROOF_LEGAL_DEMAND_NOTICE_FILE_NAME');
        const proofOfReplyDocument = demandNoticeData.proofOfReplyFileUpload && demandNoticeData.proofOfReplyFileUpload.document.find(doc => doc.fileName === 'CS_PROOF_TO_REPLY_DEMAND_NOTICE_FILE_NAME');

        return {
            modeOfDispatch: demandNoticeData.modeOfDispatchType && demandNoticeData.modeOfDispatchType.modeOfDispatchType && demandNoticeData.modeOfDispatchType.modeOfDispatchType.name || null,
            dateOfIssuance: demandNoticeData.dateOfIssuance || null,
            dateOfDispatch: demandNoticeData.dateOfDispatch || null,
            legalDemandNoticeFileStore: legalDemandNoticeDocument ? legalDemandNoticeDocument.fileStore : null,
            proofOfDispatchFileStore: proofOfServiceDocument ? proofOfServiceDocument.fileStore : null,
            proofOfService: demandNoticeData.proofOfService && demandNoticeData.proofOfService.code || null,
            dateOfDeemedService: demandNoticeData.dateOfDeemedService || null,
            dateOfAccrual: demandNoticeData.dateOfAccrual || null,
            proofOfAcknowledgmentFileStore: proofOfAcknowledgmentDocument ? proofOfAcknowledgmentDocument.fileStore : null,
            replyReceived: demandNoticeData.proofOfReply && demandNoticeData.proofOfReply.code || null,
            dateOfReply: demandNoticeData.dateOfReply || null,
            proofOfReplyFileStore: proofOfReplyDocument ? proofOfReplyDocument.fileStore : null,
        };
    });

    return demandNoticeDetailsList;
};

exports.getDelayCondonationDetails = (cases) => {
    const delayCondonationDetailsList = cases.caseDetails.delayApplications.formdata.map(dataItem => {
        const delayData = dataItem.data || {};

        const delayCondonationDocument = delayData.legalDemandNoticeFileUpload && delayData.legalDemandNoticeFileUpload.document.find(doc => doc.fileName === 'CS_DELAY_CONDONATION_APPLICATION');

        return {
            reasonForDelay: delayData.delayApplicationReason && delayData.delayApplicationReason.reasonForDelay || null,
            delayCondonationFileStore: delayCondonationDocument ? delayCondonationDocument.fileStore : null
        };
    });

    return delayCondonationDetailsList;
};

exports.getPrayerSwornStatementDetails = (cases) => {
    const prayerSwornStatementDetailsList = cases.additionalDetails.prayerSwornStatement.formdata.map(dataItem => {
        const swornStatementData = dataItem.data || {};

        const swornStatementDocument = swornStatementData.swornStatement && swornStatementData.swornStatement.document.find(doc => doc.fileName === 'CS_SWORN_STATEMENT_HEADER');

        return {
            prayerAndSwornStatementType: swornStatementData.prayerAndSwornStatementType && swornStatementData.prayerAndSwornStatementType.name || null,
            whetherComplainantWillingToSettle: swornStatementData.infoBoxData && swornStatementData.infoBoxData.data || null,
            circumstancesUnderWhichComplainantWillingToSettle: swornStatementData.caseSettlementCondition && swornStatementData.caseSettlementCondition.text || null,
            memorandumOfComplaintText: swornStatementData.memorandumOfComplaint && swornStatementData.memorandumOfComplaint.text || null,
            memorandumOfComplaintFileStore: getDocumentFileStore(swornStatementData.memorandumOfComplaint.document, 'CS_MEMORANDUM_OF_COMPLAINT_HEADER'),
            prayerForReliefText: swornStatementData.prayerForRelief && swornStatementData.prayerForRelief.text || null,
            prayerForReliefFileStore: getDocumentFileStore(swornStatementData.prayerForRelief.document, 'CS_PRAYER_FOR_RELIEF_HEADER'),
            swornStatement: swornStatementDocument ? swornStatementDocument.fileStore : null,
            additionalDetails: swornStatementData.additionalDetails && swornStatementData.additionalDetails.text || null,
            additionalActsSectionsToChargeWith: swornStatementData.additionalActsSections && swornStatementData.additionalActsSections.text || null
        };
    });

    return prayerSwornStatementDetailsList;
};
