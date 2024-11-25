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
-- Table structure for table `sys_defect`
--

DROP TABLE IF EXISTS `sys_defect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_defect` (
  `defect_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `description` text,
  `severity` tinyint(4) NOT NULL,
  `priority` tinyint(4) NOT NULL,
  `status` varchar(20) NOT NULL,
  `reported_by` bigint(20) NOT NULL,
  `assigned_to` bigint(20) DEFAULT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `closed_at` datetime DEFAULT NULL,
  `expected_fix_version` varchar(20) DEFAULT NULL,
  `actual_fix_version` varchar(20) DEFAULT NULL,
  `reproduction_steps` text,
  `environment` varchar(100) DEFAULT NULL,
  `related_requirement_id` bigint(20) DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`defect_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_reported_by` (`reported_by`),
  KEY `idx_assigned_to` (`assigned_to`),
  KEY `idx_related_requirement` (`related_requirement_id`),
  CONSTRAINT `fk_defect_assigned_to` FOREIGN KEY (`assigned_to`) REFERENCES `sys_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_defect_project` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_defect_reported_by` FOREIGN KEY (`reported_by`) REFERENCES `sys_user` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_defect_requirement` FOREIGN KEY (`related_requirement_id`) REFERENCES `sys_requirement` (`requirement_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;
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
