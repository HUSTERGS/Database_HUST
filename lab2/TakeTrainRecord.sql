drop table if exists `seatNoOptions`;
create table `seatNoOptions` (
    `SeatNo` CHAR(1)
);
insert into seatNoOptions values ('A');
insert into seatNoOptions values ('B');
insert into seatNoOptions values ('C');
insert into seatNoOptions values ('E');
insert into seatNoOptions values ('F');

-- 建立表
DROP TABLE IF EXISTS `TakeTrainRecord`;
CREATE TABLE `TakeTrainRecord`(
	`RID` int PRIMARY KEY AUTO_INCREMENT,
	`PCardID` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
	`TID` int(11) NOT NULL, 
	`SStationID` int(11) NOT NULL, 
	`AStationID` int(11) NOT NULL, 
	`CarrigeID` smallint(6), 
	`SeatRow` smallint(6) NOT NULL,
	`SeatNo` CHAR(1),
	`SStatus` int NOT NULL,
    CHECK(`SeatNo`='A' OR `SeatNo`='B' OR `SeatNo`='C' OR `SeatNo`='E' OR `SeatNo`='F' OR `SeatNo` is null ),
	CHECK(`SStatus` = 0 OR `SStatus` = 1 OR `SStatus` =2),
	FOREIGN KEY(PCardID) REFERENCES passenger(PCardID),
	FOREIGN KEY(TID) REFERENCES train(TID)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DELIMITER $$ -- 临时调整分隔符

CREATE PROCEDURE createTakeTrainRecord()
begin 
	declare RID1 int(11);
	declare PCardID1 char(18);
	declare TID1 int(11);
	declare SStationID1 int(11);
	declare AStationID1 int(11);
	declare CarrigeID1 smallint(6);
	declare SeatRow1 smallint(6);
	declare SeatNo1 char(1);
	declare SStatus1 int;

	declare Stations int(11);

	declare maxTID int(11);
	declare minTID int(11);
	declare done boolean default 0;
	declare duplicated boolean default 0;
	-- 遍历乘客表,为每一个乘客安排其乘坐的列车
	declare passenger_cursor CURSOR for select PCardID from passenger;
	declare continue handler for not found set done = 1;

	select MAX(TID) from trainpass into maxTID;
	select MIN(TID) from trainpass into minTID;
	open passenger_cursor;
	fetch passenger_cursor into PCardID1;
    while not done do
#         select * from TakeTrainRecord;
        -- 随机选择范围之内的列车流水号,并查看其所有车次
        select floor(minTID + rand() * (maxTID - minTID)) into TID1;
        -- 随机选择两条记录作为初始站以及终点站
        create temporary table temp engine=memory select SID from (SELECT SID, Sno FROM (select  * from trainpass where TID = TID1) as temp ORDER BY RAND() LIMIT 2) as SS order by Sno;
        select SID from temp limit 0, 1 into SStationID1;
        select SID from temp limit 1, 1 into AStationID1;
        drop temporary table if exists temp;
        -- 设置车厢号为1-9中的随机值,其中9用于标示站票,即空值
        set CarrigeID1 = floor(rand() * 9 + 1);
        if (CarrigeID1 = 9)
        then set CarrigeID1 = NULL;
        end if;
        -- 车厢共有20排
        set SeatRow1 = floor(rand() * 20 + 1);
        -- 在'A', 'B', 'C', 'E', 'F'中随机选取值
        select SeatNo from seatNoOptions order by rand() limit 1 into SeatNo1;
        -- 如果是站票则没有座位
        if CarrigeID1 is null
        then
            set SeatNo1 = NULL;
        end if;
        -- 0/1/2
        set SStatus1 = floor(rand() * 3);
        -- 如果已经有不同的人的记录,那么久不进行插入操作
        if not (select exists(select TID, SStationID, AStationID, CarrigeID, SeatRow, SeatNo, SStatus from TakeTrainRecord
                where TID = TID1 and
                      SStationID = SStationID1 and
                      AStationID = AStationID1 and
                      CarrigeID = CarrigeID1 and
                      SeatRow = SeatRow1 and
                      SeatNo = SeatNo1 and
                      SStatus = SStatus1
            ))
        then
            insert into TakeTrainRecord (PCardID, TID, SStationID, AStationID, CarrigeID, SeatRow, SeatNo, SStatus)
            values (PCardID1, TID1, SStationID1, AStationID1, CarrigeID1, SeatRow1, SeatNo1, SStatus1);
            fetch passenger_cursor into PCardID1;
        end if;
        end while;
end $$;

drop procedure createTakeTrainRecord;

DELIMITER ; -- 恢复分隔符
call createTakeTrainRecord();

select * from TakeTrainRecord;
select max(SStationID) from TakeTrainRecord;

