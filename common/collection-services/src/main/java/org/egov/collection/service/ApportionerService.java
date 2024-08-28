package org.egov.collection.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Payment;
import org.egov.collection.model.PaymentDetail;
import org.egov.collection.model.PaymentRequest;
import org.egov.collection.web.contract.ApportionRequest;
import org.egov.collection.web.contract.ApportionResponse;
import org.egov.collection.web.contract.Bill;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApportionerService {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Makes a call to apportion service and gets all the bills apportioned.
	 * 
	 * @param paymentRequest
	 * @return
	 */
	public Map<String,Bill> apportionBill(PaymentRequest paymentRequest) {
		
		RequestInfo requestInfo = paymentRequest.getRequestInfo();
		Payment payment = paymentRequest.getPayment();
		List<Bill> bills = payment.getPaymentDetails().stream().map(PaymentDetail::getBill).collect(Collectors.toList());
		StringBuilder uri = new StringBuilder();
		uri.append(applicationProperties.getApportionHost()).append(applicationProperties.getApportionURI());
		ApportionRequest apportionRequest = ApportionRequest.builder().bills(bills).tenantId(payment.getTenantId())
				.requestInfo(requestInfo).build();
		ApportionResponse apportionResponse;
		try {
			apportionResponse = restTemplate.postForObject(uri.toString(), apportionRequest,ApportionResponse.class);
			if (null != apportionResponse) {
				if(CollectionUtils.isEmpty(apportionResponse.getBills()))
					throw new CustomException("APPORTIONING_FAILED_CODE", "Apportioning of the bill Failed");
			}else
				throw new CustomException("APPORTIONING_FAILED_CODE", "Apportioning of the bill Failed");
		} catch (Exception e) {
			log.error("Error while apportioning the bill: ", e);
			throw new CustomException("APPORTIONING_FAILED_CODE", "Apportioning of the bill Failed");
		}

		Map<String,Bill> billIdToBillMap = apportionResponse.getBills().stream().collect(Collectors.toMap(Bill::getId, Function.identity()));

		return billIdToBillMap;
	}
	

	/**
	 * Method to segregate bills based on the tenantid, as apportioning service takes bills of one tenant at a time.
	 * 
	 * @param bills
	 * @return
	 */
	public Map<String, List<Bill>> seggregateBillOnTenantId(List<Bill> bills) {
		Map<String, List<Bill>> seggregatedBills = new HashMap<>();
		bills.forEach(bill -> {
			List<Bill> billsBelongingtoATenant = new ArrayList<>();
			if (null != seggregatedBills.get(bill.getTenantId()))
				billsBelongingtoATenant = seggregatedBills.get(bill.getTenantId());
			billsBelongingtoATenant.add(bill);
			seggregatedBills.put(bill.getTenantId(), billsBelongingtoATenant);
		});

		return seggregatedBills;
	}

}
