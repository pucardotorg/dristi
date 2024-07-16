import styled from "@emotion/styled";
import {Box, Grid, Typography} from "@mui/material";

export const ResultsSummaryContainer = styled(Box)(({success}: { success: boolean }) => (
    {
        height: "340px",
        // backgroundColor: success ? "#007E7E" : "#CB4242",
        // color: "white"
    }
));

export const VcDisplayCardContainer = styled(Box)(({cardPositioning}: {
        cardPositioning: { top?: number, right?: number ,left?:number}
    }) => (
        {
            margin: "auto",
            top: cardPositioning.top ?? 0,
            right: cardPositioning.right ?? 6,
            left:cardPositioning.left ?? 6
            
            // position: "absolute"
        }
    )
);

export const VcDisplay = styled(Grid)`
    width: calc(min(400px, 90vw));
    margin: auto;
    background: white;
    border-radius: 12px;
    padding: 5px 15px;
    box-shadow: 0 3px 15px #0000000F;
    max-height: 320px;
    overflow-y: hidden;
    margin-bottom: 12px;

`

export const VcProperty = styled(Grid)`
    padding: 10px 4px;
`

export const VcPropertyKey = styled(Typography)`
    font: normal normal normal 11px/14px Inter;
    margin-bottom: 4px
`

export const VcPropertyValue = styled(Typography)`
    font: normal normal 600 12px/15px Inter
`

export const VcVerificationFailedContainer = styled(Box)`
    display: grid;
    place-content: center;
    width: 100%;
    height: 320px;
    color: rgb(0, 0, 0, 0.1);
    font-size: 100px;
`
