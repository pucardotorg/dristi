import { Card, Header } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import CustomCard from "./CustomCard";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import JudgeScreen from "../pages/employee/Judge/JudgeScreen";
import { useTranslation } from "react-i18next";

const DRISTICard = () => {
  const { t } = useTranslation();
  const Digit = useMemo(() => window?.Digit || {}, []);
  const history = useHistory();
  const roles = Digit.UserService.getUser()?.info?.roles;
  const isJudge = useMemo(() => roles.some((role) => role.code === "CASE_APPROVER"), [roles]);
  const isScrutiny = useMemo(() => roles.some((role) => role.code === "CASE_REVIEWER"), [roles]);
  const isCourtOfficer = useMemo(() => roles.some((role) => role.code === "HEARING_CREATOR"), [roles]);
  const isBenchClerk = useMemo(() => roles.some((role) => role.code === "BENCHCLERK_ROLE"), [roles]);
  const isCitizen = useMemo(() => Boolean(Digit?.UserService?.getUser()?.info?.type === "CITIZEN"), [Digit]);
  const isNyayMitra = ["ADVOCATE_APPLICATION_VIEWER", "ADVOCATE_APPROVER", "ADVOCATE_CLERK_APPROVER"].reduce((res, curr) => {
    if (!res) return res;
    res = roles.some((role) => role.code === curr);
    return res;
  }, true);

  if (isScrutiny || isJudge || isCourtOfficer || isBenchClerk) {
    history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
  } else if (isCitizen) {
    history.push(`/${window?.contextPath}/citizen/home/home-pending-task`);
  }

  let roleType = isJudge ? "isJudge" : "default";
  return (
    <React.Fragment>
      {(() => {
        switch (roleType) {
          case "isJudge":
            return (
              <div className={"file-case-main"}>
                <JudgeScreen path={`/digit-ui/employee/dristi`} />
              </div>
            );
          default:
            return (
              <Card className="main-card-home">
                <Header className="main-card-header">{t("WHAT_DO_YOU_WISH_TO_DO")}</Header>
                <div className="main-inner-div">
                  <CustomCard
                    label={t("CS_VIEW_REGISTRATION")}
                    subLabel={t("CS_VIEW_REGISTRATION_SUB_TEXT")}
                    buttonLabel={t("CS_VIEW_PENDING_REQUESTS")}
                    className="custom-card-style"
                    onClick={() => {
                      history.push("/digit-ui/employee/dristi/registration-requests");
                    }}
                  />
                  <CustomCard
                    label={isNyayMitra ? t("CS_VIEW_PENDING_PAYMENTS") : t("CS_VIEW_CASES")}
                    subLabel={isNyayMitra ? t("CS_VIEW_PENDING_PAYMENTS_SUB_TEXT") : t("CS_VIEW_CASES_SUB_TEXT")}
                    buttonLabel={isNyayMitra ? t("CS_VIEW_PENDING_PAYMENTS") : t("CS_VIEW_CASES")}
                    className="custom-card-style"
                    onClick={() => {
                      isNyayMitra ? history.push("/digit-ui/employee/dristi/pending-payment-inbox") : history.push("/digit-ui/employee/dristi/cases");
                    }}
                  />
                </div>
              </Card>
            );
        }
      })()}
    </React.Fragment>
  );
};

export default DRISTICard;
