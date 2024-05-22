const witnessFormConfig = [
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
    head: "CS_RESPONDENT_PHONE",
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
              },
              className: "mobile-number"
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
        withoutLabel: true,
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
              className: "email-address"
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
        component: "SelectCustomTextArea",
        key: "witnessAdditionalDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              textAreaHeader: "CS_TEXTAREA_WITNESS_ADDITIONAL_DETAIL",
              placeholder: "CS_TEXTAREA_PLACEHOLDER_WITNESS_ADDITIONAL_DETAIL",
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
};
