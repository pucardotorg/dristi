import React from 'react';
import { Grid, Typography, Box } from '@mui/material';
import { ReactComponent as VerificationSuccessIcon } from '../../../../assets/verification-success-icon.svg';
import { ReactComponent as VerificationFailedIcon } from '../../../../assets/verification-failed-icon.svg';
import './resultSummary.css';
import Content from "../../../../assets/drishti_content.svg";
const ResultSummary = ({ success }: { success: boolean }) => {
  return (
    <Grid container spacing={2} alignItems="center" justifyContent="center" className="scan-qr-container">
      <Grid item xs={12} className="header-container">
        <Typography variant="h5" className="header-title">
          DRISHTI
        </Typography>
        <img src={Content} alt="Drishti Content" className="drishti-content" />
      </Grid>
        <Grid item xs={12}>
            <Typography className="document-details-title">
            Document Details
            </Typography>
        </Grid>
      {/* <Grid item xs={12} className="verification-icon-container">
        {success ? <VerificationSuccessIcon /> : <VerificationFailedIcon />}
      </Grid> */}
    </Grid>
  );
};

export default ResultSummary;
