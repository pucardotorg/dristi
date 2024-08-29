import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import { CASEService } from "./services";

const cases = {
  useIndividualView,
};

const Hooks = {
  cases,
};

const Utils = {
  browser: {
    cases: () => {},
  },
  cases: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
  CASEService,
};
