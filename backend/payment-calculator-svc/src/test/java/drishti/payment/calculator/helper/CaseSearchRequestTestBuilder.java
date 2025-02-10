package drishti.payment.calculator.helper;

import drishti.payment.calculator.web.models.CaseCriteria;
import drishti.payment.calculator.web.models.CaseSearchRequest;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

public class CaseSearchRequestTestBuilder {

    private final CaseSearchRequest.CaseSearchRequestBuilder builder ;

    public CaseSearchRequestTestBuilder() {
        this.builder = CaseSearchRequest.builder();
    }

    public static CaseSearchRequestTestBuilder builder() {
        return new CaseSearchRequestTestBuilder();
    }

    public CaseSearchRequest build() {
        return this.builder.build();
    }
    public CaseSearchRequestTestBuilder withCriteriaAndRequestInfo (String caseId, String filingNumber) {
        this.builder.criteria(List.of(CaseCriteria.builder().caseId(caseId).filingNumber(filingNumber).build())).requestInfo(new RequestInfo());
        return this;
    }
}
