import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
const order = {
  useIndividualView
};

const Hooks = {
  order
};

const Utils = {
  browser: {
    order: () => { },
  },
  order: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
