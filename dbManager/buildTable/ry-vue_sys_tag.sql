-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: hk-cdb-rgwubxhf.sql.tencentcdb.com    Database: ry-vue
-- ------------------------------------------------------
-- Server version	5.7.18-txsql-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'b41c25e3-83e8-11ef-86be-20906f490521:1-1699';

--
-- Table structure for table `sys_tag`
--

DROP TABLE IF EXISTS `sys_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tag` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `color` varchar(7) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `is_global` tinyint(1) NOT NULL DEFAULT '0',
  `usage_count` int(11) NOT NULL DEFAULT '0',
  `icon` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `idx_name_project` (`name`,`project_id`),
  KEY `idx_project_id` (`project_id`),
  CONSTRAINT `fk_tag_project` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-24 11:01:41
