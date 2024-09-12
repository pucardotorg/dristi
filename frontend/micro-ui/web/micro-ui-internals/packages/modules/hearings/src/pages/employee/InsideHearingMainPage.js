import { TextArea } from "@egovernments/digit-ui-components";
import { ActionBar, CardLabel, Dropdown, LabelFieldPair, Button } from "@egovernments/digit-ui-react-components";
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
import WitnessModal from "../../components/WitnessModal";
import { hearingService } from "../../hooks/services";
import useGetHearingLink from "../../hooks/hearings/useGetHearingLink";
import isEmpty from "lodash/isEmpty";

import TranscriptComponent from "./Transcription";
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
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const { hearingId } = Digit.Hooks.useQueryParams();
  const [filingNumber, setFilingNumber] = useState("");
  const [witnessModalOpen, setWitnessModalOpen] = useState(false);
  const [signedDocumentUploadID, setSignedDocumentUploadID] = useState("");
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

  const disableTextArea = !userHasRole("EMPLOYEE");
  // if (!userHasRole("HEARING_VIEWER")) {
  //   history.push(`/${window.contextPath}/${userType}/home/home-pending-task`);
  // }

  const { data: hearingLink } = useGetHearingLink();
  const hearingVcLink = hearingLink?.[0];
  const refetchTime = disableTextArea ? 10 * SECOND : "";
  const reqBody = {
    hearing: { tenantId },
    criteria: {
      tenantID: tenantId,
      hearingId: hearingId,
    },
  };
  const { data: hearingsData, refetch: refetchHearing = () => {} } = Digit.Hooks.hearings.useGetHearings(
    reqBody,
    { applicationNumber: "", cnrNumber: "", hearingId },
    "dristi",
    true,
    refetchTime
  );

  const { mutateAsync: _updateTranscriptRequest } = Digit.Hooks.useCustomAPIMutationHook({
    url: Urls.hearing.hearingUpdateTranscript,
    params: { applicationNumber: "", cnrNumber: "" },
    body: { tenantId, hearingType: "", status: "" },
    config: {
      mutationKey: "updateTranscript",
    },
  });

  const updateTranscriptRequest = useMemo(
    () =>
      debounce(
        (...args) =>
          _updateTranscriptRequest(...args).then((res) => {
            setHearing(res.hearing);
          }),
        1000
      ),
    [_updateTranscriptRequest]
  );

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
        setTranscriptText(hearingData?.transcript[0] || "");
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
      const selectedWitnessDefault = responseList?.additionalDetails?.witnessDetails?.formdata?.[0]?.data || {};
      setSelectedWitness(selectedWitnessDefault);
      setWitnessDepositionText(
        hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitnessDefault.uuid)?.deposition
      );
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

  const updateAPICall = (newText) => {
    const newHearingUpdated = structuredClone(hearing);
    newHearingUpdated.transcript[0] = newText;
    if (userHasRole("EMPLOYEE") && !disableTextArea) {
      updateTranscriptRequest({ body: { hearing: newHearingUpdated } });
    }
  };

  useEffect(() => {
    if (hearingsData) {
      const hearingData = hearingsData?.HearingList?.[0];
      if (hearingData && transcriptText !== hearingData?.transcript[0] && !disableTextArea) {
        updateAPICall(transcriptText);
      }
    }
  }, [transcriptText, setTranscriptText]);

  const isDepositionSaved = useMemo(() => {
    return hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitness.uuid)?.deposition.length;
  }, [selectedWitness, hearing]);

  const saveWitnessDeposition = () => {
    const updatedHearing = structuredClone(hearing);
    setWitnessModalOpen(true);
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
    const selectedWitnessDeposition = additionalDetails?.witnessDetails?.formdata?.find((w) => w.data.uuid === selectedUUID)?.data || {};
    setSelectedWitness(selectedWitnessDeposition);
    setWitnessDepositionText(
      hearing?.additionalDetails?.witnessDepositions?.find((witness) => witness.uuid === selectedWitnessDeposition.uuid)?.deposition || ""
    );
  };

  const handleEndHearingModal = () => {
    setEndHearingModalOpen(!endHearingModalOpen);
  };

  const handleExitHearing = () => {
    history.push(`/${window.contextPath}/${userType}/home/home-pending-task`);
  };

  const handleClose = () => {
    setWitnessModalOpen(false);
  };

  const handleProceed = async () => {
    try {
      const documents = Array.isArray(hearing?.documents) ? hearing.documents : [];
      const documentsFile =
        signedDocumentUploadID !== ""
          ? {
              documentType: "SIGNED",
              fileStore: signedDocumentUploadID,
            }
          : null;

      const reqBody = {
        hearing: {
          ...hearing,
          documents: documentsFile ? [...documents, documentsFile] : documents,
        },
      };

      const updateWitness = await hearingService.customApiService(
        Urls.hearing.uploadWitnesspdf,
        { tenantId: tenantId, hearing: reqBody?.hearing, hearingType: "", status: "" },
        { applicationNumber: "", cnrNumber: "" }
      );
      setWitnessModalOpen(false);
    } catch (error) {
      console.error("Error updating witness:", error);
    }
  };

  const attendanceCount = useMemo(() => hearing?.attendees?.filter((attendee) => attendee.wasPresent).length || 0, [hearing]);
  const [isRecording, setIsRecording] = useState(false);
  const IsSelectedWitness = useMemo(() => {
    return !isEmpty(selectedWitness);
  }, [selectedWitness]);

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
            hearingLink={hearingVcLink}
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
                selected={
                  IsSelectedWitness
                    ? {
                        label: `${selectedWitness.firstName} ${selectedWitness.lastName}`,
                        value: selectedWitness.uuid,
                      }
                    : {}
                }
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
              <React.Fragment>
                {activeTab === "Witness Deposition" && (
                  <React.Fragment>
                    <TextArea
                      ref={textAreaRef}
                      style={{ width: "100%", minHeight: "40vh" }}
                      value={IsSelectedWitness ? witnessDepositionText || "" : ""}
                      onChange={handleChange}
                      disabled={isDepositionSaved || disableTextArea || !IsSelectedWitness}
                    />
                    {!disableTextArea && IsSelectedWitness && !isDepositionSaved && (
                      <TranscriptComponent
                        setWitnessDepositionText={setWitnessDepositionText}
                        isRecording={isRecording}
                        setIsRecording={setIsRecording}
                        activeTab={activeTab}
                      ></TranscriptComponent>
                    )}
                  </React.Fragment>
                )}
                {activeTab !== "Witness Deposition" && (
                  <React.Fragment>
                    <TextArea
                      ref={textAreaRef}
                      style={{ width: "100%", minHeight: "40vh" }}
                      value={transcriptText || ""}
                      onChange={handleChange}
                      disabled={disableTextArea}
                    />
                    {!disableTextArea && (
                      <TranscriptComponent
                        setTranscriptText={setTranscriptText}
                        isRecording={isRecording}
                        setIsRecording={setIsRecording}
                        activeTab={activeTab}
                      ></TranscriptComponent>
                    )}
                  </React.Fragment>
                )}
              </React.Fragment>
            ) : (
              <React.Fragment>
                {activeTab === "Witness Deposition" && (
                  <TextArea
                    style={{ width: "100%", minHeight: "40vh", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                    value={IsSelectedWitness ? witnessDepositionText || "" : ""}
                    disabled
                  />
                )}
                {activeTab !== "Witness Deposition" && (
                  <TextArea
                    style={{ width: "100%", minHeight: "40vh", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                    value={transcriptText || ""}
                    disabled
                  />
                )}
              </React.Fragment>
            )}
          </div>
        </div>
        <div style={{ marginTop: "10px", marginBottom: "50px" }}>
          {activeTab === "Witness Deposition" && userHasRole("EMPLOYEE") && (
            <div>
              <Button
                label={t("SAVE_WITNESS_DEPOSITION")}
                isDisabled={isDepositionSaved}
                onButtonClick={() => {
                  saveWitnessDeposition();
                }}
              ></Button>
            </div>
          )}
        </div>
      </div>
      <div className="right-side" style={{ borderLeft: "1px solid lightgray" }}>
        <HearingSideCard hearingId={hearingId} caseId={caseData?.criteria?.[0]?.responseList?.[0]?.id} filingNumber={filingNumber}></HearingSideCard>
        {adjournHearing && (
          <AdjournHearing
            hearing={hearing}
            updateTranscript={_updateTranscriptRequest}
            transcriptText={transcriptText}
            setAdjournHearing={setAdjournHearing}
            disableTextArea={disableTextArea}
            setTranscriptText={setTranscriptText}
          />
        )}
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
            <Button
              label={"ATTENDANCE_CHIP"}
              style={{ boxShadow: "none", backgroundColor: "#ECF3FD", borderRadius: "4px", border: "none", padding: "10px" }}
              textStyles={{
                fontFamily: "Roboto",
                fontSize: "16px",
                fontWeight: 400,
                lineHeight: "18.75px",
                textAlign: "center",
                color: "#0F3B8C",
              }}
            >
              <h2
                style={{
                  paddingLeft: "4px",
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#0F3B8C",
                  fontWeight: "700",
                }}
              >
                {`${attendanceCount}`}
              </h2>
            </Button>
            {userHasRole("EMPLOYEE") && (
              <Button
                label={t("MARK_ATTENDANCE")}
                variation={"teritiary"}
                onButtonClick={handleModal}
                style={{ boxShadow: "none", backgroundColor: "none", borderRadius: "4px", border: "none", padding: "10px" }}
                textStyles={{
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  fontWeight: 700,
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#007E7E",
                }}
              />
            )}
          </div>
          {userHasRole("EMPLOYEE") ? (
            <div
              style={{
                display: "flex",
                gap: "16px",
                width: "100%",
                justifyContent: "flex-end",
              }}
            >
              <Button
                label={t("ADJOURN_HEARING")}
                variation={"secondary"}
                onButtonClick={() => setAdjournHearing(true)}
                style={{ boxShadow: "none", backgroundColor: "#fff", padding: "10px", width: "166px" }}
                textStyles={{
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  fontWeight: 700,
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#007E7E",
                }}
              />

              <Button
                label={t("END_HEARING")}
                variation={"primary"}
                onButtonClick={handleEndHearingModal}
                style={{ boxShadow: "none", backgroundColor: "#BB2C2F", border: "none", padding: "10px", width: "166px" }}
                textStyles={{
                  fontFamily: "Roboto",
                  fontSize: "16px",
                  fontWeight: 700,
                  lineHeight: "18.75px",
                  textAlign: "center",
                  color: "#ffffff",
                }}
              />
            </div>
          ) : (
            <Button label={t("EXIT_HEARING")} variation={"primary"} onButtonClick={handleExitHearing} />
          )}
        </div>
      </ActionBar>
      {isOpen && (
        <MarkAttendance
          handleModal={handleModal}
          attendees={hearing.attendees || []}
          refetchHearing={refetchHearing}
          hearingData={hearing}
          setAddPartyModal={setAddPartyModal}
        />
      )}
      <div>
        {addPartyModal && (
          <AddParty
            onCancel={onCancel}
            onAddSuccess={() => {
              refetchCase();
            }}
            caseData={caseData}
            tenantId={tenantId}
            hearing={hearing}
            refetchHearing={refetchHearing}
          ></AddParty>
        )}
      </div>
      {witnessModalOpen && (
        <WitnessModal
          handleClose={handleClose}
          hearingId={hearingId}
          setSignedDocumentUploadID={setSignedDocumentUploadID}
          handleProceed={handleProceed}
        />
      )}
      {endHearingModalOpen && (
        <EndHearing
          handleEndHearingModal={handleEndHearingModal}
          hearingId={hearingId}
          hearing={hearing}
          transcriptText={transcriptText}
          disableTextArea={disableTextArea}
          setTranscriptText={setTranscriptText}
        />
      )}
    </div>
  );
};

export default InsideHearingMainPage;
