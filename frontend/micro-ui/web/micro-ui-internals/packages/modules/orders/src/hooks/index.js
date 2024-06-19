import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
const orders = {
  useIndividualView
};

const Hooks = {
  orders
};

const Utils = {
  browser: {
    orders: () => { },
  },
  orders: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
