import { Request } from "@egovernments/digit-ui-libraries";

export const Urls = {
  Authenticate: "/user/oauth/token",
  case: {
    joinCase: "/case/v1/_verify",
  },
};

export const CASEService = {
  joinCaseService: (data, params) =>
    Request({
      url: Urls.case.joinCase,
      useCache: false,
      userService: true,
      data,
      params,
    }),
};
