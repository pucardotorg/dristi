const advocateDetailsFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "isAdvocateRepresenting",
        label: "CS_DELAY_APPLICATION_TYPE",
        isMandatory: true,
        populators: {
          label: "CS_ADVOCATE_REPRESENTING",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          isDependent: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "YES",
              name: "Yes",
              showForm: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "No",
              showForm: false,
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
    head: "CS_ADVOCATE_BASIC_DETAIL",
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "dropdown",
        key: "advocatedetails",
        label: "CS_BAR_REGISTRATION",
        isMandatory: true,
        populators: {
          label: "SELECT_RESPONDENT_TYPE",
          type: "radioButton",
          optionsKey: "name",
          error: "sample required message",
          required: false,
          isMandatory: true,
          clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
          options: [
            {
              code: "BH-1231-FD",
              name: "BH-1231-FD",
              isEnabled: true,
            },
            {
              code: "BH-1231-FD",
              name: "Sales Agreement",
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "UPLOAD_VAKALATNAMA",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
              downloadTemplateText: "VAKALATNAMA_TEMPLATE_TEXT",
              downloadTemplateLink: "https://www.jsscacs.edu.in/sites/default/files/Department%20Files/Number%20System%20.pdf",
            },
          ],
        },
      },
    ],
  },
];

export const advocateDetailsConfig = {
  formconfig: advocateDetailsFormConfig,
  header: "CS_ADVOCATE_DETAILS_HEADING",
  subtext: "CS_ADVOCATE_DETAILS_SUBTEXT",
  className: "advocate-detail"
};
