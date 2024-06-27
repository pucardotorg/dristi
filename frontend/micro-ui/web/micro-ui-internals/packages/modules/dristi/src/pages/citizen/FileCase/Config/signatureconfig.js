const signatureFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectSignature",
        key: "advocatesignature",
        label: "1. Advocate Signature",
        withoutLabel: true,
        dependentOn: "advocateDetails",
        dependentKey: "advocateName",
        populators: {
          inputs: [
            {
              key: "advocateDetails",
              icon: "AdvocateIcon",
              config: { title: "name" },
              data: [],
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
        component: "SelectSignature",
        key: "litigentsignature",
        label: "1. Litigant Signature",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "complainantDetails",
              icon: "LitigentIcon",
              config: { title: "name" },
              data: [],
            },
          ],
        },
      },
    ],
  },
];

const confirmmodalconfig = {
  actionCancelLabel: "CS_COMMON_CANCEL",
  actionSaveLabel: "CS_CONTINUE_EDITING",
  headerBarMain: "CS_EDIT_FIELDS",
  modalText: "CS_EDIT_FIELDS_TEXT",
};

export const signatureconfig = {
  formconfig: signatureFormConfig,
  header: "CS_ADD_SIGNATURE",
  subtext: "CS_ADD_SIGNATURE_SUBTEXT",
  confirmmodalconfig,
  className: "signature",
};
