use SExam;

-- 问题搜索

select *
from question q
WHERE -- teacherUsername
 IF(?1='empty', 1=1, ?1 like q.teacherName)
    AND -- difficulty
 IF(?2=-1, 1=1, q.difficulty=?2)
    AND -- score
 IF(?3=-1, 1=1, q.score=?3)
    AND -- qtype
 IF(?4=-1, 1=1, q.qtype=?4);


select * from question q WHERE IF(?1='empty', 1=1, ?1 like q.teacherName) AND IF(?2=-1, 1=1, q.difficulty=?2) AND IF(?3=-1, 1=1, q.score=?3) AND IF(?4=-1, 1=1, q.qtype=?4);

-- 试卷搜索
select r
from relations r,exam e,user_info u
where
    -- title
    if(?1='empty',1=1,?1 in select e.title from r,e where r.eid=e.eid)
    and -- studentName
    if(?2='empty',1=1,?2 in select u.name from r,u where r.studentUsername=u.username)
    and -- username
    if(?3='empty',1=1,?3=r.studentUsername)
    and -- teacherUsername
    ?4=r.teacherUsername
    and -- examState
    ?5=r.examState;

select r from relations r,exam e,user_info u where if(?1='empty',1=1,?1 in select e.title from r,e where r.eid=e.eid) and if(?2='empty',1=1,?2 in select u.name from r,u where r.studentUsername=u.username) and if(?3='empty',1=1,?3=r.studentUsername) and ?4=r.teacherUsername and ?5=r.examState;