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
    role: "ADVOCATE_ROLE",
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
    role: "ADVOCATE_CLERK_ROLE",
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerks",
      AdditionalFields: ["stateRegnNumber"],
    },
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
              options: userTypeOptions,
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
                pattern: /^[a-zA-Z0-9/]*$/i,
                errMsg: "BAR_REGISTRATION_NUMBER_INVALID",
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
                pattern: /^[a-zA-Z0-9/]*$/i,
                errMsg: "STATE_REGISTRATION_NUMBER_INVALID",
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
