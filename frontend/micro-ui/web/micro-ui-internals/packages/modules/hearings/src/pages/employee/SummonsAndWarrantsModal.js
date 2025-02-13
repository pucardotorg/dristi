import React, { useState, useEffect, useMemo } from "react";
import { useHistory } from "react-router-dom";
import { Modal, CloseSvg, Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { summonsConfig } from "../../configs/SummonsNWarrantConfig";
import useSearchOrdersService from "../../../../orders/src/hooks/orders/useSearchOrdersService";
import { formatDate } from "../../utils";
import { hearingService } from "../../hooks/services";
import { Urls } from "../../hooks/services/Urls";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";

const modalPopup = {
  height: "70%",
  minHeight: "40rem",
  width: "50%",
  minWidth: "40rem",
  position: "absolute",
  bottom: "50%",
  right: "50%",
  transform: "translate(50%, 50%)",
  borderRadius: "0.3rem",
  display: "inline-block",
  // height: "calc(100% - 64px)"
};

const actionButtonStyle = {
  position: "fixed",
  marginBottom: "0px",
  bottom: "0px",
  right: "21px",
  width: "calc(100% - 21px)",
  backgroundColor: "white",
  paddingBottom: "16px",
};

const headingStyle = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 700,
  lineHeight: "18.75px",
  textAlign: "center",
};

const ModalHeading = ({ label, orderList }) => {
  return (
    <h1 className="modal-heading" style={{ padding: 8 }}>
      <span className="heading-m">{label}</span>
      {orderList && orderList.length > 1 ? (
        <span className="heading-xs">Failed {orderList.length - 1} times</span>
      ) : (
        <span className="heading-xs">No previous failed attempts</span>
      )}{" "}
    </h1>
  );
};

function groupOrdersByParty(filteredOrders) {
  const accusedWiseOrdersMap = new Map();

  filteredOrders.forEach((order) => {
    const party = order.orderDetails?.parties?.[0];
    if (!party) return;

    let partyName = party.partyName.trim();
    let partyType = party.partyType.toLowerCase();
    if (partyType === "respondent") {
      partyType = "Accused";
    }
    if (partyType === "witness") {
      partyType = "Witness";
    }

    if (!accusedWiseOrdersMap.has(partyName)) {
      accusedWiseOrdersMap.set(partyName, { partyType, partyName, ordersList: [] });
    }

    accusedWiseOrdersMap.get(partyName).ordersList.push(order);
  });

  const accusedWiseOrdersList = Array.from(accusedWiseOrdersMap.values());

  // Sort first by partyType: "respondent", then "witness"
  accusedWiseOrdersList.sort((a, b) => {
    if (a.partyType === "Accused" && b.partyType !== "Accused") return -1;
    if (a.partyType !== "Accused" && b.partyType === "Accused") return 1;
    return 0;
  });

  accusedWiseOrdersList.forEach((party) => {
    party.ordersList.sort((a, b) => b.auditDetails.createdTime - a.auditDetails.createdTime);
  });

  return accusedWiseOrdersList;
}

const SummonsAndWarrantsModal = ({ handleClose }) => {
  const history = useHistory();
  const { t } = useTranslation();
  const { filingNumber, hearingId, taskOrderType } = Digit.Hooks.useQueryParams();
  const { state } = useLocation();
  const partyIndex = state?.state?.params?.partyIndex;
  const taskCnrNumber = state?.state?.params?.taskCnrNumber;
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [orderNumber, setOrderNumber] = useState(null);
  const [orderId, setOrderId] = useState(null);
  const [orderType, setOrderType] = useState(null);
  const [orderLoading, setOrderLoading] = useState(false);
  const userType = Digit.UserService.getType();
  const { data: caseData } = Digit.Hooks.dristi.useSearchCaseService(
    {
      criteria: [
        {
          filingNumber: filingNumber,
        },
      ],
      tenantId,
    },
    {},
    `dristi-${filingNumber}`,
    filingNumber,
    Boolean(filingNumber)
  );

  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    {
      hearing: { tenantId },
      criteria: {
        tenantID: tenantId,
        filingNumber: filingNumber,
        hearingId: hearingId,
      },
    },
    { applicationNumber: "", cnrNumber: "" },
    hearingId,
    Boolean(hearingId)
  );

  const hearingDetails = useMemo(() => hearingsData?.HearingList?.[0], [hearingsData]);

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const isCaseAdmitted = useMemo(() => caseDetails?.status === "CASE_ADMITTED", [caseDetails]);

  const { caseId, cnrNumber } = useMemo(() => ({ cnrNumber: caseDetails.cnrNumber || "", caseId: caseDetails?.id }), [caseDetails]);

  const handleCloseModal = () => {
    if (handleClose) {
      handleClose();
    } else history.goBack();
  };

  const handleNavigate = () => {
    const contextPath = window?.contextPath || "";
    history.push(
      `/${contextPath}/employee/home/home-pending-task/reissue-summons-modal?filingNumber=${filingNumber}&hearingId=${hearingId}&cnrNumber=${cnrNumber}&orderType=${orderType}`
    );
  };

  const handleIssueWarrant = async ({ cnrNumber, filingNumber, orderType, hearingId }) => {
    let reqBody = {
      order: {
        createdDate: null,
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        hearingNumber: hearingId,
        statuteSection: {
          tenantId,
        },
        orderType: orderType,
        status: "",
        isActive: true,
        workflow: {
          action: "SAVE_DRAFT",
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {
          hearingId: hearingId,
          formdata: {
            orderType: {
              code: orderType,
              type: orderType,
              name: `ORDER_TYPE_${orderType}`,
            },
            dateOfHearing: formatDate(new Date(hearingDetails?.startTime)),
            warrantFor: respondentName,
          },
        },
      },
    };
    try {
      const res = await hearingService.customApiService(Urls.order.createOrder, reqBody, { tenantId });
      hearingService.customApiService(Urls.pendingTask, {
        pendingTask: {
          name: "Order Created",
          entityType: "order-default",
          referenceId: `MANUAL_${res.order.orderNumber}`,
          status: "DRAFT_IN_PROGRESS",
          assignedTo: [],
          assignedRole: ["JUDGE_ROLE"],
          cnrNumber: null,
          filingNumber: filingNumber,
          isCompleted: true,
          stateSla: null,
          additionalDetails: {},
          tenantId,
        },
      });
      history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${res.order.orderNumber}`);
    } catch (error) {}
  };

  const { data: ordersData } = useSearchOrdersService(
    { criteria: { tenantId: tenantId, filingNumber } },
    { tenantId },
    filingNumber,
    Boolean(filingNumber)
  );

  const [orderList, setOrderList] = useState([]);

  const orderListFiltered = useMemo(() => {
    if (!ordersData?.list) return [];

    const filteredOrders = ordersData?.list?.filter(
      (item) =>
        (taskOrderType === "NOTICE" ? item.orderType === "NOTICE" : item.orderType === "SUMMONS" || item.orderType === "WARRANT") &&
        item?.status === "PUBLISHED" &&
        item?.hearingNumber === hearingId &&
        item?.additionalDetails?.formdata?.noticeOrder?.party?.data?.partyIndex === partyIndex
    );

    // make orders list by partyTypes Accused and Witness.
    const accusedWiseOrdersList = groupOrdersByParty(filteredOrders);

    return accusedWiseOrdersList;
  }, [hearingId, ordersData?.list, partyIndex, taskOrderType, ordersData]);

  const [activeIndex, setActiveIndex] = useState({ partyIndex: 0, orderIndex: 0 });
  useEffect(() => {
    setOrderList(orderListFiltered?.[0]?.ordersList || []);
    setOrderNumber(orderListFiltered?.[0]?.ordersList?.[0]?.orderNumber);
    setOrderType(orderListFiltered?.[0]?.ordersList?.[0]?.orderType);
    setOrderId(orderListFiltered?.[0]?.ordersList?.[0]?.id);
  }, [orderListFiltered]);

  const config = useMemo(() => summonsConfig({ filingNumber, orderNumber, orderId, orderType, taskCnrNumber }), [
    taskCnrNumber,
    filingNumber,
    orderId,
    orderNumber,
    orderType,
  ]);

  const getOrderPartyData = (orderType, orderList) => {
    return orderList?.find((item) => orderType === item?.orderType)?.orderDetails?.parties;
  };

  const { respondentName, partyType } = useMemo(() => {
    const partyData = getOrderPartyData(orderType, orderList);
    const respondentName = partyData?.[0]?.partyName || "Unknown";
    const partyType = partyData?.[0]?.partyType || "Respondent";
    return { respondentName, partyType };
  }, [orderList, orderType]);

  const CloseButton = (props) => {
    return (
      <div onClick={props?.onClick} className="header-bar-end">
        <CloseSvg />
      </div>
    );
  };

  const totalSummons = useMemo(() => {
    return (orderList || [])?.filter((order) => order?.orderType === "SUMMONS")?.length;
  }, [orderList]);

  const totalWarrants = useMemo(() => {
    return (orderList || [])?.filter((order) => order?.orderType === "WARRANT")?.length;
  }, [orderList]);

  const totalNotices = useMemo(() => {
    return (orderList || [])?.filter((order) => order?.orderType === "NOTICE")?.length;
  }, [orderList]);

  const lastSummon = useMemo(() => {
    return orderList?.find((order) => order?.orderType === "SUMMONS") || null;
  }, [orderList]);

  const lastWarrant = useMemo(() => {
    return orderList?.find((order) => order?.orderType === "WARRANT") || null;
  }, [orderList]);

  const lastNotice = useMemo(() => {
    return orderList?.find((order) => order?.orderType === "NOTICE") || null;
  }, [orderList]);

  const caseInfo = useMemo(() => {
    return (
      <div className="case-info">
        <div className="case-info-column">
          <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
            <span style={{ minWidth: "40%" }}>{t("Case Name & ID")}</span>
            <span>
              {caseDetails?.caseTitle}, {filingNumber}
            </span>
          </div>
          <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
            <span style={{ minWidth: "40%" }}>{t("Issued to")}</span>
            <span>{respondentName}</span>
          </div>
          <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
            <span style={{ minWidth: "40%" }}>{t("Next Hearing Date")}</span>
            <span>{hearingDetails?.startTime && formatDate(new Date(hearingDetails?.startTime), "DD-MM-YYYY")}</span>
          </div>
          {totalSummons > 0 && (
            <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
              <span style={{ minWidth: "40%" }}>{t("Last Summon issued on")}</span>
              <span>
                {lastSummon.createdDate && formatDate(new Date(lastSummon?.createdDate), "DD-MM-YYYY")} (Round {totalSummons})
              </span>
            </div>
          )}
          {totalWarrants > 0 && (
            <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
              <span style={{ minWidth: "40%" }}>{t("Last Warrant issued on")}</span>
              <span>
                {lastWarrant?.createdDate && formatDate(new Date(lastWarrant?.createdDate), "DD-MM-YYYY")} (Round {totalWarrants})
              </span>
            </div>
          )}
          {totalNotices > 0 && (
            <div className="case-info-row" style={{ display: "flex", flexDirection: "row", gap: "20px" }}>
              <span style={{ minWidth: "40%" }}>{t("Last Notice issued on")}</span>
              <span>
                {lastNotice?.createdDate && formatDate(new Date(lastNotice?.createdDate), "DD-MM-YYYY")} (Round {totalNotices})
              </span>
            </div>
          )}
        </div>

        <div className="case-info-column" style={{ marginLeft: "10px" }}>
          <a
            href={`/${window?.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Overview`}
            className="case-info-link"
          >
            {t("View Case")}
          </a>
          <a
            href={`/${window?.contextPath}/${userType}/dristi/home/view-case?caseId=${caseId}&filingNumber=${filingNumber}&tab=Orders`}
            className="case-info-link"
          >
            {t("View Order")}
          </a>
        </div>
      </div>
    );
  }, [caseDetails, filingNumber, respondentName, hearingDetails, orderList, userType, caseId]);

  const modalLabel = ["SUMMONS", "WARRANT"].includes(orderType) ? "SUMMON_WARRANT_STATUS" : "NOTICE_STATUS";

  return (
    <Modal
      isOpen={true}
      headerBarEnd={<CloseButton onClick={handleCloseModal} />}
      popupStyles={modalPopup}
      popupModuleActionBarStyles={{
        display: "none",
      }}
      formId="modal-action"
      headerBarMain={orderList && <ModalHeading label={t(modalLabel)} orderList={orderList} />}
      popupModuleMianStyles={{
        height: "calc(100% - 64px)",
        overFlowY: "auto",
        overflowX: "hidden",
      }}
    >
      <div className="summon-modal" style={{ width: "100%" }}>
        <div className="rounds-of-delivery" style={{ cursor: "pointer", marginLeft: "17px" }}>
          {orderListFiltered.map((item, index) => (
            <div
              key={index}
              onClick={() => {
                setActiveIndex({ partyIndex: index, orderIndex: 0 });
                setOrderLoading(true);
                setOrderList(item?.ordersList);
                setOrderNumber(item?.ordersList?.[0]?.orderNumber);
                setOrderType(item?.ordersList?.[0]?.orderType);
                setOrderId(item?.ordersList?.[0]?.id);
                setTimeout(() => {
                  setOrderLoading((prev) => !prev);
                }, 0);
              }}
              className={`round-item ${index === activeIndex?.partyIndex ? "active" : ""}`}
            >
              <div style={{ display: "flex", flexDirection: "column" }}>
                <span>{item?.partyName}</span>
                <span style={{ fontWeight: "400" }}>{item?.partyType}</span>
              </div>
            </div>
          ))}
        </div>
        {caseInfo}
        <h1 className="heading-m">{t("Rounds Of Delivery")}</h1>
        <div className="rounds-of-delivery" style={{ cursor: "pointer", marginLeft: "17px" }}>
          {orderList.map((item, index) => (
            <div
              key={index}
              onClick={() => {
                setActiveIndex({ ...activeIndex, orderIndex: index });
                setOrderLoading(true);
                setOrderNumber(item?.orderNumber);
                setOrderType(item?.orderType);
                setOrderId(item?.id);
                setTimeout(() => {
                  setOrderLoading((prev) => !prev);
                }, 0);
              }}
              className={`round-item ${index === activeIndex?.orderIndex ? "active" : ""}`}
              style={{ height: "50px" }}
            >
              <div style={{ display: "flex", flexDirection: "column", width: "90px" }}>
                <span>{item?.orderType}</span>
                <span>{item.createdDate && formatDate(new Date(item.createdDate), "DD-MM-YYYY")}</span>
              </div>
            </div>
          ))}
        </div>

        {orderNumber && !orderLoading && <InboxSearchComposer configs={config} defaultValues={filingNumber}></InboxSearchComposer>}

        <div className="action-buttons" style={actionButtonStyle}>
          {isCaseAdmitted && orderType !== "NOTICE" ? (
            <Button
              variation="secondary"
              className="action-button"
              label={t("Issue Warrant")}
              labelClassName={"secondary-label-selector"}
              onButtonClick={() => {
                handleIssueWarrant({
                  cnrNumber,
                  filingNumber,
                  orderType: "WARRANT",
                  hearingId,
                });
              }}
              style={{ marginRight: "1rem", fontWeight: "900" }}
            />
          ) : (
            orderType === "NOTICE" && (
              <Button
                variation="secondary"
                className="action-button"
                label={t("View Case File")}
                labelClassName={"secondary-label-selector"}
                onButtonClick={() => {
                  history.push(
                    `/${window?.contextPath}/employee/dristi/home/view-case?caseId=${caseDetails?.id}&filingNumber=${caseDetails?.filingNumber}&tab=Overview`
                  );
                }}
                style={{ marginRight: "1rem", fontWeight: "900" }}
              />
            )
          )}
          <Button
            label={t(`Re-Issue ${orderType === "SUMMONS" ? "Summon" : "Notice"}`)}
            onButtonClick={() => {
              handleNavigate();
            }}
            className="action-button"
            style={{
              boxShadow: "none",
              padding: "16px 24px",
            }}
            textStyles={headingStyle}
          />
        </div>
      </div>
    </Modal>
  );
};

export default SummonsAndWarrantsModal;
