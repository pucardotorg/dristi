const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const caseRoutes = require("./routes/caseRoutes");
const errorHandler = require("./middlewares/errorHandler");
const config = require("./config/config");

const limitSize = config?.payloadSize;

app.use(bodyParser.json({ limit: limitSize }));
app.use(bodyParser.urlencoded({ limit: limitSize, extended: true }));
app.use("/dristi-case-pdf", caseRoutes);
app.use(errorHandler);

const PORT = process.env.PORT || 8090;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
