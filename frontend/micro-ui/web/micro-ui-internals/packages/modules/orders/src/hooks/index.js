import { useIndividualView } from "./useIndividualView";
import utils from "../utils";

import { ordersService } from "./services";
import useSearchOrdersService from "./orders/useSearchOrdersService";

const orders = {
  useIndividualView,
  useSearchOrdersService,
};

const Hooks = {
  orders,
};

const Utils = {
  browser: {
    orders: () => {},
  },
  orders: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
  ordersService,
};
