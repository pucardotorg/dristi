package digit.web.models;



public interface SearchCriteria {

    String getTenantId();

    String getJudgeId();

    String getCourtId();

    Long getFromDate();

    Long getToDate();
}
