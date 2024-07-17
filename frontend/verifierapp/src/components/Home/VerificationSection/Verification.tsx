import React, { useState, useEffect, memo, useCallback } from 'react';
import { Backdrop, Box, Grid, Typography } from '@mui/material';
import QrScanner from './QrScanner';
import './Verification.css'
import ScanResult from './ScanResult';
import { useNavigate } from 'react-router-dom';
import { Vc, VcStatus } from '../../../types/data-types';
import { ScanStatusResult } from '../../../utils/config';
import { decodeQrData } from '../../../utils/qr-utils';
import { verify } from '../../../utils/verification-utils';

const Verification = memo(({ showResult }: { showResult: (vcData: Vc, vcStatusData: VcStatus) => void }) => {

    const [qrData, setQrData] = useState<any>("");
    const [scanStatus, setScanStatus] = useState<string>(ScanStatusResult.Default)
    const [isVerified, setIsVerified] = useState<boolean>(true);

    const navigate = useNavigate();

    useEffect(() => {
        const processQRCodeData = async () => {    
            if (qrData === "") return
    
            if (['invalid', 'error', ScanStatusResult.CertificateInValid].includes(qrData)) {
                setIsVerified(false)
                setScanStatus(ScanStatusResult.CertificateInValid)
                return
            };
    
            try {
                const data = await decodeQrData(qrData);
                const vc: Vc = JSON.parse(data);
                const status = await verify(vc);
                if (status?.checks?.proof === "NOK" && !window.navigator.onLine) {
                    navigate('/offline');
                }
                setIsVerified(false);
                setScanStatus(ScanStatusResult.CertificateValid);
                setTimeout(() => showResult(vc, status), 1000);
            } catch (error) {
                console.error("Error occurred while verifying the VC: ", error)
                setIsVerified(false);
                setScanStatus(ScanStatusResult.CertificateInValid);
                if (!window.navigator.onLine) {
                    navigate('/offline');
                }
            }
        }
    
        processQRCodeData();
    }, [qrData]);

    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }} open={true}>
            <Grid container className="verification-container">
                <Grid item xs={12} className="verification-text">
                    <Typography variant="h5" gutterBottom sx={{ marginTop: 2 }}>
                        Scan the QR code
                    </Typography>
                </Grid>

                {isVerified ?
                    <Grid item xs={12}>
                        <Box className="verification-box">
                            <QrScanner setQrData={setQrData} />
                        </Box>
                    </Grid>
                    :
                    <Grid item xs={12}>
                        <Box className="verification-result">
                            <ScanResult status={scanStatus} />
                        </Box>
                    </Grid>
                }
            </Grid>
        </Backdrop >
    );
})

export default Verification;
