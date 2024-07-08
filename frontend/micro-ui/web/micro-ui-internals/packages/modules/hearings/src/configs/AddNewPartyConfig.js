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
          optionsKey:"name",
          options: [
            { code: "Witness", name: "Witness" },
          ],
        },
      },
      {
        label: "Party Name",
        key: `partyName${index}`,
        type: "text",
        isMandatory: true,
        populators: {
          name: `partyName${index}`,
        },
      },
      {
        label: "Phone Number",
        type: "text",
        key: `phoneNumber${index}`,
        isMandatory: true,
        populators: {
          name: `phoneNumber${index}`,
        },
      },
      {
        label: "Email ID",
        key: `emailId${index}`,
        type: "text",
        isMandatory: true,
        populators: {
          name: `emailId${index}`,
        },
      },
      {
        label: "Address",
        key: `address${index}`,
        type: "textarea",
        isMandatory: true,
        populators: {
          name: `address${index}`,
        },
      },
      {
        label: "Additional details",
        key: `additionalDetails${index}`,
        type: "textarea",
        populators: {
          name: `additionalDetails${index}`,
        },
      },
    ],
  });
  
  export default createPartyConfig;