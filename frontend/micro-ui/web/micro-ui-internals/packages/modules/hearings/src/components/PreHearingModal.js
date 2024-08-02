import { Button, CloseSvg, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../dristi/src/components/Modal";
import { preHearingConfig } from "../configs/PreHearingConfig";
import { hearingService } from "../hooks/services";
import { ReschedulingPurpose } from "../pages/employee/ReschedulingPurpose";

function PreHearingModal({ onCancel, hearingData, courtData, individualId = "", userType }) {
  const { t } = useTranslation();
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const [totalCount, setTotalCount] = useState(null);
  const [purposeModalOpen, setPurposeModalOpen] = useState(false);
  const [purposeModalData, setPurposeModalData] = useState(false);

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const openRescheduleModal = (caseDetails) => {
    setPurposeModalData(caseDetails);
    setPurposeModalOpen(true);
  };

  const updatedConfig = useMemo(() => {
    const configCopy = structuredClone(preHearingConfig);
    configCopy.apiDetails.requestParam = {
      ...configCopy.apiDetails.requestParam,
      fromDate: hearingData.fromDate,
      toDate: hearingData.toDate,
      slot: hearingData.slot,
    };
    configCopy.additionalDetails = {
      attendeeIndividualId: userType === "LITIGANT" && individualId,
    };
    configCopy.sections.searchResult.uiConfig.columns = [
      ...configCopy.sections.searchResult.uiConfig.columns.map((column) => {
        return column.label === "Actions"
          ? {
              ...column,
              openRescheduleDialog: openRescheduleModal,
            }
          : column;
      }),
    ];
    return configCopy;
  }, [hearingData.fromDate, hearingData.toDate, hearingData.slot]);

  const getTotalCount = useCallback(
    async function () {
      const response = await hearingService
        .searchHearings(
          {
            criteria: {
              ...updatedConfig?.apiDetails?.requestBody?.criteria?.[0],
              tenantId,
              fromDate: hearingData.fromDate,
              toDate: hearingData.toDate,
              slot: hearingData.slot,
              attendeeIndividualId: individualId,
            },
          },
          {
            tenantId: tenantId,
          }
        )
        .catch(() => {
          return {};
        });
      setTotalCount(response?.TotalCount);
    },
    [updatedConfig, tenantId]
  );

  useEffect(() => {
    getTotalCount();
  }, [updatedConfig, tenantId]);

  const popUpStyle = {
    width: "70%",
    height: "fit-content",
    borderRadius: "0.3rem",
  };

  const onRescheduleAllClick = () => {
    const contextPath = window?.contextPath || "";
    window.location.href = `/${contextPath}/employee/hearings/reschedule-hearing`;
  };

  const closeFunc = () => {
    setPurposeModalOpen(false);
    setPurposeModalData({});
  };

  if (!totalCount && totalCount !== 0) {
    return null;
  }

  if (userType === "LITIGANT" && !individualId) {
    return <Loader />;
  }
  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Reschedule All Hearings")}
      formId="modal-action"
      headerBarMain={<Heading label={totalCount ? t("ADMISSION_HEARINGS") + ` (${totalCount})` : t("ADMISSION_HEARINGS")} />}
      className="pre-hearings"
      popupStyles={popUpStyle}
      popupModuleActionBarStyles={{
        display: "none",
      }}
    >
      <div style={{ minHeight: "80vh" }}>
        <InboxSearchComposer configs={updatedConfig} />
      </div>
      <div
        style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "1rem 0 0 0", borderTop: "1px solid lightgray" }}
      >
        <div>
          <strong>{hearingData.fromDate}</strong>, {hearingData.slot}
        </div>
        <Button
          className="border-none dristi-font-bold"
          onButtonClick={onRescheduleAllClick}
          label="Reschedule All Hearings"
          variation={"secondary"}
        />
      </div>
      {purposeModalOpen && <ReschedulingPurpose courtData={courtData} closeFunc={closeFunc} caseDetails={purposeModalData} />}
    </Modal>
  );
}

export default PreHearingModal;
