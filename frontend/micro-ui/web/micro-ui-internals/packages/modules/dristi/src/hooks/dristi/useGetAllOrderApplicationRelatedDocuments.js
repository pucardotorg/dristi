import { useState, useCallback } from "react";
import { DRISTIService } from "../../services";

const useGetAllOrderApplicationRelatedDocuments = () => {
  const [documents, setDocuments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchRecursiveData = useCallback(async (initialInput) => {
    setIsLoading(true);

    let currentResponse = initialInput;
    const collectedDocuments = [];

    if (currentResponse?.documents) {
      // for Maintain the sequence like first will be SignedPDF
      const signedDocuments = currentResponse?.documents?.filter((doc) => doc?.documentType === "SIGNED");
      const otherDocuments = currentResponse?.documents?.filter((doc) => doc?.documentType !== "SIGNED");
      collectedDocuments.push(...signedDocuments, ...otherDocuments);
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
          const signedApplicationDocuments = applicationData?.applicationList?.[0]?.documents?.filter((doc) => doc?.documentType === "SIGNED");
          const otherApplictionDocuments = applicationData?.applicationList?.[0]?.documents?.filter((doc) => doc?.documentType !== "SIGNED");
          collectedDocuments.push(...signedApplicationDocuments, ...otherApplictionDocuments);
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
          const signedOrderDocuments = orderData?.list?.[0]?.documents?.filter((doc) => doc?.documentType === "SIGNED");
          const otherOrderDocuments = orderData?.list?.[0]?.documents?.filter((doc) => doc?.documentType !== "SIGNED");
          collectedDocuments.push(...signedOrderDocuments, ...otherOrderDocuments);
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
