import { ArrowForward, ArrowRightInbox, Banner, Card, CardText, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { FormComposerV2, Toast } from "@egovernments/digit-ui-react-components";

import { modalConfig } from "../../citizen/FileCase/Config/sendBackModalConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomSubmitModal from "./CustomSubmitModal";

import { ReactComponent as RightArrow } from "../../citizen/Home/ImageUpload/image/arrow_forward.svg";
const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g clip-path="url(#clip0_4124_3214)">
      <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
    </g>
    <defs>
      <clipPath id="clip0_4124_3214">
        <rect width="24" height="24" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div style={{ padding: "10px" }} onClick={props.onClick}>
      <Close />
    </div>
  );
};
function SendCaseBack({ t, setShowModal, setSubmitModalInfo, submitModalInfo }) {
  const [reasons, setReasons] = useState(null);
  const [page, setPage] = useState(0);
  const history = useHistory();
  const stepItems = useMemo(() =>
    modalConfig.map(
      (step) => {
        const texts = {};
        for (const key in step.texts) {
          texts[key] = t(step.texts[key]);
        }
        return { ...step, texts };
      },
      [modalConfig]
    )
  );
  const onSubmit = (props) => {
    setPage(1);
  };
  return (
    <div>
      {page == 0 && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[0].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
        >
          <FormComposerV2
            config={[stepItems[0]]}
            t={t}
            noBoxShadow
            inline={false}
            label={t("CORE_COMMON_SEND")}
            onSecondayActionClick={() => {
              setShowModal(false);
            }}
            // onFormValueChange={onFormValueChange}
            headingStyle={{ textAlign: "center" }}
            cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
            onSubmit={(props) => onSubmit(props)}
            submitInForm
            // className={"registration-select-name"}
            secondaryActionLabel={t("CORE_LOGOUT_CANCEL")}
            buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
            actionClassName="e-filing-action-bar"
          ></FormComposerV2>
        </Modal>
      )}
      {page == 1 && (
        <Modal
          // headerBarMain={<Heading label={t(stepItems[0].headModal)} />}
          // headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          actionSaveLabel={
            <div>
              {t(submitModalInfo?.nextButtonText)}
              {submitModalInfo?.isArrow && <ArrowRightInbox />}
            </div>
          }
          actionCancelLabel={t(submitModalInfo?.backButtonText)}
          actionCancelOnSubmit={() => {
            setShowModal(false);
          }}
          className="case-types"

          // actionSaveOnSubmit={onModalSubmit}
        >
          <CustomSubmitModal t={t} submitModalInfo={submitModalInfo} />
        </Modal>
      )}
    </div>
  );
}

export default SendCaseBack;
