import { useState, useCallback } from "react";
import { DRISTIService } from "../../services";

// sort like first document will signed one, rest will sort based on documentOrder
const _getSortedByOrder = (documents) => {
  return documents?.sort((a, b) => {
    if (a?.documentType === "SIGNED" && b?.documentType !== "SIGNED") return -1;
    if (a?.documentType !== "SIGNED" && b?.documentType === "SIGNED") return 1;
    return (a?.documentOrder || 0) - (b?.documentOrder || 0);
  });
};

const useGetAllOrderApplicationRelatedDocuments = () => {
  const [documents, setDocuments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchRecursiveData = useCallback(async (initialInput) => {
    setIsLoading(true);

    let currentResponse = initialInput;
    const collectedDocuments = [];

    if (currentResponse?.documents) {
      const sortedDocuments = _getSortedByOrder(currentResponse?.documents);
      collectedDocuments.push(...sortedDocuments);
    }

    while (currentResponse?.additionalDetails?.formdata?.refApplicationId || currentResponse?.additionalDetails?.formdata?.refOrderId) {
      let nextResponse;

      if (currentResponse?.additionalDetails?.formdata?.refApplicationId) {
        const applicationData = await DRISTIService.searchSubmissions(
          {
            criteria: {
              filingNumber: currentResponse?.filingNumber,
              applicationNumber: currentResponse?.additionalDetails?.formdata?.refApplicationId,
              tenantId: currentResponse?.tenantId,
            },
            tenantId: currentResponse?.tenantId,
          },
          {}
        );

        if (applicationData?.applicationList?.[0]?.documents) {
          const applicationDocuments = _getSortedByOrder(applicationData?.applicationList?.[0]?.documents);
          collectedDocuments.push(...applicationDocuments);
        }

        nextResponse = applicationData?.applicationList?.[0];
      } else if (currentResponse?.additionalDetails?.formdata?.refOrderId) {
        const orderData = await DRISTIService.searchOrders(
          {
            tenantId: currentResponse?.tenantId,
            criteria: {
              filingNumber: currentResponse?.filingNumber,
              applicationNumber: "",
              cnrNumber: "",
              orderNumber: currentResponse?.additionalDetails?.formdata?.refOrderId,
            },
          },
          { tenantId: currentResponse?.tenantId }
        );

        if (orderData?.list?.[0]?.documents) {
          const orderDocuments = _getSortedByOrder(orderData?.list?.[0]?.documents);
          collectedDocuments.push(...orderDocuments);
        }

        nextResponse = orderData?.list?.[0];
      }

      if (!nextResponse?.additionalDetails?.formdata?.refApplicationId && !nextResponse?.additionalDetails?.formdata?.refOrderId) break;
      currentResponse = nextResponse;
    }

    setDocuments(collectedDocuments);
    setIsLoading(false);
  }, []);

  return { documents, isLoading, fetchRecursiveData };
};

export default useGetAllOrderApplicationRelatedDocuments;
