const signatureFormConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectSignature",
        key: "advocatesignature",
        label: "1. Advocate Signature",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              key: "advocateDetails",
              icon: "AdvocateIcon",
              config: { title: "name" },
              data: [{ name: "Soumya Dhasmana" }],
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
              key: "complaintDetails",
              icon: "LitigentIcon",
              config: { title: "name" },
              data: [{ name: "Sheetal Arora" }],
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
