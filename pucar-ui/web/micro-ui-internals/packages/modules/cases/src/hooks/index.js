import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
const cases = {
  useIndividualView
};

const Hooks = {
  cases
};

const Utils = {
  browser: {
    cases: () => { },
  },
  cases: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
