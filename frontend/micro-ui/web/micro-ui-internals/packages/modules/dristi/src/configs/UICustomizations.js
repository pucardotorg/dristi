import { ArrowDirection } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Link } from "react-router-dom";
import { Evidence } from "../components/Evidence";
import { OrderName } from "../components/OrderName";
import { OwnerColumn } from "../components/OwnerColumn";
import { RenderInstance } from "../components/RenderInstance";
import OverlayDropdown from "../components/OverlayDropdown";
import CustomChip from "../components/CustomChip";
import ReactTooltip from "react-tooltip";

const businessServiceMap = {
  "muster roll": "MR",
};

const inboxModuleNameMap = {
  "muster-roll-approval": "muster-roll-service",
};

const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);

const partyTypes = {
  "complainant.primary": "Complainant",
  "complainant.additional": "Complainant",
  "respondent.primary": "Respondent",
  "respondent.additional": "Respondent",
};

export const UICustomizations = {
  businessServiceMap,
  updatePayload: (applicationDetails, data, action, businessService) => {
    if (businessService === businessServiceMap.estimate) {
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
        action: action?.action,
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
        action: action?.action,
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
        action: action?.action,
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
        action: action?.action,
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
    if (businessService === businessServiceMap?.["muster roll"] && action?.action === "APPROVE") {
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
                advocateUuid: adv?.auditDetails?.createdBy,
              };
            });
          },
        },
      };
    },
  },
  getAdvocateNameUsingBarRegistrationNumberJoinCase: {
    getNames: (props) => {
      const removeOptions = props?.removeOptions ? props?.removeOptions : [];
      const removeOptionsKey = props?.removeOptionsKey || "";
      return {
        url: "/advocate/advocate/v1/status/_search",
        params: { status: "ACTIVE", tenantId: window?.Digit.ULBService.getStateId(), offset: 0, limit: 1000 },
        body: {
          tenantId: window?.Digit.ULBService.getStateId(),
        },
        config: {
          select: (data) => {
            return data.advocates
              .filter((adv) => !removeOptions?.includes(adv?.[removeOptionsKey]))
              .map((adv) => {
                return {
                  icon: (
                    <span className="icon" style={{ display: "flex", justifyContent: "space-between" }}>
                      <span className="icon">{adv?.barRegistrationNumber}</span>
                      <span className="icon" style={{ justifyContent: "end" }}>
                        {adv?.additionalDetails?.username}
                      </span>
                    </span>
                  ),
                  barRegistrationNumber: `${adv?.barRegistrationNumber}`,
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
      return !data?.applicationNumber_WILDCARD.trim() ? { label: "Please enter a valid application Number", error: true } : false;
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
            <Link
              to={`/digit-ui/employee/dristi/registration-requests/details?applicationNo=${applicationNumber}&individualId=${value}&type=${usertype}`}
            >
              <span className="action-link"> {t("Verify")}</span>
            </Link>
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
      const moduleSearchCriteria = {
        ...requestCriteria?.body?.inbox?.moduleSearchCriteria,
        ...requestCriteria?.state?.searchForm,
        tenantId: tenantId,
      };

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
              tenantId: tenantId,
            },
            limit: requestCriteria?.state?.tableForm?.limit,
            offset: requestCriteria?.state?.tableForm?.offset,
            tenantId: tenantId,
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
      const caseId = row?.businessObject?.billDetails?.caseTitleFilingNumber.split(",")[1];
      const consumerCode = row?.businessObject?.billDetails?.consumerCode;
      const service = row?.businessObject?.billDetails?.service;
      const billStatus = row?.businessObject?.billDetails?.billStatus;
      const paymentType = row?.businessObject?.billDetails?.paymentType;
      switch (key) {
        case "Case Name & ID":
          return billStatus === "ACTIVE" ? (
            <span className="link">
              <Link
                to={`/digit-ui/employee/dristi/pending-payment-inbox/pending-payment-details?filingNumber=${caseId}&businessService=${service}&consumerCode=${consumerCode}&paymentType=${paymentType}`}
              >
                {String(value || t("ES_COMMON_NA"))}
              </Link>
            </span>
          ) : (
            billStatus === "PAID" && <span>{String(value || t("ES_COMMON_NA"))}</span>
          );
        case "Amount Due":
          return <span>{`Rs. ${value}/-`}</span>;
        case "Action":
          return billStatus === "ACTIVE" ? (
            <span className="action-link">
              <Link
                style={{ display: "flex", alignItem: "center", color: "#9E400A" }}
                to={`/digit-ui/employee/dristi/pending-payment-inbox/pending-payment-details?filingNumber=${caseId}&businessService=${service}&consumerCode=${consumerCode}&paymentType=${paymentType}`}
              >
                {" "}
                <span style={{ display: "flex", alignItem: "center", textDecoration: "underline", color: "#9E400A" }}>
                  {t("CS_RECORD_PAYMENT")}
                </span>{" "}
                <ArrowDirection styles={{ height: "20px", width: "20px", fill: "#9E400A" }} />
              </Link>
            </span>
          ) : (
            billStatus === "PAID" && (
              <span
                style={{
                  padding: "5px 10px",
                  borderRadius: "15px",
                  display: "inline-block",
                  fontSize: "0.9rem",
                  textAlign: "center",
                  backgroundColor: "rgb(228, 242, 228)",
                  color: "rgb(0, 112, 60)",
                }}
              >
                {String(t("CS_AMOUNT_PAID") || t("ES_COMMON_NA"))}
              </span>
            )
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
    preProcess: (requestCriteria, additionalDetails) => {
      const filterList = Object.keys(requestCriteria.state.searchForm)
        .map((key) => {
          if (requestCriteria.state.searchForm[key]?.type) {
            return { [key]: requestCriteria.state.searchForm[key]?.type };
          } else if (requestCriteria.state.searchForm[key]?.value) {
            return { [key]: requestCriteria.state.searchForm[key]?.value };
          } else if (typeof requestCriteria.state.searchForm[key] === "string") {
            return { [key]: requestCriteria.state.searchForm[key] };
          }
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
      const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
      const status = !filterList?.status || filterList?.status === "PUBLISHED" ? "PUBLISHED" : "EMPTY";
      return {
        ...requestCriteria,
        body: {
          ...requestCriteria.body,
          criteria: {
            ...requestCriteria.body.criteria,
            ...filterList,
            status: userRoles.includes("CITIZEN") && requestCriteria.url.split("/").includes("order") ? status : filterList?.status,
          },
          tenantId,
          pagination: {
            limit: requestCriteria?.state?.tableForm?.limit,
            offSet: requestCriteria?.state?.tableForm?.offset,
          },
        },
        config: {
          ...requestCriteria.config,
          select: (data) => {
            // if (requestCriteria.url.split("/").includes("order")) {
            return userRoles.includes("CITIZEN") && requestCriteria.url.split("/").includes("order")
              ? { ...data, list: data.list?.filter((order) => order.status !== "DRAFT_IN_PROGRESS") }
              : userRoles.includes("JUDGE_ROLE") && requestCriteria.url.split("/").includes("application")
              ? { ...data, applicationList: data.applicationList?.filter((application) => application.status != "PENDINGPAYMENT") }
              : data;
            // }
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t) => {
      const showDocument =
        userRoles?.includes("APPLICATION_APPROVER") ||
        userRoles?.includes("DEPOSITION_CREATOR") ||
        userRoles?.includes("DEPOSITION_ESIGN") ||
        userRoles?.includes("DEPOSITION_PUBLISHER") ||
        row.workflow?.action !== "PENDINGREVIEW";
      switch (key) {
        case "Document":
          return showDocument ? <OwnerColumn rowData={row} colData={column} t={t} /> : "";
        case "File":
          return showDocument ? <Evidence userRoles={userRoles} rowData={row} colData={column} t={t} /> : "";
        case "Date Added":
        case "Date":
          const date = new Date(value);
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
          const year = date.getFullYear();
          const formattedDate = `${day}-${month}-${year}`;
          return <span>{formattedDate}</span>;
        case "Parties":
          return (
            <div>
              {value.length > 2 && <ReactTooltip id={`hearing-list`}>{value.map((party) => party.name).join(", ")}</ReactTooltip>}
              <span data-tip data-for={`hearing-list`}>{`${value
                .slice(0, 2)
                .map((party) => party.name)
                .join(", ")}${value.length > 2 ? `+${value.length - 2}` : ""}`}</span>
            </div>
          );
        case "Order Type":
          return <OrderName rowData={row} colData={column} value={value} />;
        case "Submission Type":
          return <OwnerColumn rowData={row} colData={column} t={t} value={value} showAsHeading={true} />;
        case "Document Type":
          return <Evidence userRoles={userRoles} rowData={row} colData={column} t={t} value={value} showAsHeading={true} />;
        case "Hearing Type":
        case "Source":
        case "Status":
          //Need to change the shade as per the value
          return <CustomChip text={t(value)} shade={value === "PUBLISHED" ? "green" : "orange"} />;
        case "Actions":
          return (
            <OverlayDropdown style={{ position: "relative" }} column={column} row={row} master="commonUiConfig" module="SearchIndividualConfig" />
          );
        default:
          break;
      }
    },
    dropDownItems: (row) => {
      const formatDate = (date) => {
        const day = String(date.getDate()).padStart(2, "0");
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
      };
      const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
      const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};
      const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
      const date = new Date(row.startTime);
      const future = row.startTime > Date.now();
      if (row.status === "SCHEDULED" && userInfo.roles.map((role) => role.code).includes("JUDGE_ROLE")) {
        return [
          {
            label: "Reschedule hearing",
            id: "reschedule",
            action: (history) => {
              const requestBody = {
                order: {
                  createdDate: new Date().getTime(),
                  tenantId: row.tenantId,
                  hearingNumber: row?.hearingId,
                  filingNumber: row.filingNumber[0],
                  cnrNumber: row.cnrNumbers[0],
                  statuteSection: {
                    tenantId: row.tenantId,
                  },
                  orderType: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                  status: "",
                  isActive: true,
                  workflow: {
                    action: OrderWorkflowAction.SAVE_DRAFT,
                    comments: "Creating order",
                    assignes: null,
                    rating: null,
                    documents: [{}],
                  },
                  documents: [],
                  additionalDetails: {
                    formdata: {
                      orderType: {
                        type: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                        isactive: true,
                        code: "INITIATING_RESCHEDULING_OF_HEARING_DATE",
                        name: "ORDER_TYPE_INITIATING_RESCHEDULING_OF_HEARING_DATE",
                      },
                      originalHearingDate: `${date.getFullYear()}-${date.getMonth() < 9 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-${
                        date.getDate() < 10 ? `0${date.getDate()}` : date.getDate()
                      }`,
                    },
                  },
                },
              };
              ordersService
                .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
                .then((res) => {
                  history.push(
                    `/${window.contextPath}/employee/orders/generate-orders?filingNumber=${row.filingNumber[0]}&orderNumber=${res.order.orderNumber}`,
                    {
                      caseId: row.caseId,
                      tab: "Orders",
                    }
                  );
                })
                .catch((err) => {});
            },
          },
          {
            label: "View transcript",
            id: "view_transcript",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View witness deposition",
            id: "view_witness",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View pending task",
            id: "view_pending_tasks",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
        ];
      }
      if (row.status === "SCHEDULED" && userInfo?.type === "CITIZEN") {
        return [
          {
            label: "Request for Reschedule hearing",
            id: "reschedule",
            action: (history) => {
              history.push(`/digit-ui/citizen/submissions/submissions-create?filingNumber=${row.filingNumber[0]}&hearingId=${row.hearingId}`);
            },
          },
          {
            label: "View transcript",
            id: "view_transcript",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View witness deposition",
            id: "view_witness",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
          {
            label: "View pending task",
            id: "view_pending_tasks",
            hide: true,
            action: (history) => {
              alert("Not Yet Implemented");
            },
          },
        ];
      }

      return [
        {
          label: "View transcript",
          id: "view_transcript",
          hide: false,
          disabled: false,
          action: (history, column, row) => {
            column.clickFunc(row);
          },
        },
        {
          label: "View witness deposition",
          id: "view_witness",
          hide: false,
          disabled: true,
          action: (history) => {
            alert("Not Yet Implemented");
          },
        },
        {
          label: "View pending task",
          id: "view_pending_tasks",
          hide: true,
          disabled: true,
          action: (history) => {
            alert("Not Yet Implemented");
          },
        },
      ];
    },
  },
  HistoryConfig: {
    preProcess: (requestCriteria, additionalDetails) => {
      return {
        ...requestCriteria,
        config: {
          ...requestCriteria.config,
          select: (data) => {
            const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
            if (data.caseFiles.length) {
              const applicationHistory = data.caseFiles[0]?.applications.map((application) => {
                return {
                  instance: `APPLICATION_TYPE_${application.applicationType}`,
                  date: application.auditDetails.createdTime,
                  status: application.status,
                };
              });
              const evidenceHistory = data.caseFiles[0]?.evidence.map((evidence) => {
                return {
                  instance: evidence.artifactType,
                  date: evidence.auditdetails.createdTime,
                  status: evidence.status,
                };
              });
              const hearingHistory = data.caseFiles[0]?.hearings.map((hearing) => {
                return { instance: `HEARING_TYPE_${hearing.hearingType}`, stage: [], date: hearing.startTime, status: hearing.status };
              });
              const orderHistory = userRoles.includes("CITIZEN")
                ? data.caseFiles[0]?.orders
                    ?.filter((order) => order.order.status !== "DRAFT_IN_PROGRESS")
                    .map((order) => {
                      return {
                        instance: `ORDER_TYPE_${order.order.orderType.toUpperCase()}`,
                        stage: [],
                        date: order.order.auditDetails.createdTime,
                        status: order.order.status,
                      };
                    })
                : data.caseFiles[0]?.orders.map((order) => {
                    return {
                      instance: `ORDER_TYPE_${order.order.orderType.toUpperCase()}`,
                      stage: [],
                      date: order.order.auditDetails.createdTime,
                      status: order.order.status,
                    };
                  });
              const historyList = [...hearingHistory, ...applicationHistory, ...orderHistory, ...evidenceHistory];
              return { ...data, history: historyList };
            } else {
              return { ...data, history: [] };
            }
          },
        },
      };
    },
    additionalCustomizations: (row, key, column, value, t) => {
      switch (key) {
        case "Instance":
          return <RenderInstance value={value} t={t} />;
        case "Date":
          const date = new Date(value);
          const day = date.getDate().toString().padStart(2, "0");
          const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Month is zero-based
          const year = date.getFullYear();
          const formattedDate = `${day}-${month}-${year}`;
          return <span>{formattedDate}</span>;
        case "Status":
          return t(value);
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
        case "Party Type":
          return partyTypes[value] ? partyTypes[value] : value;
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
        return /^[^{0-9}^\$\"<>?\\\\~!@#$%^()+={}\[\]*,/_:;“”‘’]{1,}$/i;
      case "address":
        return /^[^\$\"<>?\\\\~`!@$%^()={}\[\]*:;“”‘’]{2,256}$/i;
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
  DristiCaseUtils: {
    getAllCaseRepresentativesUUID: (caseData) => {
      let representatives = {};
      let list = [];
      caseData?.litigants?.forEach((litigant) => {
        list = caseData?.representatives
          ?.filter((item) => {
            return item?.representing?.some((lit) => lit?.individualId === litigant?.individualId) && item?.additionalDetails?.uuid;
          })
          .map((item) => item?.additionalDetails?.uuid);
        if (list?.length > 0) {
          representatives[litigant?.additionalDetails?.uuid] = list;
        } else {
          representatives[litigant?.additionalDetails?.uuid] = [litigant?.additionalDetails?.uuid];
        }
      });
      return representatives;
    },
  },
};
