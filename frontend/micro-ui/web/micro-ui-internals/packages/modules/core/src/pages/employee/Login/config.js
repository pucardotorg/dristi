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
        key: "district",
        label: "CORE_COMMON_DISTRICT",
        disable: false,
        populators: {
          name: "district",
          optionsKey: "name",
          // error: "ERR_HRMS_INVALID_DISTRICT",
          mdmsConfig: {
            masterName: "District",
            moduleName: "common-masters",
            localePrefix: "COMMON_MASTERS_DISTRICT_",
            select: "(data) => {return data['common-masters']?.District?.map((item) => {return { code: item.code, name: item.name};});}",
          },
        },
      },
      {
        isMandatory: true,
        type: "dropdown",
        key: "courtroom",
        label: "CORE_COMMON_COURT_ROOM",
        disable: false,
        populators: {
          name: "courtroom",
          optionsKey: "name",
          // error: "ERR_HRMS_INVALID_COURT_ROOM",
          mdmsConfig: {
            masterName: "Court_Rooms",
            moduleName: "common-masters",
            localePrefix: "COMMON_MASTERS_COURT_R00M_",
            select: "(data) => {return data['common-masters']?.Court_Rooms?.map((item) => {return { code: item.code, name: item.name};});}",
          },
        },
      },
    ],
  },
];
