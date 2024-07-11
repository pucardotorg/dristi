import React, { useEffect, useState, useTransition } from "react";
import { Header, Button, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import PrintAndSendDocumentModal from "../../components/Print&SendDocuments";
import { SummonsTabsConfig } from "../../configs/SuumonsConfig";
import { useTranslation } from "react-i18next";
import ReviewDocumentModal from "../../components/ReviewDocumentModal";

const defaultSearchValues = {
  eprocess: "",
  caseId: "",
};

const ReviewSummonsNoticeAndWarrant = () => {
  const { t } = useTranslation();
  const [openSigned, setOpenSigned] = useState(false);
  const [openUnsigned, setOpenUnsigned] = useState(false);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues);
  const [config, setConfig] = useState(SummonsTabsConfig?.SummonsTabsConfig?.[0]);

  const [tabData, setTabData] = useState(
    SummonsTabsConfig?.SummonsTabsConfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  );

  const handleOpen = (props) => {
    //change status to signed or unsigned
    if (props.values.status === "PAYMENT_PENDING") setOpenSigned(true);
    else setOpenUnsigned(true);
  };

  const handleClose = () => {
    setOpenSigned(false);
    setOpenUnsigned(false);
  };
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(SummonsTabsConfig?.SummonsTabsConfig?.[n]); // as per tab number filtering the config
  };

  return (
    <div style={{ width: "1,440px", height: "1,227px", padding: "48px 64px" }}>
      <div>
        <div style={{ display: "flex", gap: "20px" }}>
          <Header styles={{ fontSize: "32px" }}>{t("Review Summons, Notices & Warrants")}</Header>
        </div>

        <div className="inbox-search-wrapper">
          {/* Pass defaultValues as props to InboxSearchComposer */}
          <InboxSearchComposer
            configs={config}
            defaultValues={defaultValues}
            showTab={true}
            tabData={tabData}
            onTabChange={onTabChange}
            additionalConfig={{
              resultsTable: {
                onClickRow: (props) => {
                  handleOpen(props);
                },
              },
            }}
          ></InboxSearchComposer>
          {openUnsigned && <PrintAndSendDocumentModal handleClose={handleClose} />}
          {openSigned && <ReviewDocumentModal handleClose={handleClose} />}
        </div>
      </div>
    </div>
  );
};

export default ReviewSummonsNoticeAndWarrant;
