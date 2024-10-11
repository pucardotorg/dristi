import { Button, Card, Loader } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { JudgementIcon } from "../../../icons/svgIndex";
import PublishedOrderModal from "./PublishedOrderModal";
import useGetOrders from "../../../hooks/dristi/useGetOrders";
import useDownloadCasePdf from "../../../hooks/dristi/useDownloadCasePdf";

const JudgementViewCard = ({ caseData, width }) => {
  const { t } = useTranslation();
  const [showFinalOutcomeOrder, setShowFinalOutcomeOrder] = useState(false);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const { downloadPdf } = useDownloadCasePdf();

  const { data: outcomeOrderMapping, isLoading: isOutcomeTypeLoading } = Digit.Hooks.useCustomMDMS(Digit.ULBService.getStateId(), "case", [
    { name: "OutcomeType" },
  ]);

  const { data: ordersRes, refetch: refetchOrdersData, isLoading: isOrdersLoading } = useGetOrders(
    {
      criteria: {
        filingNumber: caseData?.filingNumber,
        tenantId: tenantId,
      },
    },
    {},
    caseData?.filingNumber,
    caseData?.filingNumber
  );

  const finalOutcomeOrderType = useMemo(() => {
    let orderType = "";
    orderType = outcomeOrderMapping?.case?.OutcomeType?.find((obj) => {
      if (obj?.judgementList.length === 0) {
        if (obj?.outcome === caseData?.case?.outcome) {
          return true;
        }
      } else {
        if (obj?.judgementList?.find((o) => o === caseData?.case?.outcome)) {
          return true;
        }
      }
    })?.orderType;
    return orderType;
  }, [outcomeOrderMapping, caseData]);

  const finalOutcomeOrder = useMemo(() => {
    return ordersRes?.list?.filter((order) => order?.orderType === finalOutcomeOrderType)?.[0];
  }, [ordersRes, finalOutcomeOrderType]);

  const handleButtonClick = () => {
    setShowFinalOutcomeOrder(true);
  };
  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    { criteria: { tenantId }, tenantId },
    { applicationNumber: "", cnrNumber: "", tenantId },
    "dristi",
    true
  );
  const hearingsList = useMemo(() => hearingsData?.HearingList?.sort((a, b) => b.startTime - a.startTime), [hearingsData]);
  const formatDate = (date, format) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    if (format === "DD-MM-YYYY") {
      return `${day}-${month}-${year}`;
    }
    return `${year}-${month}-${day}`;
  };

  const handleCloseJudgementOrder = () => {
    setShowFinalOutcomeOrder(false);
  };

  const handleDownload = (filestoreId) => {
    if (filestoreId) {
      downloadPdf(tenantId, filestoreId);
    }
  };

  if (isOutcomeTypeLoading || isOrdersLoading) {
    return <Loader></Loader>;
  }

  return (
    <React.Fragment>
      <Card
        style={{
          width: width,
          marginTop: "10px",
        }}
      >
        <div
          style={{
            fontWeight: 700,
            fontSize: "16px",
            lineHeight: "18.75px",
            color: "#231F20",
            display: "flex",
            alignItems: "center",
            gap: 15,
          }}
        >
          <JudgementIcon />
          {t("FINAL_JUDGEMENT")}
        </div>
        <hr style={{ border: "1px solid #FFF6E880" }} />
        <div style={{ display: "flex", justifyContent: "space-between", padding: "10px" }}>
          <div style={{ display: "flex", gap: "10px", justifyContent: "start", flexDirection: "column" }}>
            <div
              style={{
                fontWeight: 700,
                fontSize: "24px",
                lineHeight: "28.13px",
                color: "#231F20",
                marginTop: "5px",
              }}
            >
              {t(caseData?.case?.outcome)}
            </div>
            <div
              style={{
                fontWeight: 400,
                fontSize: "14px",
                lineHeight: "16.41px",
                color: "#3D3C3C",
                marginTop: "5px",
              }}
            >
              {formatDate(new Date(hearingsList?.[hearingsList?.length - 1]?.startTime), "DD-MM-YYYY")}
            </div>
          </div>
          <Button variation={"outlined"} onButtonClick={handleButtonClick} label={`View ${t(finalOutcomeOrderType)} Order`} />
        </div>
      </Card>
      {showFinalOutcomeOrder && (
        <PublishedOrderModal t={t} order={finalOutcomeOrder} handleDownload={handleDownload} handleOrdersTab={handleCloseJudgementOrder} />
      )}
    </React.Fragment>
  );
};

export default JudgementViewCard;
