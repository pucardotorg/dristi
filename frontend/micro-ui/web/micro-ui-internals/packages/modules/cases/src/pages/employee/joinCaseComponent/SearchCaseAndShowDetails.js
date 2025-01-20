import { InfoCard } from "@egovernments/digit-ui-components";
import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import { CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { formatDate } from "../../../utils";
import NameListWithModal from "../../../components/NameListWithModal";

const createShorthand = (fullname) => {
  const words = fullname?.split(" ");
  const firstChars = words?.map((word) => word?.charAt(0));
  const shorthand = firstChars?.join("");
  return shorthand;
};

const SearchCaseAndShowDetails = ({
  t,
  caseNumber,
  setCaseNumber,
  caseList,
  setCaseList,
  setIsSearchingCase,
  errors,
  caseDetails,
  setCaseDetails,
  onSelect,
  complainantList,
  respondentList,
}) => {
  const caseInfo = useMemo(() => {
    if (caseDetails?.caseCategory) {
      return [
        {
          key: "CS_CASE_NAME",
          value: caseDetails?.caseTitle,
        },
        {
          key: "CS_CASE_ID",
          value: caseDetails?.cnrNumber,
        },
        {
          key: "CS_FILING_NUMBER",
          value: caseDetails?.filingNumber,
        },
        {
          key: "CASE_NUMBER",
          value: caseDetails?.cmpNumber,
        },
        {
          key: "CASE_CATEGORY",
          value: caseDetails?.caseCategory,
        },
        {
          key: "CASE_TYPE",
          value: `${createShorthand(caseDetails?.statutesAndSections?.[0]?.sections?.[0])} S${
            caseDetails?.statutesAndSections?.[0]?.subsections?.[0]
          }`,
        },
        {
          key: "CS_FILING_DATE",
          value: formatDate(new Date(caseDetails?.filingDate)),
        },
      ];
    }
    return [];
  }, [caseDetails]);

  return (
    <div className="case-number-input">
      {!caseDetails?.cnrNumber && (
        <LabelFieldPair className="case-label-field-pair">
          <CardLabel className="case-input-label">{`${t("ENTER_CASE_NUMBER")}`}</CardLabel>
          <div style={{ width: "100%", display: "flex" }}>
            <TextInput
              type={"text"}
              name="caseNumber"
              value={caseNumber}
              onChange={(e) => {
                setCaseDetails({});
                setCaseList([]);
                let str = e.target.value;
                if (str) {
                  str = str.replace(/[^a-zA-Z0-9.-]/g, "");
                  if (str.length > 50) {
                    str = str.substring(0, 50);
                  }
                  setIsSearchingCase(true);
                  setCaseNumber(str);
                } else {
                  setCaseNumber("");
                }
              }}
              textInputStyle={{ width: "712px" }}
              style={{ borderRight: "1px solid #505a5f" }}
              autoFocus={true}
            />
          </div>
          {caseList &&
            caseList?.map((option, index) => {
              return (
                <div
                  className={`cp profile-dropdown--item display: flex `}
                  key={index}
                  onClick={() => {
                    onSelect(option);
                  }}
                >
                  <span> {option?.filingNumber}</span>
                </div>
              );
            })}
          <p style={{ fontSize: "12px" }}>
            {t("FILLING_NUMBER_FORMATE_TEXT")} {"KL-<6 digit sequence number>-<YYYY>"}
          </p>
        </LabelFieldPair>
      )}
      {errors?.caseNumber && (
        <InfoCard
          variant={"default"}
          label={t("INVALID_CASE_FILING_NUMBER")}
          additionalElements={
            !errors?.caseNumber?.type && [
              <p>
                {t("INVALID_CASE_INFO_TEXT")} <span style={{ fontWeight: "bold" }}>{t("NYAYA_MITRA_TEXT")}</span> {t("FOR_SUPPORT_TEXT")}
              </p>,
            ]
          }
          text={errors?.caseNumber?.type === "not-admitted" && t(errors?.caseNumber?.message)}
          inline
          textStyle={{
            width: "500px",
          }}
          className={`custom-info-card error`}
        />
      )}
      {caseDetails?.cnrNumber && (
        <React.Fragment>
          <CustomCaseInfoDiv
            t={t}
            data={caseInfo}
            column={4}
            children={
              <div>
                <div className="complainants-respondents" style={{ display: "flex", flexWrap: "wrap", gap: "0px" }}>
                  <div
                    style={{
                      flex: "0 0 50%",
                      boxSizing: "border-box",
                    }}
                  >
                    <h2 className="case-info-title">{t("COMPLAINANTS_TEXT")}</h2>
                    <NameListWithModal t={t} data={complainantList?.map((complainant) => complainant?.fullName)} type={"COMPLAINANTS_TEXT"} />
                  </div>
                  <div
                    style={{
                      flex: "0 0 50%",
                      boxSizing: "border-box",
                      borderLeft: "1px solid rgba(0, 0, 0, 0.10196)",
                      paddingLeft: "16px",
                    }}
                  >
                    <h2 className="case-info-title">{t("RESPONDENTS_TEXT")}</h2>
                    <NameListWithModal t={t} data={respondentList?.map((respondent) => respondent?.fullName)} type={"RESPONDENTS_TEXT"} />
                  </div>
                </div>
                <div className="complainants-respondents">
                  <div style={{ width: "50%" }}>
                    <h2 className="case-info-title">{t("COMPLAINTS_ADVOCATES")}</h2>
                    <NameListWithModal t={t} data={complainantList?.map((complainant) => complainant?.fullName)} type={"COMPLAINTS_ADVOCATES"} />
                  </div>
                  <div style={{ width: "50%", paddingLeft: "16px", borderLeft: "1px solid rgba(0, 0, 0, 0.10196)" }}>
                    <h2 className="case-info-title">{t("ACCUSEDS_ADVOCATES")}</h2>
                    <NameListWithModal t={t} data={[]} type={"ACCUSEDS_ADVOCATES"} />
                  </div>
                </div>
              </div>
            }
          />
        </React.Fragment>
      )}
    </div>
  );
};

export default SearchCaseAndShowDetails;
