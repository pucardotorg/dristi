import { Button, Header, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import React, { useState, useEffect, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { searchconfig } from "../../configs/ViewHearingConfig";
import { useHistory } from "react-router-dom";
import { downloadHearingsAsPDF } from "../../hooks/services/downloadHearingPDF";
import RescheduleHearing from "./ReSchedulHearing";
import { Context } from "../../components/HearingOverlayDropdown";
import { update } from "lodash";

const defaultSearchValues = {
  individualName: "",
  mobileNumber: "",
  IndividualID: "",
};

const ViewHearing = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const [defaultValues, setDefaultValues] = useState(defaultSearchValues); // State to hold default values for search fields
  const indConfigs = useMemo(searchconfig, []);

  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [rescheduleAll, setRescheduleAll] = useState(false);

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || ''; // Adjust as per your context path logic
    history.push(`/${contextPath}${path}`);
  };

  const dropdownItems = [
    { label: "View Case", path: "/employee/hearings/view-case" },
    { label: "Reschedule hearing", path: "/employee/hearings/reschedule-hearing" },
    { label: "View transcript", path: "/employee/hearings/view-transcript" },
    { label: "View witness deposition", path: "/employee/hearings/view-witness-deposition" },
    { label: "View pending task", path: "/employee/hearings/view-pending-task" },
  ];

  const rescheduleAllHearings = () => {
    setShowModal(true);
    setRescheduleAll(true);
  };
  const handleOnCancel = () => {
    setShowModal(false);
  };

  const downloadHearing = () => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const epochFromDateTime = today.getTime();
    today.setHours(23, 59, 59, 999);
    const epochToDateTime = today.getTime();
    // TODO: Prepare query list
    let params = {
      tenantId: Digit.ULBService.getCurrentTenantId(),
      judgeId: "87f0c966-e42c-490d-8edb-bfwe9a9da5b60", // TODO: Remove hardcoded judgeId
      fromDate: epochFromDateTime,
      toDate: epochToDateTime,
    };
    downloadHearingsAsPDF({ t, params });
  };

  useEffect(() => {
    // Set default values when component mounts
    setDefaultValues(defaultSearchValues);
  }, []);

  const updatedConfig = useMemo(() => {
    const actionsCol = indConfigs.sections.searchResult.uiConfig.columns.find((c) => c.columnId === "actions");
    console.log(actionsCol);
    actionsCol.onViewCaseClick = () => {
      const viewCasePath =dropdownItems.find(item=> item.label==="View Case").path;
      handleNavigate?.(viewCasePath)
    };
    actionsCol.onRescheduleClick = () => {
      setShowModal(true);
      setRescheduleAll(false);
    };
    actionsCol.onViewTranscriptClick = () => {
      const viewTranscriptPath =dropdownItems.find(item=> item.label==="View transcript").path;
      handleNavigate?.(viewTranscriptPath)
    };
    actionsCol.onViewWitnessClick = () => {
      const viewWitnessPath =dropdownItems.find(item=> item.label==="View witness deposition").path;
      handleNavigate?.(viewWitnessPath)
    };
    actionsCol.onViewPendingTaskClick = () => {
      const viewPendingTaskPath =dropdownItems.find(item=> item.label==="View pending task").path;
      handleNavigate?.(viewPendingTaskPath)
    };
  }, [indConfigs]);

  return (
    // <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
    <React.Fragment>
      <Header>{t(indConfigs?.label)}</Header>
      <div className="inbox-search-wrapper">
        {/* Pass defaultValues as props to InboxSearchComposer */}
        <InboxSearchComposer configs={indConfigs} defaultValues={defaultValues}></InboxSearchComposer>
      </div>
      <div>
        <Button onButtonClick={rescheduleAllHearings} label="Reschedule All Hearings"></Button>
      </div>
      <div>
        {showModal && <RescheduleHearing onCancel={handleOnCancel} onDismiss={handleOnCancel} rescheduleAll={rescheduleAll}></RescheduleHearing>}
      </div>
      // TODO: Create a button and call downloadHearing()
    </React.Fragment>
    // </div>
  );
};
export default ViewHearing;