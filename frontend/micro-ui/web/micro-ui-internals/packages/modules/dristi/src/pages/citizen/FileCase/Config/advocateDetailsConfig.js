const advocateDetailsFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "BoxComplainant",
        key: "boxComplainant",
        withoutLabel: true,
        isMandatory: true,
        populators: {},
      },
    ],
  },
  {
    body: [
      {
        type: "radio",
        key: "isComplainantPip",
        label: "CS_IF_COMPLAINANT_IS_PIP",
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
              showAddAdvocates: false,
              showAffidavit: true,
              isEnabled: true,
            },
            {
              code: "NO",
              name: "No",
              showAddAdvocates: true,
              showAffidavit: false,
              isEnabled: true,
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { isComplainantPip: ["showAddAdvocates"] },
    body: [
      {
        type: "component",
        component: "MultipleAdvocateNameDetails",
        key: "MultipleAdvocateNameDetails",
        withoutLabel: true,
        label: "ADVOCATE_BASIC_DETAILS",
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              name: "firstName",
            },
            {
              label: "MIDDLE_NAME_OPTIONAL",
              name: "middleName",
            },
            {
              label: "LAST_NAME",
              name: "lastName",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { boxComplainant: ["showVakalatNamaUpload"] },

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
              infoTooltipMessage: "UPLOAD_VAKALATNAMA",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF", "PNG"],
              isMultipleUpload: true,
              downloadTemplateText: "VAKALATNAMA_TEMPLATE_TEXT",
              downloadTemplateLink: "https://www.jsscacs.edu.in/sites/default/files/Department%20Files/Number%20System%20.pdf",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { isComplainantPip: ["showAffidavit"] },
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "pipAffidavitNote",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              infoHeader: "CS_PLEASE_NOTE",
              infoText: "AFFIDAVIT_NECESSARY_FOR_PIP",
              infoTooltipMessage: "ADVOCATE_DETAIL_NOTE",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    dependentKey: { isComplainantPip: ["showAffidavit"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "pipAffidavitFileUpload",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "UPLOAD_AFFIDAVIT",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF", "PNG"],
              isMultipleUpload: true,
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
    pipAffidavitFileUpload: "UPLOAD_PIP_AFFIDAVIT",
  },
};
