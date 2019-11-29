var express = require('express');
var router = express.Router();

var connection = require("../DBconfig.js").connection;

// 모든 보험상품 조회
router.get('/', function (req, res) {
    var qry = "SELECT * FROM INSURANCE;" + "SELECT * FROM COVERAGE;" + "SELECT tag1 FROM boardInfo where type = 1;";
    connection.query(qry, function (err, result, fields) {
        var rst = {"insurances" : result[0], "coverages" : result[1], "reviewBoard" : result[2]};
        res.json(rst);
    });
});

router.get('/', function (req, res) {
    var qry = "SELECT * FROM INSURANCE;" + "SELECT * FROM COVERAGE;" + "SELECT tag1 FROM boardInfo where type = 1;";
    connection.query(qry, function (err, result, fields) {
        var rst = {"insurances" : result[0], "coverages" : result[1], "reviewBoard" : result[2]};
        res.json(rst);
    });
});

router.get('/hot', function (req, res) {
    var qry = "select insurance.idx, insurance.company, insurance.productName, count(boardInfo.tag1) * insurance.score as cnt from insurance, boardInfo where boardInfo.type = 1 and boardInfo.tag1 = insurance.idx group by insurance.idx order by cnt desc limit 5;";
    connection.query(qry, function (err, result, fields) {
        var rst = {"hotInsurances" : result};
        res.json(rst);
    });
});

router.get('/ins/:insIdx', function (req, res) {
    var insIdx = req.params.insIdx;
    var qry = "SELECT productName, company FROM Insurance WHERE idx =" + Number(insIdx);
    connection.query(qry, function (err, result, fields) {
        var rst = {"recentIns" : result};
});
router.get('/hot/:idx', function (req, res) {
    var age = req.params.idx;
    var qry;
    if(idx==1){
        qry= "select insurance.idx,insurance.productName,insurance.company,insurance.productType from user,userins,insurance where birth between '1990-01-01' AND '2000-01-01' and user.idx=userins.idx and userins.insuranceidx = insurance.idx;";
    }
    else if(idx==2){
        qry= "select insurance.idx,insurance.productName,insurance.company,insurance.productType from user,userins,insurance where birth between '1980-01-01' AND '1989-01-01' and user.idx=userins.idx and userins.insuranceidx = insurance.idx;";
    }
    else if(idx==3){
        qry="select insurance.idx,insurance.productName,insurance.company,insurance.productType from user,userins,insurance where birth between '1970-01-01' AND '1979-01-01' and user.idx=userins.idx and userins.insuranceidx = insurance.idx;"
    }
    connection.query(qry, function (err, result, fields) {
        var rst = {"hotInsurances" : result};
        res.json(rst);
    });

router.get('/:coverage', function (req, res) {
    var coverage = req.params.coverage;
    var qry = "SELECT * FROM COVERAGE WHERE insurance_idx =" + Number(coverage);
    connection.query(qry, function (err, result, fields) {
        res.json(result);
    });
});


module.exports = router;