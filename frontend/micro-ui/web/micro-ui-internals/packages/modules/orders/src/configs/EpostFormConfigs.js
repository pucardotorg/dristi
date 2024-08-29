export const printAndSendDocumentsConfig = [
  {
    body: [
      {
        inline: false,
        label: "Bar Code",
        isMandatory: true,
        key: "barCode",
        type: "text",
        disable: false,
        populators: { name: "barCode", error: "Required" },
      },
      {
        type: "date",
        label: "Date of Booking",
        key: "dateOfBooking",
        populators: {
          name: "dateofBooking",
        },
      },
    ],
  },
];

export const updateEPostConfig = (status) => {
  if (status === "DELIVERED") {
    return [
      {
        body: [
          {
            isMandatory: true,
            type: "dropdown",
            key: "currentStatus",
            label: "Current Status",
            disable: false,
            populators: {
              name: "currentStatus",
              optionsKey: "name",
              options: [
                {
                  code: "IN_TRANSIT",
                  name: "In Transit",
                  isEnabled: true,
                },
                {
                  code: "NOT_DELIVERED",
                  name: "Not Delivered",
                  isEnabled: true,
                },
                {
                  code: "DELIVERED",
                  name: "Delivered",
                  isEnabled: true,
                },
                {
                  code: "NOT_UPDATED",
                  name: "Not Updated",
                  isEnabled: true,
                },
              ],
            },
          },
          {
            type: "date",
            label: "Date of Delivery",
            key: "dateOfDelivery",
            populators: {
              name: "dateOfDelivery",
            },
          },
        ],
      },
    ];
  }

  if (status === "NOT_DELIVERED") {
    return [
      {
        body: [
          {
            isMandatory: true,
            type: "dropdown",
            key: "currentStatus",
            label: "Current Status",
            disable: false,
            populators: {
              name: "currentStatus",
              optionsKey: "name",
              options: [
                {
                  code: "IN_TRANSIT",
                  name: "In Transit",
                  isEnabled: true,
                },
                {
                  code: "NOT_DELIVERED",
                  name: "Not Delivered",
                  isEnabled: true,
                },
                {
                  code: "DELIVERED",
                  name: "Delivered",
                  isEnabled: true,
                },
                {
                  code: "NOT_UPDATED",
                  name: "Not Updated",
                  isEnabled: true,
                },
              ],
            },
          },
          {
            type: "date",
            label: "Date of Delivery Attempted",
            key: "dateOfDelivery",
            populators: {
              name: "dateOfDelivery",
            },
          },
        ],
      },
    ];
  }

  return [
    {
      body: [
        {
          isMandatory: true,
          type: "dropdown",
          key: "currentStatus",
          label: "Current Status",
          disable: false,
          populators: {
            name: "currentStatus",
            optionsKey: "name",
            options: [
              {
                code: "IN_TRANSIT",
                name: "In Transit",
                isEnabled: true,
              },
              {
                code: "NOT_DELIVERED",
                name: "Not Delivered",
                isEnabled: true,
              },
              {
                code: "DELIVERED",
                name: "Delivered",
                isEnabled: true,
              },
              {
                code: "NOT_UPDATED",
                name: "Not Updated",
                isEnabled: true,
              },
            ],
          },
        },
      ],
    },
  ];
};
