import { Header, Card, Loader, ActionBar, SubmitBar, Modal, CardText, Toast, TextArea, BackButton } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useParams, useHistory } from "react-router-dom";
import DocumentDetailCard from "../../components/DocumentDetailCard";
import DocViewerWrapper from "./docViewerWrapper";
import { ReactComponent as LocationOnMapIcon } from "../../images/location_onmap.svg";
import { userTypeOptions } from "../citizen/registration/config";
import Menu from "../../components/Menu";
import { useToast } from "../../components/Toast/useToast";
import { ErrorInfoIcon, SuccessIcon } from "../../icons/svgIndex";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFFFFF">
    <path d="M0 0h24v24H0V0z" fill="none" />
    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12 19 6.41z" />
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div className="icon-bg-secondary" onClick={props.onClick}>
      <Close />
    </div>
  );
};

const LocationContent = ({ latitude = 17.2, longitude = 17.2 }) => {
  return (
    <div style={{ fontSize: "16px", display: "flex", marginTop: "-2px" }}>
      <div>
        <a
          href={`https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`}
          target="_blank"
          rel="noreferrer"
          style={{ color: "#F47738" }}
        >
          View on map
        </a>
      </div>
      <div style={{ marginLeft: "10px" }}>
        <LocationOnMapIcon></LocationOnMapIcon>
      </div>
    </div>
  );
};

const ApplicationDetails = ({ location, match }) => {
  const urlParams = new URLSearchParams(window.location.search);

  const toast = useToast();
  const userRoles = Digit.UserService.getUser()?.info?.roles.map((role) => role.code);
  const individualId = urlParams.get("individualId");
  const applicationNo = urlParams.get("applicationNo");
  const type = urlParams.get("type") || "advocate";
  const moduleCode = "DRISTI";
  const { t } = useTranslation();
  const history = useHistory();
  const [showModal, setShowModal] = useState(false);
  const [showInfoModal, setShowInfoModal] = useState({ isOpen: false, status: "" });
  const [displayMenu, setDisplayMenu] = useState(false);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [message, setMessage] = useState(null);
  const [reasons, setReasons] = useState(null);
  const [isAction, setIsAction] = useState(false);
  const { data: individualData, isLoading: isGetUserLoading } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        individualId,
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    individualId,
    Boolean(individualId)
  );

  const userType = useMemo(() => individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [
    individualData?.Individual,
  ]);

  const identifierIdDetails = useMemo(
    () => JSON.parse(individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "identifierIdDetails")?.value || "{}"),
    [individualData?.Individual]
  );

  const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [applicationNo ? { applicationNumber: applicationNo } : { individualId }],
      tenantId,
    },
    {},
    applicationNo + individualId,
    userType,
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const { isLoading: isWorkFlowLoading, data: workFlowDetails } = window?.Digit.Hooks.useWorkflowDetails({
    tenantId,
    id: applicationNo,
    moduleCode,
    config: {
      enabled: true,
      cacheTime: 0,
    },
  });
  const actions = useMemo(
    () =>
      workFlowDetails?.processInstances?.[0]?.state?.actions
        ?.filter((action) => action.roles.every((role) => userRoles.includes(role)))
        .map((action) => action.action),
    [workFlowDetails?.processInstances, userRoles]
  );

  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);
  const fileStoreId = useMemo(() => {
    return searchResult?.[0]?.documents?.[0]?.fileStore;
  }, [searchResult]);
  const fileName = useMemo(() => {
    return searchResult?.[0]?.documents?.[0]?.additionalDetails?.fileName;
  }, [searchResult]);
  useEffect(() => {
    setIsAction(searchResult?.[0]?.status === "INWORKFLOW");
  }, [searchResult]);

  const applicationNumber = useMemo(() => {
    return searchResult?.[0]?.applicationNumber;
  }, [searchResult]);

  // let isMobile = window.Digit.Utils.browser.isMobile();

  function takeAction(action) {
    const applications = searchResult;
    applications[0].workflow.action = action;
    const data = { [userTypeDetail?.apiDetails?.requestKey]: applications?.[0] };
    const url = userType === "ADVOCATE_CLERK" ? "/advocate/clerk/v1/_update" : "/advocate/advocate/v1/_update";
    if (showModal) {
      applications[0].workflow.comments = reasons;
    }
    window?.Digit.DRISTIService.advocateClerkService(url, data, tenantId, true, {})
      .then(() => {
        setShowModal(false);
        if (action === "APPROVE") {
          setShowInfoModal({ isOpen: true, status: "ES_USER_APPROVED" });
        } else if (action === "REJECT") {
          setShowInfoModal({ isOpen: true, status: "ES_USER_REJECTED" });
        }
      })
      .catch(() => {
        setShowModal(false);
        setShowInfoModal({ isOpen: true, status: "ES_API_ERROR" });
      });
  }

  function onActionSelect(action) {
    if (action === "APPROVE") {
      takeAction(action);
    }
    if (action === "REJECT") {
      setShowModal(true);
    }
    setDisplayMenu(false);
  }

  const handleDelete = (action) => {
    takeAction(action);
  };

  const addressLine1 = individualData?.Individual?.[0]?.address[0]?.addressLine1 || "";
  const addressLine2 = individualData?.Individual?.[0]?.address[0]?.addressLine2 || "";
  const buildingName = individualData?.Individual?.[0]?.address[0]?.buildingName || "";
  const street = individualData?.Individual?.[0]?.address[0]?.street || "";
  const city = individualData?.Individual?.[0]?.address[0]?.city || "";
  const pincode = individualData?.Individual?.[0]?.address[0]?.pincode || "";
  const latitude = useMemo(() => individualData?.Individual?.[0]?.address[0]?.latitude || "", [individualData?.Individual]);
  const longitude = useMemo(() => individualData?.Individual?.[0]?.address[0]?.longitude || "", [individualData?.Individual]);

  const address = `${addressLine1} ${addressLine2} ${buildingName} ${street} ${street} ${city} ${pincode}`.trim();

  const givenName = individualData?.Individual?.[0]?.name?.givenName || "";
  const otherNames = individualData?.Individual?.[0]?.name?.otherNames || "";
  const familyName = individualData?.Individual?.[0]?.name?.familyName || "";

  const fullName = `${givenName} ${otherNames} ${familyName}`.trim();

  const personalData = useMemo(
    () => [
      { title: "Name", content: fullName },
      { title: "Location", content: <LocationContent latitude={latitude} longitude={longitude}></LocationContent> },
      { title: "Address", content: address },
    ],
    [address, fullName, latitude, longitude]
  );
  const barDetails = useMemo(() => {
    return [
      { title: "BAR Registration Number", content: searchResult?.[0]?.[userTypeDetail?.apiDetails?.AdditionalFields?.[0]] || "N/A" },
      {
        title: "BAR Council ID",
        image: true,
        content: fileName,
      },
      {
        doc: <DocViewerWrapper fileStoreId={fileStoreId} tenantId={tenantId} docViewerCardClassName={"doc-card"}></DocViewerWrapper>,
        image: true,
      },
    ];
  }, [fileStoreId, searchResult, tenantId, userTypeDetail?.apiDetails?.AdditionalFields]);

  const aadharData = useMemo(() => {
    return [
      { title: "Mobile Number", content: individualData?.Individual?.[0]?.mobileNumber },
      { title: "ID Type", content: individualData?.Individual?.[0]?.identifiers[0]?.identifierType },
      {
        title: identifierIdDetails?.fileStoreId ? "ID Proof" : "Aadhar Number",
        content: identifierIdDetails?.fileStoreId ? (
          <DocViewerWrapper fileStoreId={identifierIdDetails?.fileStoreId} tenantId={tenantId} displayFilename={identifierIdDetails?.filename} />
        ) : (
          individualData?.Individual?.[0]?.identifiers[0]?.identifierId
        ),
      },
    ];
  }, [identifierIdDetails?.fileStoreId, identifierIdDetails?.filename, individualData?.Individual, tenantId]);

  const header = useMemo(() => {
    return applicationNo || applicationNumber ? t(`Application Number ${applicationNo || applicationNumber}`) : "My Application";
  }, [applicationNo, applicationNumber, t]);

  if (isSearchLoading || isGetUserLoading || isWorkFlowLoading) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div className="view-application">
        <div className="application-main">
          <Header className="application-header">{header}</Header>
          <div className="application-card">
            <DocumentDetailCard cardData={aadharData} />
            <DocumentDetailCard cardData={personalData} />
            {type === "advocate" && userType !== "ADVOCATE_CLERK" && <DocumentDetailCard cardData={barDetails} />}
          </div>
          {!applicationNo && (
            <div className="action-button-application">
              <SubmitBar
                label={t("Go_Back_Home")}
                onSubmit={() => {
                  history.push(`/${window?.contextPath}/citizen/dristi/home`);
                }}
                className="action-button-width"
              />
            </div>
          )}
          {applicationNo && (
            <div className="action-button-application">
              {actions.map((option, index) => (
                <SubmitBar
                  key={index}
                  label={option == "REJECT" ? "Reject Request" : "Accept Request"}
                  style={{ margin: "20px", backgroundColor: option == "REJECT" ? "#BB2C2F" : "#007E7E" }}
                  onSubmit={(data) => {
                    onActionSelect(option);
                  }}
                  className="action-button-width"
                />
              ))}
            </div>
          )}
          {showModal && (
            <Modal
              headerBarMain={<Heading label={t("Confirm Reject Application")} />}
              headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
              actionSaveLabel={t("Reject")}
              actionSaveOnSubmit={() => {
                handleDelete("REJECT");
              }}
              isDisabled={!reasons || !reasons.trim()}
              style={{ backgroundColor: "#BB2C2F" }}
            >
              <Card style={{ boxShadow: "none", padding: "2px 16px 2px 16px", marginBottom: "2px" }}>
                <CardText style={{ margin: "2px 0px" }}>{t(`REASON_FOR_REJECTION`)}</CardText>
                <TextArea rows={"3"} onChange={(e) => setReasons(e.target.value)} style={{ maxWidth: "100%", height: "auto" }}></TextArea>
              </Card>
            </Modal>
          )}
          {showInfoModal?.isOpen && (
            <Modal
              headerBarEnd={
                <CloseBtn
                  onClick={() => {
                    setShowInfoModal({ isOpen: false, status: "" });
                    history.push(
                      userType === "ADVOCATE_CLERK"
                        ? `/digit-ui/employee/dristi/registration-requests?type=clerk`
                        : `/digit-ui/employee/dristi/registration-requests?type=advocate`,
                      { isSentBack: true }
                    );
                  }}
                />
              }
              actionSaveLabel={t("GO TO HOME")}
              actionSaveOnSubmit={() => {
                setShowInfoModal({ isOpen: false, status: "" });
                history.push(
                  userType === "ADVOCATE_CLERK"
                    ? `/digit-ui/employee/dristi/registration-requests?type=clerk`
                    : `/digit-ui/employee/dristi/registration-requests?type=advocate`,
                  { isSentBack: true }
                );
              }}
              style={{ backgroundColor: "#BB2C2F" }}
              popmoduleClassName="request-processing-info-modal"
            >
              <div className="main-div">
                <div className="icon-div">{showInfoModal?.status === "ES_API_ERROR" ? <ErrorInfoIcon /> : <SuccessIcon />}</div>
                <div className="info-div">
                  <h1>{t(showInfoModal?.status)}</h1>
                </div>
              </div>
            </Modal>
          )}
        </div>
      </div>
    </React.Fragment>
  );
};

export default ApplicationDetails;
