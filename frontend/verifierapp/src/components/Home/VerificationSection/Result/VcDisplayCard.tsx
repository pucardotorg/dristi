import React from 'react';
import { Box, Button, Grid, Typography } from '@mui/material';
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import { SetActiveStepFunction } from '../../../../types/function-types';
import { VerificationSteps } from '../../../../utils/config';
import { VcDisplay, VcProperty, VcPropertyKey, VcPropertyValue, VcVerificationFailedContainer } from './styles';
import { convertToTitleCase, getDisplayValue } from '../../../../utils/misc';
import './vcDisplayCard.css';
import BtnQr from '../../../../../src/assets/btnqr.svg';

const VcDisplayCard = ({ vc, setActiveStep }: { vc: any, setActiveStep: SetActiveStepFunction }) => {
  return (
    <Box className="vc-display-card">
      <Grid container spacing={2} className="vc-display">
        {vc ? (
          <>
            {["caseName", "caseTypes", "issuedBy", "filingDate", "caseNumber"].map((field) => (
              <Grid item xs={12} md={6} key={field} className="vc-property">
                <Typography variant="subtitle1" className="vc-property-key">{convertToTitleCase(field)}</Typography>
                <Typography variant="body2" className="vc-property-value">{getDisplayValue(vc.credentialSubject[field])}</Typography>
              </Grid>
            ))}
            {Object.keys(vc.credentialSubject)
              .filter(key => !["id", "type", "caseName", "caseTypes", "issuedBy", "filingDate", "caseNumber"].includes(key.toLowerCase()))
              .map(key => (
                <Grid item xs={12} md={6} key={key} className="vc-property">
                  <Typography variant="subtitle1" className="vc-property-key">{convertToTitleCase(key)}</Typography>
                  <Typography variant="body2" className="vc-property-value">{getDisplayValue(vc.credentialSubject[key])}</Typography>
                </Grid>
              ))}
          </>
        ) : (
          <VcVerificationFailedContainer>
            <DescriptionOutlinedIcon fontSize="inherit" color="inherit" />
            <Typography variant="h6" color="inherit">
              Verification Failed
            </Typography>
          </VcVerificationFailedContainer>
        )}
      </Grid>
      <Grid container spacing={2} className="button-group">
        <Grid item xs={12}>
          <Button
            variant="contained"
            color="primary"
            size="medium"
            fullWidth
            onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
            className="scan-button"
            sx={{textTransform: 'none'}}
            startIcon={<img src={BtnQr} alt="QR Icon" />}
          >
            Scan Another QR Code
          </Button>
        </Grid>
        <Grid item xs={12}>
          <Button
            variant="outlined"
            color="primary"
            size="medium"
            fullWidth
            sx={{textTransform: 'none'}}
            onClick={() => setActiveStep(VerificationSteps.ScanQrCodePrompt)}
            className="scan-button"
            style={{ marginTop: 10 }}
          >
            Back To Home
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
};

export default VcDisplayCard;
