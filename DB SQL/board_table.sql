CREATE TABLE `replyInfo` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `boardIdx` int(11) NOT NULL,
  `authorIdx` int(11) NOT NULL,
  `date` varchar(30) NOT NULL,
  `content` varchar(100) NOT NULL,
  PRIMARY KEY (`idx`)
);
CREATE TABLE `boardUp` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `boardIdx` int(11) NOT NULL,
  `userIdx` int(11) NOT NULL,
  PRIMARY KEY (`idx`)
);

CREATE TABLE `boardInfo` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL COMMENT '게시글 type',
  `title` varchar(45) NOT NULL,
  `score` int(11) DEFAULT '5' COMMENT '보험 평점',
  `body` varchar(800) NOT NULL,
  `date` varchar(20) NOT NULL,
  `author` varchar(45) NOT NULL,
  `up` int(11) DEFAULT NULL COMMENT '추천수',
  `tag1` int(11) DEFAULT NULL COMMENT '어떤 보험에 대한 게시글인지',
  `tag2` int(11) DEFAULT NULL,
  `tag3` int(11) DEFAULT NULL,
  `tag4` int(11) DEFAULT NULL,
  `tag5` int(11) DEFAULT NULL,
  PRIMARY KEY (`idx`)
);
