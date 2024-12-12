import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import { submissionService } from "./services";
import useSearchSubmissionService from "./submissions/useSearchSubmissionService";
import useSearchEvidenceService from "./submissions/useSearchEvidenceService";

const submissions = {
  useIndividualView,
  useSearchSubmissionService,
  useSearchEvidenceService,
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
