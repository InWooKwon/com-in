var express = require('express');
var router = express.Router();

var connection = require("../DBconfig.js").connection;

//login POST
router.post('/', function (req, res, next) {
    var body = req.body;
    console.log(body);
    var id = body.userID;
    var password = body.userPassword;
    console.log(id + password);


    var command = 'select * from user where id=\'' + id + '\'';
    console.log(command);

    var query = connection.query(command, function (err, result, fields) {
        var approve = { 'success': false, 'user': '',  };
        if (err) {
            approve.success = false;
        }
        else {

            if (result.length > 0) {
                console.log("db userPassword" + result[0].pw);
                if (result[0].pw == password) { //비밀번호 확인
                    approve.success = true;
                    approve.user=result[0];
                    console.log(approve.user);
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

        }

        console.log("Data inserted");
        res.send(approve);

    })
})


module.exports = router;