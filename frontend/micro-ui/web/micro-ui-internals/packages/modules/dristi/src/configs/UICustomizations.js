import React from "react";
import { Link } from "react-router-dom";

const businessServiceMap = {
  "muster roll": "MR",
};

const inboxModuleNameMap = {
  "muster-roll-approval": "muster-roll-service",
};

export const UICustomizations = {
  businessServiceMap,
  updatePayload: (applicationDetails, data, action, businessService) => {
    if (businessService === businessServiceMap.estimate) {
      const workflow = {
        comment: data.comments,
        documents: data?.documents?.map((document) => {
          return {
            documentType: action?.action + " DOC",
            fileName: document?.[1]?.file?.name,
            fileStoreId: document?.[1]?.fileStoreId?.fileStoreId,
            documentUid: document?.[1]?.fileStoreId?.fileStoreId,
            tenantId: document?.[1]?.fileStoreId?.tenantId,
          };
        }),
        assignees: data?.assignees?.uuid ? [data?.assignees?.uuid] : null,
        action: action.action,
      };
      //filtering out the data
      Object.keys(workflow).forEach((key, index) => {
        if (!workflow[key] || workflow[key]?.length === 0) delete workflow[key];
      });

      return {
        estimate: applicationDetails,
        workflow,
      };
    }
    if (businessService === businessServiceMap.contract) {
      const workflow = {
        comment: data?.comments,
        documents: data?.documents?.map((document) => {
          return {
            documentType: action?.action + " DOC",
            fileName: document?.[1]?.file?.name,
            fileStoreId: document?.[1]?.fileStoreId?.fileStoreId,
            documentUid: document?.[1]?.fileStoreId?.fileStoreId,
            tenantId: document?.[1]?.fileStoreId?.tenantId,
          };
        }),
        assignees: data?.assignees?.uuid ? [data?.assignees?.uuid] : null,
        action: action.action,
      };
      //filtering out the data
      Object.keys(workflow).forEach((key, index) => {
        if (!workflow[key] || workflow[key]?.length === 0) delete workflow[key];
      });

      return {
        contract: applicationDetails,
        workflow,
      };
    }
    if (businessService === businessServiceMap?.["muster roll"]) {
      const workflow = {
        comment: data?.comments,
        documents: data?.documents?.map((document) => {
          return {
            documentType: action?.action + " DOC",
            fileName: document?.[1]?.file?.name,
            fileStoreId: document?.[1]?.fileStoreId?.fileStoreId,
            documentUid: document?.[1]?.fileStoreId?.fileStoreId,
            tenantId: document?.[1]?.fileStoreId?.tenantId,
          };
        }),
        assignees: data?.assignees?.uuid ? [data?.assignees?.uuid] : null,
        action: action.action,
      };
      //filtering out the data
      Object.keys(workflow).forEach((key, index) => {
        if (!workflow[key] || workflow[key]?.length === 0) delete workflow[key];
      });

      return {
        musterRoll: applicationDetails,
        workflow,
      };
    }
    if (businessService === businessServiceMap?.["works.purchase"]) {
      const workflow = {
        comment: data.comments,
        documents: data?.documents?.map((document) => {
          return {
            documentType: action?.action + " DOC",
            fileName: document?.[1]?.file?.name,
            fileStoreId: document?.[1]?.fileStoreId?.fileStoreId,
            documentUid: document?.[1]?.fileStoreId?.fileStoreId,
            tenantId: document?.[1]?.fileStoreId?.tenantId,
          };
        }),
        assignees: data?.assignees?.uuid ? [data?.assignees?.uuid] : null,
        action: action.action,
      };
      //filtering out the data
      Object.keys(workflow).forEach((key, index) => {
        if (!workflow[key] || workflow[key]?.length === 0) delete workflow[key];
      });

      const additionalFieldsToSet = {
        projectId: applicationDetails.additionalDetails.projectId,
        invoiceDate: applicationDetails.billDate,
        invoiceNumber: applicationDetails.referenceId.split("_")?.[1],
        contractNumber: applicationDetails.referenceId.split("_")?.[0],
        documents: applicationDetails.additionalDetails.documents,
      };
      return {
        bill: { ...applicationDetails, ...additionalFieldsToSet },
        workflow,
      };
    }
  },
  enableModalSubmit: (businessService, action, setModalSubmit, data) => {
    if (businessService === businessServiceMap?.["muster roll"] && action.action === "APPROVE") {
      setModalSubmit(data?.acceptTerms);
    }
  },
  getBusinessService: (moduleCode) => {
    if (moduleCode?.includes("estimate")) {
      return businessServiceMap?.estimate;
    } else if (moduleCode?.includes("contract")) {
      return businessServiceMap?.contract;
    } else if (moduleCode?.includes("muster roll")) {
      return businessServiceMap?.["muster roll"];
    } else if (moduleCode?.includes("works.purchase")) {
      return businessServiceMap?.["works.purchase"];
    } else if (moduleCode?.includes("works.wages")) {
      return businessServiceMap?.["works.wages"];
    } else if (moduleCode?.includes("works.supervision")) {
      return businessServiceMap?.["works.supervision"];
    } else {
      return businessServiceMap;
    }
  },
  getInboxModuleName: (moduleCode) => {
    if (moduleCode?.includes("estimate")) {
      return inboxModuleNameMap?.estimate;
    } else if (moduleCode?.includes("contract")) {
      return inboxModuleNameMap?.contracts;
    } else if (moduleCode?.includes("attendence")) {
      return inboxModuleNameMap?.attendencemgmt;
    } else {
      return inboxModuleNameMap;
    }
  },
  registrationRequestsConfig: {
    customValidationCheck: (data) => {
      return !data?.applicationNumber.trim() ? { label: "Please enter a valid application Number", error: true } : false;
    },
    preProcess: (requestCriteria, additionalDetails) => {
      const moduleSearchCriteria = {
        ...requestCriteria?.body?.inbox?.moduleSearchCriteria,
        ...requestCriteria?.state?.searchForm,
        tenantId: window?.Digit.ULBService.getStateId(),
      };
      if (additionalDetails in moduleSearchCriteria && !moduleSearchCriteria[additionalDetails]) {
        delete moduleSearchCriteria[additionalDetails];
      }
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          inbox: {
            ...requestCriteria?.body?.inbox,
            moduleSearchCriteria: {
              ...moduleSearchCriteria,
            },
          },
        },
      };
    },
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "Application No") link = ``;
      });
      return link;
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      const usertype = row?.ProcessInstance?.businessService === "advocateclerk" ? "clerk" : "advocate";
      const individualId = row?.businessObject?.individual?.individualId;
      const applicationNumber =
        row?.businessObject?.advocateDetails?.applicationNumber || row?.businessObject?.clerkDetails?.applicationNumber || row?.applicationNumber;
      switch (key) {
        case "Application No":
          return (
            <span className="link">
              <Link
                to={`/digit-ui/employee/dristi/registration-requests/details?applicationNo=${value}&individualId=${individualId}&type=${usertype}`}
              >
                {String(value ? (column?.translate ? t(column?.prefix ? `${column?.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
              </Link>
            </span>
          );
        case "Action":
          return (
            <span className="action-link">
              <Link
                to={`/digit-ui/employee/dristi/registration-requests/details?applicationNo=${applicationNumber}&individualId=${value}&type=${usertype}`}
              >
                {" "}
                {t("Verify")}
              </Link>
            </span>
          );
        case "User Type":
          return usertype === "clerk" ? "Advocate Clerk" : "Advocate";
        case "Date Created":
          const date = new Date(value);
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
          const year = date.getFullYear();
          const formattedDate = `${day}-${month}-${year}`;
          return <span>{formattedDate}</span>;
        case "Due Since (no of days)":
          const createdAt = new Date(row?.businessObject?.auditDetails?.createdTime);
          const today = new Date();
          const formattedCreatedAt = new Date(createdAt.getFullYear(), createdAt.getMonth(), createdAt.getDate());
          const formattedToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
          const differenceInTime = formattedToday.getTime() - formattedCreatedAt.getTime();
          const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));
          return <span>{differenceInDays}</span>;
        case "User Name":
          const displayName = `${value?.givenName || ""} ${value?.familyName || ""} ${value?.otherNames || ""}`;
          return displayName;
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
};
