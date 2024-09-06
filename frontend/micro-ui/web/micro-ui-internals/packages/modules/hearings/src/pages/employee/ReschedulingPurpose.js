import React, { useMemo, useState } from "react";
import { CustomDropdown, Card, Modal } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

/**
 *
 * @param {Date} date
 * @returns {string}
 */
function formatDate(date) {
  return `${date.getDate()} ${date.toLocaleString("en-IN", { month: "long" })}, ${date.getFullYear()}`;
}

export const ReschedulingPurpose = ({ courtData, caseDetails, closeFunc, rescheduleAll = false }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const [rescheduleReason, setReschduleReason] = useState("");
  const userInfo = Digit.UserService.getUser()?.info;
  const userRoles = userInfo?.roles.map((role) => role.code);
  const userType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const isJudge = userRoles.includes("JUDGE_ROLE");
  const OrderWorkflowAction = Digit.ComponentRegistryService.getComponent("OrderWorkflowActionEnum") || {};
  const ordersService = Digit.ComponentRegistryService.getComponent("OrdersService") || {};

  const courtName = courtData.find((court) => court.code === caseDetails.courtId)?.name;
  const rescheduleHearingOptions = [
    {
      id: 1,
      reason: "SCHEDULING_CONFLICT",
      visibility: isJudge ? true : false,
      isactive: true,
      code: "SCHEDULING_CONFLICT",
    },
    {
      id: 2,
      reason: "TRAVELLING",
      visibility: true,
      isactive: true,
      code: "TRAVELLING",
    },
    {
      id: 3,
      reason: "DOCUMENT_INCOMPLETE",
      visibility: true,
      isactive: true,
      code: "DOCUMENT_INCOMPLETE",
    },
    {
      id: 4,
      reason: "UNWELL",
      visibility: true,
      isactive: true,
      code: "UNWELL",
    },
    {
      id: 5,
      reason: "COURT_CLOSED",
      visibility: isJudge ? true : false,
      isactive: true,
      code: "COURT_CLOSED",
    },
  ];

  const dropdownConfig = {
    label: "HEARING_TYPE",
    type: "dropdown",
    name: "hearingType",
    optionsKey: "code",
    isMandatory: true,
    options: rescheduleHearingOptions.filter((reason) => reason.visibility),
  };

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

  const AlertIcon = () => {
    return (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="#0F3B8C" xmlns="http://www.w3.org/2000/svg">
        <path
          d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"
          fill="#0F3B8C"
        />
        <path d="M11 10h2v6h-2zm0-3h2v2h-2z" fill="#0F3B8C" />
      </svg>
    );
  };

  const Close = () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g>
        <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
      </g>
    </svg>
  );

  const CloseBtn = (props) => {
    return (
      <div style={{ padding: "10px" }} onClick={props.onClick}>
        <Close />
      </div>
    );
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${year}-${month}-${day}`;
  };

  const onGenerateOrder = () => {
    const date = new Date(caseDetails.hearing.startTime);
    const requestBody = {
      order: {
        createdDate: new Date().getTime(),
        tenantId: Digit.ULBService.getCurrentTenantId(),
        filingNumber: caseDetails.filingNumber,
        cnrNumber: caseDetails.cnrNumber,
        statuteSection: {
          tenantId: Digit.ULBService.getCurrentTenantId(),
        },
        orderType: rescheduleAll ? "RESCHEDULING_OF_MULTIPLE_HEARING" : "INITIATING_RESCHEDULING_OF_HEARING_DATE",
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
              type: rescheduleAll ? "RESCHEDULING_OF_MULTIPLE_HEARING" : "INITIATING_RESCHEDULING_OF_HEARING_DATE",
              isactive: true,
              code: rescheduleAll ? "RESCHEDULING_OF_MULTIPLE_HEARING" : "INITIATING_RESCHEDULING_OF_HEARING_DATE",
              name: rescheduleAll ? "ORDER_TYPE_RESCHEDULING_OF_MULTIPLE_HEARING" : "ORDER_TYPE_INITIATING_RESCHEDULING_OF_HEARING_DATE",
            },
            originalHearingDate: formatDate(date),
            reasonForRescheduling: rescheduleReason,
          },
        },
      },
    };
    ordersService
      .createOrder(requestBody, { tenantId: Digit.ULBService.getCurrentTenantId() })
      .then((res) => {
        history.push(
          `/${window.contextPath}/${userType}/orders/generate-orders?filingNumber=${caseDetails.filingNumber}&orderNumber=${res.order.orderNumber}`
        );
      })
      .catch((err) => {
        console.error(err);
      });
  };

  return (
    <Modal
      headerBarMain={<Heading label={t(rescheduleAll ? "RESCHEDULE_ALL_HEARING" : "RESCHEDULE_HEARING")} />}
      headerBarEnd={<CloseBtn onClick={closeFunc} />}
      actionSaveLabel="Generate Order"
      actionSaveOnSubmit={onGenerateOrder}
      actionCancelLabel="Cancel"
      actionCancelOnSubmit={closeFunc}
      style={{ marginTop: "5px" }}
      popupStyles={{ width: "50%", height: "auto" }}
      isDisabled={rescheduleReason === ""}
    >
      {rescheduleAll ? (
        <Card>
          <div className="case-card">
            <div className="case-details">
              Total Hearings
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {caseDetails?.count} </div>
            </div>
            <div className="case-details">
              Initial Hearing Date
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {formatDate(new Date(caseDetails?.fromDate))} </div>
            </div>
            <div className="case-details">
              Slot
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {caseDetails?.slot} </div>
            </div>
          </div>
        </Card>
      ) : (
        <Card>
          <div className="case-card">
            <div className="case-details">
              Case Number
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {caseDetails?.filingNumber} </div>
            </div>
            <div className="case-details">
              Court Name
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {courtName} </div>
            </div>
            <div className="case-details">
              Case Type
              <div style={{ fontWeight: "700", marginTop: "5px" }}> {caseDetails?.case_Type || "NIA S138"} </div>
            </div>
          </div>
        </Card>
      )}
      <div style={{ marginBottom: "10px" }}>{t("RESCHEDULING_REASON")}</div>
      <CustomDropdown
        t={t}
        onChange={(e) => {
          setReschduleReason(e.reason);
        }}
        // value={userType}
        config={dropdownConfig}
      ></CustomDropdown>
      {rescheduleAll && (
        <div style={{ height: "80px", backgroundColor: "#ECF3FD", borderRadius: "4px" }}>
          <div style={{ padding: 10, display: "flex", alignItems: "center" }}>
            <div>
              <AlertIcon />
            </div>
            <div style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#0A0A0A", marginLeft: "5px" }}>Please Note</div>
          </div>
          <div style={{ color: "#3D3C3C", paddingLeft: 10 }}>
            Dates will automatically be re-assigned to these {caseDetails?.count} hearings. All relevant parties will receive an order for the same.
          </div>
        </div>
      )}
    </Modal>
  );
};
