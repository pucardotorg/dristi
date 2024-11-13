const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const caseRoutes = require('./routes/caseRoutes');
const errorHandler = require('./middlewares/errorHandler');

app.use(bodyParser.json());
app.use('/dristi-case-pdf', caseRoutes);  // Updated context path
app.use(errorHandler);

const PORT = process.env.PORT || 8090;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});