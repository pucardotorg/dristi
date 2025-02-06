import { InfoCard } from "@egovernments/digit-ui-components";
import CustomCaseInfoDiv from "@egovernments/digit-ui-module-dristi/src/components/CustomCaseInfoDiv";
import { CardLabel, Dropdown, FormComposerV2, LabelFieldPair, MultiSelectDropdown, RadioButtons } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useRef } from "react";
import isEqual from "lodash/isEqual";
import { useTranslation } from "react-i18next";

const SelectParty = ({
  selectPartyData,
  setSelectPartyData,
  caseDetails,
  setSelectedParty,
  setRoleOfNewAdvocate,
  parties,
  party,
  setParty,
  selectedParty,
  searchLitigantInRepresentives,
  advocateId,
  searchAdvocateInRepresentives,
  partyInPerson,
  setPartyInPerson,
  isLitigantJoined,
  isAdvocateJoined,
}) => {
  const { t } = useTranslation();

  const targetRef = useRef(null);

  const advocateVakalatnamaConfig = useMemo(
    () => [
      {
        body: [
          {
            type: "component",
            component: "SelectCustomDragDrop",
            key: "affidavitData",
            isMandatory: true,
            withoutLabel: true,
            populators: {
              inputs: [
                {
                  name: "document",
                  documentHeader: selectPartyData?.userType?.value === "Litigant" ? "AFFIDAVIT" : "NOC_JUDGE_ORDER",
                  type: "DragDropComponent",
                  uploadGuidelines: "UPLOAD_DOC_50",
                  maxFileSize: 50,
                  maxFileErrorMessage: "CS_FILE_LIMIT_50_MB",
                  fileTypes: ["JPG", "PDF", "PNG", "JPEG"],
                  isMultipleUpload: false,
                  documentHeaderStyle: {
                    margin: "0px",
                  },
                },
              ],
            },
          },
        ],
      },
    ],
    [selectPartyData?.userType]
  );

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
      ];
    }
    return [];
  }, [caseDetails]);

  const scrollToDiv = () => {
    if (targetRef.current) {
      targetRef.current.scrollTop = targetRef.current.scrollHeight;
    }
  };

  useEffect(() => {
    scrollToDiv();
  }, [selectPartyData?.partyInvolve, party, partyInPerson]);

  return (
    <div ref={targetRef} className="select-user-join-case" style={{ width: "712px" }}>
      <CustomCaseInfoDiv t={t} data={caseInfo?.slice(0, 4)} column={4} />
      {selectPartyData?.userType?.value === "Litigant" && (
        <InfoCard
          variant={"default"}
          label={t("PLEASE_NOTE")}
          additionalElements={[<p>{t("ACKNOWLEDGE_RECEIPT_OF_SUMMONS")}</p>]}
          inline
          textStyle={{}}
          className={`custom-info-card`}
        />
      )}
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("JOINING_THIS_CASE_AS")}`}</CardLabel>
        <RadioButtons
          selectedOption={selectPartyData?.userType}
          disabled={true}
          optionsKey={"label"}
          options={[
            { label: t("ADVOCATE_OPT"), value: "Advocate" },
            { label: t("LITIGANT_OPT"), value: "Litigant" },
          ]}
        />
      </LabelFieldPair>
      <LabelFieldPair className="case-label-field-pair">
        <CardLabel className="case-input-label">{`${t("WHICH_PARTY_ARE_YOU")}`}</CardLabel>
        <RadioButtons
          selectedOption={selectPartyData?.partyInvolve}
          onSelect={(value) => {
            setSelectPartyData((selectPartyData) => ({
              ...selectPartyData,
              partyInvolve: value,
              isReplaceAdvocate: {},
              affidavit: {},
            }));
            setSelectedParty({});
            setRoleOfNewAdvocate("");
            setParty(selectPartyData?.userType?.value === "Litigant" ? {} : []);
          }}
          optionsKey={"label"}
          options={[
            { label: t("COMPLAINANTS_TEXT"), value: "COMPLAINANTS" },
            { label: t("RESPONDENTS_TEXT"), value: "RESPONDENTS" },
          ]}
          disabled={isAdvocateJoined}
        />
      </LabelFieldPair>
      {selectPartyData?.userType?.value === "Advocate" && selectPartyData?.partyInvolve?.value && (
        <LabelFieldPair className="case-label-field-pair">
          <CardLabel className="case-input-label">{`${t("ARE_YOU_REPLACING_ADVOCATE")}`}</CardLabel>
          <RadioButtons
            selectedOption={selectPartyData?.isReplaceAdvocate}
            onSelect={(value) => {
              setSelectPartyData((selectPartyData) => ({
                ...selectPartyData,
                isReplaceAdvocate: value,
                affidavit: {},
              }));
              setParty([]);
            }}
            optionsKey={"label"}
            options={[
              { label: t("YES"), value: "YES" },
              { label: t("NO"), value: "NO" },
            ]}
          />
        </LabelFieldPair>
      )}
      {((selectPartyData?.userType?.value === "Litigant" && selectPartyData?.partyInvolve?.value) || selectPartyData?.isReplaceAdvocate?.value) && (
        <LabelFieldPair className="case-label-field-pair">
          <CardLabel className="case-input-label">{`${t(
            selectPartyData?.userType?.value === "Litigant" ? "WHICH_LITIGANT" : "WHICH_LITIGANTS_REPRESENTING"
          )}`}</CardLabel>
          {selectPartyData?.userType?.value === "Litigant" ? (
            <Dropdown
              t={t}
              option={parties?.filter((filterParty) =>
                selectPartyData?.partyInvolve?.value === "COMPLAINANTS"
                  ? filterParty?.partyType?.includes("complainant")
                  : filterParty?.partyType?.includes("respondent")
              )}
              selected={party}
              optionKey={"fullName"}
              select={(e) => {
                setParty(e);
                setPartyInPerson({});
              }}
              freeze={true}
              topbarOptionsClassName={"top-bar-option"}
              disable={isLitigantJoined}
            />
          ) : (
            selectPartyData?.isReplaceAdvocate?.value && (
              <MultiSelectDropdown
                options={parties?.filter((filterParty) =>
                  selectPartyData?.partyInvolve?.value === "COMPLAINANTS"
                    ? filterParty?.partyType?.includes("complainant")
                    : filterParty?.partyType?.includes("respondent")
                )}
                selected={party}
                optionsKey={"fullName"}
                onSelect={(value) => {
                  setParty(value?.map((val) => val[1]));
                }}
                defaultUnit={"Others"}

                // placeholder={"lkjdlfjsdlkfj + 2 Others"}
                // selected={[]}
              />
            )
          )}
        </LabelFieldPair>
      )}
      {selectPartyData?.userType?.value === "Litigant" && party?.label && (
        <LabelFieldPair className="case-label-field-pair">
          <CardLabel className="case-input-label">{`${t("ARE_YOU_JOINING_AS_PARTY_IN_PERSON")}`}</CardLabel>
          <RadioButtons
            selectedOption={partyInPerson}
            onSelect={(value) => {
              setPartyInPerson(value);
              if (value?.value === "NO") setSelectPartyData((selectPartyData) => ({ ...selectPartyData, affidavit: {} }));
            }}
            optionsKey={"label"}
            options={[
              { label: t("YES"), value: "YES" },
              { label: t("NO"), value: "NO" },
            ]}
          />
        </LabelFieldPair>
      )}
      {partyInPerson?.value === "YES" && (
        <React.Fragment>
          <InfoCard
            variant={"default"}
            label={t("PLEASE_NOTE")}
            additionalElements={[
              <p>
                {t("AFFIDAVIT_STATING_PARTY_IN_PERSON")} <span style={{ fontWeight: "bold" }}>{`(${t("PARTY_IN_PERSON_TEXT")})`}</span>{" "}
              </p>,
            ]}
            inline
            textStyle={{}}
            className={`custom-info-card`}
          />
        </React.Fragment>
      )}
      {(partyInPerson?.value === "YES" || selectPartyData?.isReplaceAdvocate?.value === "YES") && (
        <FormComposerV2
          key={2}
          config={advocateVakalatnamaConfig}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
            if (!isEqual(formData, selectPartyData?.affidavit)) {
              setSelectPartyData((selectPartyData) => ({
                ...selectPartyData,
                affidavit: formData,
              }));
            }
          }}
          defaultValues={selectPartyData?.affidavit}
          className={`party-in-person-affidavit-upload`}
          noBreakLine
        />
      )}

      {selectedParty?.label &&
        (() => {
          const { isFound } = searchLitigantInRepresentives(caseDetails);
          const { isFound: advIsFound, partyType } = searchAdvocateInRepresentives(advocateId);
          if (
            (isFound && advIsFound && !selectedParty?.partyType?.includes(partyType)) ||
            (!isFound && advIsFound && !selectedParty?.partyType?.includes(partyType))
          )
            return true;
          else return false;
        })() &&
        selectPartyData?.userType?.value === "Advocate" && (
          <React.Fragment>
            <hr className="horizontal-line" />
            <InfoCard
              variant={"warning"}
              label={t("WARNING")}
              additionalElements={[
                <p>
                  {t("ALREADY_REPRESENTING")} {selectedParty?.isComplainant ? "respondent" : "complainant"}
                  {t("CANT_REPRESENT_BOTH_PARTY")}
                </p>,
              ]}
              inline
              textStyle={{}}
              className={`custom-info-card warning`}
            />
          </React.Fragment>
        )}
      {selectedParty?.label && selectPartyData?.userType?.value === "Litigant" && selectedParty?.individualId && (
        <React.Fragment>
          <hr className="horizontal-line" />
          <InfoCard
            variant={"warning"}
            label={t("WARNING")}
            additionalElements={[
              <p>
                {t("ABOVE_SELECTED_PARTY")} <span style={{ fontWeight: "bold" }}>{`${selectedParty?.label}`}</span> {t("ALREADY_JOINED_CASE")}
              </p>,
            ]}
            inline
            textStyle={{}}
            className={`custom-info-card warning`}
          />
        </React.Fragment>
      )}
    </div>
  );
};

export default SelectParty;
