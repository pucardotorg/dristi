import { Button, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import ViewCaseFile from "../scrutiny/ViewCaseFile";
import { TabSearchconfig } from "./AdmittedCasesConfig";

const fieldStyle = { marginRight: 0 };

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const AdmittedCases = () => {
  const { t } = useTranslation();
  const searchParams = new URLSearchParams(location.search);
  const filingNumber = searchParams.get("filingNumber");
  const cnr = searchParams.get("cnr");
  const title = searchParams.get("title");
  console.log(TabSearchconfig?.TabSearchconfig);
  const configList = useMemo(() => {
    return TabSearchconfig?.TabSearchconfig.map((tabConfig) => {
      return tabConfig.label === 'Parties' ?
        {
          ...tabConfig,
          apiDetails: {
            ...tabConfig.apiDetails,
            requestBody: {
              ...tabConfig.apiDetails.requestBody,
              criteria: [
                {
                  filingNumber: filingNumber,
                },
              ]
            },
          }
        } :
        {
          ...tabConfig,
          apiDetails: {
            ...tabConfig.apiDetails,
            requestParam: {
              ...tabConfig.apiDetails?.requestParam,
              filingNumber: filingNumber,
              cnrNumber: cnr,
              applicationNumber: ""
            }
          }
        }
    })
  }, [filingNumber])
  console.log(configList);
  const newTabSearchConfig = {
    ...TabSearchconfig,
    TabSearchconfig: configList
  }
  console.log(newTabSearchConfig);

  console.log(filingNumber);
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const [config, setConfig] = useState(newTabSearchConfig?.TabSearchconfig?.[0]); // initially setting first index config as default from jsonarray
  const [tabData, setTabData] = useState(
    newTabSearchConfig?.TabSearchconfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  ); // setting number of tab component and making first index enable as default
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(newTabSearchConfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };
  console.log(tabData);

  return (
    <React.Fragment>
      <div style={{ display: "flex", gap: "20px" }}>
        <Header styles={{ fontSize: "32px", marginTop: '40px' }}>{t(title)}</Header>
        <Button className={"generate-order"} label={"Generate Order / Notice"} />
      </div>

      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer
          configs={config}
          defaultValues={defaultValues}
          showTab={true}
          tabData={tabData}
          onTabChange={onTabChange}
        ></InboxSearchComposer>
      </div>
      {tabData.filter((tab) => tab.label === 'Complaints')[0].active && <div style={{ marginTop: '-90px' }}><ViewCaseFile t={t} showHeader={false} /></div>}
    </React.Fragment>
  );
};

export default AdmittedCases;
