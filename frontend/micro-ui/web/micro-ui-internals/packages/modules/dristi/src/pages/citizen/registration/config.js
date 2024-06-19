export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "REG_LITIGANT_TEXT",
    showBarDetails: false,
    isVerified: false,
    role: ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    subText: "REG_LITIGANT_SUB_TEXT",
  },
  {
    code: "ADVOCATE",
    name: "REG_ADVOCATE_TEXT",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    role: ["ADVOCATE_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/advocate/v1/_create",
      requestKey: "advocate",
      AdditionalFields: ["barRegistrationNumber"],
    },
    subText: "REG_ADVOCATE_SUB_TEXT",
  },
  {
    code: "ADVOCATE_CLERK",
    name: "REG_ADVOCATE_CLERK_TEXT",
    showBarDetails: true,
    hasStateRegistrationNo: true,
    isVerified: true,
    role: ["ADVOCATE_CLERK_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER", "DEPOSITION_CREATOR", "DEPOSITION_EDITOR", "DEPOSITION_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerk",
      AdditionalFields: ["stateRegnNumber"],
    },

    subText: "REG_ADVOCATE_CLERK_SUB_TEXT",
  },
];

export const newConfig = [
  {
    body: [
      {
        type: "text",
        label: "CORE_COMMON_FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_INVALID_NAME_ERROR",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
            minLength: 2,
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        populators: {
          name: "middleName",
          error: "ERR_HRMS_INVALID_MIDDLE_NAME",
          validation: {
            pattern: {
              message: "ERR_HRMS_INVALID_MIDDLE_NAME",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "CORE_COMMON_LAST_NAME",
        isMandatory: true,
        populators: {
          name: "lastName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "CORE_COMMON_INVALID_NAME_ERROR",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
            minLength: 2,
          },
        },
      },
    ],
  },
  {
    head: "CORE_COMMON_ENTER_ADDRESS",
    body: [
      {
        type: "component",
        component: "AddressComponent",
        key: "addressDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_LOCATION",
              type: "LocationSearch",
              name: ["pincode", "state", "district", "city", "coordinates", "locality", "buildingName", "doorNo"],
            },
            {
              label: "CORE_COMMON_PINCODE",
              type: "text",
              name: "pincode",
              validation: {
                minlength: 6,
                maxlength: 6,
                patternType: "Pincode",
                pattern: "[0-9]+",
                max: "9999999",
                errMsg: "CORE_COMMON_PINCODE_INVALID_ERROR",
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
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_STATE_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "CORE_COMMON_DISTRICT",
              type: "text",
              name: "district",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_DISTRICT_INVALID_ERROR",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "CORE_COMMON_CITY/TOWN",
              type: "text",
              name: "city",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CORE_COMMON_LOCALITY_INVALID_ERROR",
              type: "text",
              name: "locality",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CORE_COMMON_BUILDING_NAME",
              type: "text",
              name: "buildingName",
              validation: {
                errMsg: "CORE_COMMON_BUILDING_NAME_INVALID_ERROR",
                isRequired: true,
                minlength: 2,
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "CORE_COMMON_DOOR_NUMBER",
              type: "text",
              name: "doorNo",
              validation: {
                errMsg: "DOOR_NUMBER_ERROR_MESSAGE",
                pattern: /^[^\$\"'<>?~`!@$%^={}\[\]*:;“”‘’]{0,100}$/i,
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
  {
    body: [
      {
        type: "component",
        component: "CustomRadioCard",
        key: "clientDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "REG_SELECT_USER_TYPE_TEXT",
              subLabel: "REG_SELECT_USER_TYPE_SUB_TEXT",
              type: "radioButton",
              name: "selectUserType",
              optionsKey: "name",
              error: "CORE_REQUIRED_FIELD_ERROR",
              required: false,
              isMandatory: true,
              clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
              options: userTypeOptions,
              styles: { flexDirection: "column" },
            },
          ],
        },
      },
    ],
  },
  {
    head: "REG_ENTER_NAME",
    subHead: "REG_ENTER_NAME_SUB_TEXT",
    headId: "select-name-subtext", // for css
    body: [
      {
        type: "text",
        label: "CORE_COMMON_FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "CORE_COMMON_TEXT_MANDATORY_MESSAGE",
          validation: {
            pattern: {
              message: "CORE_COMMON_INVALID_NAME_ERROR",
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
        label: "CORE_COMMON_MIDDLE_NAME",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_INVALID_NAME_ERROR",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "CORE_COMMON_LAST_NAME",
        isMandatory: true,
        populators: {
          name: "lastName",
          error: "CORE_COMMON_TEXT_MANDATORY_MESSAGE",
          validation: {
            pattern: {
              message: "CORE_COMMON_INVALID_NAME_ERROR",
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
    texts: {
      // header: "CS_LOGIN_OTP",
      cardText: "REG_VERIFY_MOBILE_OTP_SUB_TEXT",
    },
  },
  {
    texts: {
      header: "REG_SIGNUP_PROVIDE_MOBILE_NUMBER",
      cardText: "REG_SIGNUP_ENTER_MOBILE_SUB_TEXT",
      submitBarLabel: "CORE_COMMON_CONTINUE",
      submitInForm: true,
    },
    inputs: [
      {
        label: "CORE_COMMON_MOBILE_NUMBER",
        type: "text",
        name: "mobileNumber",
        error: "CORE_COMMON_INVALID_MOB_NO",
        validation: {
          required: true,
          minlength: 10,
          maxlength: 10,
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "CustomRadioCard",
        key: "IdVerification",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "REG_VERFIY_IDENTITY",
              subLabel: "REG_VERFIY_IDENTITY_SUB_TEXT",
              type: "radioButton",
              name: "selectIdType",
              optionsKey: "name",
              error: "CORE_REQUIRED_FIELD_ERROR",
              validation: {},
              clearFields: { aadharNumber: "" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              options: [
                {
                  code: "AADHAR",
                  name: "REG_AADHAR_RECOMMENDATION",
                  subText: "REG_AADHAR_RECOMMENDATION_SUB_TEXT",
                },
                {
                  code: "OTHER ID",
                  name: "REG_OTHER",
                  subText: "REG_OTHER_SUB_TEXT",
                },
              ],
              optionsCustomStyle: {
                top: "40px",
              },
              styles: { flexDirection: "column" },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: "REG_ENTER_ADHAAR",
    subHead: "REG_ENTER_ADHAAR_SUB_TEXT",
    body: [
      {
        type: "component",
        component: "AdhaarInput",
        key: "AdhaarInput",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "REG_ENTER_AADHAR_NUMBER",
              type: "text",
              name: "aadharNumber",
              validation: {
                minlength: 12,
                maxlength: 12,
                patternType: "AadharNo",
                pattern: "[0-9]+",
                errMsg: "REG_AADHAR_NUMBER_INVALID",
                title: "",
              },
              clearFields: { ID_Proof: [], selectIdTypeType: "" },
              clearFieldsType: { ID_Proof: "documentUpload" },
              disableMandatoryFieldFor: ["ID_Proof", "selectIdTypeType"],
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    texts: {
      header: "CS_AADHAR_OTP",
      cardText: "CS_AADHAR_OTP_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
  },
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
              optionsKey: "name",
              error: "CORE_REQUIRED_FIELD_ERROR",
              validation: {},
              clearFields: { aadharNumber: "", ID_Proof: [] },
              clearFieldsType: { ID_Proof: "documentUpload" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              options: [
                {
                  code: "AADHAR",
                  name: "AADHAR",
                },
                {
                  code: "PAN",
                  name: "PAN",
                },
              ],
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
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
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
  },
];

export const termsAndConditionConfig = [
  {
    body: [
      {
        type: "checkbox",
        key: "Terms_Conditions",
        populators: {
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "Terms_Conditions",
          styles: { minWidth: "100%" },
          labelStyles: { padding: "8px" },
          customStyle: { minWidth: "100%" },
        },
      },
    ],
  },
];

export const advocateClerkConfig = [
  {
    head: "REG_ADVOCATE_VERFICATION",
    subHead: "REG_ADVOCATE_VERFICATION_SUB_TEXT",

    body: [
      {
        type: "component",
        component: "AdvocateDetailComponent",
        key: "clientDetails",
        // header: "Verify your identity",
        // withoutLabel: true,
        // subLabel: "Before diving in, we'll need to verify your identity for account setup",
        populators: {
          inputs: [
            {
              label: "REG_BAR_REGISTRATION_NUMBER",
              type: "text",
              name: "barRegistrationNumber",
              validation: {
                isRequired: true,
                pattern: /^[0-9A-Z/]{0,20}$/,
                errMsg: "REG_BAR_REGISTRATION_NUMBER_INVALID",
                maxlength: 20,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { stateRegnNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasBarRegistrationNo"] },
            },

            {
              label: "REG_BAR_COUNCIL_ID",
              type: "documentUpload",
              name: "barCouncilId",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
              isDependentOn: "selectUserType",
              dependentKey: { selectUserType: ["showBarDetails"] },
            },
          ],
        },
      },
    ],
  },
];
