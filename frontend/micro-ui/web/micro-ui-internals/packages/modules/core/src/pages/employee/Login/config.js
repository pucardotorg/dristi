export const loginConfig = [
  {
    texts: {
      header: "CORE_COMMON_LOGIN",
      submitButtonLabel: "CORE_COMMON_CONTINUE",
      secondaryButtonLabel: "CORE_COMMON_FORGOT_PASSWORD",
    },
    inputs: [
      {
        label: "CORE_LOGIN_USERNAME",
        type: "text",
        populators: {
          name: "username",
        },
        isMandatory: true,
      },
      {
        label: "CORE_LOGIN_PASSWORD",
        type: "password",
        populators: {
          name: "password",
        },
        isMandatory: true,
      },
      {
        isMandatory: true,
        type: "dropdown",
        key: "city",
        label: "CORE_COMMON_CITY",
        disable: false,
        populators: {
          name: "city",
          optionsKey: "name",
          error: "ERR_HRMS_INVALID_CITY",
          mdmsConfig: {
            masterName: "tenants",
            moduleName: "tenant",
            localePrefix: "TENANT_TENANTS",
            select:
              "(data) => {return data['tenant']?.tenants?.map((item) => {return { code: item.code, name: item.name + '_' + item.description };});}",
          },
        },
      },
    ],
  },
];
