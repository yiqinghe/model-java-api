CREATE TABLE `depth_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(255) DEFAULT NULL,
  `event_time` bigint(20) NOT NULL,
  `update_id` bigint(20) NOT NULL,
  `bid_1_price` varchar(255) DEFAULT NULL,
  `bid_2_price` varchar(255) DEFAULT NULL,
  `bid_3_price` varchar(255) DEFAULT NULL,
  `bid_4_price` varchar(255) DEFAULT NULL,
  `bid_5_price` varchar(255) DEFAULT NULL,
  `bid_1_qty` varchar(255) DEFAULT NULL,
  `bid_2_qty` varchar(255) DEFAULT NULL,
  `bid_3_qty` varchar(255) DEFAULT NULL,
  `bid_4_qty` varchar(255) DEFAULT NULL,
  `bid_5_qty` varchar(255) DEFAULT NULL,
  `ask_1_price` varchar(255) DEFAULT NULL,
  `ask_2_price` varchar(255) DEFAULT NULL,
  `ask_3_price` varchar(255) DEFAULT NULL,
  `ask_4_price` varchar(255) DEFAULT NULL,
  `ask_5_price` varchar(255) DEFAULT NULL,
  `ask_1_qty` varchar(255) DEFAULT NULL,
  `ask_2_qty` varchar(255) DEFAULT NULL,
  `ask_3_qty` varchar(255) DEFAULT NULL,
  `ask_4_qty` varchar(255) DEFAULT NULL,
  `ask_5_qty` varchar(255) DEFAULT NULL,
   `date_created` datetime DEFAULT NULL,
   `date_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `d_created_date_index` (`date_created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `order_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(255) DEFAULT NULL,
  `event_time` bigint(20) NOT NULL,
  `price` varchar(255) DEFAULT NULL,
  `qty` varchar(255) DEFAULT NULL,
   `date_created` datetime DEFAULT NULL,
   `date_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `o_created_date_index` (`date_created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

