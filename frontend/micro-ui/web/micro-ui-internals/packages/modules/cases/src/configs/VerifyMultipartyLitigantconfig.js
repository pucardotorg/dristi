export const VerifyMultipartyLitigantConfig = [
  {
    body: [
      {
        inline: true,
        label: "FIRST_NAME",
        isMandatory: true,
        key: "firstName",
        type: "text",
        disable: false,
        populators: {
          name: "firstName",
          error: "Required",
          validation: { pattern: /^[A-Za-z]+$/i },
          customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" },
        },
      },
      {
        inline: true,
        label: "MIDDLE_NAME",
        isMandatory: false,
        key: "middleName",
        type: "text",
        disable: false,
        labelChildren: "optional",
        populators: { name: "middleName", customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" } },
      },
      {
        inline: true,
        label: "LAST_NAME",
        isMandatory: false,
        key: "lastName",
        type: "text",
        disable: false,
        labelChildren: "optional",
        populators: { name: "lastName", customStyle: { display: "flex", flexDirection: "column", alignItems: "flex-start" } },
      },
      {
        type: "component",
        component: "VerifyPhoneNumber",
        key: "phoneNumberVerification",
        withoutLabel: true,
        label: "Mobile No",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        componentInFront: "+91",
        disableConfigFields: ["firstName", "middleName", "lastName"],
        requiredFields: ["firstName"],
        confirmModal: true,
        disableConfigKey: "individualDetails",
        isMandatory: true,
        validation: {
          required: true,
          minLength: 10,
          maxLength: 10,
          pattern: /^[6-9]\d{9}$/,
        },
        populators: {},
      },
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "vakalatnama",
        isMandatory: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Vakalatnama",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PDF", "PNG", "JPEG"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
];
