import { Banner, Card, CardLabel, CardText, CloseSvg, Modal, TextArea } from "@egovernments/digit-ui-react-components";
import React, { useMemo, useState } from "react";
import Button from "../../../components/Button";
import { useHistory, useLocation } from "react-router-dom/cjs/react-router-dom.min";
import CustomCopyTextDiv from "../../../components/CustomCopyTextDiv";
import SelectCustomNote from "../../../components/SelectCustomNote";
import { Urls } from "../../../hooks";

const customNoteConfig = {
  populators: {
    inputs: [
      {
        infoHeader: "CS_COMMON_NOTE",
        infoText: "PAYMENT_FAILED_NOTE_MSG",
        infoTooltipMessage: "CS_NOTE_TOOLTIP_CASE_TYPE",
      },
    ],
  },
};

const mockSubmitModalInfo = {
  header: "CS_PAYMENT_SUCCESSFUL",
  subHeader: "CS_PAYMENT_SUCCESSFUL_SUB_TEXT",
  backButtonText: "Back to Home",
  nextButtonText: "Schedule next hearing",
  isArrow: false,
  showTable: true,
};

const CloseBtn = (props) => {
  return (
    <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
      <CloseSvg />
    </div>
  );
};

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

function EFilingPaymentResponse({ t, setShowModal, header, subHeader, submitModalInfo = mockSubmitModalInfo, amount = 2000, path }) {
  const history = useHistory();
  const location = useLocation();
  const receiptData = location.state.state.receiptData;
  const isSuccess = location.state.state.success;
  const fileStoreId = location.state.state.fileStoreId;
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const caseId = location.state.state.caseId;

  const uri = `${window.location.origin}${Urls.FileFetchById}?tenantId=${tenantId}&fileStoreId=${fileStoreId}`;
  const commonProps = {
    whichSvg: "tick",
    headerStyles: { fontSize: "32px" },
    style: { minWidth: "100%", marginTop: "10px" },
  };

  const bannerProps = isSuccess
    ? {
        ...commonProps,
        successful: true,
        message: t(submitModalInfo?.header),
      }
    : {
        ...commonProps,
        successful: false,
        message: t("CS_PAYMENT_FAILED"),
      };

  return (
    <div className=" user-registration">
      <div className="e-filing-payment" style={{ minHeight: "100%", height: "100%" }}>
        <Banner {...bannerProps} />
        {submitModalInfo?.subHeader && isSuccess && <CardLabel className={"e-filing-card-label"}>{t(submitModalInfo?.subHeader)}</CardLabel>}
        {receiptData ? (
          <CustomCopyTextDiv
            t={t}
            keyStyle={{ margin: "8px 0px" }}
            valueStyle={{ margin: "8px 0px", fontWeight: 700 }}
            data={receiptData?.caseInfo}
            tableDataClassName={"e-filing-table-data-style"}
            tableValueClassName={"e-filing-table-value-style"}
          />
        ) : (
          <SelectCustomNote t={t} config={customNoteConfig} />
        )}
        <div className="button-field" style={{ width: "100%", marginTop: 16 }}>
          {!fileStoreId && caseId ? (
            <Button
              variation={"secondary"}
              className={"secondary-button-selector"}
              label={t("Retry Payment")}
              labelClassName={"secondary-label-selector"}
              onButtonClick={() => {
                history.push(`${path}/e-filing-payment?caseId=${caseId}`);
              }}
            />
          ) : (
            <a
              href={uri}
              target="_blank"
              rel="noreferrer"
              style={{
                display: "flex",
                color: "#505A5F",
                textDecoration: "none",
                // width: 250,
                whiteSpace: "nowrap",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}
            >
              <Button
                variation={"secondary"}
                className={"secondary-button-selector"}
                label={t("CS_PRINT_RECEIPT")}
                labelClassName={"secondary-label-selector"}
                onButtonClick={() => {}}
              />
            </a>
          )}

          <Button
            className={"tertiary-button-selector"}
            label={t("CS_GO_TO_HOME")}
            labelClassName={"tertiary-label-selector"}
            onButtonClick={() => {
              history.push(`/${window?.contextPath}/citizen/dristi/home`);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default EFilingPaymentResponse;
