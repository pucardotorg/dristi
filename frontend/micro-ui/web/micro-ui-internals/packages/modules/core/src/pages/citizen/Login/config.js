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
    body: [
      { label: "CS_ID_TYPE", type: "dropdown", key: "ID", isMandatory: true, populators: { name: "ID", options: ["Aadhar", "PAN"] } },
      { label: "Upload ID Proof", type: "documentUpload", key: "ID_Proof", populators: { name: "ID_Proof" } },
      { label: "Aadhar ID", type: "text", key: "Aadhar_ID", isMandatory: true, populators: { name: "Aadhar_ID" } },
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
