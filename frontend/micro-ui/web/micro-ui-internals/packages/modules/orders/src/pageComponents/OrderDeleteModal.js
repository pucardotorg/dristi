import React, { useMemo } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg } from "@egovernments/digit-ui-components";

function OrderDeleteModal({ setDeleteOrderIndex, deleteOrderIndex, handleDeleteOrder, t }) {
  const deleteWarningText = useMemo(() => {
    return (
      <div className="delete-warning-text">
        <h3>{`${t("THIS_CAN_NOT_BE_REVERSED")}`}</h3>
      </div>
    );
  }, []);
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  return (
    <Modal
      headerBarMain={<Heading label={t("ARE_YOU_SURE_TO_DELETE_ORDER")} />}
      headerBarEnd={
        <CloseBtn
          onClick={() => {
            setDeleteOrderIndex(null);
          }}
        />
      }
      actionCancelLabel={t("CS_COMMON_CANCEL")}
      actionCancelOnSubmit={() => {
        setDeleteOrderIndex(null);
      }}
      actionSaveLabel={t("DELETE_ORDER")}
      children={deleteWarningText}
      actionSaveOnSubmit={() => {
        handleDeleteOrder(deleteOrderIndex);
      }}
      style={{ height: "40px" }}
    ></Modal>
  );
}

export default OrderDeleteModal;
