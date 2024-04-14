export const newConfig = [
  {
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
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
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
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
              },
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
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
    head: "CS_COMMON_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "SelectComponents",
        withoutLabel: true,
        populators: {
          inputs: [
            { label: "CS_PIN_LOCATION", type: "LocationSearch", name: "pincode" },
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
                title: "ADDRESS_PINCODE_INVALID",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "STATE",
              type: "text",
              name: "permanentState",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "permanentDistrict",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "CITY/TOWN",
              type: "text",
              name: "permanentCity",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "LOCALITY",
              type: "text",
              name: "permanentLocality",
              validation: {},
            },
            {
              label: "DOOR_NAME",
              type: "text",
              name: "permanentDoorName",
              validation: {},
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
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "SELECT_USER_TYPE",
              type: "radioButton",
              name: "selectUserType",
              optionsKey: "name",
              error: "sample required message",
              required: false,
              options: [
                {
                  code: "LITIGANT",
                  name: "LITIGANT",
                  showBarDetails: false,
                },
                {
                  code: "ADVOCATE",
                  name: "ADVOCATE",
                  showBarDetails: true,
                },
                {
                  code: "ADVOCATE_CLERK",
                  name: "ADVOCATE_CLERK",
                  showBarDetails: true,
                },
              ],
            },
            {
              label: "STATE_OF_REGISTRATION",
              type: "text",
              name: "stateOfRegistration",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              dependentKey: "showBarDetails",
            },
            {
              label: "BAR_REGISTRATION_NUMBER",
              type: "text",
              name: "registrationNo",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              dependentKey: "showBarDetails",
            },
            {
              label: "BAR_COUNCIL_ID",
              type: "documentUpload",
              name: "barCouncilId",
              validation: {
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                title: "CORE_COMMON_APPLICANT_NAME_INVALID",
                patternType: "Name",
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              dependentKey: "showBarDetails",
            },
          ],
          validation: {},
        },
      },
    ],
  },
];
