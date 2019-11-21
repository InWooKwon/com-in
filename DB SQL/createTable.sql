#USER TABLE CREATE
CREATE TABLE USER (
  idx INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  id VARCHAR(32) NOT NULL,
  pw VARCHAR(32) NOT NULL,
  realName VARCHAR(32) NOT NULL,
  nickName VARCHAR(32) NOT NULL,
  birth INT(11) NOT NULL,
  email VARCHAR(32) NOT NULL,
  phoneNumber INT(32) NOT NULL,
  authKey INT(32) NOT NULL,
  PRIMARY KEY (idx)
);

#INSURANCE TABLE CREATE
CREATE TABLE INSURANCE (
  idx INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  productName VARCHAR(100) NOT NULL,
  company VARCHAR(100) NOT NULL,
  productType VARCHAR(100) NOT NULL,
  minAge INT(32),
  maxAge INT(32),
  price INT(32) NOT NULL,
  score DOUBLE(32,3) NOT NULL,
  PRIMARY KEY (idx)
);

#COVERAGE TABLE CREATEinsuranceinsurance
CREATE TABLE COVERAGE(
idx INT(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
insuranceIdx INT(11) UNSIGNED NOT NULL,
coverageType VARCHAR(255) NOT NULL,
amount INT(32) NOT NULL,
content VARCHAR(255),
PRIMARY KEY (idx),
FOREIGN KEY (insuranceIdx) REFERENCES INSURANCE (idx)
);

INSERT INTO INSURANCE(productName, company, productType, minAge, maxAge, price, score) VALUES('e건강치과보험','메리츠','치아보험',19,40,12000, 3.3);
INSERT INTO INSURANCE(productName, company, productType, minAge, maxAge, price, score) VALUES('이가탄탄보험','삼성화재','치아보험',25,50,8000, 4.5);
INSERT INTO INSURANCE(productName, company, productType, minAge, maxAge, price, score) VALUES('자동차11보험','동부화재','자동차보험',20,60,20000, 2.7);
INSERT INTO INSURANCE(productName, company, productType, minAge, maxAge, price, score) VALUES('차차차22보험','메리츠','자동차보험',30,60,15000, 3.6);
INSERT INTO INSURANCE(productName, company, productType, minAge, maxAge, price, score) VALUES('동부여행자보험','동부','여행자보험',13,70,30000, 2.9);
SELECT * FROM INSURANCE;

INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(1,'틀니',300000,'틀니 딱딱거릴 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(2,'임플란트',1000000,'임플란트 딱딱거릴 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(1,'발치',300000,'발치할 때 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(2,'틀니',10000,'틀니 덜덜거릴 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(2,'충치',90000,'열개이상 발견 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(2,'레진',500000,'세개이상 떼울 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(3,'자동차',120000,'교통 사고시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) values(4,'자동차',7600000,'보행자 쳤을 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(5,'비행기사고',100000,'비행기 추락 시 보장');
INSERT INTO COVERAGE(insuranceIdx, coverageType, amount, content) VALUES(5,'소매치기',10000,'소매치기 당했을 시 보장');
SELECT * FROM COVERAGE;




