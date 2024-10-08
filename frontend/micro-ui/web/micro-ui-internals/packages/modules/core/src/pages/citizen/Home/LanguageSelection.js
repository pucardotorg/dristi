import React, { useMemo } from "react";
import { PageBasedInput, Loader, RadioButtons, CardHeader } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";

const getLanguageSelectionStyles = () => {
  const style = {
    pageStyle: {
      display: "flex",
      justifyContent: "center",
      marginRight: "40px",
    },
    cardStyle: {
      width: "100vw",
    },
  };
  return style;
};

const LanguageSelection = () => {
  const { t } = useTranslation();
  const history = useHistory();

  const { data: { languages, stateInfo } = {}, isLoading } = Digit.Hooks.useStore.getInitData();
  const selectedLanguage = Digit.StoreData.getCurrentLanguage();

  const texts = useMemo(
    () => ({
      header: t("CS_COMMON_CHOOSE_LANGUAGE"),
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
    history.push(`/${window?.contextPath}/citizen/dristi/landing-page`);
  }

  return isLoading ? (
    <Loader />
  ) : (
    <div className="selection-card-wrapper langauge-select-wrapper">
      <PageBasedInput style={getLanguageSelectionStyles()} texts={texts} onSubmit={onSubmit}>
        <CardHeader styles={{ margin: 0, textAlign: "center", fontSize: "44px", letterSpacing: 0 }}>{t("CS_COMMON_CHOOSE_LANGUAGE")}</CardHeader>
        <CardHeader styles={{ textAlign: "center", fontSize: "20px", letterSpacing: 0 }}>{t("CS_COMMON_CHOOSE_LANGUAGE_ML")}</CardHeader>
        <RadioButtons {...RadioButtonProps} />
      </PageBasedInput>
    </div>
  );
};

export default LanguageSelection;
