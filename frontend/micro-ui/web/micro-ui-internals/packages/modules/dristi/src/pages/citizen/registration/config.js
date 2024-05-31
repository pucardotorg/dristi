export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "LITIGANT_TEXT",
    showBarDetails: false,
    isVerified: false,
    role: ["CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER"],
    subText: "LITIGANT_SUB_TEXT",
  },
  {
    code: "ADVOCATE",
    name: "ADVOCATE_TEXT",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    role: ["ADVOCATE_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/advocate/v1/_create",
      requestKey: "advocates",
      AdditionalFields: ["barRegistrationNumber"],
    },
    subText: "ADVOCATE_SUB_TEXT",
  },
  {
    code: "ADVOCATE_CLERK",
    name: "ADVOCATE_CLERK_TEXT",
    showBarDetails: true,
    hasStateRegistrationNo: true,
    isVerified: true,
    role: ["ADVOCATE_CLERK_ROLE", "CASE_CREATOR", "CASE_EDITOR", "CASE_VIEWER"],
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerks",
      AdditionalFields: ["stateRegnNumber"],
    },

    subText: "ADVOCATE_CLERK_SUB_TEXT",
  },
];

export const roles = {
  tenantId: "pg",
  moduleName: "ACCESSCONTROL-ROLES",
  roles: [
    {
      code: "CITIZEN",
      name: "Citizen",
      description: "Citizen who can raise complaint",
    },
    {
      code: "EMPLOYEE",
      name: "Employee",
      description: "Default role for all employees",
    },
    {
      code: "SUPERUSER",
      name: "Super User",
      description: "System Administrator. Can change all master data and has access to all the system screens.",
    },
    {
      code: "MDMS_ADMIN",
      name: "MDMS ADMIN",
      description: "MDMS User that can create and search schema",
    },
    {
      code: "LOC_ADMIN",
      name: "Localisation admin",
      description: "LOC_ADMIN",
    },
    {
      code: "CASE_REVIEWER",
      name: "CASE_REVIEWER",
      description: "CASE_REVIEWER",
    },
    {
      code: "CASE_CREATOR",
      name: "CASE_CREATOR",
      description: "CASE_CREATOR",
    },
    {
      code: "CASE_EDITOR",
      name: "CASE_EDITOR",
      description: "CASE_EDITOR",
    },
    {
      code: "CASE_VIEWER",
      name: "CASE_VIEWER",
      description: "CASE_VIEWER",
    },
    {
      code: "CASE_APPROVER",
      name: "CASE_APPROVER",
      description: "CASE_APPROVER",
    },
    {
      code: "PAYMENT_COLLECTOR",
      name: "PAYMENT_COLLECTOR",
      description: "PAYMENT_COLLECTOR",
    },
    {
      code: "APPLICATION_CREATOR",
      name: "APPLICATION_CREATOR",
      description: "APPLICATION_CREATOR",
    },
    {
      code: "APPLICATION_RESPONDER",
      name: "APPLICATION_RESPONDER",
      description: "APPLICATION_RESPONDER",
    },
    {
      code: "APPLICATION_APPROVER",
      name: "APPLICATION_APPROVER",
      description: "APPLICATION_APPROVER",
    },
    {
      code: "WORKFLOW_ADMIN",
      name: "Workflow Admin",
      description: "Pers0n who has authority to abandon or edit workflow",
    },
    {
      code: "HEARING_CREATOR",
      name: "HEARING_CREATOR",
      description: "HEARING_CREATOR",
    },
    {
      code: "HEARING_SCHEDULER",
      name: "HEARING_SCHEDULER",
      description: "HEARING_SCHEDULER",
    },
    {
      code: "HEARING_START",
      name: "HEARING_START",
      description: "HEARING_START",
    },
    {
      code: "HEARING_APPROVER",
      name: "HEARING_APPROVER",
      description: "HEARING_APPROVER",
    },
    {
      code: "HEARING_ACCEPTOR",
      name: "HEARING_ACCEPTOR",
      description: "HEARING_ACCEPTOR",
    },
    {
      code: "HEARING_DATE_REQUESTOR",
      name: "HEARING_DATE_REQUESTOR",
      description: "HEARING_DATE_REQUESTOR",
    },
    {
      code: "HEARING_CLOSER",
      name: "HEARING_CLOSER",
      description: "HEARING_CLOSER",
    },
    {
      code: "ORDER_CREATOR",
      name: "ORDER_CREATOR",
      description: "ORDER_CREATOR",
    },
    {
      code: "ORDER_APPROVER",
      name: "ORDER_APPROVER",
      description: "ORDER_APPROVER",
    },
    {
      code: "ORDER_REASSIGN",
      name: "ORDER_REASSIGN",
      description: "ORDER_REASSIGN",
    },
    {
      code: "ORDER_ESIGN",
      name: "ORDER_ESIGN",
      description: "ORDER_ESIGN",
    },
    {
      code: "ORDER_STAMP",
      name: "ORDER_STAMP",
      description: "ORDER_STAMP",
    },
    {
      code: "ORDER_CLOSER",
      name: "ORDER_CLOSER",
      description: "ORDER_CLOSER",
    },
    {
      code: "DEPOSITION_CREATOR",
      name: "DEPOSITION_CREATOR",
      description: "DEPOSITION_CREATOR",
    },
    {
      code: "DEPOSITION_ESIGN",
      name: "DEPOSITION_ESIGN",
      description: "DEPOSITION_ESIGN",
    },
    {
      code: "DEPOSITION_PUBLISHER",
      name: "DEPOSITION_PUBLISHER",
      description: "DEPOSITION_PUBLISHER",
    },
    {
      code: "COURT_ADMIN",
      name: "COURT_ADMIN",
      description: "COURT_ADMIN",
    },
    {
      code: "SYSTEM_ADMIN",
      name: "SYSTEM_ADMIN",
      description: "SYSTEM_ADMIN",
    },
    {
      code: "HRMS_ADMIN",
      name: "HRMS_ADMIN",
      description: "HRMS_ADMIN",
    },
    {
      code: "TASK_CREATOR",
      name: "TASK_CREATOR",
      description: "TASK_CREATOR",
    },
    {
      code: "TASK_APPROVER",
      name: "TASK_APPROVER",
      description: "TASK_APPROVER",
    },
    {
      code: "TASK_CLOSER",
      name: "TASK_CLOSER",
      description: "TASK_CLOSER",
    },
    {
      code: "TASK_REJECTOR",
      name: "TASK_REJECTOR",
      description: "TASK_REJECTOR",
    },
    {
      code: "TASK_RESPONDER",
      name: "TASK_RESPONDER",
      description: "TASK_RESPONDER",
    },
    {
      code: "TASK_REASSIGN",
      name: "TASK_REASSIGN",
      description: "TASK_REASSIGN",
    },
    {
      code: "TASK_UPDATOR",
      name: "TASK_UPDATOR",
      description: "TASK_UPDATOR",
    },
    {
      code: "ADVOCATE_APPROVER",
      name: "ADVOCATE_APPROVER",
      description: "User will be able to approve new advocate registration/update applications",
    },
    {
      code: "ADVOCATE_CLERK_APPROVER",
      name: "ADVOCATE_CLERK_APPROVER",
      description: "User will be able to approve new advocate-clerk registration/update applications",
    },
    {
      code: "ADVOCATE_ROLE",
      name: "ADVOCATE_ROLE",
      description: "User will be assigned an advocate role",
    },
    {
      code: "ADVOCATE_CLERK_ROLE",
      name: "ADVOCATE_CLERK_ROLE",
      description: "User will be assigned an advocate-clerk role",
    },
  ],
};

export const newConfig = [
  {
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
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
            minLength: 2,
          },
        },
      },
      {
        type: "text",
        label: "MIDDLE_NAME",
        populators: {
          name: "middleName",
          error: "ERR_HRMS_INVALID_MIDDLE_NAME",
          validation: {
            pattern: {
              message: "ERR_HRMS_INVALID_MIDDLE_NAME",
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
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
              value: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
            },
            title: "",
            patternType: "Name",
            minLength: 2,
          },
        },
      },
    ],
  },
  {
    head: "CS_ENTER_ADDRESS",
    body: [
      {
        type: "component",
        component: "AddressComponent",
        key: "addressDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_PIN_LOCATION",
              type: "LocationSearch",
              name: ["pincode", "state", "district", "city", "coordinates", "locality", "buildingName", "doorNo"],
            },
            {
              label: "PINCODE",
              type: "text",
              name: "pincode",
              validation: {
                minlength: 6,
                maxlength: 6,
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
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_STATE_INVALID",
                patternType: "Name",
                title: "",
              },
              isMandatory: true,
            },
            {
              label: "DISTRICT",
              type: "text",
              name: "district",
              validation: {
                isRequired: true,
                pattern: /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,50}$/i,
                errMsg: "CORE_COMMON_APPLICANT_DISTRICT_INVALID",
                patternType: "Name",
                title: "",
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
              label: "BUILDING_NAME",
              type: "text",
              name: "buildingName",
              validation: {
                errMsg: "ADDRESS_BUILDING_NAME_INVALID",
                isRequired: true,
                minlength: 2,
                title: "",
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
        component: "CustomRadioCard",
        key: "clientDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "SELECT_USER_TYPE_TEXT",
              subLabel: "SELECT_USER_TYPE_SUB_TEXT",
              type: "radioButton",
              name: "selectUserType",
              optionsKey: "name",
              error: "sample required message",
              required: false,
              isMandatory: true,
              clearFields: { stateOfRegistration: "", barRegistrationNumber: "", barCouncilId: [], stateRegnNumber: "" },
              options: userTypeOptions,
              styles: { flexDirection: "column" },
            },
          ],
        },
      },
    ],
  },
  {
    head: "CS_ENTER_NAME",
    subHead: "CS_ENTER_NAME_SUB_TEXT",
    headId: "select-name-subtext", // for css
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
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
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
        label: "CORE_COMMON_MIDDLE_NAME",
        populators: {
          name: "middleName",
          validation: {
            pattern: {
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
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
              message: "CORE_COMMON_APPLICANT_NAME_INVALID",
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
    texts: {
      // header: "CS_LOGIN_OTP",
      cardText: "CS_LOGIN_OTP_TEXT",
    },
  },
  {
    texts: {
      header: "CS_ENTER_MOBILE",
      cardText: "CS_ENTER_MOBILE_SUB_TEXT",
      submitBarLabel: "CS_COMMON_CONTINUE",
      submitInForm: true,
    },
    inputs: [
      {
        label: "CORE_COMMON_MOBILE_NUMBER",
        type: "text",
        name: "mobileNumber",
        error: "ERR_HRMS_INVALID_MOB_NO",
        validation: {
          required: true,
          minlength: 10,
          maxlength: 10,
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "CustomRadioCard",
        key: "IdVerification",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_VERFIY_IDENTITY",
              subLabel: "CS_VERFIY_IDENTITY_SUB_TEXT",
              type: "radioButton",
              name: "selectIdType",
              optionsKey: "name",
              error: "sample required message",
              validation: {},
              clearFields: { aadharNumber: "" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              options: [
                {
                  code: "AADHAR",
                  name: "CS_ADHAAR",
                  subText: "CS_ADHAAR_SUB_TEXT",
                },
                {
                  code: "OTHER ID",
                  name: "CS_OTHER",
                  subText: "CS_OTHER_SUB_TEXT",
                },
              ],
              optionsCustomStyle: {
                top: "40px",
              },
              styles: { flexDirection: "column" },
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    head: "CS_ENTER_ADHAAR",
    subHead: "CS_ENTER_ADHAAR_TEXT",
    body: [
      {
        type: "component",
        component: "AdhaarInput",
        key: "AdhaarInput",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "ENTER_AADHAR_NUMBER",
              type: "text",
              name: "aadharNumber",
              validation: {
                minlength: 12,
                maxlength: 12,
                patternType: "AadharNo",
                pattern: "[0-9]+",
                errMsg: "AADHAR_NUMBER_INVALID",
                title: "",
              },
              clearFields: { ID_Proof: [], selectIdTypeType: "" },
              clearFieldsType: { ID_Proof: "documentUpload" },
              disableMandatoryFieldFor: ["ID_Proof", "selectIdTypeType"],
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    texts: {
      header: "CS_AADHAR_OTP",
      cardText: "CS_AADHAR_OTP_TEXT",
      nextText: "CS_COMMONS_NEXT",
      submitBarLabel: "CS_COMMONS_NEXT",
    },
  },
  {
    head: "UPLOAD-ID",
    subHead: "UPLOAD_SUBTEXT",
    body: [
      {
        type: "component",
        component: "SelectUserTypeComponent",
        key: "SelectUserTypeComponent",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "CS_ID_TYPE",
              type: "dropdown",
              name: "selectIdType",
              optionsKey: "name",
              error: "sample required message",
              validation: {},
              clearFields: { aadharNumber: "", ID_Proof: [] },
              clearFieldsType: { ID_Proof: "documentUpload" },
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              disableFormValidation: false,
              options: [
                {
                  code: "AADHAR",
                  name: "AADHAR",
                },
                {
                  code: "PAN",
                  name: "PAN",
                },
              ],
              optionsCustomStyle: {
                top: "40px",
              },
            },
            {
              label: "Upload ID Proof",
              type: "documentUpload",
              name: "ID_Proof",
              validation: {},
              clearFields: { aadharNumber: "" },
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
              isMandatory: true,
              disableMandatoryFieldFor: ["aadharNumber"],
              errorMessage: "CUSTOM_DOCUMENT_ERROR_MSG",
              disableFormValidation: false,
            },
          ],
          validation: {},
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

export const advocateClerkConfig = [
  {
    head: "CORE_ADVOCATE_VERFICATION",
    subHead: "CORE_ADVOCATE_VERFICATION_TEXT",

    body: [
      {
        type: "component",
        component: "AdvocateDetailComponent",
        key: "clientDetails",
        // header: "Verify your identity",
        // withoutLabel: true,
        // subLabel: "Before diving in, we'll need to verify your identity for account setup",
        populators: {
          inputs: [
            {
              label: "BAR_REGISTRATION_NUMBER",
              type: "text",
              name: "barRegistrationNumber",
              validation: {
                isRequired: true,
                pattern: /^[0-9A-Z/]+$/,
                errMsg: "BAR_REGISTRATION_NUMBER_INVALID",
                maxlength: 15,
              },
              isMandatory: true,
              isDependentOn: "selectUserType",
              clearFields: { stateRegnNumber: "" },
              dependentKey: { selectUserType: ["showBarDetails", "hasBarRegistrationNo"] },
            },

            {
              label: "BAR_COUNCIL_ID",
              type: "documentUpload",
              name: "barCouncilId",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
              allowedFileTypes: /(.*?)(png|jpeg|pdf)$/i,
              isDependentOn: "selectUserType",
              dependentKey: { selectUserType: ["showBarDetails"] },
            },
          ],
        },
      },
    ],
  },
];
