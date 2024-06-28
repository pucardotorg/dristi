import {
  Button,
  CardLabel,
  CardLabelError,
  CheckSvg,
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
import { RightArrow } from "../../../../dristi/src/icons/svgIndex";
import { CASEService } from "../../hooks/services";

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

const JoinHomeLocalisation = {
  ENTER_CASE_NUMBER: "ENTER_CASE_NUMBER",
  INVALID_CASE_FILING_NUMBER: "INVALID_CASE_FILING_NUMBER",
  INVALID_CASE_INFO_TEXT: "INVALID_CASE_INFO_TEXT",
  CONFIRM_JOIN_CASE: "CONFIRM_JOIN_CASE",
  PLEASE_NOTE: "PLEASE_NOTE",
  SIX_DIGIT_CODE_INFO: "SIX_DIGIT_CODE_INFO",
  ADVOCATE_OPT: "ADVOCATE_OPT",
  LITIGANT_OPT: "LITIGANT_OPT",
  PLEASE_CHOOSE_PARTY: "PLEASE_CHOOSE_PARTY",
  COMPLAINANT_BRACK: "COMPLAINANT_BRACK",
  RESPONDENT_BRACK: "RESPONDENT_BRACK",
  WARNING: "WARNING",
  FOR_THE_SELECTED: "FOR_THE_SELECTED",
  ALREADY_AN_ADVOCATE: "ALREADY_AN_ADVOCATE",
  PLEASE_CHOOSE_PROCEED: "PLEASE_CHOOSE_PROCEED",
  PRIMARY_ADVOCATE: "PRIMARY_ADVOCATE",
  SUPPORTING_ADVOCATE: "SUPPORTING_ADVOCATE",
  REPRESENT_SELF: "REPRESENT_SELF",
  YES: "YES",
  NO_HAVE_ADVOCATE: "NO_HAVE_ADVOCATE",
  ADD_ADVOCATE_LATER: "ADD_ADVOCATE_LATER",
  ADD_ADVOCATE_ANYTIME: "ADD_ADVOCATE_LATER",
  SUBMISSION_NECESSARY: "SUBMISSION_NECESSARY",
  FILL_FORM_VAKALATNAMA: "FILL_FORM_VAKALATNAMA",
  PARTY_PARTIES: "PARTY_PARTIES",
  AFFIDAVIT: "AFFIDAVIT",
  TYPE_AFFIDAVIT_CONTENT: "TYPE_AFFIDAVIT_CONTENT",
  ENTER_CODE_JOIN_CASE: "ENTER_CODE_JOIN_CASE",
  JOIN_CASE_SUCCESS: "JOIN_CASE_SUCCESS",
  BACK_HOME: "BACK_HOME",
  CONFIRM_ATTENDANCE: "CONFIRM_ATTENDANCE",
  JOINING_THIS_CASE_AS: "JOINING_THIS_CASE_AS",
  SKIP_LATER: "SKIP_LATER",
};

const JoinCaseHome = ({ t }) => {
  const history = useHistory();
  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("MODAL");
  const CustomCaseInfoDiv = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMCASEINFODIV");
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DOCVIEWERWRAPPER");
  const SelectCustomDragDrop = window?.Digit?.ComponentRegistryService?.getComponent("SelectCustomDragDrop");
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
  const [advocateDetail, setAdvocateDetail] = useState({});
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
  const [formData, setFormData] = useState({});
  const [affidavitText, setAffidavitText] = useState("");
  const [success, setSuccess] = useState(false);

  const [advocateId, setAdvocateId] = useState("");
  const [adovacteVakalatnama, setAdovacteVakalatnama] = useState({});
  const [individualId, setIndividualId] = useState("");

  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);

  const documentUploaderConfig = {
    key: "vakalatnama",
    populators: {
      inputs: [
        {
          name: "vakalatnama",
          documentHeader: "Vakalatnama",
          documentSubText: "",
          isOptional: "",
          infoTooltipMessage: "",
          type: "DragDropComponent",
          uploadGuidelines: t("UPLOAD_DOC_50"),
          maxFileSize: 50,
          maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
          fileTypes: ["JPG", "PNG", "PDF"],
          isMultipleUpload: false,
        },
      ],
    },
  };

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
    } else if (step === 2) {
      if (userType === "Litigant" && barRegNumber !== "") {
        setIsDisabled(false);
      }
    } else if (step === 3 || step === 4) {
      setIsDisabled(false);
    } else if (step === 5) {
      setIsDisabled(true);
      setSuccess(true);
    }

    if (step !== 5) {
      setSuccess(false);
    }

    if (barRegNumber === "" && step === 2) {
      setAdvocateDetail({});
      setFormData({});
      setBarDetails([]);
      setIsDisabled(true);
    }
    if (affidavitText) {
      setIsDisabled(false);
    }
  }, [step, userType, selectedParty, representingYourself, roleOfNewAdvocate, caseNumber, barRegNumber, affidavitText]);

  const modalItem = [
    // 0
    {
      modalMain: (
        <div className="case-number-input">
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.ENTER_CASE_NUMBER)}`}</CardLabel>
            <div style={{ width: "100%" }}>
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
              label={t(JoinHomeLocalisation.INVALID_CASE_FILING_NUMBER)}
              additionalElements={{}}
              inline
              text={t(JoinHomeLocalisation.INVALID_CASE_INFO_TEXT)}
              textStyle={{}}
              className={`custom-info-card error`}
            />
          )}
          {caseDetails?.caseNumber && (
            <React.Fragment>
              <h3 className="sure-text">{t(JoinHomeLocalisation.CONFIRM_JOIN_CASE)}</h3>
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
                label={t(JoinHomeLocalisation.PLEASE_NOTE)}
                additionalElements={{}}
                inline
                text={t(JoinHomeLocalisation.SIX_DIGIT_CODE_INFO)}
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
            <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.JOINING_THIS_CASE_AS)}`}</CardLabel>
            <RadioButtons
              selectedOption={userType}
              onSelect={(value) => {
                setUserType(value);
                setSelectedParty("");
                setRepresentingYourself("");
                setRoleOfNewAdvocate("");
              }}
              options={[t(JoinHomeLocalisation.ADVOCATE_OPT), t(JoinHomeLocalisation.LITIGANT_OPT)]}
            />
          </LabelFieldPair>
          {userType !== "" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.PLEASE_CHOOSE_PARTY)}`}</CardLabel>
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
                      .join(", ")}  ${t(JoinHomeLocalisation.COMPLAINANT_BRACK)}`,
                    `${caseDetails?.additionalDetails?.respondentDetails?.formdata
                      ?.map((data) => `${data?.data?.respondentFirstName} ${data?.data?.respondentLastName}`)
                      .join(", ")} ${t(JoinHomeLocalisation.RESPONDENT_BRACK)}`,
                  ]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {selectedParty !== "" && barRegNumber !== "" && userType === "Advocate" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <InfoCard
                variant={"warning"}
                label={t(JoinHomeLocalisation.WARNING)}
                additionalElements={{}}
                inline
                text={t(`${t(JoinHomeLocalisation.FOR_THE_SELECTED)} ${selectedParty}, ${t(JoinHomeLocalisation.ALREADY_AN_ADVOCATE)}`)}
                textStyle={{}}
                className={`custom-info-card warning`}
              />

              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.PLEASE_CHOOSE_PROCEED)}`}</CardLabel>
                <RadioButtons
                  selectedOption={roleOfNewAdvocate}
                  onSelect={(value) => {
                    setRoleOfNewAdvocate(value);
                    setRepresentingYourself("");
                  }}
                  options={[t(JoinHomeLocalisation.PRIMARY_ADVOCATE), t(JoinHomeLocalisation.SUPPORTING_ADVOCATE)]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {selectedParty !== "" && userType === "Litigant" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.REPRESENT_SELF)}`}</CardLabel>
                <RadioButtons
                  selectedOption={representingYourself}
                  onSelect={(value) => {
                    setRepresentingYourself(value);
                  }}
                  options={[t(JoinHomeLocalisation.YES), t(JoinHomeLocalisation.NO_HAVE_ADVOCATE)]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {representingYourself === "Yes" && (
            <InfoCard
              variant={"default"}
              label={t(JoinHomeLocalisation.PLEASE_NOTE)}
              additionalElements={{}}
              inline
              text={t(JoinHomeLocalisation.ADD_ADVOCATE_LATER)}
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
                label={userType === "Litigant" ? t(JoinHomeLocalisation.PLEASE_NOTE) : t("INFO")}
                additionalElements={{}}
                inline
                text={
                  userType === "Litigant" && representingYourself !== "Yes"
                    ? t(JoinHomeLocalisation.ADD_ADVOCATE_ANYTIME)
                    : userType === "Litigant" && representingYourself === "Yes"
                      ? t(JoinHomeLocalisation.SUBMISSION_NECESSARY)
                      : t(JoinHomeLocalisation.FILL_FORM_VAKALATNAMA)
                }
                textStyle={{}}
                className={`custom-info-card`}
              />
              {representingYourself !== "Yes" ? (
                <React.Fragment>
                  <LabelFieldPair className="case-label-field-pair">
                    <CardLabel className="case-input-label">{`${"BAR registration"}`}</CardLabel>
                    <div style={{ width: "100%", maxWidth: "960px" }}>
                      <TextInput
                        // t={t}
                        style={{ width: "100%" }}
                        type={"text"}
                        name="barRegNumber"
                        value={barRegNumber}
                        onChange={(e) => {
                          setBarRegNumber(e.target.value);
                        }}
                        disable={userType === "Litigant" ? false : true}
                      />
                      {errors?.barRegNumber && <CardLabelError> {errors?.barRegNumber?.message} </CardLabelError>}
                      { }
                    </div>
                  </LabelFieldPair>
                  <CustomCaseInfoDiv t={t} data={barDetails} />
                  {userType === "Advocate" && (
                    <LabelFieldPair className="case-label-field-pair">
                      <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.PARTY_PARTIES)}`}</CardLabel>
                      <Dropdown
                        t={t}
                        option={parties}
                        selected={parties.find((value) => value.title === party)}
                        optionKey={"title"}
                        select={(e) => setParty(e.title)}
                        freeze={true}
                        disable={true}
                      />
                    </LabelFieldPair>
                  )}
                  {userType === "Litigant" && advocateDetail?.barRegistrationNumber && (
                    <SelectCustomDragDrop
                      t={t}
                      formData={formData}
                      config={documentUploaderConfig}
                      onSelect={(e, p) => {
                        console.log("e", p);
                        setFormData({
                          [documentUploaderConfig.key]: p,
                        });
                      }}
                    />
                  )}
                </React.Fragment>
              ) : (
                <React.Fragment>
                  <LabelFieldPair className="case-label-field-pair">
                    <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.AFFIDAVIT)}`}</CardLabel>
                    <div style={{ width: "100%", maxWidth: "960px" }}>
                      <textarea
                        value={affidavitText}
                        onChange={(e) => {
                          setAffidavitText(e.target.value);
                        }}
                        rows={5}
                        className="custom-textarea-style"
                        placeholder={t(JoinHomeLocalisation.TYPE_AFFIDAVIT_CONTENT)}
                      ></textarea>

                      {errors?.affidavitText && <CardLabelError> {errors?.affidavitText?.message} </CardLabelError>}
                      { }
                    </div>
                  </LabelFieldPair>
                </React.Fragment>
              )}
            </React.Fragment>
          )}
        </div>
      ),
    },
    // 3
    {
      modalMain: (
        <div className="view-document-vak">
          {adovacteVakalatnama && adovacteVakalatnama?.fileStore && (
            <DocViewerWrapper
              key={adovacteVakalatnama?.fileStore}
              fileStoreId={adovacteVakalatnama?.fileStore}
              tenantId={tenantId}
              docWidth="100%"
              docHeight="calc(100% - 84px)"
              showDownloadOption={false}
            />
          )}
        </div>
      ),
    },
    // 4
    {
      modalMain: (
        <div className="enter-validation-code">
          <InfoCard
            variant={"default"}
            label={t(JoinHomeLocalisation.PLEASE_NOTE)}
            additionalElements={{}}
            inline
            text={t(JoinHomeLocalisation.SIX_DIGIT_CODE_INFO)}
            textStyle={{}}
            className={`custom-info-card`}
          />
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.ENTER_CODE_JOIN_CASE)}`}</CardLabel>
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
              { }
            </div>
          </LabelFieldPair>
        </div>
      ),
    },
    // 5
    {
      modalMain: (
        <div className="join-a-case-success">
          <div className={`joining-message ${success ? "join-success" : "join-failed"}`}>
            <h3 className="message-header">{t(JoinHomeLocalisation.JOIN_CASE_SUCCESS)}</h3>
            <div style={{ width: "48px", height: "48px" }}>
              <CheckSvg />
            </div>
          </div>
          {success && (
            <React.Fragment>
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
              <div className="action-button-success">
                <Button className={"selector-button-border"} label={t(JoinHomeLocalisation.BACK_HOME)} onButtonClick={() => setStep(step - 1)} />
                <Button className={"selector-button-primary"} label={t(JoinHomeLocalisation.CONFIRM_ATTENDANCE)}>
                  <RightArrow />
                </Button>
              </div>
            </React.Fragment>
          )}
        </div>
      ),
    },
  ];

  useEffect(() => {
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
  }, [caseDetails]);

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ""; // Adjust as per your context path logic
    history.push(`/${contextPath}${path}`);
  };

  const closeModal = () => {
    setCaseNumber("");
    setCaseDetails({});
    setUserType("");
    setBarRegNumber("");
    setAdvocateName("");
    setBarDetails([]);
    setSelectedParty("");
    setSelectedParty("");
    setRepresentingYourself("");
    setRoleOfNewAdvocate("");
    setValidationCode("");
    setErrors({});
    setCaseInfo([]);
    setStep(0);
    setShow(false);
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
      const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
        {
          Individual: {
            userUuid: [userInfo?.uuid],
          },
        },
        { tenantId, limit: 1000, offset: 0 },
        "",
        userInfo?.uuid && isUserLoggedIn
      );
      // console.log('individualId:', individualData?.Individual?.[0]?.individualId)
      setIndividualId(individualData?.Individual?.[0]?.individualId);
    } else if (step === 1) {
      console.log("first");
      if (userType && userType === "Litigant" && selectedParty && representingYourself) {
        setBarRegNumber("");
        setIsDisabled(true);
        setStep(step + 1);
        setBarDetails([]);
        setErrors({
          ...errors,
          barRegNumber: undefined,
        });
      } else if (userType && userType === "Advocate" && selectedParty) {
        if (roleOfNewAdvocate !== "I’m a supporting advocate") {
          const advocateResponse = await DRISTIService.searchIndividualAdvocate(
            {
              criteria: [
                {
                  individualId: individualId,
                },
              ],
              tenantId,
            },
            {}
          );

          if (advocateResponse?.advocates[0]?.responseList?.length > 0) {
            setBarRegNumber(advocateResponse?.advocates[0]?.responseList[0]?.barRegistrationNumber);
            setAdvocateId(advocateResponse?.advocates[0]?.responseList[0]?.id);
            setIsDisabled(false);
            setAdovacteVakalatnama(advocateResponse?.advocates[0]?.responseList[0]?.documents[0]);
            setAdovacteVakalatnama(advocateResponse?.advocates[0]?.responseList[0]?.documents[0]);
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
                value: advocateResponse?.advocates[0]?.responseList[0]?.additionalDetails?.username,
              },
            ]);
            setErrors({
              ...errors,
              barRegNumber: undefined,
            });
          } else {
            setErrors({
              ...errors,
              barRegNumber: "You don't have permission to join case as advocate. Please contact Nyaya Mitra for support",
            });
          }
          setStep(step + 1);
        } else {
          const advocateResponse = await DRISTIService.searchIndividualAdvocate(
            {
              criteria: [
                {
                  barRegistrationNumber: caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.barRegistrationNumber,
                },
              ],
              tenantId,
            },
            {}
          );

          const individualData = await window?.Digit.DRISTIService.searchIndividualUser(
            {
              Individual: {
                individualId: advocateResponse?.advocates[0]?.responseList[0]?.individualId,
              },
            },
            { tenantId, limit: 1000, offset: 0 },
            "",
            userInfo?.uuid && isUserLoggedIn
          );
          console.log("individualData", individualData);
          setPrimaryAdvocateDetail([
            {
              key: "Name",
              value: caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.advocateName,
            },
            {
              key: "Bar Council Id",
              value: caseDetails?.additionalDetails?.advocateDetails?.formdata[0]?.data?.barRegistrationNumber,
            },
            {
              key: "Email",
              value: individualData.Individual[0]?.email || "Email Not Available",
            },
          ]);
          setStep(step + 1);
        }
      }
    } else if (step === 2) {
      if (roleOfNewAdvocate === "I’m a supporting advocate") {
        closeModal();
        return;
      }
      if (userType === "Litigant") {
        if (representingYourself !== "Yes") {
          if (!advocateDetail?.barRegistrationNumber) {
            const advocateResponse = await DRISTIService.searchIndividualAdvocate(
              {
                criteria: [
                  {
                    barRegistrationNumber: barRegNumber,
                  },
                ],
                tenantId,
              },
              {}
            );
            if (advocateResponse?.advocates[0]?.responseList.length > 0) {
              const temp = advocateResponse?.advocates[0].responseList[0];
              setAdvocateDetail({
                barRegistrationNumber: temp?.barRegistrationNumber,
              });
              setBarDetails([
                {
                  key: "Name",
                  value: temp?.additionalDetails?.username,
                },
              ]);
              if (temp?.documents?.length > 0) {
                setFormData({
                  [documentUploaderConfig.key]: {
                    [documentUploaderConfig.key]: [
                      {
                        documentName: temp.documents[0].additionalDetails.fileName,
                        documentType: temp.documents[0].documentType,
                        fileName: temp.documents[0].additionalDetails.fileName,
                        fileStore: temp.documents[0].fileStore,
                      },
                    ],
                  },
                });
              }
              setErrors({
                ...errors,
                barRegNumber: undefined,
              });
            } else {
              setErrors({
                ...errors,
                barRegNumber: {
                  message: "Entered Bar Registration not found, Please enter correct one.",
                },
              });
            }
          } else {
            setIsDisabled(true);
            setStep(step + 2);
          }
        } else {
          setStep(step + 2);
        }
      } else {
        setIsDisabled(false);
        setStep(step + 1);
      }
    } else if (step === 3) {
      setIsDisabled(true);
      setStep(step + 1);
    } else if (step === 4 && validationCode.length === 6) {
      if (userType === "Advocate") {
        const response = await CASEService.joinCaseService(
          {
            caseFilingNumber: caseNumber,
            tenantId: tenantId,
            accessCode: validationCode,
            representative: {
              tenantId: tenantId,
              advocateId: advocateId,
              representing: [
                {
                  tenantId: tenantId,
                  individualId: selectedParty.includes("Complainant")
                    ? caseDetails?.additionalDetails?.complaintDetails?.formdata[0]?.data?.individualDetails?.individualId
                    : caseDetails?.additionalDetails?.respondentDetails?.formdata[0]?.data?.individualDetails?.individualId,
                },
              ],
            },
          },
          {}
        );
        console.log("Join case (advocate):", response);
      } else {
        console.log("hojojoij");
        const response = await CASEService.joinCaseService(
          {
            caseFilingNumber: caseNumber,
            tenantId: tenantId,
            accessCode: validationCode,
            litigant: {
              tenantId: tenantId,
              individualId: representingYourself === "Yes" ? individualId : "",
              affidavitText,
              partyCategory: "INDIVIDUAL",
            },
          },
          {}
        );
        console.log("Join case (litigant):", response);
      }
    } else if (step === 5) {
      setStep(6);
    }
    console.log("step", step);
  };

  return (
    <div>
      <Button
        variation={"secondary"}
        className={"secondary-button-selector"}
        label={t("JOIN_A_CASE")}
        labelClassName={"secondary-label-selector"}
        onButtonClick={() => setShow(true)}
      />
      {show && (
        <Modal
          headerBarEnd={<CloseBtn onClick={closeModal} />}
          actionCancelLabel={((step === 0 && caseDetails?.caseNumber) || step !== 0) && t("CS_COMMON_BACK")}
          actionCancelOnSubmit={() => {
            if (step === 0 && caseDetails?.caseNumber) {
              setCaseDetails({});
            } else if (step === 4 && userType === "Litigant") setStep(step - 2);
            else setStep(step - 1);
          }}
          actionSaveLabel={roleOfNewAdvocate === "I’m a supporting advocate" && step === 2 ? t("GOT_IT_TEXT") : t("PROCEED_TEXT")}
          actionSaveOnSubmit={onProceed}
          formId="modal-action"
          headerBarMain={<Heading label={t("JOIN_A_CASE")} />}
          className={`join-a-case-modal ${success && "case-join-success"}`}
          isDisabled={isDisabled}
        >
          {step >= 0 && modalItem[step]?.modalMain}
          {step === 2 && userType === "Litigant" && representingYourself !== "Yes" && (
            <Button
              className={"skip-button"}
              label={t(JoinHomeLocalisation.SKIP_LATER)}
              onButtonClick={() => {
                setStep(4);
                setBarRegNumber("");
              }}
            />
          )}
        </Modal>
      )}
    </div>
  );
};

export default JoinCaseHome;
