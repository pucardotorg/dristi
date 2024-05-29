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
        type: "text",
        label: "FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "LAST_NAME",
        isMandatory: true,
        populators: {
          name: "lastName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
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
        component: "SelectCustomNote",
        key: "personalDetailsNote",
        withoutLabel: true,
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
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_PHONE_NUMBER",
              type: "text",
              name: "mobileNumber",
              isMandatory: true,
              error: "ERR_HRMS_INVALID_MOB_NO",
              componentInFront: "+91",
              validation: {
                required: true,
                minLength: 10,
                maxLength: 10,
                pattern: /^[6-9]\d{9}$/,
                isArray: true,
              },
              className: "mobile-number",
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
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_EMAILS",
              type: "text",
              name: "emailId",
              isMandatory: true,
              error: "ERR_HRMS_INVALID_MOB_NO",
              validation: {
                required: true,
                pattern: /\S+@\S+\.\S+/,
                isArray: true,
              },
              className: "email-address",
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
        isMandatory: true,
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
              isMandatory: true,
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
        component: "SelectComponentsMulti",
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
        withoutLabel: true,
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
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "CS_202_INQUIRY_AFFIDAVIT",
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
  className: "respondent",
};
