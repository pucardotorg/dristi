import { ButtonSelector, CitizenInfoLabel, Close, CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import CustomDetailsCard from "../../../components/CustomDetailsCard";
import { useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";

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
      <div style={{ width: "100%", display: "flex", justifyContent: "space-between" }}>
        <ButtonSelector label={t("CS_COMMON_DOWNLOAD")} />
        <div style={{ display: "flex", justifyContent: "space-between", gap: "30px" }}>
          <ButtonSelector
            label={t("CS_COMMON_BACK")}
            onSubmit={() => {
              setPage(0);
            }}
          />
          <ButtonSelector
            label={t("CS_START_FILLING")}
            onSubmit={() => {
              history.push(`${path}/case`);
            }}
          />
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
      headerBarEnd={<CloseBtn onClick={onCancel} isMobileView={true} />}
      actionCancelLabel={page === 0 ? t("CORE_LOGOUT_CANCEL") : null}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("CS_CORE_WEB_PROCEED")}
      hideSubmit={page !== 0}
      actionSaveOnSubmit={onSelect}
      formId="modal-action"
      headerBarMain={<Heading label={page === 0 ? t("CS_SELECT_CASETYPE_HEADER") : t("CS_LIST_DOCUMENTS") + ` (${detailsCardList.length})`} />}
      style={{ height: "3vh" }}
    >
      <div>
        {detailsCardList.map((item) => (
          <CustomDetailsCard header={item.header} subtext={item.subtext} serialNumber={item.serialNumber} style={{ width: "100%" }} />
        ))}
      </div>
      {page === 0 && (
        <CitizenInfoLabel
          style={{ maxWidth: "100%", height: "90px" }}
          textStyle={{ margin: 8 }}
          iconStyle={{ margin: 0 }}
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
