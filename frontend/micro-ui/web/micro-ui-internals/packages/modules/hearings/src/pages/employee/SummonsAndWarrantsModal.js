import React, { useState, useEffect, useMemo } from "react";
import { useHistory } from "react-router-dom";
import { Modal, CloseSvg, Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { summonsConfig } from "../../configs/SummonsNWarrantConfig";
import useSearchOrdersService from "../../../../orders/src/hooks/orders/useSearchOrdersService";

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
  overflowY: "scroll",
};

const SummonsAndWarrantsModal = () => {
  const history = useHistory();
  const { t } = useTranslation();
  // const { filingNumber } = Digit.Hooks.useQueryParams();
  const filingNumber = "F-C.1973.002-2024-001383";
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [orderNumber, setOrderNumber] = useState(null);
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
    "dristi",
    filingNumber,
    Boolean(filingNumber)
  );

  const caseDetails = useMemo(
    () => ({
      ...caseData?.criteria?.[0]?.responseList?.[0],
    }),
    [caseData]
  );

  const { caseId } = useMemo(() => ({ cnrNumber: caseDetails.cnrNumber || "", caseId: caseDetails?.id }), [caseDetails]);

  const handleCloseModal = () => {
    history.goBack();
  };

  const handleNavigate = () => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/employee/orders/orders-create`);
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
      (item) => (item.orderType === "SUMMONS" || item.orderType === "WARRANT") && item?.status === "PUBLISHED"
    );

    const sortedOrders = filteredOrders?.sort((a, b) => {
      return new Date(b.auditDetails.createdTime) - new Date(a.auditDetails.createdTime);
    });

    return sortedOrders;
  }, [ordersData]);

  const [activeIndex, setActiveIndex] = useState(0);
  useEffect(() => {
    setOrderList(orderListFiltered || []);
    setOrderNumber(orderListFiltered?.[0]?.orderNumber);
  }, [orderListFiltered]);

  const config = useMemo(() => summonsConfig({ filingNumber, orderNumber }), [orderNumber]);

  const CloseButton = (props) => {
    return (
      <div onClick={props?.onClick} className="header-bar-end">
        <CloseSvg />
      </div>
    );
  };

  const ModalHeading = ({ label }) => {
    return (
      <h1 className="modal-heading" style={{ padding: 8 }}>
        <span className="heading-m">{label}</span>
        <span className="heading-xs">Failed 2 times</span>
      </h1>
    );
  };

  return (
    <Modal
      isOpen={true}
      headerBarEnd={<CloseButton onClick={handleCloseModal} />}
      popupStyles={modalPopup}
      popupModuleActionBarStyles={{
        display: "none",
      }}
      formId="modal-action"
      headerBarMain={<ModalHeading label={t("Summons and Warrants Status")} />}
    >
      <div className="case-info">
        <div className="case-info-column">
          <span className="case-info-label">{t("Case Name & ID")}</span>
          <span className="case-info-label">{t("Issued to")}</span>
          <span className="case-info-label">{t("Next Hearing Date")}</span>
          <span className="case-info-label">{t("Issued on")}</span>
        </div>

        <div className="case-info-column">
          <span className="case-info-value">
            {caseDetails?.caseTitle}, {filingNumber}
          </span>
          <span className="case-info-value">Vikram Singh (Respondent 1)</span>
          <span className="case-info-value">04/07/2024</span>
          <span className="case-info-value">23/05/2024 (Round 3)</span>
        </div>

        <div className="case-info-column">
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
          <span></span>
          <span></span>
        </div>
      </div>

      <h1 className="heading-m">{t("Rounds Of Delivery")}</h1>
      <div></div>
      <div className="rounds-of-delivery" style={{ cursor: "pointer" }}>
        {orderList.map((item, index) => (
          <div
            key={index}
            onClick={() => {
              setActiveIndex(index);
              setOrderLoading(true);
              setOrderNumber(item?.orderNumber);
              setTimeout(() => {
                setOrderLoading((prev) => !prev);
              }, 0);
            }}
            className={`round-item ${index === activeIndex ? "active" : ""}`}
          >
            {`${orderList.length - index} (${item?.orderType})`}
          </div>
        ))}
      </div>

      {orderNumber && !orderLoading && <InboxSearchComposer configs={config} defaultValues={filingNumber}></InboxSearchComposer>}

      <div className="action-buttons">
        <Button
          variation="secondary"
          className="action-button"
          label={t("Issue Warrant")}
          labelClassName={"secondary-label-selector"}
          onButtonClick={() => {
            handleNavigate();
          }}
          style={{ marginRight: "1rem", fontWeight: "900" }}
        />
        <Button
          label={t("Re-Issue Summon")}
          onButtonClick={() => {
            handleNavigate();
          }}
          className="action-button"
        />
      </div>
    </Modal>
  );
};

export default SummonsAndWarrantsModal;
