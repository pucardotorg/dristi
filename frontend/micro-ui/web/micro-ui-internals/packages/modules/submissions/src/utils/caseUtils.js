export const getAllAssignees = (caseDetails) => {
  if (Array.isArray(caseDetails?.representatives || []) && caseDetails?.representatives?.length > 0) {
    return caseDetails?.representatives
      ?.reduce((res, curr) => {
        if (curr && curr?.additionalDetails?.uuid) {
          res.push(curr?.additionalDetails?.uuid);
        }
        if (curr && curr?.representing && Array.isArray(curr?.representing || []) && curr?.representing?.length > 0) {
          const representingUuids = curr?.representing?.reduce((result, current) => {
            if (current && current?.additionalDetails?.uuid) {
              result.push(current?.additionalDetails?.uuid);
            }
            return result;
          }, []);
          res.push(representingUuids);
        }
        return res;
      }, [])
      ?.flat();
  } else if (Array.isArray(caseDetails?.litigants || []) && caseDetails?.litigants?.length > 0) {
    return caseDetails?.litigants
      ?.reduce((res, curr) => {
        if (curr && curr?.additionalDetails?.uuid) {
          res.push(curr?.additionalDetails?.uuid);
        }
        return res;
      }, [])
      ?.flat();
  }
  return null;
};
