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

  complainantService: (url, data, tenantId) =>
    Request({
      url: url,
      useCache: false,
      userService: false,
      method: "POST",
      auth: true,
      data,
      params: { tenantId },
    }),
};
