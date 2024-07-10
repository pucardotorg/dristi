import { BackButton, HelpOutlineIcon, PrivateRoute, Toast } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { Switch } from "react-router-dom";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
import Breadcrumb from "../../components/BreadCrumb";
import { useToast } from "../../components/Toast/useToast";
import AdmittedCases from "./AdmittedCases/AdmittedCase";
import ApplicationDetails from "./ApplicationDetails";
import EFilingPaymentResponse from "./Payment/EFilingPaymentResponse";
import PaymentInbox from "./Payment/PaymentInbox";
import ViewPaymentDetails from "./Payment/ViewPaymentDetails";
import CaseFileAdmission from "./admission/CaseFileAdmission";
import Home from "./home";
import ViewCaseFile from "./scrutiny/ViewCaseFile";

const EmployeeApp = ({ path, url, userType, tenants, parentRoute }) => {
  const { t } = useTranslation();
  const location = useLocation();
  const { toastMessage, toastType, closeToast } = useToast();
  const Inbox = window?.Digit?.ComponentRegistryService?.getComponent("Inbox");
  const hideHomeCrumb = [`${path}/cases`];
  const roles = window?.Digit.UserService.getUser()?.info?.roles;
  const isJudge = roles.some((role) => role.code === "CASE_APPROVER");
  const employeeCrumbs = [
    {
      path: `/digit-ui/employee`,
      content: t("ES_COMMON_HOME"),
      show: !hideHomeCrumb.includes(location.pathname),
      isLast: false,
    },
    {
      path: `${path}/view-case`,
      content: t("VIEW_CASE"),
      show: location.pathname.includes("/view-case"),
      isLast: true,
    },
    {
      path: `${path}/registration-requests`,
      content: t("ES_REGISTRATION_REQUESTS"),
      show: location.pathname.includes("/registration-requests"),
      isLast: !location.pathname.includes("/details"),
    },
    {
      path: `${path}/pending-payment-inbox`,
      content: t("CS_PENDING_PAYMENT_INBOX"),
      show: location.pathname.includes("/pending-payment-inbox"),
      isLast: !location.pathname.includes("/pending-payment-inbox"),
    },
    {
      path: `${path}/pending-payment-inbox/pending-payment-details`,
      content: t("CS_PENDING_PAYMENT_DETAILS"),
      show: location.pathname.includes("/pending-payment-details"),
      isLast: true,
    },
    {
      path: ``,
      content: t("ES_APPLICATION_DETAILS"),
      show: location.pathname.includes("/registration-requests/details"),
      isLast: true,
    },
  ];
  return (
    <Switch>
      <React.Fragment>
        <div className="ground-container">
          {!location.pathname.endsWith("/registration-requests") &&
            !location.pathname.includes("/pending-payment-inbox") &&
            !location.pathname.includes("/case") &&
            location.search.includes("?caseId") &&
            !location.pathname.includes("/employee/dristi/admission") &&
            !location.pathname.includes("/view-case") && (
              <div className="back-button-home">
                <BackButton />
                {!isJudge && (
                  <span style={{ display: "flex", justifyContent: "right", gap: "5px" }}>
                    <span style={{ color: "#f47738" }}>Help</span>
                    <HelpOutlineIcon />
                  </span>
                )}
              </div>
            )}
          {(location.pathname.includes("/pending-payment-inbox") || location.pathname.includes("/view-case")) && (
            <Breadcrumb crumbs={employeeCrumbs} breadcrumbStyle={{ paddingLeft: 20 }}></Breadcrumb>
          )}
          <PrivateRoute exact path={`${path}/registration-requests`} component={Inbox} />
          <PrivateRoute exact path={`${path}/registration-requests/details`} component={(props) => <ApplicationDetails {...props} />} />
          <PrivateRoute exact path={`${path}/pending-payment-inbox`} component={PaymentInbox} />
          <PrivateRoute exact path={`${path}/pending-payment-inbox/response`} component={EFilingPaymentResponse} />
          <PrivateRoute exact path={`${path}/pending-payment-inbox/pending-payment-details`} component={ViewPaymentDetails} />
          <div className={location.pathname.endsWith("employee/dristi/cases") ? "file-case-main" : ""}></div>
          <PrivateRoute exact path={`${path}/cases`} component={Home} />
          <PrivateRoute exact path={`${path}/admission`} component={(props) => <CaseFileAdmission {...props} t={t} path={path} />} />
          <PrivateRoute exact path={`${path}/home/view-case`} component={(props) => <AdmittedCases />} />
          <PrivateRoute exact path={`${path}/case`} component={(props) => <ViewCaseFile {...props} t={t} />} />
        </div>
        {toastMessage && (
          <Toast
            style={{ right: 24, left: "unset" }}
            label={toastMessage}
            onClose={closeToast}
            {...(toastType !== "success" && { [toastType]: true })}
          />
        )}
      </React.Fragment>
    </Switch>
  );
};

export default EmployeeApp;
