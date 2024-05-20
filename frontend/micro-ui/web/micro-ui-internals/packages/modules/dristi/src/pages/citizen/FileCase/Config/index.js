import { advocateDetailsConfig } from "./advocateDetailsConfig";
import { chequeDetailsConfig } from "./chequedetailsConfig";
import { complaintdetailconfig } from "./complaindetailsConfig";
import { debtliabilityconfig } from "./debtLiabilityConfig";
import { delayApplicationConfig } from "./delayApplicationConfig";
import { respondentconfig } from "./respondentConfig";
import { reviewcasefileconfig } from "./reviewcasefileconfig";

export const sideMenuConfig = [
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_LITIGENT_DETAILS",
    children: [
      {
        key: "complaintDetails",
        label: "CS_COMPLAINT_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: complaintdetailconfig,
      },
      {
        key: "respondentDetails",
        label: "CS_RESPONDENT_DETAILS",
        checked: false,
        isCompleted: false,
        isDisabled: false,
        pageConfig: respondentconfig,
      },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_CASE_SPECIFIC_DETAILS",
    children: [
      { label: "CS_CHEQUE_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: chequeDetailsConfig },
      { label: "CS_DEBT_LIABILITY_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: debtliabilityconfig },
      { label: "CS_DEMAND_NOTICE_DETAILS", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_DELAY_APPLICATIONS", checked: false, isCompleted: false, isDisabled: false, pageConfig: delayApplicationConfig },
    ],
    checked: false,
    isCompleted: 0,
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_ADDITIONAL_DETAILS",
    children: [
      { label: "CS_WITNESS_DETAILS", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_PRAYER_SWORN_STATEMENT", checked: false, isCompleted: false, isDisabled: false },
      { label: "CS_ADVOCATE_DETAILS", checked: false, isCompleted: false, isDisabled: false, pageConfig: advocateDetailsConfig },
    ],
  },
  {
    isOpen: false,
    isDisabled: false,
    title: "CS_REVIEW_SIGN",
    children: [
      { label: "CS_REVIEW_CASE_FILE", checked: false, isCompleted: false, isDisabled: false, pageConfig: reviewcasefileconfig },
      { label: "CS_ADD_SIGNATURE", checked: false, isCompleted: false, isDisabled: false },
    ],
  },
];
