import { useTranslation } from "react-i18next";
import React, { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";
import { Header, InboxSearchComposer, Button } from "@egovernments/digit-ui-react-components";
import { TabSearchconfig } from "../../../../orders/src/configs/OrdersHomeConfig";
import { ordersService } from "../../hooks/services";
import { CaseWorkflowAction } from "../../utils/caseWorkflow";

const fieldStyle = { marginRight: 0 };

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const OrdersHome = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const cnrNumber = urlParams.get("cnrNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const [config, setConfig] = useState(TabSearchconfig?.TabSearchconfig?.[0]); // initially setting first index config as default from jsonarray
  const [tabData, setTabData] = useState(
    TabSearchconfig?.TabSearchconfig?.map((configItem, index) => ({ key: index, label: configItem.label, active: index === 0 ? true : false }))
  ); // setting number of tab component and making first index enable as default
  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const onTabChange = (n) => {
    setTabData((prev) => prev.map((i, c) => ({ ...i, active: c === n ? true : false }))); //setting tab enable which is being clicked
    setConfig(TabSearchconfig?.TabSearchconfig?.[n]); // as per tab number filtering the config
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  };

  const handleCreateOrder = () => {
    const reqbody = {
      order: {
        createdDate: formatDate(new Date()),
        tenantId,
        cnrNumber,
        filingNumber: filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "Bail",
        status: "",
        isActive: true,
        workflow: {
          action: CaseWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        additionalDetails: {},
      },
    };
    ordersService
      .createOrder(reqbody, { tenantId })
      .then(() => {
        history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${filingNumber}`);
      })
      .catch(() => {});
  };
  if (!cnrNumber || !filingNumber) {
    history.push("/digit-ui/employee");
  }
  return (
    <React.Fragment>
      <div style={{ display: "flex", gap: "20px" }}>
        <Header styles={{ fontSize: "32px" }}>{t(config?.label)}</Header>
        <Button className={"generate-order"} label={"Generate Order / Notice"} onButtonClick={handleCreateOrder} />
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
    </React.Fragment>
  );
};

export default OrdersHome;
