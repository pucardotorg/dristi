import { Button, Card, CardHeader, CardText, Loader } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { WaitIcon } from "../../../icons/svgIndex";
import { userTypeOptions } from "../registration/config";

function ApplicationAwaitingPage({ individualId }) {
  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const applicationNo = urlParams.get("applicationNo");
  const type = urlParams.get("type") || "advocate";
  const moduleCode = "DRISTI";
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const [isFetching, setIsFetching] = useState(true);
  const [isFetchingAdvoacte, setIsFetchingAdvocate] = useState(true);
  const { data: individualData, isLoading: isGetUserLoading, refetch: refetch } = window?.Digit.Hooks.dristi.useGetIndividualUser(
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

  const { data: searchData, isLoading: isSearchLoading, refetch: refetchAdvocateClerk } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [applicationNo ? { applicationNumber: applicationNo } : { individualId }],
      tenantId,
    },
    {},
    applicationNo + individualId,
    userType,
    userType === "ADVOCATE" ? "/advocate/v1/_search" : "/advocate/clerk/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item.code === userType) || {};
  }, [userType]);
  const searchResult = useMemo(() => {
    return searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey]);

  const applicationNumber = useMemo(() => {
    return searchResult?.[0]?.applicationNumber;
  }, [searchResult]);
  useEffect(() => {
    refetch().then(() => {
      refetchAdvocateClerk().then(() => {
        setIsFetchingAdvocate(false);
      });
      setIsFetching(false);
    });
  }, []);

  if (isGetUserLoading || isSearchLoading || isFetching || isFetchingAdvoacte) {
    return <Loader />;
  }
  return (
    <Card className="response-main">
      <div style={{ maxHeight: "40vh" }}>
        <WaitIcon />
      </div>
      <div style={{ "text-align": "center" }}>
        <CardHeader> {t("APPROVAL_WAITING")}</CardHeader>
      </div>
      <div style={{ "text-align": "center", maxWidth: "50%" }}>
        <CardText>{`${t("RG_YOUR_REGISTRATION_NUMBER")} (ID: ${applicationNumber}).${t("RG_RECEIVE_MESSAGE")}`}</CardText>
      </div>
      <div>
        <Button
          onButtonClick={() => {
            history.push(`/digit-ui/citizen/dristi/home/application-details?individualId=${individualId}&applicationNo=${applicationNumber}`);
          }}
          label={t("VIEW_MY_APPLICATION")}
          style={{
            flex: 1,
            boxShadow: "none",
          }}
        ></Button>
      </div>
    </Card>
  );
}

export default ApplicationAwaitingPage;
