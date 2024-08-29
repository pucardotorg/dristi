import React, { useState } from "react";
import axios from "axios";

const DownloadIcon = () => {
  return (
    <svg width="12" height="15" viewBox="0 0 12 15" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M11.8337 5H8.50033V0H3.50033V5H0.166992L6.00033 10.8333L11.8337 5ZM0.166992 12.5V14.1667H11.8337V12.5H0.166992Z" fill="#0B0C0C" />
    </svg>
  );
};
const DashboardPage = () => {
  const getCurrentDate = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };
  const [stepper, setStepper] = useState(1);
  const [startDate, setStartDate] = useState(getCurrentDate());
  const [endDate, setEndDate] = useState(getCurrentDate());
  const [selectedRange, setSelectedRange] = useState({ startDate: startDate, endDate: endDate });
  const [downloadingIndices, setDownloadingIndices] = useState([]);
  const [downloadTimers, setDownloadTimers] = useState({});

  const handleSubmit = () => {
    if (startDate == null || endDate == null || startDate > endDate) {
      setError("End date should be after the start date");
    } else {
      setError(null);
      setSelectedRange({ startDate: startDate, endDate: endDate });
    }
  };

  const [error, setError] = useState(null);
  const reportOptions = [
    {
      name: "A Diary Register",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.hearing.id%2CData.hearing.hearingId%2CData.hearing.hearingType%2CData.hearing.startTime%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.hearing.id%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.hearingId%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.hearingType%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.startTime%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3Afd69ad22-31df-49d4-955d-abd204b7d3cf%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27A%20Diary%20Register%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Calendar of summary cases",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.caseDetails.filingNumber%2CData.caseDetails.additionalDetails.respondentDetails.formdata.data.respondentFirstName%2CData.caseDetails.additionalDetails.respondentDetails.formdata.data.respondentLastName%2CData.caseDetails.caseCategory%2CData.caseDetails.statutesAndSections.sections%2CData.caseDetails.filingDate%2CsentenceDate%2CoffenceDate%2CcomplaintReportDate%2CaccusedApprehensionDate%2CtrialCommencementDate%2CData.caseDetails.judgementDate%2CremarksExplanationDelay%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.caseDetails.filingNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.additionalDetails.respondentDetails.formdata.data.respondentFirstName%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.additionalDetails.respondentDetails.formdata.data.respondentLastName%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.caseCategory%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.statutesAndSections.sections%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.filingDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsentenceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AoffenceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcomplaintReportDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AaccusedApprehensionDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AtrialCommencementDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.judgementDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AremarksExplanationDelay%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3A%277b602bbe-8e92-4f8c-9ba7-645bc38767d2%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Calendar%20of%20summary%20cases%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Hearing Book",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.hearing.id%2CData.hearing.hearingId%2CData.hearing.endTime%2CData.hearing.notes%2CnextHearingTime%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.hearing.id%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.hearingId%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.endTime%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.hearing.notes%2Cinclude_unmapped%3Atrue%29%2C%28field%3AnextHearingTime%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3A%2734dff190-e8c0-4b7b-9d2d-08f65dc3f455%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Hearing%20Book%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Process Register",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.orderDetails.filingNumber%2CData.orderDetails.statuteSection.sections%2CData.orderDetails.orderType%2CData.orderDetails.orderNumber%2CData.orderDetails.createdDate%2CorderDeliveryDate%2CsentToPoliceDate%2CreceivedBackDate%2CorderDeliveryMode%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.orderDetails.filingNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.orderDetails.statuteSection.sections%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.orderDetails.orderType%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.orderDetails.orderNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.orderDetails.createdDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderDeliveryDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsentToPoliceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AreceivedBackDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderDeliveryMode%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%28%27%24state%27%3A%28store%3AappState%29%2Cmeta%3A%28alias%3A%21n%2Cdisabled%3A%21f%2Cfield%3AData.orderDetails.orderType%2Cindex%3A%2706fbff8a-d238-49d2-9073-596beb2a5648%27%2Ckey%3AData.orderDetails.orderType%2Cnegate%3A%21f%2Cparams%3A%21%28SUMMONS%2CNOTICES%2CWARRANTS%29%2Ctype%3Aphrases%2Cvalue%3A%21%28SUMMONS%2CNOTICES%2CWARRANTS%29%29%2Cquery%3A%28bool%3A%28minimum_should_match%3A1%2Cshould%3A%21%28%28match_phrase%3A%28Data.orderDetails.orderType%3ASUMMONS%29%29%2C%28match_phrase%3A%28Data.orderDetails.orderType%3ANOTICES%29%29%2C%28match_phrase%3A%28Data.orderDetails.orderType%3AWARRANTS%29%29%29%29%29%29%29%2Cindex%3A%2706fbff8a-d238-49d2-9073-596beb2a5648%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Process%20Register%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Statement of cases in which sanction to write-off is requested",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28caseNumber%2CcaseYear%2CaccusedName%2CsentenceDate%2CfineAmount%2CsanctioningDate%2CsanctioningNumber%2CstepsToRealiseFine%2Cremarks%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AcaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcaseYear%2Cinclude_unmapped%3Atrue%29%2C%28field%3AaccusedName%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsentenceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AfineAmount%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsanctioningDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsanctioningNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AstepsToRealiseFine%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aremarks%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3Aaefc863e-2678-40dd-ac9a-5e32cf8bb2b5%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Statement%20of%20cases%20in%20which%20sanction%20to%20write-off%20is%20requested%27%2Cversion%3A%278.11.3%27%29",
    },

    {
      name: "Register of calendar and committal proceeding cases",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.caseDetails.caseDetails.chequeDetails.formdata.data.depositDate%2CData.caseDetails.filingDate%2CcomplainantName%2CrespondantName%2CData.caseDetails.statutesAndSections.statute%2CData.caseDetails.statutesAndSections.sections%2CfilingDate%2CbailApprehensionDate%2CcourtCaseNumber%2CregistrationDate%2CtrialCommencementDate%2CjudgementDate%2CdeliveryDate%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.caseDetails.caseDetails.chequeDetails.formdata.data.depositDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.filingDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcomplainantName%2Cinclude_unmapped%3Atrue%29%2C%28field%3ArespondantName%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.statutesAndSections.statute%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.caseDetails.statutesAndSections.sections%2Cinclude_unmapped%3Atrue%29%2C%28field%3AfilingDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AbailApprehensionDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcourtCaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AregistrationDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AtrialCommencementDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AjudgementDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AdeliveryDate%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%28meta%3A%28field%3AData.caseDetails.caseDetails.chequeDetails.formdata.data.depositDate%2Cindex%3A%2725a94dc3-def9-4f69-b9cc-65173506e64f%27%2Cparams%3A%28%29%29%2Cquery%3A%28range%3A%28Data.caseDetails.caseDetails.chequeDetails.formdata.data.depositDate%3A%28format%3Astrict_date_optional_time%2Cgte%3Anow-1y%2Fd%2Clte%3Anow%29%29%29%29%29%2Cindex%3A%2725a94dc3-def9-4f69-b9cc-65173506e64f%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28Data.caseDetails.caseDetails.chequeDetails.formdata.data.depositDate%3A%28format%3Astrict_date_optional_time%2Corder%3Adesc%29%29%29%29%2Ctitle%3A%27Register%20of%20calendar%20and%20committal%20proceeding%20cases%27%2Cversion%3A%278.11.3%27%29",
    },

    {
      name: "Register of calendars submitted",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28judgementDate%2CnumberOfCases%2CsubmissionDateOfCalenders%2CcalanderRetrunDate%2Cremarks%2CCourt%2COrder%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AjudgementDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AnumberOfCases%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsubmissionDateOfCalenders%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcalanderRetrunDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aremarks%2Cinclude_unmapped%3Atrue%29%2C%28field%3ACourt%2Cinclude_unmapped%3Atrue%29%2C%28field%3AOrder%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3Ace0f075f-e0a3-443f-b3a4-2c66502c564d%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Register%20of%20calendars%20submitted%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Register of criminal miscellaneous petitions",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.applicationDetails.applicationNumber.keyword%2CData.applicationDetails.createdDate%2CData.applicationDetails.filingNumber%2CpetitionerName%2CpetitionerPositionInCase%2CpetitionAbstract%2CData.applicationDetails.orderDetails.orderType%2CorderLastModifiedBy%2CorderLastModifiedDate%2CorderStatus%2CorderExecutionDate%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.applicationDetails.applicationNumber.keyword%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.applicationDetails.createdDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.applicationDetails.filingNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3ApetitionerName%2Cinclude_unmapped%3Atrue%29%2C%28field%3ApetitionerPositionInCase%2Cinclude_unmapped%3Atrue%29%2C%28field%3ApetitionAbstract%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.applicationDetails.orderDetails.orderType%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderLastModifiedBy%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderLastModifiedDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderStatus%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderExecutionDate%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3Ab0459391-7d83-44c9-98c8-00ed778fb9f2%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28Data.applicationDetails.applicationNumber.keyword%3Aasc%29%29%29%2Ctitle%3A%27Register%20of%20criminal%20miscellaneous%20petitions%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Register of court fees and process fees",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28caseNumber%2CprocessFees%2CotherFees%2Cremarks%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AcaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AprocessFees%2Cinclude_unmapped%3Atrue%29%2C%28field%3AotherFees%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aremarks%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3A%274b3ac326-95c4-4591-b667-5af07c316f82%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Register%20of%20court%20fees%20and%20process%20fees%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Register of long pending cases",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28caseYear%2CcaseNumber%2CexpectedAppearanceDate%2CappearanceDate%2CaccusedDescription%2CorderNumber%2CorderDate%2CregistryDate%2CattachmentDate%2CevidenceRecordingDate%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AcaseYear%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AexpectedAppearanceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AappearanceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AaccusedDescription%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AorderDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AregistryDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AattachmentDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AevidenceRecordingDate%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3A%279f12a2d3-8470-481e-8431-de072b8e96a6%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27%20Register%20of%20long%20pending%20cases%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Register of remarks on calendars",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28JudicialMagistrateDate%2CJudicialMagistrateInitials%2CJudicialMagistrateRemarks%2CCaseNumber%2CcourtName%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AJudicialMagistrateDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AJudicialMagistrateInitials%2Cinclude_unmapped%3Atrue%29%2C%28field%3AJudicialMagistrateRemarks%2Cinclude_unmapped%3Atrue%29%2C%28field%3ACaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcourtName%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3A%277e733301-1275-4b27-b2f7-d90f8343e1b5%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Register%20of%20remarks%20on%20calendars%27%2Cversion%3A%278.11.3%27%29",
    },
    {
      name: "Register of summary trials",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28Data.orderDetails.filingNumber%2CData.orderDetails.orderType%2CoffenceDate%2CfilingDate%2CcomplainantName%2CrespondantName%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AData.orderDetails.filingNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AData.orderDetails.orderType%2Cinclude_unmapped%3Atrue%29%2C%28field%3AoffenceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AfilingDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcomplainantName%2Cinclude_unmapped%3Atrue%29%2C%28field%3ArespondantName%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%28%27%24state%27%3A%28store%3AappState%29%2Cmeta%3A%28alias%3A%21n%2Cdisabled%3A%21f%2Cfield%3AData.orderDetails.orderType%2Cindex%3A%2717079292-caf7-4300-ac87-78bf486914e5%27%2Ckey%3AData.orderDetails.orderType%2Cnegate%3A%21f%2Cparams%3A%28query%3AJUDGEMENT%29%2Ctype%3Aphrase%29%2Cquery%3A%28match_phrase%3A%28Data.orderDetails.orderType%3AJUDGEMENT%29%29%29%29%2Cindex%3A%2717079292-caf7-4300-ac87-78bf486914e5%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Register%20of%20summary%20trials%27%2Cversion%3A%278.11.3%27%29",
    },

    {
      name: "Register of warrants issued in execution of sentences",
      downloadLink:
        "https://dristi-kerala-dev.pucar.org/kibana/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28caseNumber%2CcaseYear%2CaccusedName%2CsentenceDate%2CfineAmount%2CsanctioningDate%2CsanctioningNumber%2CstepsToRealiseFine%2Cremarks%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3AcaseNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AcaseYear%2Cinclude_unmapped%3Atrue%29%2C%28field%3AaccusedName%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsentenceDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AfineAmount%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsanctioningDate%2Cinclude_unmapped%3Atrue%29%2C%28field%3AsanctioningNumber%2Cinclude_unmapped%3Atrue%29%2C%28field%3AstepsToRealiseFine%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aremarks%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%29%2Cindex%3Aaefc863e-2678-40dd-ac9a-5e32cf8bb2b5%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28_score%3Adesc%29%29%29%2Ctitle%3A%27Statement%20of%20cases%20in%20which%20sanction%20to%20write-off%20is%20requested%27%2Cversion%3A%278.11.3%27%29",
    },
  ];

  const handleDownload = async (downloadLink, index) => {
    setDownloadingIndices((prev) => [...prev, index]);
    setDownloadTimers((prev) => ({ ...prev, [index]: 0 }));

    const timer = setInterval(() => {
      setDownloadTimers((prev) => ({ ...prev, [index]: prev[index] + 1 }));
      console.log(downloadTimers);
    }, 1000);

    const baseUrl = "https://dristi-kerala-dev.pucar.org";
    const config = {
      headers: {
        "kbn-xsrf": "",
        "Content-Type": "application/json",
      },
    };
    try {
      const response = await axios.post(downloadLink, null, { ...config });
      console.log("Report generation initiated:", response.data);

      if (response.data && response.data.path) {
        const reportUrl = `${baseUrl}${response.data.path}`;
        console.log(reportUrl, "url");

        await new Promise((resolve) => setTimeout(resolve, 30000));

        const csvResponse = await axios.get(reportUrl, {
          ...config,
          responseType: "blob",
        });

        const blob = new Blob([csvResponse.data], { type: "text/csv" });

        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = `${reportOptions[index]["name"]}.csv`; // You can set a specific filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      } else {
        console.error("Report path not found in the response");
        setError();
      }
    } catch (error) {
      console.error("Error generating or downloading report:", error);
    } finally {
      clearInterval(timer);
      setDownloadingIndices((prev) => prev.filter((i) => i !== index));
      setDownloadTimers((prev) => {
        const newTimers = { ...prev };
        delete newTimers[index];
        return newTimers;
      });
    }
  };

  return (
    <div style={{ display: "flex" }}>
      <div style={{ display: "flex", flexDirection: "column", width: "220px", padding: "10px", gap: "16px" }}>
        <button
          className="dashboard-btn"
          style={{
            padding: "16px",
            color: stepper === 1 ? "#007E7E" : "#77787B",
            fontWeight: stepper === 1 ? "600" : "400",
          }}
          onClick={() => setStepper(1)}
        >
          Case Dynamics
        </button>
        <button
          className="dashboard-btn"
          style={{
            padding: "16px",
            color: stepper === 2 ? "#007E7E" : "#77787B",
            fontWeight: stepper === 2 ? "600" : "400",
          }}
          onClick={() => setStepper(2)}
        >
          Hearings Analysis
        </button>
        <button
          className="dashboard-btn"
          style={{
            padding: "16px",
            color: stepper === 3 ? "#007E7E" : "#77787B",
            fontWeight: stepper === 3 ? "600" : "400",
          }}
          onClick={() => setStepper(3)}
        >
          Appearance
        </button>
        <button
          className="dashboard-btn"
          style={{
            padding: "16px",
            color: stepper === 4 ? "#007E7E" : "#77787B",
            fontWeight: stepper === 4 ? "600" : "400",
          }}
          onClick={() => setStepper(4)}
        >
          Download Reports
        </button>
      </div>
      <div
        style={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "flex-start",
          padding: "20px",
          border: "none",
        }}
      >
        {stepper != 4 && (
          <div style={{ display: "flex", flexDirection: "row", gap: "15px", justifyContent: "flex-start", marginBottom: "10px" }}>
            <label style={{ display: "flex", gap: "8px" }}>
              From
              <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
            </label>
            <label style={{ display: "flex", gap: "8px" }}>
              to
              <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
            </label>
            <button onClick={handleSubmit} style={{ height: "20px" }}>
              Filter
            </button>
          </div>
        )}
        {error ? (
          <span style={{ color: "red" }}>{error}</span>
        ) : (
          <div style={{ flexGrow: 1, width: "100%" }}>
            {stepper === 1 && (
              <iframe
                src={`https://dristi-kerala-dev.pucar.org/kibana/app/dashboards#/view/5ea148e0-54a5-11ef-bca8-d927e7d27354?embed=true&_g=(refreshInterval:(pause:!t,value:60000),time:(from:'${selectedRange.startDate}',to:'${selectedRange.endDate}'))&_a=()&hide-filter-bar=true`}
                height="600"
                width="100%"
                title=" Case Dynamics Dashboard"
              />
            )}
            {stepper === 2 && (
              <iframe
                src={`https://dristi-kerala-dev.pucar.org/kibana/app/dashboards#/view/40428570-4b46-11ef-91c9-cf3be62bc7a6?embed=true&_g=(refreshInterval:(pause:!t,value:60000),time:(from:'${selectedRange.startDate}',to:'${selectedRange.endDate}'))&_a=()&hide-filter-bar=true`}
                height="600"
                width="100%"
                title="Hearings Analysis Dashboard"
              />
            )}
            {stepper === 3 && (
              <iframe
                src={`https://dristi-kerala-dev.pucar.org/kibana/app/dashboards#/view/aeeb2370-4f23-11ef-a07b-f500038d4785?embed=true&_g=(refreshInterval:(pause:!t,value:60000),time:(from:'${selectedRange.startDate}',to:'${selectedRange.endDate}'))&_a=()&hide-filter-bar=true`}
                height="600"
                width="100%"
                title="Appearance Dashboard"
              />
            )}
            {stepper === 4 && (
              <div style={{ marginTop: "20px" }}>
                <h2 style={{ fontWeight: "bold", margin: "10px" }}>Available Reports</h2>
                <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                  {reportOptions.map((option, index) => (
                    <div
                      key={index}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                        padding: "10px",
                        border: "1px solid #ccc",
                        borderRadius: "4px",
                        maxWidth: "600px",
                      }}
                    >
                      <span>{option.name}</span>
                      <button
                        onClick={() => handleDownload(option.downloadLink, index)}
                        disabled={downloadingIndices.includes(index)}
                        style={{ display: "flex", alignItems: "center", gap: "5px" }}
                      >
                        {downloadingIndices.includes(index) ? (
                          <div style={{ display: "flex" }}>Downloading... {downloadTimers[index]}s</div>
                        ) : (
                          <DownloadIcon />
                        )}
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default DashboardPage;
