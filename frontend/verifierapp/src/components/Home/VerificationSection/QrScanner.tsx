import React, {memo, useEffect, useRef, useState} from 'react';
import {Scanner} from '@yudiel/react-qr-scanner';
import CameraAccessDenied from "./CameraAccessDenied";
import { ScanStatusResult, VerificationSteps} from "../../../utils/config";
import { useActiveStepContext } from '../../../pages/Home';

let timer: NodeJS.Timeout;

const QrScanner = memo(({  setQrData }: {
    setQrData: (data: string) => void
}) => {
    const [isCameraBlocked, setIsCameraBlocked] = useState(false);

    const { setActiveStep } = useActiveStepContext();

    const scannerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        // Disable inbuilt border around the video
        if (scannerRef?.current) {
            let svgElements = scannerRef?.current?.getElementsByTagName('svg');
            if (svgElements.length === 1) {
                svgElements[0].style.display = 'none';
            }
        }
    }, [scannerRef]);

    return (
        <div ref={scannerRef}>
            <Scanner
                onResult={(text, result) => {
                    setQrData(text);
                }}
                onError={(error) => {
                    setQrData(ScanStatusResult.CertificateInValid);
                    console.error("QR Scanner error:", error);
                    clearTimeout(timer);
                    setIsCameraBlocked(true);
                }}
                options={{
                    constraints: {
                        "width": {
                            "min": 640,
                            "ideal": 720,
                            "max": 1920
                        },
                        "height": {
                            "min": 640,
                            "ideal": 720,
                            "max": 1080
                        },
                        facingMode: "environment"
                    },
                    delayBetweenScanSuccess: 10000 // Scan once
                }}
                styles={{
                    container: {
                        width: "316px",
                        placeContent: "center",
                        display: "grid",
                        placeItems: "center",
                        borderRadius: "12px"
                    },
                    video: {
                        zIndex: 1000
                    }
                }}
            />
            <CameraAccessDenied open={isCameraBlocked} handleClose={() => {
                setActiveStep(VerificationSteps.ScanQrCodePrompt);
                setIsCameraBlocked(false)
            }}/>
        </div>
    );
})

export default QrScanner;
