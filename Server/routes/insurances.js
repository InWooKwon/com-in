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

router.get('/:coverage', function (req, res) {
    var coverage = req.params.coverage;
    var qry = "SELECT * FROM COVERAGE WHERE insurance_idx =" + Number(coverage);
    connection.query(qry, function (err, result, fields) {
        res.json(result);
    });
});


module.exports = router;