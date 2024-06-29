import React, { useState } from "react";
import PreHearingModal from "./PreHearingModal";
import Button from "../../../dristi/src/components/Button";

function TemporaryButton() {
  const [showPreHearingModal, setShowPreHearingModal] = useState(false);

  const handleButtonClick = () => {
    setShowPreHearingModal(true);
  };

  const handleCloseModal = () => {
    setShowPreHearingModal(false);
  };

  return (
    <div>
      <Button style={{ width: "900px" }} onButtonClick={handleButtonClick} label="Show Pre Hearing Modal" variation={"secondary"} />
      {showPreHearingModal && <PreHearingModal onCancel={handleCloseModal} onSubmit={handleCloseModal} />}
    </div>
  );
}

export default TemporaryButton;
