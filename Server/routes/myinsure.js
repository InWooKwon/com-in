var express = require('express');
var router = express.Router();

var URLEncoder = require('urlencode');
var http = require('https');
var connection = require("../DBconfig.js").connection;

var authkey = "";


router.get('/:idx', function (req, res_client, next) {
  var idx=req.params.idx;
  var qry = "SELECT * FROM USER WHERE idx = "+idx;
  connection.query(qry, req.params.idx, function (err, result, fields) {
    if (err) {
      console.log(err);
    }
    else {
      authkey = result[0].authKey;

      var specify = { "조회구분": "1" };
      var request_json = { "Auth_key": authkey, "Module": "hdfire", "Class": "개인보험", "Job": "계약내용조회", "Input": specify };
      var string_json = JSON.stringify(request_json);
      var jsondata = URLEncoder.encode(URLEncoder.encode(string_json, "UTF-8"), "UTF-8"); //안코딩 처리
      var url_coocon = "dev2.coocon.co.kr";// 호출주소
      var postString = "JSONData=" + jsondata;
      console.log(postString);

      var options = {
        host: url_coocon,
        method: 'POST',
        port: 8443,
        path: '/sol/gateway/ins_wapi_mobile.jsp?' + postString,
      };
      var req = http.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (body) {
          res_client.json(JSON.parse(body));
        });
      });
      req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
      });
      req.end();
    }
  });
});

router.get('/review/:idx',function(req,res,next){
  var idx=req.params.idx;
  var qry1="SELECT insurance.idx,productName,company,productType,minAge,maxAge,price,score FROM userins,insurance where useridx="+idx+" and userins.insuranceidx = insurance.idx";
  connection.query(qry1,function(err,rst,fields){
    if(err) return res.json({success:false});
    res.json({"insurances":rst});
  })
})

// 출처: https://devnauts.tistory.com/95 [devnauts]








module.exports = router;