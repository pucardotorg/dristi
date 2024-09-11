export const Urls = {
  Authenticate: "/user/oauth/token",
  orders: {
    orderCreate: "/order/v1/create",
    orderUpdate: "/order/v1/update",
    orderSearch: "/order/v1/search",
    taskCreate: "/task/v1/create",
    pendingTask: "/analytics/pending_task/v1/create",
    createHearings: "/hearing/v1/create",
    updateHearings: "/hearing/v1/update",
    getPendingTaskFields: "/inbox/v2/_getFields",
    orderPreviewPdf: "/egov-pdf/order",
    searchTasks: "/task/v1/search",
  },
  FileFetchById: "/filestore/v1/files/id",
  Epost: {
    EpostUpdate: "/epost-tracker/epost/v1/_updateEPost",
  },
  Scheduler: {
    reschedule: "/scheduler/hearing/v1/_reschedule",
  },
  Task: {
    uploadDoc: "/task/v1/uploadDocument",
    updateTask: "/task/v1/update",
  },
  SBIPayment: {
    payment: "/sbi-backend/payment/v1/_processTransaction",
  },
};
