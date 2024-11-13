import React from "react";
import { CardText, Modal } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const getStyles = () => ({
  modal: {
    width: "563px",
    height: "544px",
    maxHeight: "544px",
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    borderRadius: "4px",
  },
  actionBar: {
    display: "flex",
    justifyContent: "flex-end",
    position: "absolute",
    right: 0,
    bottom: 0,
    width: "100%",
    borderTop: "1px solid #dbd7d2",
    padding: "10px 16px 16px 0px",
  },
  content: {
    height: "420px",
    padding: "10px 20px",
    width: "536px",
    overflowY: "auto", // Make the content scrollable
  },
  document: {
    width: "450px",
    height: "122px",
    borderBottom: "1px solid #E8E8E8",
    display: "flex",
    justifyContent: "flex-start",
    padding: "16px 0px",
  },
  number: {
    fontWeight: 700,
    color: "#77787B",
    fontSize: "16px",
    marginRight: "5px",
  },
  textheading: {
    fontWeight: 700,
    fontSize: "18px",
  },
  text: {
    marginTop: "10px",
  },
  textHint: {
    fontWeight: 400,
    color: "#77787B",
    fontSize: "16px",
    lineHeight: "16.41px",
    marginTop: "10px",
  },
  // Custom scrollbar styles
  scrollbar: {
    "&::-webkit-scrollbar": {
      width: "6px",
    },
    "&::-webkit-scrollbar-thumb": {
      backgroundColor: "#888",
      borderRadius: "10px",
    },
    "&::-webkit-scrollbar-thumb:hover": {
      backgroundColor: "#555",
    },
    "&::-webkit-scrollbar-track": {
      backgroundColor: "#f1f1f1",
    },
  },
});

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="black">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ padding: 5 }}>
      <div className={"icon-bg-secondary"} style={{ backgroundColor: "white", cursor: "pointer" }}>
        {" "}
        <Close />{" "}
      </div>
    </div>
  );
};

const ListOfSuretyDocumentModal = ({ handleClose, heading, data }) => {
  const { t } = useTranslation();
  const styles = getStyles();
  return (
    <div>
      <Modal
        popupStyles={styles.modal}
        popupModuleActionBarStyles={styles.actionBar}
        popupModuleMianStyles={{}}
        headerBarMain={<Heading label={`${t(heading)} (${data?.length})`} />}
        headerBarEnd={<CloseBtn onClick={handleClose} />}
        actionSaveLabel={t("Download")}
        formId="modal-action"
      >
        <div style={{ ...styles.content, ...styles.scrollbar }}>
          {data?.map((data, index) => (
            <div style={styles.document}>
              <div style={styles.number}>{String(index + 1).padStart(2, "0")}. </div>
              <div>
                <div style={styles.textheading}>{t(data?.name)}</div>
                <div style={styles.text}>{t(data?.description)}</div>
                <div style={styles.textHint}>{t(data?.hint)}</div>
              </div>
            </div>
          ))}
        </div>
      </Modal>
    </div>
  );
};

export default ListOfSuretyDocumentModal;
