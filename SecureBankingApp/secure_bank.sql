-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 29, 2015 at 12:48 PM
-- Server version: 5.6.26
-- PHP Version: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `secure_bank`
--
CREATE DATABASE IF NOT EXISTS `secure_bank` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `secure_bank`;

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `ACC_NO` int(11) NOT NULL,
  `USER_ID` varchar(20) DEFAULT NULL,
  `BALANCE` decimal(10,2) DEFAULT NULL,
  `TYPE` enum('SAVINGS','CHECKIN','MERCHANT') DEFAULT NULL,
  `OPEN_DATE` date DEFAULT NULL,
  `CLOSING_DATE` date DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`ACC_NO`, `USER_ID`, `BALANCE`, `TYPE`, `OPEN_DATE`, `CLOSING_DATE`) VALUES
(1, 'vdoosac', '899998.00', 'SAVINGS', '2015-10-20', NULL),
(5, 'merch1', '0.00', 'MERCHANT', '2015-10-20', NULL),
(7, 'merch1', '78.00', 'MERCHANT', '2015-10-20', NULL),
(8, 'merch1', '1000000.00', 'MERCHANT', '2015-10-20', NULL),
(9, 'vdoosac', '780.00', 'CHECKIN', '2015-10-20', NULL),
(10, 'vdoosa2', '100272.00', 'SAVINGS', '2015-10-28', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `LOG_ID` int(11) NOT NULL,
  `USER_ID` varchar(20) DEFAULT NULL,
  `ACCOUNT_NO` int(11) DEFAULT NULL,
  `L_DATE` date DEFAULT NULL,
  `SEVERITY` enum('MNG','EMP') DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pii`
--

DROP TABLE IF EXISTS `pii`;
CREATE TABLE IF NOT EXISTS `pii` (
  `PII_ID` int(11) NOT NULL,
  `USER_ID` varchar(20) NOT NULL,
  `FROM_USER_ID` varchar(20) NOT NULL,
  `TYPE` enum('REQUEST','AUTHORIZE') NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pii`
--

INSERT INTO `pii` (`PII_ID`, `USER_ID`, `FROM_USER_ID`, `TYPE`) VALUES
(5, 'vdoosam2', 'vdoosam2', 'AUTHORIZE'),
(6, 'vdoosam', 'admin', 'AUTHORIZE');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE IF NOT EXISTS `transaction` (
  `T_ID` int(11) NOT NULL,
  `TO_ACCOUNT` int(11) DEFAULT NULL,
  `FROM_ACCOUNT` int(11) DEFAULT NULL,
  `AMOUNT` decimal(10,2) DEFAULT NULL,
  `T_TYPE` enum('TRANSFER','PAYMENT') DEFAULT NULL,
  `T_STATUS` enum('PENDING','COMPLETE') DEFAULT NULL,
  `T_DATE` date DEFAULT NULL,
  `AUTHORISED_EMP` varchar(20) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`T_ID`, `TO_ACCOUNT`, `FROM_ACCOUNT`, `AMOUNT`, `T_TYPE`, `T_STATUS`, `T_DATE`, `AUTHORISED_EMP`) VALUES
(1, 1, 5, '45.00', 'TRANSFER', 'COMPLETE', '2015-10-01', NULL),
(2, 1, 5, '78.00', 'TRANSFER', 'COMPLETE', '2015-10-15', 'vdoosam'),
(3, 10, 1, '120.00', 'TRANSFER', 'COMPLETE', '2015-10-28', NULL),
(5, 10, 1, '124.00', 'TRANSFER', 'PENDING', '2015-10-29', NULL),
(6, 10, 1, '100002.00', 'TRANSFER', 'COMPLETE', '2015-10-29', 'vdoosam');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `USER_ID` varchar(20) NOT NULL DEFAULT '',
  `SSN` varchar(20) DEFAULT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `PHONE_NO` varchar(12) DEFAULT NULL,
  `PASSWORD` varchar(60) DEFAULT NULL,
  `GENDER` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `USER_TYPE` enum('ROLE_NORMAL','ROLE_MERCHANT','ROLE_EMPLOYEE','ROLE_MANAGER','ROLE_ADMIN') DEFAULT NULL,
  `GOV_CERT` varchar(20) DEFAULT NULL,
  `AUTHORIZED_TRANSACTIONS` enum('YES','NO') NOT NULL DEFAULT 'NO',
  `STATUS` enum('ACTIVE','LOCKED') NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`USER_ID`, `SSN`, `NAME`, `ADDRESS`, `DOB`, `EMAIL`, `PHONE_NO`, `PASSWORD`, `GENDER`, `USER_TYPE`, `GOV_CERT`, `AUTHORIZED_TRANSACTIONS`, `STATUS`) VALUES
('admin', '', 'Vikranth Admin', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$8k6fqQi1GyIB1gJ1jQ1BEuwcSMyV3bl7Ne/dLiPx/0r.Vy.V4b5gy', 'MALE', 'ROLE_ADMIN', NULL, 'NO', 'ACTIVE'),
('customer1', '', 'Customer One', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$WAJDkxd0PmH8MrUraSLOROeBmDjX0Tp8W5rdKMtRCEDttOUiLxRYG', 'MALE', 'ROLE_NORMAL', NULL, 'YES', 'ACTIVE'),
('empl', '111111111', 'Employee', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$va/fxDewh9Cur7brcCQ07ebJSs8AhSaZ3UFhoSYVyrgNYSmOCdKXi', 'MALE', 'ROLE_EMPLOYEE', NULL, 'NO', 'ACTIVE'),
('merch1', '', 'Merchant AB', '1255 East University Drive, Apt 120', '1980-01-01', 'vdoosa@asu.edu', '4803348771', '$2a$12$hR2b7U0msQAQEwB9dutB1ujc4xbUCmnSnaeVqBeqlnrjc4yDqozZi', 'OTHER', 'ROLE_MERCHANT', NULL, 'NO', 'ACTIVE'),
('merch2', '', 'Amazon Services', '1255 East University Drive, Apt 120', '1980-01-01', 'vdoosa@asu.edu', '4803348770', '$2a$12$nEbGmN3Bmhug51tWN3DiXOsiaDS5Z6aokK3IxzO3fHMu20yuQ4ynm', 'OTHER', 'ROLE_MERCHANT', NULL, 'NO', 'ACTIVE'),
('test2', '1111111111', 'Test', '1255 East University Drive, Apt 120', '1990-11-21', 'test@test.com', '4803348770', '$2a$12$3EUG4MWk2Ult2csEVld4kOkQDwARj2z2h2umVJE2tfvVIQkwoxSYK', 'MALE', 'ROLE_NORMAL', NULL, 'NO', 'ACTIVE'),
('vdoosa12', '', 'Vikrant  dsjhd sjhdsj', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$Dbfnmua1xwHHN7e.SWMOT.VC0hQDHSbXZUDVeR2WQgS.kEP6jEple', 'OTHER', 'ROLE_NORMAL', NULL, 'NO', 'ACTIVE'),
('vdoosa2', '111111111', 'Vikranth M', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$tyFtbptkxOrLA1UpTjJ.junoCCghmVAY1IEBmYn5JQKGEI98zwyCi', 'MALE', 'ROLE_NORMAL', NULL, 'NO', 'ACTIVE'),
('vdoosac', '', 'Vikranth Customer', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348771', '$2a$12$A4iyr/HqcUCmx8Gi2lLOtuLD2yMtmXoCjh2ApFuKZM2MisVmj371m', 'MALE', 'ROLE_NORMAL', NULL, 'YES', 'ACTIVE'),
('vdoosam', '', 'Vikranth Manager', 'Some address 3', '1990-04-18', 'vickydoosa@gmail.com', '1987456321', '$2a$12$zLVrxv0g5w6fzvKGDf4sXOepzt/T3XnULocQsT.uGkcVdKc.qLN/W', 'MALE', 'ROLE_MANAGER', NULL, 'NO', 'ACTIVE'),
('vdoosam2', '1111111111', 'Vikranth Manage', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348770', '$2a$12$.eo7EZuQRp/nNodg995hW.aeOd62BNZqquydbu5n5GfAHB6ETk0YS', 'FEMALE', 'ROLE_MANAGER', NULL, 'NO', 'ACTIVE');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`ACC_NO`),
  ADD KEY `USER_ID` (`USER_ID`);

--
-- Indexes for table `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`LOG_ID`),
  ADD KEY `ACCOUNT_NO` (`ACCOUNT_NO`),
  ADD KEY `USER_ID` (`USER_ID`);

--
-- Indexes for table `pii`
--
ALTER TABLE `pii`
  ADD PRIMARY KEY (`PII_ID`),
  ADD UNIQUE KEY `PII_ibfk_3` (`USER_ID`,`FROM_USER_ID`),
  ADD KEY `PII_ibfk_2` (`FROM_USER_ID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`T_ID`),
  ADD KEY `TO_ACCOUNT` (`TO_ACCOUNT`),
  ADD KEY `FROM_ACCOUNT` (`FROM_ACCOUNT`),
  ADD KEY `AUTHORISED_EMP` (`AUTHORISED_EMP`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`USER_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `ACC_NO` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `LOG_ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `pii`
--
ALTER TABLE `pii`
  MODIFY `PII_ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `T_ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `ACCOUNT_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`);

--
-- Constraints for table `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `LOG_ibfk_1` FOREIGN KEY (`ACCOUNT_NO`) REFERENCES `account` (`ACC_NO`),
  ADD CONSTRAINT `LOG_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`);

--
-- Constraints for table `pii`
--
ALTER TABLE `pii`
  ADD CONSTRAINT `PII_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`),
  ADD CONSTRAINT `PII_ibfk_2` FOREIGN KEY (`FROM_USER_ID`) REFERENCES `user` (`USER_ID`);

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `TRANSACTION_ibfk_1` FOREIGN KEY (`TO_ACCOUNT`) REFERENCES `account` (`ACC_NO`),
  ADD CONSTRAINT `TRANSACTION_ibfk_2` FOREIGN KEY (`FROM_ACCOUNT`) REFERENCES `account` (`ACC_NO`),
  ADD CONSTRAINT `TRANSACTION_ibfk_3` FOREIGN KEY (`AUTHORISED_EMP`) REFERENCES `user` (`USER_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
