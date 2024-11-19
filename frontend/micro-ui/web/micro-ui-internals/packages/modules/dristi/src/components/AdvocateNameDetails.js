import { CardLabel, FormComposerV2, LabelFieldPair, TextInput } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { idProofVerificationConfig } from "../configs/component";
import { userTypeOptions } from "../pages/citizen/registration/config";

function splitNamesPartiallyFromFullName(fullName) {
  const nameParts = fullName?.trim()?.split(/\s+/);

  let firstName = "";
  let middleName = "";
  let lastName = "";

  const numParts = nameParts?.length;

  if (numParts === 1) {
    firstName = nameParts?.[0];
  } else if (numParts === 2) {
    firstName = nameParts?.[0];
    lastName = nameParts?.[1];
  } else if (numParts >= 3) {
    firstName = nameParts?.[0];
    lastName = nameParts?.[numParts - 1];
    middleName = nameParts?.slice(1, numParts - 1)?.join(" ");
  }

  return {
    firstName: firstName,
    middleName: middleName,
    lastName: lastName ? lastName : "",
  };
}

function AdvocateNameDetails({ t, config, onSelect, formData = {}, errors, register }) {
  const [advocateName, setAdvocateName] = useState({ firstName: "", middleName: "", lastName: "" });
  const [isApproved, setIsApproved] = useState(false);

  const token = window.localStorage.getItem("token");
  const isUserLoggedIn = Boolean(token);
  const moduleCode = "DRISTI";
  const userInfo = JSON.parse(window.localStorage.getItem("user-info"));
  const tenantId = window.localStorage.getItem("tenant-id");
  const { data, isLoading, refetch } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        userUuid: [userInfo?.uuid],
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    "HOME",
    userInfo?.uuid && isUserLoggedIn
  );

  const individualId = useMemo(() => data?.Individual?.[0]?.individualId, [data?.Individual]);
  const userType = useMemo(() => data?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "userType")?.value, [data?.Individual]);

  const { data: searchData, isLoading: isSearchLoading } = window?.Digit.Hooks.dristi.useGetAdvocateClerk(
    {
      criteria: [{ individualId }],
      tenantId,
    },
    { tenantId },
    moduleCode,
    Boolean(isUserLoggedIn && individualId && userType === "ADVOCATE"),
    "/advocate/v1/_search"
  );

  const userTypeDetail = useMemo(() => {
    return userTypeOptions.find((item) => item?.code === userType) || {};
  }, [userType]);

  const searchResult = useMemo(() => {
    return userType === "ADVOCATE" && searchData?.[`${userTypeDetail?.apiDetails?.requestKey}s`]?.[0]?.responseList;
  }, [searchData, userTypeDetail?.apiDetails?.requestKey, userType]);

  const isApprovalPending = useMemo(() => {
    return (
      userType === "ADVOCATE" &&
      Array.isArray(searchResult) &&
      searchResult?.length > 0 &&
      searchResult?.[0]?.isActive === false &&
      searchResult?.[0]?.status !== "INACTIVE"
    );
  }, [searchResult, userType]);

  useEffect(() => {
    const isPending = isApprovalPending;
    setIsApproved(!isPending);
  }, []);

  const selectedAdvindividualId = useMemo(() => formData?.advocateBarRegNumberWithName?.[0]?.individualId || null, [
    formData?.advocateBarRegNumberWithName,
  ]);

  const { data: selectedIndividual, isLoading: isInidividualLoading } = window?.Digit.Hooks.dristi.useGetIndividualUser(
    {
      Individual: {
        individualId: selectedAdvindividualId,
      },
    },
    { tenantId, limit: 1000, offset: 0 },
    moduleCode,
    `getindividual-${selectedAdvindividualId}`,
    selectedAdvindividualId
  );

  const idType = selectedIndividual?.Individual?.[0]?.identifiers[0]?.identifierType || "";
  const identifierIdDetails = JSON.parse(
    selectedIndividual?.Individual?.[0]?.additionalFields?.fields?.find((obj) => obj.key === "identifierIdDetails")?.value || "{}"
  );

  useEffect(() => {
    if (formData.advocateBarRegNumberWithName) {
      const fullName = formData?.advocateBarRegNumberWithName?.[0]?.advocateName;
      let advName = "";
      if (!fullName) {
        advName = splitNamesPartiallyFromFullName("");
      } else {
        advName = splitNamesPartiallyFromFullName(fullName);
      }
      setAdvocateName(advName);
    }
  }, [formData]);

  useEffect(() => {
    if (isApproved && searchResult) {
      const barRegNum = searchResult[0]?.barRegistrationNumber;
      const userName = searchResult[0]?.additionalDetails?.username;
      const advocateId = searchResult[0]?.id;
      const advocateUuid = searchResult[0]?.auditDetails?.createdBy;
      const individualId = searchResult[0]?.individualId;
      onSelect("advocateBarRegNumberWithName", [
        {
          barRegistrationNumber: `${barRegNum} (${userName})`,
          advocateName: userName,
          isDisable: true,
          barRegistrationNumberOriginal: barRegNum,
          advocateId,
          advocateUuid,
          individualId,
        },
      ]);
      onSelect("AdvocateNameDetails", {
        firstName: splitNamesPartiallyFromFullName(userName)?.firstName,
        middleName: splitNamesPartiallyFromFullName(userName)?.middleName,
        lastName: splitNamesPartiallyFromFullName(userName)?.lastName,
        advocateMobileNumber: selectedIndividual?.Individual?.[0]?.mobileNumber || '',
        advocateIdProof: identifierIdDetails?.fileStoreId
          ? [
              {
                name: idType,
                fileStore: identifierIdDetails?.fileStoreId,
                documentName: identifierIdDetails?.filename,
                fileName: "ID Proof",
              },
            ]
          : null,
      });
    }
    if (isApproved && !searchResult && selectedIndividual) {
      onSelect("AdvocateNameDetails", {
        ...formData?.AdvocateNameDetails,
        advocateMobileNumber: selectedIndividual?.Individual?.[0]?.mobileNumber || '',
        advocateIdProof: identifierIdDetails?.fileStoreId
          ? [
              {
                name: idType,
                fileStore: identifierIdDetails?.fileStoreId,
                documentName: identifierIdDetails?.filename,
                fileName: "ID Proof",
              },
            ]
          : null,
      });
    }
  }, [isApproved, onSelect, searchResult, selectedIndividual]);

  const inputs = useMemo(
    () =>
      config?.populators?.inputs || [
        {
          label: "FIRST_NAME",
          type: "text",
          name: "firstName",
        },
        {
          label: "MIDDLE_NAME",
          type: "text",
          name: "middleName",
        },
        {
          label: "LAST_NAME",
          type: "text",
          name: "lastName",
        },
      ],
    [config?.populators?.inputs]
  );

  return (
    <div className={"advocate-basic-info"} style={config?.componentStyle}>
      {formData?.advocateBarRegNumberWithName && (
        <React.Fragment>
          {inputs?.map((input, index) => {
            let currentValue = advocateName?.[input.name] || "";
            return (
              <React.Fragment key={index}>
                <CardLabel>{t(input?.label)}</CardLabel>
                <LabelFieldPair>
                  <div className={`field ${input?.inputFieldClassName}`}>
                    {
                      <React.Fragment>
                        <TextInput className="field desktop-w-full" name={input?.name} disable={input?.isDisabled} value={currentValue} />
                      </React.Fragment>
                    }
                  </div>
                </LabelFieldPair>
              </React.Fragment>
            );
          })}
        </React.Fragment>
      )}
      {!formData?.advocateBarRegNumberWithName && <div></div>}
    </div>
  );
}

export default AdvocateNameDetails;
