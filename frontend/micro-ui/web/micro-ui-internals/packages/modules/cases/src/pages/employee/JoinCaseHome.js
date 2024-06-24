import {
  Button,
  CardLabel,
  CardLabelError,
  CloseSvg,
  Dropdown,
  LabelFieldPair,
  RadioButtons,
  TextInput,
} from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { InfoCard } from "@egovernments/digit-ui-components";
import { DRISTIService } from "../../../../dristi/src/services";

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
    </div>
  );
};

const JoinCaseHome = ({ t }) => {
  const history = useHistory();
  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("MODAL");
  const CustomCaseInfoDiv = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMCASEINFODIV");
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DOCVIEWERWRAPPER");
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const [show, setShow] = useState(false);
  const [step, setStep] = useState(0);
  const [caseNumber, setCaseNumber] = useState("");
  const [caseDetails, setCaseDetails] = useState({});
  const [userType, setUserType] = useState("");
  const [barRegNumber, setBarRegNumber] = useState("");
  const [advocateName, setAdvocateName] = useState("");
  const [barDetails, setBarDetails] = useState([]);
  const [selectedParty, setSelectedParty] = useState("");
  const [representingYourself, setRepresentingYourself] = useState("");
  const [roleOfNewAdvocate, setRoleOfNewAdvocate] = useState("");
  const [parties, setParties] = useState([
    { title: "Subarna Sadhu", value: "Subarna Sadhu" },
    { title: "Nitish Kumar", value: "Nitish Kumar" },
  ]);
  const [primaryAdvocateDetail, setPrimaryAdvocateDetail] = useState([
    {
      key: "Name",
      value: "Keshav Jagan",
    },
    {
      key: "Bar Council Id",
      value: "BAR126738930089",
    },
    {
      key: "Email",
      value: "keshav@gmail.com",
    },
  ]);
  const [party, setParty] = useState("Subarna Sadhu");
  const [validationCode, setValidationCode] = useState("");
  const [isDisabled, setIsDisabled] = useState(false);
  const [errors, setErrors] = useState({});
  const [caseInfo, setCaseInfo] = useState([]);

  useEffect(() => {
    // console.log('step', step)
    if (step === 0) {
      if (caseNumber !== "") setIsDisabled(false);
      else setIsDisabled(true);
    } else if (step === 1) {
      if (
        (userType && userType === "Litigant" && selectedParty && representingYourself) ||
        (userType && userType === "Advocate" && selectedParty && barRegNumber === "") ||
        (userType && userType === "Advocate" && selectedParty && barRegNumber && roleOfNewAdvocate)
      ) {
        setIsDisabled(false);
      } else {
        setIsDisabled(true);
      }
    } else if (step === 2 && selectedParty) {
      setIsDisabled(false);
    } else if (step === 3 || step === 4) {
      setIsDisabled(false);
    } else if (step === 5) {
      setIsDisabled(true);
    } else if (step === 5) {
    }
  }, [step, userType, selectedParty, representingYourself, roleOfNewAdvocate, caseNumber, barRegNumber]);

  const modalItem = [
    // 0
    {
      modalMain: (
        <div className="case-number-input">
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${"You are joining this case as?"}`}</CardLabel>
            <div style={{ width: "100%", maxWidth: "960px" }}>
              <TextInput
                // t={t}
                style={{ width: "100%" }}
                type={"text"}
                name="caseNumber"
                value={caseNumber}
                onChange={(e) => {
                  setCaseDetails({});
                  setCaseNumber(e.target.value);
                }}
                pattern={"^[a-zA-Z]+(?:[a-zA-Z-.`' ]*[a-zA-Z])?$"}

                // disable={editScreen}
              />
            </div>
          </LabelFieldPair>
          {errors?.caseNumber && (
            <InfoCard
              variant={"default"}
              label={t("Invalid Case / Filing Number")}
              additionalElements={{}}
              inline
              text={t("If you think the entered number is correct, please contact Nyaya Mitra for support")}
              textStyle={{}}
              className={`custom-info-card error`}
            />
          )}
          {caseDetails?.caseNumber && (
            <React.Fragment>
              <h3 className="sure-text">Are you sure you want to join the following case?</h3>
              <CustomCaseInfoDiv
                t={t}
                data={caseInfo}
                column={4}
                children={
                  <div className="complainants-respondents">
                    <div style={{ width: "50%" }}>
                      <h2 className="case-info-title">{t("Complainants")}</h2>
                      <div className="case-info-value">
                        <span>
                          {caseDetails?.additionalDetails?.complaintDetails?.formdata
                            ?.map(
                              (data) => `${data?.data?.firstName}${data?.data?.middleName && " " + data?.data?.middleName} ${data?.data?.lastName}`
                            )
                            .join(", ")}
                        </span>
                      </div>
                    </div>
                    <div style={{ width: "50%" }}>
                      <h2 className="case-info-title">{t("Respondents")}</h2>
                      <div className="case-info-value">
                        <span>
                          {caseDetails?.additionalDetails?.respondentDetails?.formdata
                            ?.map((data) => `${data?.data?.respondentFirstName} ${data?.data?.respondentLastName}`)
                            .join(", ")}
                        </span>
                      </div>
                    </div>
                  </div>
                }
              />
              <InfoCard
                variant={"default"}
                label={t("Please Note")}
                additionalElements={{}}
                inline
                text={t(
                  "A six digit code to join the case is either available as a part of your Summons or with parties who have already joined this case"
                )}
                textStyle={{}}
                className={`custom-info-card`}
              />
            </React.Fragment>
          )}
        </div>
      ),
    },
    // 1
    {
      modalMain: (
        <div className="select-user">
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${t("You are joining this case as?")}`}</CardLabel>
            <RadioButtons
              selectedOption={userType}
              onSelect={(value) => {
                setUserType(value);
                setSelectedParty("");
                setRepresentingYourself("");
                setRoleOfNewAdvocate("");
              }}
              options={["Advocate", "Litigant"]}
            />
          </LabelFieldPair>
          {userType !== "" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t("Please choose the Party you are representing?")}`}</CardLabel>
                <RadioButtons
                  selectedOption={selectedParty}
                  onSelect={(value) => {
                    setSelectedParty(value);
                    setRoleOfNewAdvocate("");
                    setRepresentingYourself("");
                  }}
                  options={[
                    `${caseDetails?.additionalDetails?.complaintDetails?.formdata
                      ?.map((data) => `${data?.data?.firstName}${data?.data?.middleName && " " + data?.data?.middleName} ${data?.data?.lastName}`)
                      .join(", ")} (Complainant)`,
                    `${caseDetails?.additionalDetails?.respondentDetails?.formdata
                      ?.map((data) => `${data?.data?.respondentFirstName} ${data?.data?.respondentLastName}`)
                      .join(", ")} (Respondent)`,
                  ]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {selectedParty !== "" && barRegNumber !== "" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <InfoCard
                variant={"warning"}
                label={t("Warning")}
                additionalElements={{}}
                inline
                text={t(
                  `For the selected ${selectedParty}, There is already an advocate assigned. If you still wish to join the case, please select your role in this case`
                )}
                textStyle={{}}
                className={`custom-info-card warning`}
              />

              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t("Please choose how you wish to proceed")}`}</CardLabel>
                <RadioButtons
                  selectedOption={roleOfNewAdvocate}
                  onSelect={(value) => {
                    setRoleOfNewAdvocate(value);
                  }}
                  options={[`I’m taking over as a Primary Advocate`, `I’m a supporting advocate`]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {selectedParty !== "" && userType === "Litigant" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t("Will you be representing yourself (Party in Person)?")}`}</CardLabel>
                <RadioButtons
                  selectedOption={representingYourself}
                  onSelect={(value) => {
                    setRepresentingYourself(value);
                  }}
                  options={[`Yes`, `No, I have an Advocate`]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {representingYourself === "Yes" && (
            <InfoCard
              variant={"default"}
              label={t("Please Note")}
              additionalElements={{}}
              inline
              text={t(
                "You can always add an Advocate at a later point in time during the course of this Case.  Until then you will be considered a Party in Person"
              )}
              textStyle={{}}
              className={`custom-info-card`}
            />
          )}
        </div>
      ),
    },
    // 2
    {
      modalMain: (
        <div className="general-details-vek">
          {roleOfNewAdvocate === "I’m a supporting advocate" ? (
            <React.Fragment>
              <InfoCard
                variant={"default"}
                label={t("Info")}
                additionalElements={{}}
                inline
                text={t("Only primary advocates can add supporting advocates")}
                textStyle={{}}
                className={`custom-info-card`}
              />
              <div className="primary-advocate-details">
                <h3 className="contact-text">Please contact the primary advocate on this case to get yourself added</h3>
                <CustomCaseInfoDiv t={t} data={primaryAdvocateDetail} />
              </div>
            </React.Fragment>
          ) : (
            <React.Fragment>
              <InfoCard
                variant={"default"}
                label={t("Info")}
                additionalElements={{}}
                inline
                text={t("Fill the form fields below to generate your vakalatnama")}
                textStyle={{}}
                className={`custom-info-card`}
              />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${"BAR registration"}`}</CardLabel>
                <div style={{ width: "100%", maxWidth: "960px" }}>
                  <TextInput
                    // t={t}
                    style={{ width: "100%" }}
                    type={"text"}
                    name="barRegNumber"
                    value={barRegNumber}
                    readOnly={true}
                    // disable={editScreen}
                  />
                  {errors?.caseNumber && <CardLabelError> {errors?.caseNumber?.message} </CardLabelError>}
                  {}
                </div>
              </LabelFieldPair>
              <CustomCaseInfoDiv t={t} data={barDetails} />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${"Party / Parties"}`}</CardLabel>
                <Dropdown
                  t={t}
                  option={parties}
                  selected={parties.find((value) => value.title === party)}
                  optionKey={"title"}
                  select={(e) => setParty(e.title)}
                  freeze={true}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
        </div>
      ),
    },
    // 3
    {
      modalMain: (
        <div className="view-document-vak">
          <DocViewerWrapper
            key={"kljdf"}
            fileStoreId={"a17c4b20-c0bd-4d58-aa3f-69f8261a0a49"}
            tenantId={"pg"}
            docWidth="100%"
            docHeight="calc(100% - 84px)"
            showDownloadOption={false}
          />
        </div>
      ),
    },
    // 4
    {
      modalMain: (
        <div className="enter-validation-code">
          <InfoCard
            variant={"default"}
            label={t("Please Note")}
            additionalElements={{}}
            inline
            text={t(
              "A six digit code to join the case is either available as a part of your Summons or with parties who have already joined this case"
            )}
            textStyle={{}}
            className={`custom-info-card`}
          />
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${"Enter Code to join the case"}`}</CardLabel>
            <div style={{ width: "100%", maxWidth: "960px" }}>
              <TextInput
                // t={t}
                style={{ width: "100%" }}
                type={"text"}
                name="validationCode"
                value={validationCode}
                onChange={(e) => {
                  if (e.target.value?.length <= 6) {
                    setValidationCode(e.target.value);
                  } else {
                    setIsDisabled(false);
                    return;
                  }
                }}
                // disable={editScreen}
              />
              {errors?.caseNumber && <CardLabelError> {errors?.validationCode?.message} </CardLabelError>}
              {}
            </div>
          </LabelFieldPair>
        </div>
      ),
    },
  ];

  useEffect(() => {
    // console.log('caseDetails', caseDetails)
    setCaseInfo([
      {
        key: "CASE_CATEGORY",
        value: caseDetails?.caseCategory,
      },
      {
        key: "CASE_TYPE",
        value: caseDetails.caseType,
      },
      {
        key: "SUBMITTED_ON",
        value: caseDetails?.filingDate,
      },
      {
        key: "CASE_STAGE",
        value: caseDetails?.stage,
      },
    ]);
    setBarRegNumber(caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.barRegistrationNumber);
    setAdvocateName(caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.advocateName);
    setBarDetails([
      {
        key: "CASE_NUMBER",
        value: caseDetails?.caseNumber,
      },
      {
        key: "Court Complex",
        value: caseDetails?.courtName,
      },
      {
        key: "Advocate",
        value: caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.advocateName,
      },
    ]);
  }, [caseDetails]);

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ""; // Adjust as per your context path logic
    history.push(`/${contextPath}${path}`);
  };

  const onProceed = async () => {
    if (step === 0) {
      if (!caseDetails?.caseNumber) {
        const response = await DRISTIService.searchCaseService(
          {
            criteria: [
              {
                filingNumber: caseNumber,
              },
            ],
            tenantId,
          },
          {}
        );
        console.log("response", response?.criteria[0]?.responseList[0]);
        if (response?.criteria[0]?.responseList?.length > 0) {
          setCaseDetails(response?.criteria[0]?.responseList[0]);
          setErrors({
            ...errors,
            caseNumber: undefined,
          });
        } else {
          setErrors({
            ...errors,
            caseNumber: {
              message: "Not Found",
            },
          });
        }
      } else {
        setStep(step + 1);
        setIsDisabled(true);
      }
    } else if (step === 1) {
      if ((userType && userType === "Litigant" && selectedParty && representingYourself) || (userType && userType === "Advocate" && selectedParty)) {
        setIsDisabled(true);
        setStep(step + 1);
      }
    } else if (step === 2) {
      if (roleOfNewAdvocate === "I’m a supporting advocate") {
        setShow(false);
      } else {
        setIsDisabled(false);
        setStep(step + 1);
      }
    } else if (step === 3) {
      setIsDisabled(true);
      setStep(step + 1);
    } else if (step === 4 && validationCode.length === 6) {
      setStep(step + 1);
    } else if (step === 5) {
      setStep(6);
    }
  };

  return (
    <div>
      <p>Join a case here</p>
      <Button label={"Join a Case"} onButtonClick={() => setShow(true)}></Button>
      {show && (
        <Modal
          headerBarEnd={<CloseBtn onClick={() => setShow(false)} />}
          actionCancelLabel={((step === 0 && caseDetails?.caseNumber) || step !== 0) && t("Back")}
          actionCancelOnSubmit={() => {
            if (step == 0 && caseDetails?.caseNumber) {
              setCaseDetails({});
            } else setStep(step - 1);
          }}
          actionSaveLabel={roleOfNewAdvocate === "I’m a supporting advocate" && step === 2 ? "Got it" : "Proceed"}
          actionSaveOnSubmit={onProceed}
          formId="modal-action"
          headerBarMain={<Heading label={"Join a Case"} />}
          className="join-a-case-modal"
          isDisabled={isDisabled}
        >
          {step >= 0 && modalItem[step]?.modalMain}
        </Modal>
      )}
    </div>
  );
};

export default JoinCaseHome;
