import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import { useGetPendingTask } from "./useGetPendingTask";
const home = {
  useIndividualView,
  useGetPendingTask,
};

const Hooks = {
  home,
};

const Utils = {
  browser: {
    home: () => {},
  },
  home: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
