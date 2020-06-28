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
    `Uid`       int PRIMARY KEY AUTO_INCREMENT,                               -- 用户唯一标识
    `isAdmin`   boolean                                             NOT NULL, -- 是否是管理者
    `Email`     char(20) UNIQUE                                     NOT NULL, -- 邮箱，可以为空，实际操作的时候需要
    `Password`  char(18)                                            NOT NULL, -- 密码，只存储哈希值
    `Username`  char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, -- 用户名
    `IDCardNum` char(20),                                                     -- 身份证号
    `PhoneNum`  char(20)                                                      -- 手机号
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


-- 4. 取票通知表
drop table if exists `Notification`;
create table `Notification`
(
    `Oid`      int      NOT NULL, -- 对应订单
    `Received` boolean  NOT NULL, -- 是否已经确认并缴费
    `NotiDate` datetime NOT NULL, -- 通知时间

    CONSTRAINT `order_oid` foreign key (`Oid`) references `Order` (`Oid`) ON DELETE RESTRICT ON UPDATE RESTRICT
);


-- 5. 座位信息
drop table if exists `Seats`;
create table `Seats`
(
    `Oid`    int,
    `SeatNo` int,

    constraint `seat_oid` foreign key (`Oid`) references `Order` (`Oid`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- 插入航班信息

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("上海", "武汉", "2020-06-16 18:03:49", "2020-06-16 22:03:49", 20, "中国航空公司", 400);

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("上海", "重庆", "2020-06-18 18:50:00", "2020-06-18 21:45:00", 25, "四川航空", 600);

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("上海", "重庆", "2020-06-18 19:15:00", "2020-06-18 22:15:00", 10, "东方航空", 600);

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("武汉", "重庆", "2020-06-25 17:20:00", "2020-06-25 18:55:00", 2, "测试人数", 620);


insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("武汉", "重庆", "2020-07-01 17:20:00", "2020-07-1 18:55:00", 2, "东方航空", 620);

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("武汉", "重庆", "2020-07-01 15:20:00", "2020-07-1 16:55:00", 2, "四川航空", 650);

insert into PlaneInfo (SStation, AStation, STime, ATime, MaxCap, Company, Cost)
VALUES ("武汉", "重庆", "2020-07-01 17:20:00", "2020-07-1 18:55:00", 2, "中国航空", 600);


-- 判断某一个航班是否满员
select count(*)
from `Order`
where Pid = 1
  and Canceled = false;

select count(*)
from `Order`
where Canceled = false
  and Pid = 1;


-- 管理员用户
insert into Users (isAdmin, Email, Password, Username)
VALUES (true, "admin@qq.com", "admin", "管理员");
-- 普通用户
insert into Users (isAdmin, Email, Password, Username)
VALUES (false, "test@test.com", "test", "测试用户1");


-- 时间在过去,并且已经缴费的人,才能计算满座率
select PlaneInfo.Pid,
       PlaneInfo.SStation,
       PlaneInfo.AStation,
       date(PlaneInfo.STime) `SDate`,
       PlaneInfo.MaxCap,
       count(*)              `Count`
from Notification,
     PlaneInfo,
     `Order`
where PlaneInfo.Pid = `Order`.Pid
  and PlaneInfo.STime < now()
  and Notification.Oid = `Order`.Oid
  and Notification.Received = true
group by PlaneInfo.Pid;

-- 时间在未来,并且order没有取消的人,才能计算预定情况

select PlaneInfo.Pid,
       PlaneInfo.SStation,
       PlaneInfo.AStation,
       date(PlaneInfo.STime) `SDate`,
       PlaneInfo.MaxCap,
       count(*)              `Count`
from PlaneInfo,
     `Order`
where PlaneInfo.Pid = `Order`.Pid
  and PlaneInfo.STime > now()
  and `Order`.Canceled = false
group by PlaneInfo.Pid;

update Users
set Username='default',
    Email='1@1.com',
    IDCardNum='420281199911280057',
    PhoneNum='15272052183'
where Uid = 1;


update Users
set Username='default',
    Email='1@1.com'
where Uid = 1;
-- 用于生成通知
drop trigger if exists generateNotification;
create trigger generateNotification
    after insert
    on `Order`
    for each row
begin
    insert into Notification (Oid, Received, NotiDate)
    select new.Oid, false, date(PlaneInfo.STime) - 1
    from PlaneInfo
    where PlaneInfo.Pid = new.Pid;
end;

-- 选择某一架航班的用户信息


select `Notification`.*
from `Notification`,
     `Order`
where `Order`.Oid = Notification.Oid
  and `Order`.Uid = 2;



-- String userName, String ID, String seatNo, long payed
-- 用于获取指定的航班的基本信息
-- 对于已经起飞的飞机，那么就只用计算满座率，认定预定并且已经缴费的人
select Users.Username, `Order`.Pid, Seats.SeatNo, true `payed`
from Users,
     `Order`,
     Notification,
     Seats
where Users.Uid = `Order`.Uid
  and `Order`.Oid = Notification.Oid
  and Notification.Received = true
and `Order`.Pid = 1
and Seats.Oid = `Order`.Oid;

-- 对于还没有起飞的飞机，计算