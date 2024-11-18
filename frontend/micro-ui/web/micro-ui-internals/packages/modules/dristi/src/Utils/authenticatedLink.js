import React from "react";
import axios from "axios";

const AuthenticatedLink = ({ t, uri, displayFilename = false, pdf = false }) => {
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
          const blob = new Blob([response.data], { type: pdf ? "application/pdf" : "application/octet-stream" });
          const mimeType = response.data.type || "application/octet-stream";
          const extension = mimeType.includes("/") ? mimeType.split("/")[1] : "bin";
          const blobUrl = URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = blobUrl;
          link.download = `downloadedFile.${extension}`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          URL.revokeObjectURL(blobUrl);
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
        // width: 250,
        maxWidth: "250px",
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
