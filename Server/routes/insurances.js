var express = require('express');
var router = express.Router();

var connection = require("../DBconfig.js").connection;

// 모든 보험상품 조회
router.get('/', function (req, res) {
    var qry = "SELECT * FROM INSURANCE;" + "SELECT * FROM COVERAGE;";
    connection.query(qry, function (err, result, fields) {
        var rst = {"insurances" : result[0], "coverages" : result[1]};
        res.json(rst);
    });
    console.log("보험조회");
});


router.get('/:coverage', function (req, res) {
    var coverage = req.params.coverage;
    var qry = "SELECT * FROM COVERAGE WHERE insurance_idx =" + Number(coverage);
    connection.query(qry, function (err, result, fields) {
        res.json(result);
    });
});


module.exports = router;