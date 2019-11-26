var express = require('express');
var router = express.Router();

var connection = require("../DBconfig.js").connection;

//register POST
router.post('/', function (req, res) {
    var approve = { 'success': false ,'dup_id':false,'dup_nick':false};

    var body = req.body;
    var id = body.userID;
    var password = body.userPassword;
    var name = body.userName;
    var nick = body.userNick;
    var birth = body.userBirth;
    var phone = body.userPhone;
    var email = body.userEmail;
    var key = body.AuthKey;
    var dup=body.dup;
    if(dup=="1"){//중복 검사
        var command = 'select * from user where id=\'' + id + '\'';
        connection.query(command,function(err,rows,fields){
            if(err){
                console.log("register dup_id error");
            }
            else{
                if(rows.length>0){
                    approve.dup_id=true;
                }
                else{
                    approve.dup_id=false;
                }
                res.send(approve);
            }
        });
    }
    else if(dup==2){//중복검사
        var qry='SELECT * FROM user where nickName =\''+nick+'\'';
        connection.query(qry,function(err,rows,fields){
            if(err){
                console.log("register dup_id error");
            }
            else{
                if(rows.length>0){
                    approve.dup_nick=true;
                }
                else{
                    approve.dup_nick=false;
                }
                res.send(approve);
            }
        });
    }

    else{
        var command = 'INSERT INTO user (id, pw, realName, nickName, birth, email, phoneNumber, authKey) values (\'' + id + '\',\'' + password + '\',\'' + name + '\',\'' + nick + '\',\'' + birth + '\',\'' + email + '\',\'' + phone + '\',\'' + key + '\')';
        console.log(command);
        connection.query(command, function (err, rows, fields) {
            if (err) {
                console.log("register fail");
                approve.success = false;
                res.send(approve);
            }
            else {
                console.log("Data inserted");
                approve.success = true;
                res.send(approve);
            }   

        })
    }
});


module.exports = router;