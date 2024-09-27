export const registerRespondentConfig = {
  individualConfig: [
    // info text
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
                infoText: "These details have been entered about the complainant. Carefully go through all details before confirming.",
                infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
                type: "InfoComponent",
              },
            ],
          },
        },
      ],
    },
    // respondent type
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
            options: [
              {
                code: "INDIVIDUAL",
                name: "Individual",
              },
              {
                code: "REPRESENTATIVE",
                name: "Representative of an Entity",
              },
            ],
          },
        },
      ],
    },
    // name
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
              minLength: 2,
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
          isMandatory: true,
          labelChildren: "optional",
          populators: {
            name: "respondentLastName",
            error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
            validation: {
              pattern: {
                message: "CORE_COMMON_APPLICANT_NAME_INVALID",
                value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
              },
              minLength: 2,
              // maxLength: 100,
              title: "",
              patternType: "Name",
            },
          },
        },
      ],
    },
    // note for mobile number
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
                infoText:
                  "All case-related updates will be shared with this contact number. Kindly ensure that the enetered phone number is accurate.",
                infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
                type: "InfoComponent",
              },
            ],
          },
        },
      ],
    },
    // mobile number
    {
      body: [
        {
          type: "text",
          label: "Accused’s Phone Number",
          isMandatory: true,
          populators: {
            error: "CORE_REQUIRED_FIELD_ERROR",
            componentInFront: "+91",
            name: "mobileNumber",
            prefix: "",
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
          removeAddLocationButton: true,
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
  ],
  companyConfig: [
    // info text
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
                infoText: "These details have been entered about the complainant. Carefully go through all details before confirming.",
                infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
                type: "InfoComponent",
              },
            ],
          },
        },
      ],
    },
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
            clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
            options: [
              {
                code: "INDIVIDUAL",
                name: "Individual",
              },
              {
                code: "REPRESENTATIVE",
                name: "Representative of an Entity",
              },
            ],
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
              minLength: 2,
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
          isMandatory: true,
          populators: {
            name: "respondentLastName",
            error: "FIRST_LAST_NAME_MANDATORY_MESSAGE",
            validation: {
              pattern: {
                message: "CORE_COMMON_APPLICANT_NAME_INVALID",
                value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i,
              },
              minLength: 2,
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
      body: [
        {
          type: "text",
          label: "Accused’s Phone Number",
          isMandatory: true,
          populators: {
            error: "CORE_REQUIRED_FIELD_ERROR",
            componentInFront: "+91",
            name: "mobileNumber",
            prefix: "",
            validation: {
              required: true,
              minLength: 10,
              maxLength: 10,
              pattern: /^[6-9]\d{9}$/,
            },
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
                isMandatory: true,
                name: "document",
                documentHeader: "COMPANY_DOCUMENT_DETAILS",
                type: "DragDropComponent",
                uploadGuidelines: "UPLOAD_DOC_50",
                maxFileSize: 50,
                maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
                fileTypes: ["JPG", "PDF"],
                isMultipleUpload: false,
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
  ],
};

export const uploadIdConfig = [
  {
    head: "UPLOAD-ID",
    subHead: "UPLOAD_SUBTEXT",
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_ID_TYPE",
              type: "dropdown",
              name: "selectIdType",
              optionsKey: "type",
              error: "CORE_REQUIRED_FIELD_ERROR",
              validation: {},
              clearFields: { aadharNumber: "", ID_Proof: [] },
              clearFieldsType: { ID_Proof: "documentUpload" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              mdmsConfig: {
                masterName: "IdentifierType",
                moduleName: "User Registration",
                select: "(data) => {return data['User Registration'].IdentifierType?.map((item) => {return item;});}",
              },
              optionsCustomStyle: {
                top: "40px",
              },
            },
            {
              label: "Upload ID Proof",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              clearFields: { aadharNumber: "" },
              allowedFileTypes: /(.*?)(png|jpg|pdf)$/i,
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
            },
          ],
          validation: {},
        },
      },
    ],
    texts: {},
  },
];

export const uploadResponseDocumentConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "Upload document",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              clearFields: { aadharNumber: "" },
              allowedFileTypes: /(.*?)(png|jpg|pdf)$/i,
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
            },
          ],
          validation: {},
        },
      },
    ],
    texts: {},
  },
];
