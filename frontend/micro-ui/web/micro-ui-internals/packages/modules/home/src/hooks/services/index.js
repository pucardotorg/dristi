import { Request } from "@egovernments/digit-ui-libraries";
export const Urls = {
  getPendingTaskFields: "/inbox/v2/_getFields",
};
export const HomeService = {
  getPendingTaskService: (data, params) =>
    Request({
      url: Urls.getPendingTaskFields,
      useCache: false,
      userService: true,
      data,
      params,
    }),
};
