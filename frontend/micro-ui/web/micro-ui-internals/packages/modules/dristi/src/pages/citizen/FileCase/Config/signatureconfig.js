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
              key: "complaintDetails",
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
              data: [{ name: "Sheetal Arora" }, { name: "Mehul Das" }],
            },
          ],
        },
      },
    ],
  },
];
export const signatureconfig = {
  formconfig: signatureFormConfig,
  header: "CS_ADD_SIGNATURE_HEADING",
  subtext: "CS_ADD_SIGNATURE_SUBTEXT",
};
