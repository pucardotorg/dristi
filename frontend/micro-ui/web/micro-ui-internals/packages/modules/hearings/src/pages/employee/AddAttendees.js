import React, { useEffect, useMemo, useState } from 'react';
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";
import useGetHearings from '../../../../dristi/src/hooks/dristi/useGetHearings';


const AddAttendees = () => {

    const tenantId = "pg";
    const { data: hearingResponse, refetch: refetch } = useGetHearings(
      { hearing: { tenantId }, tenantId },
      { applicationNumber: "", cnrNumber: "", tenantId },
      "dristi",
      true
    );
    const hearingDetails = useMemo(() => hearingResponse?.HearingList || [], [hearingResponse]);
    useEffect(() => {
        const fetchData = async () => {
          try {
            await refetch();
          } catch (error) {
            console.error("Error refetching data:", error);
          }
          console.log(hearingDetails);
        };

        fetchData();
      }, [ refetch]);
  const [attendeesConfig, setAttendeesConfig] = useState({
    "title": "Attendees Checklist",
    "status": "ACTIVE",
    "questions": [
      {
        "uuid": "onlineAttendees",
        "questionStatement": "online",
        "options": ["Attendee 1", "Attendee 2", "Attendee 3","Attendee 4"],
        "required": true,
      },
      {
        "uuid": "offlineAttendees",
        "questionStatement": "offline",
        "options": ["Attendee A", "Attendee B", "Attendee C","Attendee 3"],
        "required": true,
      }
    ]
  });

  const onFormSubmit = (data) => {
    // Handle form submission logic here
    const formattedData = {};
    for (const key in data) {
      const question = attendeesConfig.questions.find(q => q.uuid === key);
      if (question) {
        formattedData[question.questionStatement] = data[key];
      }
    }
    console.log("Form Data:", formattedData);
  };

  const formConfig = attendeesConfig.questions.map((question) => (
    {
    isMandatory: question.required,
    key: question.uuid,
    type: "multiselectdropdown",
    inline: false,
    disable: false,
    label: `Select ${question.questionStatement} Attendees` ,
    populators: {
      name: question.uuid,
      optionsKey: "label",
      defaultText: 'select attendees',
      selectedText: 'attendees',
      options: question.options.map(option => ({ value: option, label: option })),
      required: question.required,
      error: "Required"
    }
  }));

  return (
    <FormComposerV2
      inline={false}
      config={[{
        head: attendeesConfig.title,
        body: formConfig
      }]}
      onSubmit={onFormSubmit}
      label="Submit"
    ></FormComposerV2>
  );
};

export default AddAttendees;
