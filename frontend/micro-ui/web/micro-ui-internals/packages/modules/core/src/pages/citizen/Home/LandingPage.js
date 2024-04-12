import React, { useMemo } from "react";
import { Loader, ButtonSelector, Card, RegisterIcon, LoginIcon, Button } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const LandingPage = () => {
  const { t } = useTranslation();
  const history = useHistory();

  const { data: { languages, stateInfo } = {}, isLoading } = Digit.Hooks.useStore.getInitData();
  const selectedLanguage = Digit.StoreData.getCurrentLanguage();

  const texts = useMemo(
    () => ({
      header: t("CS_LANDING_PAGE"),
      submitBarLabel: t("CORE_COMMON_CONTINUE"),
    }),
    [t]
  );

  const RadioButtonProps = useMemo(
    () => ({
      options: languages,
      optionsKey: "label",
      additionalWrapperClass: "reverse-radio-selection-wrapper",
      onSelect: (language) => Digit.LocalizationService.changeLanguage(language.value, stateInfo.code),
      selectedOption: languages?.filter((i) => i.value === selectedLanguage)[0],
    }),
    [selectedLanguage, languages]
  );

  function onSubmit() {
    history.push(`/digit-ui/citizen/select-location`);
  }

  return isLoading ? (
    <Loader />
  ) : (
    <div className="selection-card-wrapper" style={{ marginTop: "80px" }}>
      <Card
        style={{
          height: "75vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          gap: "40px",
          minWidth: "100%",
        }}
      >
        <Button
          onButtonClick={() => {
            history.push(`/digit-ui/citizen/login`);
          }}
          label={t("CS_COMMON_REGISTER")}
          style={{
            flex: 1,
            maxHeight: "7vh",
            width: "40vw",
          }}
        >
          <RegisterIcon />
        </Button>
        <Button
          label={t("CS_COMMON_LOGIN")}
          style={{
            flex: 1,
            maxHeight: "7vh",
            width: "40vw",
          }}
        >
          <LoginIcon />
        </Button>
      </Card>
    </div>
  );
};

export default LandingPage;
