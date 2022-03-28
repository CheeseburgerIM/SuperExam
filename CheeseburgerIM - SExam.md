# CheeseburgerIM

## SuperExam

<center>后端域名&接口文档：<a href="https://sexam.api.cheeseburgerim.space" target="_blank">https://sexam.api.cheeseburgerim.sapce</a></center>

<center>项目静态资源：<a href="https://sexam.static.cheeseburgerim.space" target="_blank">https://sexam.static.cheeseburgerim.space+相对路径</a></center>

### Interface

```java
// 接口返回结果
return json {
    status: Integer,
    result: Object,
    message: String
}
// 请求状态码
status {
    200: success,
    403: failure
}
```

### User

```java
// 注册
{{domain}}/user/api/register;
参数 {
    username String,
    password String,
    name String,
    email String,
    phoneNumber String,
    school String,
    sex String,
    avatar File
}
// 登录
{{domain}}/user/api/login;
参数 {
    username String,
    password String
}
结果 {
    String token
}
// 修改个人信息
{{domain}}/user/api/change/info;
参数 {
    username String,
    name String,
    sex String,
    email String,
    school String
}
// 修改密码
{{domain}}/user/api/change/password;
参数 {
    username String,
    oldPwd String 旧密码,
    newPwd String 新密码
}
// 获取个人信息
{{domain}}/user/api/get/info;
参数 {
    username String
}
// 获取全部用户
{{domain}}/user/api/get/all;
参数 {
    null
}
// 获取用户活跃度
{{domain}}/user/api/get/activity;
参数 {
    username String
}
// 获取用户日志
{{domain}}/user/api/get/log;
参数 {
    username String
}
```

### Question

```java
// 上传题目
{{domain}}/question/api/upload;
参数 {
    teacherUsername String,
    teacherName String,
    title String,
    answer String,
    analysis String,
    items String,
    score double,
    difficulty double,
    qtype int
}
// 编辑题目
{{domain}}/question/api/edit;
参数 {
    qid String,
    title String,
    answer String,
    analysis String,
    items String,
    score double,
    difficulty double,
    qtype int
}
// 上传题目中的图片
{{domain}}/question/api/upload/image;
参数 {
    image File
}
结果 {
    String 图片相对路径
}
// 获取一道题的信息
{{domain}}/question/api/get;
参数 {
    qid String
}
// 获取全部题目
{{domain}}/question/api/search;
参数 {
    null
}
// 获取学生错题列表
{{domain}}/question/api/wrong;
参数 {
    username String
}
// 斩掉一道错题
{{domain}}/question/api/kill;
参数 {
    qid String 问题id,
    eid String 试卷id,
    studentUsername String 学生用户名
}
// 获取题目分析
{{domain}}/question/api/analyze;
参数 {
    qid String 问题id
}
```

### Class

```java
// 创建班级
{{domain}}/class/api/create;
参数 {
    username String 教师账号,
    cname String 班级名称,
    teacherName String 教师名
}
// 编辑班级信息
{{domain}}/class/api/edit;
参数 {
    cid String 班级id,
    cname String 班级名称
}
//根据班级口令模糊搜索匹配的课程
{{domain}}/class/api/searchClass
参数 {
    command String 班级口令
}
// 学生输入班级口令进入班级
{{domain}}/class/api/enter;
参数 {
    username String 学生账号,
    command String 班级口令
}
// 教室邀请学生加入班级
{{domain}}/class/api/invite;
参数 {
    cid String 课程id,
    username String 学生账号,
    teacherName String 教室昵称
}
// 教室解散班级
{{domain}}/class/api/dismiss;
参数 {
    cid String 课程id
}
// 教室踢出某个学生
{{domain}}/class/api/kick;
参数 {
    cid String 课程id,
    studentUsername String 学生账号
}
// 搜索班级
{{domain}}/class/api/searchClass;
参数 {
    command String
}
// 获取某个班级的信息
{{domain}}/class/api/get/single;
参数 {
    cid String 课程id
}
// 获取某个老师创建的全部班级
{{domain}}/class/api/get/all;
参数 {
    teacherUsername String 教室账号
}
// 获取系统中创建的全部班级
{{domain}}/class/api/search;
参数 {
    null
}
// 管理端主页概览
{{domain}}/class/api/info;
参数 {
    username String 教室账号
}
// 获取班级全部学生
{{domain}}/class/api/get/student;
参数 {
    cid String 班级id
}
// 获取某一学生加入的全部班级
{{domain}}/class/api/get/student/class;
参数 {
    username String 学生id
}
```

### Module

```java
// 创建试卷模块
{{domain}}/module/api/create;
参数 {
    title String 模块名称,
    questionsId String 模块中包含的问题id
}
// 管理端模块分析
{{domain}}/module/api/analyze;
参数 {
    mid String 模块id
}
```

### Exam

```java
// 创建试卷
{{domain}}/exam/api/create;
参数 {
    teacherUsername String 教师账号,
    title String 试卷名称,
    tips String 考试注意事项,
    startTime String 考试开始日期,
    endTime String 考试结束日期,
    examClass String 考试班级,
    course String 考试科目,
    duration int 考试时间(以分钟为单位),
    totalScore double 试卷总分,
    questionCnt int 试卷问题总数,
    modules List<module> 试卷模块列表
}
// 编辑试卷
{{domain}}/exam/api/edit;
参数 {
    eid String 试卷id,
    teacherUsername String 教师账号,
    title String,
    tips String,
    startTime String,
    endTime String,
    examClass String,
    course String,
    duration int,
    totalScore double,
    questionCnt int,
    modules List<module>
}
// 获取某一试卷的信息
{{domain}}/exam/api/getInfo;
参数 {
    eid String 试卷id
}
// 开始考试
{{domain}}/exam/api/startExam;
参数 {
    eid String 试卷id,
    username String 学生账号
}
// 获取试卷题目
{{domain}}/exam/api/getQuestions;
参数 {
    eid String 试卷id
}
// 提交试卷
{{domain}}/exam/api-/submit;
参数 {
    username String 学生账号,
    eid String 试卷id,
    questionList List<Question> 题目列表
}
// 查看待批改或者已批改试卷（用一个就行）
{{domain}}/exam/api/getAnswerPaper;
参数 {
    studentUsername String 学生账号,
    eid String 试卷id
}
// 提交审批
{{domain}}/exam/api/correct;
参数 {
    studentUsername String 学生账号,
    eid String 试卷id,
    teacherUsername String 教师账号,
    correctList List<Correct>
}
// 教师端答卷管理
{{domain}}/exam/api/search;
参数 {
    teacherUsername String 教师账号,
    examState int 试卷状态
}
// 学生端的已提交试卷
{{domain}}/exam/api/getExams;
参数 {
    username String 学生账号
}
// 班级顶部结果分析
{{domain}}/exam/api/getExamAnalyze;
参数 {
    eid String 试卷id,
    studentUsername String 学生账号,
    teacherUsername String 教师账号
}
// 学生端试卷分析
{{domain}}/exam/api/getMyAnalyze;
参数 {
    username String 学生账号,
    eid String 试卷id
}
// 获取某个老师的全部试卷
{{domain}}/exam/api/getExamsByTeacher;
参数 {
    teacherUsername String 教师账号
}
//获取某个学生的待考试列表
{{domain}}/exam/api/getExamByUsername
参数 {
    username String 学生账号
}
//判定试卷作弊
{{domain}}/exam/api/cheat
参数 {
    eid String 试卷id
    teacherUsername String 教师账号
    studentUsername String 学生账号
}
```

### Message

```java
// 获取学生全部信息
{{domain}}/message/api/get/all;
参数 {
    username String 学生账号
}
// 读取某一条消息
{{domain}}/message/api/read;
参数 {
    mid String 消息id
}
```

### Screenshot

```java
// 保存截图
{{domain}}/screenshot/api/take;
参数 {
    eid String 试卷id,
    studentUsername String 学生账号,
    image File 截图
}
结果 {
    图片相对路径
}
// 获取截图
{{domain}}/screenshot/api/get;
参数 {
    eid String 试卷id,
    studentUsername String 学生账号
}
```