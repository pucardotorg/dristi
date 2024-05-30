import { AppContainer, Close, CloseSvg, DetailsCard, Modal } from "@egovernments/digit-ui-react-components";
import React from "react";
import { Route, Switch, useHistory, useRouteMatch } from "react-router-dom/cjs/react-router-dom.min";
import CustomDetailsCard from "../../../components/CustomDetailsCard";
import CitizenInfoLabel from "../../../components/CitizenInfoLabel";

function FileCase({ t }) {
  const { path } = useRouteMatch();
  const history = useHistory();
  const onCancel = () => {
    history.push("/digit-ui/citizen/dristi/home");
  };
  const onSelect = () => {
    console.debug("Shubhi happy :)))");
  };
  const Heading = (props) => {
    return <h1 className="heading-m">{props.label}</h1>;
  };
  const CloseBtn = (props) => {
    return (
      <div onClick={props?.onClick} style={props?.isMobileView ? { padding: 5 } : null}>
        {props?.isMobileView ? (
          <CloseSvg />
        ) : (
          <div className={"icon-bg-secondary"} style={{ backgroundColor: "#505A5F" }}>
            {" "}
            <Close />{" "}
          </div>
        )}
      </div>
    );
  };
  const caseTypeDetails = [
    { header: "Case Category", subtext: "Criminal", serialNumber: "01." },
    {
      header: "Status / Act",
      subtext: "Negotiable Insruments Act",
      serialNumber: "02.",
    },
    { header: "Section", subtext: "138", serialNumber: "03." },
  ];
  return (
    <div className="citizen-form-wrapper" style={{ minWidth: "100%" }}>
      <Switch>
        <AppContainer>
          <Route path={`${path}`} exact>
            <Modal
              headerBarEnd={<CloseBtn onClick={onCancel} isMobileView={false} />}
              actionCancelLabel={t("CORE_LOGOUT_CANCEL")}
              actionCancelOnSubmit={onCancel}
              actionSaveLabel={t("CS_CORE_WEB_PROCEED")}
              actionSaveOnSubmit={onSelect}
              formId="modal-action"
              headerBarMain={<Heading label={t("CS_SELECT_CASETYPE_HEADER")} />}
              style={{}}
            >
              {caseTypeDetails.map((item) => (
                <CustomDetailsCard header={item.header} subtext={item.subtext} serialNumber={item.serialNumber} style={{ width: "100%" }} />
              ))}
              <CitizenInfoLabel
                style={{ maxWidth: "100%", height: "90px" }}
                textStyle={{ margin: 8 }}
                iconStyle={{ margin: 0 }}
                info={t("ES_COMMON_NOTE")}
                text={t("ES_BANNER_LABEL")}
                className="doc-banner"
              ></CitizenInfoLabel>
            </Modal>
          </Route>
        </AppContainer>
      </Switch>
    </div>
  );
}

export default FileCase;
