const { logger } = require("../logger");

function renderError(res, errorMessage, errorCode, errorObject) {
    if (errorCode === undefined) errorCode = 500;
    logger.error(`${errorMessage}: ${errorObject ? errorObject.stack || errorObject : ''}`);
    res.status(errorCode).send({ errorMessage, errorCode });
}

module.exports = { renderError };