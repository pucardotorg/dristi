import { Button, TextArea } from "@egovernments/digit-ui-components";
import { ActionBar, Card } from "@egovernments/digit-ui-react-components";
import debounce from "lodash/debounce";
import React, { useEffect, useMemo, useRef, useState } from "react";
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

  const onCancel = () => {
    setAddPartyModal(false);
  };

  const onClickAddWitness = () => {
    setAddPartyModal(true);
  };

  if (!hearingId) {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}/employee/hearings/home`);
  }

  const userDetails = JSON.parse(localStorage.getItem("user-info"));
  const userRoles = userDetails.roles;
  const userType = userDetails.type === "CITIZEN" ? "citizen" : "employee";

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

  const { mutate: _updateTranscriptRequest } = Digit.Hooks.useCustomAPIMutationHook({
    url: Urls.hearing.hearingUpdate,
    params: { applicationNumber: "", cnrNumber: "" },
    body: { tenantId, hearing, hearingType: "", status: "" },
    config: {
      mutationKey: "updateTranscript",
    },
  });

  const updateTranscriptRequest = useMemo(() => debounce(_updateTranscriptRequest, 1000), [_updateTranscriptRequest]);

  const { data: caseDataResponse } = Digit.Hooks.dristi.useSearchCaseService(
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
        responseList?.additionalDetails?.witnessDetails?.formdata?.map((data) => ({
          label: `${data.data.firstName} ${data.data.lastName}`,
          value: `${data.data.firstName} ${data.data.lastName}`,
        }))
      );
      setSelectedWitness(responseList?.additionalDetails?.witnessDetails?.formdata?.[0]?.data || {});
      setWitnessDepositionText(responseList?.additionalDetails?.witnessDetails?.formdata?.[0]?.data?.deposition || "");
    }
  }, [caseDataResponse]);

  const handleModal = () => {
    setIsOpen(!isOpen);
  };

  const handleChange = (e) => {
    const newText = e.target.value;
    if (activeTab === "Witness Deposition") {
      setWitnessDepositionText(newText);
    } else {
      setTranscriptText(newText);
      setHearing((prevHearing) => {
        if (Object.keys(prevHearing).length === 0) {
          console.warn("Hearing object is empty");
          return prevHearing;
        }

        const updatedHearing = structuredClone(prevHearing);

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
        if (userHasRole("EMPLOYEE") || userHasRole("JUDGE")) {
          updateTranscriptRequest(updatedHearing);
        }
        return updatedHearing;
      });
    }
  };

  const handleDropdownChange = (event) => {
    const selectedName = event.target.value;
    const selectedWitness = additionalDetails?.witnessDetails?.formdata?.find((w) => w.data.name === selectedName);
    setSelectedWitness(selectedWitness);
    setWitnessDepositionText(selectedWitness?.data?.deposition || "");
  };

  const handleEndHearingModal = () => {
    setEndHearingModalOpen(!endHearingModalOpen);
  };

  const attendanceCount = useMemo(() => hearing?.attendees?.filter((attendee) => attendee.wasPresent).length || 0, [hearing]);

  return (
    <div style={{ display: "flex" }}>
      <Card>
        <div style={{ border: "1px", padding: "40px, 40px", gap: "32px" }}>
          <EvidenceHearingHeader
            caseData={caseData}
            hearing={hearing}
            setActiveTab={setActiveTab}
            activeTab={activeTab}
            filingNumber={filingNumber}
            onAddParty={onClickAddWitness}
          ></EvidenceHearingHeader>
        </div>
        {activeTab === "Witness Deposition" && (
          <div style={{ width: "100%", marginTop: "15px", marginBottom: "10px" }}>
            <label htmlFor="dropdown">Select Witness</label>
            <select
              id="dropdown"
              onChange={handleDropdownChange}
              style={{ width: "940px", height: "40px", border: "1px solid #3D3C3C", fontSize: "16px" }}
              // disabled={!checkUserApproval("HEARING_SCHEDULER")} // Disable if user doesn't have approval
            >
              {options.map((option) => (
                <option key={option.value} value={option.value}>
                  &nbsp;{option.label}
                </option>
              ))}
            </select>
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
                + Add New Witness
              </button>
            </div>
          </div>
        )}
        <div style={{ padding: "40px, 40px", gap: "16px" }}>
          <div style={{ minWidth: "940px", minHeight: "277px", gap: "16px", border: "1px solid", marginTop: "2px" }}>
            {userHasRole("EMPLOYEE") || userHasRole("JUDGE") ? (
              <TextArea
                ref={textAreaRef}
                style={{ minWidth: "940px", minHeight: "453px" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : transcriptText}
                onChange={handleChange}
                disabled={selectedWitness.isSigned}
              />
            ) : (
              <TextArea
                style={{ minWidth: "940px", minHeight: "453px", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : transcriptText}
                readOnly
              />
            )}
          </div>
        </div>
        <div style={{ marginTop: "10px", marginBottom: "50px" }}>
          {activeTab === "Witness Deposition" && (
            <div>
              {selectedWitness.isSigned ? (
                <div
                  style={{
                    marginTop: "10px",
                    color: "#007E7E",
                    fontWeight: "bold",
                    width: "180px",
                    height: "24px",
                    display: "flex",
                    alignItems: "center",
                  }}
                >
                  <svg
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    style={{
                      width: "20px",
                      height: "20px",
                      marginRight: "10px",
                    }}
                  >
                    <circle cx="12" cy="12" r="12" fill="#007E7E" />
                    <path d="M7 12L10 15L17 8" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                  Signature Added
                </div>
              ) : (
                <button
                  style={{
                    border: "1px solid #007E7E",
                    backgroundColor: userHasRole("CASE_VIEWER") ? "#FFFFFF" : "#E0E0E0",
                    color: userHasRole("CASE_VIEWER") ? "#007E7E" : "#A0A0A0",
                    fontWeight: 700,
                    padding: "8px 24px 8px 24px",
                    cursor: userHasRole("CASE_VIEWER") ? "pointer" : "default",
                    display: "inline-block",
                    fontSize: "16px",
                    width: "175px",
                    height: "40px",
                    marginTop: "10px",
                  }}
                  disabled={!userHasRole("CASE_VIEWER")}
                  // onClick={() => console.log(witnessDepositionText)}  // for modal for add signature
                >
                  Add Signature
                </button>
              )}
            </div>
          )}
        </div>
      </Card>
      <Card>
        <HearingSideCard hearingId={hearingId} caseId={caseData?.criteria?.[0]?.responseList?.[0]?.id} filingNumber={filingNumber}></HearingSideCard>
        {adjournHearing && <AdjournHearing hearing={hearing} tenantID={tenantId} />}
      </Card>
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
                width: "100%",
              }}
            >
              Attendance: <strong>{attendanceCount}</strong>
            </button>
            <Button
              label={"Mark Attendance"}
              variation={"teritiary"}
              onClick={handleModal}
              // onClick={() => handleNavigate("/employee/hearings/mark-attendance")}
              style={{ width: "100%" }}
            />
          </div>
          <div
            style={{
              display: "flex",
              gap: "16px",
              width: "100%",
            }}
          >
            <Button label={"Adjourn Hearing"} variation={"secondary"} onClick={() => setAdjournHearing(true)} style={{ width: "100%" }} />

            <Button
              label={"End Hearing"}
              variation={"primary"}
              onClick={handleEndHearingModal}
              // onClick={() => handleNavigate("/employee/hearings/end-hearing")}
              style={{ width: "100%" }}
            />
          </div>
          {isOpen && (
            <MarkAttendance
              handleModal={handleModal}
              attendees={attendees}
              setAttendees={setAttendees}
              hearing={hearing}
              setAddPartyModal={setAddPartyModal}
            />
          )}
        </div>
      </ActionBar>

      <div>
        {addPartyModal && (
          <AddParty onCancel={onCancel} onDismiss={onCancel} caseData={caseData} tenantId={tenantId} hearingId={hearingId}></AddParty>
        )}
      </div>
      {endHearingModalOpen && <EndHearing handleEndHearingModal={handleEndHearingModal} hearingId={hearingId} hearing={hearing} />}
    </div>
  );
};

export default InsideHearingMainPage;
