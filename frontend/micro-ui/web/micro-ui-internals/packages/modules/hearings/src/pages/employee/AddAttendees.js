import { Button, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import React, { useMemo } from "react";

const AddAttendees = ({ attendees = [], setAddPartyModal, handleModal, formError, form, setForm, setIsDisabled }) => {
  const onClickAddWitness = () => {
    handleModal();
    setAddPartyModal(true);
  };
  const attendeeOptions = Array.isArray(attendees)
    ? attendees.map((attendee) => ({
        value: attendee.individualId,
        label: attendee.name,
      }))
    : [];

  const selectedOfflineAttendees = useMemo(
    () =>
      attendees
        .filter((attendee) => attendee.type === "OFFLINE" && attendee.wasPresent)
        .map((attendee) => {
          return {
            value: attendee.individualId,
            label: attendee.name,
          };
        }),
    [attendees]
  );

  const selectedOnlineAttendees = useMemo(
    () =>
      attendees
        .filter((attendee) => attendee.type === "ONLINE" && attendee.wasPresent)
        .map((attendee) => {
          return {
            value: attendee.individualId,
            label: attendee.name,
          };
        }),
    [attendees]
  );

  const formConfig = [
    {
      label: "Select Online Attendees",
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
        error: "Required",
      },
    },
    {
      label: "Select Offline Attendees",
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
        error: "Required",
      },
    },
  ];

  function applyMultiSelectDropdownFix(setValue, formData, keys) {
    {
      Array.isArray(keys) &&
        keys.forEach((key) => {
          if (formData[key] && Array.isArray(formData[key]) && formData[key].length === 0) {
            setValue(key, undefined);
          }
        });
    }
  }
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
            label={"+ Add New Parties to this Case"}
            onButtonClick={onClickAddWitness}
            variation={"teritiary"}
            style={{ border: "none", marginTop: "10px", color: "#rgba(0, 126, 126, 1)" }}
          ></Button>
        }
        childrenAtTheBottom={true}
      />
    </div>
  );
};

export default AddAttendees;
