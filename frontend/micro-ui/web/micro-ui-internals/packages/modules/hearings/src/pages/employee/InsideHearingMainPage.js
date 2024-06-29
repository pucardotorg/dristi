import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory, Link } from "react-router-dom";
import { Header, ActionBar, SVG, SubmitBar, Card } from "@egovernments/digit-ui-react-components";
import { Button, TextArea } from "@egovernments/digit-ui-components";
import EvidenceHearingHeader from "./EvidenceHeader";
import HearingSideCard from "./HearingSideCard";

const fieldStyle = { marginRight: 0 };

const InsideHearingMainPage = () => {

  const [isSignatureAdded, setIsSignatureAdded] = useState(false);
  const history = useHistory();

  const handleNavigate = (path) => {
    const contextPath = window?.contextPath || "";
    history.push(`/${contextPath}${path}`);
  };

  const handleClick = () => {
    setIsSignatureAdded(true);
  };

  return (
    <div style={{ display: "flex" }}>
      <Card>
        <EvidenceHearingHeader></EvidenceHearingHeader>
        <div style={{ padding: "20px 40px", minWidth: "940px", minHeight: "453px" }}>
          <TextArea style={{ minWidth: "940px", minHeight: "453px" }}></TextArea>
          <div>
            {isSignatureAdded ? (
              <div
                style={{
                  display: "flex",
                  marginTop: "20px",
                  alignItems: "center",
                  color: "#008000",
                  fontSize: "18px",
                  fontWeight: "500",
                }}
              >
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                  style={{
                    width: "20px",
                    height: "20px",
                    marginRight: "10px",
                  }}
                >
                  <circle cx="12" cy="12" r="12" fill="#006400" />
                  <path d="M7 12L10 15L17 8" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
                Signature Added
              </div>
            ) : (
              <button
                onClick={handleClick}
                style={{
                  backgroundColor: "#ffffff",
                  color: "#c4c4c4",
                  border: "1px solid #c4c4c4",
                  padding: "10px 20px",
                  marginTop: "20px",
                  fontSize: "16px",
                  fontWeight: "900",
                  cursor: "pointer",
                  boxShadow: "none",
                  transition: "all 0.3s ease",
                }}
              >
                <strong>Add Signature</strong>
              </button>
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
