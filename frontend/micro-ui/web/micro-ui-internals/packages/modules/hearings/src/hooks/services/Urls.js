export const Urls = {
  Authenticate: "/user/oauth/token",
  pendingTask: "/analytics/pending_task/v1/create",
  hearing: {
    hearingUpdateTranscript: "/hearing/v1/update_transcript_additional_attendees",
    searchHearings: "/hearing/v1/search",
    searchTasks: "/task/v1/search",
    updateHearings: "/hearing/v1/update",
    downloadWitnesspdf: "/hearing/witnessDeposition/v1/downloadPdf",
    uploadWitnesspdf: "/hearing/witnessDeposition/v1/uploadPdf",
  },
  order: {
    createOrder: "/order/v1/create",
  },
  case: {
    caseSearch: "/case/v1/_search",
  },
  FileFetchById: "/filestore/v1/files/id",
};
