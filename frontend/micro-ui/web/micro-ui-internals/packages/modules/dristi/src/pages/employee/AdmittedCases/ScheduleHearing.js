import React, { useMemo, useState } from "react";
import { useTranslation } from "react-i18next";
import { modalConfig, selectParticipantConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { CloseSvg, Modal } from "@egovernments/digit-ui-react-components";
import AdmissionActionModal from "../admission/AdmissionActionModal";
import { DRISTIService } from "../../../services";

const ScheduleHearing = ({ tenantId, setShowModal, caseData, setUpdateCounter, showToast, advocateDetails, caseAdmittedSubmit }) => {
  const { t } = useTranslation();
  const [modalInfo, setModalInfo] = useState({ type: "schedule", page: 0 });
  const [selectedChip, setSelectedChip] = useState(null);
  const [scheduleHearingParams, setScheduleHearingParams] = useState({});

  const partyTypes = {
    "complainant.primary": "Complainant",
    "complainant.additional": "Complainant",
    "respondent.primary": "Respondent",
    "respondent.additional": "Respondent",
  };

  const scheduleHearing = async (data = {}) => {
    return DRISTIService.createHearings(
      {
        hearing: {
          tenantId: tenantId,
          filingNumber: [caseData.filingNumber],
          hearingType: data.purpose,
          status: true,
          attendees: [
            ...Object.values(data.participant)
              .map((val) => val.attendees?.map((attendee) => JSON.parse(attendee)))
              .flat(Infinity),
            ...advocateDetails,
          ],
          startTime: Date.parse(
            `${data.date
              .split(" ")
              ?.map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          endTime: Date.parse(
            `${data.date
              .split(" ")
              ?.map((date, i) => (i === 0 ? date.slice(0, date.length - 2) : date))
              .join(" ")}`
          ),
          workflow: {
            action: "CREATE",
            assignes: [],
            comments: "Create new Hearing",
            documents: [{}],
          },
          documents: [],
        },
        tenantId,
      },
      { tenantId: tenantId }
    );
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };
  const Heading = (props) => {
    return (
      <div className="evidence-title">
        <h1 className="heading-m">{props.label}</h1>
        <h3 className="status">{props?.status}</h3>
      </div>
    );
  };

  const setPurposeValue = (value, input) => {
    setScheduleHearingParams({ ...scheduleHearingParams, purpose: value });
  };

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const handleClickDate = (label) => {
    const newSelectedChip = selectedChip === label ? null : label;
    setSelectedChip(newSelectedChip);
    setScheduleHearingParams({
      ...scheduleHearingParams,
      date: newSelectedChip,
    });
  };

  const handleScheduleCase = async (props) => {
    await scheduleHearing(props).then((res) => {
      setShowModal(false);
      res.responseInfo.status === "successful"
        ? showToast({
            isError: false,
            message: "HEARING_CREATE_SUCCESSFUL",
          })
        : showToast({
            isError: true,
            message: "HEARING_CREATE_UNSUCCESSFUL",
          });
    });
  };

  const updateConfigWithCaseDetails = (config, caseDetails) => {
    const litigantsNames = caseDetails.litigants?.map((litigant) => {
      return { name: litigant.additionalDetails.fullName, individualId: litigant.individualId, type: partyTypes[litigant.partyType] };
    });
    const witnessNames = caseDetails.additionalDetails.witnessDetails.formdata?.map((data) => {
      return { name: `${data.data.firstName} ${data.data.lastName}`, type: "Witness" };
    });

    config.checkBoxes.forEach((checkbox) => {
      if (checkbox.key === "Litigants" && checkbox.dependentFields) {
        checkbox.dependentFields = litigantsNames;
      } else if (checkbox.key === "Witness" && checkbox.dependentFields) {
        checkbox.dependentFields = witnessNames;
      }
    });

    return config;
  };

  const updatedConfig = caseData.case && updateConfigWithCaseDetails(selectParticipantConfig, caseData.case);

  return (
    <React.Fragment>
      <Modal
        headerBarMain={<Heading label={t("CS_SCHEDULE_HEARING")} />}
        headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
        hideSubmit={true}
        popupStyles={{ width: "917px" }}
      >
        <AdmissionActionModal
          //   config={modalConfig[2]}
          t={t}
          setShowModal={setShowModal}
          setModalInfo={setModalInfo}
          modalInfo={modalInfo}
          selectedChip={selectedChip}
          setSelectedChip={setSelectedChip}
          showCustomDateModal={showCustomDateModal}
          setPurposeValue={setPurposeValue}
          scheduleHearingParams={scheduleHearingParams}
          setScheduleHearingParam={setScheduleHearingParams}
          submitModalInfo={{}}
          handleClickDate={handleClickDate}
          handleScheduleCase={handleScheduleCase}
          handleAdmitCase={() => {}}
          updatedConfig={updatedConfig}
          tenantId={tenantId}
          handleScheduleNextHearing={() => {}}
          disabled={false}
          isCaseAdmitted={true}
          caseAdmittedSubmit={caseAdmittedSubmit}
          scheduleHearing={true}
        />
      </Modal>
    </React.Fragment>
  );
};

export default ScheduleHearing;
