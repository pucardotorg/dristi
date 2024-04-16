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
};
