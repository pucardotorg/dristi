import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import { submissionService } from "./services";
const submissions = {
  useIndividualView,
};

const Hooks = {
  submissions,
};

const Utils = {
  browser: {
    submissions: () => {},
  },
  submissions: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
  submissionService,
};
