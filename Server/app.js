var express = require('express');
var http = require('http');
var router = express.Router();
var app = express();
var bodyParser = require('body-parser');

var insurancesRouter = require('./routes/insurances');
var boardRouter = require('./routes/board');
var loginRouter = require('./routes/login');
var registerRouter = require('./routes/register');

var connection = require("./DBconfig.js").connection;

connection.connect();

app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

app.use('/insurances', insurancesRouter);
app.use('/board', boardRouter);
app.use('/login', loginRouter);
app.use('/register', registerRouter);

module.exports = router;
http.createServer(app).listen(9090, function () {
    console.log('서버 실행중...');
});