import { Request } from "@egovernments/digit-ui-libraries";
import { Urls } from "./Urls";

export const submissionService = {
  createApplication: (data, params) =>
    Request({
      url: Urls.application.applicationCreate,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  updateApplication: (data, params) =>
    Request({
      url: Urls.application.applicationUpdate,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  searchApplication: (data, params) =>
    Request({
      url: Urls.application.applicationSearch,
      useCache: true,
      userService: true,
      data,
      params,
    }),
  customApiService: (url, data, params, useCache = false, userService = true) =>
    Request({
      url: url,
      useCache,
      userService,
      data,
      params,
    }),
  getPendingTaskService: (data, params) =>
    Request({
      url: Urls.application.getPendingTaskFields,
      useCache: false,
      userService: true,
      data,
      params,
    }),
};
