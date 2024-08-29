import { useQuery, useQueryClient } from "react-query";
import { DRISTIService } from "../../services";

function usePaymentCalculator(data, params, keys, enabled) {
    const { isLoading, data: calculationResponse, isFetching, refetch, error } = useQuery(
        `GET_PAYMENT_${keys}`,
        () => DRISTIService.getPaymentBreakup(data, params),
        {
            cacheTime: 0,
            enabled: Boolean(enabled),
            retry: false, // Disable automatic retries to prevent flooding the API with requests
        }
    );

    if (error) {
        console.error("Error fetching payment:", error);
    }

    return {
        isLoading,
        isFetching,
        data: calculationResponse,
        refetch,
        error,
    };
}

export default usePaymentCalculator;
