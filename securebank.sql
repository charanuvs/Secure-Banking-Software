-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: securebank
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.14.04.1

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
-- Current Database: `securebank`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `securebank` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `securebank`;

--
-- Table structure for table `ACCOUNT`
--

DROP TABLE IF EXISTS `ACCOUNT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACCOUNT` (
  `ACC_NO` varchar(30) NOT NULL DEFAULT '',
  `USER_ID` varchar(20) DEFAULT NULL,
  `BALANCE` varchar(20) DEFAULT NULL,
  `TYPE` varchar(20) DEFAULT NULL,
  `OPEN_DATE` date DEFAULT NULL,
  `CLOSING_DATE` date DEFAULT NULL,
  PRIMARY KEY (`ACC_NO`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `ACCOUNT_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACCOUNT`
--

LOCK TABLES `ACCOUNT` WRITE;
/*!40000 ALTER TABLE `ACCOUNT` DISABLE KEYS */;
/*!40000 ALTER TABLE `ACCOUNT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LOG`
--

DROP TABLE IF EXISTS `LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LOG` (
  `LOG_ID` varchar(20) NOT NULL DEFAULT '',
  `USER_ID` varchar(20) DEFAULT NULL,
  `ACCOUNT_NO` varchar(30) DEFAULT NULL,
  `L_DATE` date DEFAULT NULL,
  `SEVERITY` varchar(20) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`LOG_ID`),
  KEY `ACCOUNT_NO` (`ACCOUNT_NO`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `LOG_ibfk_1` FOREIGN KEY (`ACCOUNT_NO`) REFERENCES `ACCOUNT` (`ACC_NO`),
  CONSTRAINT `LOG_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LOG`
--

LOCK TABLES `LOG` WRITE;
/*!40000 ALTER TABLE `LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSACTION`
--

DROP TABLE IF EXISTS `TRANSACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRANSACTION` (
  `T_ID` varchar(20) NOT NULL DEFAULT '',
  `TO_ACCOUNT` varchar(20) DEFAULT NULL,
  `FROM_ACCOUNT` varchar(20) DEFAULT NULL,
  `AMOUNT` varchar(20) DEFAULT NULL,
  `T_TYPE` varchar(20) DEFAULT NULL,
  `T_STATUS` varchar(20) DEFAULT NULL,
  `T_DATE` date DEFAULT NULL,
  `AUTHORISED_EMP` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`T_ID`),
  KEY `TO_ACCOUNT` (`TO_ACCOUNT`),
  KEY `FROM_ACCOUNT` (`FROM_ACCOUNT`),
  KEY `AUTHORISED_EMP` (`AUTHORISED_EMP`),
  CONSTRAINT `TRANSACTION_ibfk_1` FOREIGN KEY (`TO_ACCOUNT`) REFERENCES `ACCOUNT` (`ACC_NO`),
  CONSTRAINT `TRANSACTION_ibfk_2` FOREIGN KEY (`FROM_ACCOUNT`) REFERENCES `ACCOUNT` (`ACC_NO`),
  CONSTRAINT `TRANSACTION_ibfk_3` FOREIGN KEY (`AUTHORISED_EMP`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSACTION`
--

LOCK TABLES `TRANSACTION` WRITE;
/*!40000 ALTER TABLE `TRANSACTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `TRANSACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER` (
  `USER_ID` varchar(20) NOT NULL DEFAULT '',
  `SSN` varchar(20) DEFAULT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `EMAIl` varchar(20) DEFAULT NULL,
  `PHONE_NO` varchar(12) DEFAULT NULL,
  `PASSWORD` varchar(20) DEFAULT NULL,
  `GENDER` varchar(10) DEFAULT NULL,
  `USER_TYPE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-16 14:07:04