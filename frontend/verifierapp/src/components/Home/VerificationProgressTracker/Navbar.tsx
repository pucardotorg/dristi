import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
// import injiLogo from "../../../assets/inji-logo.svg";
import { NavbarContainer } from "./styles";

function Navbar(props: any) {
  return (
    <AppBar position="static">
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <Box display="flex" alignItems="center">
            <Box>
              <Typography variant="h5" component="div">DRISHTI</Typography>
              <Typography variant="subtitle1" component="div">An initiative by Govt. of India</Typography>
            </Box>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}

export default Navbar;
