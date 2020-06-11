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


select *
from PlaneInfo
where SStation = "上海"
  and AStation = "武汉"
  and date(STime) > "2020/10/1" order by ATime;
