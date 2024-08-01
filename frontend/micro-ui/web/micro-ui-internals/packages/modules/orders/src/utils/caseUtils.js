export const getAllAssignees = (caseDetails, getAdvocates = true, getLitigent = true) => {
  if (Array.isArray(caseDetails?.representatives || []) && caseDetails?.representatives?.length > 0) {
    return caseDetails?.representatives
      ?.reduce((res, curr) => {
        if (getAdvocates && curr && curr?.additionalDetails?.uuid) {
          res.push(curr?.additionalDetails?.uuid);
        }
        if (getLitigent && curr && curr?.representing && Array.isArray(curr?.representing || []) && curr?.representing?.length > 0) {
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

export const getAdvocates = (caseDetails) => {
  let litigants = {};
  let list = [];

  caseDetails?.litigants?.forEach((litigant) => {
    list = caseDetails?.representatives
      ?.filter((item) => {
        return item?.representing?.some((lit) => lit?.individualId === litigant?.individualId) && item?.additionalDetails?.uuid;
      })
      .map((item) => item?.additionalDetails?.uuid);
    if (list?.length > 0) {
      litigants[litigant?.additionalDetails?.uuid] = list;
    } else {
      litigants[litigant?.additionalDetails?.uuid] = [litigant?.additionalDetails?.uuid];
    }
  });
  return litigants;
};
