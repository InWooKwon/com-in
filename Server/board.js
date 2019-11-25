var moment = require('moment'); //시간정보 받아오기 위해서 (npm install moment, npm install moment-timezone)
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");

var express = require('express');
var http = require('http');
var bodyparser=require('body-parser');
var app=express(); 
var router = express.Router();


app.use(bodyparser.json());



var connection = require("./DBconfig.js").connection;
connection.connect();

//게시판 가져오기 
app.get('/board',function(req,res){
    var qry="SELECT * FROM boardInfo";
    connection.query(qry,function(err,rows,fields){
        if(err){
            console.log("error: getting in board");
        }
        var rst={"board":rows};
        res.json(rst);
    });
}); 

//게시글에 추천하기
app.post('/board/up',function(req,res){
    var body = req.body;
    var index = body.index;
    var user = body.user;
    var up = body.up; //현재 게시글의 추천 수
    var rst={"success":false, "dup":true, "up":up};

    //이미 추천했는지 확인
    var qry2 = "SELECT * FROM boardUp WHERE boardIdx = \'"+index+"\' AND userIdx= \'"+user+"\'";
        connection.query(qry2,function(err,rows,fields){
            if(err){
                console.log("error");
            }
            else{
                
                if(rows.length>0) //이미존재
                {
                    console.log("already up");
                    rst.dup=true;
                }
                else{ //올리기 가능
                    //추천 가능일 경우 추천 테이블에 등록
                    var qry1="INSERT INTO boardUp (boardIdx, userIdx) values (\'"+index+"\',\'"+user+"\')"
                    connection.query(qry1,function(err,rows,fields){
                        if(err){
                            console.log("error: recommend in reviewboard");
                            rst.success=false;
                        }
                        else{
                            rst.success=true;
                            var qry3 = "UPDATE boardInfo SET up = up+1 WHERE idx= "+index;
                            connection.query(qry3,function(err,rows,fields){
                                if(err){
                                    console.log("board table(추천 수) update error");
                                }
                                else{
                                    console.log("update");
                                    rst.up=up+1;
                                    res.json(rst); //success=true 값 전송되면 추천 완료
                                }
                            });       
                        }
                    });
                }
            }
        });
});

//게시글 추천 취소
app.post('/board/down',function(req,res){
    var body = req.body;
    var index = body.index;
    var user = body.user;
    var up = body.up;
    var rst={"success":false, "dup":false, "up":up};

    //이미 추천했는지 확인
    var qry2 = "SELECT * FROM boardUp WHERE boardIdx = \'"+index+"\' AND userIdx= \'"+user+"\'";
        connection.query(qry2,function(err,rows,fields){
            if(err){
                console.log("error");
            }
            else{
                if(rows.length>0){
                //추천 가능일 경우 추천 테이블에 등록
                var qry1="DELETE FROM boardUp WHERE idx = "+rows[0].idx;
                connection.query(qry1,function(err,rows,fields){
                    if(err){
                        console.log("error: recommend in reviewboard");
                        rst.success=false;
                    }
                    else{
                        rst.success=true;
                        var qry3 = "UPDATE boardInfo SET up = up-1 WHERE idx= "+index;
                        connection.query(qry3,function(err,rows,fields){
                            if(err){
                                console.log("board table(추천 수) update error");
                            }
                            else{
                                console.log("update");
                                rst.up=up-1;
                                res.json(rst); //success=true 값 전송되면 추천 완료
                            }
                        });       
                    }
                });
                }
                else{
                    console.log("they don't recommend");
                }
            }

            
        });
});

//게시글 삭제
app.delete('/board/:idx',function(req,res){
    var body = req.body;
    var index = body.index;
    var rst = {"success":false};
    var qry = "DELECT FROM boardInfo WHERE idx = ?";
    connection.query(qry,req.param.idx,function(err,rows,fields){
        if(err){
            console.log("error: delete board");
        }
        else{
            rst.success = true;
            res.json(rst);
        }
    })
})

//해당 개시글의 답글 불러오기
app.get('/board/reply/:idx',function(req,res){
    var body = req.body;
    var qry="SELECT * FROM replyInfo WHERE boardIdx =?";
    connection.query(qry,req.param.idx,function(err,rows,fields){
        if(err)
            console.log("error: getting in reply");
        else{
            var rst={"reply":rows};
            res.json(rst);
        }
    })
})

//해당 게시글에 답글쓰기
app.post('/board/reply',function(req,res){
    var body = req.body;
    var index = body.index;
    var user = body.user;
    var date = moment().format('YYYY-MM-DD HH:mm:ss');
    var content = body.body;
    var qry="INSERT INTO replyInfo (boardIdx, authorIdx, date, content) values (\'"+index+"\',\'"+user+"\',\'"+date+"\',\'"+content+"\')";
    var rst={"success":false};
    connection.query(qry,function(err,rows,fields){
        if(err)
            console.log("error: can't write reply");
        else{
            
            rst.success=true;
            res.json(rst);
        }
    });
});

//답글 삭제
app.delete('/board/reply/:idx',function(req,res){
    var body = req.body;
    var index = body.index;
    var rst = {"success":false};
    var qry = "DELECT FROM replyInfo WHERE idx = ?";
    connection.query(qry,req.param.index,function(err,rows,fields){
        if(err){
            console.log("error: delete board");
        }
        else{
            rst.success = true;
            res.json(rst);
        }
    })
})

app.put('/board/:idx',function(req,res){

    var body= req.body;
    var index = body.index;
    var content = body.body;
    var qry = 'UPDATE boardInfo SET body = \''+content+'\' WHERE idx = ?';
    var rst = {"success":false};
    connection.query(qry,req.param.idx,function(err,rows){
        if(error){
            console.log("error : error modify")
            rst.success=false;
        }
        else{
            rst.success=true;
            res.json(rst);
        }

    })
 

});

//데이터 넣기
app.post('/board',function(req,res){
    //front에서 게시판 쓸 정보 받아옴.
    var body = req.body;
    var user = body.user;
    var title=body.title;
    var score = body.score;
    var content = body.body
    var type = body.type;
    var date = moment().format('YYYY-MM-DD HH:mm:ss');
    var tag1 =body.tag1; //쓸 보험 index
    var tag2 = body.tag2;
    var tag3 = body.tag3;
    var tag4 = body.tag4;
    var tag5 = body.tag5;

    if(type==1){
        //중복 확인 (같은 보험에 같은 유저인지)
        var qry="SELECT * FROM boardInfo WHERE author = \'"+user+"\' AND type=1 "+"AND tag1= \'"+tag1+"\'";
        var qry2="INSERT INTO boardInfo (type, title, score, body, date,author, tag1) values (1,\'"+title+"\',\'"+score+"\',\'"+content+"\',\'"+date+"\',\'"+user+"\',\'"+tag1+"\')";
        console.log(qry);
        console.log(qry2);
        connection.query(qry,function(err,rows,fields){
            var rst={"success":false,"score":false};
            //score 합산 boardInfo 적용
            var count_score = 0;
            var count_user = 0;
            var insure_score = 0; 

            console.log("rows :"+rows.length);

            if(err){//보드에 작성
                console.log("error: write in reviewboard")
            }
            else{
                if(rows.length>0){
                    rst.success=false;
                    console.log("insert fail");
                }
                else{
                    connection.query(qry2,function(err,rows,fields){
                        if(err){
                            console.log("error: write in reviewboard");
                            rst.success=false;
                        }
                        else{
                            rst.success=true;
                        }
                    
                    });
                }
            }

            //score 업데이트
            var qry3 = "SELECT * FROM boardInfo WHERE tag1 = "+tag1+" AND type = 1";
            console.log(qry3);
             connection.query(qry3,function(err,rows,fields){
            if(err){
                console.log("error: update score in boardInfo > get total score of the reviewboard");
                rst.success = false;
            }
            else{
                count_user = rows.length;
                for(var i=0; i<count_user; i++) {
                    count_score = count_score + rows[i].score;
                }
                insure_score=parseFloat(count_score/count_user).toFixed(2);
                var qry4= "UPDATE INSURANCE SET score = " + insure_score + " WHERE idx = "+tag1;
                console.log("insure_score"+insure_score);
                console.log(qry4);
                connection.query(qry4,function(err,rows,fields){
                    if(err){
                        console.log("error : update insurance score");
                        console.log(err);
                        rst.score=false;
                    }
                    else{
                        rst.score=true;
                    }
                })
            }
            });
      
            res.json(rst);
        });

        
    }

    else{
        var qry="INSERT INTO boardInfo (type, title, body, date,author, tag1, tag2, tag3, tag4, tag5) values ("+type+",\'"+title+"\',\'"+content+"\',\'"+date+"\',\'"+user+"\',\'"+tag1+"\',\'"+tag2+"\',\'"+tag3+"\',\'"+tag4+"\',\'"+tag5+"\')";
        connection.query(qry,function(err,rows,fields){
            if(err){
                console.log("error: write in questionboard & freeboard");
                rst.success=false;
                res.json(rst); //success=true 값 전송되면 게시글 작성 완료
            }
            else{
                rst.success=true;
                res.json(rst); //success=true 값 전송되면 게시글 작성 완료
            } 
            
        });
    }
}); 


module.exports = router;

http.createServer(app).listen(9090,function(){
    console.log('서버 실행중...');
});

