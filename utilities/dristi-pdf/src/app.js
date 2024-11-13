var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
var config = require("./config");
const cors = require("cors");

const order = require("./routes/order");
const application = require("./routes/application");
const pdfRoutes = require("./routes/pdfRoutes");
// var {listenConsumer} = require("./consumer")

var app = express();
app.disable("x-powered-by");

app.use(cors());
app.options("*", cors()); // Preflight requests
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use(config.app.contextPath + "/order", order);
app.use(config.app.contextPath + "/application", application);
app.use(config.app.contextPath + "/dristi-pdf", pdfRoutes);
// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

// Commenting consumer listener becuase excel bill gen is not required. IFMS adapter will process the payment.
// listenConsumer();

module.exports = app;
