export const loginSteps = [
  {
    texts: {
      header: "CS_SIGNIN_PROVIDE_MOBILE_NUMBER",
      cardText: "CS_WELCOME",
      nextText: "CS_SIGNIN_NEXT",
      submitBarLabel: "CS_SIGNIN_NEXT",
      submitInForm: true,
    },
    inputs: [
      {
        label: "CORE_COMMON_PHONE_NUMBER",
        type: "text",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        validation: {
          required: true,
          minlength: 10,
          maxlength: 10,
        },
      },
    ],
  },
  {
    texts: {
      header: "CS_REGISTER_PROVIDE_USER_NAME",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
    inputs: [
      {
        label: "First Name",
        type: "text",
        name: "name",
        error: "ERR_HRMS_INVALID_USER_NAME",
        validation: {
          required: true,
          minlength: 1,
          pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
          patternType: "Name",
        },
      },
      {
        label: "Middle Name (optional)",
        type: "text",
        name: "name",
        error: "ERR_HRMS_INVALID_USER_NAME",
        validation: {
          required: false,
          minlength: 1,
          pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
          patternType: "Name",
        },
      },
      {
        label: "Last Name",
        type: "text",
        name: "name",
        error: "ERR_HRMS_INVALID_USER_NAME",
        validation: {
          required: true,
          minlength: 2,
          pattern: /^(?!\s{0,50}$)[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{2,50}$/i,
          patternType: "Name",
        },
      },
    ],
  },
  {
    texts: {
      header: "CS_LOGIN_OTP",
      cardText: "CS_LOGIN_OTP_TEXT",
    },
  },
  {
    head: "Id-Verification",

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
              hasBreakPoint: true,
            },
            {
              label: "ENTER_AADHAR_NUMBER",
              type: "text",
              name: "aadharNumber",
              validation: {
                minlength: 12,
                maxlength: 12,
                patternType: "AadharNo",
                pattern: "[0-9]+",
                errMsg: "AADHAR_NUMBER_INVALID",
                title: "",
              },
              clearFields: { ID_Proof: [], selectIdTypeType: "" },
              clearFieldsType: { ID_Proof: "documentUpload" },
              disableMandatoryFieldFor: ["ID_Proof", "selectIdTypeType"],
              isMandatory: true,
              checkAadharVerification: true,
            },
            {
              type: "infoBox",
              name: "infoBox",
              bannerLabel: "INFO_BANNER_LABEL",
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
    head: "Terms and Conditions",
    body: [
      {
        type: "checkbox",
        key: "terms_Conditions",
        populators: { title: "I agree to Lorem ipsum dolor sit amet, consectetur adipiscing elit", name: "terms_Conditions" },
      },
    ],
  },
];
