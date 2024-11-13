import { Button } from "@egovernments/digit-ui-components";
import React from "react";
import { useTranslation } from "react-i18next";

const displayAttendeesLabelStyle = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 700,
  lineHeight: "18.75px",
  textAlign: "left",
};

const displayAttendeesValueStyle = {
  fontFamily: "Roboto",
  fontSize: "16px",
  fontWeight: 400,
  lineHeight: "18.75px",
  textAlign: "left",
};

const DisplayAttendees = ({ partiesToAttend, onlineAttendees = [], offlineAttendees = [], handleAttendees, handleModal }) => {
  const { t } = useTranslation();

  return (
    <div>
      <div style={{ textAlign: "left", fontSize: "24px", backgroundColor: "#F7F5F3", padding: "16px 24px" }}>
        <div style={{ display: "flex", gap: "16px" }}>
          <span className="display-attendees-label" style={displayAttendeesLabelStyle}>
            {t("PARTIES_TO_ATTEND")}
          </span>
          <span className="display-attendees-value" style={displayAttendeesValueStyle}>
            {partiesToAttend}
          </span>
        </div>
        <div style={{ display: "flex", gap: "16px" }}>
          <span className="display-attendees-label" style={displayAttendeesLabelStyle}>
            {t("PARTIES_IN_ATTENDANCE_ONLINE")}
          </span>
          <span className="display-attendees-value" style={displayAttendeesValueStyle}>
            {onlineAttendees.length}
          </span>
        </div>
        <div style={{ display: "flex", gap: "16px" }}>
          <span className="display-attendees-label" style={displayAttendeesLabelStyle}>
            {t("PARTIES_IN_ATTENDANCE_OFFLINE")}
          </span>
          <span className="display-attendees-value" style={displayAttendeesValueStyle}>
            {offlineAttendees.length}
          </span>
        </div>
      </div>
      {onlineAttendees.length !== 0 && (
        <div style={{ borderRadius: "4px 0px 0px 0px", border: "1px solid #E8E8E8", marginTop: "12px", padding: "4px" }}>
          {t("ONLINE_ATTENDEES")}
          <hr style={{ border: "none", borderTop: "2px solid #E8E8E8", margin: "12px 0" }} />
          <ul>
            {onlineAttendees.map((attendee) => (
              <li key={attendee.individualId || attendee.name}>
                {attendee.name} - {attendee.type}
              </li>
            ))}
          </ul>
        </div>
      )}
      {offlineAttendees.length !== 0 && (
        <div style={{ borderRadius: "4px 0px 0px 0px", border: "1px solid #E8E8E8", marginTop: "12px", padding: "4px" }}>
          {t("OFFLINE_ATTENDEES")}
          <hr style={{ border: "none", borderTop: "2px solid #E8E8E8", margin: "12px 0" }} />
          <ul>
            {offlineAttendees.map((attendee) => (
              <li key={attendee.individualId || attendee.name}>
                {attendee.name} - {attendee.type}
              </li>
            ))}
          </ul>
        </div>
      )}
      {onlineAttendees.length + offlineAttendees.length === 0 && (
        <div style={{ textAlign: "left", marginTop: "12px" }}>{t("NO_MARKED_ATTENDEES")}</div>
      )}
      <Button
        label={t("ADD_ATTENDEES")}
        onClick={handleAttendees}
        variation={"teritiary"}
        style={{
          borderColor: "#007E7E",
          minWidth: "50px",
          height: "40px",
          backgroundColor: "#fff",
        }}
        textStyles={{
          fontFamily: "Roboto",
          fontSize: "16px",
          fontWeight: 700,
          lineHeight: "18.75px",
          textAlign: "start",
          color: "#007E7E",
        }}
      />
      <div style={{ display: "flex", justifyContent: "flex-end", gap: "16px", padding: "4px" }}>
        <Button
          variation={"teritiary"}
          label={t("SHARE_LINK")}
          style={{
            border: "1px solid",
            borderColor: "#007E7E",
            height: "40px",
            minWidth: "50px",
            backgroundColor: "#fff",
          }}
          textStyles={{
            fontFamily: "Roboto",
            fontSize: "16px",
            fontWeight: 700,
            lineHeight: "18.75px",
            textAlign: "center",
            color: "#007E7E",
          }}
          icon={"Share"}
          iconFill={"#007E7E"}
          className={"take-action-btn-class"}
        ></Button>
        <Button
          label={t("ADD_ATTENDANCE_DONE")}
          onClick={handleModal}
          style={{
            height: "40px",
            minWidth: "50px",
          }}
          textStyles={{
            textAlign: "center",
            margin: 0,
          }}
        ></Button>
      </div>
    </div>
  );
};

export default DisplayAttendees;
