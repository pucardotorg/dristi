import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import ScanQrCode from './ScanQrCode';
import { ActiveStepContext } from '../../../pages/Home';  // Assuming ActiveStepProvider is the context provider for active steps
import { VerificationSteps } from '../../../utils/config';


describe('ScanQrCode Component', () => {
  test('Button click sets active step to ActivateCamera', () => {
    const setActiveStep = jest.fn();
    let activeStep: number = 0;

    const { getByText } = render(
      <ActiveStepContext.Provider value={{ activeStep, setActiveStep }}>
        <ScanQrCode />
      </ActiveStepContext.Provider>
    );

    const button = getByText('Scan QR Code');
    fireEvent.click(button);
    expect(setActiveStep).toHaveBeenCalledWith(VerificationSteps.ActivateCamera);
  });
});