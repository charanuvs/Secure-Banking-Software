-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 19, 2015 at 09:37 PM
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
  `STATUS` enum('ACTIVE','LOCKED') NOT NULL DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`USER_ID`, `SSN`, `NAME`, `ADDRESS`, `DOB`, `EMAIL`, `PHONE_NO`, `PASSWORD`, `GENDER`, `USER_TYPE`, `GOV_CERT`, `STATUS`) VALUES
('merch1', '', 'Merchant AB', '1255 East University Drive, Apt 120', '1980-01-01', 'vdoosa@asu.edu', '4803348771', '$2a$10$hOJ62n4CLrK4/PYJ5RM.pOSee.Zg8wTLxNm1WrOAc3Ng4umnqrGAS', 'OTHER', 'ROLE_MERCHANT', NULL, 'ACTIVE'),
('vdoosa', '', 'Vikranth Doosa', '1255 East University Drive, Apt 120', '1990-11-21', 'vdoosa@asu.edu', '4803348775', '$2a$10$NRhNYxOpWFSGH7mu6MyJ9u6vb29yUXSArF8HEJXtd4sphfAdy2O4i', 'MALE', 'ROLE_NORMAL', NULL, 'ACTIVE'),
('vdoosa2', '', 'Vikranth Customer', '1255 East University Drive, Apt 120', '1990-08-02', 'vdoosa@asu.edu', '4803348775', '$2a$10$lEYuz8r2RdCDaQzVdV7Vg.EvqxbjLNtwQClE9oWJ1o1jOhAtBNlEy', 'MALE', 'ROLE_NORMAL', NULL, 'ACTIVE'),
('vdoosam', NULL, 'Vikranth Manager', 'Some address', '1990-04-18', 'vickydoosa@gmail.com', '19874563210', '$2a$10$zSv2QnEyksK7gldd4f7vXOuVzOiQUSBJa1cj88DK.PK1q7pkF.J6m', 'MALE', 'ROLE_MANAGER', NULL, 'ACTIVE');

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
  MODIFY `ACC_NO` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `LOG_ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `T_ID` int(11) NOT NULL AUTO_INCREMENT;
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
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `TRANSACTION_ibfk_1` FOREIGN KEY (`TO_ACCOUNT`) REFERENCES `account` (`ACC_NO`),
  ADD CONSTRAINT `TRANSACTION_ibfk_2` FOREIGN KEY (`FROM_ACCOUNT`) REFERENCES `account` (`ACC_NO`),
  ADD CONSTRAINT `TRANSACTION_ibfk_3` FOREIGN KEY (`AUTHORISED_EMP`) REFERENCES `user` (`USER_ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
