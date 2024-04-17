import { Button } from "@egovernments/digit-ui-react-components";
import React from "react";
import { useTranslation } from "react-i18next";
import { useHistory } from "react-router-dom";
import { ReactComponent as RegisterImage }  from './ImageUpload/image/register.svg';
import { ReactComponent as RightArrow }  from './ImageUpload/image/arrow_forward.svg';


function TakeUserToRegistration() {
  const { t } = useTranslation();
  const history = useHistory();
  return (
    <div style={{ "display": "flex", "flexDirection": "column", "width": "90vw", "height": "52vh", "background": "white", "alignItems": "center"}}>
      <div style={{"maxHeight":'40vh'}}> 
        <RegisterImage></RegisterImage>
      </div>
      <div style={{"text-align": "center"}}>
      <h2 > You are yet to register yourself! </h2>
      </div>
      <div>
      <Button
      onButtonClick={() => {
        history.push(`/digit-ui/citizen/dristi/home/login/id-verification`);
      }}
      label={t("Register")}
      style={{
        flex: 1,
        maxHeight: "7vh",
        width: "20vw",
        background: "none",
        color: "#F47738",
        boxShadow: "none",
      }}
      >
        <RightArrow />
       </Button>
      </div>
      </div> 
      );
}

export default TakeUserToRegistration;
