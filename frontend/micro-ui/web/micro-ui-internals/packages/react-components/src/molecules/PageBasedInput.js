import React from "react";
import Card from "../atoms/Card";
import SubmitBar from "../atoms/SubmitBar";

const PageBasedInput = ({ style, children, texts, onSubmit }) => {
  return (
    <div style={{ ...(style ? style.pageStyle : {}) }} className="PageBasedInputWrapper PageBased">
      <Card style={{ ...(style ? style.cardStyle : {}) }}>
        {children}
        <SubmitBar className="SubmitBarInCardInDesktopView" label={texts.submitBarLabel} onSubmit={onSubmit} />
      </Card>
      <div className="SubmitBar">
        <SubmitBar label={texts.submitBarLabel} onSubmit={onSubmit} />
      </div>
    </div>
  );
};

export default PageBasedInput;
