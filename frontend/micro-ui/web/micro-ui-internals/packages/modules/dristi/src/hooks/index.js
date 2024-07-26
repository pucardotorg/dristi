import useGetAdvocateClerk from "./dristi/useGetAdvocateClerk";
import useGetAdvocateClientServices from "./dristi/useGetAdvocateClientServices";
import useGetIndividualAdvocate from "./dristi/useGetIndividualAdvocate";
import useGetIndividualUser from "./dristi/useGetIndividualUser";
import useIndividualService from "./dristi/useIndividualService";

import { DRISTIService } from "../services";
import useGetEvidence from "./dristi/useGetEvidence";
import useGetOrders from "./dristi/useGetOrders";
import useGetSubmissions from "./dristi/useGetSubmissions";
import useInboxCustomHook from "./dristi/useInboxCustomHook";
import useSearchCaseService from "./dristi/useSearchCaseService";
import { useToast } from "../components/Toast/useToast.js";
import useCreateHearings from "./dristi/useCreateHearings.js";

export const Urls = {
  Authenticate: "/user/oauth/token",
  dristi: {
    individual: "/individual/v1/_create",
    searchIndividual: "/individual/v1/_search",
    searchIndividualAdvocate: "/advocate/advocate/v1/_search",
    searchIndividualClerk: "/advocate/clerk/v1/_search",
    updateAdvocateDetails: "/advocate/advocate/v1/_update",
    caseCreate: "/case/case/v1/_create",
    caseUpdate: "/case/case/v1/_update",
    caseSearch: "/case/case/v1/_search",
    evidenceSearch: "/evidence/artifacts/v1/_search",
    evidenceCreate: "/evidence/artifacts/v1/_create",
    evidenceUpdate: "/evidence/artifacts/v1/_update",
    searchHearings: "/hearing/v1/search",
    createHearings: "/hearing/v1/create",
    demandCreate: "/billing-service/demand/_create",
    ordersSearch: "/order/order/v1/search",
    submissionsSearch: "/application/application/v1/search",
    submissionsUpdate: "/application/application/v1/update",
    pendingTask: "/analytics/pending_task/v1/create",
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
  useSearchCaseService,
  useCreateHearings,
  useGetEvidence,
  useGetOrders,
  useGetSubmissions,
  useToast,
};

const Hooks = {
  dristi,
};

const Utils = {
  dristi: {},
};
export const CustomizedHooks = {
  Hooks,
  DRISTIService,
  Utils,
};
