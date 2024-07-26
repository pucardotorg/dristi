import { Request } from "@egovernments/digit-ui-libraries";
export const Urls = {
  getPendingTaskFields: "/inbox/v2/_getFields",
  caseSearch: "/case/v1/_search",
  applicationSearch: "/application/v1/search",
  orderCreate: "/order/v1/create",
  pendingTask: "/analytics/pending_task/v1/create",
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
  customApiService: (url, data, params, useCache = false, userService = true) =>
    Request({
      url: url,
      useCache: useCache,
      userService: true,
      data,
      params,
    }),
};
