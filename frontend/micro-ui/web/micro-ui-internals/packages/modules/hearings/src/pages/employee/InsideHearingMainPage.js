import React, { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, Link } from "react-router-dom";
import { useQuery, useQueryClient } from "react-query";
import { Header, ActionBar, SVG, SubmitBar, Card } from "@egovernments/digit-ui-react-components";
import { Button, TextArea } from "@egovernments/digit-ui-components";
import EvidenceHearingHeader from "./EvidenceHeader";
import HearingSideCard from "./HearingSideCard";
import debounce from 'lodash/debounce';

const fieldStyle = { marginRight: 0 };

const fetchLatestText = async () => {
  return localStorage.getItem("latest-text");
};

const InsideHearingMainPage = () => {
  const history = useHistory();
  const [immediateText, setImmediateText] = useState("this is good");
  const [delayedText, setDelayedText] = useState("");
  const [userRoles, setUserRoles] = useState([]);
  const textAreaRef = useRef(null);
  const queryClient = useQueryClient();

  useEffect(() => {
    const userDetails = JSON.parse(localStorage.getItem("user-info"));
    setUserRoles(userDetails.roles);
  }, []);

  const checkUserApproval = (userRole) => {
    return userRoles.some((role) => role.name === userRole);
  };

  const { data: latestText } = useQuery("latestText", fetchLatestText, {
    refetchInterval: 5000,
    enabled: !checkUserApproval("ADVOCATE_APPROVER"),
  });

  useEffect(() => {
    if (latestText) {
      setDelayedText(latestText);
    }
  }, [latestText]);

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}${path}`);
  };

  const updateText = debounce((newText) => {
    localStorage.setItem("latest-text", newText);
    setDelayedText(newText);
    queryClient.invalidateQueries("latestText");
  }, 5000);

  const handleChange = (e) => {
    const newText = e.target.value;
    setImmediateText(newText);
    updateText(newText);
  };

  return (
    <div style={{ display: "flex" }}>
      <Card>
        <div style={{ border: "1px", padding: "40px, 40px", gap: "32px" }}>
          <EvidenceHearingHeader></EvidenceHearingHeader>
        </div>
        <div style={{ padding: "40px, 40px", gap: "16px" }}>
          <div style={{ minWidth: "940px", minHeight: "453px", gap: "16px", border: "1px solid", marginTop: "2px" }}>
            {checkUserApproval("ADVOCATE_APPROVER") ? (
              <TextArea ref={textAreaRef} style={{ minWidth: "940px", minHeight: "453px" }} value={immediateText} onChange={handleChange} />
            ) : (
              <TextArea
                style={{ minWidth: "940px", minHeight: "453px", cursor: "default", backgroundColor: "#E8E8E8", color: "#3D3C3C" }}
                value={delayedText}
                readOnly
              />
            )}
          </div>
        </div>
      </Card>
      <Card>
        <HearingSideCard></HearingSideCard>
      </Card>
      <ActionBar>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            width: "100%",
          }}
        >
          <div
            style={{
              display: "flex",
              gap: "16px",
            }}
          >
            <button
              style={{
                border: "1px solid blue",
                backgroundColor: "#e6f0ff",
                color: "#1a73e8",
                fontWeight: "bold",
                padding: "10px 20px",
                borderRadius: "5px",
                cursor: "pointer",
                display: "inline-block",
                fontSize: "16px",
                width: "100%",
              }}
            >
              Attendance: <strong>03</strong>
            </button>
            <Button
              label={"Mark Attendance"}
              variation={"teritiary"}
              onClick={() => handleNavigate("/employee/hearings/mark-attendance")}
              style={{ width: "100%" }}
            />
          </div>
          <div
            style={{
              display: "flex",
              gap: "16px",
              width: "100%",
            }}
          >
            <Button
              label={"Adjourn Hearing"}
              variation={"secondary"}
              onClick={() => handleNavigate("/employee/hearings/adjourn-hearing")}
              style={{ width: "100%" }}
            />

            <Button
              label={"End Hearing"}
              variation={"primary"}
              onClick={() => handleNavigate("/employee/orders/orders-create?orderType=SUMMON")}
              style={{ width: "100%" }}
            />
          </div>
        </div>
      </ActionBar>
    </div>
  );
};

export default InsideHearingMainPage;
