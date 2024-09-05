import { Request } from "@egovernments/digit-ui-libraries";
import { Urls } from "./Urls";

export const ordersService = {
  createOrder: (data, params) =>
    Request({
      url: Urls.orders.orderCreate,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  updateOrder: (data, params) =>
    Request({
      url: Urls.orders.orderUpdate,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  searchOrder: (data, params) =>
    Request({
      url: Urls.orders.orderSearch,
      useCache: true,
      userService: true,
      data,
      params,
    }),
  customApiService: (url, data, params, useCache = false, userService = true) =>
    Request({
      url: url,
      useCache: useCache,
      userService,
      data,
      params,
    }),
  getPendingTaskService: (data, params) =>
    Request({
      url: Urls.orders.getPendingTaskFields,
      useCache: false,
      userService: true,
      data,
      params,
    }),
  createHearings: (data, params) =>
    Request({
      url: Urls.orders.createHearings,
      useCache: false,
      userService: false,
      data,
      params,
    }),
  updateHearings: (data, params) =>
    Request({
      url: Urls.orders.updateHearings,
      useCache: false,
      userService: false,
      data,
      params,
    }),
};

export const EpostService = {
  EpostUpdate: (data, params) =>
    Request({
      url: Urls.Epost.EpostUpdate,
      useCache: true,
      userService: true,
      data,
      params,
    }),
  customApiService: (url, data, params, useCache = false, userService = true) =>
    Request({
      url: url,
      useCache: useCache,
      userService,
      data,
      params,
    }),
};
export const schedulerService = {
  RescheduleHearing: (data, params) =>
    Request({
      url: Urls.Scheduler.reschedule,
      useCache: true,
      userService: true,
      data,
      params,
    }),
};

export const taskService = {
  UploadTaskDocument: (data, params) =>
    Request({
      url: Urls.Task.uploadDoc,
      useCache: true,
      userService: true,
      data,
      params,
    }),

  updateTask: (data, params) =>
    Request({
      url: Urls.Task.updateTask,
      useCache: true,
      userService: true,
      data,
      params,
    }),
};

export const SBIPaymentService = {
  SBIPayment: (data, params) =>
    Request({
      url: Urls.SBIPayment.payment,
      useCache: false,
      userService: true,
      data,
      params,
    }),
};
