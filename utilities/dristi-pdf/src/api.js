var config = require("./config");
var axios = require("axios").default;
var url = require("url");
const { logger } = require("./logger");
const { Pool } = require("pg");

const pool = new Pool({
  user: config.DB_USER,
  host: config.DB_HOST,
  database: config.DB_NAME,
  password: config.DB_PASSWORD,
  port: config.DB_PORT,
});

auth_token = config.auth_token;

async function search_case(cnrNumber, tenantId, requestinfo) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.case, config.paths.case_search),
      data: {
        RequestInfo: requestinfo,
        tenantId: tenantId,
        criteria: [
          {
            cnrNumber: cnrNumber,
          },
        ],
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.case_search}: ${error.message}`);
    throw error;
  }
}

async function search_order(
  tenantId,
  orderId,
  requestinfo,
  isOrderNumber = false
) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.order, config.paths.order_search),
      data: {
        RequestInfo: requestinfo,
        tenantId: tenantId,
        criteria: {
          tenantId: tenantId,
          ...(isOrderNumber ? { orderNumber: orderId } : { id: orderId }),
        },
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.order_search}: ${error.message}`);
    throw error;
  }
}

async function search_hearing(tenantId, cnrNumber, requestinfo) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.hearing, config.paths.hearing_search),
      data: {
        RequestInfo: requestinfo,
        criteria: {
          tenantId: tenantId,
          cnrNumber: cnrNumber,
        },
        pagination: {
          limit: 10,
          offset: 0,
          sortBy: "createdTime",
          order: "desc",
        },
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.hearing_search}: ${error.message}`);
    throw error;
  }
}

async function search_mdms(
  uniqueIdentifier,
  schemaCode,
  tenantID,
  requestInfo
) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.mdms, config.paths.mdms_search),
      data: {
        RequestInfo: requestInfo,
        MdmsCriteria: {
          tenantId: tenantID,
          schemaCode: schemaCode,
          uniqueIdentifiers: [uniqueIdentifier],
        },
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.mdms_search}: ${error.message}`);
    throw error;
  }
}

async function search_hrms(tenantId, employeeTypes, courtRooms, requestinfo) {
  var params = {
    tenantId: tenantId,
    employeetypes: employeeTypes,
    courtrooms: courtRooms,
    limit: 10,
    offset: 0,
  };
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.hrms, config.paths.hrms_search),
      data: {
        RequestInfo: requestinfo,
      },
      params,
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.hrms_search}: ${error.message}`);
    throw error;
  }
}

async function search_individual(tenantId, individualId, requestinfo) {
  var params = {
    tenantId: tenantId,
    limit: 10,
    offset: 0,
  };
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.individual, config.paths.individual_search),
      data: {
        RequestInfo: requestinfo,
        Individual: {
          individualId: individualId,
        },
      },
      params,
    });
  } catch (error) {
    logger.error(
      `Error in ${config.paths.individual_search}: ${error.message}`
    );
    throw error;
  }
}

async function search_advocate(tenantId, individualId, requestinfo) {
  var params = {
    tenantId: tenantId,
    limit: 10,
    offset: 0,
  };
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.advocate, config.paths.advocate_search),
      data: {
        RequestInfo: requestinfo,
        criteria: [
          {
            individualId: individualId,
          },
        ],
      },
      params,
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.advocate_search}: ${error.message}`);
    throw error;
  }
}

async function search_individual_uuid(tenantId, userUuid, requestinfo) {
  var params = {
    tenantId: tenantId,
    limit: 10,
    offset: 0,
  };
  try {
    return await axios({
      method: "post",
      url: url.resolve(config.host.individual, config.paths.individual_search),
      data: {
        RequestInfo: requestinfo,
        Individual: {
          userUuid: [userUuid],
        },
      },
      params,
    });
  } catch (error) {
    logger.error(
      `Error in ${config.paths.individual_search}: ${error.message}`
    );
    throw error;
  }
}

async function search_application(tenantId, applicationId, requestinfo) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(
        config.host.application,
        config.paths.application_search
      ),
      data: {
        RequestInfo: requestinfo,
        tenantId: tenantId,
        criteria: {
          tenantId: tenantId,
          applicationNumber: applicationId,
        },
      },
    });
  } catch (error) {
    logger.error(
      `Error in ${config.paths.application_search}: ${error.message}`
    );
    throw error;
  }
}

async function search_sunbirdrc_credential_service(
  tenantId,
  code,
  uuid,
  requestinfo
) {
  try {
    return await axios({
      method: "post",
      url: url.resolve(
        config.host.sunbirdrc_credential_service,
        config.paths.sunbirdrc_credential_service_search
      ),
      data: {
        RequestInfo: requestinfo,
        tenantId: tenantId,
        code: code,
        uuid: uuid,
      },
    });
  } catch (error) {
    logger.error(
      `Error in ${config.paths.sunbirdrc_credential_service_search}: ${error.message}`
    );
    throw error;
  }
}

async function create_pdf(tenantId, key, data, requestinfo) {
  try {
    return await axios({
      responseType: "stream",
      method: "post",
      url: url.resolve(config.host.pdf, config.paths.pdf_create),
      data: Object.assign(requestinfo, data),
      params: {
        tenantId: tenantId,
        key: key,
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.pdf_create}: ${error.message}`);
    throw error;
  }
}

async function search_message(tenantId, module, locale, requestinfo) {
  try {
    return await axios({
      responseType: "json",
      method: "post",
      url: url.resolve(config.host.localization, config.paths.message_search),
      data: Object.assign(requestinfo),
      params: {
        tenantId: tenantId,
        module: module,
        locale: locale,
      },
    });
  } catch (error) {
    logger.error(`Error in ${config.paths.message_search}: ${error.message}`);
    throw error;
  }
}

module.exports = {
  pool,
  create_pdf,
  search_hrms,
  search_case,
  search_order,
  search_mdms,
  search_individual,
  search_hearing,
  search_sunbirdrc_credential_service,
  search_individual_uuid,
  search_application,
  search_advocate,
  search_message,
};
