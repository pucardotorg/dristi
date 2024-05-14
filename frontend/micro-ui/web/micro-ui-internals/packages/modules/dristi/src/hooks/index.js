import useGetAdvocateClerk from "./dristi/useGetAdvocateClerk";
import useGetAdvocateClientServices from "./dristi/useGetAdvocateClientServices";
import useGetIndividualAdvocate from "./dristi/useGetIndividualAdvocate";
import useIndividualService from "./dristi/useIndividualService";
import useGetIndividualUser from "./dristi/useGetIndividualUser";
import { DRISTIService } from "../services";
import useInboxCustomHook from "./dristi/useInboxCustomHook";

export const Urls = {
  Authenticate: "/user/oauth/token",
  dristi: {
    individual: "/individual/v1/_create",
    searchIndividual: "/individual/v1/_search",
    searchIndividualAdvocate: "/advocate/advocate/v1/_search",
    searchIndividualClerk: "/advocate/clerk/v1/_search",
    updateAdvocateDetails: "/advocate/advocate/v1/_update",
  },
  FileFetchById: "/filestore/v1/files/id",
};

const dristi = {
  useGetAdvocateClerk,
  useGetAdvocateClientServices,
  useGetIndividualAdvocate,
  useIndividualService,
  useGetIndividualUser,
  useInboxCustomHook,
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

export const CustomizedHooks = {
  Hooks,
  DRISTIService,
  Utils,
};
