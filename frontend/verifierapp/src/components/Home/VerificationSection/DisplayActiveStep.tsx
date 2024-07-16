import React, { useState } from 'react';
import ScanQrCode from "./ScanQrCode";
import Verification from "./Verification";
import Result from "./Result";
import { VcStatus } from "../../../types/data-types";
import { useActiveStepContext } from "../../../pages/Home";
import { VerificationSteps } from "../../../utils/config";

const DisplayActiveStep = () => {
    const { getActiveStep, setActiveStep } = useActiveStepContext();
    const activeStep = getActiveStep();

    const [vc, setVc] = useState(null);
    const [vcStatus, setVcStatus] = useState({ status: "Verifying", checks: [] } as VcStatus);

    const goToShowingResult = (vcData: any, vcStatusData: VcStatus) => {
        setActiveStep(VerificationSteps.DisplayResult)
        setVcStatus(vcStatusData);
        setVc(vcData);
    }

    switch (activeStep) {
        case VerificationSteps.ScanQrCodePrompt:
            return (<ScanQrCode />);
        case VerificationSteps.ActivateCamera:
        case VerificationSteps.Verifying:
            return (<Verification showResult={goToShowingResult} />);
        case VerificationSteps.DisplayResult:
            return (<Result setActiveStep={setActiveStep} vc={vc} vcStatus={vcStatus} />);
        default:
            return (<></>);
    }
}

export default DisplayActiveStep;