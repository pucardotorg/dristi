import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
const hearings = {
  useIndividualView
};

const Hooks = {
  hearings
};

const Utils = {
  browser: {
    hearings: () => { },
  },
  hearings: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
