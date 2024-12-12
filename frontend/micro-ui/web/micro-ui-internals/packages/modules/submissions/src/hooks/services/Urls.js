export const Urls = {
  Authenticate: "/user/oauth/token",
  application: {
    applicationCreate: "/application/v1/create",
    applicationUpdate: "/application/v1/update",
    applicationSearch: "/application/v1/search",
    pendingTask: "/analytics/pending_task/v1/create",
    getPendingTaskFields: "/inbox/v2/_getFields",
    submissionPreviewPdf: "/egov-pdf/application",
    taskCreate: "/task/v1/create",
  },
  evidence: {
    evidenceSearch: "/evidence/v1/_search",
    evidenceCreate: "/evidence/v1/_create",
    evidenceUpdate: "/evidence/v1/_update",
  },
  FileFetchById: "/filestore/v1/files/id",
};
