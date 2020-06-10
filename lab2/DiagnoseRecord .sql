use database_lab2;

-- 不是所有的乘客都要计入这个表
drop table if exists `DiagnoseRecord`;
create table `DiagnoseRecord` (
    `DID` int PRIMARY KEY AUTO_INCREMENT,
	`PCardID` char(18) NOT NULL unique,
	`DDay` date NOT NULL,
	`DStatus` smallint NOT NULL,
	`FDay` date, -- 不一定发病，所以可以为NULL
	CHECK(DStatus>0 AND DStatus<4),
	foreign key (PCardID) references passenger(PCardID)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DELIMITER $$
create procedure createDiagnoseRecord()
begin
    -- 设置共有1000人经过了检测
    declare totalTestNum int(11) default 10000;
    declare PCardID1 char(18);
    declare DDay1 date;
    declare DStatus1 smallint;
    declare FDay1 date;
    declare done boolean default 0;
    declare tempDay date; -- 用于辅助判断日期的前后，由于要保证先发病再诊断
    -- 随机选出totalTestNum个人作为测试人数
    declare passenger_cursor cursor for select PCardID from (select * from passenger order by rand() limit totalTestNum) as temp;
    declare continue handler for not found set done = 1;
    open passenger_cursor;
    fetch passenger_cursor into PCardID1;
    -- 先发病，再诊断
    -- 所有乘车记录中，最晚是2019/12/01到2020/03/03，认为有3个月，再结合只查看发病前14天乘坐的列车，故开始日期设置为2019/12/15，范围为80天

    while not done do
        SET DDAY1=date_add('2019-12-15', interval floor(rand() * 80) day);
        -- 随机设置为确诊,疑似以及排除新冠
        set DStatus1 = floor(rand() * 3 + 1);
        if DStatus1 = 3
        then
            -- 如果排除了新冠,说明没有发病
            set FDay1 = NULL;
        else
            -- 按照常理,发病日期应该在检查前的14天之内
            set FDay1 = date_sub(DDay1, interval floor(rand() * 14 + 1) day);
        end if;
        insert into DiagnoseRecord (PCardID, DDay, DStatus, FDay) VALUES (PCardID1, DDay1, DStatus1, FDay1);
        fetch passenger_cursor into PCardID1;
    end while;

end $$;
DELIMITER ; -- 恢复分隔符

drop procedure createDiagnoseRecord;
call createDiagnoseRecord();

