import React from 'react';
import {Box, CircularProgress} from "@mui/material";

function Loader(props: any) {
    return (
        <Box sx={{color: "#007E7E"}}>
            <CircularProgress size={76} thickness={2} color={"inherit"}/>
        </Box>
    );
}

export default Loader;