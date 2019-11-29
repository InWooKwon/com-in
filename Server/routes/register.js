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


router.delete('/:idx',function(req,res){
    
    var qry = "DELETE FROM USER WHERE idx = ?";
    connection.query(qry,req.params.idx,function(err, rows, fields){
        var rst={"success":false};
        if(err){
            rst.success=false;
            console.log("withdraw user error");
        }
        else{
            rst.success=true;
        }
        res.json(rst);

    });

});

router.put('/:idx',function(req,res){

    var body= req.body;
    var index = req.params.idx;
    var content = body.modify;
   // var content2 = body.modify2;
  //  var content3=body.modify3;
    var case_modify = body.case_modify;
    var qry = '';


    if(case_modify==1){
        qry = 'UPDATE USER SET nickName = \''+content+'\' WHERE idx = ?';
    }
    else if(case_modify==2){
        qry = 'UPDATE USER SET pw = \''+content+'\' WHERE idx = ?';
    }
    else if(case_modify==3){
        qry = 'UPDATE USER SET email = \''+content+'\' WHERE idx = ?';
    }
    console.log(qry);
    
    connection.query(qry,index,function(err,rows){
        var rst = {"success":false};
        if(error){
            console.log("error : error modify")
            rst.success=false;
        }
        else{
            rst.success=true;
            
        }
        res.json(rst);
    })
});


module.exports = router;