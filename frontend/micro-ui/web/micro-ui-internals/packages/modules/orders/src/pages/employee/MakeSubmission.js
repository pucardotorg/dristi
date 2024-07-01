import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { FormComposerV2, Header } from "@egovernments/digit-ui-react-components";
import { configs } from "../../configs/ordersCreateConfig";
import { transformCreateData } from "../../utils/createUtils";
import { CustomDeleteIcon } from "../../../../dristi/src/icons/svgIndex";
import OrderReviewModal from "../../pageComponents/OrderReviewModal";
import OrderSignatureModal from "../../pageComponents/OrderSignatureModal";
import OrderDeleteModal from "../../pageComponents/OrderDeleteModal";
import useSearchOrdersService from "../../hooks/orders/useSearchOrdersService";
import { ordersService } from "../../hooks/services";
import useSearchCaseService from "../../../../dristi/src/hooks/dristi/useSearchCaseService";
import { CaseWorkflowAction, CaseWorkflowState } from "../../utils/caseWorkflow";
import { Loader } from "@egovernments/digit-ui-components";
import OrderSucessModal from "../../pageComponents/OrderSucessModal";
import ReviewSubmissionModal from "../../pageComponents/ReviewSubmissionModal";

const fieldStyle = { marginRight: 0 };

const MakeSubmission = () => {
  const defaultValue = {};

  const { t } = useTranslation();
  const history = useHistory();
  const urlParams = new URLSearchParams(window.location.search);
  const filingNumber = urlParams.get("filingNumber");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedOrder, setSelectedOrder] = useState(0);
  const [deleteOrderIndex, setDeleteOrderIndex] = useState(null);
  const [showReviewModal, setShowReviewModal] = useState(true);
  const [showsignatureModal, setShowsignatureModal] = useState(null);
  const [showSignatureModal, setShowSignatureModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [formdata, setFormdata] = useState({});

  const handleSaveDraft = () => {};

  const handleBack = () => {};

  return (
    <div>
      {showReviewModal && (
        <ReviewSubmissionModal
          t={t}
          // order={currentOrder}
          setShowReviewModal={setShowReviewModal}
          setShowsignatureModal={setShowsignatureModal}
          handleBack={handleBack}
        />
      )}
    </div>
  );
};

export default MakeSubmission;
