var express = require('express');
var router = express.Router();
var mysql = require("mysql");
var app = express();
var http = require('http');
var bodyParser= require('body-parser');

app.set('port',process.env.PORT || 3000);
app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());



//커넥션 연결
var connection = mysql.createConnection({

	host : 'localhost',
	port : 3306,
	user: "root",
	password: "alzlwl258",
	database: "mydb"
});

connection.connect();

//login POST
app.post('/login', function(req,res, next){
    var approve ={'success':'NO', 'userID':'', 'userPassword':''};

	var body = req.body;
	var id = body.userID;
    var password = body.userPassword;
    console.log(id+password);


    var command = 'select * from userInfo where userID=\''+id+'\'';
    console.log(command);
    
	var query = connection.query(command, function(err,rows,fields){
		if(err){
            approve.success='NO';
            throw err;
        }
        else{
            
            if(rows.length > 0){
                if(rows[0].userPassword == password){ //비밀번호 확인
                    approve.success='YES';
                    approve.userID = id;
                    approve.userPassword = password;
                    console.log(approve.userID);
                    console.log("login success");
                }
                else{
                    approve.success='NO';
                    console.log("login fail, not match password");
                }
                
            }
            else{ //id 존재 X
                approve.success='NO';
                console.log("login fail, there is no id");
            }
    
        console.log("Data inserted");
        res.send(approve);
        }

    })
})

//register POST
app.post('/register', function(req,res){
    var approve ={'success':'NO'};

	var body = req.body;
	var id = body.userID;
	var password = body.userPassword;
	var name = body.userName;
	var nick = body.userNick;
	var birth = body.userBirth;
	var phone = body.userPhone;
	var email = body.userEmail;
	var key = body.AuthKey;
    var command = 'INSERT INTO userInfo (userID, userPassword, userName, userNick, userBirth, userEmail, userPhone, AuthKey) values (\''+ id + '\',\''+password+'\',\''+name+'\',\''+nick+'\',\''+birth+'\',\''+email+'\',\''+phone+'\',\''+key+'\')';
    console.log(command);
    var query = connection.query(command, function(err,rows,fields){
		if(err){
            console.log("register fail");
			approve.success='NO';
		}
		else{
			console.log("Data inserted");
			approve.success='YES';
        }
        
        res.send(approve);

	})
})

  

module.exports=router;
var server = http.createServer(app).listen(app.get('port'),function(){
    console.log("익스프레스로 웹 서버를 실행함 : "+ app.get('port'));
});