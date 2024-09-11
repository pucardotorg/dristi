import _ from "lodash";
import { UICustomizations } from "../configs/UICustomizations";

import { CustomisedHooks } from "../hooks";

export const overrideHooks = () => {
  Object.keys(CustomisedHooks).map((ele) => {
    if (ele === "Hooks") {
      Object.keys(CustomisedHooks[ele]).map((hook) => {
        Object.keys(CustomisedHooks[ele][hook]).map((method) => {
          setupHooks(hook, method, CustomisedHooks[ele][hook][method]);
        });
      });
    } else if (ele === "Utils") {
      Object.keys(CustomisedHooks[ele]).map((hook) => {
        Object.keys(CustomisedHooks[ele][hook]).map((method) => {
          setupHooks(hook, method, CustomisedHooks[ele][hook][method], false);
        });
      });
    } else {
      Object.keys(CustomisedHooks[ele]).map((method) => {
        setupLibraries(ele, method, CustomisedHooks[ele][method]);
      });
    }
  });
};
const setupHooks = (HookName, HookFunction, method, isHook = true) => {
  window.Digit = window.Digit || {};
  window.Digit[isHook ? "Hooks" : "Utils"] = window.Digit[isHook ? "Hooks" : "Utils"] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName] = window.Digit[isHook ? "Hooks" : "Utils"][HookName] || {};
  window.Digit[isHook ? "Hooks" : "Utils"][HookName][HookFunction] = method;
};
/* To Overide any existing libraries  we need to use similar method */
const setupLibraries = (Library, service, method) => {
  window.Digit = window.Digit || {};
  window.Digit[Library] = window.Digit[Library] || {};
  window.Digit[Library][service] = method;
};

/* To Overide any existing config/middlewares  we need to use similar method */
export const updateCustomConfigs = () => {
  setupLibraries("Customizations", "dristiOrders", { ...window?.Digit?.Customizations?.dristiOrders, ...UICustomizations });
  // setupLibraries("Utils", "parsingUtils", { ...window?.Digit?.Utils?.parsingUtils, ...parsingUtils });
};

export default {};

export const formatDateDifference = (previousDate) => {
  const currentDate = new Date();
  let previousDateObj;

  if (typeof previousDate === "string" && previousDate.includes("-")) {
    const [day, month, year] = previousDate.split("-");
    previousDateObj = new Date(year, month - 1, day);
  } else {
    previousDateObj = new Date(Number(previousDate));
  }

  const timeDifference = currentDate - previousDateObj;
  const dayDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

  return dayDifference;
};

export const formatDate = (date) => {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}-${month}-${year}`;
};

export const convertToDateInputFormat = (dateInput) => {
  let date;

  if (typeof dateInput === "number") {
    date = new Date(dateInput);
  } else if (typeof dateInput === "string" && dateInput.includes("-")) {
    const [day, month, year] = dateInput.split("-");
    if (!isNaN(day) && !isNaN(month) && !isNaN(year) && day.length === 2 && month.length === 2 && year.length === 4) {
      date = new Date(`${year}-${month}-${day}`);
    } else {
      console.error("Invalid date format");
    }
  } else {
    console.error("Invalid input type or format");
  }

  return formatDate(date);
};
