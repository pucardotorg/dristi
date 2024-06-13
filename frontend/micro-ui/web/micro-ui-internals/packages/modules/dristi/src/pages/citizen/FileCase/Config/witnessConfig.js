const witnessFormConfig = [
  {
    head: "CS_WITNESS_NAME",
    body: [
      {
        type: "text",
        label: "FIRST_NAME",
        isMandatory: true,
        populators: {
          name: "firstName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "VERIFY_PHONE_ERROR_TEXT",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        labelChildren: "optional",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "VERIFY_PHONE_ERROR_TEXT",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
          },
        },
      },
      {
        type: "text",
        label: "LAST_NAME",
        isMandatory: true,
        populators: {
          name: "lastName",
          error: "CORE_REQUIRED_FIELD_ERROR",
          validation: {
            pattern: {
              message: "VERIFY_PHONE_ERROR_TEXT",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            minLength: 2,
            title: "",
            patternType: "Name",
          },
        },
      },
    ],
  },
  {
    head: "CS_WITNESS_CONTACT_DETAILS",
    subHead: "CS_WITNESS_NOTE",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "phonenumbers",
        withoutLabel: true,
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
                isNumber: true,
              },
              className: "mobile-number",
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: " ",
    body: [
      {
        type: "component",
        component: "SelectBulkInputs",
        key: "emails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CORE_COMMON_EMAILS",
              type: "text",
              name: "emailId",
              error: "ERR_HRMS_INVALID_MOB_NO",
              validation: {
                required: true,
                pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
              },
              className: "email-address",
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
        component: "SelectComponentsMulti",
        key: "addressDetails",
        formType: "Witness",
        withoutLabel: true,
        error: "sample required message",
        required: false,
        isMandatory: true,
        populators: {
          inputs: [
            { label: "CS_COMMON_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
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
        component: "SelectCustomTextArea",
        key: "witnessAdditionalDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "text",
              textAreaHeader: "CS_TEXTAREA_WITNESS_ADDITIONAL_DETAIL",
              placeholder: "CS_TEXTAREA_PLACEHOLDER_ADDITIONAL_DETAIL",
              headerClassName: "dristi-font-bold",
              type: "TextAreaComponent",
            },
          ],
        },
      },
    ],
  },
];

export const witnessConfig = {
  formconfig: witnessFormConfig,
  header: "CS_WITNESS_DETAIL_HEADING",
  subtext: "CS_WITNESS_DETAIL_SUBTEXT",
  isOptional: false,
  addFormText: "ADD_WITNESS",
  formItemName: "Witness",
  className: "witness-details"
};
