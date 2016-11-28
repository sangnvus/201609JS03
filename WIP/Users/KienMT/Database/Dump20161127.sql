-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: favn
-- ------------------------------------------------------
-- Server version	5.7.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ambulances`
--

DROP TABLE IF EXISTS `ambulances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ambulances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `team` varchar(255) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ambulance_users1_idx` (`user_id`),
  CONSTRAINT `fk_ambulance_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ambulances`
--

LOCK TABLES `ambulances` WRITE;
/*!40000 ALTER TABLE `ambulances` DISABLE KEYS */;
/*!40000 ALTER TABLE `ambulances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ANSWER_QUESTION1_idx` (`question_id`),
  KEY `fk_answers_users1_idx` (`user_id`),
  CONSTRAINT `fk_ANSWER_QUESTION1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_answers_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `callers`
--

DROP TABLE IF EXISTS `callers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `callers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(15) DEFAULT NULL,
  `injury_id` int(11) DEFAULT NULL,
  `symptom` varchar(500) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `dispatcher_user_id` int(11) DEFAULT NULL,
  `ambulance_user_id` int(11) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_CALLER_INJURY1_idx` (`injury_id`),
  KEY `fk_callers_users1_idx` (`dispatcher_user_id`),
  KEY `fk_callers_users2_idx` (`ambulance_user_id`),
  CONSTRAINT `fk_CALLER_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_callers_users1` FOREIGN KEY (`dispatcher_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_callers_users2` FOREIGN KEY (`ambulance_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `callers`
--

LOCK TABLES `callers` WRITE;
/*!40000 ALTER TABLE `callers` DISABLE KEYS */;
/*!40000 ALTER TABLE `callers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `centers`
--

DROP TABLE IF EXISTS `centers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `centers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `center_name` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centers`
--

LOCK TABLES `centers` WRITE;
/*!40000 ALTER TABLE `centers` DISABLE KEYS */;
INSERT INTO `centers` VALUES (1,'115 Hà Nội','04 115 115','115HN@gmail.com','Ba Đình - Hà Nội',NULL,NULL,NULL),(2,'115 Hồ Chí Minh','09 115 115','115HCM@gmail.com','Quận 1 - Tp. HCM',NULL,NULL,NULL),(3,'115 Đà Nẵng','05 115 115','115DN@gmail.com','Ngô Quyền - Đà nẵng',NULL,NULL,NULL),(4,'115 Cần Thơ','06 115 115','115CanTho@gmail.com','Trung Nghĩa - Cần Thơ',NULL,NULL,NULL);
/*!40000 ALTER TABLE `centers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faqs`
--

DROP TABLE IF EXISTS `faqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faqs` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `injurie_id` int(11) NOT NULL,
  `question` varchar(500) DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `fk_faqs_users1_idx` (`user_id`),
  KEY `fk_faqs_injuries1_idx` (`injurie_id`),
  CONSTRAINT `fk_faqs_injuries1` FOREIGN KEY (`injurie_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_faqs_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faqs`
--

LOCK TABLES `faqs` WRITE;
/*!40000 ALTER TABLE `faqs` DISABLE KEYS */;
/*!40000 ALTER TABLE `faqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_facilities`
--

DROP TABLE IF EXISTS `health_facilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `health_facilities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `vincity` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_facilities`
--

LOCK TABLES `health_facilities` WRITE;
/*!40000 ALTER TABLE `health_facilities` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_facilities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `injuries`
--

DROP TABLE IF EXISTS `injuries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `injuries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `injury_name` varchar(255) DEFAULT NULL,
  `symptom` varchar(500) DEFAULT NULL,
  `priority` varchar(15) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `injuries`
--

LOCK TABLES `injuries` WRITE;
/*!40000 ALTER TABLE `injuries` DISABLE KEYS */;
INSERT INTO `injuries` VALUES (1,'Dị ứng / Sốc phản vệ','Dị ứng / Sốc phản vệ','Cao',NULL,NULL,'2016-11-23 08:05:13','2016-11-23 08:05:13'),(2,'Hen suyễn','Hen suyễn','Cao',NULL,NULL,'2016-11-23 08:05:24','2016-11-23 08:05:24'),(3,'Chảy máu','Chảy máu','Cao',NULL,NULL,'2016-11-23 08:05:38','2016-11-23 08:05:38'),(4,'Gãy xương','Gãy xương','Cao',NULL,NULL,'2016-11-23 08:05:49','2016-11-23 08:05:49'),(5,'Bỏng','Bỏng','Cao',NULL,NULL,'2016-11-23 08:05:58','2016-11-23 08:05:58'),(6,'Nghẹn','Nghẹn','Cao',NULL,NULL,'2016-11-23 08:06:04','2016-11-23 08:06:04'),(7,'Choáng / Chấn thương đầu','Choáng / Chấn thương đầu','Cao',NULL,NULL,'2016-11-23 08:06:15','2016-11-23 08:06:15'),(8,'Hạ đường huyết','Hạ đường huyết','Cao',NULL,NULL,'2016-11-23 08:06:24','2016-11-23 08:06:24'),(9,'Hoảng loạn','Hoảng loạn','Cao',NULL,NULL,'2016-11-23 08:06:33','2016-11-23 08:06:33'),(10,'Đau tim','Đau tim','Cao',NULL,NULL,'2016-11-23 08:06:39','2016-11-23 08:06:39'),(11,'Sốc nhiệt','Sốc nhiệt','Cao',NULL,NULL,'2016-11-23 08:06:47','2016-11-23 08:06:47'),(12,'Hạ thân nhiệt','Hạ thân nhiệt','Cao',NULL,NULL,'2016-11-23 08:06:59','2016-11-23 08:06:59'),(13,'Viêm màng não','Viêm màng não','Cao',NULL,NULL,'2016-11-23 08:07:07','2016-11-23 08:07:07'),(14,'Ngộ độc','Ngộ độc','Cao',NULL,NULL,'2016-11-23 08:07:14','2016-11-23 08:07:14'),(15,'Co giật / Động kinh','Co giật / Động kinh','Cao',NULL,NULL,'2016-11-23 08:07:20','2016-11-23 08:07:20'),(16,'Côn trùng đốt/cắn','Côn trùng đốt/cắn','Cao',NULL,NULL,'2016-11-23 08:07:25','2016-11-23 08:07:25'),(17,'Rắn độc cắn','Rắn độc cắn','Cao',NULL,NULL,'2016-11-23 08:07:31','2016-11-23 08:07:31'),(18,'Động vật cắn','Động vật cắn','Cao',NULL,NULL,'2016-11-23 08:07:38','2016-11-23 08:07:38'),(19,'Trật khớp và bong gân','Trật khớp và bong gân','Cao',NULL,NULL,'2016-11-23 08:07:45','2016-11-23 08:07:45'),(20,'Đột quỵ','Đột quỵ','Cao',NULL,NULL,'2016-11-23 08:07:53','2016-11-23 08:07:53'),(21,'Bất tỉnh','Bất tỉnh','Cao',NULL,NULL,'2016-11-23 08:08:12','2016-11-23 08:08:12'),(22,'Bất tỉnh và còn thở','Bất tỉnh và còn thở','Cao',NULL,NULL,'2016-11-23 08:08:18','2016-11-23 08:08:18'),(23,'Bất tỉnh và ngừng thở','Bất tỉnh và ngừng thở','Cao',NULL,NULL,'2016-11-23 08:08:26','2016-11-23 08:08:26');
/*!40000 ALTER TABLE `injuries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructions`
--

DROP TABLE IF EXISTS `instructions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instructions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `injury_id` int(11) NOT NULL,
  `step` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `isMakeCall` tinyint(1) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `audio` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_INSTRUCTION_INJURY1_idx` (`injury_id`),
  CONSTRAINT `fk_INSTRUCTION_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructions`
--

LOCK TABLES `instructions` WRITE;
/*!40000 ALTER TABLE `instructions` DISABLE KEYS */;
INSERT INTO `instructions` VALUES (1,1,1,'Dị ứng / Sốc phản vệ',NULL,NULL,NULL,NULL,'2016-11-23 08:05:13','2016-11-23 08:05:13'),(2,2,1,'Hen suyễn',NULL,NULL,NULL,NULL,'2016-11-23 08:05:24','2016-11-23 08:05:24'),(3,3,1,'Chảy máu',NULL,NULL,NULL,NULL,'2016-11-23 08:05:38','2016-11-23 08:05:38'),(4,4,1,'Gãy xương',NULL,NULL,NULL,NULL,'2016-11-23 08:05:49','2016-11-23 08:05:49'),(5,5,1,'Bỏng',NULL,NULL,NULL,NULL,'2016-11-23 08:05:58','2016-11-23 08:05:58'),(6,6,1,'Nghẹn',NULL,NULL,NULL,NULL,'2016-11-23 08:06:04','2016-11-23 08:06:04'),(7,7,1,'Choáng / Chấn thương đầu',NULL,NULL,NULL,NULL,'2016-11-23 08:06:15','2016-11-23 08:06:15'),(8,8,1,'Hạ đường huyết',NULL,NULL,NULL,NULL,'2016-11-23 08:06:24','2016-11-23 08:06:24'),(9,9,1,'Hoảng loạn',NULL,NULL,NULL,NULL,'2016-11-23 08:06:33','2016-11-23 08:06:33'),(10,10,1,'Đau tim',NULL,NULL,NULL,NULL,'2016-11-23 08:06:39','2016-11-23 08:06:39'),(11,11,1,'Sốc nhiệt',NULL,NULL,NULL,NULL,'2016-11-23 08:06:47','2016-11-23 08:06:47'),(12,12,1,'Hạ thân nhiệt',NULL,NULL,NULL,NULL,'2016-11-23 08:06:59','2016-11-23 08:06:59'),(13,13,1,'Viêm màng não',NULL,NULL,NULL,NULL,'2016-11-23 08:07:07','2016-11-23 08:07:07'),(14,14,1,'Ngộ độc',NULL,NULL,NULL,NULL,'2016-11-23 08:07:14','2016-11-23 08:07:14'),(15,15,1,'Co giật / Động kinh',NULL,NULL,NULL,NULL,'2016-11-23 08:07:20','2016-11-23 08:07:20'),(16,16,1,'Côn trùng đốt/cắn',NULL,NULL,NULL,NULL,'2016-11-23 08:07:25','2016-11-23 08:07:25'),(17,17,1,'Rắn độc cắn',NULL,NULL,NULL,NULL,'2016-11-23 08:07:31','2016-11-23 08:07:31'),(18,18,1,'Động vật cắn',NULL,NULL,NULL,NULL,'2016-11-23 08:07:38','2016-11-23 08:07:38'),(19,19,1,'Trật khớp và bong gân',NULL,NULL,NULL,NULL,'2016-11-23 08:07:45','2016-11-23 08:07:45'),(20,20,1,'Đột quỵ',NULL,NULL,NULL,NULL,'2016-11-23 08:07:53','2016-11-23 08:07:53'),(21,21,1,'Bất tỉnh',NULL,NULL,NULL,NULL,'2016-11-23 08:08:12','2016-11-23 08:08:12'),(22,22,1,'Bất tỉnh và còn thở',NULL,NULL,NULL,NULL,'2016-11-23 08:08:18','2016-11-23 08:08:18'),(23,23,1,'Bất tỉnh và ngừng thở',NULL,NULL,NULL,NULL,'2016-11-23 08:08:26','2016-11-23 08:08:26');
/*!40000 ALTER TABLE `instructions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_instructions`
--

DROP TABLE IF EXISTS `learning_instructions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learning_instructions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `injury_id` int(11) NOT NULL,
  `step` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `explain` varchar(500) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `audio` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_learning_instructions_injuries1_idx` (`injury_id`),
  CONSTRAINT `fk_learning_instructions_injuries1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_instructions`
--

LOCK TABLES `learning_instructions` WRITE;
/*!40000 ALTER TABLE `learning_instructions` DISABLE KEYS */;
INSERT INTO `learning_instructions` VALUES (1,1,1,'Dị ứng / Sốc phản vệ','Dị ứng / Sốc phản vệ',NULL,NULL,NULL,'2016-11-23 08:05:13','2016-11-23 08:05:13'),(2,2,1,'Hen suyễn','Hen suyễn',NULL,NULL,NULL,'2016-11-23 08:05:24','2016-11-23 08:05:24'),(3,3,1,'Chảy máu','Chảy máu',NULL,NULL,NULL,'2016-11-23 08:05:38','2016-11-23 08:05:38'),(4,3,2,'Chảy máu','Chảy máu',NULL,NULL,NULL,'2016-11-23 08:05:38','2016-11-23 08:05:38'),(5,4,1,'Gãy xương','Gãy xương',NULL,NULL,NULL,'2016-11-23 08:05:49','2016-11-23 08:05:49'),(6,5,1,'Bỏng','Bỏng',NULL,NULL,NULL,'2016-11-23 08:05:58','2016-11-23 08:05:58'),(7,6,1,'Nghẹn','Nghẹn',NULL,NULL,NULL,'2016-11-23 08:06:04','2016-11-23 08:06:04'),(8,7,1,'Choáng / Chấn thương đầu','Choáng / Chấn thương đầu',NULL,NULL,NULL,'2016-11-23 08:06:15','2016-11-23 08:06:15'),(9,8,1,'Hạ đường huyết','Hạ đường huyết',NULL,NULL,NULL,'2016-11-23 08:06:24','2016-11-23 08:06:24'),(10,9,1,'Hoảng loạn','Hoảng loạn',NULL,NULL,NULL,'2016-11-23 08:06:33','2016-11-23 08:06:33'),(11,10,1,'Đau tim','Đau tim',NULL,NULL,NULL,'2016-11-23 08:06:39','2016-11-23 08:06:39'),(12,11,1,'Sốc nhiệt','Sốc nhiệt',NULL,NULL,NULL,'2016-11-23 08:06:47','2016-11-23 08:06:47'),(13,12,1,'Hạ thân nhiệt','Hạ thân nhiệt',NULL,NULL,NULL,'2016-11-23 08:06:59','2016-11-23 08:06:59'),(14,13,1,'Viêm màng não','Viêm màng não',NULL,NULL,NULL,'2016-11-23 08:07:07','2016-11-23 08:07:07'),(15,14,1,'Ngộ độc','Ngộ độc',NULL,NULL,NULL,'2016-11-23 08:07:14','2016-11-23 08:07:14'),(16,15,1,'Co giật / Động kinh','Co giật / Động kinh',NULL,NULL,NULL,'2016-11-23 08:07:20','2016-11-23 08:07:20'),(17,16,1,'Côn trùng đốt/cắn','Côn trùng đốt/cắn',NULL,NULL,NULL,'2016-11-23 08:07:25','2016-11-23 08:07:25'),(18,17,1,'Rắn độc cắn','Rắn độc cắn',NULL,NULL,NULL,'2016-11-23 08:07:31','2016-11-23 08:07:31'),(19,18,1,'Động vật cắn','Động vật cắn',NULL,NULL,NULL,'2016-11-23 08:07:38','2016-11-23 08:07:38'),(20,19,1,'Trật khớp và bong gân','Trật khớp và bong gân',NULL,NULL,NULL,'2016-11-23 08:07:45','2016-11-23 08:07:45'),(21,20,1,'Đột quỵ','Đột quỵ',NULL,NULL,NULL,'2016-11-23 08:07:53','2016-11-23 08:07:53'),(22,21,1,'Bất tỉnh','Bất tỉnh',NULL,NULL,NULL,'2016-11-23 08:08:12','2016-11-23 08:08:12'),(23,22,1,'Bất tỉnh và còn thở','Bất tỉnh và còn thở',NULL,NULL,NULL,'2016-11-23 08:08:18','2016-11-23 08:08:18'),(24,23,1,'Bất tỉnh và ngừng thở','Bất tỉnh và ngừng thở',NULL,NULL,NULL,'2016-11-23 08:08:26','2016-11-23 08:08:26');
/*!40000 ALTER TABLE `learning_instructions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_types`
--

DROP TABLE IF EXISTS `notification_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `noti_type_name` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_types`
--

LOCK TABLES `notification_types` WRITE;
/*!40000 ALTER TABLE `notification_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `noti_type_id` int(11) NOT NULL,
  `type_id` int(11) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_notification_type1_idx` (`noti_type_id`),
  CONSTRAINT `fk_notification_notification_type1` FOREIGN KEY (`noti_type_id`) REFERENCES `notification_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `injury_id` int(11) DEFAULT NULL,
  `asker` varchar(45) DEFAULT NULL,
  `asker_email` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `count_answer` int(11) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_QUESTION_INJURY1_idx` (`injury_id`),
  CONSTRAINT `fk_QUESTION_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin',NULL,NULL,NULL),(2,'expert',NULL,NULL,NULL),(3,'dispatcher',NULL,NULL,NULL),(4,'ambulance',NULL,NULL,NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `update_table`
--

DROP TABLE IF EXISTS `update_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `update_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `update_table`
--

LOCK TABLES `update_table` WRITE;
/*!40000 ALTER TABLE `update_table` DISABLE KEYS */;
INSERT INTO `update_table` VALUES (1,'injury',NULL,NULL);
/*!40000 ALTER TABLE `update_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `center_id` int(11) DEFAULT NULL,
  `role_id` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `remember_token` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `fk_USER_ROLE1_idx` (`role_id`),
  KEY `fk_USER_CENTER1_idx` (`center_id`),
  CONSTRAINT `fk_USER_CENTER1` FOREIGN KEY (`center_id`) REFERENCES `centers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_ROLE1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'admin',1,1,'$2y$10$T2ORq/DkUvO1LSqMY1DTHu81DVHUR5knER9xqPMr9Bi3KRD6lgmBG','Mai Trung Kiên','0169 463 9816','kienmt@gmail.com','Hà Nội','fftTiPVtp5tNpssRrVUR2oe75i9yA8xzJcgPd2uFeWN18qFQ5z2nFBLQ9nkX',NULL,'2016-11-27 09:21:51',NULL),(8,'expert',2,2,'$2y$10$fy6YXLI/iYMmq8UpBaxKeulk/KL8Hg37gI.jf/.dEhrQoaAEcxTq6','Nguyễn Thu Hà','0165 599 3368','hantse03246@fpt.edu.vn','Tokyo',NULL,NULL,NULL,NULL),(9,'dispatcher',3,3,'$2y$10$tiZ79omQsRGiVGguZCkQxeJcrWpZtUduKglVijNKWTHBtQoy2lLhW','Đàm Huy Hùng','0906 060906','hunggia@gmail.com','Hưng Yên','G9yGWX7nhul3ThKK4AkxksJz8CZ0B5RdED60av8XC3hmQY9xO3LdjYnY2C0T',NULL,'2016-11-27 09:40:16',NULL),(10,'ambulance',4,4,'$2y$10$z9QeOsIqfeNMMjyOQaSbaOQXMR5.SCHIpVKr77dN1NudVkXoFzRMa','Nguyễn Tuấn Tú','0908 090608 ','tutuan@gmail.com','Singapore',NULL,NULL,NULL,NULL),(11,'1',NULL,1,'$2y$10$BdqeIjuDOtBbkasKl47yYud5uu0J0RVHfs0ZmvCtlG1OpRPWYe6cy','xe 115','0123456789','xe115@gmail.com','ha noi',NULL,NULL,'2016-11-26 00:47:24','2016-11-26 00:47:24');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-27 21:09:06
