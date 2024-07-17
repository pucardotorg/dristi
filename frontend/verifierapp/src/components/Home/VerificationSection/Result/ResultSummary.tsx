import React from 'react';
import { Grid, Typography, Box } from '@mui/material';
import './resultSummary.css';
import Content from "../../../../assets/drishti_content.svg";
const ResultSummary = () => {
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
    </Grid>
  );
};

export default ResultSummary;
