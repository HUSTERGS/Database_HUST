-- 1.分别用一条sql语句完成对乘车记录表基本的增、删、改的操作
-- 增
INSERT INTO TakeTrainRecord(PCardID, TID, SStationID, AStationID, CarrigeID, SeatRow, SeatNo, SStatus)
VALUES ('110101193412052028', 37363, 7, 10, 3, 19, 'E', 1);
-- 改
UPDATE TakeTrainRecord
SET SStatus = 2
WHERE (PCardID = '110101193412052028' AND TID = 37363);
-- 删
DELETE
FROM TakeTrainRecord
WHERE (PCardID = '110101193412052028' AND TID = 37363);

-- 2.将乘车记录表中的从武汉出发的乘客的乘车记录插入到一个新表WH_TakeTrainRecord中。
DROP TABLE IF EXISTS WH_TakeTrainRecord;
CREATE TABLE WH_TakeTrainRecord AS
    (SELECT *
     FROM TakeTrainRecord
     WHERE SStationID IN
           (SELECT SID FROM station WHERE CityName = '武汉'));

-- 3. 手动操作
-- 4. 手动操作
-- 5. 创建视图

CREATE TABLE TestTable
(
    ID   CHAR(3),
    Name CHAR(30)
);


INSERT INTO TestTable(ID, Name)
VALUES ('1', 'a');
INSERT INTO TestTable(ID, Name)
VALUES ('1', 'a');
INSERT INTO TestTable(ID, Name)
VALUES ('1', 'a');

SELECT*
FROM TestTable;


-- 创建视图
CREATE VIEW PassengerDiag
AS
(
SELECT DiagnoseRecord.PCardID,
       passenger.PName,
       passenger.Age,
       TakeTrainRecord.TID,
       train.SDate,
       TakeTrainRecord.CarrigeID,
       TakeTrainRecord.SeatRow,
       TakeTrainRecord.SeatNo
FROM TakeTrainRecord,
     passenger,
     DiagnoseRecord,
     train
WHERE DiagnoseRecord.DStatus = 1
  AND                               -- 确诊
    DiagnoseRecord.PCardID = passenger.PCardID
  AND DiagnoseRecord.PCardID = TakeTrainRecord.PCardID
  AND TakeTrainRecord.TID = train.TID
  AND TakeTrainRecord.SStatus = '1' -- 上车了
ORDER BY PCardID, SDate DESC);
-- 按身份证号升序排序，身份证号一致，则按发车日期降序排序


-- 触发器
DROP TRIGGER IF EXISTS DiagnoseT;
CREATE TRIGGER DiagnoseT
AFTER INSERT ON DiagnoseRecord
FOR EACH ROW
BEGIN
	IF new.DStatus = 1 -- 确诊
	THEN
     INSERT INTO TrainContactor(CDate,CCardID,DStatus,PCardID)
		 SELECT TRO.SDate,TRO.PCardID,2,new.PCardID -- 疑似
		 FROM (SELECT new.PCardID,TakeTrainRecord.TID ,TakeTrainRecord.CarrigeID,TakeTrainRecord.SeatRow,train.SDate   -- 病人14天内乘车记录的临时表
                                 FROM TakeTrainRecord,train   -- 存储病人ID，车次号，车厢号，座位排号，发车日期
                                 WHERE TakeTrainRecord.PCardID = new.PCardID -- 病人的记录
                                 AND train.TID = TakeTrainRecord.TID
                                 AND TakeTrainRecord.SStatus = 1 -- 已乘车
                                 AND new.FDay >= train.SDate  -- 14天之内
                                 AND date_sub(new.FDay, interval 14 day ) < train.SDate) AS TRP,
           (SELECT TakeTrainRecord.PCardID,TakeTrainRecord.TID ,TakeTrainRecord.CarrigeID,TakeTrainRecord.SeatRow,train.SDate   -- 除病人外其他人14天内乘车记录的临时表
                                 FROM TakeTrainRecord,train   -- 存储病人ID，车次号，车厢号，座位排号，发车日期
                                 WHERE TakeTrainRecord.PCardID!=new.PCardID -- 不是病人的记录
                                 AND train.TID = TakeTrainRecord.TID
                                 AND TakeTrainRecord.SStatus = 1 -- 已乘车
                                 AND new.FDay >= train.SDate  -- 14天之内
                                 AND date_sub(new.FDay, interval 14 day ) < train.SDate) AS TRO
		 WHERE (
            (TRO.SeatRow = TRP.SeatRow ) OR -- 同一行
						(TRO.SeatRow - 1 = TRP.SeatRow)OR  -- 前一行
						(TRO.SeatRow + 1 = TRP.SeatRow ))  -- 后一行
             AND TRO.CarrigeID = TRP.CarrigeID   -- 同一车厢
             AND TRO.TID = TRP.TID  -- 同一车次
             AND TRO.SDate = TRP.SDate;	-- 同一天

      UPDATE TrainContactor -- 更新密切接触者中感染者信息
      SET DStatus = 1
	    WHERE TrainContactor.CCardID = new.PCardID
            OR TrainContactor.CCardID IN(SELECT PCardID FROM DiagnoseRecord WHERE DiagnoseRecord.DStatus = 1) ;	-- 注意，为避免重复加入的情况，会检查诊断表是否已确诊

	END IF;
END;

-- 测试
select TR1.PCardID, TR2.PCardID, TR1.SDate
from (select T1.*, train.SDate
      from TakeTrainRecord T1,
           train
      where T1.TID = train.TID
        and T1.SStatus = 1
        and T1.PCardID not in (select PCardID from DiagnoseRecord)) as TR1,
     (select T2.*, train.SDate
      from TakeTrainRecord T2,
           train
      where T2.TID = train.TID
        and T2.SStatus = 1
         and T2.PCardID not in (select CCardID from TrainContactor)) as TR2
where (TR1.SeatRow = TR2.SeatRow or
       TR1.SeatRow - 1 = TR2.SeatRow or
       TR1.SeatRow + 1 = TR2.SeatRow)
  and TR1.CarrigeID = TR2.CarrigeID
  and TR1.TID = TR2.TID
  and TR1.SDate = TR2.SDate
  and TR1.PCardID != TR2.PCardID;
-- T1没有加入Diagnose,T2没有被接触,那么将T1加入Diagnose应该会触发,导致T2被加入接触者表
-- T1 = 150122200501130536
-- T2 = 420325195106010392
-- date = 2020-01-29,那么选择之后的2020-02-01
select * from TrainContactor where CCardID = '420325195106010392';
# insert into DiagnoseRecord (DID, PCardID, DDay, DStatus, FDay) VALUES
# (10001,'150204194606171996', '2019-12-29', 1, '2019-12-29');
delete from DiagnoseRecord where PCardID = '150122200501130536';
delete from TrainContactor where CCardID = '420325195106010392';
insert into DiagnoseRecord (PCardID, DDay, DStatus, FDay) VALUES
('150122200501130536', date('2020-01-24'), 1, date('2020-01-24'));
select * from TrainContactor where CCardID = '420325195106010392';


