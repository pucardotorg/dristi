import React, { memo } from 'react';
import { Button, Grid, Typography } from '@mui/material';
import { ScanStatusResult, VerificationSteps } from '../../../utils/config';
import BtnQr from "../../../assets/btnqr.svg";
import {
    CheckCircleOutline,
    ErrorOutline,
    DownloadDoneOutlined,
    CancelOutlined,
} from '@mui/icons-material';

import './ScanResult.css'

const ScanResult = memo(({ status, setActiveStep }: { status: string, setActiveStep: (activeStep: number) => void }) => {

    switch (status) {
        case ScanStatusResult.Loading:
            return (
                <div>
                    <DownloadDoneOutlined style={{ width: 80, height: 80, color: '#FFFFFF' }} />
                    <Typography className='scan-result-status' variant="body1" gutterBottom>
                        Loading results...
                    </Typography>
                </div>
            )

        case ScanStatusResult.CertificateValid:
            return (
                <div>
                    <CheckCircleOutline style={{ width: 80, height: 80, color: '#00703C' }} />
                    <Typography sx={{ marginTop: 4 }} className='scan-result-status' variant="body1" gutterBottom>
                        This given certificate is valid!
                    </Typography>
                </div>
            )

        case ScanStatusResult.CertificateInValid:
            return (
                <div>
                    <CancelOutlined style={{ width: 80, height: 80, fill: '#BB2C2F' }} />
                    <Typography sx={{ marginTop: 4 }} variant="body1" gutterBottom>
                        The given certificate is invalid!
                    </Typography>

                    <Grid container spacing={2} className="scan-result-button1">
                        <Grid item xs={12}>
                            <Button
                                variant="contained"
                                color="primary"
                                size="small"
                                fullWidth
                                sx={{ marginTop: 1, textTransform: 'none' }}
                                onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
                                className="scan-button"
                                startIcon={<img src={BtnQr} alt="QR Icon" />}
                            >
                                Scan another QR Code
                            </Button>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="body1" sx={{ cursor: 'pointer' }}
                                onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
                            >Back to Home</Typography>
                        </Grid>
                    </Grid>
                </div>
            )
        case ScanStatusResult.Failed:
            return (
                <div>
                    <ErrorOutline style={{ width: 80, height: 80, color: '#77787B' }} />
                    <Typography sx={{ marginTop: 4 }} variant="h5" gutterBottom>
                        We can not connect to our servers at the moment.
                    </Typography>

                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <Button
                                variant="contained"
                                color="primary"
                                size="small"
                                sx={{ marginTop: 1, textTransform: 'none' }}
                                fullWidth
                                onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
                                className="scan-button"
                                startIcon={<img src={BtnQr} alt="QR Icon" />}
                            >
                                Try Again
                            </Button>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="body1" gutterBottom
                                sx={{ cursor: 'pointer' }}
                                onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
                            >Back to Home</Typography>
                        </Grid>
                    </Grid>
                </div>
            )
        default:
            return (<></>)
    }
})

export default ScanResult