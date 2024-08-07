import { Button, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";
import { useTranslation } from "react-i18next";

function applyMultiSelectDropdownFix(setValue, formData, keys) {
  Array.isArray(keys) &&
    keys.forEach((key) => {
      if (formData[key] && Array.isArray(formData[key]) && formData[key].length === 0) {
        setValue(key, undefined);
      }
    });
}

const AddAttendees = ({
  attendees = [],
  setAttendees,
  handleAttendees,
  hearingData,
  setAddPartyModal,
  handleModal,
  form,
  formError,
  setForm,
  setIsDisabled,
}) => {
  const { t } = useTranslation();
  const onClickAddWitness = () => {
    handleModal();
    setAddPartyModal(true);
  };

  const attendeeOptions = Array.isArray(attendees)
    ? attendees.map((attendee) => ({
        value: attendee.individualId || attendee.name,
        label: attendee.name,
      }))
    : [];

  const selectedOfflineAttendees = useMemo(
    () =>
      attendees
        .filter((attendee) => !attendee.isOnline && attendee.wasPresent)
        .map((attendee) => {
          return {
            value: attendee.individualId || attendee.name,
            label: attendee.name,
          };
        }),
    [attendees]
  );

  const selectedOnlineAttendees = useMemo(
    () =>
      attendees
        .filter((attendee) => attendee.isOnline && attendee.wasPresent)
        .map((attendee) => {
          return {
            value: attendee.individualId || attendee.name,
            label: attendee.name,
          };
        }),
    [attendees]
  );

  const formConfig = [
    {
      label: "SELECT_ONLINE_PARTIES",
      isMandatory: true,
      key: "onlineAttendees",
      type: "dropdown",
      populators: {
        name: "onlineAttendees",
        allowMultiSelect: true,
        optionsKey: "label",
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        selectedText: "Attendee(s)",
        defaultText: "select attendees",
        options: attendeeOptions,
      },
    },
    {
      label: "SELECT_OFFLINE_PARTIES",
      isMandatory: true,
      key: "offlineAttendees",
      type: "dropdown",
      populators: {
        name: "offlineAttendees",
        allowMultiSelect: true,
        optionsKey: "label",
        error: "CORE_REQUIRED_FIELD_ERROR",
        required: true,
        isMandatory: true,
        selectedText: "Attendee(s)",
        defaultText: "select attendees",
        options: attendeeOptions,
      },
    },
  ];

  const multiSelectDropdownKeys = () => {
    const foundKeys = [];
    formConfig.forEach((field) => {
      if (field.type === "dropdown" && field.populators.allowMultiSelect) {
        foundKeys.push(field.key);
      }
    });
    return foundKeys;
  };

  const onFormChange = (setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
    applyMultiSelectDropdownFix(setValue, formData, multiSelectDropdownKeys);

    if (formData.offlineAttendees.length === 0 && formData.onlineAttendees.length === 0) {
      setIsDisabled(true);
    } else {
      setIsDisabled(false);
    }
    if (JSON.stringify(formData) !== JSON.stringify(form)) {
      setForm(formData);
    }
  };

  return (
    <div>
      {formError && <p style={{ color: "red" }}>{formError}</p>}
      <FormComposerV2
        inline={false}
        config={[
          {
            body: formConfig,
          },
        ]}
        defaultValues={{
          offlineAttendees: selectedOfflineAttendees,
          onlineAttendees: selectedOnlineAttendees,
        }}
        onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
          onFormChange(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues);
        }}
        children={
          <Button
            label={t("ADD_NEW_PARTIES_TO_CASE")}
            onButtonClick={onClickAddWitness}
            variation={"secondary"}
            style={{ border: "none", boxShadow: "none", backgroundColor: "#fff", padding: "10px", minWidth: "166px" }}
            textStyles={{
              fontFamily: "Roboto",
              fontSize: "16px",
              fontWeight: 700,
              lineHeight: "18.75px",
              textAlign: "center",
              color: "#007E7E",
            }}
          ></Button>
        }
        childrenAtTheBottom={true}
      />
    </div>
  );
};

export default AddAttendees;
