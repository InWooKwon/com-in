
var mysql = require('mysql');

var connection = mysql.createConnection({
    host: "127.0.0.1",
    port: 3306,
    user: "root",
    password : "Jongu7317",
    database : "FinTech",
    multipleStatements: true,
    dateStrings: 'date'
});

module.exports.connection = connection;