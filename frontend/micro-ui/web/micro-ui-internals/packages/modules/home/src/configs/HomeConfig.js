import { TabBenchSearchConfig } from "./BenchHomeConfig";
import { TabCourtRoomSearchConfig } from "./CourtRoomHomeConfig";
import { TabFSOSearchConfig } from "./FSOHomeConfig";
import { TabJudgeSearchConfig } from "./JudgeHomeConfig";
import { TabLitigantSearchConfig } from "./LitigantHomeConfig";

export const CaseWorkflowState = {
  CASE_RE_ASSIGNED: "CASE_RE_ASSIGNED",
  DRAFT_IN_PROGRESS: "DRAFT_IN_PROGRESS",
  UNDER_SCRUTINY: "UNDER_SCRUTINY",
  CASE_ADMITTED: "CASE_ADMITTED",
  PENDING_ADMISSION: "PENDING_ADMISSION",
};

export const subStageOptions = [
  "Filing",
  "Cognizance",
  "Inquiry",
  "Appearance",
  "Framing of charges",
  "Evidence",
  "Arguments",
  "Judgment",
  "Post-Judgement",
];
export const outcomesOptions = [
  "Withdrawn",
  "Settled",
  "Transferred",
  "Dismissed",
  "Allowed",
  "Partly allowed",
  "Convicted",
  "Partly convicted",
  "Abated",
];

export const userTypeOptions = [
  {
    code: "LITIGANT",
    name: "LITIGANT_TEXT",
    showBarDetails: false,
    isVerified: false,
    role: [
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
    ],
    subText: "LITIGANT_SUB_TEXT",
  },
  {
    code: "ADVOCATE",
    name: "ADVOCATE_TEXT",
    showBarDetails: true,
    isVerified: true,
    hasBarRegistrationNo: true,
    role: [
      "ADVOCATE_ROLE",
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
    ],
    apiDetails: {
      serviceName: "/advocate/advocate/v1/_create",
      requestKey: "advocate",
      AdditionalFields: ["barRegistrationNumber"],
    },
    subText: "ADVOCATE_SUB_TEXT",
  },
  {
    code: "ADVOCATE_CLERK",
    name: "ADVOCATE_CLERK_TEXT",
    showBarDetails: true,
    hasStateRegistrationNo: true,
    isVerified: true,
    role: [
      "ADVOCATE_CLERK_ROLE",
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
    ],
    apiDetails: {
      serviceName: "/advocate/clerk/v1/_create",
      requestKey: "clerk",
      AdditionalFields: ["stateRegnNumber"],
    },

    subText: "ADVOCATE_CLERK_SUB_TEXT",
  },
];

export const rolesToConfigMapping = [
  {
    roles: [
      "CASE_CREATOR",
      "CASE_EDITOR",
      "CASE_VIEWER",
      "DEPOSITION_CREATOR",
      "DEPOSITION_VIEWER",
      "APPLICATION_CREATOR",
      "APPLICATION_VIEWER",
      "HEARING_VIEWER",
      "ORDER_VIEWER",
      "SUBMISSION_CREATOR",
      "SUBMISSION_RESPONDER",
      "SUBMISSION_DELETE",
    ],
    config: TabLitigantSearchConfig,
    isLitigant: true,
    showJoinFileOption: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/home/file-case/case",
      urlDependentOn: "status",
      urlDependentValue: ["DRAFT_IN_PROGRESS", "CASE_RE_ASSIGNED"],
      params: [{ key: "caseId", value: "id" }],
    },
  },
  {
    roles: ["CASE_APPROVER"],
    config: TabJudgeSearchConfig,
    isJudge: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  {
    roles: ["HEARING_CREATOR"],
    config: TabJudgeSearchConfig,
    isCourtOfficer: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  {
    roles: ["CASE_REVIEWER"],
    config: TabFSOSearchConfig,
    isFSO: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/case",
      urlDependentOn: "status",
      urlDependentValue: "UNDER_SCRUTINY",
      params: [{ key: "caseId", value: "id" }],
    },
  },
  {
    roles: ["BENCHCLERK_ROLE"],
    config: TabBenchSearchConfig,
    isCourtOfficer: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  {
    roles: ["HEARING_CREATOR"],
    config: TabCourtRoomSearchConfig,
    isCourtOfficer: true,
    onRowClickRoute: {
      dependentUrl: "/dristi/admission",
      urlDependentOn: "status",
      urlDependentValue: "PENDING_ADMISSION",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
];

export const caseTypes = [{ name: "NIA S138", code: "NIA S138" }];

export const taskTypes = [
  { code: "case", name: "Case" },
  { code: "hearing", name: "Hearing" },
  { code: "order-managelifecycle", name: "Order" },
  { code: "order-judgement", name: "Order of Judgement" },
  { code: "async-voluntary-submission-managelifecycle", name: "Voluntary Submission" },
  { code: "async-submission-with-response-managelifecycle", name: "Submission With Response" },
  { code: "async-order-submission-managelifecycle", name: "Submission Without Response" },
];
export const pendingTaskCaseActions = {
  PAYMENT_PENDING: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Make Payment",
    redirectDetails: {
      url: "/home/home-pending-task/e-filing-payment-breakdown",
    },
  },
  UNDER_SCRUTINY: {
    actorName: ["FSO"],
    actionName: "Case Filed and ready for FSO to review",
    redirectDetails: {
      url: "/dristi/case",
      params: [{ key: "caseId", value: "id" }],
    },
  },
  CASE_RE_ASSIGNED: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Case Sent Back for Edit",
    redirectDetails: {
      url: "/dristi/home/file-case/case",
      params: [{ key: "caseId", value: "id" }],
    },
  },
  PENDING_ADMISSION: {
    actorName: ["JUDGE"],
    actionName: "Case Approved from Scrutiny",
    redirectDetails: {
      url: "/dristi/admission",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  ADMISSION_HEARING_SCHEDULED: {
    actorName: ["JUDGE"],
    actionName: "Admission hearing scheduled - Admit Case",
    redirectDetails: {
      url: "/dristi/admission",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  CASE_ADMITTED: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/dristi/admission",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "caseId", value: "id" },
      ],
    },
  },
  SCHEDULE_HEARING: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/home/home-pending-task/home-schedule-hearing",
      params: [{ key: "filingNumber", value: "filingNumber" }],
    },
  },
};

export const pendingTaskHearingActions = {
  SCHEDULE_HEARING: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/home/home-pending-task/home-schedule-hearing",
      params: [{ key: "filingNumber", value: "filingNumber" }],
    },
  },
  OPTOUT: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Schedule admission hearing",
    redirectDetails: {
      url: "/home/home-pending-task/home-schedule-hearing",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "status", defaultValue: "OPTOUT" },
      ],
    },
  },
};

export const pendingTaskOrderActions = {
  SAVE_DRAFT: {
    actorName: ["JUDGE"],
    actionName: "Schedule admission hearing",
    customFunction: "handleCreateOrder",
    additionalDetailsKeys: ["orderType"],
    redirectDetails: {
      url: "/orders/generate-orders",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  DRAFT_IN_PROGRESS: {
    actorName: ["JUDGE"],
    actionName: "Draft in Progress for Order",
    redirectDetails: {
      url: "/orders/generate-orders",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "orderNumber", value: "referenceId" },
      ],
    },
  },

  SUMMON_WARRANT_STATUS: {
    actorName: ["JUDGE"],
    actionName: "Show Summon-Warrant Status",
    redirectDetails: {
      url: "/home/home-pending-task/summons-warrants-modal",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "hearingId", value: "referenceId" },
      ],
    },
  },
  PAYMENT_PENDING_POST: {
    actorName: ["JUDGE"],
    actionName: "Show Summon-Warrant Status",
    redirectDetails: {
      url: "/home/home-pending-task/post-payment-modal",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "orderNumber", value: "referenceId" },
      ],
    },
  },
};

export const pendingTaskOrderOfJudgementActions = {};

export const pendingTaskVoluntarySubmissionActions = {
  MAKE_PAYMENT_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Payment for Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  ESIGN_THE_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Esign the Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  PENDINGREVIEW: {
    actorName: ["JUDGE"],
    actionName: "Review the submission",
    customFunction: "handleReviewSubmission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  PENDINGAPPROVAL: {
    actorName: ["JUDGE"],
    actionName: "Review the submission",
    customFunction: "handleReviewSubmission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
};

export const pendingTaskSubmissionWithResponseActions = {
  CREATE_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Mandatory Submission of Documents",
    customFunction: "handleReviewOrder",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "orderNumber", value: "referenceId" },
      ],
    },
  },
  MAKE_PAYMENT_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Payment for Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  ESIGN_THE_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Esign the Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
};

export const pendingTaskSubmissionWithoutResponseActions = {
  CREATE_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Mandatory Submission of Documents",
    customFunction: "handleReviewOrder",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "orderNumber", value: "referenceId" },
      ],
    },
  },
  MAKE_PAYMENT_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Payment for Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
  ESIGN_THE_SUBMISSION: {
    actorName: ["LITIGANT/ADVOCATE"],
    actionName: "Esign the Submission",
    redirectDetails: {
      url: "/submissions/submissions-create",
      params: [
        { key: "filingNumber", value: "filingNumber" },
        { key: "applicationNumber", value: "referenceId" },
      ],
    },
  },
};

export const selectTaskType = {
  case: pendingTaskCaseActions,
  hearing: pendingTaskHearingActions,
  "order-managelifecycle": pendingTaskOrderActions,
  "order-judgement": pendingTaskOrderOfJudgementActions,
  "async-voluntary-submission-managelifecycle": pendingTaskVoluntarySubmissionActions,
  "async-submission-with-response-managelifecycle": pendingTaskSubmissionWithResponseActions,
  "async-order-submission-managelifecycle": pendingTaskSubmissionWithoutResponseActions,
};
