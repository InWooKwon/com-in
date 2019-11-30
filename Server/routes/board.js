var express = require('express');
var router = express.Router();

var moment = require('moment'); //시간정보 받아오기 위해서 (npm install moment, npm install moment-timezone)
require('moment-timezone');
moment.tz.setDefault("Asia/Seoul");

var connection = require("../DBconfig.js").connection;

var updatescore = function(tag) {
    // Do Something
    //별점 업데이트
    //score 합산 boardInfo 적용
    var count_score = 0;
    var count_user = 0;
    var insure_score = 0;
    console.log("tag : "+tag);

    var qry="SELECT * FROM boardInfo WHERE type =1 AND tag1 = "+tag;
    console.log(tag);
    console.log(qry);

    connection.query(qry,function(err,rows,fields){
        console.log("rows length : "+rows.length);
        count_user = rows.length;
        for(var i=0; i<count_user; i++) {
            count_score = count_score + rows[i].score;
        }
        insure_score=parseFloat(count_score/count_user).toFixed(2);
        var qry2= "UPDATE INSURANCE SET score = " + insure_score + " WHERE idx = "+tag;
        console.log("test update insure_score : "+insure_score);
        connection.query(qry2,function(err,rows,fields){
            var score=false;
            if(err){
                console.log("error : update insurance score");
                score=false;
            }
            else{
                score=true;
                console.log("score : "+score.toString());
                return score;
            }
        })
    });

   
  };

//게시판 가져오기 
router.get('/',function(req,res){
    var qry="SELECT * FROM boardInfo order by date desc;";
    connection.query(qry,function(err,rows,fields){
        if(err){
            console.log("error: getting in board");
        }
        var rst={"board":rows};
        res.json(rst);
    });
}); 
router.get('/:boardidx/:useridx',function(req,res){
    var boardidx=req.params.boardidx;
    var useridx=req.params.useridx;
    var qry="SELECT * FROM boardInfo where idx="+boardidx;
    console.log(qry);
    connection.query(qry,function(err,rows,fields){
        if(err){
            console.log("error: getting in board");
        }
        var qry1="SELECT * FROM boardUp where boardIdx = "+boardidx+" and userIdx = "+useridx;
        console.log("board get : "+boardidx+"     "+useridx);
        connection.query(qry1,function(err,check,fields){
            if(err){
                console.log("error:getting in reply");
            }
            else{
                var check=false;
                if(check.length>0){ // 눌럿으면 true;
                    check=true;
                }
                else{ // 안눌럿으면 false
                    check=false;
                }
                var rst={"board":rows,
                        "check":check};
                res.json(rst);
            }
        });
    });

    
});
/*router.get('/:idx',function(req,res){
    var idx=req.params.idx;
    var qry="SELECT * FROM boardInfo where idx="+idx;
    console.log(qry);
    connection.query(qry,function(err,rows,fields){
        if(err){
            console.log("error: getting in board");
        }
        var rst={"board":rows};
        res.json(rst);
    });
});*/ 
//게시글에 추천하기
router.post('/up',function(req,res){
    var body = req.body;
    var index = body.index;
    var user = body.user;
    var up = body.up; //현재 게시글의 추천 수
    var rst={"success":false, "up":up};

    //이미 추천했는지 확인
    var qry2 = "SELECT * FROM boardUp WHERE boardIdx = \'"+index+"\' AND userIdx= \'"+user+"\'";
        connection.query(qry2,function(err,rows,fields){
            if(err){
                console.log("error");
            }
            else{
                    var qry1="INSERT INTO boardUp (boardIdx, userIdx) values (\'"+index+"\',\'"+user+"\')"
                    connection.query(qry1,function(err,rows,fields){
                        
                        if(err){
                            console.log("error: recommend in reviewboard");
                            rst.success=false;
                            res.json(rst);
                        }
                        else{
                            var qry3 = "UPDATE boardInfo SET up = up+1 WHERE idx= "+index;
                            connection.query(qry3,function(err,rows,fields){
                                var rst={"success":false, "up":up};
                                if(err){
                                    rst.success=false;
                                    console.log("board table(추천 수) update error");
                                }
                                else{
                                    console.log("update");
                                    rst.up=up+1;
                                    rst.success=true;
                                    res.json(rst); //success=true 값 전송되면 추천 완료
                                }
                            });       
                        }
                    });
            }
        });
});

//게시글 추천 취소
router.post('/down',function(req,res){
    var body = req.body;
    var index = body.index;
    var user = body.user;
    var up = body.up;
   

    //이미 추천했는지 확인
    var qry2 = "SELECT * FROM boardUp WHERE boardIdx = \'"+index+"\' AND userIdx= \'"+user+"\'";
        connection.query(qry2,function(err,rows,fields){
            if(err){
                console.log("error");
            }
            else{
                if(rows.length>0){
                //추천 했을 경우 삭제
                var qry1="DELETE FROM boardUp WHERE idx = "+rows[0].idx;
                connection.query(qry1,function(err,rows,fields){
                    var rst={"success":false, "dup":false};
                   
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
                            }
                        });       
                    }
                    res.json(rst); //success=true 값 전송되면 추천 완료
                });
                }
                else{
                    console.log("they don't recommend");
                }
            }

            
        });
});


//게시글 삭제
router.delete('/:idx',function(req,res){
    var qry = "DELETE FROM boardInfo WHERE idx = ?";
    var qry2 = "SELECT * FROM boardInfo WHERE idx =?";
    console.log(req.params.idx);
    connection.query(qry2,req.params.idx,function(err,rows,fields){
        var rst = {"success":false};
        if(err){     
            rst.success=false;
            console.log("there is no board in db");
        }
        else{
            rst.success=true;
            connection.query(qry,req.params.idx,function(err,rows_delete,fields){
                
                if(err){
                    console.log("error: delete board");
                }
                else{
                    updatescore(rows[0].tag1);
                }
            })
        }
        res.json(rst);

    })
   
})

//해당 개시글의 답글 불러오기
router.get('/reply/reply/:idx',function(req,res){
    var idx=req.params.idx;
    var qry="SELECT * FROM replyInfo WHERE boardIdx ="+idx;
    console.log(qry);
    connection.query(qry,function(err,rows,fields){
        if(err)
            console.log("error: getting in reply");
        else{
            var rst={"reply":rows};
            res.json(rst);
        }
    })
})

//해당 게시글에 답글쓰기
router.post('/reply',function(req,res){
    var body = req.body;
    console.log(body);
    var index = body.index;
    var user = body.user;
    var date = moment().format('YYYY-MM-DD HH:mm:ss');
    var content = body.content;
    var qry="INSERT INTO replyInfo (boardIdx, authorIdx, date, content) values ("+index+","+user+",\'"+date+"\',\'"+content+"\')";
    connection.query(qry,function(err,rows,fields){
        var rst={"success":false};
        if(err)
            console.log("error: can't write reply");
        else{
            
            rst.success=true;
            res.json(rst);
        }
    });
});

//답글 삭제
router.delete('/reply/:idx',function(req,res){
    var index = req.params.idx;
    
    var qry = "DELETE FROM replyInfo WHERE idx = ?";
    connection.query(qry,index,function(err,rows,fields){
        var rst = {"success":false};
        if(err){
            console.log("error: delete board");
        }
        else{
            rst.success = true;
            res.json(rst);
        }
    })
})

router.put('/:idx',function(req,res){

    var body= req.body;
    var index = req.params.idx;
    var content = body.body;
    var qry = 'UPDATE boardInfo SET body = \''+content+'\' WHERE idx = ?';
    
    connection.query(qry,index,function(err,rows){
        var rst = {"success":false};
        if(error){
            console.log("error : error modify")
            rst.success=false;
        }
        else{
            rst.success=true;
            
        }
        res.json(rst);
    })
});

//데이터 넣기
router.post('/',function(req,res){
    //front에서 게시판 쓸 정보 받아옴.
    var body = req.body;
    console.log(body);
    var user = body.author;
    var title=body.title;
    var score = body.score;
    var content = body.body
    var type = body.type;
    var date = moment().format('YYYY-MM-DD HH:mm:ss');
    var tag1 =body.tag1 ? body.tag1 : 0; //쓸 보험 index
    var tag2 = body.tag2 ? body.tag2 : 0;
    var tag3 = body.tag3 ? body.tag3 : 0;
    var tag4 = body.tag4 ? body.tag4 : 0;
    var tag5 = body.tag5 ? body.tag5 : 0;
    var rst={"success":false};
    

    if(type==1){
        //중복 확인 (같은 보험에 같은 유저인지)
        var qry="SELECT * FROM boardInfo WHERE author = \'"+user+"\' AND type=1 "+"AND tag1= "+tag1+"";
        var qry2="INSERT INTO boardInfo (type, title, score, body, date,author, tag1) values (1,\'"+title+"\',"+score+",\'"+content+"\',\'"+date+"\',\'"+user+"\',"+tag1+")";
        console.log(qry);
        console.log(qry2);
        connection.query(qry,function(err,rows,fields){
            if(err){//보드에 작성
                console.log("error: write in reviewboard");
            }
            else{
                if(rows.length>0){
                    console.log("insert fail");
                }
                else{
                    connection.query(qry2,function(err,rows_insert,fields){

                        if(err){
                            console.log("error: write in reviewboard");
                            rst.success=false;
                        }
                        else{
                            //score
                            rst.success=true;
                            console.log("rst score : "+rst.success.toString());
                            updatescore(tag1);
                            
                        }
                        res.json(rst);
                    });
                }
            }
            
        });

        
    }

    else{
        var qry="INSERT INTO boardInfo (type, title, body, date,author, tag1, tag2, tag3, tag4, tag5) values ("+
        type+",\'"+title+"\',\'"+content+"\',\'"+date+"\',\'"+user+"\'"+","+
        tag1+","+tag2+","+tag3+","+tag4+","+tag5+")";
        connection.query(qry,function(err,rows,fields){
            if(err){
                console.log(err);
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

