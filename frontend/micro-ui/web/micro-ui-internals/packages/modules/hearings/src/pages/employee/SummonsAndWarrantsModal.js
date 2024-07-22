import React, { useState, useEffect, useMemo } from "react";
import { useHistory } from 'react-router-dom';
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

const SummonsAndWarrantsModal = ({ isOpen, setShowModal, caseData }) => {
  const history = useHistory();
  const { t } = useTranslation();
  const { filingNumber, cnrNumber, applicationNumber } = caseData;
  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleNavigate = () => {
    const contextPath = window?.contextPath || '';
    history.push(`/${contextPath}/employee/orders/orders-create`);
  };

  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { data: ordersData, refetch: refetchOrdersData, isOrdersLoading, isFetching: isOrdersFetching } = useSearchOrdersService(
    { criteria: { tenantId: tenantId, filingNumber, applicationNumber, cnrNumber } },
    { tenantId },
    filingNumber,
    Boolean(filingNumber)
  );

  const [orderList, setOrderList] = useState([]);

  const orderListFiltered = useMemo(() => {
    if (!ordersData?.list) return [];

    const filteredOrders = ordersData?.list?.filter((item) => item.orderType === "SUMMONS" || item.orderType === "Warrant");

    const sortedOrders = filteredOrders?.sort((a, b) => {
      return new Date(b.auditDetails.createdTime) - new Date(a.auditDetails.createdTime);
    });

    return sortedOrders;
  }, [ordersData]);

  useEffect(() => {
    setOrderList(orderListFiltered || []);
  }, [orderListFiltered]);

  const [activeIndex, setActiveIndex] = useState(0);

  const [config, setConfig] = useState(summonsConfig({ filingNumber}));

  const CloseButton = (props) => {
    return (
      <div onClick={props?.onClick} className="header-bar-end">
        <CloseSvg />
      </div>
    );
  };

  const ModalHeading = ({ label }) => {
    return (
      <h1 className="modal-heading">
        <span className="heading-m">{label}</span>
        <span
          className="heading-xs"
        >
          Failed 2 times
        </span>
      </h1>
    );
  };

  return (
    <Modal
      isOpen={isOpen}
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
          <span className="case-info-value">Aparna vs Vikram, PB-PT-2023</span>
          <span className="case-info-value">Vikram Singh (Respondent 1)</span>
          <span className="case-info-value">04/07/2024</span>
          <span className="case-info-value">23/05/2024 (Round 3)</span>
        </div>

        <div className="case-info-column">
          <a href="#" className="case-info-link">
            {t("View Case")}
          </a>
          <a href="#" className="case-info-link">
            {t("View Order")}
          </a>
          <span></span>
          <span></span>
        </div>
      </div>

      <h1 className="heading-m">{t("Rounds Of Delivery")}</h1>
      <div></div>
      <div className="rounds-of-delivery" >
        {orderList.map((item, index) => (
          <div
            key={index}
            onClick={() => setActiveIndex(index)}
            className={`round-item ${index === activeIndex ? "active" : ""}`}
          >
            {`${orderList.length - index} (${item?.orderType})`}
          </div>
        ))}
      </div>

      <InboxSearchComposer configs={config} defaultValues={filingNumber}></InboxSearchComposer>

      <div className="action-buttons">
        <Button
          variation="secondary"
          className="action-button"
          label={t("Issue Warrant")}
          labelClassName={"secondary-label-selector"}
          onButtonClick={() => {handleNavigate()}}
          style={{ marginRight: "1rem", fontWeight: "900" }}
        />
        <Button label={t("Re-Issue Summon")} onButtonClick={() => {handleNavigate()}} className="action-button" />
      </div>
    </Modal>
  );
};

export default SummonsAndWarrantsModal;
