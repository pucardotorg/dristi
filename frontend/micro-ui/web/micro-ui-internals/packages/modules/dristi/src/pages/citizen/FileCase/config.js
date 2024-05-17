export const advocateClerkConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "clientDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "BAR_REGISTRATION_NUMBER",
              type: "text",
              name: "barRegistrationNumber",
              validation: {
                isRequired: true,
                pattern: /^[a-zA-Z0-9/]*$/i,
                errMsg: "BAR_REGISTRATION_NUMBER_INVALID",
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { stateRegnNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasBarRegistrationNo"] },
            },
            {
              label: "STATE_REGISTRATION_NUMBER",
              type: "text",
              name: "stateRegnNumber",
              validation: {
                isRequired: true,
                pattern: /^[a-zA-Z0-9/]*$/i,
                errMsg: "STATE_REGISTRATION_NUMBER_INVALID",
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { barRegistrationNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasStateRegistrationNo"] },
            },
            {
              label: "BAR_COUNCIL_ID",
              type: "documentUpload",
              name: "barCouncilId",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              dependentKey: { selectUserType: ["showBarDetails"] },
            },
          ],
        },
      },
    ],
  },
];

export const complainantDetailsConfig = [
  {
    body: [
      {
        type: "radio",
        key: "respondentType",
        label: "CS_RESPONDENT_TYPE",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "INDIVIDUAL",
              name: "Individual",
              showCompanyDetails: false,
              commonFields: true,
              isEnabled: true,
            },
            {
              code: "REPRESENTATIVE",
              name: "Representative of an Entity",
              showCompanyDetails: true,
              commonFields: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
          customStyle: {
            display: "flex",
            gap: 50,
          },
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "VerificationComponent",
        key: "complainantId",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "COMPLAINANT_ID",
              name: "complainantId",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_COMMON_COMPLAINANT_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "userDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              inputFieldClassName: "user-details-form-style",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              inputFieldClassName: "user-details-form-style",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "VerifyPhoneNumber",
        key: "complainantId",
        withoutLabel: true,
        label: "PHONE_NUMBER",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        componentInFront: "+91",
        validation: {
          required: true,
          minLength: 10,
          maxLength: 10,
          pattern: /^[6-9]\d{9}$/,
        },
        populators: {},
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_COMMON_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            { label: "CS_PIN_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              inputFieldClassName: "user-details-form-style",
              validation: {
                minlength: 6,
                maxlength: 7,
                patternType: "Pincode",
                pattern: "[0-9]+",
                max: "9999999",
                errMsg: "ADDRESS_PINCODE_INVALID",
                isRequired: true,
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "STATE",
              type: "text",
              name: "state",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "city",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "LOCALITY",
              type: "text",
              name: "locality",
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DOOR_NUMBER",
              type: "text",
              name: "doorNo",
              inputFieldClassName: "user-details-form-style",
              validation: {
                errMsg: "ADDRESS_DOOR_NO_INVALID",
                pattern: /^[^\$\"'<>?~`!@$%^={}\[\]*:;“”‘’]{2,50}$/i,
                isRequired: true,
                minlength: 2,
                title: "",
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
];

const respondentFromconfig = [
  {
    body: [
      {
        type: "radio",
        key: "respondentType",
        label: "CS_RESPONDENT_TYPE",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "INDIVIDUAL",
              name: "Individual",
              showCompanyDetails: false,
              commonFields: true,
              isEnabled: true,
            },
            {
              code: "REPRESENTATIVE",
              name: "Representative of an Entity",
              showCompanyDetails: true,
              commonFields: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_NAME",
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "userDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "personalDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_PERSONAL_DETAILS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_RESPONDENT_PHONE",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "phonenumbers",
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_PHONE_NUMBER",
              type: "text",
              name: "mobileNumber",
              error: "ERR_HRMS_INVALID_MOB_NO",
              componentInFront: "+91",
              validation: {
                required: true,
                minLength: 10,
                maxLength: 10,
                pattern: /^[6-9]\d{9}$/,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_RESPONDENT_EMAIL",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "emails",
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_EMAILS",
              type: "text",
              name: "mobileNumber",
              error: "ERR_HRMS_INVALID_MOB_NO",
              validation: {
                required: true,
                pattern: /\S+@\S+\.\S+/,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_COMPANY_DETAIL",
    dependentKey: { respondentType: ["showCompanyDetails"] },
    body: [
      {
        type: "text",
        key: "company_Name",
        label: "company_Name",
        populators: {
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "Terms_Conditions",
          styles: { minWidth: "100%" },
          labelStyles: { padding: "8px" },
          customStyle: { minWidth: "100%" },
        },
      },
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "companyDetailsUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "COMPANY_DOCUMENT_DETAILS",
              type: "DragDropComponent",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_RESPONDENT_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressDetails",
        withoutLabel: true,
        error: "sample required message",
        required: false,
        isMandatory: true,
        populators: {
          inputs: [
            { label: "CS_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              validation: {
                minlength: 6,
                maxlength: 7,
                patternType: "Pincode",
                pattern: "[0-9]+",
                max: "9999999",
                errMsg: "ADDRESS_PINCODE_INVALID",
                isRequired: true,
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "STATE",
              type: "text",
              name: "state",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "city",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_ADDRESS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_ADDRESS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Aadhar",
              isOptional: "optional",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
];

export const respondentconfig = {
  formconfig: respondentFromconfig,
  header: "CS_RESPONDENT_DETAIL_HEADING",
  subtext: "CS_RESPONDENT_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_RESPONDENT",
  formItemName: "Respondent",
};

const complaintdetailconfig = {
  formconfig: complainantDetailsConfig,
  header: "CS_COMPLAINT_DETAIL_HEADING",
  subtext: "CS_RESPONDENT_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_Complaint",
  formItemName: "Complainant",
};

const debtLiabilityFromconfig = [
  {
    body: [
      {
        type: "dropdown",
        key: "liabilityNature",
        label: "Nature of debt / liability for which cheque(s) was/were received (e.g. loan, sales agreement etc)",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "LOAN",
              name: "Loan",
              showAmountCovered: false,
              isEnabled: true,
            },
            {
              code: "SALES",
              name: "Sales Agreement",
              showAmountCovered: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "radio",
        key: "liabilityType",
        label: "What was the total amount the cheque(s) covered?",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "FULL_LIABILITY",
              name: "Full Liability",
              showAmountCovered: false,
              isEnabled: true,
            },
            {
              code: "PARTIAL_LIABILITY",
              name: "Partial Liability",
              showAmountCovered: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
              apiDetails: {
                serviceName: "/advocate/advocate/v1/_create",
                requestKey: "advocates",
                AdditionalFields: ["barRegistrationNumber"],
              },
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { liabilityType: ["showAmountCovered"] },
    body: [
      {
        type: "text",
        key: "totalAmount",
        label: "What was the total amount the cheque(s) covered? ",
        populators: {
          validation: {},
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "Terms_Conditions",
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_ADDRESS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_ADDRESS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Aadhar",
              isOptional: "optional",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "delayApplicationReason",
        populators: {
          inputs: [
            {
              textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
];
const debtliabilityconfig = {
  formconfig: debtLiabilityFromconfig,
  header: "CS_DEBT_LIABILITY_HEADING",
  subtext: "CS_DEBT_LIABILITY_SUBTEXT",
  isOptional: false,
};
const delayApplicationFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "delayApplicationType",
        label: "CS_DELAY_APPLICATION_TYPE",
        isMandatory: true,
        populators: {
          label: "CS_DELAY_APPLICATION_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "YES",
              name: "YES",
              showForm: false,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "NO",
              showForm: true,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomTextArea",
        key: "delayApplicationReason",
        populators: {
          inputs: [
            {
              textAreaHeader: "CS_TEXTAREA_HEADER_DELAY_REASON",
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_ADDRESS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_ADDRESS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { delayApplicationType: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Aadhar",
              isOptional: "optional",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 1024 * 1024 * 5,
              maxFileErrorMessage: "CS_FILE_LIMIT_1_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
            },
          ],
        },
      },
    ],
  },
];

const delayApplicationConfig = {
  formconfig: delayApplicationFormConfig,
  header: "CS_RESPONDENT_DELAY_APPLICATION_HEADING",
  subtext: "CS_RESPONDENT_DELAY_APPLICATION_SUBTEXT",
};

const advocateDetailsFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "isAdvocateRepresenting",
        label: "CS_DELAY_APPLICATION_TYPE",
        isMandatory: true,
        populators: {
          label: "CS_ADVOCATE_REPRESENTING",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "YES",
              name: "Yes",
              showForm: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "No",
              showForm: false,
              isVerified: true,
              hasBarRegistrationNo: true,
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    head: "CS_ADVOCATE_BASIC_DETAIL",
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "dropdown",
        key: "advocatedetails",
        label: "CS_BAR_REGISTRATION",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "BH-1231-FD",
              name: "BH-1231-FD",
              isEnabled: true,
            },
            {
              code: "BH-1231-FD",
              name: "Sales Agreement",
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "UPLOAD_VAKALATNAMA",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
              downloadTemplateText: "VAKALATNAMA_TEMPLATE_TEXT",
              downloadTemplateLink: "https://www.jsscacs.edu.in/sites/default/files/Department%20Files/Number%20System%20.pdf",
            },
          ],
        },
      },
    ],
  },
];

const advocateDetailsConfig = {
  formconfig: advocateDetailsFormConfig,
  header: "CS_ADVOCATE_DETAILS_HEADING",
  subtext: "CS_ADVOCATE_DETAILS_SUBTEXT",
};
export const sideMenuConfig = [
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_LITIGENT_DETAILS",
    children: [
      { label: "CS_COMPLAINT_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: complaintdetailconfig },
      { label: "CS_RESPONDENT_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: respondentconfig },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_CASE_SPECIFIC_DETAILS",
    children: [
      { label: "CS_CHECK_DETAILS", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_DEBT_LIABILITY_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: debtliabilityconfig },
      { label: "CS_DEMAND_NOTICE_DETAILS", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_DELAY_APPLICATIONS", checked: false, isCompleted: false, isDisabled: false, pageConfig: delayApplicationConfig },
    ],
    checked: false,
    isCompleted: 0,
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_ADDITIONAL_DETAILS",
    children: [
      { label: "CS_WITNESS_DETAILS", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_PRAYER_SWORN_STATEMENT", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_ADVOCATE_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: advocateDetailsConfig },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_REVIEW_SIGN",
    children: [
      { label: "CS_REVIEW_CASE_FILE", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_ADD_SIGNATURE", checked: false, isCompleted: false, isDisabled: false },
    ],
  },
];
