import styled from "@emotion/styled";
import {StepContent, Typography} from "@mui/material";

export const StepperIconContainer = styled('div')(({ ownerState }: {
    ownerState: { completed: boolean, active: boolean }
}) => ({
    backgroundColor: '#FFF',
    color: '#007E7E',
    ...((ownerState.active || ownerState.completed) && {
        backgroundColor: '#007E7E',
        color: '#FFF',
    }),
    border: '1px solid #007E7E',
    zIndex: 1,
    width: 24,
    height: 24,
    display: 'flex',
    borderRadius: '50%',
    justifyContent: 'center',
    alignItems: 'center',
    font: 'normal normal 600 12px/15px Inter'
}));

export const StepLabelContent = styled(Typography)`
    font: normal normal bold 16px/20px Inter;
`;

export const StepContentContainer = styled(StepContent)`
    border-color: #007E7E;
    padding-bottom: 30px;
    display: block
`

export const StepContentDescription = styled(Typography)`
    font: normal normal normal 14px/19px Inter;
    color: #535353;
`
