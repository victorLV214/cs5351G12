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
-- Table structure for table `sys_work_item`
--

DROP TABLE IF EXISTS `sys_work_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_work_item` (
  `work_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL,
  `iteration_id` bigint(20) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `description` text,
  `type` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL,
  `priority` tinyint(4) NOT NULL,
  `assigned_to` bigint(20) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `completed_date` date DEFAULT NULL,
  `estimated_effort` decimal(5,2) DEFAULT NULL,
  `actual_effort` decimal(5,2) DEFAULT NULL,
  `parent_work_item_id` bigint(20) DEFAULT NULL,
  `related_requirement_id` bigint(20) DEFAULT NULL,
  `related_defect_id` bigint(20) DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`work_item_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_iteration_id` (`iteration_id`),
  KEY `idx_assigned_to` (`assigned_to`),
  KEY `idx_parent_work_item` (`parent_work_item_id`),
  KEY `idx_related_requirement` (`related_requirement_id`),
  KEY `idx_related_defect` (`related_defect_id`),
  CONSTRAINT `fk_work_item_assigned_to` FOREIGN KEY (`assigned_to`) REFERENCES `sys_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_defect` FOREIGN KEY (`related_defect_id`) REFERENCES `sys_defect` (`defect_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_iteration` FOREIGN KEY (`iteration_id`) REFERENCES `sys_iteration` (`iteration_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_parent` FOREIGN KEY (`parent_work_item_id`) REFERENCES `sys_work_item` (`work_item_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_project` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_requirement` FOREIGN KEY (`related_requirement_id`) REFERENCES `sys_requirement` (`requirement_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;
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

-- Dump completed on 2024-11-24 11:01:38
