import { Link, useHistory } from "react-router-dom";
import _ from "lodash";

//create functions here based on module name set in mdms(eg->SearchProjectConfig)
//how to call these -> Digit?.Customizations?.[masterName]?.[moduleName]
// these functions will act as middlewares
// var Digit = window.Digit || {};
const customColumnStyle = { whiteSpace: "nowrap" };
const businessServiceMap = {};

const inboxModuleNameMap = {};

export const UICustomizations = {
  minTodayDateValidation: () => {
    return {
      min: new Date().toISOString().split("T")[0],
    };
  },
  maxTodayDateValidation: () => {
    return {
      max: new Date().toISOString().split("T")[0],
    };
  },

  orderTitleValidation: () => {
    return {
      pattern: /^(\b\w+\b\s*){0,15}$/i,
    };
  },

  alphaNumericValidation: () => {
    return {
      pattern: /[^a-zA-Z0-9\s]/g,
    };
  },

  alphaNumericInputTextValidation: () => {
    return {
      pattern: /^[a-zA-Z0-9 ]+$/i,
    };
  },
};
