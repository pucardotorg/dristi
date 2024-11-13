const respondentFromconfig = [
  {
    head: "CS_RESPONDENT_TYPE",
    body: [
      {
        type: "radio",
        key: "respondentType",
        withoutLabel: true,
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          mdmsConfig: {
            masterName: "ComplainantRespondentType",
            moduleName: "case",
            select: "(data) => {return data['case'].ComplainantRespondentType?.map((item) => {return item;});}",
          },
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_NAME",
    updateLabelOn: "respondentType.showCompanyDetails",
    updateLabel: {
      key: "head",
      value: "CS_COMMON_ENTITY_DETAIL",
    },
    defaultLabel: {
      key: "head",
      value: "CS_RESPONDENT_NAME",
    },
    dependentKey: { respondentType: ["commonFields"] },
    body: [
      {
        type: "text",
        label: "FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "respondentFirstName",
          error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            minLength: 1,
            // maxLength: 100,
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        labelChildren: "optional",
        isMandatory: false,
        populators: {
          name: "respondentMiddleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "LAST_NAME",
        labelChildren: "optional",
        isMandatory: false,
        populators: {
          name: "respondentLastName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
            },
            // maxLength: 100,
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
    updateLabelOn: "respondentType.showCompanyDetails",
    updateLabel: {
      key: "head",
      value: "CS_REPRESENTATIVE_PHONE",
    },
    defaultLabel: {
      key: "head",
      value: "CS_RESPONDENT_PHONE",
    },
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
                pattern: /^[6-9]\d{9}$/,
                isArray: true,
                minLength: 10,
                maxLength: 10,
                isNumber: true,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { phonenumbers: ["mobileNumber"] },
    head: "WHATSAPP_SEND_CONFIRMATION",
    body: [
      {
        type: "radio",
        key: "whatsAppConfirmation",
        withoutLabel: true,
        isMandatory: true,
        populators: {
          type: "radioButton",
          name: "whatsAppConfirmation",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          options: [
            {
              code: "YES",
              name: "Yes",
            },
            {
              code: "NO",
              name: "No",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { respondentType: ["commonFields"] },
    head: "CS_RESPONDENT_EMAIL",
    updateLabelOn: "respondentType.showCompanyDetails",
    updateLabel: {
      key: "head",
      value: "CS_REPRESENTATIVE_EMAIL",
    },
    defaultLabel: {
      key: "head",
      value: "CS_RESPONDENT_EMAIL",
    },
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
                maxLength: 150,
                pattern: {
                  patternType: "email",
                  masterName: "commonUiConfig",
                  moduleName: "patternValidation",
                },
                isArray: true,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { emails: ["emailId"] },
    head: "SEND_EMAIL_CONFIRMATION",
    body: [
      {
        type: "radio",
        key: "emailConfirmation",
        withoutLabel: true,
        isMandatory: true,
        populators: {
          name: "emailConfirmation",
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
          required: false,
          isMandatory: true,
          isDependent: true,
          options: [
            {
              code: "YES",
              name: "Yes",
            },
            {
              code: "NO",
              name: "No",
            },
          ],
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
        key: "companyName",
        label: "company_Name",
        isMandatory: true,
        populators: {
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "companyName",
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
              isMandatory: false,
              name: "document",
              documentHeader: "COMPANY_DOCUMENT_DETAILS",
              isOptional: "CS_IS_OPTIONAL",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF", "PNG"],
              isMultipleUpload: true,
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
        component: "SelectComponentsMulti",
        key: "addressDetails",
        withoutLabel: true,
        error: "CORE_REQUIRED_FIELD_ERROR",
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
            {
              label: "ADDRESS",
              type: "text",
              name: "locality",
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
        key: "inquiryAffidavitFileUpload",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "AFFIDAVIT_UNDER_SECTION_225_BNSS",
              isOptional: "CS_IS_OPTIONAL",
              infoTooltipMessage: "AFFIDAVIT_UNDER_SECTION_225_BNSS_TOOLTIP_MSG",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF", "PNG"],
              isMultipleUpload: true,
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
  formItemName: "CS_RESPONDENT",
  className: "respondent",
};
