import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
const hearing = {
  useIndividualView
};

const Hooks = {
  hearing
};

const Utils = {
  browser: {
    hearing: () => { },
  },
  hearing: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
