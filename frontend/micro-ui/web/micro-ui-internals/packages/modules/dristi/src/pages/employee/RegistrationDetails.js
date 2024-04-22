import {
  Header,
  Card,
  CardSectionHeader,
  PDFSvg,
  Loader,
  StatusTable,
  Menu,
  ActionBar,
  SubmitBar,
  Modal,
  CardText,
} from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useParams, useHistory } from "react-router-dom";
import DocumentDetailCard from "../../components/DocumentDetailCard";
import DocViewerWrapper from "./docViewerWrapper";
import { ReactComponent as LocationOnMapIcon } from "./image/location_onmap.svg";

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

const LocationContent = () => {
  return (
    <div style={{ fontSize: "16px", display: "flex", marginTop: "-2px" }}>
      <div>
        <a href="https://www.google.com/maps" target="_blank" style={{ color: "#F47738" }}>
          View on map
        </a>
      </div>
      <div style={{ marginLeft: "10px" }}>
        <LocationOnMapIcon></LocationOnMapIcon>
      </div>
    </div>
  );
};

const RegisterDetails = ({ location, match }) => {
  const { id, applicationNo } = useParams();
  const urlParams = new URLSearchParams(window.location.search);
  const isAction = urlParams.get("isAction");
  const moduleCode = "DRISTI";
  const { t } = useTranslation();
  const history = useHistory();
  const [showModal, setShowModal] = useState(false);
  const [displayMenu, setDisplayMenu] = useState(false);
  const tenantId = Digit.ULBService.getCurrentTenantId();

  //   const { isLoading, data } = Digit.Hooks.br.useBRSearch(tenantId, { ids: id });
  // const data = {};
  const { data, isLoading, refetch } = Digit.Hooks.dristi.useGetIndividualAdvocate(
    {
      RequestInfo: {
        apiId: "Rainmaker",
        authToken: "6c20cb67-a3d5-436d-be87-4a12fb81d52b",
        userInfo: {
          id: 92,
          uuid: "ab8004c1-544f-4e20-acf8-b3327e6e8ff6",
          userName: "qasuperuser1",
          name: "Super User",
          mobileNumber: "7474747471",
          emailId: "qasuperuser@gmail.com",
          locale: null,
          type: "EMPLOYEE",
          roles: [
            {
              name: "MDMS ADMIN",
              code: "MDMS_ADMIN",
              tenantId: "pg",
            },
            {
              name: "Super User",
              code: "SUPERUSER",
              tenantId: "pg",
            },
            {
              name: "HRMS ADMIN",
              code: "HRMS_ADMIN",
              tenantId: "pg",
            },
            {
              name: "USER_APPROVER",
              code: "USER_APPROVER",
              tenantId: "pg",
            },
          ],
          active: true,
          tenantId: "pg",
          permanentCity: null,
        },
        msgId: "1713761331254|en_IN",
        plainAccessRequest: {},
      },
      criteria: [{ applicationNumber: applicationNo, id: null, barRegistrationNumber: null }],
      status: ["INWORKFLOW"],
    },
    {},
    moduleCode
  );
  console.debug(data);

  let isMobile = window.Digit.Utils.browser.isMobile();

  function onActionSelect(action) {
    // setSelectedAction(action);
    if (action === "Approve") {
      history.push(`/digit-ui/employee/br/responseemp`);
    }
    if (action === "Reject") {
      setShowModal(true);
    }
    setDisplayMenu(false);
  }

  const handleDelete = () => {
    const details = {
      events: [
        {
          ...data?.applicationData,
          status: "CANCELLED",
        },
      ],
    };
    history.push(`/digit-ui/employee`);
  };
  const aadharData = [
    { title: "Mobile Number", content: "+91 9876543210" },
    { title: "ID Type", content: "Aadhar" },
    { title: "Aadhar Number", content: "4321 1234 4312" },
  ];
  const personalData = [
    { title: "Name", content: "Nawal Kishor Tiwari" },
    { title: "Location", content: <LocationContent></LocationContent> },
    { title: "Address", content: "12, 5th street" },
  ];
  const barDetails = [
    { title: "State of Registration", content: "Kerala" },
    { title: "Bar Registration Number", content: 1233123 },
    { title: "Bar Council ID", image: true, content: <DocViewerWrapper pdfUrl={"https://www.irs.gov/pub/irs-pdf/fw9.pdf"}></DocViewerWrapper> },
  ];
  const header = applicationNo ? t(`Application Number ${applicationNo}`) : "My Application";
  return (
    <div>
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
                history.push("/digit-ui/employee/dristi/registration-requests");
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
          actionCancelOnSubmit={() => setShowModal(false)}
          actionSaveLabel={t("Reject")}
          actionSaveOnSubmit={handleDelete}
        >
          <Card style={{ boxShadow: "none" }}>
            <CardText>{t(`Reject Application due to invalid information`)}</CardText>
          </Card>
        </Modal>
      )}
    </div>
  );
};

export default RegisterDetails;
