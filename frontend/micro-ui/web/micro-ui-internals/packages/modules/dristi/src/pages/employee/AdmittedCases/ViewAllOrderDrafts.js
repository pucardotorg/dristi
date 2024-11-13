import React from "react";
import Modal from "../../../components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";

const ViewAllOrderDrafts = ({ t, setShow, draftOrderList, filingNumber }) => {
  const history = useHistory();
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
      </div>
    );
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
      actionSaveLabel={null}
      hideSubmit={true}
      actionCancelLabel={null}
      popupStyles={{ width: "800px" }}
      popupModuleMianStyles={{ maxHeight: "500px", overflow: "auto", display: "flex", flexDirection: "column", gap: "30px", marginTop: "15px" }}
      headerBarMain={<Heading label={`${t("DRAFT_ORDER_HEADER")} (${draftOrderList.length})`} />}
    >
      {draftOrderList.map((order) => (
        <div style={{ width: "100%", display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <div style={{ width: "75%", display: "flex", flexDirection: "column", gap: "10px" }}>
            <div style={{ fontWeight: 700, fontSize: "16px", lineHeight: "18.75px", color: "#101828" }}>
              {t(`ORDER_TYPE_${order.orderType.toUpperCase()}`)}
            </div>
            <div style={{ display: "flex", gap: "5px" }}>
              <div style={{ fontWeight: 600, fontSize: "14px", lineHeight: "20px", color: "#101828" }}>Deadline: </div>
              <div style={{ fontWeight: 500, fontSize: "14px", lineHeight: "20px", color: "#101828" }}>24 Jul, 2024</div>
            </div>
          </div>
          <div
            onClick={() => {
              setShow(false);
              history.push(`/${window.contextPath}/employee/orders/generate-orders?filingNumber=${filingNumber}&orderNumber=${order?.orderNumber}`);
            }}
            style={{ cursor: "pointer", fontWeight: 500, fontSize: "16px", lineHeight: "20px", color: "#007E7E" }}
          >
            {t("VIEW_LINK")}
          </div>
        </div>
      ))}
    </Modal>
  );
};

export default ViewAllOrderDrafts;
