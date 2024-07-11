import React from "react";
import { CardText, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const Heading = (props) => {
  return (
    <div style={{ width: "264px", height: "28px" }}>
      <p style={{ fontWeight: 700, fontSize: "24px", lineHeight: "28.13px", textAlign: "center", color: "#0A0A0A" }}>{props.label}</p>
    </div>
  );
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#0A0A0A">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick}>
      <div className={"icon-bg-secondary"} style={{ backgroundColor: "#ffff", cursor: "pointer" }}>
        {" "}
        <Close />{" "}
      </div>
    </div>
  );
};

const PrintAndSendDocumentModal = ({ handleOpen }) => {
  const { t } = useTranslation();
  return (
    <div>
      <Modal
        popupStyles={{
          width: "616px",
          height: "430px",
          maxHeight: "481px",
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          borderRadius: "4px",
        }}
        headerBarMainStyle={{ padding: "0px 10px 0px 10px" }}
        headerBarMain={<Heading label={t("Print & Send Documents")} />}
        headerBarEnd={<CloseBtn onClick={handleOpen} />}
        actionSaveLabel={t("Mark As Sent")}
      >
        <div style={{ height: "310px" }}>
          <div style={{ width: "568px", height: "83px", marginTop: "16px" }}>
            <div style={{ fontWeight: 700, fontSize: "#0B0C0C", fontSize: "16px", lineHeight: "18.75px" }}>Print Documents</div>
            <div
              style={{
                border: "1px solid #BBBBBD",
                display: "flex",
                justifyContent: "space-between",
                borderRadius: "4px",
                padding: "12px 16px",
                marginTop: "16px",
              }}
            >
              <div style={{ fontSize: "16px", color: "#505A5F", fontWeight: 700, lineHeight: "18.75px" }}>Summons Document</div>
              <button style={{ fontWeight: 400, fontSize: "16px", color: "#007E7E", backgroundColor: "white", lineHeight: "18.75px" }}>Print</button>
            </div>
          </div>
          <div style={{ width: "568px", height: "182px", marginTop: "20px", backgroundColor: "#F7F5F3", padding: "16px 24px" }}>
            <div style={{ display: "flex", width: "520px", justifyContent: "space-between", marginBottom: "15px", color: "#3D3C3C" }}>
              <div style={{ fontWeight: 700, fontSize: "16px", color: "#0A0A0A" }}>Issued to</div>
              <div>Vikram Singh</div>
              <div style={{ textDecoration: "underline" }}>View Order</div>
            </div>
            <div style={{ display: "flex", width: "520px", marginBottom: "15px", color: "#3D3C3C" }}>
              <div style={{ fontWeight: 700, fontSize: "16px", color: "#0A0A0A" }}>Issued Date</div>
              <div style={{ marginLeft: "122px" }}>23/04/2024</div>
            </div>
            <div style={{ display: "flex", width: "520px", marginBottom: "15px", color: "#3D3C3C" }}>
              <div style={{ fontWeight: 700, fontSize: "16px", color: "#0A0A0A" }}>Next Hearing Date</div>
              <div style={{ marginLeft: "75px" }}>04/07/2024</div>
            </div>
            <div style={{ display: "flex", width: "520px", marginBottom: "5px", color: "#3D3C3C" }}>
              <div style={{ fontWeight: 700, fontSize: "16px", color: "#0A0A0A" }}>Channel Details</div>
              <div>
                <div style={{ marginLeft: "95px" }}>Physical Post </div>
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default PrintAndSendDocumentModal;
