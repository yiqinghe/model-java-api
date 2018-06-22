insert into depth_data(id,symbol,event_time,update_id,
bid_1_price,bid_2_price,bid_3_price,bid_4_price,bid_5_price,
bid_1_qty,bid_2_qty,bid_3_qty,bid_4_qty,bid_5_qty,
ask_1_price,ask_2_price,ask_3_price,ask_4_price,ask_5_price,
ask_1_qty,ask_2_qty,ask_3_qty,ask_4_qty,ask_5_qty,
date_created,date_updated) select '11','ETHUSDT',1529065646363,127406878,
'491.23000000','491.22000000','491.21000000','486.00000000',NULL,
'5.00000000','1.83904000','0.00000000','17.67415000',NULL,
'491.96000000','491.97000000','492.00000000','492.02000000','492.19000000',
'0.00000000','0.90000000','0.00000000','0.50000000','0.00000000',
'2018-06-15 20:27:27','2018-06-15 20:27:27' from dual;

select id,symbol,event_time,update_id, date_created,bid_1_price,ask_1_price,date_updated from depth_data order by event_time;

select count(*) from (select distinct symbol,event_time,update_id,
                                      bid_1_price,bid_2_price,bid_3_price,bid_4_price,bid_5_price,
                                      bid_1_qty,bid_2_qty,bid_3_qty,bid_4_qty,bid_5_qty,
                                      ask_1_price,ask_2_price,ask_3_price,ask_4_price,ask_5_price,
                                      ask_1_qty,ask_2_qty,ask_3_qty,ask_4_qty,ask_5_qty from depth_data)T;

TRUNCATE TABLE depth_data;
