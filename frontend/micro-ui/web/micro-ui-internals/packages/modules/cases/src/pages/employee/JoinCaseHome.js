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
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { InfoCard } from "@egovernments/digit-ui-components";
import { DRISTIService } from "../../../../dristi/src/services";
import { AdvocateIcon, RightArrow, SearchIcon } from "../../../../dristi/src/icons/svgIndex";
import { CASEService } from "../../hooks/services";
import isEqual from "lodash/isEqual";
import { useTranslation } from "react-i18next";

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
  CASE_NO_ADMITTED_STATUS: "CASE_NO_ADMITTED_STATUS",
  INVALID_ACCESS_CODE_MESSAGE: "INVALID_ACCESS_CODE_MESSAGE",
  AFFIDAVIT_MINIMUM_CHAR_MESSAGE: "AFFIDAVIT_MINIMUM_CHAR_MESSAGE",
  FILLING_NUMBER_FORMATE_TEXT: `FILLING_NUMBER_FORMATE_TEXT`,
  FILLING_NUMBER_FORMATE_TEXT_VALUE: "FILLING_NUMBER_FORMATE_TEXT_VALUE",
  INVALID_CASE_INFO_TEXT: "INVALID_CASE_INFO_TEXT",
  NYAYA_MITRA_TEXT: "NYAYA_MITRA_TEXT",
  FOR_SUPPORT_TEXT: "FOR_SUPPORT_TEXT",
  COMPLAINANTS_TEXT: "COMPLAINANTS_TEXT",
  RESPONDENTS_TEXT: "RESPONDENTS_TEXT",
  WHICH_PARTY_AFFILIATED: "WHICH_PARTY_AFFILIATED",
  ADD_ADVOCATE_LATER: "ADD_ADVOCATE_LATER",
  PARTY_IN_PERSON_TEXT: "PARTY_IN_PERSON_TEXT",
  PRIMARY_ADD_SUPPORTING_ADVOCATE: "PRIMARY_ADD_SUPPORTING_ADVOCATE",
  CONTACT_PRIMARTY_ADVOCATE: "CONTACT_PRIMARTY_ADVOCATE",
  REPRESENT_SELF_PARTY: "REPRESENT_SELF_PARTY",
  NO_OBJECTION_UPLOAD_TEXT: "NO_OBJECTION_UPLOAD_TEXT",
  COURT_ORDER_UPLOAD_TEXT: "COURT_ORDER_UPLOAD_TEXT",
  ALREADY_PART_OF_CASE: "ALREADY_PART_OF_CASE",
  CASE_NOT_ADMITTED_TEXT: "The above case is not in the admitted state.",
  JOIN_CASE_BACK_TEXT: "Back",
};

const advocateVakalatnamaAndNocConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "nocFileUpload",
        isMandatory: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "NO_OBJECTION_UPLOAD_TEXT",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "advocateCourtOrder",
        isMandatory: true,
        withoutLabel: true,
        populators: {
          inputs: [
            {
              name: "document",
              documentHeader: "COURT_ORDER_UPLOAD_TEXT",
              infoTooltipMessage: "Tooltip",
              type: "DragDropComponent",
              uploadGuidelines: "UPLOAD_DOC_50",
              maxFileSize: 50,
              maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
              fileTypes: ["JPG", "PNG", "PDF"],
              isMultipleUpload: false,
            },
          ],
        },
      },
    ],
  },
];
const advocateVakalatnamaConfig = [
  {
    body: [
      {
        type: "component",
        component: "SelectCustomDragDrop",
        key: "adcVakalatnamaFileUpload",
        isMandatory: true,
        withoutLabel: true,
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
            },
          ],
        },
      },
    ],
  },
];

const JoinCaseHome = ({ refreshInbox }) => {
  const { t } = useTranslation();

  const Modal = window?.Digit?.ComponentRegistryService?.getComponent("MODAL");
  const CustomCaseInfoDiv = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMCASEINFODIV");
  const DocViewerWrapper = window?.Digit?.ComponentRegistryService?.getComponent("DOCVIEWERWRAPPER");
  const SelectCustomDragDrop = window?.Digit?.ComponentRegistryService?.getComponent("SelectCustomDragDrop");
  const CustomErrorTooltip = window?.Digit?.ComponentRegistryService?.getComponent("CUSTOMERRORTOOLTIP");
  const CustomButton = window?.Digit?.ComponentRegistryService?.getComponent("CustomButton");
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const [show, setShow] = useState(false);
  const [step, setStep] = useState(0);
  const [caseNumber, setCaseNumber] = useState("");
  const [caseDetails, setCaseDetails] = useState({});
  const [searchCaseResult, setSearchCaseResult] = useState({});
  const [userType, setUserType] = useState("");
  const [barRegNumber, setBarRegNumber] = useState("");
  const [barDetails, setBarDetails] = useState([]);
  const [selectedParty, setSelectedParty] = useState({});
  const [representingYourself, setRepresentingYourself] = useState("");
  const [roleOfNewAdvocate, setRoleOfNewAdvocate] = useState("");
  const [parties, setParties] = useState([]);
  const [advocateDetail, setAdvocateDetail] = useState({});
  const [advocateDetailForm, setAdvocateDetailForm] = useState({});
  const [replaceAdvocateDocuments, setReplaceAdvocateDocuments] = useState({});
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
  const [individualAddress, setIndividualAddress] = useState({});
  const [name, setName] = useState({});
  const [isSignedAdvocate, setIsSignedAdvocate] = useState(false);
  const [isSignedParty, setIsSignedParty] = useState(false);
  const [complainantList, setComplainantList] = useState([]);
  const [respondentList, setRespondentList] = useState([]);
  const [individualDoc, setIndividualDoc] = useState([]);
  const [advocateName, setAdvocateName] = useState("");

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

  const barRegistrationSerachConfig = useMemo(() => {
    return [
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
              moduleName: "getAdvocateNameUsingBarRegistrationNumberJoinCase",
              customfn: "getNames",
              optionsKey: "barRegistrationNumber",
              removeOptions: caseDetails?.additionalDetails?.advocateDetails?.formdata?.map((data) => data?.data?.barRegistrationNumber),
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
  }, [caseDetails]);

  const onFormValueChange = (formData) => {
    if (!isEqual(formData, advocateDetailForm)) {
      setAdvocateDetailForm(formData);
    }
  };

  useEffect(() => {
    if (step === 0 && !caseNumber) {
      setErrors({
        ...errors,
        caseNumber: undefined,
      });
    }
    if (step === 1) {
      if (userType && userType === "Litigant" && selectedParty?.label && representingYourself) {
        setIsDisabled(false);
      } else if (userType && userType === "Advocate") {
        if (selectedParty?.label) {
          if (selectedParty?.isComplainant) {
            if (
              (caseDetails?.additionalDetails?.advocateDetails?.formdata?.length > 0 && roleOfNewAdvocate) ||
              caseDetails?.additionalDetails?.advocateDetails?.formdata?.length === 0
            ) {
              setIsDisabled(false);
            } else {
              setIsDisabled(true);
            }
          } else {
            setIsDisabled(false);
          }
        }
      } else {
        setIsDisabled(true);
      }
    } else if (step === 2) {
      if (userType === "Litigant" && representingYourself !== "Yes") {
        if (advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber && advocateDetailForm?.vakalatnamaFileUpload) {
          setIsDisabled(false);
        } else {
          setIsDisabled(true);
        }
      } else if (userType === "Litigant" && representingYourself === "Yes" && affidavitText) {
        if (affidavitText?.length > 1) {
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
    } else if (step === 4) {
      if (isSignedAdvocate && isSignedParty) {
        setIsDisabled(false);
      } else setIsDisabled(true);
    }

    if (step !== 8) {
      setSuccess(false);
    }
  }, [step, userType, selectedParty, representingYourself, roleOfNewAdvocate, caseNumber, barRegNumber, affidavitText, parties, advocateDetailForm]);

  const searchCase = async (caseNumber) => {
    if (caseNumber) {
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
        if (response?.criteria[0]?.responseList[0]?.status === "CASE_ADMITTED") {
          setIsDisabled(false);
          setSearchCaseResult(response?.criteria[0]?.responseList[0]);
          setErrors({
            ...errors,
            caseNumber: undefined,
          });
        } else {
          setIsDisabled(true);
          setErrors({
            ...errors,
            caseNumber: {
              type: "not-admitted",
              message: JoinHomeLocalisation.CASE_NOT_ADMITTED_TEXT,
            },
          });
        }
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
    }
  };

  useEffect(() => {
    const getData = setTimeout(() => {
      searchCase(caseNumber);
    }, 1000);
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
    setIndividualId(individualData?.Individual?.[0]?.individualId);
    setName(individualData?.Individual?.[0]?.name);
    const addressLine1 = individualData?.Individual?.[0]?.address[0]?.addressLine1 || "Telangana";
    const addressLine2 = individualData?.Individual?.[0]?.address[0]?.addressLine2 || "Rangareddy";
    const buildingName = individualData?.Individual?.[0]?.address[0]?.buildingName || "";
    const street = individualData?.Individual?.[0]?.address[0]?.street || "";
    const city = individualData?.Individual?.[0]?.address[0]?.city || "";
    const pincode = individualData?.Individual?.[0]?.address[0]?.pincode || "";
    const latitude = individualData?.Individual?.[0]?.address[0]?.latitude || "";
    const longitude = individualData?.Individual?.[0]?.address[0]?.longitude || "";
    const doorNo = individualData?.Individual?.[0]?.address[0]?.doorNo || "";
    const address = `${doorNo ? doorNo + "," : ""} ${buildingName ? buildingName + "," : ""} ${street}`.trim();
    const identifierIdDetails = JSON.parse(
      individualData?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "identifierIdDetails")?.value || "{}"
    );
    const idType = individualData?.Individual?.[0]?.identifiers[0]?.identifierType || "";
    setIndividualDoc(
      identifierIdDetails?.fileStoreId
        ? [{ fileName: `${idType} Card`, fileStore: identifierIdDetails?.fileStoreId, documentName: identifierIdDetails?.filename }]
        : null
    );
    setIndividualAddress({
      pincode: pincode,
      district: addressLine2,
      city: city,
      state: addressLine1,
      coordinates: {
        longitude: latitude,
        latitude: longitude,
      },
      locality: address,
    });
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
      setAdvocateName(advocateResponse?.advocates[0]?.responseList[0]?.additionalDetails?.username);
      setUserType(t(JoinHomeLocalisation.ADVOCATE_OPT));
    } else {
      setUserType(t(JoinHomeLocalisation.LITIGANT_OPT));
    }
  };

  useEffect(() => {
    fetchBasicUserInfo();
  }, [show]);

  const paymentCalculation = [
    { key: "Amount Due", value: 600, currency: "Rs" },
    { key: "Court Fees", value: 400, currency: "Rs" },
    { key: "Advocate Fees", value: 1000, currency: "Rs" },
    { key: "Total Fees", value: 2000, currency: "Rs", isTotalFee: true },
  ];

  const modalItem = [
    // 0
    {
      modalMain: (
        <div className="case-number-input">
          <LabelFieldPair className="case-label-field-pair">
            <CardLabel className="case-input-label">{`${t(JoinHomeLocalisation.ENTER_CASE_NUMBER)}`}</CardLabel>
            <div style={{ width: "100%", display: "flex" }}>
              <TextInput
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
              />
              <div className="icon-div">
                <SearchIcon />
              </div>
            </div>
            <p style={{ fontSize: "12px" }}>
              {t(JoinHomeLocalisation.FILLING_NUMBER_FORMATE_TEXT)} {"F-<StatuteSection>-<YYYY>-<7 digit sequence number>"}
            </p>
          </LabelFieldPair>
          {errors?.caseNumber && (
            <InfoCard
              variant={"default"}
              label={t(JoinHomeLocalisation.INVALID_CASE_FILING_NUMBER)}
              additionalElements={
                !errors?.caseNumber?.type && [
                  <p>
                    {t(JoinHomeLocalisation.INVALID_CASE_INFO_TEXT)}{" "}
                    <span style={{ fontWeight: "bold" }}>{t(JoinHomeLocalisation.NYAYA_MITRA_TEXT)}</span> {t(JoinHomeLocalisation.FOR_SUPPORT_TEXT)}
                  </p>,
                ]
              }
              text={errors?.caseNumber?.type === "not-admitted" && t(errors?.caseNumber?.message)}
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
                setSelectedParty({});
                setRepresentingYourself("");
                setRoleOfNewAdvocate("");
              }}
              disabled={true}
              options={["Advocate", "Litigant"]}
            />
          </LabelFieldPair>
          {userType !== "" && (
            <React.Fragment>
              <hr className="horizontal-line" />
              <LabelFieldPair className="case-label-field-pair">
                <CardLabel className="case-input-label">
                  {userType === "Litigant" ? t(JoinHomeLocalisation.WHICH_PARTY_AFFILIATED) : `${t(JoinHomeLocalisation.PLEASE_CHOOSE_PARTY)}`}
                </CardLabel>
                <RadioButtons
                  selectedOption={selectedParty}
                  onSelect={(value) => {
                    setSelectedParty(value);
                    setRoleOfNewAdvocate("");
                    setRepresentingYourself("");
                  }}
                  optionsKey={"label"}
                  options={userType === "Litigant" ? respondentList : [...complainantList, ...respondentList]}
                />
              </LabelFieldPair>
            </React.Fragment>
          )}
          {selectedParty?.label &&
            caseDetails?.representatives?.find((data) => data?.representing?.[0]?.individualId === selectedParty?.individualId) !== undefined &&
            userType === "Advocate" && (
              <React.Fragment>
                <hr className="horizontal-line" />
                <InfoCard
                  variant={"warning"}
                  label={t(JoinHomeLocalisation.WARNING)}
                  additionalElements={[
                    <p>
                      {t(JoinHomeLocalisation.FOR_THE_SELECTED)} <span style={{ fontWeight: "bold" }}>{selectedParty?.label}</span>{" "}
                      {t(JoinHomeLocalisation.ALREADY_AN_ADVOCATE)}
                    </p>,
                  ]}
                  inline
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
          {selectedParty?.label && userType === "Litigant" && (
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
                          <span style={{ fontWeight: "bold" }}>{`(${t(JoinHomeLocalisation.PARTY_IN_PERSON_TEXT)})`}</span>
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
                        selected={party}
                        optionKey={"label"}
                        select={(e) => setParty(e)}
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
                          let input = e.target.value;
                          input = input.slice(0, 100).trimStart().replace(/\s+/g, " ");
                          setAffidavitText(input);
                          if (!input) setIsDisabled(true);
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
          <FormComposerV2
            key={2}
            config={advocateVakalatnamaConfig}
            onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
              if (!isEqual(formData, adovacteVakalatnama)) {
                setAdovacteVakalatnama(formData);
              }
              if (formData?.adcVakalatnamaFileUpload) {
                setIsDisabled(false);
              } else setIsDisabled(true);
            }}
            defaultValues={adovacteVakalatnama}
            cardStyle={{ minWidth: "100%" }}
            secondaryLabel={t("CS_SAVE_DRAFT")}
            className={`noc-court-order-upload-form`}
            noBreakLine
          />
          {adovacteVakalatnama && adovacteVakalatnama?.adcVakalatnamaFileUpload?.document && (
            <DocViewerWrapper
              key={adovacteVakalatnama?.adcVakalatnamaFileUpload?.document?.[0]?.File?.name}
              selectedDocs={adovacteVakalatnama?.adcVakalatnamaFileUpload?.document}
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
        <div className="e-sign-page">
          <InfoCard
            variant={"default"}
            label={t(JoinHomeLocalisation.PLEASE_NOTE)}
            additionalElements={{}}
            inline
            text={"This vakalatnama will formally be added to the case after all relevant parties have added their signatures."}
            textStyle={{}}
            className={`custom-info-card`}
          />
          <div className="select-signature-main">
            <div className="select-signature-header">
              <h1 className="signature-label">{"1. Advocate Signature"}</h1>
            </div>
            <div className="select-signature-list">
              <div className="signature-body">
                <div className="icon-and-title">
                  <AdvocateIcon />
                  <h3 className="signature-title">{advocateDetailForm?.additionalDetails?.username}</h3>
                </div>
                {isSignedAdvocate && <span className="signed">{t("SIGNED")}</span>}
                {!isSignedAdvocate && (
                  <div className="signed-button-group">
                    <CustomButton
                      label={"E-Sign"}
                      onButtonClick={() => {
                        setIsDisabled(false);
                        setIsSignedAdvocate(true);
                      }}
                      className={"aadhar-sign-in"}
                      labelClassName={"aadhar-sign-in"}
                    ></CustomButton>
                  </div>
                )}
              </div>
            </div>
          </div>
          <div className="select-signature-main" key={"advocate"}>
            <div className="select-signature-header">
              <h1 className="signature-label">{"2. Party Signature"}</h1>
            </div>
            <div className="select-signature-list" key={"party"}>
              <div className="signature-body">
                <div className="icon-and-title">
                  <AdvocateIcon />
                  <h3 className="signature-title">{selectedParty?.label}</h3>
                </div>
                {isSignedParty && <span className="signed">{t("SIGNED")}</span>}
                {!isSignedParty && (
                  <div className="signed-button-group">
                    <CustomButton
                      label={"E-Sign"}
                      onButtonClick={() => {
                        setIsSignedParty(true);
                      }}
                      className={"aadhar-sign-in"}
                      labelClassName={"aadhar-sign-in"}
                    ></CustomButton>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      ),
    },
    // 5
    {
      modalMain: (
        <div className="payment-due-wrapper" style={{ display: "flex", flexDirection: "column" }}>
          <div className="payment-calculator-wrapper" style={{ display: "flex", flexDirection: "column" }}>
            {paymentCalculation.map((item) => (
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  borderTop: item.isTotalFee && "1px solid #BBBBBD",
                  fontSize: item.isTotalFee && "16px",
                  fontWeight: item.isTotalFee && "700",
                  paddingTop: item.isTotalFee && "12px",
                }}
              >
                <span>{item.key}</span>
                <span>
                  {item.currency} {item.value}
                </span>
              </div>
            ))}
          </div>
        </div>
      ),
    },
    // 6
    {
      modalMain: (
        <div className="noc-court-order-upload">
          <FormComposerV2
            key={1}
            config={advocateVakalatnamaAndNocConfig}
            onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
              if (!isEqual(formData, replaceAdvocateDocuments)) {
                setReplaceAdvocateDocuments(formData);
              }
              if (formData?.nocFileUpload && formData?.advocateCourtOrder) {
                setIsDisabled(false);
              } else setIsDisabled(true);
            }}
            defaultValues={replaceAdvocateDocuments}
            cardStyle={{ minWidth: "100%" }}
            secondaryLabel={t("CS_SAVE_DRAFT")}
            className={`noc-court-order-upload-form`}
            noBreakLine
          />
        </div>
      ),
    },
    // 7
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
                  val = val.replace(/\D/g, "");
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
    // 8
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
                <Button
                  className={"selector-button-border"}
                  label={t(JoinHomeLocalisation.BACK_HOME)}
                  onButtonClick={() => {
                    closeModal();
                    refreshInbox();
                  }}
                />
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

  const onDocumentUpload = async (fileData, filename, tenantId) => {
    if (fileData?.fileStore) return fileData;
    const fileUploadRes = await window?.Digit.UploadServices.Filestorage("DRISTI", fileData, tenantId);
    return { file: fileUploadRes?.data, fileType: fileData.type, filename };
  };

  useEffect(() => {
    if (caseDetails?.caseCategory) {
      setCaseInfo([
        {
          key: "CASE_CATEGORY",
          value: caseDetails?.caseCategory,
        },
        {
          key: "CASE_TYPE",
          value: `${caseDetails?.statutesAndSections?.[0]?.sections?.[0]}, ${caseDetails?.statutesAndSections?.[0]?.subsections?.[0]}`,
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

      setComplainantList(
        caseDetails?.additionalDetails?.complainantDetails?.formdata?.map((data, index) => ({
          ...data?.data,
          label: `${data?.data?.firstName} ${data?.data?.middleName && data?.data?.middleName + " "}${data?.data?.lastName} ${t(
            JoinHomeLocalisation.COMPLAINANT_BRACK
          )}`,
          partyType: index === 0 ? "complainant.primary" : "complainant.additional",
          isComplainant: true,
          individualId: data?.data?.complainantVerification?.individualDetails?.individualId,
        }))
      );
      setRespondentList(
        caseDetails?.additionalDetails?.respondentDetails?.formdata
          ?.map((data, index) => ({
            ...data?.data,
            label: `${data?.data?.respondentFirstName}${data?.data?.respondentMiddleName ? " " + data?.data?.respondentMiddleName : ""} ${
              data?.data?.respondentLastName
            } ${t(JoinHomeLocalisation.RESPONDENT_BRACK)}`,
            index: index,
            partyType: index === 0 ? "respondent.primary" : "respondent.additional",
            isRespondent: true,
            individualId: data?.data?.respondentVerification?.individualDetails?.individualId,
          }))
          ?.filter((data) => data?.respondentVerification?.individualDetails?.individualId)
          ?.map((data) => data)
      );
    }
  }, [caseDetails]);

  useEffect(() => {
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
        value: advocateName,
      },
    ]);
  }, [caseDetails, advocateName]);

  const closeModal = () => {
    setCaseNumber("");
    setCaseDetails({});
    setUserType("");
    setSelectedParty({});
    setRepresentingYourself("");
    setRoleOfNewAdvocate("");
    setBarRegNumber("");
    setBarDetails([]);
    setValidationCode("");
    setErrors({});
    setCaseInfo([]);
    setStep(0);
    setShow(false);
    setIsSignedAdvocate(false);
    setIsSignedParty(false);
    setAdvocateDetailForm({});
    setReplaceAdvocateDocuments({});
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

  const onProceed = useCallback(async () => {
    if (step === 0) {
      if (!caseDetails?.caseNumber) {
        setCaseDetails(searchCaseResult);
        setCaseNumber(searchCaseResult?.filingNumber);
      } else {
        if (userType === "Advocate") {
          const isFound = caseDetails?.representatives?.find((item) => item.advocateId === advocateId) !== undefined;
          if (isFound) {
            setStep(8);
            setMessageHeader(t(JoinHomeLocalisation.ALREADY_PART_OF_CASE));
            setSuccess(true);
          } else {
            setStep(step + 1);
          }
        } else if (userType === "Litigant") {
          const isFound = caseDetails?.litigants?.find((item) => item.individualId === individualId) !== undefined;
          if (isFound) {
            setStep(8);
            setMessageHeader(t(JoinHomeLocalisation.ALREADY_PART_OF_CASE));
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
      if (userType && userType === "Litigant" && selectedParty?.label && representingYourself) {
        setBarRegNumber("");
        setIsDisabled(true);
        setStep(step + 1);
        setBarDetails([]);
        setErrors({
          ...errors,
          barRegNumber: undefined,
        });
      } else if (userType && userType === "Advocate" && selectedParty?.label) {
        setParties([...parties, selectedParty]);
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
          setStep(step + 5);
        } else {
          if (affidavitText.endsWith(" ")) setAffidavitText(affidavitText.slice(0, -1));
          setIsDisabled(true);
          setStep(step + 5);
        }
      } else {
        setIsDisabled(false);
        setStep(step + 1);
      }
    } else if (step === 3) {
      setIsDisabled(true);
      setStep(step + 3);
    } else if (step === 4) {
      setStep(step + 1);
      setIsDisabled(false);
    } else if (step === 5) {
      if (roleOfNewAdvocate === t(JoinHomeLocalisation.PRIMARY_ADVOCATE)) {
        setStep(step + 1);
      } else {
        setStep(step + 2);
      }
      setIsDisabled(true);
    } else if (step === 6) {
      setStep(step + 1);
      setIsDisabled(true);
    } else if (step === 7 && validationCode.length === 6) {
      if (userType === "Advocate") {
        if (caseDetails?.additionalDetails?.advocateDetails?.formdata?.length > 0) {
          const nocDocument = await Promise.all(
            replaceAdvocateDocuments?.nocFileUpload?.document?.map(async (document) => {
              if (document) {
                const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                return {
                  documentType: uploadedData.fileType || document?.documentType,
                  fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                  documentName: uploadedData.filename || document?.documentName,
                  fileName: `NOC (${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName})`,
                  individualId,
                };
              }
            }) || []
          );
          const courOrderDocument = await Promise.all(
            replaceAdvocateDocuments?.advocateCourtOrder?.document?.map(async (document) => {
              if (document) {
                const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                return {
                  documentType: uploadedData.fileType || document?.documentType,
                  fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                  documentName: uploadedData.filename || document?.documentName,
                  fileName: `Court Order (${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName})`,
                  individualId,
                };
              }
            }) || []
          );
          const vakalatnamaDocument = await Promise.all(
            adovacteVakalatnama?.adcVakalatnamaFileUpload?.document?.map(async (document) => {
              if (document) {
                const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                return {
                  documentType: uploadedData.fileType || document?.documentType,
                  fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                  documentName: uploadedData.filename || document?.documentName,
                  fileName: `Vakalatnama (${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName})`,
                  individualId,
                };
              }
            }) || []
          );
          const [res, err] = await submitJoinCase({
            additionalDetails: {
              ...caseDetails?.additionalDetails,
              advocateDetails: (() => {
                const advocateFormdataCopy = structuredClone(caseDetails?.additionalDetails?.advocateDetails?.formdata);
                if (selectedParty?.isComplainant)
                  advocateFormdataCopy.splice(0, 1, {
                    data: {
                      advocateId: advocateDetailForm?.id,
                      advocateName: advocateDetailForm?.additionalDetails?.username,
                      barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                      vakalatnamaFileUpload: vakalatnamaDocument?.length > 0 && vakalatnamaDocument,
                      nocFileUpload: nocDocument?.length > 0 && nocDocument,
                      courtOrderFileUpload: courOrderDocument?.length > 0 && courOrderDocument,
                      isAdvocateRepresenting: {
                        code: "YES",
                        name: "Yes",
                        showForm: true,
                        isEnabled: true,
                      },
                      advocateBarRegNumberWithName: [
                        {
                          modified: true,
                          advocateId: advocateDetailForm?.id,
                          advocateName: advocateDetailForm?.additionalDetails?.username,
                          barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                          barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                        },
                      ],
                      barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                    },
                  });
                return {
                  ...caseDetails?.additionalDetails?.advocateDetails,
                  formdata: selectedParty?.isComplainant
                    ? advocateFormdataCopy
                    : [
                        ...caseDetails?.additionalDetails?.advocateDetails?.formdata,
                        {
                          data: {
                            advocateId: advocateDetailForm?.id,
                            advocateName: advocateDetailForm?.additionalDetails?.username,
                            barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                            vakalatnamaFileUpload: vakalatnamaDocument?.length > 0 && vakalatnamaDocument,
                            nocFileUpload: nocDocument?.length > 0 && nocDocument,
                            courtOrderFileUpload: courOrderDocument?.length > 0 && courOrderDocument,
                            isAdvocateRepresenting: {
                              code: "YES",
                              name: "Yes",
                              showForm: true,
                              isEnabled: true,
                            },
                            advocateBarRegNumberWithName: [
                              {
                                modified: true,
                                advocateId: advocateDetailForm?.id,
                                advocateName: advocateDetailForm?.additionalDetails?.username,
                                barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                                barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                              },
                            ],
                            barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                          },
                        },
                      ],
                };
              })(),
            },
            caseFilingNumber: caseNumber,
            tenantId: tenantId,
            accessCode: validationCode,
            representative: {
              tenantId: tenantId,
              advocateId: advocateId,
              representing: [
                {
                  tenantId: tenantId,
                  individualId: selectedParty?.individualId || null,
                  partyType: selectedParty?.partyType,
                },
              ],
              additionalDetails: {
                document: {
                  vakalatnamaFileUpload: vakalatnamaDocument?.length > 0 && vakalatnamaDocument,
                  nocFileUpload: nocDocument?.length > 0 && nocDocument,
                  courtOrderFileUpload: courOrderDocument?.length > 0 && courOrderDocument,
                },
              },
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
          const [res, err] = await submitJoinCase({
            additionalDetails: {
              ...caseDetails?.additionalDetails,
              advocateDetails: (() => {
                return {
                  ...caseDetails?.additionalDetails?.advocateDetails,
                  formdata: [
                    ...caseDetails?.additionalDetails?.advocateDetails?.formdata,
                    {
                      data: {
                        advocateId: advocateDetailForm?.id,
                        advocateName: advocateDetailForm?.additionalDetails?.username,
                        barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                        vakalatnamaFileUpload: {
                          document: [
                            {
                              documentType: adovacteVakalatnama?.documentType,
                              fileStore: adovacteVakalatnama?.fileStore,
                              documentName: adovacteVakalatnama?.additionalDetails?.fileName,
                              fileName: `Vakalatnama (${selectedParty?.label})`,
                              individualId: selectedParty?.individualId,
                            },
                          ],
                        },
                        isAdvocateRepresenting: {
                          code: "YES",
                          name: "Yes",
                          showForm: true,
                          isEnabled: true,
                        },
                        advocateBarRegNumberWithName: [
                          {
                            modified: true,
                            advocateId: advocateDetailForm?.id,
                            advocateName: advocateDetailForm?.additionalDetails?.username,
                            barRegistrationNumber: advocateDetailForm?.barRegistrationNumber,
                            barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                          },
                        ],
                        barRegistrationNumberOriginal: advocateDetailForm?.barRegistrationNumber,
                      },
                    },
                  ],
                };
              })(),
            },
            caseFilingNumber: caseNumber,
            tenantId: tenantId,
            accessCode: validationCode,
            representative: {
              tenantId: tenantId,
              advocateId: advocateId,
              representing: [
                {
                  tenantId: tenantId,
                  individualId: selectedParty?.individualId || null,
                  partyType: selectedParty?.partyType,
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
        }
      } else {
        if (representingYourself === "Yes") {
          const [res, err] = await submitJoinCase(
            {
              additionalDetails: {
                ...caseDetails?.additionalDetails,
                respondentDetails: {
                  ...caseDetails?.additionalDetails?.respondentDetails,
                  formdata: [
                    ...caseDetails?.additionalDetails?.respondentDetails?.formdata?.map((data, index) => {
                      if (index === selectedParty?.index) {
                        return {
                          ...data,
                          data: {
                            ...data?.data,
                            respondentFirstName: name?.givenName,
                            respondentMiddleName: name?.otherNames,
                            respondentLastName: name?.familyName,
                            addressDetails: [
                              {
                                ...data?.data?.addressDetails?.[0],
                                addressDetails: {
                                  ...data?.data?.addressDetails?.[0]?.addressDetails,
                                  ...individualAddress,
                                },
                              },
                            ],
                            respondentVerification: {
                              individualDetails: {
                                individualId: individualId,
                                document: individualDoc,
                              },
                            },
                          },
                        };
                      }
                      return data;
                    }),
                  ],
                },
              },
              caseFilingNumber: caseNumber,
              tenantId: tenantId,
              accessCode: validationCode,
              litigant: {
                additionalDetails: {
                  firstName: name?.givenName,
                  middleName: name?.otherNames,
                  lastName: name?.familyName,
                  fullName: `${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName}`,
                  affidavitText,
                },
                tenantId: tenantId,
                individualId: individualId,
                partyCategory: "INDIVIDUAL",
                partyType: selectedParty?.partyType,
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
        } else {
          const newDocument = await Promise.all(
            advocateDetailForm?.vakalatnamaFileUpload?.document?.map(async (document) => {
              if (document) {
                const uploadedData = await onDocumentUpload(document, document.name, tenantId);
                return {
                  documentType: uploadedData.fileType || document?.documentType,
                  fileStore: uploadedData.file?.files?.[0]?.fileStoreId || document?.fileStore,
                  documentName: uploadedData.filename || document?.documentName,
                  fileName: `Vakalatnama (${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName})`,
                  individualId,
                };
              }
            }) || []
          );
          const [res, err] = await submitJoinCase(
            {
              additionalDetails: {
                ...caseDetails?.additionalDetails,
                respondentDetails: {
                  ...caseDetails?.additionalDetails?.respondentDetails,
                  formdata: [
                    ...caseDetails?.additionalDetails?.respondentDetails?.formdata?.map((data, index) => {
                      if (index === selectedParty?.index) {
                        return {
                          ...data,
                          data: {
                            ...data?.data,
                            respondentFirstName: name?.givenName,
                            respondentMiddleName: name?.otherNames,
                            respondentLastName: name?.familyName,
                            respondentVerification: {
                              individualDetails: {
                                individualId: individualId,
                                document: individualDoc,
                              },
                            },
                          },
                        };
                      }
                      return data;
                    }),
                  ],
                },
                ...(advocateDetailForm?.advocateBarRegNumberWithName && {
                  advocateDetails: (() => {
                    if (
                      caseDetails?.additionalDetails?.advocateDetails?.formdata?.some(
                        (data) => data?.data?.advocateId === advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateId
                      )
                    ) {
                      return {
                        ...caseDetails?.additionalDetails?.advocateDetails,
                        formdata: [
                          ...caseDetails?.additionalDetails?.advocateDetails?.formdata?.map((data, index) => {
                            if (data?.data?.advocateId === advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateId)
                              return {
                                ...data,
                                data: {
                                  ...data?.data,
                                  vakalatnamaFileUpload: {
                                    document: [...data?.data?.vakalatnamaFileUpload?.document, ...newDocument],
                                  },
                                },
                              };
                            return data;
                          }),
                        ],
                      };
                    } else {
                      return {
                        ...caseDetails?.additionalDetails?.advocateDetails,
                        formdata: [
                          ...caseDetails?.additionalDetails?.advocateDetails?.formdata,
                          {
                            data: {
                              advocateId: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateId,
                              advocateName: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateName,
                              barRegistrationNumber: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
                              vakalatnamaFileUpload: {
                                document: [...newDocument],
                              },
                              isAdvocateRepresenting: {
                                code: "YES",
                                name: "Yes",
                                showForm: true,
                                isEnabled: true,
                              },
                              advocateBarRegNumberWithName: [
                                {
                                  modified: true,
                                  advocateId: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateId,
                                  advocateName: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateName,
                                  barRegistrationNumber: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
                                  barRegistrationNumberOriginal: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
                                },
                              ],
                              barRegistrationNumberOriginal: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.barRegistrationNumber,
                            },
                          },
                        ],
                      };
                    }
                  })(),
                }),
              },
              caseFilingNumber: caseNumber,
              tenantId: tenantId,
              accessCode: validationCode,
              litigant: {
                additionalDetails: {
                  firstName: name?.givenName,
                  middleName: name?.otherNames,
                  lastName: name?.familyName,
                  fullName: `${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName}`,
                  affidavitText,
                },
                tenantId: tenantId,
                individualId: individualId,
                partyCategory: "INDIVIDUAL",
                partyType: selectedParty?.partyType,
              },
              ...(advocateDetailForm?.advocateBarRegNumberWithName && {
                representative: {
                  tenantId: tenantId,
                  advocateId: advocateDetailForm?.advocateBarRegNumberWithName?.[0]?.advocateId,
                  // id: "9567f3c2-8b49-4936-b849-a81cb83f43c4",
                  representing: [
                    {
                      additionalDetails: {
                        firstName: name?.givenName,
                        middleName: name?.otherNames,
                        lastName: name?.familyName,
                        fullName: `${name?.givenName}${name?.otherNames ? " " + name?.otherNames + " " : " "}${name?.familyName}`,
                        document: newDocument,
                      },
                      tenantId: tenantId,
                      individualId: individualId,
                      partyCategory: "INDIVIDUAL",
                      partyType: selectedParty?.partyType,
                    },
                  ],
                },
              }),
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
    }
  }, [
    adovacteVakalatnama?.adcVakalatnamaFileUpload?.document,
    adovacteVakalatnama?.additionalDetails?.fileName,
    adovacteVakalatnama?.documentType,
    adovacteVakalatnama?.fileStore,
    advocateDetailForm?.additionalDetails?.username,
    advocateDetailForm?.advocateBarRegNumberWithName,
    advocateDetailForm?.barRegistrationNumber,
    advocateDetailForm?.id,
    advocateDetailForm?.vakalatnamaFileUpload?.document,
    advocateId,
    affidavitText,
    caseDetails?.additionalDetails,
    caseDetails?.caseNumber,
    caseDetails?.litigants,
    caseDetails?.representatives,
    caseNumber,
    errors,
    individualDoc,
    individualId,
    isUserLoggedIn,
    name?.familyName,
    name?.givenName,
    name?.otherNames,
    parties,
    replaceAdvocateDocuments?.advocateCourtOrder?.document,
    replaceAdvocateDocuments?.nocFileUpload?.document,
    representingYourself,
    roleOfNewAdvocate,
    searchCaseResult,
    selectedParty,
    step,
    t,
    tenantId,
    userInfo?.uuid,
    userType,
    validationCode,
  ]);

  const handleKeyDown = useCallback(
    (event) => {
      if (event.key === "Enter") {
        if (!isDisabled) onProceed();
      }
    },
    [onProceed, isDisabled]
  );

  useEffect(() => {
    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

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
          actionCancelLabel={((step === 0 && caseDetails?.caseNumber) || step !== 0) && t(JoinHomeLocalisation.JOIN_CASE_BACK_TEXT)}
          actionCancelOnSubmit={() => {
            if (step === 0 && caseDetails?.caseNumber) {
              setCaseDetails({});
            } else if (step === 6) {
              setStep(step - 3);
            } else if (step === 7) {
              if (userType === "Litigant") setStep(step - 5);
              else {
                if (roleOfNewAdvocate === t(JoinHomeLocalisation.PRIMARY_ADVOCATE)) setStep(step - 3);
                else setStep(step - 4);
              }
              setValidationCode("");
              setErrors({
                ...errors,
                validationCode: undefined,
              });
            } else if (step === 3 && userType === "Advocate") {
              setStep(step - 1);
            } else setStep(step - 1);
            setIsDisabled(false);
          }}
          actionSaveLabel={
            step === 2 && roleOfNewAdvocate === "I’m a supporting advocate"
              ? t("GOT_IT_TEXT")
              : // : step === 3
              // ? "E-Sign"
              step === 4
              ? "Done"
              : step === 5
              ? "Make Payment"
              : userType === "Litigant" && step === 0 && caseInfo.length === 0
              ? "Search"
              : t("PROCEED_TEXT")
          }
          actionSaveOnSubmit={onProceed}
          formId="modal-action"
          headerBarMain={<Heading label={step === 4 ? "E-Sign" : step === 5 ? "Payment" : t("JOIN_A_CASE")} />}
          className={`join-a-case-modal ${success && "case-join-success"}`}
          popupModuleActionBarClassName={`${
            step === 2 && userType === "Litigant" && representingYourself !== "Yes" ? "join-case-form-composer" : ""
          }`}
          isDisabled={isDisabled}
        >
          {step >= 0 && modalItem[step]?.modalMain}
          {((step === 2 && userType === "Litigant" && representingYourself !== "Yes") || step === 4 || step === 5) && (
            <Button
              className={"skip-button"}
              label={t(JoinHomeLocalisation.SKIP_LATER)}
              onButtonClick={() => {
                if (userType === "Litigant") {
                  setStep(7);
                  setBarRegNumber("");
                  setAdvocateDetailForm({});
                } else {
                  if (roleOfNewAdvocate) {
                    setStep(6);
                    if (step === 4) {
                      setIsSignedAdvocate(false);
                      setIsSignedParty(false);
                    } else if (step === 5) {
                    }
                  } else {
                    setStep(7);
                  }
                }
              }}
            />
          )}
        </Modal>
      )}
    </div>
  );
};

export default JoinCaseHome;
