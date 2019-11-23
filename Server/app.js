var express = require('express');
var http = require('http');
var router = express.Router();
var mysql = require('mysql');
var app = express();
var bodyParser = require('body-parser');

var connection = mysql.createConnection({
    host: "127.0.0.1",
    port: 3306,
    user: "root",
    password: "1111",
    database: "comindb",
    multipleStatements: true
});
connection.connect();


//app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());


// 모든 보험상품 조회
app.get('/insurances', function (req, res) {
    var qry = "SELECT * FROM INSURANCE;" + "SELECT * FROM COVERAGE;";
    connection.query(qry, function (err, result, fields) {
        var rst = {"insurances" : result[0], "coverages" : result[1]};
        res.json(rst);
    });
});


app.get('/insurances/:coverage', function (req, res) {
    var coverage = req.params.coverage;
    var qry = "SELECT * FROM COVERAGE WHERE insurance_idx =" + Number(coverage);
    connection.query(qry, function (err, result, fields) {
        res.json(result);
    });
});

//login POST
app.post('/login', function (req, res, next) {
    var approve = { 'success': false, 'userID': '', 'userPassword': '' };

    var body = req.body;
    console.log(body);
    var id = body.userID;
    var password = body.userPassword;
    console.log(id + password);


    var command = 'select * from user where id=\'' + id + '\'';
    console.log(command);

    var query = connection.query(command, function (err, result, fields) {
        if (err) {
            approve.success = false;
        }
        else {

            if (result.length > 0) {
                console.log("db userPassword" + result[0].userPassword);
                if (result[0].pw == password) { //비밀번호 확인
                    approve.success = true;
                    approve.userID = id;
                    approve.userPassword = password;
                    console.log(approve.userID);
                    console.log("login success");
                }
                else {
                    approve.success = false;
                    console.log("login fail, not match password");
                }
            }
            else { //id 존재 X
                approve.success = false;
                console.log("login fail, there is no id");
            }

            console.log("Data inserted");
            res.send(approve);
            approve.success = false;
        }

    })
})

//register POST
app.post('/register', function (req, res) {
    var approve = { 'success': 'NO' };

    var body = req.body;
    var id = body.userID;
    var password = body.userPassword;
    var name = body.userName;
    var nick = body.userNick;
    var birth = body.userBirth;
    var phone = body.userPhone;
    var email = body.userEmail;
    var key = body.AuthKey;
    var command = 'INSERT INTO user (id, pw, realName, nickName, birth, email, phoneNumber, authKey) values (\'' + id + '\',\'' + password + '\',\'' + name + '\',\'' + nick + '\',\'' + birth + '\',\'' + email + '\',\'' + phone + '\',\'' + key + '\')';
    console.log(command);
    var query = connection.query(command, function (err, rows, fields) {
        if (err) {
            console.log("register fail");
            approve.success = 'NO';
        }
        else {
            console.log("Data inserted");
            approve.success = 'YES';
        }

        res.send(approve);

    })
})

module.exports = router;
http.createServer(app).listen(9090, function () {
    console.log('서버 실행중...');
});