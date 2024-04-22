import Urls from "../atoms/urls";
import { Request } from "../atoms/Utils/Request";

export const DRISTIService = {
  postIndividualService: (data, tenantId) =>
    Request({
      url: Urls.dristi.individual,
      useCache: false,
      userService: false,
      method: "POST",
      auth: true,
      data,
      params: { tenantId },
    }),
  searchIndividualUser: (data, params) =>
    Request({
      url: Urls.dristi.searchIndividual,
      useCache: false,
      userService: false,
      method: "POST",
      auth: true,
      data,
      params,
    }),

  complainantService: (url, data, tenantId, userService = false, additionInfo) =>
    Request({
      url: url,
      useCache: false,
      userService: userService,
      method: "POST",
      auth: true,
      data,
      params: { tenantId },
      additionInfo,
    }),
  searchIndividualAdvocate: (data, params) =>
    Request({
      url: Urls.dristi.searchIndividualAdvocate,
      useCache: false,
      userService: false,
      method: "POST",
      auth: true,
      data,
      params,
    }),
};
