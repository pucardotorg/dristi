export const loginSteps = [
  {
    texts: {
      header: "CS_LOGIN_PROVIDE_MOBILE_NUMBER",
      cardText: "CS_LOGIN_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
    inputs: [
      {
        label: "CORE_COMMON_MOBILE_NUMBER",
        type: "text",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        validation: {
          required: true,
          minLength: 10,
          maxLength: 10,
        },
      },
    ],
  },
  {
    texts: {
      header: "CS_LOGIN_OTP",
      cardText: "CS_LOGIN_OTP_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
  },
  {
    head: "Id-Verification",
    // body: [
    //   { label: "CS_ID_TYPE", type: "dropdown", key: "ID", isMandatory: true, populators: { name: "ID", options: ["Aadhar", "PAN"] } },
    //   { label: "Upload ID Proof", type: "documentUpload", key: "ID_Proof", populators: { name: "ID_Proof" }, localePrefix: "Enter_Aadhar" },
    //   {
    //     label: "Aadhar ID",
    //     type: "text",
    //     key: "Aadhar_ID",
    //     isMandatory: true,
    //     populators: { name: "Aadhar_ID" },
    //   },
    // ],
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
              name: "selectIdTypeType",
              optionsKey: "name",
              error: "sample required message",
              validation: {},
              clearFields: { aadharNumber: "" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              options: [
                {
                  code: "Aadhar",
                  name: "Aadhar",
                },
                {
                  code: "PAN",
                  name: "PAN",
                },
              ],
            },
            {
              label: "Upload ID Proof",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              clearFields: { aadharNumber: "" },
              allowedFileTypes: /(.*?)(pdf)$/i,
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
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
                title: "AADHAR_NUMBER_INVALID",
              },
              clearFields: { ID_Proof: [], selectIdTypeType: "" },
              disableMandatoryFieldFor: ["ID_Proof", "selectIdTypeType"],
              isMandatory: true,
              isDisabled: true,
            },
            {
              type: "infoBox",
              name: "aadharNumber",
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
      header: "CS_LOGIN_OTP",
      cardText: "CS_LOGIN_OTP_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
  },
  {
    head: "Terms and Conditions",
    body: [
      {
        type: "checkbox",
        key: "Terms_Conditions",
        populators: { title: "I agree to Lorem ipsum dolor sit amet, consectetur adipiscing elit", name: "Terms_Conditions" },
      },
    ],
  },
];
