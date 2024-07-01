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
};
