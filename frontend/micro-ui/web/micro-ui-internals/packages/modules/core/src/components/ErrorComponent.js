import React from "react";
import { useTranslation } from "react-i18next";

const getStyles = () => ({
  errorBoundary: {
    width: "100vw",
    height: "100vh",
    fontSize: "16px",
    fontFamily: "sans-serif",
    display: "flex",
    justifyContent: "flex-start",
    alignItems: "center",
    flexDirection: "column",
    paddingTop: "60px",
  },
  errorContainer: {
    display: "flex",
    width: "500px",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    textAlign: "center",
    marginTop: "20px",
  },
  image: {
    width: "40vw",
    marginBottom: "30px",
  },
  heading: {
    fontSize: "28px",
    fontWeight: "bold",
    color: "#00796b",
    marginBottom: "10px",
  },
  paragraph: {
    color: "#666666",
    fontSize: "16px",
    lineHeight: "1.5",
    textAlign: "center",
    margin: "0",
  },
  link: {
    color: "#0066cc",
    textDecoration: "none",
    cursor: "pointer",
  },
  button: {
    marginTop: "10px",
    height: "40px",
    width: "153px",
    borderRadius: "0px",
    padding: "8px 24px",
    color: "white",
    cursor: "pointer",
    backgroundColor: "#00796b",
    border: "none",
    fontSize: "16px",
  },
});

const ErrorConfig = {
  error: {
    imgUrl: `https://pucarfilestore.blob.core.windows.net/pucar-filestore/pg/pucar-assets/Frame.png`,
    infoMessage: "ERROR_MESSAGE",
    buttonInfo: "ACTION_TEST_HOME",
  },
  maintenance: {
    imgUrl: `https://s3.ap-south-1.amazonaws.com/egov-qa-assets/maintainence-image.png`,
    infoMessage: "CORE_UNDER_MAINTENANCE",
    buttonInfo: "ACTION_TEST_HOME",
  },
  notfound: {
    imgUrl: `https://s3.ap-south-1.amazonaws.com/egov-qa-assets/PageNotFound.png`,
    infoMessage: "MODULE_NOT_FOUND",
    buttonInfo: "ACTION_TEST_HOME",
  },
};

const errorData = {
  email: "oncourtskollam@keralacourts.in",
  mobile: "0474-2919099",
};

const ErrorComponent = (props) => {
  const { type = "error" } = Digit.Hooks.useQueryParams();
  const config = ErrorConfig[type];
  const { t } = useTranslation();

  const stateInfo = props.stateInfo;
  const styles = getStyles();

  return (
    <div style={styles.errorBoundary}>
      <div style={styles.errorContainer}>
        <img src={config.imgUrl} alt="error" style={styles.image} />
        <h1 style={styles.heading}>{t(config.infoMessage)}</h1>
        {type === "error" && (
          <p style={styles.paragraph}>
            {t("SOMETING_ERROR_HAPPENEND")}
            <br />
            <span style={{ whiteSpace: "nowrap" }}>
              {t("ERROR_CONTANCT")}{" "}
              <a
                href={`mailto:${errorData?.email}`}
                style={styles.link}
                onMouseOver={(e) => (e.target.style.textDecoration = "underline")}
                onMouseOut={(e) => (e.target.style.textDecoration = "none")}
              >
                {errorData?.email}
              </a>{" "}
              or{" "}
              <a
                href={`tel:${errorData?.mobile}`}
                style={{ ...styles.link, whiteSpace: "nowrap" }}
                onMouseOver={(e) => (e.target.style.textDecoration = "underline")}
                onMouseOut={(e) => (e.target.style.textDecoration = "none")}
              >
                {errorData?.mobile}
              </a>
            </span>
          </p>
        )}
        <button
          onClick={() => {
            props.goToHome();
          }}
          style={styles.button}
        >
          {t(config.buttonInfo)}
        </button>
      </div>
    </div>
  );
};

export default ErrorComponent;
