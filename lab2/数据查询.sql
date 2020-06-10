-- 1. 查询确诊者“张三”的在发病前14天内的乘车记录；此处改为了'陆岳泽'
select TakeTrainRecord.*, passenger.PName, DiagnoseRecord.FDay, train.SDate
from TakeTrainRecord,
     passenger,
     train,
     DiagnoseRecord
where TakeTrainRecord.PCardID = passenger.PCardID
  and passenger.PName = '陆岳泽'
  and TakeTrainRecord.TID = train.TID
  and passenger.PCardID = DiagnoseRecord.PCardID
  and DiagnoseRecord.DStatus = 1
  and -- 发病
    train.SDate <= DiagnoseRecord.FDay
  and -- 在发病之前
    train.SDate >= date_sub(DiagnoseRecord.FDay, interval 14 day);
-- 14天内

select  * from passenger where PCardID = '623026200412094655';

-- 2. 查询所有从城市“武汉”出发的乘客乘列车所到达的城市名
select distinct SName
from station
where SID in
      (select TakeTrainRecord.AStationID
       from TakeTrainRecord,
            station
       where TakeTrainRecord.SStationID = station.SID
         and station.CityName = '武汉');

-- 3. 计算每位新冠患者从发病到确诊的时间间隔（天数）及患者身份信息，并将结果按照发病时间天数的降序排列；
select datediff(DiagnoseRecord.DDay, DiagnoseRecord.FDay) `interval`, passenger.*
from DiagnoseRecord,
     passenger
where DiagnoseRecord.PCardID = passenger.PCardID
  and DiagnoseRecord.DStatus = 1
order by datediff(DiagnoseRecord.DDay, DiagnoseRecord.FDay) desc;

-- 4. 查询“2020-01-22”从“武汉”发出的所有列车；
select train.*
from train,
     station
where train.AStationID = station.SID
  and station.CityName = '武汉'
  and train.SDate = '2020-01-22';

-- 5. 查询“2020-01-22”途经“武汉”的所有列车；
select distinct train.*
from train,
     trainpass
where train.TID = trainpass.TID
  and date(trainpass.ATime) = '2020-01-22'
  and trainpass.SID in (select distinct SID from station where station.CityName = '武汉');

-- 6. 查询“2020-01-22”从武汉离开的所有乘客的身份证号、所到达的城市、到达日期；
select temp.PCardID, station.CityName
from (select TakeTrainRecord.PCardID, TakeTrainRecord.AStationID
      from TakeTrainRecord,
           trainpass,
           station
      where trainpass.TID = TakeTrainRecord.TID
        and date(trainpass.STime) = '2020-01-22'
        and -- 所乘坐列车的出发的时间为2020-01-22
          station.SID = trainpass.SID
        and station.CityName = '武汉') as temp,
     station-- 出发的车站是武汉
-- temp中存储的是所有当天从武汉出发的乘客所到达的车站编号，需要再select一次，将编号转化为城市名
where station.SID = temp.AStationID;

-- 7.统计“2020-01-22” 从武汉离开的所有乘客所到达的城市及达到各个城市的武汉人员数
select station.CityName, count(*)
from (select TakeTrainRecord.AStationID
      from TakeTrainRecord,
           trainpass,
           station
      where trainpass.TID = TakeTrainRecord.TID
        and date(trainpass.STime) = '2020-01-22'
        and -- 所乘坐列车的出发的时间为2020-01-22
          station.SID = trainpass.SID
        and station.CityName = '武汉') as temp,
     station-- 出发的车站是武汉
-- temp中存储的是所有当天从武汉出发的乘客所到达的车站编号，需要再select一次，将编号转化为城市名
where station.SID = temp.AStationID
group by station.CityName;

-- 8. 查询2020年1月到达武汉的所有人员；
select passenger.*
from TakeTrainRecord,
     trainpass,
     station,
     passenger
where TakeTrainRecord.TID = trainpass.TID
  and TakeTrainRecord.SStationID = station.SID
  and station.CityName = '武汉'
  and trainpass.SID = station.SID
  and date(trainpass.ATime) <= '2020-01-31'
  and date(trainpass.ATime) >= '2020-01-01'
  and -- 一月份
    passenger.PCardID = TakeTrainRecord.PCardID;

-- 9. 查询2020年1月乘车途径武汉的外地人员（身份证非“420”开头）；
select distinct passenger.*
from passenger,
     TakeTrainRecord,
     station,
     trainpass
where TakeTrainRecord.PCardID = passenger.PCardID
  and trainpass.TID = TakeTrainRecord.TID
  and trainpass.SID = station.SID
  and station.CityName = '武汉'
  and left(passenger.PCardID, 3) <> '420'
  and trainpass.STime >= '2020-01-01'
  and trainpass.ATime <= '2020-01-31';

-- 10. 统计“2020-01-22”乘坐过‘G007’号列车的新冠患者在火车上的密切接触乘客人数（每位新冠患者的同车厢人员都算同车密切接触）。
-- 不存在G007这辆列车，改为Z4177
select count(*)
from TakeTrainRecord,
     (select distinct TakeTrainRecord.CarrigeID
      from DiagnoseRecord,
           train,
           TakeTrainRecord
      where DiagnoseRecord.PCardID = TakeTrainRecord.PCardID
        and DiagnoseRecord.DStatus = 1
        and TakeTrainRecord.TID = train.TID
        and train.TName = 'Z4177'
        and date(train.STime) <= '2020-01-22'
        and date(train.ATime) >= '2020-01-22'
        and TakeTrainRecord.CarrigeID is not null) as temp,
     train -- temp表中为患者所在的车厢号
where TakeTrainRecord.CarrigeID = temp.CarrigeID
  and train.TName = 'Z4177'
  and train.TID = TakeTrainRecord.TID
  and date(train.STime) <= '2020-01-22'
  and date(train.ATime) >= '2020-01-22';


-- 11. 查询一趟列车的一节车厢中有3人及以上乘客被确认患上新冠的列车名、出发日期，车厢号；
select TName, SDate, CarrigeID
from train,
     TakeTrainRecord,
     DiagnoseRecord
where DiagnoseRecord.DStatus = 1
  and TakeTrainRecord.PCardID = DiagnoseRecord.PCardID
  and TakeTrainRecord.TID = train.TID
  and TakeTrainRecord.SStatus = 1
group by CarrigeID, TName, train.SDate
having count(*) >= 2;
-- 数据量过小

-- 12. 查询没有感染任何周边乘客的新冠乘客的身份证号、姓名、乘车日期；
select distinct passenger.PCardID, passenger.PName, train.SDate
from train,
     TakeTrainRecord,
     DiagnoseRecord,
     TrainContactor,
     passenger
where train.TID = TakeTrainRecord.TID
  and TakeTrainRecord.PCardID = DiagnoseRecord.PCardID
  and # 同一个人
    TakeTrainRecord.SStatus = 1
  and ## 上车了
    passenger.PCardID = TakeTrainRecord.PCardID
  and DiagnoseRecord.DStatus = 1
  and ## 感染
        DiagnoseRecord.PCardID not in
        (select TrainContactor.PCardID from TrainContactor where TrainContactor.DStatus = 1);

-- 13. 查询到达 “北京”、或“上海”，或“广州”（即终点站）的列车名，要求where子句中除了连接条件只能有一个条件表达式；
select distinct TName
from train
where AStationID in (select SID from station where CityName in ('北京', '上海', '广州'));

-- 14. 查询“2020-01-22”从“武汉站”出发，然后当天换乘另一趟车的乘客身份证号和首乘车次号，结果按照首乘车次号降序排列，同车次则按照乘客身份证号升序排列；
select *
from TakeTrainRecord `TR1`,
     TakeTrainRecord `TR2`,
     train
where TR1.SStationID in (select SID from station where station.SName = '武汉站')
  and TR1.TID in (select TID from train where SDate = '2020-01-22')
  and TR1.PCardID = TR2.PCardID
  and # 同一个乘客
    TR1.SStatus = 1
  and TR2.SStatus = 1
  and exists(select *
             from trainpass TP1
             where TR1.TID = TP1.TID
               and TR1.AStationID = TP1.SID
               and TP1.STime <= (select ATime from trainpass TP2 where TP2.TID = TP2.SID))
  and TR1.TID <> TR2.TID
  and TR2.TID in (select TP.TID
                  from trainpass TP
                  where ((date_format(TP.ATime, '%Y-%m-%m') = '2020-01-22')
                      or (date_format(TP.STime, '%Y-%m-%m') = '2020-01-22'))
                    and TR2.TID = TP.TID
                    and TR2.SStationID = TP.SID
)
group by TR1.PCardID, TR1.TID
having count(TR1.PCardID) > 1
order by TR1.TID desc, TR1.PCardID;

-- 15. 查询所有新冠患者的身份证号，姓名及其2020年以来所乘坐过的列车名、发车日期，要求即使该患者未乘坐过任何列车也要列出来；
select PA.PCardID, PA.PName, TM.TName, TM.SDate
from (select D.PCardID, P.PName
      from DiagnoseRecord D,
           passenger P
      where D.PCardID = P.PCardID
        and D.DStatus = 1) as PA
         left outer join (select TR.PCardID, T.TName, T.SDate
                          from TakeTrainRecord TR,
                               train T,
                               trainpass TP1
                          where TR.TID = T.TID
                            and TR.SStationID = TP1.SID
                            and T.TID = TP1.TID
                            and TR.SStatus = 1
                            and date_format(TP1.STime, '%Y-%m') >= '2020-01') as TM on (PA.PCardID = TM.PCardID)
order by PA.PCardID;


-- 16. 查询所有发病日期相同而且确诊日期相同的病患统计信息，包括：发病日期、确诊日期和患者人数，结果按照发病日期降序排列的前提下再按照确诊日期降序排列。

SELECT FDay, DDay, count(*)
FROM DiagnoseRecord
WHERE DStatus = 1
GROUP BY FDay, DDay
ORDER BY FDay DESC, DDay DESC;