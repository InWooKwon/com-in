var express = require('express');
var http = require('http');
var bodyparser=require('body-parser');

var mysql=require('mysql');
var connection=mysql.createConnection({
    host : "127.0.0.1",
    port : 3306,
    user : "root",
    password : "Jongu7317",
    database : "FinTech"
});
connection.connect();

var app=express(); 

app.use(bodyparser.urlencoded({extended:false}));
app.use(bodyparser.json());

app.get('/insurances',function(req,res){
    var qry="SELECT * FROM INSURANCE";
    connection.query(qry,function(err,result,fields){
        var rst={"insurances":result};
        res.json(rst);
    });
}); // 모든 보험상품 조회
app.get('/insurances/:coverage',function(req,res){
    var coverage = req.params.coverage;
    var qry = "SELECT * FROM COVERAGE WHERE insurance_idx =" + Number(coverage);
    connection.query(qry,function(err,result,fields){
        res.json(result);
    });
});

http.createServer(app).listen(9090,function(){
    console.log('서버 실행중...');
});