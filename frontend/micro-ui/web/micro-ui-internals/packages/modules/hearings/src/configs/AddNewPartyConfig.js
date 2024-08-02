// AddNewPartyConfig.js
const createPartyConfig = (index) => ({
  head: `Party ${index}`,
  body: [
    {
      label: "Party Type",
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
      label: "Party Name",
      key: `partyName${index}`,
      type: "text",
      isMandatory: true,
      populators: {
        name: `partyName${index}`,
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "Phone Number",
      type: "text",
      key: `phoneNumber${index}`,
      isMandatory: true,
      populators: {
        name: `phoneNumber${index}`,
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "Email ID",
      key: `emailId${index}`,
      type: "text",
      isMandatory: true,
      populators: {
        name: `emailId${index}`,
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
    {
      label: "Address",
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
      label: "Additional details",
      key: `additionalDetails${index}`,
      type: "textarea",
      populators: {
        name: `additionalDetails${index}`,
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
      },
    },
  ],
});

export default createPartyConfig;
