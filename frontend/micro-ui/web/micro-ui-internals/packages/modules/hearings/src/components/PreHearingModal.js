import React, { useState } from "react";
import Modal from "../../../dristi/src/components/Modal";
import { CloseSvg, InboxSearchComposer } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";

const sectionsParentStyle = {
  height: "50%",
  display: "flex",
  flexDirection: "column",
  gridTemplateColumns: "20% 1fr",
  gap: "1rem",
};

const configPreHearing = {
  label: "ES_COMMON_INBOX",
  type: "inbox",
  // customHookName: "dristi.useInboxCustomHook",
  apiDetails: {
    serviceName: "/inbox/v2/_search",
    requestParam: {},
    requestBody: {
      inbox: {
        processSearchCriteria: {
          businessService: ["advocate"],
          moduleName: "Advocate services",
          tenantId: "pg",
        },
        moduleSearchCriteria: {
          tenantId: "pg",
        },
        tenantId: "pg",
        limit: 10,
        offset: 0,
      },
    },
    minParametersForSearchForm: 1,
    masterName: "commonUiConfig",
    moduleName: "registrationRequestsConfig",
    searchFormJsonPath: "requestBody.inbox.moduleSearchCriteria",
    tableFormJsonPath: "requestBody.inbox",
  },
  sections: {
    // search: {
    //   uiConfig: {
    //     // headerStyle: null,
    //     // type: "registration-requests-table-search",
    //     primaryLabel: "ES_COMMON_SEARCH",
    //     secondaryLabel: "ES_COMMON_CLEAR_SEARCH",
    //     minReqFields: 1,
    //     defaultValues: {
    //       applicationNumber: "",
    //       isActive: false,
    //       tenantId: "pg",
    //     },
    //     fields: [
    //       {
    //         // label: "Search Case Name or ID",
    //         type: "text",
    //         isMandatory: false,
    //         disable: false,
    //         populators: {
    //           name: "applicationNumber",
    //           error: "BR_PATTERN_ERR_MSG",
    //           validation: {
    //             pattern: {},
    //             minlength: 2,
    //           },
    //         },
    //       },
    //     ],
    //   },
    //   // label: "Registration-Requests",
    //   children: {},
    //   show: true,
    // },
    searchResult: {
      label: "",
      uiConfig: {
        columns: [
          {
            label: "Case Name",
            // jsonPath: "businessObject.advocateDetails.applicationNumber",
            additionalCustomization: true,
          },
          {
            label: "Stage",
            jsonPath: "businessObject.individual.name",
            additionalCustomization: true,
          },
          {
            label: "Case Type",
            jsonPath: "ProcessInstance.businessService",
            additionalCustomization: true,
          },
          {
            label: "Pending Tasks",
            jsonPath: "businessObject.auditDetails.createdTime",
            additionalCustomization: true,
          },
          { label: "Action", jsonPath: "businessObject.individual.individualId", additionalCustomization: true },
        ],
        enableGlobalSearch: false,
        enableColumnSort: true,
        resultsJsonPath: "items",
      },
      children: {},
      show: true,
    },
  },
  additionalSections: {},
  additionalDetails: "applicationNumber",
};

const sampleData = [
  { caseName: "ABC vs. XYZ", stage: "Trail", caseType: "NIA S138", pendingTasks: "-", actions: "Start" },
  { caseName: "ABC vs. XYZ", stage: "Trail", caseType: "NIA S138", pendingTasks: "-", actions: "Start" },
  { caseName: "ABC vs. XYZ", stage: "Inquiry", caseType: "NIA S138", pendingTasks: "4", actions: "Start" },
  { caseName: "ABC vs. XYZ", stage: "Filling", caseType: "NIA S138", pendingTasks: "6", actions: "Start" },
  { caseName: "ABC vs. XYZ", stage: "Filling", caseType: "NIA S138", pendingTasks: "3", actions: "Start" },
];

function PreHearingModal({ onCancel, onSubmit }) {
  const { t } = useTranslation();

  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };

  const CloseBtn = (props) => {
    return (
      <div onClick={props.onClick} style={{ height: "100%", display: "flex", alignItems: "center", paddingRight: "20px", cursor: "pointer" }}>
        <CloseSvg />
      </div>
    );
  };

  const popUpStyle = {
    width: "70%",
    height: "80%",
    borderRadius: "0.3rem",
  };

  return (
    <Modal
      headerBarEnd={<CloseBtn onClick={onCancel} />}
      // actionCancelLabel={t("CORE_LOGOUT_CANCEL")}
      actionCancelOnSubmit={onCancel}
      actionSaveLabel={t("Reschedule All Hearings")}
      actionSaveOnSubmit={onSubmit}
      formId="modal-action"
      headerBarMain={<Heading label={t("Admission Hearings (34)")} />}
      className="pre-hearings"
      popupStyles={popUpStyle}
    >
      <div></div>
      <div className="inbox-search-wrapper">
        <InboxSearchComposer customStyle={sectionsParentStyle} configs={configPreHearing}></InboxSearchComposer>
      </div>
    </Modal>
  );
}

export default PreHearingModal;
