import {
  Header,
  Card,
  Loader,
  Menu,
  ActionBar,
  SubmitBar,
  Modal,
  CardText,
  Toast,
  TextInput,
  TextArea,
} from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useParams, useHistory } from "react-router-dom";
import DocumentDetailCard from "../../components/DocumentDetailCard";
import DocViewerWrapper from "./docViewerWrapper";
import { ReactComponent as LocationOnMapIcon } from "./image/location_onmap.svg";
import { userTypeOptions } from "../citizen/registration/config";

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
  const { id, applicationNo } = useParams();
  const urlParams = new URLSearchParams(window.location.search);
  const isAction = urlParams.get("isAction");
  const individualId = urlParams.get("individualId");
  const type = urlParams.get("type") || "advocate";
  const moduleCode = "DRISTI";
  const { t } = useTranslation();
  const history = useHistory();
  const [showModal, setShowModal] = useState(false);
  const [displayMenu, setDisplayMenu] = useState(false);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [message, setMessage] = useState(null);
  const [reasons, setReasons] = useState(null);

  const { data: individualData, isLoading: isGetUserLoading } = Digit.Hooks.dristi.useGetIndividualUser(
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

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);

  const { data: searchData, isLoading: isSearchLoading } = Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    {},
    applicationNo + individualId,
    userType,
    userType === "ADVOCATE" ? "/advocate/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );
  const searchResult = useMemo(() => {
    return searchData?.[userTypeDetail?.apiDetails?.requestKey];
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);
  const fileStoreId = useMemo(() => {
    return searchResult?.[0].documents?.[0]?.fileStore;
  }, [searchResult]);

  let isMobile = window.Digit.Utils.browser.isMobile();

  function takeAction(action) {
    const applications = searchResult;
    applications[0].workflow.action = action.toUpperCase();
    const data = { [userTypeDetail?.apiDetails?.requestKey]: applications };
    const url = userType === "ADVOCATE_CLERK" ? "/advocate/clerk/v1/_update" : "/advocate/advocate/v1/_update";
    if (showModal) {
      applications[0].workflow.comments = reasons;
    }
    Digit.DRISTIService.advocateClerkService(url, data, tenantId, true, {})
      .then(() => {
        setShowModal(false);
        setMessage(action === "Approve" ? t("ES_USER_APPROVED") : t("ES_USER_REJECTED"));
        setTimeout(() => history.push(`/digit-ui/employee/dristi/registration-requests?type=${type}`), 2000);
      })
      .catch(() => {
        setShowModal(false);
        setMessage(t("ES_API_ERROR"));
        setTimeout(() => history.push(`/digit-ui/employee/dristi/registration-requests?type=${type}`), 2000);
      });
  }

  function onActionSelect(action) {
    if (action === "Approve") {
      takeAction(action);
    }
    if (action === "Reject") {
      setShowModal(true);
    }
    setDisplayMenu(false);
  }

  const handleDelete = () => {
    takeAction("Reject");
  };

  const addressLine1 = individualData?.Individual?.[0]?.address[0]?.addressLine1 || "";
  const addressLine2 = individualData?.Individual?.[0]?.address[0]?.addressLine2 || "";
  const buildingName = individualData?.Individual?.[0]?.address[0]?.buildingName || "";
  const street = individualData?.Individual?.[0]?.address[0]?.street || "";
  const landmark = individualData?.Individual?.[0]?.address[0]?.landmark || "";
  const city = individualData?.Individual?.[0]?.address[0]?.city || "";
  const pincode = individualData?.Individual?.[0]?.address[0]?.pincode || "";
  const latitude = useMemo(() => individualData?.Individual?.[0]?.address[0]?.latitude || "", [individualData?.Individual]);
  const longitude = useMemo(() => individualData?.Individual?.[0]?.address[0]?.longitude || "", [individualData?.Individual]);

  const address = `${addressLine1} ${addressLine2} ${buildingName} ${street} ${landmark} ${city} ${pincode}`.trim();

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
      { title: "Bar Registration Number", content: searchResult?.[0]?.[userTypeDetail?.apiDetails?.AdditionalFields?.[0]] || "N/A" },
      {
        title: "Bar Council ID",
        image: true,
        content: <DocViewerWrapper fileStoreId={fileStoreId} tenantId={tenantId}></DocViewerWrapper>,
      },
    ];
  }, [fileStoreId, searchResult, tenantId, userTypeDetail?.apiDetails?.AdditionalFields]);

  if (isSearchLoading || isGetUserLoading) {
    return <Loader />;
  }

  const aadharData = [
    { title: "Mobile Number", content: individualData?.Individual?.[0]?.mobileNumber },
    { title: "ID Type", content: individualData?.Individual?.[0]?.identifiers[0]?.identifierType },
    { title: "Aadhar Number", content: individualData?.Individual?.[0]?.identifiers[0]?.identifierId },
  ];

  const header = applicationNo ? t(`Application Number ${applicationNo}`) : "My Application";
  return (
    <div style={{ paddingLeft: "20px" }}>
      <Header>{header}</Header>
      <DocumentDetailCard cardData={aadharData} />
      <DocumentDetailCard cardData={personalData} header={"Personal Details"} />
      <DocumentDetailCard cardData={barDetails} header={"BAR Details"} />
      {applicationNo && (
        <ActionBar>
          {displayMenu && applicationNo ? (
            <Menu
              menuItemStyle={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                flexDirection: "column",
              }}
              localeKeyPrefix={"BR"}
              options={["Approve", "Reject"]}
              t={t}
              onSelect={onActionSelect}
            />
          ) : null}
          <SubmitBar
            label={isAction ? t("Take_Action") : t("Go_Back_Home")}
            onSubmit={() => {
              if (isAction) {
                setDisplayMenu(!displayMenu);
              } else {
                history.push(`/digit-ui/employee/dristi/registration-requests?type=${type}`);
              }
            }}
          />
        </ActionBar>
      )}
      {showModal && (
        <Modal
          headerBarMain={<Heading label={t("Confirm Reject Application")} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          actionCancelLabel={t("Cancel")}
          actionCancelOnSubmit={() => {
            setShowModal(false);
          }}
          actionSaveLabel={t("Reject")}
          actionSaveOnSubmit={handleDelete}
          isDisabled={!reasons}
        >
          <Card style={{ boxShadow: "none", padding: "2px 16px 2px 16px", marginBottom: "2px" }}>
            <CardText style={{ margin: "2px 0px" }}>{t(`REASON_FOR_REJECTION`)}</CardText>
            <TextArea rows={"3"} onChange={(e) => setReasons(e.target.value)} style={{ maxWidth: "100%", height: "auto" }}></TextArea>
          </Card>
        </Modal>
      )}
      {message && (
        <Toast error={message === t("ES_API_ERROR") || message === t("ES_USER_REJECTED")} label={message} onClose={() => setMessage(null)} />
      )}
    </div>
  );
};

export default ApplicationDetails;
