export const newConfig = [
  {
    head: "CS_COMMON_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SearchLocationAddress",
        key: "SearchLocationAddress",
        withoutLabel: true,
        populators: {
          inputs: [{ label: "CS_PIN_LOCATION", type: "LocationSearch", name: "correspondenceCity" }],
          validation: {},
        },
      },
      // {
      //   label: "PINCODE",
      //   type: "text",
      //   name: "pincode",
      //   validation: {
      //     minlength: 6,
      //     maxlength: 7,
      //     pattern: "[0-9]+",
      //     max: "9999999",
      //     title: t("ADDRESS_PINCODE_INVALID"),
      //   },
      // },
    ],
  },
];
