import { advocateDetailsConfig } from "./advocateDetailsConfig";
import { chequeDetailsConfig } from "./chequedetailsConfig";
import { complaintdetailconfig } from "./complaindetailsConfig";
import { debtliabilityconfig } from "./debtLiabilityConfig";
import { delayApplicationConfig } from "./delayApplicationConfig";
import { demandNoticeConfig } from "./demandNoticeConfig";
import { prayerAndSwornConfig } from "./prayerAndSwornConfig";
import { respondentconfig } from "./respondentConfig";
import { reviewcasefileconfig } from "./reviewcasefileconfig";
import { signatureconfig } from "./signatureconfig";
import { witnessConfig } from "./witnessConfig";

export const sideMenuConfig = [
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_LITIGENT_DETAILS",
    children: [
      {
        key: "complaintDetails",
        label: "CS_COMPLAINT_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: complaintdetailconfig,
        mandatoryFields: [
          "complainantType",
          "complainantId.complainantId",
          "firstName",
          "lastName",
          "complainantVerification.otpNumber", // checkThis- make sure to unset otpNumber if otp model is closed or canceled.
        ],
        initialMandatoryFieldCount: 10,
        dependentMandatoryFields: [
          { field: "addressCompanyDetails-select.pincode", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "addressCompanyDetails-select.state", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "addressCompanyDetails-select.district", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "addressCompanyDetails-select.city", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "addressCompanyDetails-select.locality", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "addressDetails-select.pincode", dependentOn: "complainantType", dependentOnKey: "isIndividual" },
          { field: "addressDetails-select.state", dependentOn: "complainantType", dependentOnKey: "isIndividual" },
          { field: "addressDetails-select.district", dependentOn: "complainantType", dependentOnKey: "isIndividual" },
          { field: "addressDetails-select.city", dependentOn: "complainantType", dependentOnKey: "isIndividual" },
          { field: "addressDetails-select.locality", dependentOn: "complainantType", dependentOnKey: "isIndividual" },
          { field: "companyName", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
          { field: "companyDetailsUpload.document", dependentOn: "complainantType", dependentOnKey: "showCompanyDetails" },
        ],
        optionalFields: ["middleName"],
        initialOptionalFieldCount: 1,
        dependentOptionalFields: [],
      },
      {
        key: "respondentDetails",
        label: "CS_RESPONDENT_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: respondentconfig,
        mandatoryFields: ["respondentType", "respondentFirstName", "respondentLastName"],
        ifMultipleAddressLocations: {
          // using this for counting mandatory fields in case of multiple locations .
          dataKey: "addressDetails",
          mandatoryFields: [
            "addressDetails.pincode",
            "addressDetails.state",
            "addressDetails.district",
            "addressDetails.city",
            "addressDetails.locality",
          ],
        },
        initialMandatoryFieldCount: 8,
        dependentMandatoryFields: [
          { field: "companyName", dependentOn: "respondentType", dependentOnKey: "showCompanyDetails" },
          { field: "companyDetailsUpload.document", dependentOn: "respondentType", dependentOnKey: "showCompanyDetails" },
        ],
        optionalFields: ["middleName", "phonenumbers.mobileNumber", "emails.emailId", "inquiryAffidavitFileUpload.document"],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 4,
      },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_CASE_SPECIFIC_DETAILS",
    children: [
      {
        key: "chequeDetails",
        label: "CS_CHEQUE_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: chequeDetailsConfig,
        mandatoryFields: [
          "chequeSignatoryName",
          "bouncedChequeFileUpload.document",
          "name",
          "chequeNumber",
          "issuanceDate",
          "bankName",
          "ifsc",
          "chequeAmount",
          "depositDate",
          "depositChequeFileUpload.document",
          "returnMemoFileUpload.document",
        ],
        dependentMandatoryFields: [],
        initialMandatoryFieldCount: 11,
        optionalFields: ["chequeAdditionalDetails"],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 1,
      },
      {
        key: "debtLiabilityDetails",
        label: "CS_DEBT_LIABILITY_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: debtliabilityconfig,
        mandatoryFields: ["liabilityNature", "liabilityType"],
        initialMandatoryFieldCount: 2,
        dependentMandatoryFields: [{ field: "totalAmount", dependentOn: "liabilityType", dependentOnKey: "showAmountCovered" }],
        optionalFields: ["debtLiabilityFileUpload.document", "additionalDebtLiabilityDetails.text"],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 2,
      },
      {
        key: "demandNoticeDetails",
        label: "CS_DEMAND_NOTICE_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: demandNoticeConfig,
        mandatoryFields: [
          "SelectUserTypeComponent",
          "dateOfIssuance",
          "dateOfDispatch",
          "SelectCustomDragDrop.legalDemandNoticeFileUpload",
          "SelectCustomDragDrop.proofOfDispatchFileUpload",
          "proofOfService",
          "proofOfReply",
          "dateOfAccrual",
          "delayApplicationType",
        ],
        initialMandatoryFieldCount: 9,
        dependentMandatoryFields: [
          { field: "dateOfService", dependentOn: "proofOfService", dependentOnKey: "showProofOfAcknowledgment" },
          {
            field: "SelectCustomDragDrop.proofOfAcknowledgmentFileUpload",
            dependentOn: "proofOfService",
            dependentOnKey: "showProofOfAcknowledgment",
          },
        ],
        optionalFields: [],
        dependentOptionalFields: [
          { field: "dateOfReply", dependentOn: "proofOfReply", dependentOnKey: "showProofOfReply" },
          {
            field: "SelectCustomDragDrop.proofOfReplyFileUpload",
            dependentOn: "proofOfReply",
            dependentOnKey: "showProofOfReply",
          },
        ],
        initialOptionalFieldCount: 0,
      },
      {
        key: "delayApplications",
        label: "CS_DELAY_APPLICATIONS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: delayApplicationConfig,
        mandatoryFields: ["delayApplicationType"],
        initialMandatoryFieldCount: 1,
        dependentMandatoryFields: [
          { field: "delayApplicationReason.reasonForDelay", dependentOn: "delayApplicationType", dependentOnKey: "showForm" },
          {
            field: "condonationFileUpload.document",
            dependentOn: "delayApplicationType",
            dependentOnKey: "showForm",
          },
        ],
        optionalFields: [],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 0,
      },
    ],
    checked: false,
    isCompleted: 0,
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_ADDITIONAL_DETAILS",
    children: [
      {
        key: "witnessDetails",
        label: "CS_WITNESS_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: witnessConfig,
        mandatoryFields: [
          "firstName", // whole witness details form is optional.
          "lastName",
        ],
        ifMultipleAddressLocations: {
          // using this for counting mandatory fields in case of multiple locations .
          dataKey: "addressDetails",
          mandatoryFields: [
            "addressDetails.pincode",
            "addressDetails.state",
            "addressDetails.district",
            "addressDetails.city",
            "addressDetails.locality",
          ],
        },
        initialMandatoryFieldCount: 0,
        dependentMandatoryFields: [],
        optionalFields: ["middleName", "phonenumbers.mobileNumber", "emails.emailId", "witnessAdditionalDetails.text"],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 4,
      },
      {
        key: "prayerSwornStatement",
        label: "CS_PRAYER_SWORN_STATEMENT",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: prayerAndSwornConfig,
        mandatoryFields: ["prayerAndSwornStatementType"],
        anyOneOfTheseMandatoryFields: [
          ["memorandumOfComplaint.text", "memorandumOfComplaint.document"],
          ["prayerForRelief.text", "prayerForRelief.document"],
        ],
        initialMandatoryFieldCount: 3,
        dependentMandatoryFields: [],
        optionalFields: [
          "caseSettlementCondition",
          "SelectCustomDragDrop.swornStatement",
          "additionalDetails.text",
          "additionalActsSections.text",
          "SelectUploadDocWithName.docName",
          "SelectUploadDocWithName.document",
        ],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 6,
      },
      {
        key: "advocateDetails",
        label: "CS_ADVOCATE_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: advocateDetailsConfig,
        mandatoryFields: [
          "isAdvocateRepresenting",
          // if advocateBarRegistrationNumber is present, 3 name fields will be filled automatically.
        ],
        initialMandatoryFieldCount: 1,
        dependentMandatoryFields: [
          { field: "barRegistrationNumber", dependentOn: "isAdvocateRepresenting", dependentOnKey: "showForm" },
          { field: "advocateName", dependentOn: "isAdvocateRepresenting", dependentOnKey: "showForm" },
          {
            field: "vakalatnamaFileUpload.document",
            dependentOn: "isAdvocateRepresenting",
            dependentOnKey: "showForm",
          },
        ],
        optionalFields: [],
        dependentOptionalFields: [],
        initialOptionalFieldCount: 0,
      },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_REVIEW_SIGN",
    children: [
      {
        key: "reviewCaseFile",
        label: "CS_REVIEW_CASE_FILE",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: reviewcasefileconfig,
      },
      { key: "addSignature", label: "CS_ADD_SIGNATURE", checked: false, isCompleted: false, isDisabled: false, pageConfig: signatureconfig },
    ],
  },
];
