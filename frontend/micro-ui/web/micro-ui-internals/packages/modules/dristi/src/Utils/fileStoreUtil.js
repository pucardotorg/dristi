export const getFilestoreId = () => {
  const origin = window.location.origin;
  if (origin.includes("kerala-dev")) {
    return "2aefb901-edc6-4a45-95f8-3ea383a513f5";
  } else if (origin.includes("kerala-qa")) {
    return "0cdd01bf-5c6c-43de-86df-48406ce4f5a8";
  } else if (origin.includes("kerala-hc")) {
    return "35558d0c-e494-47b9-8920-01a1a67eccdd";
  } else {
    return "35558d0c-e494-47b9-8920-01a1a67eccdd";
  }
};
