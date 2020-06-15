create schema database_lab3;
use database_lab3;

-- 以MySQL为数据库
-- 1. 航班信息表
drop table if exists `PlaneInfo`;
create table `PlaneInfo`
(
    `Pid`      int PRIMARY KEY AUTO_INCREMENT,                               -- 航班唯一标示
    `SStation` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, -- 开始站
    `AStation` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, -- 目的站
    `STime`    datetime                                            NOT NULL, -- 出发时间
    `ATime`    datetime                                            NOT NULL, -- 到达时间
    `MaxCap`   int                                                 NOT NULL, -- 最大容纳人数
    `Company`  char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, -- 航班所属公司
    `Cost`     int                                                 NOT NULL  -- 金额
);
-- 用户信息表
drop table if exists `Users`;
create table `Users`
(
    `Uid`      int PRIMARY KEY AUTO_INCREMENT,                               -- 用户唯一标识
    `isAdmin`  boolean                                             NOT NULL, -- 是否是管理者
    `Email`    char(20)                                            NOT NULL, -- 邮箱，可以为空，实际操作的时候需要
    `Password` char(18)                                            NOT NULL, -- 密码，只存储哈希值
    `Username` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
);

-- 2. 订票信息表
drop table if exists `Order`;
create table `Order`
(
    `Oid`      int PRIMARY KEY AUTO_INCREMENT, -- 订单唯一标识
    `Uid`      int     NOT NULL,               -- 对应用户
    `Pid`      int     NOT NULL,               -- 对应航班
    `Canceled` boolean NOT NULL,               -- 是否退票


    CONSTRAINT `order_uid` foreign key (`Uid`) references `Users` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `plane_uid` foreign key (`Pid`) references `PlaneInfo` (`Pid`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- 3. 航班座位情况表


-- 4. 取票通知表
drop table if exists `Notification`;
create table `Notification`
(
    `Oid`      int      NOT NULL, -- 对应订单
    `Received` boolean  NOT NULL, -- 是否已经确认并缴费
    `NotiDate` datetime NOT NULL, -- 通知时间

    CONSTRAINT `order_oid` foreign key (`Oid`) references `Order` (`Oid`) ON DELETE RESTRICT ON UPDATE RESTRICT
);


select *
from PlaneInfo
where SStation = "上海"
  and AStation = "武汉"
  and date(STime) > "2020/10/1"
order by ATime;


insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("上海", "武汉", "2020-06-16 23:03:49", "2020-06-16 23:03:49", 20, "中国航空公司", 400);

-- 判断某一个航班是否满员
select count(*)
from `Order`
where Pid = 1
  and Canceled = false;

select count(*)
from `Order`
where Canceled = false
  and Pid = 1;

insert into Users (isAdmin, Email, Password, Username)
VALUES (true, "admin@qq.com", "admin", "管理员");

insert into `Order` (Uid, Pid, Canceled)
VALUES (1, 1, false);

select PlaneInfo.*, `Order`.Canceled, `Order`.Oid
from `Order`,
     `PlaneInfo`
where `Order`.Pid = PlaneInfo.Pid
  and `Order`.Uid = 1

update Order
set Canceled= true
where Oid = 1;

select *
from `Notification`,
     `Order`
where `Order`.Oid = Notification.Oid
  and `Order`.Uid = 1;


-- 时间在过去,并且已经缴费的人,才能计算满座率
select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime) `SDate`, PlaneInfo.MaxCap, count(*) `Count`
from Notification,
     PlaneInfo,
     `Order`
where PlaneInfo.Pid = `Order`.Pid
  and PlaneInfo.STime < now()
  and Notification.Oid = `Order`.Oid
  and Notification.Received = true
group by PlaneInfo.Pid;

-- 时间在未来,并且order没有取消的人,才能计算预定情况

select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime) `SDate`, PlaneInfo.MaxCap, count(*) `Count`
from  PlaneInfo,
     `Order`
where PlaneInfo.Pid = `Order`.Pid
  and PlaneInfo.STime > now()
  and `Order`.Canceled = false
group by PlaneInfo.Pid;


select PlaneInfo.Pid, PlaneInfo.SStation, PlaneInfo.AStation, date(PlaneInfo.STime), PlaneInfo.Cost from PlaneInfo, `Order`, Users, Notification
where PlaneInfo.Pid = `Order`.Pid and
      `Order`.Uid = Users.Uid and
      `Order`.Canceled = false and
      Notification.Received = true and
      Notification.Oid = `Order`.Oid and
      Users.Uid = 1;

select *
from Users;
select *
from PlaneInfo;
select *
from `Order`;
-- 用于生成通知
drop trigger if exists generateNotification;
create trigger generateNotification
    after insert
    on `Order`
    for each row
begin
    insert into Notification (Oid, Received, NotiDate)
    select new.Oid, false, date(PlaneInfo.STime)
    from PlaneInfo
    where PlaneInfo.Pid = new.Pid;
end






