import React from "react";
import PropTypes from "prop-types";
import { ActionBar, Card, CardCaption, CardHeader, CardText, LinkButton, SubmitBar } from "@egovernments/digit-ui-react-components";

const InputCard = ({
  t,
  children,
  texts = {},
  submit = false,
  inputs = [],
  inputRef,
  onNext,
  onSkip,
  isDisable,
  onAdd,
  isMultipleAllow = false,
  cardStyle = {},
}) => {
  const isMobile = window.Digit.Utils.browser.isMobile();
  // TODO: inputs handle
  return (
    <Card style={cardStyle}>
      {texts.headerCaption && <CardCaption>{t(texts.headerCaption)}</CardCaption>}
      {texts?.header && <CardHeader>{t(texts.header)}</CardHeader>}
      {texts?.cardText && <CardText>{t(texts.cardText)}</CardText>}
      {children}
      {texts.submitBarLabel && texts.submitInForm ? (
        <SubmitBar disabled={isDisable} submit={submit} label={t(texts.submitBarLabel)} onSubmit={onNext} />
      ) : null}
      {texts.submitBarLabel && !texts.submitInForm ? (
        <ActionBar>
          <SubmitBar disabled={isDisable} submit={submit} label={t(texts.submitBarLabel)} onSubmit={onNext} />
        </ActionBar>
      ) : null}
      {texts.skipLabel ? <CardText style={{ marginTop: "10px", textAlign: isMobile ? "center" : "left" }}> {t(texts.skipLabel)} </CardText> : null}
      {texts.skipText ? <LinkButton label={t(texts.skipText)} onClick={onSkip} /> : null}
      {isMultipleAllow && texts.addMultipleText ? <LinkButton label={t(texts.addMultipleText)} onClick={onAdd} /> : null}
    </Card>
  );
};

InputCard.propTypes = {
  text: PropTypes.object,
  submit: PropTypes.bool,
  onNext: PropTypes.func,
  onSkip: PropTypes.func,
  onAdd: PropTypes.func,
  t: PropTypes.func,
};

InputCard.defaultProps = {
  texts: {},
  submit: false,
  onNext: undefined,
  onSkip: undefined,
  onAdd: undefined,
  t: (value) => value,
};

export default InputCard;
