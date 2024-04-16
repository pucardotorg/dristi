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

const RegisterDetails = ({ location, match }) => {
  const { id, applicationNo } = useParams();
  const urlParams = new URLSearchParams(window.location.search);
  const isAction = urlParams.get("isAction");

  const { t } = useTranslation();
  const history = useHistory();
  const [showModal, setShowModal] = useState(false);
  const [displayMenu, setDisplayMenu] = useState(false);
  const tenantId = Digit.ULBService.getCurrentTenantId();

  //   const { isLoading, data } = Digit.Hooks.br.useBRSearch(tenantId, { ids: id });
  const data = {};

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
    { title: "Location", content: "View on map" },
    { title: "Address", content: "12, 5th street" },
  ];
  const barDetails = [
    { title: "State of Registration", content: "Kerala" },
    { title: "Bar Registration Number", content: 1233123 },
    { title: "Bar Council ID" },
  ];
  return (
    <div>
      <Header>{t(`Application Number ${applicationNo}`)}</Header>
      <DocumentDetailCard cardData={aadharData} />
      <DocumentDetailCard cardData={personalData} header={"Personal Details"} />
      <DocumentDetailCard cardData={barDetails} header={"BAR Details"} />
      <ActionBar>
        {displayMenu ? (
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
