import { useIndividualView } from "./useIndividualView";
import utils from "../utils";
import useUpdateHearingsService from "./hearings/useUpdateHearingsService";
import useGetHearings from "./hearings/useGetHearings";
import usePreHearingModalData from "./usePreHearingModalData";
import useGetHearingSlotMetaData from "./useGetHearingSlotMetaData";

const hearings = {
  useIndividualView,
  useUpdateHearingsService,
  useGetHearings,
  usePreHearingModalData,
  useGetHearingSlotMetaData,
};

const Hooks = {
  hearings,
};

const Utils = {
  browser: {
    hearings: () => {},
  },
  hearings: {
    ...utils,
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
