import { InfoCard } from "@egovernments/digit-ui-components";
import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import { CardLabel, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { formatDate } from "../../../utils";
import NameListWithModal from "../../../components/NameListWithModal";
import { createShorthand, getFullName, getUserUUID } from "../../../utils/joinCaseUtils";
import { useTranslation } from "react-i18next";

const SearchCaseAndShowDetails = ({
  caseNumber,
  setCaseNumber,
  caseList,
  setCaseList,
  setIsSearchingCase,
  errors,
  caseDetails,
  setCaseDetails,
  onSelect,
}) => {
  const { t } = useTranslation();

  const tenantId = Digit.ULBService.getCurrentTenantId();

  const [complainantList, setComplainantList] = useState([]);
  const [complainantAdvocateList, setComplainantAdvocateList] = useState([]);

  const [respondentList, setRespondentList] = useState([]);
  const [respondentAdvocateList, setRespondentAdvocateList] = useState([]);

  useEffect(() => {
    const getComplainantList = async (formdata) => {
      const complainantList = await Promise.all(
        formdata?.map(async (data, index) => {
          try {
            const response = await getUserUUID(data?.individualId, tenantId);
            const { givenName, otherNames, familyName } = response?.Individual?.[0]?.name || {};

            const fullName = getFullName(" ", givenName, otherNames, familyName);

            return {
              ...data?.data,
              label: `${fullName} ${t("COMPLAINANT_BRACK")}`,
              fullName: fullName,
              partyType: index === 0 ? "complainant.primary" : "complainant.additional",
              isComplainant: true,
              individualId: data?.individualId,
              uuid: response?.Individual?.[0]?.userUuid,
            };
          } catch (error) {
            console.error(error);
          }
        })
      );
      setComplainantList(complainantList);
    };

    const getRespondentList = async (formdata) => {
      const respondentList = await Promise.all(
        formdata?.map(async (data, index) => {
          try {
            let response = undefined;
            let fullName = "";
            if (data?.data?.respondentVerification?.individualDetails?.individualId) {
              response = await getUserUUID(data?.data?.respondentVerification?.individualDetails?.individualId, tenantId);
            }
            if (response) {
              const { givenName, otherNames, familyName } = response?.Individual?.[0]?.name || {};

              fullName = getFullName(" ", givenName, otherNames, familyName);
            } else {
              const { respondentFirstName, respondentMiddleName, respondentLastName } = data?.data || {};
              fullName = getFullName(null, respondentFirstName, respondentMiddleName, respondentLastName);
            }
            return {
              ...data?.data,
              label: `${fullName} ${t("RESPONDENT_BRACK")}`,
              fullName: fullName,
              index: index,
              partyType: index === 0 ? "respondent.primary" : "respondent.additional",
              isRespondent: true,
              individualId: data?.data?.respondentVerification?.individualDetails?.individualId,
              uuid: response?.Individual?.[0]?.userUuid,
            };
          } catch (error) {
            console.error(error);
          }
        })
      );
      setRespondentList(respondentList?.map((data) => data));
    };

    if (caseDetails?.cnrNumber) {
      getComplainantList(
        caseDetails?.litigants
          ?.filter((litigant) => litigant?.partyType?.includes("complainant"))
          ?.map((litigant) => ({ individualId: litigant?.individualId }))
      );
      getRespondentList(caseDetails?.additionalDetails?.respondentDetails?.formdata);
      setComplainantAdvocateList(
        caseDetails?.representatives
          ?.filter((represent) => represent?.representing?.[0]?.partyType?.includes("complainant"))
          ?.map((represent) => represent?.additionalDetails?.advocateName)
      );
      setRespondentAdvocateList(
        caseDetails?.representatives
          ?.filter((represent) => represent?.representing?.[0]?.partyType?.includes("respondent"))
          ?.map((represent) => represent?.additionalDetails?.advocateName)
      );
    }
  }, [caseDetails, t, tenantId]);

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
                    <NameListWithModal t={t} data={complainantAdvocateList} type={"COMPLAINTS_ADVOCATES"} />
                  </div>
                  <div style={{ width: "50%", paddingLeft: "16px", borderLeft: "1px solid rgba(0, 0, 0, 0.10196)" }}>
                    <h2 className="case-info-title">{t("ACCUSEDS_ADVOCATES")}</h2>
                    <NameListWithModal t={t} data={respondentAdvocateList} type={"ACCUSEDS_ADVOCATES"} />
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
