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
import useApplicationDetails from "./dristi/useApplicationDetails.js";
import useEvidenceDetails from "./dristi/useEvidenceDetails.js";
import { useGetPendingTask } from "./dristi/useGetPendingTask.js";

export const Urls = {
  Authenticate: "/user/oauth/token",
  dristi: {
    individual: "/individual/v1/_create",
    searchIndividual: "/individual/v1/_search",
    searchIndividualAdvocate: "/advocate/advocate/v1/_search",
    searchIndividualClerk: "/advocate/clerk/v1/_search",
    updateAdvocateDetails: "/advocate/advocate/v1/_update",
    caseCreate: "/case/v1/_create",
    caseUpdate: "/case/v1/_update",
    caseSearch: "/case/v1/_search",
    evidenceSearch: "/evidence/v1/_search",
    evidenceCreate: "/evidence/v1/_create",
    evidenceUpdate: "/evidence/v1/_update",
    searchHearings: "/hearing/v1/search",
    createHearings: "/hearing/v1/create",
    updateHearings: "/hearing/v1/update",
    demandCreate: "/billing-service/demand/_create",
    ordersSearch: "/order/v1/search",
    ordersCreate: "/order/v1/create",
    submissionsSearch: "/application/v1/search",
    submissionsUpdate: "/application/v1/update",
    addSubmissionComment: "/application/v1/addcomment",
    addEvidenceComment: "/evidence/v1/addcomment",
    pendingTask: "/analytics/pending_task/v1/create",
    getPendingTaskFields: "/inbox/v2/_getFields",
  },
  case: {
    addWitness: "/case/v1/add/witness",
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
  useApplicationDetails,
  useEvidenceDetails,
  useToast,
  useGetPendingTask,
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
