import { CardText, Modal, Toast } from "@egovernments/digit-ui-react-components";
import React, { useEffect, useMemo, useState } from "react";
import { FormComposerV2 } from "@egovernments/digit-ui-react-components";

import { modalConfig } from "../../citizen/FileCase/Config/admissionActionConfig";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";
import CustomSubmitModal from "../../../components/CustomSubmitModal";
import ScheduleAdmission from "./ScheduleAdmission";
import SelectParticipant from "./SelectParticipant";
import CustomCalendar from "../../../components/CustomCalendar";
import { WhiteRightArrow } from "../../../icons/svgIndex";
import { formatDateInMonth } from "../../../Utils";
import { DRISTIService } from "../../../services";
import ScheduleHearing from "./ScheduleHearingModal";
import { OrderWorkflowAction } from "../../../Utils/orderWorkflow";
import { Urls } from "../../../hooks";

const Heading = (props) => {
  return <h1 className="heading-m">{props.label}</h1>;
};

const Close = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g clip-path="url(#clip0_4124_3214)">
      <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="#0A0A0A" />
    </g>
    <defs>
      <clipPath id="clip0_4124_3214">
        <rect width="24" height="24" fill="white" />
      </clipPath>
    </defs>
  </svg>
);

const CloseBtn = (props) => {
  return (
    <div style={{ padding: "10px", cursor: "pointer" }} onClick={props.onClick}>
      <Close />
    </div>
  );
};
function AdmissionActionModal({
  t,
  setShowModal,
  setSubmitModalInfo,
  submitModalInfo,
  modalInfo,
  setModalInfo,
  path,
  handleSendCaseBack,
  handleAdmitCase,
  handleScheduleCase,
  updatedConfig,
  tenantId,
  // hearingDetails,
  handleScheduleNextHearing,
  disabled,
  filingNumber,
  isCaseAdmitted = false,
  createAdmissionOrder = false,
  caseAdmittedSubmit = () => {},
  caseAdmitLoader,
  caseDetails,
  scheduleHearing = false,
}) {
  const history = useHistory();
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [label, setLabel] = useState(false);

  const closeToast = () => {
    setShowErrorToast(false);
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      closeToast();
    }, 2000);

    return () => clearTimeout(timer);
  }, [closeToast]);

  const stepItems = useMemo(() => {
    return modalConfig.map((step) => {
      const texts = {};
      for (const key in step?.texts) {
        texts[key] = t(step?.texts[key]);
      }
      return { ...step, texts };
    });
  }, [t]);

  const [scheduleHearingParams, setScheduleHearingParam] = useState(!isCaseAdmitted ? { purpose: t("ADMISSION") } : {});
  const isGenerateOrderDisabled = useMemo(() => Boolean(!scheduleHearingParams?.purpose || !scheduleHearingParams?.date), [scheduleHearingParams]);
  console.log("first", scheduleHearingParams, isGenerateOrderDisabled);

  const onSubmit = (props, wordLimit) => {
    const words = props?.commentForLitigant?.trim()?.split(/\s+/);
    if (!props?.commentForLitigant) {
      setShowErrorToast(true);
      setLabel("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS");
      return;
    }
    if (words?.length >= wordLimit) {
      setShowErrorToast(true);
      setLabel(`ES_WORD_COUNT_LIMIT ${wordLimit}`);
    } else {
      handleSendCaseBack(props);
    }
  };
  const showSuccessModal = (modalInfo) => {
    if (!modalInfo) return false;
    const { page, type } = modalInfo;
    return (page === 1 && (type === "admitCase" || type === "sendCaseBack")) || (page === 2 && type === "schedule");
  };
  const [selectedCustomDate, setSelectedCustomDate] = useState(new Date());

  const handleSelect = (date) => {
    setScheduleHearingParam({ ...scheduleHearingParams, date: formatDateInMonth(date) });
    setSelectedCustomDate(date);
  };
  const onCalendarConfirm = () => {
    setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: true });
    setSelectedChip(null);
  };
  const [selectedChip, setSelectedChip] = React.useState(null);

  const setPurposeValue = (value, input) => {
    setScheduleHearingParam({
      ...scheduleHearingParams,
      purpose: isCaseAdmitted || createAdmissionOrder ? value : value.code,
    });
  };

  const showCustomDateModal = () => {
    setModalInfo({ ...modalInfo, showDate: true });
  };
  const [selectedValues, setSelectedValues] = useState({});

  const handleInputChange = (values) => {
    setSelectedValues(values);
  };
  const handleClickDate = (label) => {
    const newSelectedChip = selectedChip === label ? null : label;
    setSelectedChip(newSelectedChip);
    setScheduleHearingParam({
      ...scheduleHearingParams,
      date: newSelectedChip,
    });
  };

  const handleCloseCustomDate = () => {
    setModalInfo({ ...modalInfo, page: 0, showDate: false, showCustomDate: false });
    setScheduleHearingParam({
      ...scheduleHearingParams,
      date: "",
    });
  };

  const handleNextCase = () => {
    DRISTIService.searchCaseService(
      {
        criteria: [
          {
            status: ["PENDING_ADMISSION"],
          },
        ],
        tenantId,
      },
      {}
    )
      .then((res) => {
        if (res?.criteria?.[0]?.responseList?.[0]?.id) {
          history.push(
            `/${window?.contextPath}/employee/dristi/admission?filingNumber=${res?.criteria?.[0]?.responseList?.[0]?.filingNumber}&caseId=${res?.criteria?.[0]?.responseList?.[0]?.id}`
          );
        } else {
          history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
        }
      })
      .catch(() => {
        history.push(`/${window?.contextPath}/employee/home/home-pending-task`);
      });
  };

  const handleIssueNotice = async (hearingDate, hearingNumber) => {
    try {
      const orderBody = {
        createdDate: new Date().getTime(),
        tenantId,
        cnrNumber: caseDetails?.cnrNumber,
        filingNumber,
        statuteSection: {
          tenantId,
        },
        orderType: "NOTICE",
        status: "",
        isActive: true,
        workflow: {
          action: OrderWorkflowAction.SAVE_DRAFT,
          comments: "Creating order",
          assignes: null,
          rating: null,
          documents: [{}],
        },
        documents: [],
        ...(hearingNumber && { hearingNumber }),
        additionalDetails: {
          formdata: {
            orderType: {
              code: "NOTICE",
              type: "NOTICE",
              name: "ORDER_TYPE_NOTICE",
            },
            hearingDate,
          },
        },
      };
      DRISTIService.customApiService(Urls.dristi.ordersCreate, orderBody, { tenantId })
        .then((res) => {
          history.push(`/digit-ui/employee/orders/generate-orders?filingNumber=${caseDetails?.filingNumber}&orderNumber=${res.order.orderNumber}`, {
            caseId: caseDetails?.id,
            tab: "Orders",
          });
        })
        .catch();
    } catch (error) {}
  };

  return (
    <React.Fragment>
      {modalInfo?.page === 0 && modalInfo?.type === "sendCaseBack" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[0].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popmoduleClassName={"send-case-back-modal"}
        >
          <FormComposerV2
            config={[stepItems[0]]}
            t={t}
            noBoxShadow
            inline={false}
            label={t("CORE_COMMON_SEND")}
            onSecondayActionClick={() => {
              setShowModal(false);
            }}
            // onFormValueChange={onFormValueChange}
            headingStyle={{ textAlign: "center" }}
            cardStyle={{ minWidth: "100%", padding: 20, display: "flex", flexDirection: "column", alignItems: "center" }}
            onSubmit={(props) => onSubmit(props, stepItems[0]?.wordCount)}
            submitInForm
            // className={"registration-select-name"}
            secondaryActionLabel={t("CORE_LOGOUT_CANCEL")}
            buttonStyle={{ alignSelf: "center", minWidth: "50%" }}
            actionClassName="e-filing-action-bar"
          ></FormComposerV2>
          {showErrorToast && <Toast error={true} label={t(label)} isDleteBtn={true} onClose={closeToast} />}
        </Modal>
      )}
      {modalInfo?.page === 0 && modalInfo?.type === "admitCase" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[1].headModal)} />}
          actionSaveLabel={t(stepItems[1]?.submitText)}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          isDisabled={caseAdmitLoader}
          actionSaveOnSubmit={(props) => handleAdmitCase(props)}
        >
          <CardText>{t(stepItems[1]?.text)}</CardText>
        </Modal>
      )}

      {modalInfo?.page === 0 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={scheduleHearing ? t("CS_SCHEDULE_HEARING") : t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popupStyles={{ width: "917px" }}
        >
          <ScheduleAdmission
            config={stepItems[2]}
            t={t}
            setShowModal={setShowModal}
            setModalInfo={setModalInfo}
            modalInfo={modalInfo}
            selectedChip={selectedChip}
            setSelectedChip={setSelectedChip}
            showCustomDateModal={showCustomDateModal}
            setPurposeValue={setPurposeValue}
            scheduleHearingParams={scheduleHearingParams}
            setScheduleHearingParam={setScheduleHearingParam}
            submitModalInfo={submitModalInfo}
            handleClickDate={handleClickDate}
            disabled={disabled}
            isCaseAdmitted={isCaseAdmitted}
            createAdmissionOrder={createAdmissionOrder}
            isSubmitBarDisabled={isGenerateOrderDisabled}
            caseAdmittedSubmit={caseAdmittedSubmit}
          />
        </Modal>
      )}
      {modalInfo?.page === 1 && modalInfo?.type === "schedule" && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[2].headModal)} />}
          headerBarEnd={<CloseBtn onClick={() => setShowModal(false)} />}
          hideSubmit={true}
          popmoduleClassName={"schedule-admission-select-participants-modal"}
        >
          <SelectParticipant
            config={updatedConfig}
            setShowModal={setShowModal}
            modalInfo={modalInfo}
            setModalInfo={setModalInfo}
            scheduleHearingParams={scheduleHearingParams}
            setScheduleHearingParam={setScheduleHearingParam}
            handleInputChange={handleInputChange}
            selectedValues={selectedValues}
            setSelectedValues={setSelectedValues}
            handleScheduleCase={handleScheduleCase}
            setShowErrorToast={setShowErrorToast}
            t={t}
          />
          {showErrorToast && <Toast error={true} label={t("ES_COMMON_PLEASE_ENTER_ALL_MANDATORY_FIELDS")} isDleteBtn={true} onClose={closeToast} />}
        </Modal>
      )}
      {modalInfo?.showDate && (
        <Modal
          headerBarMain={<Heading label={t(stepItems[3].headModal)} />}
          headerBarEnd={<CloseBtn onClick={handleCloseCustomDate} />}
          // actionSaveLabel={t("CS_COMMON_CONFIRM")}
          hideSubmit={true}
          popmoduleClassName={"custom-date-selector-modal"}

          // actionSaveOnSubmit={onSelect}
        >
          <CustomCalendar
            config={stepItems[3]}
            minDate={new Date()}
            t={t}
            onCalendarConfirm={onCalendarConfirm}
            handleSelect={handleSelect}
            selectedCustomDate={selectedCustomDate}
            tenantId={tenantId}
          />
        </Modal>
      )}
      {showSuccessModal(modalInfo) && (
        <Modal
          actionSaveLabel={
            <div>
              {t(submitModalInfo?.nextButtonText)}
              {submitModalInfo?.isArrow && <WhiteRightArrow />}
            </div>
          }
          actionCancelLabel={t(submitModalInfo?.backButtonText)}
          actionCancelOnSubmit={() => {
            history.push(`/${window?.contextPath}/employee`);
          }}
          actionSaveOnSubmit={() => {
            if (submitModalInfo?.nextButtonText === "SCHEDULE_NEXT_HEARING") {
              // handleScheduleNextHearing();
              setModalInfo({ page: 3, type: "schedule" });
            } else if (submitModalInfo?.nextButtonText === "ISSUE_NOTICE") {
              handleIssueNotice();
            } else {
              handleNextCase();
            }
          }}
          className="case-types"
          formId="modal-action"
        >
          <CustomSubmitModal submitModalInfo={submitModalInfo} t={t} />
        </Modal>
      )}
      {modalInfo?.page === 3 && modalInfo?.type === "schedule" && (
        <ScheduleHearing
          config={stepItems[2]}
          t={t}
          setShowModal={setShowModal}
          setModalInfo={setModalInfo}
          modalInfo={modalInfo}
          selectedChip={selectedChip}
          setSelectedChip={setSelectedChip}
          showCustomDateModal={showCustomDateModal}
          setPurposeValue={setPurposeValue}
          scheduleHearingParams={scheduleHearingParams}
          setScheduleHearingParam={setScheduleHearingParam}
          submitModalInfo={submitModalInfo}
          handleClickDate={handleClickDate}
          disabled={disabled}
          isCaseAdmitted={isCaseAdmitted}
          isSubmitBarDisabled={isGenerateOrderDisabled}
          caseAdmittedSubmit={caseAdmittedSubmit}
          oldCaseDetails={caseDetails}
        />
      )}
      {modalInfo?.page === 4 && (
        <Modal
          actionSaveLabel={
            <div>
              {t(submitModalInfo?.nextButtonText)}
              {submitModalInfo?.isArrow && <WhiteRightArrow />}
            </div>
          }
          actionCancelLabel={t(submitModalInfo?.backButtonText)}
          actionCancelOnSubmit={() => {
            history.push(`/${window?.contextPath}/employee`);
          }}
          actionSaveOnSubmit={() => {
            if (submitModalInfo?.nextButtonText === "SCHEDULE_NEXT_HEARING") {
              handleScheduleNextHearing();
            } else if (submitModalInfo?.nextButtonText === "CS_SCHEDULE_ADMISSION_HEARING") {
              setModalInfo({ ...modalInfo, page: 0, type: "schedule" });
            } else {
              handleNextCase();
            }
          }}
          className="case-types"
          formId="modal-action"
        >
          <CustomSubmitModal submitModalInfo={submitModalInfo} t={t} />
        </Modal>
      )}
    </React.Fragment>
  );
}

export default AdmissionActionModal;
