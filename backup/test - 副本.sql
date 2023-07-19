-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: test_user
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administration`
--

DROP TABLE IF EXISTS `administration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administration` (
  `update_allowed` tinyint(1) DEFAULT NULL,
  `delete_allowed` tinyint(1) DEFAULT NULL,
  `user_id` varchar(25) NOT NULL,
  `group_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administration`
--

LOCK TABLES `administration` WRITE;
/*!40000 ALTER TABLE `administration` DISABLE KEYS */;
INSERT INTO `administration` VALUES (0,0,'test01',1),(0,0,'test01',2),(0,0,'test02',1);
/*!40000 ALTER TABLE `administration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `core_meta`
--

DROP TABLE IF EXISTS `core_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `core_meta` (
  `meta_id` int NOT NULL,
  `version` int DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`meta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `core_meta`
--

LOCK TABLES `core_meta` WRITE;
/*!40000 ALTER TABLE `core_meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extra_meta`
--

DROP TABLE IF EXISTS `extra_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extra_meta` (
  `meta_id` int NOT NULL,
  `version` int DEFAULT NULL,
  `group_id` int DEFAULT NULL,
  `data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`meta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extra_meta`
--

LOCK TABLES `extra_meta` WRITE;
/*!40000 ALTER TABLE `extra_meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `extra_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow`
--

DROP TABLE IF EXISTS `flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow` (
  `record_id` int NOT NULL,
  `flow_id` int DEFAULT NULL,
  `version` int DEFAULT NULL,
  `committed` tinyint(1) DEFAULT NULL,
  `commit_message` varchar(100) DEFAULT NULL,
  `last_build` varchar(100) DEFAULT NULL,
  `core_meta_id` int DEFAULT NULL,
  `extra_meta_id` int DEFAULT NULL,
  `graph_data` varchar(1000) DEFAULT NULL,
  `blackboard` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow`
--

LOCK TABLES `flow` WRITE;
/*!40000 ALTER TABLE `flow` DISABLE KEYS */;
INSERT INTO `flow` VALUES (1003,1000,0,1,'consequat','2023-07-18 17:46:18',33322331,-93476200,'tempor','incididunt'),(1004,1000,1,0,'','1970-01-01 00:00:00',33322331,-93476200,'tempor','incididunt'),(1005,1000,2,0,'in','1970-01-01 00:00:00',10,120,'officia sint','enim culpa veniam elit'),(1006,1000,3,0,'in','1970-01-01 00:00:00',10,120,'officia sint','enim culpa veniam elit');
/*!40000 ALTER TABLE `flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_summary`
--

DROP TABLE IF EXISTS `flow_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_summary` (
  `flow_id` int NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `des` varchar(200) DEFAULT NULL,
  `last_build` varchar(100) DEFAULT NULL,
  `last_commit` varchar(100) DEFAULT NULL,
  `last_version` int DEFAULT NULL,
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_summary`
--

LOCK TABLES `flow_summary` WRITE;
/*!40000 ALTER TABLE `flow_summary` DISABLE KEYS */;
INSERT INTO `flow_summary` VALUES (1000,'magna in','ut sit cupidatat aliqua in','1970-01-01 00:00:00','1970-01-01 00:00:00',3);
/*!40000 ALTER TABLE `flow_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instance`
--

DROP TABLE IF EXISTS `instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instance` (
  `uuid` varchar(25) NOT NULL,
  `flow_record_id` int DEFAULT NULL,
  `build_time` varchar(25) DEFAULT NULL,
  `complete` tinyint(1) DEFAULT NULL,
  `has_error` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instance`
--

LOCK TABLES `instance` WRITE;
/*!40000 ALTER TABLE `instance` DISABLE KEYS */;
INSERT INTO `instance` VALUES ('irure',1006,'dolore amet eu nostrud',1,0);
/*!40000 ALTER TABLE `instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `node`
--

DROP TABLE IF EXISTS `node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `node` (
  `instance_id` varchar(20) DEFAULT NULL,
  `node_id` int NOT NULL,
  `start_time` varchar(25) DEFAULT NULL,
  `end_time` varchar(25) DEFAULT NULL,
  `options` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `node`
--

LOCK TABLES `node` WRITE;
/*!40000 ALTER TABLE `node` DISABLE KEYS */;
/*!40000 ALTER TABLE `node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pack_group`
--

DROP TABLE IF EXISTS `pack_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pack_group` (
  `group_id` int NOT NULL,
  `group_name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pack_group`
--

LOCK TABLES `pack_group` WRITE;
/*!40000 ALTER TABLE `pack_group` DISABLE KEYS */;
INSERT INTO `pack_group` VALUES (1,'group01'),(2,'group02'),(3,'group03');
/*!40000 ALTER TABLE `pack_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` varchar(20) NOT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('test01',1),('test02',1),('test03',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-19 13:40:29
