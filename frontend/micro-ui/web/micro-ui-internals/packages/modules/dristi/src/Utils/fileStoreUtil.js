export const getFilestoreId = () => {
  const origin = window.location.origin;
  if (origin.includes("kerala-dev")) {
    return "a640da74-007d-4a8a-b9ab-621c50970f64";
  } else if (origin.includes("kerala-qa")) {
    return "0cdd01bf-5c6c-43de-86df-48406ce4f5a8";
  } else if (origin.includes("kerala-hc")) {
    return "35558d0c-e494-47b9-8920-01a1a67eccdd";
  } else if (origin.includes("kerala-uat")) {
    return "9d23b127-c9e9-4fd1-9dc8-e2e762269046";
  } else {
    return "35558d0c-e494-47b9-8920-01a1a67eccdd";
  }
};
