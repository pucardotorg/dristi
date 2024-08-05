import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { SVG } from "@egovernments/digit-ui-components";

const DisplayAttendees = ({ partiesToAttend, onlineAttendees = [], offlineAttendees = [], handleAttendees, handleModal }) => {
  const { t } = useTranslation();

  return (
    <div>
      <div style={{ textAlign: "left", fontSize: "24px", backgroundColor: "#F7F5F3", padding: "16px 24px" }}>
        <div style={{ display: "flex", gap: "16px" }}>
          <span>{t("PARTIES_TO_ATTEND")}</span>
          <span>{partiesToAttend}</span>
        </div>
        <div style={{ display: "flex", gap: "16px" }}>
          <span>{t("PARTIES_IN_ATTENDANCE_ONLINE")}</span>
          <span>{onlineAttendees.length}</span>
        </div>
        <div style={{ display: "flex", gap: "16px" }}>
          <span>{t("PARTIES_IN_ATTENDANCE_OFFLINE")}</span>
          <span>{offlineAttendees.length}</span>
        </div>
      </div>
      {onlineAttendees.length !== 0 && (
        <div style={{ borderRadius: "4px 0px 0px 0px", border: "1px solid #E8E8E8", marginTop: "12px", padding: "4px" }}>
          {t("ONLINE_ATTENDEES")}
          <hr style={{ border: "none", borderTop: "2px solid #E8E8E8", margin: "12px 0" }} />
          <ul>
            {onlineAttendees.map((attendee) => (
              <li key={attendee.individualId}>{attendee.name} - {attendee.type}</li>
            ))}
          </ul>
        </div>
      )}
      {offlineAttendees.length !== 0 && (
        <div style={{ border: "1px solid #E8E8E8", marginTop: "12px", height: "100px" }}>
          {t("OFFLINE_ATTENDEES")}
          <hr style={{ border: "none", borderTop: "2px solid #E8E8E8", margin: "12px 0" }} />
          <ul>
            {offlineAttendees.map((attendee) => (
              <li key={attendee.individualId}>
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
        onButtonClick={handleAttendees}
        variation={"teritiary"}
        style={{ border: "none", marginTop: "10px", color: "#rgba(0, 126, 126, 1)" }}
      />
      <div style={{ display: "flex", justifyContent: "flex-end", gap: "16px", padding: "4px" }}>
        <Button variation={"teritiary"} label={t("SHARE_LINK")} icon={<SVG.Share />} iconFill={"#007E7E"}></Button>
        <Button label={"Done"} onButtonClick={handleModal}></Button>
      </div>
    </div>
  );
};

export default DisplayAttendees;
