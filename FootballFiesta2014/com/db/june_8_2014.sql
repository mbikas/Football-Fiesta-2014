-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.33-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema fifa
--

CREATE DATABASE IF NOT EXISTS fifa;
USE fifa;

--
-- Definition of table `matches`
--

DROP TABLE IF EXISTS `matches`;
CREATE TABLE `matches` (
  `match_id` int(10) unsigned NOT NULL,
  `team1_id` varchar(5) DEFAULT NULL,
  `team2_id` varchar(5) DEFAULT NULL,
  `team1_score` int(10) unsigned DEFAULT NULL,
  `team2_score` int(10) unsigned DEFAULT NULL,
  `team1_description` text,
  `team2_description` text,
  `team1_name` varchar(45) DEFAULT NULL,
  `team2_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matches`
--

/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` (`match_id`,`team1_id`,`team2_id`,`team1_score`,`team2_score`,`team1_description`,`team2_description`,`team1_name`,`team2_name`) VALUES 
 (1,'BRA','CRO',0,0,'','','Brazil','Croatia'),
 (2,'MEX','CMR',0,0,'','','Mexico','Cameroon'),
 (3,'ESP','NED',0,0,'','','Spain','Netherlands'),
 (4,'CHL','AUS',0,0,'','','Chile','Australia'),
 (5,'COL','GRC',0,0,'','','Colombia','Greece'),
 (6,'CIV','JPN',0,0,'','','Côte d\'Ivoire','Japan'),
 (7,'URY','CRI',0,0,'','','Uruguay','Costa Rica'),
 (8,'GBR','ITA',0,0,'','','England','Italy'),
 (9,'CHE','ECU',0,0,'','','Switzerland','Ecuador'),
 (10,'FRA','HND',0,0,'','','France','honduras'),
 (11,'ARG','BIH',0,0,'','','Argentina','Bosnia and Herzegovina'),
 (12,'IRN','NGA',0,0,'','','Iran','Nigeria'),
 (13,'GER','PRT',0,0,'','','Germany','Portugal'),
 (14,'GHA','USA',0,0,'','','Ghana','USA'),
 (15,'BEL','DZA',0,0,'','','Belgium','Algeria'),
 (16,'RUS','KOR',0,0,'','','Russia','Korea Republic'),
 (17,'BRA','MEX',0,0,'','','Brazil','Mexico'),
 (18,'CMR','CRO',0,0,'','','Cameroon','Croatia'),
 (19,'ESP','CHL',0,0,'','','Spain','Chile'),
 (20,'AUS','NED',0,0,'','','Australia','Netherlands'),
 (21,'COL','CIV',0,0,'','','Colombia','Côte d\'Ivoire'),
 (22,'JPN','GRC',0,0,'','','Japan','Greece'),
 (23,'URY','GBR',0,0,'','','Uruguay','England'),
 (24,'ITA','CRI',0,0,'','','Italy','Costa Rica'),
 (25,'CHE','FRA',0,0,'','','Switzerland','France'),
 (26,'HND','ECU',0,0,'','','honduras','Ecuador'),
 (27,'ARG','IRN',0,0,'','','Argentina','Iran'),
 (28,'NGA','BIH',0,0,'','','Nigeria','Bosnia and Herzegovina'),
 (29,'GER','GHA',0,0,'','','Germany','Ghana'),
 (30,'USA','PRT',0,0,'','','USA','Portugal'),
 (31,'BEL','RUS',0,0,'','','Belgium','Russia'),
 (32,'KOR','DZA',0,0,'','','Korea Republic','Algeria'),
 (33,'CMR','BRA',0,0,'','','Cameroon','Brazil'),
 (34,'CRO','MEX',0,0,'','','Croatia','Mexico'),
 (35,'AUS','ESP',0,0,'','','Australia','Spain'),
 (36,'NED','CHL',0,0,'','','Netherlands','Chile'),
 (37,'JPN','COL',0,0,'','','Japan','Colombia'),
 (38,'GRC','CIV',0,0,'','','Greece','Côte d\'Ivoire'),
 (39,'ITA','URY',0,0,'','','Italy','Uruguay'),
 (40,'CRI','GBR',0,0,'','','Costa Rica','England'),
 (41,'HND','CHE',0,0,'','','honduras','Switzerland'),
 (42,'ECU','FRA',0,0,'','','Ecuador','France'),
 (43,'NGA','ARG',0,0,'','','Nigeria','Argentina'),
 (44,'BIH','IRN',0,0,'','','Bosnia and Herzegovina','Iran'),
 (45,'USA','GER',0,0,'','','USA','Germany'),
 (46,'PRT','GHA',0,0,'','','Portugal','Ghana'),
 (47,'KOR','BEL',0,0,'','','Korea Republic','Belgium'),
 (48,'DZA','RUS',0,0,'','','Algeria','Russia'),
 (49,'1A','2B',0,0,'','','1A','2B'),
 (50,'1C','2D',0,0,'','','1C','2D'),
 (51,'1B','2A',0,0,'','','1B','2A'),
 (52,'1D','2C',0,0,'','','1D','2C'),
 (53,'1E','2F',0,0,'','','1E','2F'),
 (54,'1G','2H',0,0,'','','1G','2H'),
 (55,'1F','2E',0,0,'','','1F','2E'),
 (56,'1H','2G',0,0,'','','1H','2G'),
 (57,'W49','W50',0,0,'','','W49','W50'),
 (58,'W53','W54',0,0,'','','W53','W54'),
 (59,'W51','W52',0,0,'','','W51','W52'),
 (60,'W55','W56',0,0,'','','W55','W56'),
 (61,'W57','W58',0,0,'','','W57','W58'),
 (62,'W59','W60',0,0,'','','W59','W60'),
 (63,'L61','L62',0,0,'','','L61','L62'),
 (64,'W61','W62',0,0,'','','W61','W62');
/*!40000 ALTER TABLE `matches` ENABLE KEYS */;


--
-- Definition of table `point_table`
--

DROP TABLE IF EXISTS `point_table`;
CREATE TABLE `point_table` (
  `group` varchar(2) NOT NULL,
  `team_id` varchar(10) NOT NULL,
  `match` int(10) unsigned DEFAULT NULL,
  `won` int(10) unsigned DEFAULT NULL,
  `drawn` int(10) unsigned DEFAULT NULL,
  `lost` int(10) unsigned DEFAULT NULL,
  `GF` int(10) unsigned DEFAULT NULL,
  `GA` int(10) unsigned DEFAULT NULL,
  `PTS` double DEFAULT NULL,
  `team_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`team_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `point_table`
--

/*!40000 ALTER TABLE `point_table` DISABLE KEYS */;
INSERT INTO `point_table` (`group`,`team_id`,`match`,`won`,`drawn`,`lost`,`GF`,`GA`,`PTS`,`team_name`) VALUES 
 ('F','ARG',0,0,0,0,0,0,0,'Argentina'),
 ('B','AUS',0,0,0,0,0,0,0,'Australia'),
 ('H','BEL',0,0,0,0,0,0,0,'Belgium'),
 ('F','BIH',0,0,0,0,0,0,0,'Bosnia and Herzegovina'),
 ('A','BRA',0,0,0,0,0,0,0,'Brazil'),
 ('E','CHE',0,0,0,0,0,0,0,'Switzerland'),
 ('B','CHL',0,0,0,0,0,0,0,'Chile'),
 ('C','CIV',0,0,0,0,0,0,0,'Côte d\'Ivoire'),
 ('A','CMR',0,0,0,0,0,0,0,'Cameroon'),
 ('C','COL',0,0,0,0,0,0,0,'Colombia'),
 ('D','CRI',0,0,0,0,0,0,0,'Costa Rica'),
 ('A','CRO',0,0,0,0,0,0,0,'Croatia'),
 ('H','DZA',0,0,0,0,0,0,0,'Algeria'),
 ('E','ECU',0,0,0,0,0,0,0,'Ecuador'),
 ('B','ESP',0,0,0,0,0,0,0,'Spain'),
 ('E','FRA',0,0,0,0,0,0,0,'France'),
 ('D','GBR',0,0,0,0,0,0,0,'England'),
 ('G','GER',0,0,0,0,0,0,0,'Germany'),
 ('G','GHA',0,0,0,0,0,0,0,'Ghana'),
 ('C','GRC',0,0,0,0,0,0,0,'Greece'),
 ('E','HND',0,0,0,0,0,0,0,'honduras'),
 ('F','IRN',0,0,0,0,0,0,0,'Iran'),
 ('D','ITA',0,0,0,0,0,0,0,'Italy'),
 ('C','JPN',0,0,0,0,0,0,0,'Japan'),
 ('H','KOR',0,0,0,0,0,0,0,'Korea Republic'),
 ('A','MEX',0,0,0,0,0,0,0,'Mexico'),
 ('B','NED',0,0,0,0,0,0,0,'Netherlands'),
 ('F','NGA',0,0,0,0,0,0,0,'Nigeria'),
 ('G','PRT',0,0,0,0,0,0,0,'Portugal'),
 ('H','RUS',0,0,0,0,0,0,0,'Russia'),
 ('D','URY',0,0,0,0,0,0,0,'Uruguay'),
 ('G','USA',0,0,0,0,0,0,0,'USA');
/*!40000 ALTER TABLE `point_table` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
