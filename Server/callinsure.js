var express = require('express');
var router = express.Router();

var URLEncoder = require('urlencode');
var http = require('https');
const fs = require('fs');


var jbAry = new Array('kfccins', 'samsunglife', 'shinhanlife', 'pcalife', 'abllife', 'dylife' ,'hanwhalife', 'inglife' ,'kdblife', 'linalife', 'cblife', 'hanalife', 'cardif','ibki', 'mggeneralins',' hwgeneralins', 'axadirect',' nhfire','epostins', 'miraelife', 'heungkuklife', 'hdfire', 'kyobolife', 'metlife', 'prudlife', 'nhlife', 'dgblife', 'dongbulife', 'lifeplanet', 'hdlife','kbli','aia','meritzfire', 'heungkukfire', 'kbinsure', 'lotteins','aig','idbins' ,'thekins','chubb','cardifcare','samsungfire');
const text = '';

console.log(jbAry.length);
for(var i=0; i<jbAry.length;i++){
var specify = {"조회구분":"1"};
var request_json ={"Auth_key": "aidbe124gAtrat10Tage", "Module": jbAry[i],"Class": "개인보험","Job": "계약내용조회","Input": specify};
var string_json=JSON.stringify(request_json);
var jsondata = URLEncoder.encode(URLEncoder.encode(string_json, "UTF-8"), "UTF-8"); //안코딩 처리
var url_coocon = "dev2.coocon.co.kr";// 호출주소
var postString= "JSONData="+jsondata;

var options = {
  host: url_coocon,
    method: 'POST',
  port: 8443,
  path : '/sol/gateway/ins_wapi_mobile.jsp?'+postString,
};  
var req = http.request(options, function(res) {
  res.setEncoding('utf8');
  res.on('data', function (body) {
  
    fs.appendFile('./target.txt',body+'\n\n\n\n', (err) => {

        if(err) throw err;
    
        console.log('File Appended');
    
    });

  });
});
req.on('error', function(e) {
  console.log('problem with request: ' + e.message);
});
req.end();



}