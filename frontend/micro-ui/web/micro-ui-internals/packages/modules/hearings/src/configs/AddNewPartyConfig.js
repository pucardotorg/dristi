// AddNewPartyConfig.js
const createPartyConfig = (index) => ({
  head: `Party ${index}`,
  body: [
    {
      label: "PARTY_TYPE",
      type: "dropdown",
      isMandatory: true,
      key: `partyType${index}`,
      populators: {
        name: `partyType${index}`,
        defaultValue: "Witness",
        optionsKey: "name",
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        options: [{ code: "Witness", name: "Witness" }],
      },
    },
    {
      label: "PARTY_NAME",
      key: `partyName${index}`,
      type: "text",
      isMandatory: true,
      populators: {
        name: `partyName${index}`,
        error: "CS_ALPHABETS_ALLOWED",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "PARTY_PHONE_NUMBER",
      type: "number",
      key: `phoneNumber${index}`,
      isMandatory: true,
      populators: {
        name: `phoneNumber${index}`,
        error: "INVALID_PHONE_NUMBER",
        validation: {
          minlength: 10,
          maxlength: 10,
          patternType: "Pincode",
          pattern: "[0-9]+",
          max: "9999999",
        },
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "PARTY_EMAIL_ID",
      key: `emailId${index}`,
      type: "text",
      isMandatory: true,
      populators: {
        name: `emailId${index}`,
        error: "CS_ENTER_VALID_EMAIL",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "PARTY_ADDRESS",
      key: `address${index}`,
      type: "text",
      isMandatory: true,
      populators: {
        name: `address${index}`,
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      key: `additionalDetails${index}`,
      type: "component",
      inline: true,
      isMandatory: false,
      component: "SelectCustomTextArea",
      populators: {
        inputs: [
          {
            textAreaSubHeader: "PARTY_ADDITIONAL_DETAILS",
            placeholder: "Description",
            type: "TextAreaComponent",
            isOptional: true,
            name: "text",
          },
        ],

        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
  ],
});

export default createPartyConfig;
