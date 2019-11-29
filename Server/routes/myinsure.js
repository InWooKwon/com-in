var express = require('express');
var router = express.Router();

var URLEncoder = require('urlencode');
var http = require('https');
var connection = require("../DBconfig.js").connection;

var authkey="";


router.get('/:idx',function(req,res_client,next){
    var qry = "SELECT * FROM USER WHERE idx = ?";
    connection.query(qry,req.params.idx,function(err,result,fields){
        if(err){
            console.log(err);
        }
        else{
          authkey=result[0].authKey;
            
              var specify = {"조회구분":"1"};
              var request_json ={"Auth_key": authkey, "Module": "hdfire","Class": "개인보험","Job": "계약내용조회","Input": specify};
              var string_json=JSON.stringify(request_json);
              var jsondata = URLEncoder.encode(URLEncoder.encode(string_json, "UTF-8"), "UTF-8"); //안코딩 처리
              var url_coocon = "dev2.coocon.co.kr";// 호출주소
              var postString= "JSONData="+jsondata;
              console.log(postString);

              var options = {
                host: url_coocon,
                  method: 'POST',
                port: 8443,
                path : '/sol/gateway/ins_wapi_mobile.jsp?'+postString,
              };  
              var req = http.request(options, function(res) {
                console.log('Status: ' + res.statusCode);
                console.log('Headers: ' + JSON.stringify(res.headers));
                res.setEncoding('utf8');
                res.on('data', function (body) {
                
                  console.log('Body: ' + body);
                  console.log(JSON.parse(body));
                  res_client.json(JSON.parse(body));

                });
              });
              req.on('error', function(e) {
                console.log('problem with request: ' + e.message);
              });
              req.end();
            
        }
    });
});


module.exports = router;