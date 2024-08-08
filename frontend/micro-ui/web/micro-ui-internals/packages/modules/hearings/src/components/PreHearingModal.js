import { Button, CloseSvg, InboxSearchComposer, Loader } from "@egovernments/digit-ui-react-components";
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import Modal from "../../../dristi/src/components/Modal";
import { preHearingConfig } from "../configs/PreHearingConfig";
import { hearingService } from "../hooks/services";
import { ReschedulingPurpose } from "../pages/employee/ReschedulingPurpose";
import { formatDate } from "../utils";

function PreHearingModal({ onCancel, hearingData, courtData, individualId, userType }) {
  const { t } = useTranslation();
  const tenantId = useMemo(() => window?.Digit.ULBService.getCurrentTenantId(), []);
  const [totalCount, setTotalCount] = useState(null);
  const [purposeModalOpen, setPurposeModalOpen] = useState(false);
  const [purposeModalData, setPurposeModalData] = useState({});
  const [rescheduleAll, setRescheduleAll] = useState(false);

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
      attendeeIndividualId: userType === "citizen" && individualId,
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
  }, [hearingData.fromDate, hearingData.toDate, hearingData.slot, userType, individualId]);

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
    setRescheduleAll(true);
    openRescheduleModal(hearingData);
  };

  const closeFunc = () => {
    setPurposeModalOpen(false);
    setPurposeModalData({});
  };

  if (!totalCount && totalCount !== 0) {
    return null;
  }

  if (userType === "citizen" && !individualId) {
    return <Loader />;
  }

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Reschedule All Hearings")}
      formId="modal-action"
      headerBarMain={<Heading label={`${t("TOTAL_HEARINGS")} (${hearingData.count})`} />}
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
          <strong>{formatDate(new Date(hearingData.fromDate))}</strong>, {hearingData.slot}
        </div>
        {Digit.UserService.getType() === "employee" && (
          <Button
            className="border-none dristi-font-bold"
            onButtonClick={onRescheduleAllClick}
            label={t("RESCHEDULE_ALL_HEARINGS")}
            variation={"secondary"}
          />
        )}
      </div>
      {purposeModalOpen && (
        <ReschedulingPurpose rescheduleAll={rescheduleAll} courtData={courtData} closeFunc={closeFunc} caseDetails={purposeModalData} />
      )}
    </Modal>
  );
}

export default PreHearingModal;
