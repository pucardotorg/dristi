import React, { useEffect, useMemo, useState } from "react";
import { Button, Loader, TextInput } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import { useTranslation } from "react-i18next";
import TasksComponent from "../../components/TaskComponent";

const getStyles = () => ({
  container: { display: "flex", flexDirection: "row", height: "100vh" },
  centerPanel: { flex: 3, padding: "24px 40px 16px 16px", border: "1px solid #e0e0e0", marginLeft: "20px" },
  title: { width: "584px", height: "38px", color: "#0A0A0A", fontSize: "32px", fontWeight: 700, marginBottom: "20px" },
  rightPanel: { flex: 1, padding: "24px 16px 24px 24px", borderLeft: "1px solid #ccc" },
  signaturePanel: { display: "flex", flexDirection: "column" },
  signatureTitle: { fontSize: "24px", fontWeight: 700, color: "#3D3C3C" },
  goButton: { padding: 20, boxShadow: "none" },
});

const ADiaryPage = ({ path }) => {
  const { t } = useTranslation();
  const history = useHistory();
  const getCurrentDate = () => {
    const today = new Date();
    return new Date(today.getTime() - today.getTimezoneOffset() * 60000).toISOString().split("T")[0];
  };
  const Digit = window.Digit || {};
  const tenantId = window?.Digit.ULBService.getCurrentTenantId();
  const userInfo = Digit?.UserService?.getUser()?.info;
  const userInfoType = useMemo(() => (userInfo?.type === "CITIZEN" ? "citizen" : "employee"), [userInfo]);
  const userRoles = Digit?.UserService?.getUser?.()?.info?.roles || [];
  const styles = getStyles();
  const [entryDate, setEntryDate] = useState(new Date().setHours(0, 0, 0, 0));
  const [selectedDate, setSelectedDate] = useState(getCurrentDate());
  const [offSet, setOffset] = useState(0);
  const limit = 10;
  const emblemBigImageLink = window?.globalConfigs?.getConfig("EMBLEM_BIG");
  const onCourtsImageLink = window?.globalConfigs?.getConfig("ON_COURTS_LOGO");
  const [taskType, setTaskType] = useState({});

  const { data: diaryEntries, isLoading: isDiaryEntriesLoading, refetch: refetchDiaryEntries } = Digit.Hooks.dristi.useSearchADiaryService(
    {
      criteria: {
        tenantId: tenantId,
        judgeId: "super",
        date: entryDate,
      },
      pagination: {
        limit,
        offSet,
      },
    },
    {},
    `diary-entries-${entryDate}-${offSet}`,
    entryDate,
    Boolean(entryDate)
  );
  const handleDateChange = (e) => {
    setSelectedDate(e.target.value);
  };

  const handleGoClick = () => {
    const updatedDate = new Date(selectedDate).setHours(0, 0, 0, 0);
    setEntryDate(updatedDate);
  };
  const handleNext = () => {
    if (diaryEntries?.pagination?.totalCount > offSet + limit) {
      setOffset((prevOffset) => prevOffset + limit);
    }
  };

  const handlePrevious = () => {
    if (offSet > 0) {
      setOffset((prevOffset) => Math.max(prevOffset - limit, 0));
    }
  };

  const handleRowClick = (entry) => {
    if (entry?.referenceType === "Order") {
      history.push(
        `/${window?.contextPath}/${userInfoType}/orders/generate-orders?filingNumber=${entry?.additionalDetails?.filingNumber}&orderNumber=${entry?.referenceId}`,
        { diaryEntry: entry }
      );
    }
    if (entry?.referenceType === "Documents") {
      history.push(
        `/${window?.contextPath}/${userInfoType}/dristi/home/view-case?caseId=${entry?.additionalDetails?.caseId}&filingNumber=${entry?.additionalDetails?.filingNumber}&tab=Documents&artifactNumber=${entry?.referenceId}`,
        { diaryEntry: entry }
      );
    }
  };

  if (isDiaryEntriesLoading) {
    return <Loader />;
  }

  return (
    <div style={styles.container}>
      <div style={styles.centerPanel}>
        <div style={styles.title}>{t("SIGN_THE_A_DIARY")}</div>
        <div style={{ display: "flex", gap: "40px", marginTop: "10px", marginBottom: "20px" }}>
          <div style={{ marginTop: "10px", fontWeight: "bold" }}>{t("A_DIARY_DATED_HEADING")}</div>
          <TextInput
            className="field desktop-w-full"
            key={"entryDate"}
            type={"date"}
            onChange={handleDateChange}
            style={{ paddingRight: "3px" }}
            defaultValue={selectedDate}
            max={new Date().toISOString().split("T")[0]}
          />
          <Button label={t("GO")} variation={"primary"} style={styles.goButton} onButtonClick={handleGoClick} />
        </div>
        <div style={{ display: "flex", alignItems: "right", justifyContent: "space-between", marginBottom: "16px" }}>
          <div style={{ display: "flex", gap: "20px" }}>
            <img
              style={{ height: "100px" }}
              src={emblemBigImageLink || "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"}
              alt="mSeva"
            />
            <div style={{ alignContent: "end" }}>
              <h2 style={{ fontWeight: "bold", fontSize: "15px", margin: 0 }}>{"ജില്ലാ കോടതി കൊല്ലം"}</h2>
              <h2 style={{ fontWeight: "bold", fontSize: "25px", margin: 0 }}>{"District Court Kollam"}</h2>
            </div>
          </div>
          <img
            style={{ height: "100px" }}
            src={onCourtsImageLink || "https://cdn.jsdelivr.net/npm/@egovernments/digit-ui-css@1.0.7/img/m_seva_white_logo.png"}
            alt="mSeva"
          />
        </div>
        <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", marginTop: "50px" }}>{t("IN_THE_COURT_NAME")}</h2>
        <hr style={{ border: "1px solid black", width: "100%", margin: "30px 0" }} />
        <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", marginTop: "30px" }}>{t("IN_THE_COURT_OF_JUDGE_KOLLAM")}</h2>
        <h2 style={{ fontSize: "15px", fontWeight: "bold", textAlign: "center", margin: "30px 0px" }}>{`${t("A_DIARY_DATED")}: ${new Date(
          entryDate
        )?.toLocaleDateString("en-US")}`}</h2>
        <table style={{ width: "100%", marginTop: "20px", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ background: "#007E7E", color: "#FFF" }}>
              <th style={{ padding: "18px", border: "1px solid #000" }}>{t("S_NO")}</th>
              <th style={{ padding: "18px", border: "1px solid #000" }}>{t("CASE_TYPE_CASE_NUMBER_CASE_YEAR")}</th>
              <th style={{ padding: "18px", border: "1px solid #000" }}>{t("PROCEEDINGS_OR_BUSINESS_OF_DAY")}</th>
              <th style={{ padding: "18px", border: "1px solid #000" }}>{t("NEXT_HEARING_DATE")}</th>
            </tr>
          </thead>
          <tbody>
            {diaryEntries?.entries?.map((entry, index) => (
              <tr key={index} style={{ cursor: "pointer" }} onClick={() => handleRowClick(entry)}>
                <td style={{ padding: "18px", border: "1px solid #000" }}>{index + 1}</td>
                <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.caseNumber}</td>
                <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.businessOfDay}</td>
                <td style={{ padding: "18px", border: "1px solid #000" }}>{entry?.hearingDate || ""}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div style={{ display: "flex", justifyContent: "space-between", marginTop: "30px" }}>
          <button onClick={handlePrevious} disabled={offSet === 0} style={{ padding: "8px 12px", cursor: "pointer" }}>
            {t("PREVIOUS")}
          </button>
          <span>
            {diaryEntries?.pagination?.totalCount > 0 ? offSet + 1 : 0} - {Math.min(offSet + limit, diaryEntries?.pagination?.totalCount)} of{" "}
            {diaryEntries?.pagination?.totalCount}
          </span>
          <button
            onClick={handleNext}
            disabled={offSet + limit >= diaryEntries?.pagination?.totalCount}
            style={{ padding: "8px 12px", cursor: "pointer" }}
          >
            {t("NEXT")}
          </button>
        </div>
      </div>
      <div style={styles.rightPanel}>
        {
          <TasksComponent
            taskType={taskType}
            setTaskType={setTaskType}
            isLitigant={userRoles.includes("CITIZEN")}
            uuid={userInfo?.uuid}
            userInfoType={userInfoType}
            hideFilters={true}
          />
        }
      </div>
    </div>
  );
};

export default ADiaryPage;
