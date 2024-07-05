import {
  Button,
  CardLabel,
  CardLabelError,
  CheckSvg,
  CloseSvg,
  Dropdown,
  FormComposerV2,
  LabelFieldPair,
  RadioButtons,
  TextInput,
} from "@egovernments/digit-ui-react-components";
import React, { useEffect, useState } from "react";
import { InfoCard } from "@egovernments/digit-ui-components";
import { DRISTIService } from "../../../../dristi/src/services";
import { RightArrow } from "../../../../dristi/src/icons/svgIndex";
import { CASEService } from "../../hooks/services";
import isEqual from "lodash/isEqual";
import { first } from "lodash";

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
  CASE_NO_ADMITTED_STATUS: "The above case doesn't have admitted status",
  INVALID_ACCESS_CODE_MESSAGE: "Access code is invalid/incorrect",
  AFFIDAVIT_MINIMUM_CHAR_MESSAGE: "Enter atleast 20 characters",
  FILLING_NUMBER_FORMATE_TEXT: `Filing Number Format:`,
  FILLING_NUMBER_FORMATE_TEXT_VALUE: "F-<StatuteSection>-<YYYY>-<7 digit sequence number>",
  INVALID_CASE_INFO_TEXT: "If you think the entered number is correct, please contact",
  NYAYA_MITRA_TEXT: "Nyaya Mitra",
  FOR_SUPPORT_TEXT: "for support",
  COMPLAINANTS_TEXT: "Complainants",
  RESPONDENTS_TEXT: "Respondents",
  WHICH_PARTY_AFFILIATED: "Which party are you affiliated with in this case?",
  ADD_ADVOCATE_LATER: "You can always add an Advocate at a later point in time during the course of this Case. Until then you will be considered a",
  PARTY_IN_PERSON_TEXT: "Party in Person",
  PRIMARY_ADD_SUPPORTING_ADVOCATE: "Only primary advocates can add supporting advocates",
  CONTACT_PRIMARTY_ADVOCATE: "Please contact the primary advocate on this case to get yourself added",
  REPRESENT_SELF_PARTY: "This submission is necessary if you choose to represent yourself",
};

const barRegistrationSerachConfig = [
  {
    body: [
      {
        type: "apidropdown",
        key: "advocateBarRegistrationNumber",
        label: "CS_BAR_REGISTRATION",
        populators: {
          allowMultiSelect: false,
          name: "advocateBarRegNumberWithName",
          validation: {},
          masterName: "commonUiConfig",
          moduleName: "getAdvocateNameUsingBarRegistrationNumber",
          customfn: "getNames",
          optionsKey: "barRegistrationNumber",
          optionsCustomStyle: {
            marginTop: "40px",
            justifyContent: "space-between",
            flexDirection: "row-reverse",
            maxHeight: "200px",
            overflowY: "scroll",
          },
        },
      },
    ],
  },
  {
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "AdvocateNameDetails",
        key: "AdvocateNameDetails",
        withoutLabel: true,
        populators: {
          inputs: [
            {
              label: "FIRST_NAME",
              type: "text",
              name: "firstName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
            {
              label: "MIDDLE_NAME",
              type: "text",
              name: "middleName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {},
            },
            {
              label: "LAST_NAME",
              type: "text",
              name: "lastName",
              isDisabled: true,
              inputFieldClassName: "user-details-form-style",
              validation: {
                isRequired: true,
              },
              isMandatory: true,
            },
          ],
          validation: {},
        },
      },
    ],
  },
  {
    dependentKey: { isAdvocateRepresenting: ["showForm"] },
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "vakalatnamaFileUpload",
        isMandatory: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "UPLOAD_VAKALATNAMA",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
              downloadTemplateText: "VAKALATNAMA_TEMPLATE_TEXT",
              downloadTemplateLink: "https://www.jsscacs.edu.in/sites/default/files/Department%20Files/Number%20System%20.pdf",
            },
          ],
        },
      },
    ],
  },
];

const JoinCaseHome = ({ t }) => {
  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("MODAL");
  const CustomCaseInfoDiv = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMCASEINFODIV");
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DOCVIEWERWRAPPER");
  const SelectCustomDragDrop = window?.Digit?.ComponentRegistryService?.getComponent("SelectCustomDragDrop");
  const CustomErrorTooltip = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMERRORTOOLTIP");
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
  const [parties, setParties] = useState([]);
  const [advocateDetail, setAdvocateDetail] = useState({});
  const [advocateDetailForm, setAdvocateDetailForm] = useState({});
  const [primaryAdvocateDetail, setPrimaryAdvocateDetail] = useState([]);

  const [party, setParty] = useState("");
  const [validationCode, setValidationCode] = useState("");
  const [isDisabled, setIsDisabled] = useState(true);
  const [errors, setErrors] = useState({});
  const [caseInfo, setCaseInfo] = useState([]);
  const [formData, setFormData] = useState({});
  const [affidavitText, setAffidavitText] = useState("");
  const [success, setSuccess] = useState(false);
  const [messageHeader, setMessageHeader] = useState(t(JoinHomeLocalisation.JOIN_CASE_SUCCESS));

  const [advocateId, setAdvocateId] = useState("");
  const [adovacteVakalatnama, setAdovacteVakalatnama] = useState({});
  const [individualId, setIndividualId] = useState("");
  const [name, setName] = useState({});

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

  const onFormValueChange = (formData) => {
    if (!isEqual(formData, advocateDetailForm)) {
      setAdvocateDetailForm(formData);
    }
    setIsDisabled(false);
  };

  useEffect(() => {
    if (step === 1) {
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
      if (userType === "Litigant" && representingYourself !== "Yes" && barRegNumber) {
        if (barRegNumber === "") {
          setAdvocateDetail({});
          setFormData({});
          setBarDetails([]);
          setIsDisabled(true);
        } else {
          setIsDisabled(false);
        }
      }
      if (userType === "Litigant" && representingYourself === "Yes" && affidavitText) {
        if (affidavitText?.length > 20) {
          setIsDisabled(false);
          setErrors({
            ...errors,
            affidavitText: undefined,
          });
        } else {
          setIsDisabled(true);
          setErrors({
            ...errors,
            affidavitText: {
              message: JoinHomeLocalisation.AFFIDAVIT_MINIMUM_CHAR_MESSAGE,
            },
          });
        }
      }
    }

    if (step !== 5) {
      setSuccess(false);
    }
  }, [step, userType, selectedParty, representingYourself, roleOfNewAdvocate, caseNumber, barRegNumber, affidavitText, parties]);

  const serarchCase = async (caseNumber) => {
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
    if (response?.criteria[0]?.responseList?.length === 1) {
      setIsDisabled(false);
      setErrors({
        ...errors,
        caseNumber: undefined,
      });
    } else {
      setIsDisabled(true);
      if (caseNumber)
        setErrors({
          ...errors,
          caseNumber: {
            message: JoinHomeLocalisation.INVALID_CASE_INFO_TEXT,
          },
        });
    }
  };

  useEffect(() => {
    const getData = setTimeout(() => {
      serarchCase(caseNumber);
    }, 500);
    return () => clearTimeout(getData);
  }, [caseNumber]);

  const fetchBasicUserInfo = async () => {
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
    console.log("individualData", individualData);
    setIndividualId(individualData?.Individual?.[0]?.individualId);
    setName(individualData?.Individual?.[0]?.name);

    const advocateResponse = await DRISTIService.searchIndividualAdvocate(
      {
        criteria: [
          {
            individualId: individualData?.Individual?.[0]?.individualId,
          },
        ],
        tenantId,
      },
      {}
    );

    if (advocateResponse?.advocates[0]?.responseList?.length > 0) {
      setBarRegNumber(advocateResponse?.advocates[0]?.responseList[0]?.barRegistrationNumber);
      setAdvocateId(advocateResponse?.advocates[0]?.responseList[0]?.id);
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
      setUserType(t(JoinHomeLocalisation.ADVOCATE_OPT));
    } else {
      setUserType(t(JoinHomeLocalisation.LITIGANT_OPT));
    }
  };

  useEffect(() => {
    fetchBasicUserInfo();
  }, [show]);

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
                  let str = e.target.value;
                  if (str) {
                    str = str.replace(/[^a-zA-Z0-9.-]/g, "");
                    if (str.length > 50) {
                      str = str.substring(0, 50);
                    }
                    setIsDisabled(true);
                    setCaseNumber(str);
                  } else {
                    setCaseNumber("");
                  }
                }}
                // disable={editScreen}
              />
            </div>
            <p style={{ fontSize: "12px" }}>
              {t(JoinHomeLocalisation.FILLING_NUMBER_FORMATE_TEXT)} {t(JoinHomeLocalisation.FILLING_NUMBER_FORMATE_TEXT_VALUE)}
            </p>
          </LabelFieldPair>
          {errors?.caseNumber && (
            <InfoCard
              variant={"default"}
              label={t(JoinHomeLocalisation.INVALID_CASE_FILING_NUMBER)}
              additionalElements={[
                <p>
                  {t(JoinHomeLocalisation.INVALID_CASE_INFO_TEXT)}{" "}
                  <span style={{ fontWeight: "bold" }}>{t(JoinHomeLocalisation.NYAYA_MITRA_TEXT)}</span> {t(JoinHomeLocalisation.FOR_SUPPORT_TEXT)}
                </p>,
              ]}
              inline
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
                      <h2 className="case-info-title">{t(JoinHomeLocalisation.COMPLAINANTS_TEXT)}</h2>
                      <div className="case-info-value">
                        <span>
                          {caseDetails?.additionalDetails?.complainantDetails?.formdata
                            ?.map(
                              (data) => `${data?.data?.firstName}${data?.data?.middleName && " " + data?.data?.middleName} ${data?.data?.lastName}`
                            )
                            .join(", ")}
                        </span>
                      </div>
                    </div>
                    <div style={{ width: "50%" }}>
                      <h2 className="case-info-title">{t(JoinHomeLocalisation.RESPONDENTS_TEXT)}</h2>
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
        <div className="select-user-join-case">
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
              disabled={true}
              options={[t(JoinHomeLocalisation.ADVOCATE_OPT), t(JoinHomeLocalisation.LITIGANT_OPT)]}
            />
          </LabelFieldPair>
          {userType !== "" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">
                  {userType === t(JoinHomeLocalisation.LITIGANT_OPT)
                    ? t(JoinHomeLocalisation.WHICH_PARTY_AFFILIATED)
                    : `${t(JoinHomeLocalisation.PLEASE_CHOOSE_PARTY)}`}
                </CardLabel>
                <RadioButtons
                  selectedOption={selectedParty}
                  onSelect={(value) => {
                    setSelectedParty(value);
                    setRoleOfNewAdvocate("");
                    setRepresentingYourself("");
                  }}
                  options={[
                    `${caseDetails?.additionalDetails?.complainantDetails?.formdata
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
                    // setIsDisabled(false);
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
              additionalElements={[
                <p>
                  {t(JoinHomeLocalisation.ADD_ADVOCATE_LATER)}{" "}
                  <span style={{ fontWeight: "bold" }}>{t(JoinHomeLocalisation.PARTY_IN_PERSON_TEXT)}</span>
                </p>,
              ]}
              inline
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
                text={t(JoinHomeLocalisation.PRIMARY_ADD_SUPPORTING_ADVOCATE)}
                textStyle={{}}
                className={`custom-info-card`}
              />
              <div className="primary-advocate-details">
                <h3 className="contact-text">{t(JoinHomeLocalisation.CONTACT_PRIMARTY_ADVOCATE)}</h3>
                <CustomCaseInfoDiv t={t} data={primaryAdvocateDetail} />
              </div>
            </React.Fragment>
          ) : (
            <React.Fragment>
              <InfoCard
                variant={"default"}
                label={userType === "Litigant" ? t(JoinHomeLocalisation.PLEASE_NOTE) : t("INFO")}
                additionalElements={
                  userType === "Litigant" && representingYourself !== "Yes"
                    ? [
                        <p>
                          {t(JoinHomeLocalisation.ADD_ADVOCATE_LATER)}{" "}
                          <span style={{ fontWeight: "bold" }}>{t(JoinHomeLocalisation.PARTY_IN_PERSON_TEXT)}</span>
                        </p>,
                      ]
                    : userType === "Litigant" && representingYourself === "Yes"
                    ? [
                        <p>
                          {t(JoinHomeLocalisation.REPRESENT_SELF_PARTY)}{" "}
                          <span style={{ fontWeight: "bold" }}>{`(${JoinHomeLocalisation.PARTY_IN_PERSON_TEXT})`}</span>
                        </p>,
                      ]
                    : {}
                }
                inline
                text={
                  userType === "Litigant" && representingYourself !== "Yes"
                    ? undefined
                    : userType === "Litigant" && representingYourself === "Yes"
                    ? undefined
                    : t(JoinHomeLocalisation.FILL_FORM_VAKALATNAMA)
                }
                textStyle={{}}
                className={`custom-info-card`}
              />
              {userType !== "Litigant" ? (
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
                          setAdvocateDetail({});
                          setBarDetails([]);
                        }}
                        disable={userType === "Litigant" ? false : true}
                      />
                      {errors?.barRegNumber && <CardLabelError> {t(errors?.barRegNumber?.message)} </CardLabelError>}
                      {}
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
                </React.Fragment>
              ) : representingYourself !== "Yes" ? (
                <React.Fragment>
                  <FormComposerV2
                    config={barRegistrationSerachConfig}
                    onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
                      onFormValueChange(formData);
                    }}
                    defaultValues={advocateDetailForm}
                    cardStyle={{ minWidth: "100%" }}
                    secondaryLabel={t("CS_SAVE_DRAFT")}
                    cardClassName={`join-a-case-advocate-search`}
                    className={`advocate-detail`}
                    noBreakLine
                  />
                  {userType === "Litigant" && advocateDetail?.barRegistrationNumber && (
                    <SelectCustomDragDrop
                      t={t}
                      formData={formData}
                      config={documentUploaderConfig}
                      onSelect={(e, p) => {
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
                    <div className="join-case-tooltip-wrapper">
                      <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.AFFIDAVIT)}`}</CardLabel>
                      <CustomErrorTooltip message={`${t(JoinHomeLocalisation.AFFIDAVIT)}`} showTooltip={true} icon />
                    </div>
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

                      {errors?.affidavitText && <CardLabelError> {t(errors?.affidavitText?.message)} </CardLabelError>}
                      {}
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
            <div className="join-case-tooltip-wrapper">
              <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.ENTER_CODE_JOIN_CASE)}`}</CardLabel>
              <CustomErrorTooltip message={`${t(JoinHomeLocalisation.ENTER_CODE_JOIN_CASE)}`} showTooltip={true} icon />
            </div>
            <div style={{ width: "100%", maxWidth: "960px" }}>
              <TextInput
                // t={t}
                style={{ width: "100%" }}
                type={"text"}
                name="validationCode"
                value={validationCode}
                onChange={(e) => {
                  let val = e.target.value;
                  val = val.substring(0, 6);
                  setValidationCode(val);
                  if (val.length === 6) {
                    setIsDisabled(false);
                  } else {
                    setIsDisabled(true);
                  }

                  setErrors({
                    ...errors,
                    validationCode: undefined,
                  });
                }}
                // disable={editScreen}
              />
              {errors?.validationCode && <CardLabelError> {t(errors?.validationCode?.message)} </CardLabelError>}
              {}
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
            <h3 className="message-header">{messageHeader}</h3>
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
                      <h2 className="case-info-title">{t(JoinHomeLocalisation.COMPLAINANTS_TEXT)}</h2>
                      <div className="case-info-value">
                        <span>
                          {caseDetails?.additionalDetails?.complainantDetails?.formdata
                            ?.map(
                              (data) => `${data?.data?.firstName}${data?.data?.middleName && " " + data?.data?.middleName} ${data?.data?.lastName}`
                            )
                            .join(", ")}
                        </span>
                      </div>
                    </div>
                    <div style={{ width: "50%" }}>
                      <h2 className="case-info-title">{t(JoinHomeLocalisation.RESPONDENTS_TEXT)}</h2>
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
                <Button className={"selector-button-border"} label={t(JoinHomeLocalisation.BACK_HOME)} onButtonClick={() => closeModal()} />
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
    if (caseDetails?.caseCategory)
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

  const submitJoinCase = async (data) => {
    let res;
    try {
      res = await CASEService.joinCaseService(data, {});
      return [res, undefined];
    } catch (err) {
      return [res, err];
    }
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
        if (response?.criteria[0]?.responseList?.length === 1) {
          const caseData = response?.criteria[0]?.responseList[0];
          if (caseData?.status === "CASE_ADMITTED") {
            setCaseDetails(response?.criteria[0]?.responseList[0]);
            setCaseNumber(response?.criteria[0]?.responseList[0]?.filingNumber);
            setErrors({
              ...errors,
              caseNumber: undefined,
            });
          } else {
            setErrors({
              ...errors,
              caseNumber: {
                message: JoinHomeLocalisation.CASE_NO_ADMITTED_STATUS,
              },
            });
            setIsDisabled(true);
          }
        } else {
          setErrors({
            ...errors,
            caseNumber: {
              message: JoinHomeLocalisation.INVALID_CASE_INFO_TEXT,
            },
          });
        }
      } else {
        if (userType === t(JoinHomeLocalisation.ADVOCATE_OPT)) {
          const isFound = caseDetails?.representatives?.find((item) => item.advocateId === advocateId) !== undefined;
          if (isFound) {
            setStep(5);
            setMessageHeader("You are already part of this case");
            setSuccess(true);
          } else {
            setStep(step + 1);
          }
        } else if (userType === t(JoinHomeLocalisation.LITIGANT_OPT)) {
          const isFound = caseDetails?.litigants?.find((item) => item.individualId === individualId) !== undefined;
          if (isFound) {
            setStep(5);
            setMessageHeader("You are already part of this case");
            setSuccess(true);
          } else {
            setStep(step + 1);
          }
        } else {
          setStep(step + 1);
        }
        setIsDisabled(true);
      }
    } else if (step === 1) {
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
        setParties([...parties, { title: selectedParty, value: selectedParty }]);
        setParty(selectedParty);
        if (roleOfNewAdvocate !== "I’m a supporting advocate") {
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
        if (representingYourself !== "Yes" && advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.data?.barRegistrationNumber) {
          setIsDisabled(true);
          setStep(step + 2);
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
        const [res, err] = await submitJoinCase({
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
                  ? caseDetails?.additionalDetails?.complainantDetails?.formdata[0]?.data?.complainantVerification?.individualDetails?.individualId
                  : caseDetails?.additionalDetails?.respondentDetails?.formdata[0]?.data?.individualDetails?.individualId,
                partyType: selectedParty.includes("Complainant") ? "complainant.primary" : "respondent.primary",
              },
            ],
          },
        });
        if (res) {
          setStep(step + 1);
          setSuccess(true);
        } else {
          setErrors({
            ...errors,
            validationCode: {
              message: JoinHomeLocalisation.INVALID_ACCESS_CODE_MESSAGE,
            },
          });
        }
      } else {
        const cPrimary = caseDetails?.litigants?.find((item) => item?.partyType === "complainant.primary") !== undefined;
        const rPrimary = caseDetails?.litigants?.find((item) => item?.partyType === "respondent.primary") !== undefined;
        let partyType = "";
        if (selectedParty.includes("Complainant")) {
          if (cPrimary) partyType = "complainant.additional";
          else partyType = "complainant.primary";
        } else {
          if (rPrimary) partyType = "respondent.additional";
          else partyType = "respondent.primary";
        }
        if (representingYourself === "Yes") {
          const [res, err] = await submitJoinCase(
            {
              caseFilingNumber: caseNumber,
              tenantId: tenantId,
              accessCode: validationCode,
              litigant: {
                additionalDetails: {
                  first: name?.givenName,
                  middleName: name?.otherNames,
                  lastName: name?.familyName,
                },
                tenantId: tenantId,
                individualId: individualId,
                affidavitText,
                partyCategory: "INDIVIDUAL",
                partyType: partyType,
              },
            },
            {}
          );
          if (res) {
            setStep(step + 1);
            setSuccess(true);
          } else {
            setErrors({
              ...errors,
              validationCode: {
                message: JoinHomeLocalisation.INVALID_ACCESS_CODE_MESSAGE,
              },
            });
          }
        }
      }
    } else if (step === 5) {
      setStep(6);
    }
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
          actionSaveLabel={
            roleOfNewAdvocate === "I’m a supporting advocate" && step === 2
              ? t("GOT_IT_TEXT")
              : userType === "Litigant" && step === 0 && caseInfo.length === 0
              ? "Search"
              : t("PROCEED_TEXT")
          }
          actionSaveOnSubmit={onProceed}
          formId="modal-action"
          headerBarMain={<Heading label={t("JOIN_A_CASE")} />}
          className={`join-a-case-modal ${success && "case-join-success"}`}
          popupModuleActionBarClassName={`${
            step === 2 && userType === "Litigant" && representingYourself !== "Yes" ? "join-case-form-composer" : ""
          }`}
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
