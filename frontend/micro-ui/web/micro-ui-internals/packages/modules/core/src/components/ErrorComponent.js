import React from "react";
import { useTranslation } from "react-i18next";

const getStyles = () => ({
  errorBoundary: {
    width: "100vw",
    height: "calc(100vh - 84px)",
    fontSize: "16px",
    fontFamily: "sans-serif",
    display: "flex",
    justifyContent: "flex-start",
    alignItems: "center",
    flexDirection: "column",
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
    height: "50vh",
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
    boxShadow: "none",
    backgroundColor: "transparent",
    border: "none",
    borderBottom: "3px solid black",
  },
  buttonTextStyle: {
    fontFamily: "Raleway",
    fontSize: "29.71px",
    fontWeight: 400,
    lineHeight: "34.88px",
    color: "#3A3A3A",
  },
});

const LeftBackArrow = () => (
  <svg width="33" height="20" viewBox="0 0 33 20" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path
      d="M3.05469 6.50713L25.8915 6.50713C28.7354 6.50713 31.0408 8.81253 31.0408 11.6564L31.0408 13.5453C31.0408 16.3892 28.7354 18.6946 25.8915 18.6946L8.01997 18.6946"
      stroke="#3A3A3A"
      stroke-width="2.5769"
      stroke-linejoin="round"
    />
    <path
      d="M7.57031 1.09025C5.45497 3.20559 4.26899 4.39158 2.15365 6.50692L7.57031 11.9236"
      stroke="#3A3A3A"
      stroke-width="2.5769"
      stroke-linejoin="round"
    />
  </svg>
);

const ErrorConfig = {
  error: {
    imgUrl: window?.globalConfigs?.getConfig("ERROR_IMAGE"),
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
  const CustomButton = window?.Digit?.ComponentRegistryService?.getComponent("CustomButton");

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
        <CustomButton
          onButtonClick={() => {
            props.goToHome();
          }}
          icon={<LeftBackArrow />}
          label={t(config.buttonInfo)}
          style={styles.button}
          textStyles={styles.buttonTextStyle}
        />
      </div>
    </div>
  );
};

export default ErrorComponent;
