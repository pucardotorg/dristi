export const respondentconfig = [
  {
    head: "CS_RESPONDENT_NAME",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "userDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                title: "",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
              },
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                title: "",
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
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "personalDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_PERSONAL_DETAILS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_PERSONAL_DETAILS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_PHONE",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "phonenumbers",
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_PHONE_NUMBER",
              type: "text",
              name: "mobileNumber",
              error: "ERR_HRMS_INVALID_MOB_NO",
              componentInFront: "+91",
              validation: {
                required: true,
                minLength: 10,
                maxLength: 10,
                pattern: /^[6-9]\d{9}$/,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_EMAIL",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "emails",
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_EMAILS",
              type: "text",
              name: "mobileNumber",
              error: "ERR_HRMS_INVALID_MOB_NO",
              validation: {
                required: true,
                pattern: /\S+@\S+\.\S+/,
              },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: "CS_RESPONDENT_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressDetails",
        withoutLabel: true,
        error: "sample required message",
        required: false,
        isMandatory: true,
        populators: {
          inputs: [
            { label: "CS_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              validation: {
                minlength: 6,
                maxlength: 7,
                patternType: "Pincode",
                pattern: "[0-9]+",
                max: "9999999",
                errMsg: "ADDRESS_PINCODE_INVALID",
                isRequired: true,
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "STATE",
              type: "text",
              name: "state",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "city",
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
    body: [
      {
        type: "component",
        component: "SelectCustomNote",
        key: "addressDetailsNote",
        populators: {
          inputs: [
            {
              infoHeader: "CS_COMMON_NOTE",
              infoText: "CS_NOTETEXT_RESPONDENT_ADDRESS",
              infoTooltipMessage: "CS_NOTETOOLTIP_RESPONDENT_ADDRESS",
              type: "InfoComponent",
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "condonationFileUpload",
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "Aadhar",
              isOptional: "optional",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "Upload .png",
              maxFileSize: 1024 * 1024 * 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_1_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
  // {
  //   body: [
  //     {
  //       type: "component",
  //       component: "SelectUserTypeComponent",
  //       key: "clientDetails",
  //       withoutLabel: true,
  //       populators: {
  //         inputs: [
  //           {
  //             label: "SELECT_USER_TYPE",
  //             type: "radioButton",
  //             name: "selectUserType",
  //             optionsKey: "name",
  //             error: "sample required message",
  //             required: false,
  //             isMandatory: true,
  //             clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
  //             options: [
  //               {
  //                 code: "LITIGANT",
  //                 name: "LITIGANT",
  //                 showBarDetails: false,
  //                 isVerified: false,
  //               },
  //               {
  //                 code: "ADVOCATE",
  //                 name: "ADVOCATE",
  //                 showBarDetails: true,
  //                 isVerified: true,
  //                 hasBarRegistrationNo: true,
  //                 apiDetails: {
  //                   serviceName: "/advocate/advocate/v1/_create",
  //                   requestKey: "advocates",
  //                   AdditionalFields: ["barRegistrationNumber"],
  //                 },
  //               },
  //               {
  //                 code: "ADVOCATE_CLERK",
  //                 name: "ADVOCATE CLERK",
  //                 showBarDetails: true,
  //                 hasStateRegistrationNo: true,
  //                 isVerified: true,
  //                 apiDetails: {
  //                   serviceName: "/advocate/clerk/v1/_create",
  //                   requestKey: "clerks",
  //                   AdditionalFields: ["stateRegnNumber"],
  //                 },
  //               },
  //             ],
  //           },
  //         ],
  //       },
  //     },
  //   ],
  // },
];

export const termsAndConditionConfig = [
  {
    body: [
      {
        type: "checkbox",
        key: "Terms_Conditions",
        populators: {
          title: "FIRST_TERMS_AND_CONDITIONS",
          name: "Terms_Conditions",
          styles: { minWidth: "100%" },
          labelStyles: { padding: "8px" },
          customStyle: { minWidth: "100%" },
        },
      },
    ],
  },
];
