const advocateDetailsFormConfig = [
  {
    body: [
      {
        type: "radio",
        key: "isAdvocateRepresenting",
        label: "CS_IF_ADVOCATE_IS_COMPLAINANT",
        isMandatory: true,
        populators: {
          type: "radioButton",
          optionsKey: "name",
          error: "CORE_REQUIRED_FIELD_ERROR",
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
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    head: "CS_ADVOCATE_BASIC_DETAILS",
    body: [
      {
        type: "apidropdown",
        key: "advocateBarRegistrationNumber",
        label: "CS_BAR_REGISTRATION",
        isMandatory: true,
        populators: {
          allowMultiSelect: false,
          name: "advocateBarRegNumberWithName",
          isMandatory: true,
          validation: {},
          masterName: "commonUiConfig",
          moduleName: "getAdvocateNameUsingBarRegistrationNumber",
          customfn: "getNames",
          optionsKey: "barRegistrationNumber",
          optionsCustomStyle: {
            marginTop: "40px",
            justifyContent: "space-between",
            flexDirection: "row-reverse",
            maxHeight: "200px",
            overflowY: "scroll",
          },
        },
      },
    ],
  },
  {
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "AdvocateNameDetails",
        key: "AdvocateNameDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {},
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
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
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "vakalatnamaFileUpload",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "UPLOAD_VAKALATNAMA",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF"],
              isMultipleUpload: true,
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
  className: "advocate-detail",
  selectDocumentName: {
    vakalatnamaFileUpload: "UPLOAD_VAKALATNAMA",
  },
};
