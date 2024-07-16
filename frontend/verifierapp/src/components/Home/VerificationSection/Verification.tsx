import React, { useState, useEffect, memo } from 'react';
import { Backdrop, Box, Grid, Typography } from '@mui/material';
import QrScanner from './QrScanner';
import { useActiveStepContext } from '../../../pages/Home';
import './Verification.css'
import ScanResult from './ScanResult';
import { useNavigate } from 'react-router-dom';
import { VcStatus } from '../../../types/data-types';
import { ScanStatusResult, VerificationSteps } from '../../../utils/config';
import { decodeQrData } from '../../../utils/qr-utils';
import { verify } from '../../../utils/verification-utils';

const Verification = memo(({showResult}: {showResult: (vcData: any, vcStatusData: VcStatus) => void}) => {
    const { setActiveStep } = useActiveStepContext();
    const [scanStatus, setScanStatus] = useState("")
    const [isVerified, setIsVerified] = useState(true);

    const navigate = useNavigate();

    const [qrData, setQrData] = useState("");

    useEffect(() => {
        if (qrData === "") return
        
        if (qrData === 'invalid' || qrData === 'error') {
            setIsVerified(false)
            setScanStatus(ScanStatusResult.CertificateInValid)
            return
        };

        try {
            setActiveStep(VerificationSteps.Verifying);
            decodeQrData(qrData).then((data) => {
                let vc = JSON.parse(data);
                // TODO: is it a vc? - check format
                console.log("decodeQrData vc", vc)
                verify(vc)
                    .then(status => {
                        console.log("decodeQrData verifyStatus: ", status);
                        // did-resolution fails if the internet is not available and proof can't be verified
                        if (status?.checks?.proof === "NOK"
                            && !window.navigator.onLine) {
                            navigate('/offline');
                        }

                        setIsVerified(false)
                        setScanStatus(ScanStatusResult.CertificateValid)
                        
                        setTimeout(() => {
                            showResult(vc, status)
                        }, 500) 
                    })
                    .catch(error => {
                        console.error("Error occurred while verifying the VC: ", error);
                        console.error("Error code: ", error.code);
                        setIsVerified(false)
                        setScanStatus(ScanStatusResult.CertificateInValid)
                        
                        if (!window.navigator.onLine) {
                            navigate('/offline');
                            return;
                        }
                    })
            }).catch(error => {
                setIsVerified(false)
                setScanStatus(ScanStatusResult.CertificateInValid)
            })
        } catch (error) {
            setIsVerified(false)
            setScanStatus(ScanStatusResult.Failed)
        }
    }, [qrData]);

    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
            open={true}
            onClick={() => { }}>

            <Grid container className="verification-container">
                <Grid item xs={12} className="verification-text">
                    <Typography variant="h5" gutterBottom sx={{marginTop: 2}}>
                        Scan the QR code
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    {isVerified ? 
                    <Box className="verification-box">
                            <QrScanner
                                setActiveStep={setActiveStep}
                                setQrData={setQrData}
                            />
                    </Box>
                    : 
                    <Box className="verification-result">
                        <ScanResult status= {scanStatus} setActiveStep={setActiveStep}/>
                    </Box>
                    }
                </Grid>

            </Grid>
        </Backdrop>
    );
})

export default Verification;
