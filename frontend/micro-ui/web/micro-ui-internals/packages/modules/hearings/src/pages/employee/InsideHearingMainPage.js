import { Button, TextArea } from "@egovernments/digit-ui-components";
import { ActionBar, CardLabel, Dropdown, LabelFieldPair } from "@egovernments/digit-ui-react-components";
import debounce from "lodash/debounce";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { Urls } from "../../hooks/services/Urls";
import AddParty from "./AddParty";
import AdjournHearing from "./AdjournHearing";
import EndHearing from "./EndHearing";
import EvidenceHearingHeader from "./EvidenceHeader";
import HearingSideCard from "./HearingSideCard";
import MarkAttendance from "./MarkAttendance";

const SECOND = 1000;

const InsideHearingMainPage = () => {
  const history = useHistory();
  const [activeTab, setActiveTab] = useState("Transcript/Summary");
  const [transcriptText, setTranscriptText] = useState("");
  const [hearing, setHearing] = useState({});
  const [witnessDepositionText, setWitnessDepositionText] = useState("");
  const [caseData, setCaseData] = useState(null);
  const [options, setOptions] = useState([]);
  const [additionalDetails, setAdditionalDetails] = useState({});
  const [selectedWitness, setSelectedWitness] = useState({});
  const [addPartyModal, setAddPartyModal] = useState(false);
  const [adjournHearing, setAdjournHearing] = useState(false);
  const [endHearingModalOpen, setEndHearingModalOpen] = useState(false);
  const textAreaRef = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const [attendees, setAttendees] = useState([]);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { hearingId } = Digit.Hooks.useQueryParams();
  const [filingNumber, setFilingNumber] = useState("");
  const { t } = useTranslation();

  const onCancel = () => {
    setAddPartyModal(false);
  };

  const onClickAddWitness = () => {
    setAddPartyModal(true);
  };

  const userType = Digit?.UserService?.getType?.();

  if (!hearingId) {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/${userType}/home/pending-task`);
  }

  const userRoles = Digit?.UserService?.getUser?.()?.info?.roles || [];

  const userHasRole = (userRole) => {
    return userRoles.some((role) => role.code === userRole);
  };

  // if (!userHasRole("HEARING_VIEWER")) {
  //   history.push(`/${window.contextPath}/${userType}/home/home-pending-task`);
  // }

  const reqBody = {
    hearing: { tenantId },
    criteria: {
      tenantID: tenantId,
      hearingId: hearingId,
    },
  };
  const { data: hearingsData } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", hearingId },
    "dristi",
    !!userHasRole("HEARING_VIEWER"),
    10 * SECOND
  );

  const { mutateAsync: _updateTranscriptRequest } = Digit.Hooks.useCustomAPIMutationHook({
    url: Urls.hearing.hearingUpdate,
    params: { applicationNumber: "", cnrNumber: "" },
    body: { tenantId, hearingType: "", status: "" },
    config: {
      mutationKey: "updateTranscript",
    },
  });

  const updateTranscriptRequest = useMemo(() => debounce(_updateTranscriptRequest, 1000), [_updateTranscriptRequest]);

  const { data: caseDataResponse, refetch: refetchCase } = Digit.Hooks.dristi.useSearchCaseService(
    {
      criteria: [
        {
          filingNumber,
        },
      ],
      tenantId,
    },
    {},
    "dristi",
    filingNumber,
    filingNumber
  );

  useEffect(() => {
    if (hearingsData) {
      const hearingData = hearingsData?.HearingList?.[0];
      // hearing data with particular id will always give array of one object
      if (hearingData) {
        setHearing(hearingData);
        setTranscriptText(hearingData?.transcript[0]);
        setAttendees(hearingData.attendees || []);
        setFilingNumber(hearingData?.filingNumber[0]);
      }
    }
  }, [hearingsData]);

  useEffect(() => {
    if (caseDataResponse) {
      setCaseData(caseDataResponse);
      const responseList = caseDataResponse?.criteria?.[0]?.responseList?.[0];
      setAdditionalDetails(responseList?.additionalDetails);
      setOptions(
        responseList?.additionalDetails?.witnessDetails?.formdata?.map((witness) => ({
          label: `${witness.data.firstName} ${witness.data.lastName}`,
          value: witness.data.uuid,
        }))
      );
      const selectedWitness = responseList?.additionalDetails?.witnessDetails?.formdata?.[0]?.data || {};
      setSelectedWitness(selectedWitness);
      setWitnessDepositionText(hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitness.uuid)?.deposition);
    }
  }, [caseDataResponse, hearing]);

  const handleModal = () => {
    setIsOpen(!isOpen);
  };

  const handleChange = (e) => {
    const newText = e.target.value;
    if (activeTab === "Witness Deposition") {
      setWitnessDepositionText(newText);
    } else {
      setTranscriptText(newText);

      if (Object.keys(hearing).length === 0) {
        console.warn("Hearing object is empty");
        return hearing;
      }

      const updatedHearing = structuredClone(hearing);

      if (activeTab === "Witness Deposition") {
        if (!updatedHearing?.additionalDetails?.witnesses) {
          updatedHearing.additionalDetails.witnesses = [];
        }
        const newWitness = {
          uuid: selectedWitness?.data?.uuid,
          name: selectedWitness?.data?.name,
          depositionText: newText,
        };
        updatedHearing.additionalDetails.witnesses.push(newWitness);
      } else {
        updatedHearing.transcript[0] = newText;
      }
      if (userHasRole("EMPLOYEE")) {
        updateTranscriptRequest({ body: { hearing: updatedHearing } });
      }
    }
  };

  const isDepositionSaved = Boolean(
    hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitness.uuid)?.deposition
  );

  const saveWitnessDeposition = () => {
    const updatedHearing = structuredClone(hearing);
    updatedHearing.additionalDetails = updatedHearing.additionalDetails || {};
    updatedHearing.additionalDetails.witnessDepositions = updatedHearing.additionalDetails.witnessDepositions || [];
    if (isDepositionSaved) {
      return;
    }
    updatedHearing.additionalDetails.witnessDepositions.push({
      ...selectedWitness,
      deposition: witnessDepositionText,
    });
    _updateTranscriptRequest({ body: { hearing: updatedHearing } }).then((res) => {
      setHearing(res.hearing);
    });
  };

  const handleDropdownChange = (selectedWitnessOption) => {
    const selectedUUID = selectedWitnessOption.value;
    const selectedWitness = additionalDetails?.witnessDetails?.formdata?.find((w) => w.data.uuid === selectedUUID)?.data || {};
    setSelectedWitness(selectedWitness);
    console.debug(hearing, selectedWitness);
    setWitnessDepositionText(
      hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitness.uuid)?.deposition || ""
    );
  };

  const handleEndHearingModal = () => {
    setEndHearingModalOpen(!endHearingModalOpen);
  };

  const handleExitHearing = () => {
    history.push(`/${window.contextPath}/${userType}/home/home-pending-task`);
  };

  const attendanceCount = useMemo(() => hearing?.attendees?.filter((attendee) => attendee.wasPresent).length || 0, [hearing]);

  return (
    <div className="admitted-case" style={{ display: "flex" }}>
      <div className="left-side" style={{ padding: "24px 40px" }}>
        <React.Fragment>
          <EvidenceHearingHeader
            caseData={caseData?.criteria?.[0]?.responseList?.[0]}
            hearing={hearing}
            setActiveTab={setActiveTab}
            activeTab={activeTab}
            filingNumber={filingNumber}
            onAddParty={onClickAddWitness}
          ></EvidenceHearingHeader>
        </React.Fragment>
        {activeTab === "Witness Deposition" && (
          <div style={{ width: "100%", marginTop: "15px", marginBottom: "10px" }}>
            <LabelFieldPair className="case-label-field-pair">
              <CardLabel className="case-input-label">{`Select Witness`}</CardLabel>
              <Dropdown
                option={options}
                optionKey={"label"}
                select={handleDropdownChange}
                freeze={true}
                disable={false}
                style={{ width: "100%", height: "40px", fontSize: "16px" }}
              />
            </LabelFieldPair>
            {userHasRole("EMPLOYEE") && (
              <div style={{ width: "151px", height: "19px", fontSize: "13px", color: "#007E7E", marginTop: "2px" }}>
                <button
                  style={{
                    background: "none",
                    border: "none",
                    padding: 0,
                    margin: 0,
                    cursor: "pointer",
                    fontSize: "13px",
                    color: "#007E7E",
                    fontWeight: 700,
                  }}
                  onClick={onClickAddWitness}
                >
                  + {t("CASE_ADD_PARTY")}
                </button>
              </div>
            )}
          </div>
        )}
        <div style={{ padding: "40px, 40px", gap: "16px" }}>
          <div style={{ gap: "16px", border: "1px solid", marginTop: "2px" }}>
            {userHasRole("EMPLOYEE") ? (
              <TextArea
                ref={textAreaRef}
                style={{ width: "100%", minHeight: "40vh" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : transcriptText}
                onChange={handleChange}
                disabled={activeTab === "Witness Deposition" && isDepositionSaved}
              />
            ) : (
              <TextArea
                style={{ width: "100%", minHeight: "40vh", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : transcriptText}
                disabled
              />
            )}
          </div>
        </div>
        <div style={{ marginTop: "10px", marginBottom: "50px" }}>
          {activeTab === "Witness Deposition" && userHasRole("EMPLOYEE") && (
            <div>
              <Button
                label={t("SAVE_WITNESS_DEPOSITION")}
                isDisabled={isDepositionSaved}
                onClick={() => {
                  saveWitnessDeposition();
                }}
              ></Button>
            </div>
          )}
        </div>
      </div>
      <div className="right-side">
        <HearingSideCard hearingId={hearingId} caseId={caseData?.criteria?.[0]?.responseList?.[0]?.id} filingNumber={filingNumber}></HearingSideCard>
        {adjournHearing && <AdjournHearing hearing={hearing} updateTranscript={_updateTranscriptRequest} tenantID={tenantId} />}
      </div>
      <ActionBar>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            width: "100%",
          }}
        >
          <div
            style={{
              display: "flex",
              gap: "16px",
            }}
          >
            <button
              style={{
                border: "1px solid blue",
                backgroundColor: "#e6f0ff",
                color: "#1a73e8",
                fontWeight: "bold",
                padding: "10px 20px",
                borderRadius: "5px",
                cursor: "pointer",
                display: "inline-block",
                fontSize: "16px",
              }}
            >
              Attendance: <strong>{attendanceCount}</strong>
            </button>
            {userHasRole("EMPLOYEE") && (
              <Button
                label={"Mark Attendance"}
                variation={"teritiary"}
                onClick={handleModal}
                // onClick={() => handleNavigate("/employee/hearings/mark-attendance")}
                style={{ width: "100%" }}
              />
            )}
          </div>
          {userHasRole("EMPLOYEE") ? (
            <div
              style={{
                display: "flex",
                gap: "16px",
                width: "100%",
              }}
            >
              <Button label={t("ADJOURN_HEARING")} variation={"secondary"} onClick={() => setAdjournHearing(true)} style={{ width: "100%" }} />

              <Button label={t("END_HEARING")} variation={"primary"} onClick={handleEndHearingModal} style={{ width: "100%" }} />
            </div>
          ) : (
            <Button label={t("EXIT_HEARING")} variation={"primary"} onClick={handleExitHearing} />
          )}
          {isOpen && (
            <MarkAttendance
              handleModal={handleModal}
              attendees={attendees}
              setAttendees={setAttendees}
              hearingData={hearing}
              setAddPartyModal={setAddPartyModal}
            />
          )}
        </div>
      </ActionBar>

      <div>
        {addPartyModal && (
          <AddParty
            onCancel={onCancel}
            onAddSuccess={() => {
              refetchCase();
            }}
            caseData={caseData}
            tenantId={tenantId}
            hearingId={hearingId}
          ></AddParty>
        )}
      </div>
      {endHearingModalOpen && <EndHearing handleEndHearingModal={handleEndHearingModal} hearingId={hearingId} hearing={hearing} />}
    </div>
  );
};

export default InsideHearingMainPage;
