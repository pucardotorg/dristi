import { ButtonSelector, CitizenInfoLabel, Close, CloseSvg, DownloadIcon } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import CustomDetailsCard from "../../../components/CustomDetailsCard";
import { useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import Modal from "../../../components/Modal";
import Button from "../../../components/Button";
import { ReactComponent as FileDownload } from "../../../icons/file_download.svg";
import { DRISTIService } from "../../../services";
import { Loader } from "@egovernments/digit-ui-components";
import { userTypeOptions } from "../registration/config";

const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
};

function CaseType({ t }) {
  const { path } = useRouteMatch();
  const history = useHistory();
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [page, setPage] = useState(0);
  const [isDisabled, setIsDisabled] = useState(false);
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
    const token = window.localStorage.getItem("token");
    const isUserLoggedIn = Boolean(token);
    const moduleCode = "DRISTI";
    const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
    const roles = userInfo?.roles;
    const { data: individualData, isLoading, refetch, isFetching } = window?.Digit.Hooks.dristi.useGetIndividualUser(
      {
        Individual: {
          userUuid: [userInfo?.uuid],
        },
      },
      { tenantId, limit: 1000, offset: 0 },
      moduleCode,
      "",
      userInfo?.uuid && isUserLoggedIn
    );
    const individualId = individualData?.Individual?.[0]?.individualId;

    const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
      individualData?.Individual,
    ]);

    const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
      {
        criteria: [{ individualId }],
        tenantId,
      },
      {},
      individualId,
      userType,
      userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
    );

    const userTypeDetail = useMemo(() => {
      return userTypeOptions.find((item) => item.code === userType) || {};
    }, [userType]);

    const searchResult = useMemo(() => {
      return searchData?.[userTypeDetail?.apiDetails?.requestKey];
    }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

    const advocateId = useMemo(() => {
      return searchResult?.[0]?.id;
    }, [searchResult]);

    if (isLoading || isFetching || isSearchLoading) {
      return <Loader />;
    }

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
            isDisabled={false}
            onButtonClick={() => {
              setIsDisabled(true);
              const cases = {
                tenantId,
                resolutionMechanism: "COURT",
                caseDescription: "Case description",
                linkedCases: [],
                filingDate: formatDate(new Date()),
                caseDetails: {},
                caseCategory: "CRIMINAL",
                statutesAndSections: [
                  {
                    tenantId,
                    statute: "Statute",
                    sections: ["Negotiable Instruments Act", "02."],
                    subsections: ["138", "03."],
                  },
                ],
                litigants: [
                  {
                    tenantId,
                    partyCategory: "INDIVIDUAL",
                  },
                ],
                representatives: [
                  {
                    advocateId: advocateId,
                    tenantId,
                    representing: [],
                  },
                ],
                documents: [
                  {
                    documentType: null,
                    fileStore: null,
                    documentUid: "",
                    additionalDetails: {},
                  },
                ],
                workflow: {
                  action: "SAVE_DRAFT",
                  comments: null,
                  assignes: null,
                  documents: [
                    {
                      documentType: null,
                      fileStore: null,
                      documentUid: null,
                      additionalDetails: null,
                    },
                  ],
                },
                additionalDetails: {},
              };
              DRISTIService.caseCreateService({ cases, tenantId })
                .then((res) => {
                  history.push(`${path}/case?caseId=${res?.cases[0]?.id}`);
                })
                .finally(() => setIsDisabled(false));
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
        subtext: "Negotiable Instruments Act",
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
          <CustomDetailsCard header={item.header} subtext={item.subtext} serialNumber={item.serialNumber} style={{ width: "100%" }} />
        ))}
      </div>
      {page === 0 && (
        <CitizenInfoLabel
          style={{ maxWidth: "100%", padding: "8px", borderRadius: "4px" }}
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
