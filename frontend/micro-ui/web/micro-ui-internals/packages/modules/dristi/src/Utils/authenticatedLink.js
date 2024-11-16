import React from "react";
import axios from "axios";

const AuthenticatedLink = ({ t, uri, displayFilename = false }) => {
  const handleClick = (e) => {
    e.preventDefault();

    const authToken = localStorage.getItem("token");
    axios
      .get(uri, {
        headers: {
          "auth-token": `${authToken}`,
        },
        responseType: "blob",
      })
      .then((response) => {
        if (response.status === 200) {
          const blob = new Blob([response.data], { type: "application/octet-stream" });
          const blobUrl = URL.createObjectURL(blob);

          window.open(blobUrl, "_blank");
        } else {
          console.error("Failed to fetch the PDF:", response.statusText);
        }
      })
      .catch((error) => {
        console.error("Error during the API request:", error);
      });
  };

  return (
    <span
      onClick={handleClick}
      style={{
        display: "flex",
        color: "#007e7e",
        width: 250,
        whiteSpace: "nowrap",
        overflow: "hidden",
        textOverflow: "ellipsis",
        cursor: "pointer",
        textDecoration: "underline",
      }}
    >
      {displayFilename ? t(displayFilename) : t("CS_CLICK_TO_DOWNLOAD")}
    </span>
  );
};

export default AuthenticatedLink;
