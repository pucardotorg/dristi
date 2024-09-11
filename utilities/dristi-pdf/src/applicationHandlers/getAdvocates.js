function getAdvocates(caseDetails) {
  let litigants = {};
  let list = [];

  caseDetails?.litigants?.forEach((litigant) => {
    list =
      caseDetails?.representatives
        ?.filter((item) => {
          return (
            item?.representing?.some(
              (lit) => lit?.individualId === litigant?.individualId
            ) && item?.additionalDetails?.uuid
          );
        })
        ?.map((item) => {
          return item;
        }) || [];
    litigants[litigant?.additionalDetails?.uuid] = list;
  });
  return litigants;
}

module.exports = { getAdvocates };
