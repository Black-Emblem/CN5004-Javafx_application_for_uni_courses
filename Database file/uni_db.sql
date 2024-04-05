-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Jun 05, 2022 at 04:47 PM
-- Server version: 10.6.5-MariaDB
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uni db`
--

-- --------------------------------------------------------

--
-- Table structure for table `assignmentrelations`
--

DROP TABLE IF EXISTS `assignmentrelations`;
CREATE TABLE IF NOT EXISTS `assignmentrelations` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `grade` int(3) DEFAULT NULL,
  `userID` int(11) NOT NULL,
  `assignmentsID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `userID` (`userID`,`assignmentsID`),
  KEY `assignmentrelations_ibfk_1` (`assignmentsID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `assignmentrelations`
--

INSERT INTO `assignmentrelations` (`ID`, `grade`, `userID`, `assignmentsID`) VALUES
(10, 95, 3, 5),
(11, 65, 3, 9),
(12, 76, 3, 6),
(13, 84, 3, 8),
(14, NULL, 3, 2),
(15, NULL, 3, 7),
(16, NULL, 2, 5),
(17, NULL, 2, 9),
(18, NULL, 2, 6),
(19, NULL, 2, 8),
(20, NULL, 2, 2),
(21, NULL, 2, 7);

-- --------------------------------------------------------

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
CREATE TABLE IF NOT EXISTS `assignments` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `grade` int(11) DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  `courseID` int(11) DEFAULT NULL,
  `doDate` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `courseID` (`courseID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `assignments`
--

INSERT INTO `assignments` (`ID`, `name`, `description`, `grade`, `type`, `courseID`, `doDate`) VALUES
(1, 'test', '................................................................................', NULL, 'test', 1, '2023-05-17 12:11:56'),
(2, 'assignment2', 'assignment description', 0, 'test', 2, '2023-05-17 00:00:00'),
(3, 'test', '................................................................................', NULL, 'test', 3, '2023-05-17 12:11:56'),
(4, 'test', '................................................................................', NULL, 'test', 4, '2023-05-17 12:11:56'),
(5, 'assignment5', 'assignment description', 25, 'test', 5, '2021-06-03 00:00:00'),
(6, 'assignment6', 'assignment description', 30, 'test', 6, '2021-06-03 13:17:59'),
(7, 'assignment7', 'assignment description', 0, 'test', 7, '2023-05-17 00:00:00'),
(8, 'assignment8', 'assignment description', 15, 'test', 8, '2022-05-22 18:36:15'),
(9, 'assignment9', 'assignment description', 50, 'test', 9, '2022-05-24 19:13:05');

-- --------------------------------------------------------

--
-- Table structure for table `courserelations`
--

DROP TABLE IF EXISTS `courserelations`;
CREATE TABLE IF NOT EXISTS `courserelations` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `courseID` int(11) NOT NULL,
  `grade` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `userID` (`userID`,`courseID`) USING BTREE,
  KEY `courserelations_ibfk_2` (`courseID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `courserelations`
--

INSERT INTO `courserelations` (`ID`, `userID`, `courseID`, `grade`) VALUES
(10, 3, 5, 80),
(11, 3, 9, 86),
(12, 3, 6, 83),
(13, 3, 8, NULL),
(14, 3, 2, NULL),
(15, 3, 7, NULL),
(16, 2, 5, 80),
(17, 2, 9, NULL),
(18, 2, 6, NULL),
(19, 2, 8, NULL),
(20, 2, 2, NULL),
(21, 2, 7, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
CREATE TABLE IF NOT EXISTS `courses` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `semester` int(2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`ID`, `name`, `description`, `semester`) VALUES
(1, 'ADVANCED PROGRAMMING', 'This course is designed for the intermediate learner who needs basic learning of programming language and basics of programming language (C++). We will start simple programs, where you can learn how to create your own code in C/C++. You can see here complete program with the help of small examples with graphical output available under GNU GCC 4.4.3 compilers and visual studio express 2005 editor.You should know the functions and objects, variables etc., Data types in C/C++, conditionals in c++, loops control flow statements, arrays, while loops, do loop, if statement, switch case statement and loops and condition to get successfull completion of all subjects in two semesters. The project assignment is based upon students choice', 4),
(2, 'DATA COMMUNICATIONS AND NETWORKS', 'The course aim at providing learners with basic skills to implement data networks. Learners will cover topics including protocol layers (OSI Layer 2), packet types (IP/Ethernet), link layer protocols (ARCNET, Ethernet 802.11), wireless LAN technologies & MAC address format, IP subnets, routing techniques & addressing formats for IPv4 addresses. As part of the module, students will be introduced to concepts related to transmission control protocol (TCP). Networking devices associated with these communication protocols include routers, switches and hubs and firewalls and NAT gateways.', 3),
(3, 'DATA STRUCTURES AND ALGORITHMS', 'Data Structures and Algorithms is a course which teaches you the basic concepts of Computer Science for beginners. The course is mainly designed to make the student familiar with the concepts of algorithms and data structures. Data structure helps us to store data in an organized way and so it is very important to understand it. An algorithm is a set of rules that are used to solve an arithmetic problem.', 4),
(4, 'DATABASE SYSTEMS', 'The course is based on the database systems and its applications. The course is based on the database systems and its applications. The applications are basically used in all the business, government, and social life. The course also provides information on the database systems and its applications. The course aims to provide the knowledge to the students and faculty on the database systems and its applications. The course includes the basic theory, design, and implementation of the database systems. The course includes basic theory, design, and implementation of the database systems. The course aims to give the practical knowledge to the students. The course is designed to provide good knowledge to the students to help them in their career.', 5),
(5, 'INTRODUCTION TO COMPUTER SYSTEMS AND NETWORKS', 'Introduction to Computer Systems and Networks will be a two-semester course. The first semester will be taken during the fall semester of the first year in the course. The computer systems and networking courses include a foundation of computer technology and a review of elementary concepts and definitions that are necessary to understand computer systems and networking. A basic idea of how computers work, the computer hardware, and the operating systems and applications that control and use the hardware are covered. In addition, a brief overview of networking is provided for the first semester. The second semester is an in-depth study and examination of network security, protocols, and applications. The examination consists of a final project and a final exam. Computer hardware and operating systems are covered in three lectures and labs. A brief introduction to networking is covered in one lecture and labs.', 1),
(6, 'INTRODUCTION TO SOFTWARE DEVELOPMENT', 'Introduction to software development is a very popular course among many students because it is easy to understand and understand. The course includes the basics of computer science and computer programming, as well as the more sophisticated areas of software design and development. At the same time learning it, you get to experience the practical aspect of software development. You get the opportunity to learn about all the tools and methodologies to develop a complete software. The course provides the foundation for the advanced study of software engineering. The course is well equipped with the necessary resources to introduce you to the concept of object-oriented programming and structured programming. The course will teach you about the architecture and software design and development. It also covers the basics of computer science, programming, and problem solving.', 2),
(7, 'INTRODUCTION TO WEB TECHNOLOGIES', 'The course aims to provide an introduction to the basic concepts of Web Technology and a comprehensive overview of all aspects of Web technologies that students of Computer Science and Engineering will encounter during their studies. Students will be provided an exposure to the underlying technologies and their design and implementation. Students will learn to develop a knowledge base on Web technologies. Students will learn to develop an understanding of the fundamental issues of Web Technology and the various Web-based applications. Course content includes Web technology related aspects of HTML, HTML 5 (XHTML), XML, JavaScript, Web 2.0 and Web development', 3),
(8, 'MATHS FOR COMPUTING', 'This course is designed to enable the student to develop his or her mathematical skills and to provide them with a thorough understanding of the fundamentals of mathematics. The course is intended for students who are to be employed in the field of computing. The students will be provided with a general understanding of mathematics and the various mathematical tools and techniques that are used in the computing field.', 2),
(9, 'WEB AND MOBILE APPLICATION DEVELOPMENT', 'Web and Mobile Application Development course is an introduction to web and mobile application development. This course will give you a good understanding of the technology and tools involved in web and application development and will help you to develop web and Mobile applications for your own use. Students will have to be online for most of the course duration; however, they will be able to complete the course in their own time.', 1);

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
CREATE TABLE IF NOT EXISTS `grade` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `grade` int(3) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `userID` int(11) NOT NULL,
  `courseID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `courseID` (`courseID`,`userID`),
  KEY `userID` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `grade`
--

INSERT INTO `grade` (`ID`, `grade`, `date`, `userID`, `courseID`) VALUES
(14, 83, '2022-05-30 16:56:20', 3, 5),
(15, 80, '2022-05-30 16:56:26', 3, 6),
(16, 12, '2022-05-30 16:56:35', 3, 8),
(17, 75, '2022-05-30 16:57:12', 3, 9);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `role` char(1) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `surname` varchar(40) DEFAULT NULL,
  `pEmail` varchar(40) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `currentSemester` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `role`, `email`, `password`, `name`, `surname`, `pEmail`, `phone`, `birthday`, `gender`, `currentSemester`) VALUES
(1, 'A', 'admin1@qduni.edu.gr', 'admin', '', '', '', '0', '2022-06-09', '', 0),
(2, 'T', 'teacher1@qduni.edu.gr', 'teacher1', 'test', 'test', '---', '5555555555', '2022-05-19', 'Male', 0),
(3, 'S', 'jimmy1@qduni.edu.gr', 'jimmy1', 'Jimmy', 'Jazz', 'Jimmy@jazz.com', '5555555555', '1999-04-15', 'Male', 2);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assignmentrelations`
--
ALTER TABLE `assignmentrelations`
  ADD CONSTRAINT `assignmentrelations_ibfk_1` FOREIGN KEY (`assignmentsID`) REFERENCES `assignments` (`ID`),
  ADD CONSTRAINT `assignmentrelations_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`);

--
-- Constraints for table `assignments`
--
ALTER TABLE `assignments`
  ADD CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`courseID`) REFERENCES `courses` (`ID`);

--
-- Constraints for table `courserelations`
--
ALTER TABLE `courserelations`
  ADD CONSTRAINT `courserelations_ibfk_2` FOREIGN KEY (`courseID`) REFERENCES `courses` (`ID`),
  ADD CONSTRAINT `courserelations_ibfk_3` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`);

--
-- Constraints for table `grade`
--
ALTER TABLE `grade`
  ADD CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`courseID`) REFERENCES `courses` (`ID`),
  ADD CONSTRAINT `grade_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
