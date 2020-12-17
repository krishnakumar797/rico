-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.20-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for testdb
DROP DATABASE IF EXISTS `testdb`;
CREATE DATABASE IF NOT EXISTS `testdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `testdb`;

-- Dumping structure for table testdb.address
DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `house_name` varchar(150) NOT NULL,
  `street_name` varchar(150) NOT NULL,
  `resident_status` varchar(150) NOT NULL,
  `city_name` varchar(150) NOT NULL,
  `state_name` varchar(150) NOT NULL,
  `country` varchar(50) NOT NULL,
  `pin_code` varchar(50) NOT NULL,
  `person_id` bigint(11) NOT NULL,
  `created_by` varchar(150) NOT NULL,
  `created_date` date NOT NULL,
  `created_date_time` datetime NOT NULL,
  `last_modified_by` varchar(150) NOT NULL,
  `last_modified_date` date NOT NULL,
  `last_modified_date_time` datetime NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdu13rl17o4h24m9gt7b2bdobo` (`person_id`),
  CONSTRAINT `FK_address_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKdu13rl17o4h24m9gt7b2bdobo` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;

-- Dumping data for table testdb.address: ~7 rows (approximately)
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;

-- Dumping structure for table testdb.person
DROP TABLE IF EXISTS `person`;
CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL DEFAULT '0',
  `last_name` varchar(50) NOT NULL DEFAULT '0',
  `age` int(11) NOT NULL DEFAULT '0',
  `email` varchar(100) NOT NULL DEFAULT '0',
  `created_by` varchar(150) NOT NULL DEFAULT '0',
  `created_date` date NOT NULL,
  `created_date_time` datetime NOT NULL,
  `last_modified_by` varchar(150) NOT NULL DEFAULT '0',
  `last_modified_date` date NOT NULL,
  `last_modified_date_time` datetime NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  `tet` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=utf8;

-- Dumping data for table testdb.person: ~4 rows (approximately)
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
/*!40000 ALTER TABLE `person` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
