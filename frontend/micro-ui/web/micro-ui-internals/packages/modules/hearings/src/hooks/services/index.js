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
};
