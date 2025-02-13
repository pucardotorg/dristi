import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { VerifyMultipartyLitigantConfig } from "../../../configs/VerifyMultipartyLitigantconfig";
import ButtonSelector from "@egovernments/digit-ui-module-dristi/src/components/ButtonSelector";
import { ForwardArrow, BackwardArrow } from "@egovernments/digit-ui-module-dristi/src/icons/svgIndex";

const fieldStyle = { marginRight: 0, width: "100%" };

const LitigantVerification = ({
  t,
  party,
  setParty,
  goBack,
  onProceed,
  alreadyJoinedMobileNumber,
  setAlreadyJoinedMobileNumber,
  isDisabled,
  setIsDisabled,
}) => {
  const modalRef = useRef(null);
  const [index, setIndex] = useState(0);
  const [litigants, setLitigants] = useState(party);

  const modifiedFormConfig = useMemo(() => {
    const applyUiChanges = (config) => ({
      ...config,
      head: litigants?.some((litigant) => litigant?.isComplainant) ? t("COMPLAINANT_BASIC_DETAILS") : t("ACCUSED_BASIC_DETAILS"),
      body: config?.body?.map((body) => {
        let tempBody = {
          ...body,
        };
        if (body?.labelChildren === "optional") {
          tempBody = {
            ...tempBody,
            labelChildren: <span style={{ color: "#77787B" }}>&nbsp;{`${t("CS_IS_OPTIONAL")}`}</span>,
          };
        }
        if (litigants?.[index]?.phoneNumberVerification?.isUserVerified && config?.body?.[3]?.disableConfigFields?.includes(body?.key)) {
          tempBody = {
            ...tempBody,
            disable: true,
          };
        }
        return tempBody;
      }),
    });

    return VerifyMultipartyLitigantConfig?.map((config) => applyUiChanges(config));
  }, [litigants, index, t]);

  useEffect(() => {
    if (
      litigants?.[index]?.phoneNumberVerification?.isUserVerified &&
      !alreadyJoinedMobileNumber?.some((mobileNumber) => litigants?.[index]?.phoneNumberVerification?.mobileNumber === mobileNumber)
    ) {
      setAlreadyJoinedMobileNumber((alreadyJoinedMobileNumber) => [
        ...alreadyJoinedMobileNumber,
        litigants?.[index]?.phoneNumberVerification?.mobileNumber,
      ]);
    }
  }, [litigants, index, alreadyJoinedMobileNumber, setAlreadyJoinedMobileNumber]);

  const areFilesEqual = (existingFile, newFile) => {
    if (!existingFile && !newFile) return true;

    if (!existingFile || !newFile) return false;

    return existingFile.name === newFile.name && existingFile.size === newFile.size && existingFile.lastModified === newFile.lastModified;
  };

  const shouldUpdateState = (selectedParty, formData) => {
    const commonFields = ["firstName", "middleName", "lastName"];

    const hasBasicInfoChanged = commonFields.some((field) => selectedParty[field] !== formData[field]);

    const hasPhoneNumberChanged =
      selectedParty?.phoneNumberVerification?.mobileNumber !== formData?.phoneNumberVerification?.mobileNumber ||
      selectedParty?.phoneNumberVerification?.otpNumber !== formData?.phoneNumberVerification?.otpNumber ||
      selectedParty?.phoneNumberVerification?.isUserVerified !== formData?.phoneNumberVerification?.isUserVerified;

    const hasDocumentChanged =
      !(selectedParty?.vakalatnama === null && formData?.vakalatnama === null) &&
      !areFilesEqual(selectedParty?.vakalatnama?.document?.[0], formData?.vakalatnama?.document?.[0]);
    const isDocumentNull = formData?.vakalatnama === null && selectedParty?.vakalatnama !== null;

    return hasBasicInfoChanged || hasPhoneNumberChanged || hasDocumentChanged || isDocumentNull;
  };

  useEffect(
    () =>
      setIsDisabled(
        !litigants.every((litigant) => litigant?.phoneNumberVerification?.isUserVerified === true && litigant?.vakalatnama?.document?.length > 0)
      ),
    [litigants, setIsDisabled]
  );

  const handleScrollToTop = () => {
    if (modalRef.current) {
      modalRef.current.scrollTop = 0;
    }
  };

  const onFormValueChange = (setValue, formData, formState, reset, setError, clearErrors) => {
    if (formData?.firstName || formData?.middleName || formData?.lastName) {
      const formDataCopy = structuredClone(formData);
      for (const key in formDataCopy) {
        if (["firstName", "middleName", "lastName"].includes(key) && Object.hasOwnProperty.call(formDataCopy, key)) {
          const oldValue = formDataCopy[key];
          let value = oldValue;
          if (typeof value === "string") {
            if (value.length > 100) {
              value = value.slice(0, 100);
            }

            let updatedValue = value
              .replace(/[^a-zA-Z\s]/g, "")
              .trimStart()
              .replace(/ +/g, " ");
            if (updatedValue !== oldValue) {
              const element = document.querySelector(`[name="${key}"]`);
              const start = element?.selectionStart;
              const end = element?.selectionEnd;
              setValue(key, updatedValue);
              setTimeout(() => {
                element?.setSelectionRange(start, end);
              }, 0);
            }
          }
        }
      }
    }
    if (shouldUpdateState(litigants[index], formData)) {
      setLitigants(
        litigants?.map((item, i) => {
          return i === index
            ? {
                ...item,
                ...formData,
              }
            : item;
        })
      );
    }
    if (
      alreadyJoinedMobileNumber?.some((mobileNumber) => formData?.phoneNumberVerification?.mobileNumber === mobileNumber) &&
      !Object.keys(formState?.errors).includes("phoneNumberVerification") &&
      !formData?.phoneNumberVerification?.isUserVerified
    ) {
      setError("phoneNumberVerification", { mobileNumber: "MOBILE_NUMBER_ALREADY_REGISTERED_FOR_THIS_CASE", isDuplicateNumber: true });
    } else if (
      !alreadyJoinedMobileNumber?.some((mobileNumber) => formData?.phoneNumberVerification?.mobileNumber === mobileNumber) &&
      Object.keys(formState?.errors).includes("phoneNumberVerification")
    ) {
      clearErrors("phoneNumberVerification");
    }
    if (formData?.firstName && Object.keys(formState?.errors).includes("firstName")) {
      clearErrors("firstName");
    }
  };

  const handleKeyDown = useCallback(
    (event) => {
      if (event.key === "Enter") {
        onProceed(litigants);
      }
    },
    [onProceed, litigants]
  );

  useEffect(() => {
    window.addEventListener("keydown", handleKeyDown);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

  return (
    <React.Fragment>
      <div ref={modalRef} className="litigant-verification">
        <FormComposerV2
          key={index}
          config={modifiedFormConfig}
          onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors) =>
            onFormValueChange(setValue, formData, formState, reset, setError, clearErrors)
          }
          defaultValues={{
            ...litigants?.[index],
          }}
          fieldStyle={fieldStyle}
          className={"multi-litigant-composer"}
        />
      </div>
      <div className={"multi-litigant-composer-footer"}>
        <div className={"multi-litigant-composer-footer-left"}>
          <ButtonSelector
            ButtonBody={<BackwardArrow />}
            onSubmit={() => {
              setIndex(Math.max(0, index - 1));
              handleScrollToTop();
            }}
            isDisabled={index === 0}
            className={"arrow-button"}
          />
          <ButtonSelector
            ButtonBody={<ForwardArrow />}
            onSubmit={() => {
              setIndex(Math.min(litigants?.length - 1, index + 1));
              handleScrollToTop();
            }}
            isDisabled={index === litigants?.length - 1}
            className={"arrow-button"}
          />
        </div>
        <div className={"multi-litigant-composer-footer-right"}>
          <ButtonSelector theme={"border"} textStyles={{ margin: 0 }} label={t("JOIN_CASE_BACK_TEXT")} onSubmit={goBack} />
          <ButtonSelector
            textStyles={{ margin: 0 }}
            label={t("PROCEED_TEXT")}
            onSubmit={() => {
              setParty(litigants);
              onProceed(litigants);
            }}
            isDisabled={isDisabled}
          />
        </div>
      </div>
    </React.Fragment>
  );
};

export default LitigantVerification;
