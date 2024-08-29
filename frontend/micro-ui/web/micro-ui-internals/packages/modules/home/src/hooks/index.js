import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import { useGetPendingTask } from "./useGetPendingTask";
import { HomeService } from "./services";
import useSearchReschedule from "./useSearchReschedule";
const home = {
  useIndividualView,
  useGetPendingTask,
  useSearchReschedule,
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
  HomeService,
};
