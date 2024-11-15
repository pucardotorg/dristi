import React from "react";
import { Link } from "react-router-dom";
import { formatDate } from "../../../cases/src/utils";
import { formatDateDifference } from "../../../orders/src/utils";

const customColumnStyle = { whiteSpace: "nowrap" };

const handleTaskDetails = (taskDetails) => {
  try {
    // Check if taskDetails is a string
    if (typeof taskDetails === "string") {
      // First, remove escape characters like backslashes if present
      const cleanedDetails = taskDetails.replace(/\\n/g, "").replace(/\\/g, "");

      // Try parsing the cleaned string as JSON
      const parsed = JSON.parse(cleanedDetails);

      // If the parsed result is a string, try parsing it again
      if (typeof parsed === "string") {
        try {
          return JSON.parse(parsed);
        } catch (e) {
          return parsed;
        }
      }

      // Return the parsed object if it's already a valid JSON object
      return parsed;
    }

    // If taskDetails is not a string, return it as it is
    return taskDetails;
  } catch (error) {
    console.error("Failed to parse taskDetails:", error);
    return null;
  }
};

const handleNavigate = (path) => {
  console.log("Funvtion called ");
  const contextPath = window?.contextPath || "";

  window.location.href = `/${contextPath}${path}`;
};

export const UICustomizations = {
  EpostTrackingUiConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      const ePostTrackerSearchCriteria = {
        ...requestCriteria?.body?.ePostTrackerSearchCriteria,
        processNumber: requestCriteria?.state?.searchForm?.processNumber ? requestCriteria?.state?.searchForm?.processNumber : "",
        deliveryStatusList: requestCriteria?.state?.searchForm?.deliveryStatusList?.selected
          ? [requestCriteria?.state?.searchForm?.deliveryStatusList?.selected]
          : requestCriteria?.body?.ePostTrackerSearchCriteria.deliveryStatusList,
        pagination: {
          sortBy: requestCriteria?.state?.searchForm?.pagination?.sortBy
            ? requestCriteria?.state?.searchForm?.pagination?.sortBy
            : requestCriteria?.body?.ePostTrackerSearchCriteria?.pagination?.sortBy,
          orderBy: requestCriteria?.state?.searchForm?.pagination?.order
            ? requestCriteria?.state?.searchForm?.pagination?.order
            : requestCriteria?.body?.ePostTrackerSearchCriteria?.pagination?.orderBy,
        },
      };
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          ePostTrackerSearchCriteria,
          processNumber: "",
          deliveryStatusList: {},
          pagination: {
            sortBy: "",
            order: "",
          },
        },
        config: {
          ...requestCriteria?.config,
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      switch (key) {
        case "Delivery Status":
          return t(value);
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
  SearchHearingsConfig: {
    customValidationCheck: (data) => {
      const { createdFrom, createdTo } = data;
      if ((createdFrom === "" && createdTo !== "") || (createdFrom !== "" && createdTo === ""))
        return { warning: true, label: "ES_COMMON_ENTER_DATE_RANGE" };

      return false;
    },
    preProcess: (data) => {
      console.log(data, "data");
      return data;
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      switch (key) {
        case "MASTERS_WAGESEEKER_ID":
          return (
            <span className="link">
              <Link to={`/${window.contextPath}/employee/masters/view-wageseeker?tenantId=${row?.tenantId}&individualId=${value}`}>
                {String(value ? (column.translate ? t(column.prefix ? `${column.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
              </Link>
            </span>
          );

        case "MASTERS_SOCIAL_CATEGORY":
          return value ? <span style={customColumnStyle}>{String(t(`MASTERS_${value}`))}</span> : t("ES_COMMON_NA");

        case "CORE_COMMON_PROFILE_CITY":
          return value ? <span style={customColumnStyle}>{String(t(Digit.Utils.locale.getCityLocale(value)))}</span> : t("ES_COMMON_NA");

        case "MASTERS_WARD":
          return value ? (
            <span style={customColumnStyle}>{String(t(Digit.Utils.locale.getMohallaLocale(value, row?.tenantId)))}</span>
          ) : (
            t("ES_COMMON_NA")
          );

        case "MASTERS_LOCALITY":
          return value ? (
            <span style={customColumnStyle}>{String(t(Digit.Utils.locale.getMohallaLocale(value, row?.tenantId)))}</span>
          ) : (
            t("ES_COMMON_NA")
          );
        default:
          return t("ES_COMMON_NA");
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "MASTERS_WAGESEEKER_ID")
          link = `/${window.contextPath}/employee/masters/view-wageseeker?tenantId=${tenantId}&wageseekerId=${row[key]}`;
      });
      return link;
    },
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
  },
  homeLitigantUiConfig: {
    customValidationCheck: (data) => {
      //checking both to and from date are present
      const { createdFrom, createdTo } = data;
      if ((createdFrom === "" && createdTo !== "") || (createdFrom !== "" && createdTo === ""))
        return { warning: true, label: "ES_COMMON_ENTER_DATE_RANGE" };
      else if (!data?.filingNumber?.trim() && !data?.caseType?.trim()) return { label: "PlEASE_APPLY_FILTER_CASE_ID", error: true };
      return false;
    },
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
      const tenantId = window?.Digit.ULBService.getStateId();
      const { data: outcomeTypeData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "case", [{ name: "OutcomeType" }], {
        select: (data) => {
          return (data?.case?.OutcomeType || []).flatMap((item) => {
            return item?.judgementList?.length > 0 ? item.judgementList : [item?.outcome];
          });
        },
      });
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          tenantId,
          ...additionalDetails,
          ...("sortBy" in additionalDetails && {
            [additionalDetails.sortBy]: undefined,
            sortBy: undefined,
          }),
          ...(requestCriteria?.body?.criteria[0]["outcome"] && {
            outcome: outcomeTypeData,
          }),
          ...(requestCriteria?.state?.searchForm?.outcome && {
            outcome: [requestCriteria?.state?.searchForm?.outcome?.outcome],
          }),
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          pagination: {
            limit: requestCriteria?.state?.tableForm?.limit,
            offSet: requestCriteria?.state?.tableForm?.offset,
            ...("sortBy" in additionalDetails && {
              ...requestCriteria?.state?.searchForm[additionalDetails.sortBy],
            }),
          },
        },
      ];
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          criteria,
          tenantId,
        },
        config: {
          ...requestCriteria?.config,
          select: (data) => {
            return { ...data, totalCount: data?.criteria?.[0]?.pagination?.totalCount };
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      const today = new Date();
      const formattedToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
      switch (key) {
        case "Draft Name":
        case "CS_CASE_NAME":
          return (
            <span className="case-name-on-hover">
              {row?.caseTitle ? (row?.caseTitle?.trim().endsWith("vs") ? `${row?.caseTitle} _______` : row?.caseTitle) : t("CASE_UNTITLED")}
            </span>
          );
        case "CASE_TYPE":
          return <span>NIA S138</span>;
        case "CS_OUTCOME":
          return t(value);
        case "CS_STAGE":
          return t(row?.status);
        case "CS_FILING_DATE":
          return <span>{formatDate(new Date(value))}</span>;
        case "CS_LAST_EDITED":
          const createdAt = new Date(value);
          const formattedCreatedAt = new Date(createdAt.getFullYear(), createdAt.getMonth(), createdAt.getDate());
          const differenceInTime = formattedToday.getTime() - formattedCreatedAt.getTime();
          const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));
          return <span>{differenceInDays} Days ago</span>;
        default:
          return t("ES_COMMON_NA");
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "MASTERS_WAGESEEKER_ID")
          link = `/${window.contextPath}/employee/masters/view-wageseeker?tenantId=${tenantId}&wageseekerId=${row[key]}`;
      });
      return link;
    },
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
  },
  homeFSOUiConfig: {
    customValidationCheck: (data) => {
      //checking both to and from date are present
      const { createdFrom, createdTo } = data;
      if ((createdFrom === "" && createdTo !== "") || (createdFrom !== "" && createdTo === ""))
        return { warning: true, label: "ES_COMMON_ENTER_DATE_RANGE" };
      return false;
    },
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
      const tenantId = window?.Digit.ULBService.getStateId();
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          tenantId,
          ...additionalDetails,
          ...("sortBy" in additionalDetails && {
            [additionalDetails.sortBy]: undefined,
            sortBy: undefined,
          }),
          pagination: {
            limit: requestCriteria?.state?.tableForm?.limit,
            offSet: requestCriteria?.state?.tableForm?.offset,
            ...("sortBy" in additionalDetails && {
              ...requestCriteria?.state?.searchForm[additionalDetails.sortBy],
            }),
          },
        },
      ];
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          criteria,
          tenantId,
        },
        config: {
          ...requestCriteria?.config,
          select: (data) => {
            return { ...data, totalCount: data?.criteria?.[0]?.pagination?.totalCount };
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      const today = new Date();
      const formattedToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
      switch (key) {
        case "CASE_TYPE":
          return <span>NIA S138</span>;
        case "CS_SCRUTINY_STATUS":
          return t(row?.status === "UNDER_SCRUTINY" ? "IN_PROGRESS" : "NOT_STARTED");
        case "CS_DAYS_FILING":
          const createdAt = new Date(value);
          const formattedCreatedAt = new Date(createdAt.getFullYear(), createdAt.getMonth(), createdAt.getDate());
          const differenceInTime = formattedToday.getTime() - formattedCreatedAt.getTime();
          const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));
          return <span style={{ color: differenceInDays > 2 && "#9E400A", fontWeight: differenceInDays > 2 ? 500 : 400 }}>{differenceInDays}</span>;
        default:
          return t("ES_COMMON_NA");
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "MASTERS_WAGESEEKER_ID")
          link = `/${window.contextPath}/employee/masters/view-wageseeker?tenantId=${tenantId}&wageseekerId=${row[key]}`;
      });
      return link;
    },
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
  },
  homeJudgeUIConfig: {
    customValidationCheck: (data) => {
      //checking both to and from date are present
      const { createdFrom, createdTo } = data;
      if ((createdFrom === "" && createdTo !== "") || (createdFrom !== "" && createdTo === ""))
        return { warning: true, label: "ES_COMMON_ENTER_DATE_RANGE" };
      return false;
    },
    preProcess: (requestCriteria, additionalDetails) => {
      const tenantId = window?.Digit.ULBService.getStateId();
      const { data: outcomeTypeData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "case", [{ name: "OutcomeType" }], {
        select: (data) => {
          return (data?.case?.OutcomeType || []).flatMap((item) => {
            return item?.judgementList?.length > 0 ? item.judgementList : [item?.outcome];
          });
        },
      });
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          tenantId,
          ...additionalDetails,
          ...("sortBy" in additionalDetails && {
            [additionalDetails.sortBy]: undefined,
            sortBy: undefined,
          }),
          ...(requestCriteria?.body?.criteria[0]["outcome"] && {
            outcome: outcomeTypeData,
          }),
          ...(requestCriteria?.state?.searchForm?.outcome && {
            outcome: [requestCriteria?.state?.searchForm?.outcome?.outcome],
          }),
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          pagination: {
            limit: requestCriteria?.state?.tableForm?.limit,
            offSet: requestCriteria?.state?.tableForm?.offset,
            ...("sortBy" in additionalDetails && {
              ...requestCriteria?.state?.searchForm[additionalDetails.sortBy],
            }),
          },
        },
      ];
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          ...(requestCriteria?.state?.searchForm?.substage && {
            substage: requestCriteria?.state?.searchForm?.substage?.subStage,
          }),
          criteria,
          tenantId,
        },
        config: {
          ...requestCriteria?.config,
          select: (data) => {
            return { ...data, totalCount: data?.criteria?.[0]?.pagination?.totalCount };
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      switch (key) {
        case "CASE_TYPE":
          return <span>NIA S138</span>;
        case "CS_FILING_DATE":
          return <span>{formatDate(new Date(value))}</span>;
        case "CD_OUTCOME":
          return t(value);
        case "CS_STAGE":
          return t(row?.status);
        default:
          return t("ES_COMMON_NA");
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "MASTERS_WAGESEEKER_ID")
          link = `/${window.contextPath}/employee/masters/view-wageseeker?tenantId=${tenantId}&wageseekerId=${row[key]}`;
      });
      return link;
    },
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
  },
  reviewSummonWarrantNotice: {
    preProcess: (requestCriteria, additionalDetails) => {
      const filterList = Object.keys(requestCriteria.state.searchForm)
        ?.map((key) => {
          if (requestCriteria.state.searchForm[key]) return { [key]: requestCriteria.state.searchForm[key] };
        })
        ?.filter((filter) => filter)
        .reduce(
          (fieldObj, item) => ({
            ...fieldObj,
            ...item,
          }),
          {}
        );
      const tenantId = window?.Digit.ULBService.getStateId();
      const { data: sentData } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "Order", [{ name: "SentStatus" }], {
        select: (data) => {
          return (data?.Order?.SentStatus || []).flatMap((item) => {
            return [item?.code];
          });
        },
      });
      let completeStatusData = requestCriteria.body?.criteria?.completeStatus || [];
      if (completeStatusData?.length === 0 || (typeof completeStatusData === "object" && !Array.isArray(completeStatusData))) {
        completeStatusData = sentData;
      }
      const isCompleteStatus = Boolean(Object.keys(filterList?.completeStatus || {}).length);
      const isIssueDate = Boolean(Object.keys(filterList?.sortCaseListByDate || {}).length);
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria.body,
          criteria: {
            completeStatus: completeStatusData,
            ...filterList,
            orderType: filterList?.orderType ? [filterList?.orderType?.code] : [],
            applicationStatus: filterList?.applicationStatus?.code || "",
            ...(isCompleteStatus && { completeStatus: [filterList?.completeStatus?.code] }),
          },
          tenantId,
          pagination: {
            limit: requestCriteria?.state?.tableForm?.limit,
            offSet: requestCriteria?.state?.tableForm?.offset,
            ...(isIssueDate && filterList?.sortCaseListByDate),
          },
        },
        config: {
          ...requestCriteria?.config,
          select: (data) => {
            return { ...data, list: data?.list?.filter((order) => order.taskType) };
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      const caseDetails = handleTaskDetails(row?.taskDetails);
      switch (key) {
        case "CASE_NAME_ID":
          return `${row?.caseName}, ${value}`;
        case "STATUS":
          return t(value); // document status
        case "ISSUED":
          return `${formatDateDifference(value)} days ago`;
        case "ORDER_TYPE":
          return t(value);
        case "DELIEVERY_CHANNEL":
          return caseDetails?.deliveryChannels?.channelName || "N/A";
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
};
