import { ArrowDirection } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Link } from "react-router-dom";
import { Evidence } from "../components/Evidence";
import { OrderName } from "../components/OrderName";
import { OwnerColumn } from "../components/OwnerColumn";

const businessServiceMap = {
  "muster roll": "MR",
};

const inboxModuleNameMap = {
  "muster-roll-approval": "muster-roll-service",
};

const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);

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
  getAdvocateNameUsingBarRegistrationNumber: {
    getNames: () => {
      return {
        url: "/advocate/advocate/v1/status/_search",
        params: { status: "ACTIVE", tenantId: window?.Digit.ULBService.getStateId(), offset: 0, limit: 1000 },
        body: {
          tenantId: window?.Digit.ULBService.getStateId(),
        },
        config: {
          select: (data) => {
            return data.advocates.map((adv) => {
              return {
                icon: (
                  <span className="icon" style={{ display: "flex", justifyContent: "space-between" }}>
                    <span className="icon">{adv?.barRegistrationNumber}</span>
                    <span className="icon" style={{ justifyContent: "end" }}>
                      {adv?.additionalDetails?.username}
                    </span>
                  </span>
                ),
                barRegistrationNumber: `${adv?.barRegistrationNumber} (${adv?.additionalDetails?.username})`,
                advocateName: adv?.additionalDetails?.username,
                advocateId: adv?.id,
                barRegistrationNumberOriginal: adv?.barRegistrationNumber,
                data: adv,
              };
            });
          },
        },
      };
    },
  },
  registrationRequestsConfig: {
    customValidationCheck: (data) => {
      return !data?.applicationNumber.trim() ? { label: "Please enter a valid application Number", error: true } : false;
    },
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
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
            processSearchCriteria: {
              ...requestCriteria?.body?.inbox?.processSearchCriteria,
              tenantId: window?.Digit.ULBService.getStateId(),
            },
            tenantId: window?.Digit.ULBService.getStateId(),
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

      const today = new Date();
      const formattedToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());

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
          const formattedCreatedAt = new Date(createdAt.getFullYear(), createdAt.getMonth(), createdAt.getDate());
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
  scrutinyInboxConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          tenantId: window?.Digit.ULBService.getStateId(),
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
          },
        },
      ];
      if (additionalDetails in criteria[0] && !criteria[0][additionalDetails]) {
        criteria.splice(0, 1, {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          [additionalDetails]: "",
          tenantId: window?.Digit.ULBService.getStateId(),
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
          },
        });
      }
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
          criteria,
          tenantId: window?.Digit.ULBService.getStateId(),
          config: {
            ...requestCriteria?.config,
            select: (data) => {
              return { ...data, totalCount: data?.criteria?.[0]?.pagination?.totalCount };
            },
          },
        },
      };
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "Case ID") link = ``;
      });
      return link;
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      switch (key) {
        case "Stage":
          return <span>{t("UNDER_SCRUTINY")}</span>;
        case "Case Type":
          return <span>NIA S138</span>;
        case "Days Since Filing":
          const today = new Date();
          const formattedToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
          const datearr = value.split("-");
          const filedAt = new Date(datearr[2], datearr[1] - 1, datearr[0]);
          const formattedFiledAt = new Date(filedAt.getFullYear(), filedAt.getMonth(), filedAt.getDate());
          const diffInTime = formattedToday.getTime() - formattedFiledAt.getTime();
          const diffInDays = Math.ceil(diffInTime / (1000 * 3600 * 24));
          return <span>{diffInDays}</span>;
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
  paymentInboxConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
      const tenantId = window?.Digit.ULBService.getStateId();
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          tenantId,
          ...("sortBy" in additionalDetails && {
            [additionalDetails.sortBy]: undefined,
          }),
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
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
    additionalValidations: (type, data, keys) => {
      if (type === "date") {
        return data[keys.start] && data[keys.end] ? () => new Date(data[keys.start]).getTime() <= new Date(data[keys.end]).getTime() : true;
      }
    },
    MobileDetailsOnClick: (row, tenantId) => {
      let link;
      Object.keys(row).map((key) => {
        if (key === "Case ID") link = ``;
      });
      return link;
    },
    additionalCustomizations: (row, key, column, value, t, searchResult) => {
      switch (key) {
        case "Case ID":
          return (
            <span className="link">
              <Link to={`/digit-ui/employee/dristi/pending-payment-inbox/pending-payment-details?filingNumber=${value}`}>
                {String(value ? (column?.translate ? t(column?.prefix ? `${column?.prefix}${value}` : value) : value) : t("ES_COMMON_NA"))}
              </Link>
            </span>
          );
        case "Case Type":
          return <span>NIA S138</span>;
        case "Stage":
          return <span>E-filing</span>;
        case "Amount Due":
          return <span>Rs 2000</span>;
        case "Action":
          return (
            <span className="action-link">
              <Link
                style={{ display: "flex", alignItem: "center", color: "#9E400A" }}
                to={`/digit-ui/employee/dristi/pending-payment-inbox/pending-payment-details?caseId=${value}`}
              >
                {" "}
                <span style={{ display: "flex", alignItem: "center", textDecoration: "underline", color: "#9E400A" }}>
                  {t("CS_RECORD_PAYMENT")}
                </span>{" "}
                <ArrowDirection styles={{ height: "20px", width: "20px", fill: "#9E400A" }} />
              </Link>
            </span>
          );
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
  litigantInboxConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      // We need to change tenantId "processSearchCriteria" here
      const tenantId = window?.Digit.ULBService.getStateId();
      const criteria = [
        {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          tenantId,
          ...additionalDetails,
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
          },
        },
      ];
      if (additionalDetails?.searchKey in criteria[0] && !criteria[0][additionalDetails?.searchKey]) {
        criteria.splice(0, 1, {
          ...requestCriteria?.body?.criteria[0],
          ...requestCriteria?.state?.searchForm,
          [additionalDetails.searchKey]: "",
          ...additionalDetails,
          tenantId,
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
          },
        });
      }
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
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
        case "Case Type":
          return <span>NIA S138</span>;
        case "Stage":
          return t(row?.status);
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
  judgeInboxConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      const tenantId = window?.Digit.ULBService.getStateId();
      // We need to change tenantId "processSearchCriteria" here
      const criteria = requestCriteria?.body?.criteria?.map((item) => {
        return {
          ...item,
          ...requestCriteria?.state?.searchForm,
          tenantId,
          pagination: {
            limit: requestCriteria?.body?.inbox?.limit,
            offSet: requestCriteria?.body?.inbox?.offset,
          },
        };
      });

      return {
        ...requestCriteria,
        body: {
          ...requestCriteria?.body,
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
        case "Case Type":
          return <span>NIA S138</span>;
        case "Stage":
          return t(row?.status);
        default:
          return t("ES_COMMON_NA");
      }
    },
  },
  SearchIndividualConfig: {
    // preProcess: (requestCriteria, additionalDetails) => {

    //   return {
    //     ...requestCriteria,
    //     config: {
    //       select: (data) => {
    //         return { ...data };
    //       },
    //     },
    //   };
    // },
    additionalCustomizations: (row, key, column, value, t) => {
      const showDocument =
        userRoles?.includes("APPLICATION_APPROVER") ||
        userRoles?.includes("DEPOSITION_CREATOR") ||
        userRoles?.includes("DEPOSITION_ESIGN") ||
        userRoles?.includes("DEPOSITION_PUBLISHER") ||
        row.workflow.action !== "PENDINGREVIEW";
      switch (key) {
        case "Document":
          return showDocument ? <OwnerColumn rowData={row} colData={column} t={t} /> : "";
        case "File":
          return showDocument ? <Evidence rowData={row} colData={column} t={t} /> : "";
        case "Date Added":
          const date = new Date(value);
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
          const year = date.getFullYear();
          const formattedDate = `${day}-${month}-${year}`;
          return <span>{formattedDate}</span>;
        case "Parties":
          return (
            <span>{`${value
              .slice(0, 2)
              .map((party) => party.name)
              .join(",")}${value.length > 2 ? `+${value.length - 2}` : ""}`}</span>
          );
        case "Order Type":
          return <OrderName rowData={row} colData={column} value={value} />;
        case "Submission Name":
          return <OwnerColumn rowData={row} colData={column} t={t} value={value} showAsHeading={true} />;
        case "Document Type":
          return <Evidence rowData={row} colData={column} t={t} value={value} showAsHeading={true} />;
        default:
          break;
      }
    },
  },
  PartiesConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      return {
        ...requestCriteria,
        config: {
          ...requestCriteria.config,
          select: (data) => {
            const litigants = data.criteria[0].responseList[0].litigants?.length > 0 ? data.criteria[0].responseList[0].litigants : [];
            const finalLitigantsData = litigants.map((litigant) => {
              return {
                ...litigant,
                name: litigant.additionalDetails?.fullName,
              };
            });
            const reps = data.criteria[0].responseList[0].representatives?.length > 0 ? data.criteria[0].responseList[0].representatives : [];
            const finalRepresentativesData = reps.map((rep) => {
              return {
                ...rep,
                name: rep.additionalDetails?.advocateName,
                partyType: `Advocate (for ${rep.representing.map((client) => client?.additionalDetails?.fullName).join(", ")})`,
              };
            });
            return {
              ...data,
              criteria: {
                ...data.criteria[0],
                responseList: {
                  ...data.criteria[0].responseList[0],
                  parties: [...finalLitigantsData, ...finalRepresentativesData],
                },
              },
            };
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t) => {
      switch (key) {
        // case "Document":
        //   return <OwnerColumn name={row?.name?.familyName} t={t} />;
        case "Date Added":
          const date = new Date(value);
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
          const year = date.getFullYear();
          const formattedDate = `${day}-${month}-${year}`;
          return <span>{formattedDate}</span>;
        default:
          break;
      }
    },
  },
  patternValidation: (key) => {
    switch (key) {
      case "contact":
        return /^[6-9]\d{9}$/;
      case "email":
        return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      case "userName":
        return /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,100}$/i;
      default:
        return;
    }
  },
  maxDateValidation: (key) => {
    switch (key) {
      case "date":
        return new Date().toISOString().split("T")[0];
      default:
        return;
    }
  },
};

const CommentComponent = ({ key, comment }) => {
  return (
    <div className="comment-body" key={key}>
      <div className="name-logo">
        <div className="comment-avatar">
          <span>{comment?.author[0]}</span>
        </div>
      </div>
      <div className="comment-details">
        <h3 className="comment-header">
          {comment?.author} <span className="times-stamp">{comment?.timestamp} </span>
        </h3>
        <p className="comment-text">{comment?.text}</p>
      </div>
    </div>
  );
};
