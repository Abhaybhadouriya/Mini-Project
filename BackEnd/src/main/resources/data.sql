-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 21, 2024 at 02:17 AM
-- Server version: 8.0.40-0ubuntu0.24.04.1
-- PHP Version: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables in any order
DROP TABLE IF EXISTS `student_bills`;
DROP TABLE IF EXISTS `student_payment`;
DROP TABLE IF EXISTS `bills`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `bills_seq`;
DROP TABLE IF EXISTS `students_seq`;
DROP TABLE IF EXISTS `student_bills_seq`;
DROP TABLE IF EXISTS `student_payment_seq`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `MiniProject`
--

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
                         `id` bigint NOT NULL,
                         `amount` decimal(38,2) NOT NULL,
                         `bill_date` date NOT NULL,
                         `deadline` date NOT NULL,
                         `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`id`, `amount`, `bill_date`, `deadline`, `description`) VALUES
                                                                                 (1, 5000.00, '2024-01-01', '2024-01-15', 'Spring 2024 Tuition Fee'),
                                                                                 (2, 100.00, '2024-01-01', '2024-01-20', 'Library Fee 2024'),
                                                                                 (3, 300.00, '2024-01-01', '2024-01-25', 'Laboratory Fee'),
                                                                                 (4, 150.00, '2024-01-01', '2024-01-30', 'Student Activity Fee'),
                                                                                 (5, 800.00, '2024-01-01', '2024-01-15', 'Health Insurance Fee');

-- --------------------------------------------------------

--
-- Table structure for table `bills_seq`
--

CREATE TABLE `bills_seq` (
                             `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bills_seq`
--

INSERT INTO `bills_seq` (`next_val`) VALUES
    (1);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
                            `id` bigint NOT NULL,
                            `cgpa` float DEFAULT NULL,
                            `domain` varchar(255) DEFAULT NULL,
                            `email` varchar(255) NOT NULL,
                            `first_name` varchar(255) NOT NULL,
                            `graduation_year` int DEFAULT NULL,
                            `last_name` varchar(255) DEFAULT NULL,
                            `password` varchar(255) NOT NULL,
                            `photograph_path` varchar(255) DEFAULT NULL,
                            `placement_id` int DEFAULT NULL,
                            `roll_number` varchar(255) NOT NULL,
                            `specialisation` varchar(255) DEFAULT NULL,
                            `total_credits` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `cgpa`, `domain`, `email`, `first_name`, `graduation_year`, `last_name`, `password`, `photograph_path`, `placement_id`, `roll_number`, `specialisation`, `total_credits`) VALUES
                                                                                                                                                                                                            (1, 3.8, 'Computer Science', 'john.doe@iiitb.com', 'John', 2024, 'Doe', '$2a$10$N9y1fZNzyLaVazDw6djprO/WhFKWp8tIKzvIRP7NTgYEPvDyp.CQ6', '/photos/john.jpg', 101, 'R2023001', 'AI/ML', 85.5),
                                                                                                                                                                                                            (2, 3.9, 'Computer Science', 'jane.smith@iiitb.com', 'Jane', 2024, 'Smith', '$2a$10$N9y1fZNzyLaVazDw6djprO/WhFKWp8tIKzvIRP7NTgYEPvDyp.CQ6', '/photos/jane.jpg', 102, 'R2023002', 'Cybersecurity', 90),
                                                                                                                                                                                                            (3, 3.7, 'Information Technology', 'alice.j@iiitb.com', 'Alice', 2024, 'Johnson', '$2a$10$N9y1fZNzyLaVazDw6djprO/WhFKWp8tIKzvIRP7NTgYEPvDyp.CQ6', '/photos/alice.jpg', 103, 'R2023003', 'Web Development', 82.5),
                                                                                                                                                                                                            (4, 3.6, 'Computer Science', 'bob.wilson@iiitb.com', 'Bob', 2024, 'Wilson', '$2a$10$N9y1fZNzyLaVazDw6djprO/WhFKWp8tIKzvIRP7NTgYEPvDyp.CQ6', '/photos/bob.jpg', 104, 'R2023004', 'Data Science', 78),
                                                                                                                                                                                                            (5, 3.9, 'Information Technology', 'eva.brown@iiitb.com', 'Eva', 2024, 'Brown', '$2a$10$N9y1fZNzyLaVazDw6djprO/WhFKWp8tIKzvIRP7NTgYEPvDyp.CQ6', '/photos/eva.jpg', 105, 'R2023005', 'Cloud Computing', 88.5);

-- --------------------------------------------------------

--
-- Table structure for table `students_seq`
--

CREATE TABLE `students_seq` (
                                `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `students_seq`
--

INSERT INTO `students_seq` (`next_val`) VALUES
    (1);

-- --------------------------------------------------------

--
-- Table structure for table `student_bills`
--

CREATE TABLE `student_bills` (
                                 `id` bigint NOT NULL,
                                 `bill_id` bigint NOT NULL,
                                 `student_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student_bills`
--

INSERT INTO `student_bills` (`id`, `bill_id`, `student_id`) VALUES
                                                                (1, 1, 1),
                                                                (2, 2, 1),
                                                                (3, 1, 2),
                                                                (4, 1, 3),
                                                                (5, 1, 4),
                                                                (6, 1, 5),
                                                                (7, 3, 1),
                                                                (8, 4, 2),
                                                                (9, 5, 3),
                                                                (10, 2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `student_bills_seq`
--

CREATE TABLE `student_bills_seq` (
                                     `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student_bills_seq`
--

INSERT INTO `student_bills_seq` (`next_val`) VALUES
    (1);

-- --------------------------------------------------------

--
-- Table structure for table `student_payment`
--

CREATE TABLE `student_payment` (
                                   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   `amount` decimal(38,2) NOT NULL,
                                   `description` varchar(255) DEFAULT NULL,
                                   `payment_date` date NOT NULL,
                                   `bill_id` bigint NOT NULL,
                                   `student_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE student_payment AUTO_INCREMENT = 10000;  -- Change 6 to the next correct value

--Q
-- Dumping data for table `student_payment`
--

INSERT INTO `student_payment` (`amount`, `description`, `payment_date`, `bill_id`, `student_id`) VALUES
                                                                                                           ( 100.00, 'Library Fee Payment', '2024-01-10', 2, 1),
                                                                                                           ( 2500.00, 'Partial Tuition Payment', '2024-01-12', 1, 2),
                                                                                                           ( 5000.00, 'Full Tuition Payment', '2024-01-05', 1, 3),
                                                                                                           ( 5000.00, 'Full Tuition Payment', '2024-01-08', 1, 5),
                                                                                                           ( 150.00, 'Activity Fee Payment', '2024-01-15', 4, 2),
                                                                                                           ( 2000.00, 'Partial Tuition Payment', '2024-01-14', 1, 1),
                                                                                                           ( 1000.00, 'Partial Tuition Payment', '2024-01-13', 1, 4),
                                                                                                           ( 800.00, 'Insurance Fee Payment', '2024-01-16', 5, 3),
                                                                                                           ( 300.00, 'Lab Fee Payment', '2024-01-11', 3, 5),
                                                                                                           (100.00, 'Library Fee Payment', '2024-01-09', 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `student_payment_seq`
--

CREATE TABLE `student_payment_seq` (
                                       `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student_payment_seq`
--

INSERT INTO `student_payment_seq` (`next_val`) VALUES
    (1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKe2rndfrsx22acpq2ty1caeuyw` (`email`),
  ADD UNIQUE KEY `UKkmd86jf46110c60b412tjt2bg` (`roll_number`);

--
-- Indexes for table `student_bills`
--
ALTER TABLE `student_bills`
    ADD PRIMARY KEY (`id`),
  ADD KEY `FK1cjki67sbh6bfskg3xdxatg4m` (`bill_id`),
  ADD KEY `FKgpjfkogm41luf5fegmk1p58iq` (`student_id`);

--
-- Indexes for table `student_payment`
--

ALTER TABLE `student_payment`
  ADD KEY `FK89o422fd8lxotfgx7q3ruu58` (`bill_id`),
  ADD KEY `FKt2px4t3p1ovu0ta2w64ptofvp` (`student_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `student_bills`
--
ALTER TABLE `student_bills`
    ADD CONSTRAINT `FK1cjki67sbh6bfskg3xdxatg4m` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`),
  ADD CONSTRAINT `FKgpjfkogm41luf5fegmk1p58iq` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);

--
-- Constraints for table `student_payment`
--
ALTER TABLE `student_payment`
    ADD CONSTRAINT `FK89o422fd8lxotfgx7q3ruu58` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`),
  ADD CONSTRAINT `FKt2px4t3p1ovu0ta2w64ptofvp` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`);
COMMIT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;