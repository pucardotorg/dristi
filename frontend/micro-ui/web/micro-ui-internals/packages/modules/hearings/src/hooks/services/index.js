import { Request } from "@egovernments/digit-ui-libraries";
import { Urls } from "./Urls";

export const hearingService = {
  updateHearing: (data, params) => {
    return Request({
      url: Urls.hearing.hearingUpdate,
      useCache: false,
      userService: false,
      data,
      params,
    });
  },
  searchHearings: (data, params) => {
    return Request({
      url: Urls.hearing.searchHearings,
      useCache: false,
      userService: false,
      data,
      params,
    });
  },
  startHearing: ({ hearing }, params) => {
    return Request({
      url: Urls.hearing.updateHearings,
      useCache: false,
      userService: false,
      data: { hearing: { ...hearing, workflow: { action: "START" } } },
      params,
    });
  },
  customApiService: (url, data, params, useCache = false, userService = true) =>
    Request({
      url: url,
      useCache: useCache,
      userService: true,
      data,
      params,
    }),
};
