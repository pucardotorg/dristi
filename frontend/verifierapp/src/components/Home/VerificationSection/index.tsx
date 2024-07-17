import React, { useCallback, useState } from 'react';
import ScanQrCode from "./ScanQrCode";
import Verification from "./Verification";
import Result from "./Result";
import { Vc, VcStatus } from "../../../types/data-types";
import { useActiveStepContext } from "../../../pages/Home";
import { VerificationSteps } from "../../../utils/config";

export default function() {
    const { activeStep, setActiveStep } = useActiveStepContext();

    const [vc, setVc] = useState<Vc>();
    const [vcStatus, setVcStatus] = useState<VcStatus>({ status: "Verifying", checks: [] });

    const goToShowingResult = useCallback((vcData: Vc, vcStatusData: VcStatus) => {
        setActiveStep(VerificationSteps.DisplayResult)
        setVcStatus(vcStatusData);
        setVc(vcData);
    }, [])

    switch (activeStep) {
        case VerificationSteps.ScanQrCodePrompt:
            return (<ScanQrCode />);
        case VerificationSteps.ActivateCamera:
            return (<Verification showResult={goToShowingResult} />);
        case VerificationSteps.DisplayResult:
            return (<Result setActiveStep={setActiveStep} vc={vc} />);
        default:
            return (<></>);
    }
}