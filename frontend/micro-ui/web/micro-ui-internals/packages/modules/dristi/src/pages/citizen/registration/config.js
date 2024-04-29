export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "LITIGANT",
    showBarDetails: false,
    isVerified: false,
  },
  {
    code: "ADVOCATE",
    name: "ADVOCATE",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    apiDetails: {
      serviceName: "/advocate/advocate/v1/_create",
      requestKey: "advocates",
      AdditionalFields: ["barRegistrationNumber"],
    },
  },
  {
    code: "ADVOCATE_CLERK",
    name: "ADVOCATE CLERK",
    showBarDetails: true,
    hasStateRegistrationNo: true,
    isVerified: true,
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerks",
      AdditionalFields: ["stateRegnNumber"],
    },
  },
];

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
    head: "CS_COMMON_ADDRESS_DETAIL",
    body: [
      {
        type: "component",
        component: "SelectComponents",
        key: "addressDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            { label: "CS_PIN_LOCATION", type: "LocationSearch", name: ["pincode", "state", "district", "city", "coordinates", "locality"] },
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
            {
              label: "LOCALITY",
              type: "text",
              name: "locality",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "DOOR_NUMBER",
              type: "text",
              name: "doorNo",
              validation: {
                errMsg: "ADDRESS_DOOR_NO_INVALID",
                pattern: /^[^\$\"'<>?~`!@$%^={}\[\]*:;“”‘’]{2,50}$/i,
                isRequired: true,
                minlength: 2,
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
        component: "SelectUserTypeComponent",
        key: "clientDetails",
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
              isMandatory: true,
              clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
              options: [
                {
                  code: "LITIGANT",
                  name: "LITIGANT",
                  showBarDetails: false,
                  isVerified: false,
                },
                {
                  code: "ADVOCATE",
                  name: "ADVOCATE",
                  showBarDetails: true,
                  isVerified: true,
                  hasBarRegistrationNo: true,
                  apiDetails: {
                    serviceName: "/advocate/advocate/v1/_create",
                    requestKey: "advocates",
                    AdditionalFields: ["barRegistrationNumber"],
                  },
                },
                {
                  code: "ADVOCATE_CLERK",
                  name: "ADVOCATE CLERK",
                  showBarDetails: true,
                  hasStateRegistrationNo: true,
                  isVerified: true,
                  apiDetails: {
                    serviceName: "/advocate/clerk/v1/_create",
                    requestKey: "clerks",
                    AdditionalFields: ["stateRegnNumber"],
                  },
                },
              ],
            },
          ],
        },
      },
    ],
  },
];

export const termsAndConditionConfig = [
  {
    body: [
      {
        type: "checkbox",
        key: "Terms_Conditions",
        populators: {
          title: "I agree to Lorem ipsum dolor sit amet, consectetur adipiscing elit",
          name: "Terms_Conditions",
          styles: { minWidth: "100%" },
          labelStyles: { padding: "8px" },
          customStyle: { minWidth: "100%" },
        },
      },
    ],
  },
];

export const advocateClerkConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "clientDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "BAR_REGISTRATION_NUMBER",
              type: "text",
              name: "barRegistrationNumber",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { stateRegnNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasBarRegistrationNo"] },
            },
            {
              label: "STATE_REGISTRATION_NUMBER",
              type: "text",
              name: "stateRegnNumber",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { barRegistrationNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasStateRegistrationNo"] },
            },
            {
              label: "BAR_COUNCIL_ID",
              type: "documentUpload",
              name: "barCouncilId",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              dependentKey: { selectUserType: ["showBarDetails"] },
            },
          ],
        },
      },
    ],
  },
];
