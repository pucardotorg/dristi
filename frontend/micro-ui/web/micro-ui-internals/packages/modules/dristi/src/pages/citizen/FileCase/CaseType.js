import { ButtonSelector, CitizenInfoLabel, Close, CloseSvg, DownloadIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import CustomDetailsCard from "../../../components/CustomDetailsCard";
import { useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import Modal from "../../../components/Modal";
import Button from "../../../components/Button";
import { ReactComponent as FileDownload } from '../../../icons/file_download.svg';

function CaseType({ t }) {
  const { path } = useRouteMatch();
  const history = useHistory();
  const [page, setPage] = useState(0);
  const onCancel = () => {
    history.push("/digit-ui/citizen/dristi/home");
  };
  const onSelect = () => {
    setPage(1);
  };
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
  const Submitbar = () => {
    return (
      <div className="submit-bar-div">
        <Button
          icon={<FileDownload />}
          className="download-button"
          label={t("CS_COMMON_DOWNLOAD")}
        />
        <div className="right-div">
          <Button
            className="cancel-button"
            label={t("CS_COMMON_BACK")}
            onButtonClick={() => {
              setPage(0);
            }}
          />
          <Button
            className="start-filling-button"
            label={t("CS_START_FILLING")}
            onButtonClick={() => {
              history.push(`${path}/respondent-details`);
            }}
          />
          {/* <ButtonSelector
            label={t("CS_START_FILLING")}
            onSubmit={() => {
              history.push(`${path}/case`);
            }}
          /> */}
        </div>
      </div>
    );
  };

  const detailsCardList = useMemo(() => {
    const caseTypeDetails = [
      { header: "Case Category", subtext: "Criminal", serialNumber: "01." },
      {
        header: "Status / Act",
        subtext: "Negotiable Insruments Act",
        serialNumber: "02.",
      },
      { header: "Section", subtext: "138", serialNumber: "03." },
    ];
    const listDocumentDetails = [
      {
        header: "Proof of Identity",
        subtext: "PAN Card, Aadhar card, Passport, Driving license, Voter ID, Ration card or Bank passbook",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "01.",
      },
      {
        header: "Bounced Cheque",
        subtext: "A copy of the bounced chequeon the  basis which this case is being filed",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "02.",
      },
      {
        header: "Cheque Return Memo",
        subtext: "The document received from the bank that has the information that the cheque has bounced",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "03.",
      },
      {
        header: "Proof od Debt/ Liability",
        subtext: "Anything to prove some sort of agreement between you and the respondent",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "04.",
      },
      {
        header: "Legal Demand Notice",
        subtext:
          "Any intimation you provided to the respondent to informing them that their cheque had bounced and they still owed you the cheque amount",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "05",
      },
      {
        header: "Notarised Affidavit",
        subtext:
          "This is a replacement for your sworn statement which reduces the chances that an admission hearing is needed for the court to take cognisance of your case.",
        subnote: "Upload .pdf or .jpg. Maximum upload size of 50MB",
        serialNumber: "06",
      },
    ];
    return page === 0 ? caseTypeDetails : listDocumentDetails;
  }, [page]);

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_CORE_WEB_PROCEED")}
      hideSubmit={page !== 0}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
      headerBarMain={<Heading label={page === 0 ? t("CS_SELECT_CASETYPE_HEADER") : t("CS_LIST_DOCUMENTS") + ` (${detailsCardList.length})`} />}
      // style={{ height: "3vh" }}
      className="case-types"
    >
      <div className="case-types-main-div">
        {detailsCardList.map((item) => (
          <CustomDetailsCard header={item.header} subtext={item.subtext} subnote={item.subnote} serialNumber={item.serialNumber} />

        ))}
      </div>
      {page === 0 && (
        <CitizenInfoLabel
          style={{ maxWidth: "100%", padding: '8px', borderRadius: '4px' }}
          info={t("ES_COMMON_NOTE")}
          text={t("ES_BANNER_LABEL")}
          className="doc-banner"
        ></CitizenInfoLabel>
      )}
      {page === 1 && <Submitbar />}
    </Modal>
  );
}

export default CaseType;
