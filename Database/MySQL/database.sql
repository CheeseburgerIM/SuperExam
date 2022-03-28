create database SExam;

use SExam;

create table sexam_user
(
    username VARCHAR(17) PRIMARY KEY,
    password VARCHAR(17),
    status   VARCHAR(7)
);

create table user_info
(
    username     VARCHAR(17) PRIMARY KEY,
    email        VARCHAR(70),
    name         VARCHAR(17),
    phoneNumber  VARCHAR(11),
    school       VARCHAR(17),
    sex          VARCHAR(7),
    avatarPath   VARCHAR(270),
    registerTime VARCHAR(30),
    warTimes     INTEGER,
    questions    INTEGER,
    overcomes    INTEGER,
    accuracy     INTEGER,
    averagePoint INTEGER,
    velocity     INTEGER,
    excellent    INTEGER
);

create table user_calendar
(
    username VARCHAR(17),
    cdate    VARCHAR(17),
    activity INTEGER,
    PRIMARY KEY (username, cdate)
);

create table exam
(
    eid             VARCHAR(40) PRIMARY KEY,
    teacherUsername VARCHAR(27),
    title           VARCHAR(27),
    tips            varchar(300),
    startTime       VARCHAR(30),
    endTime         VARCHAR(30),
    course          VARCHAR(14), -- subject
    duration        INTEGER,
    modulesId       VARCHAR(300),
    totalScore      DECIMAL(4, 1),
    questionCnt     INTEGER
);

create table module
(
    mid         VARCHAR(40) PRIMARY KEY,
    title       VARCHAR(27),
    questionsId VARCHAR(700)
);

create table messages
(
    mid      VARCHAR(40),
    username VARCHAR(27),
    title    VARCHAR(27),
    mdate    VARCHAR(30),
    content  VARCHAR(270),
    mstate   INTEGER,
    mtype    VARCHAR(7),
    PRIMARY KEY (mid, username)
);

create table classes
(
    cid           VARCHAR(40) PRIMARY KEY,
    username      VARCHAR(27), -- teacherUsername
    cname         VARCHAR(17),
    teacherName   VARCHAR(17),
    studentNumber INTEGER,
    studentList   VARCHAR(800),
    command       VARCHAR(16),
    examCnt       INTEGER
);

create table classstudent
(
    username VARCHAR(40),
    cid      VARCHAR(40),
    PRIMARY KEY (username, cid)
);

create table classexam
(
    cid VARCHAR(40),
    eid VARCHAR(40),
    PRIMARY KEY (cid, eid)
);

create table relations
(
    eid             VARCHAR(40),
    teacherUsername VARCHAR(40),
    studentUsername VARCHAR(27),
    score           DECIMAL(4, 1), -- 学生试卷得分
    startTime       VARCHAR(30),   -- 学生开始考试时间
    submitTime      VARCHAR(30),
    rightCnt        INTEGER,
    examState       INTEGER,       -- -1未做 0待批 1已批,
    PRIMARY KEY (eid, teacherUsername, studentUsername)
);

create table question
(
    qid             VARCHAR(40) PRIMARY KEY,
    teacherUsername VARCHAR(27),
    teacherName     VARCHAR(27),
    title           VARCHAR(300),
    answer          VARCHAR(300),
    analysis        VARCHAR(300),
    items           VARCHAR(500),
    score           DECIMAL(3, 1),
    difficulty      DECIMAL(2, 1),
    count           INTEGER, -- 出现次数
    rightCnt        INTEGER, -- 答对次数
    qtype           INTEGER  -- >0
);

create table doneQuestion
(
    qid             VARCHAR(40),
    eid             VARCHAR(40),
    studentUsername VARCHAR(17),
    answer          VARCHAR(300),
    studentAnswer   VARCHAR(300),
    score           DECIMAL(3, 1),
    qtype           INTEGER,
    status          INTEGER, -- -1错误 0待批改 1正确 2一批改
    isOvercome      INTEGER, -- 0 未斩 1 已斩
    PRIMARY KEY (qid, eid, studentUsername)
);

create table user_log
(
    username VARCHAR(40),
    ldate VARCHAR(30),
    content VARCHAR(70),
    PRIMARY KEY(username,ldate)
);

create table screenshot
(
    studentUsername VARCHAR(40),
    eid VARCHAR(40),
    imagePath VARCHAR(270),
    PRIMARY KEY(eid,studentUsername)
);
