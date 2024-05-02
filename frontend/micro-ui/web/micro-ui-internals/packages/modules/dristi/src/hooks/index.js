import useGetAdvocateClerk from "./dristi/useGetAdvocateClerk";
import useGetAdvocateClientServices from "./dristi/useGetAdvocateClientServices";
import useGetIndividualAdvocate from "./dristi/useGetIndividualAdvocate";
import useIndividualService from "./dristi/useIndividualService";
import useGetIndividualUser from "./dristi/useGetIndividualUser";

const Urls = {
  dristi: {
    individual: "/individual/v1/_create",
    searchIndividual: "/individual/v1/_search",
    searchIndividualAdvocate: "/advocate/advocate/v1/_search",
    searchIndividualClerk: "/advocate/clerk/v1/_search",
    updateAdvocateDetails: "/advocate/advocate/v1/_update",
  },
};

const Digit = window?.Digit || {};

export const DRISTIService = {
  postIndividualService: (data, tenantId) =>
    Digit.CustomService.getResponse({
      url: Urls.dristi.individual,
      useCache: false,
      userService: false,
      data,
      params: { tenantId },
    }),
  updateAdvocateService: (data, params) =>
    Digit.CustomService.getResponse({
      url: Urls.dristi.updateAdvocateDetails,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  searchIndividualUser: (data, params) =>
    Digit.CustomService.getResponse({
      url: Urls.dristi.searchIndividual,
      useCache: false,
      userService: false,
      data,
      params,
    }),

  advocateClerkService: (url, data, tenantId, userService = false, additionInfo) =>
    Digit.CustomService.getResponse({
      url: url,
      useCache: false,
      userService: userService,
      data,
      params: { tenantId },
      additionInfo,
    }),
  searchIndividualAdvocate: (data, params) =>
    Digit.CustomService.getResponse({
      url: Urls.dristi.searchIndividualAdvocate,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  searchAdvocateClerk: (url, data, params) =>
    Digit.CustomService.getResponse({
      url: url,
      useCache: false,
      userService: true,
      data,
      params,
    }),
};

const dristi = {
  useGetAdvocateClerk,
  useGetAdvocateClientServices,
  useGetIndividualAdvocate,
  useIndividualService,
  useGetIndividualUser,
};

const contracts = {};

const Hooks = {
  attendance: {
    update: () => {},
  },
  dristi,
  contracts,
};

const Utils = {
  browser: {
    sample: () => {},
  },
  dristi: {},
};

export const CustomisedHooks = {
  Hooks,
  DRISTIService,
  Utils,
};
