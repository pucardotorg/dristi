import React, {useEffect, useRef, useState} from 'react';
import {Scanner} from '@yudiel/react-qr-scanner';
import CameraAccessDenied from "./CameraAccessDenied";
import { VerificationSteps} from "../../../utils/config";

let timer: NodeJS.Timeout;

function QrScanner({setActiveStep, setQrData}: {
    setQrData: (data: string) => void, setActiveStep: (activeStep: number) => void
}) {
    const [isCameraBlocked, setIsCameraBlocked] = useState(false);

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
                    console.log("Scanner text", text)
                    setActiveStep(VerificationSteps.Verifying);
                    setQrData(text);
                }}
                onError={(error) => {
                    setQrData("invalid");
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
                    delayBetweenScanSuccess: 100000 // Scan once
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
}

export default QrScanner;
