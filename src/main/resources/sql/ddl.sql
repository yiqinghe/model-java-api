mysql -h 127.0.0.1 -u root -p
Paic1234

show databases;
use binance;

alter table depth_data add column exchange varchar(30) DEFAULT NULL;

alter table order_price add column exchange varchar(30) DEFAULT NULL;