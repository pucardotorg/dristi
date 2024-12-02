const { createLogger, format, transports } = require("winston");

const myFormat = format.printf(({ level, message, label, timestamp }) => {
  const formattedMessage =
    typeof message === "string" ? message : JSON.stringify(message);
  return `${timestamp} [${label}] [${level}]: ${formattedMessage}`;
});

const logger = createLogger({
  format: format.combine(
    format.label({ label: "BFF" }),
    format.timestamp({ format: " YYYY-MM-DD HH:mm:ss.SSSZZ " }),
    format.json(),
    myFormat
  ),
  transports: [new transports.Console()],
});

//export default logger;
module.exports = { logger };
