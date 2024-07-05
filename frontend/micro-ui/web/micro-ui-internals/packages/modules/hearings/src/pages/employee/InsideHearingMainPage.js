import { useHistory } from "react-router-dom";
import React, { useEffect, useRef, useState } from "react";
import { ActionBar, Card } from "@egovernments/digit-ui-react-components";
import { Button, TextArea } from "@egovernments/digit-ui-components";
import EvidenceHearingHeader from "./EvidenceHeader";
import HearingSideCard from "./HearingSideCard";
import EndHearing from "./EndHearing";
import MarkAttendance from "./MarkAttendance";
import debounce from "lodash/debounce";
import AddParty from "./AddParty";
import  add  from "lodash/add";

const fieldStyle = { marginRight: 0 };

const InsideHearingMainPage = () => {
  const history = useHistory();
  const [activeTab, setActiveTab] = useState("Transcript/Summary");
  const [immediateText, setImmediateText] = useState("");
  const [hearing, setHearing] = useState({});
  const [delayedText, setDelayedText] = useState("");
  const [witnessDepositionText, setWitnessDepositionText] = useState("");
  const [userRoles, setUserRoles] = useState([]);
  const [options, setOptions] = useState([]);
  const [additionalDetails, setAdditionalDetails] = useState({});
  const [selectedWitness, setSelectedWitness] = useState({});
  const [addPartyModal, setAddPartyModal]= useState(false);

  const [endHearingModalOpen, setEndHearingModalOpen] = useState(false);

  const textAreaRef = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const [attendees, setAttendees] = useState([]);
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { hearingId: hearingId } = Digit.Hooks.useQueryParams(); // query paramas

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

  useEffect(() => {
    const userDetails = JSON.parse(localStorage.getItem("user-info"));
    setUserRoles(userDetails.roles);
  }, []);

  const checkUserApproval = (userRole) => {
    return userRoles.some((role) => role.name === userRole);
  };

  const reqBody = {
    hearing: { tenantId, hearing },
    criteria: {
      tenantID: tenantId,
      hearingId: hearingId,
    },
  };
  const { data: latestText } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", hearingId },
    "dristi",
    !checkUserApproval("CASE_VIEWER"),
    3000
  );

  const { data: hearingResponse, refetch } = Digit.Hooks.hearings.useUpdateHearingsService(
    { tenantId, hearing, hearingType: "", status: "" },
    { applicationNumber: "", cnrNumber: "" },
    "dristi",
    !checkUserApproval("CASE_VIEWER")
  );

  useEffect(() => {
    if (latestText) {
      const hearingData = latestText?.HearingList?.[0];
      // hearing data with particular id will always give array of one object
      if (hearingData) {
        setHearing(hearingData);
        const additionalDetails = hearingData?.additionalDetails || {};
        const processedAdditionalDetails = {
          ...additionalDetails,
          witnesses: additionalDetails.witnesses || [],
        };
        setAdditionalDetails(processedAdditionalDetails);
        setOptions(processedAdditionalDetails.witnesses.map((witness) => ({ label: witness.partyName, value: witness.partyName })));
        setImmediateText(hearingData?.transcript[0]);
        setDelayedText(hearingData?.transcript[0]);
        setSelectedWitness(processedAdditionalDetails.witnesses[0] || {});
        setWitnessDepositionText(processedAdditionalDetails.witnesses[0]?.deposition || "");
        setAttendees(hearingData.attendees || []);
      }
    }
  }, [latestText]);

  const handleModal = () => {
    setIsOpen(!isOpen);
  };

  const updateText = debounce(async (newText) => {
    try {
      setHearing((prevHearing) => {
        if (Object.keys(prevHearing).length === 0) {
          console.warn("Hearing object is empty");
          return prevHearing;
        }

        const updatedHearing = { ...prevHearing };
        if (activeTab === "Witness Deposition") {
          const witnessIndex = updatedHearing.additionalDetails.witnesses.findIndex((w) => w.name === selectedWitness);
          if (witnessIndex >= 0) {
            updatedHearing.additionalDetails.witnesses[witnessIndex].deposition = newText;
          }
        } else {
          updatedHearing.transcript[0] = newText;
        }
        return updatedHearing;
      });

      await refetch(); // calling the update api
      setDelayedText(newText);
      console.log("Updated hearings service successfully");
    } catch (error) {
      console.error("Error updating hearings service:", error);
    }
  }, 3000);

  const handleChange = (e) => {
    const newText = e.target.value;
    if (activeTab === "Witness Deposition") {
      setWitnessDepositionText(newText);
    } else {
      setImmediateText(newText);
      updateText(newText);
    }
  };

  const handleDropdownChange = (event) => {
    const selectedName = event.target.value;
    const selectedWitness = additionalDetails.witnesses.find((w) => w.name === selectedName);
    setSelectedWitness(selectedWitness);
    setWitnessDepositionText(selectedWitness?.deposition || "");
  };

  const handleEndHearingModal = () => {
    setEndHearingModalOpen(!endHearingModalOpen);
  };

  return (
    <div style={{ display: "flex" }}>
      <Card>
        <div style={{ border: "1px", padding: "40px, 40px", gap: "32px" }}>
          <EvidenceHearingHeader setActiveTab={setActiveTab} activeTab={activeTab}></EvidenceHearingHeader>
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
            {checkUserApproval("CASE_VIEWER") ? (
              <TextArea
                ref={textAreaRef}
                style={{ minWidth: "940px", minHeight: "453px" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : immediateText}
                onChange={handleChange}
                disabled={selectedWitness.isSigned}
              />
            ) : (
              <TextArea
                style={{ minWidth: "940px", minHeight: "453px", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                value={activeTab === "Witness Deposition" ? witnessDepositionText : delayedText}
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
                    backgroundColor: checkUserApproval("CASE_VIEWER") ? "#FFFFFF" : "#E0E0E0",
                    color: checkUserApproval("CASE_VIEWER") ? "#007E7E" : "#A0A0A0",
                    fontWeight: 700,
                    padding: "8px 24px 8px 24px",
                    cursor: checkUserApproval("CASE_VIEWER") ? "pointer" : "default",
                    display: "inline-block",
                    fontSize: "16px",
                    width: "175px",
                    height: "40px",
                    marginTop: "10px",
                  }}
                  disabled={!checkUserApproval("CASE_VIEWER")}
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
        <HearingSideCard></HearingSideCard>
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
              Attendance: <strong>03</strong>
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
            <Button
              label={"Adjourn Hearing"}
              variation={"secondary"}
              onClick={() => handleNavigate("/employee/hearings/adjourn-hearing")}
              style={{ width: "100%" }}
            />

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
        {addPartyModal && <AddParty onCancel={onCancel} onDismiss={onCancel} hearing={hearing} tenantId={tenantId} hearingId={hearingId}></AddParty>}
      </div>
      {endHearingModalOpen && <EndHearing handleEndHearingModal={handleEndHearingModal} hearingId={hearingId} hearing={hearing} />}
    </div>
  );
};

export default InsideHearingMainPage;
