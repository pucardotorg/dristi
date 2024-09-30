import { Request } from "@egovernments/digit-ui-libraries";
import { Urls } from "./Urls";

const judgeId = window?.globalConfigs?.getConfig("JUDGE_ID") || "JUDGE_ID";
const benchId = window?.globalConfigs?.getConfig("BENCH_ID") || "BENCH_ID";
const courtId = window?.globalConfigs?.getConfig("COURT_ID") || "COURT_ID";
const presidedBy = {
  judgeId: [judgeId],
  benchId: benchId,
  courtId: courtId,
};

export const hearingService = {
  updateHearingTranscript: (data, params) => {
    const updatedData = {
      ...data,
      hearing: {
        ...data.hearing,
        presidedBy: presidedBy,
      },
    };
    return Request({
      url: Urls.hearing.hearingUpdateTranscript,
      useCache: false,
      userService: false,
      data: updatedData,
      params,
    });
  },
  updateHearings: (data, params) => {
    const updatedData = {
      ...data,
      hearing: {
        ...data.hearing,
        presidedBy: presidedBy,
      },
    };
    return Request({
      url: Urls.hearing.updateHearings,
      useCache: false,
      userService: false,
      data: updatedData,
      params,
    });
  },
  searchHearings: (data, params) => {
    return Request({
      url: Urls.hearing.searchHearings,
      useCache: false,
      userService: false,
      data,
      params,
    });
  },
  searchTaskList: (data, params) => {
    return Request({
      url: Urls.hearing.searchTasks,
      useCache: false,
      userService: false,
      data,
      params,
    });
  },
  startHearing: ({ hearing }, params) => {
    const updatedData = { hearing: { ...hearing, presidedBy: presidedBy, workflow: { action: "START" } } };
    return Request({
      url: Urls.hearing.updateHearings,
      useCache: false,
      userService: false,
      data: updatedData,
      params,
    });
  },
  customApiService: (url, data, params, useCache = false, userDownload = false) =>
    Request({
      url: url,
      useCache: useCache,
      userService: true,
      data,
      params,
      userDownload,
    }),

  generateWitnessDepostionDownload: (data, params) =>
    Request({
      url: Urls.hearing.downloadWitnesspdf,
      useCache: false,
      userService: false,
      data,
      params,
      userDownload: true,
    }),
};
