-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 13, 2020 at 05:04 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foody`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `order_list` ()  NO SQL
SELECT o.order_id ,o.customer_id,m.menu_name,p.payment_type,CONCAT(c.phone_no,' ,',c.state,' ,',c.city,' ,',c.landmark,' ,',c.pincode) AS Address,o.quantity as Qnt FROM orders o INNER JOIN menu m ON m.menu_id=o.menu_id INNER JOIN payment p ON p.order_id=o.order_id INNER JOIN customer c ON c.customer_id=o.customer_id WHERE o.order_status='PAYMENT_CONFIRMED' ORDER BY p.time_stamp ASC$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email_id` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_no` varchar(10) NOT NULL,
  `state` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `landmark` varchar(255) NOT NULL,
  `pincode` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `first_name`, `last_name`, `email_id`, `password`, `phone_no`, `state`, `city`, `landmark`, `pincode`) VALUES
(1, 'mark', 'berg', 'berg11@gmail.com', '12345', '9111111111', 'karnataka', 'mangalore', 'state bank', 574154),
(2, 'winston', 'dsouza', 'winstonds12@gmail.com', '123456', '9764316497', 'karnataka', 'mangalore', 'urva store', 574154),
(3, 'Sheldon', 'Sam', 'sam12@gmail.com', '2345', '9888888856', 'karnataka', 'mangalore', 'urva store', 574154),
(4, 'kishore', 'kumar', 'kumar45@gmail.com', '12345', '9865326598', 'karnataka', 'Bangalore', 'hsr layout', 50004),
(5, 'bob', 'sin', 'bob14@gmail.com', '45698', '9081649731', 'karnataka', 'bangalore', 'bda complex hbr layout', 560102),
(6, 'meril', 'dsouza', 'meril11@gmail.com', '123456', '9632895563', 'karnataka', 'bangalore', 'vijayanagar vijaya bank layout', 560040),
(7, 'max', 'dsouza', 'max12@gmail.com', '123456', '9741628856', 'karnataka', 'mangalore', 'urva store', 574154),
(8, 'maxton', 'mosses', 'mos12@gmail.com', '123', '9741628856', 'karnataka', 'bangalore', 'city centre mall mg road', 574154),
(9, 'Feona', 'Melisa', 'melisa@gmail.com', '123', '9101928856', 'karnataka', 'mangalore', 'near state bank circle', 574154),
(10, 'niro', 'Y', 'niro@gmail.com', '12345', '1223488776', 'tn', 'vlr', 'lolo', 121212),
(11, 'mano', 'kumar', 'mano@gmail.com', '12345', '1234123412', 'tn', 'vlr', 'momo', 123123),
(12, 'admin', 'admin', '12', '12', '', '', '', '', 0),
(13, 'admin1', 'admin', 'niranjan111reddy@gmail.com', '1234', '', '', '', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `menu_id` int(255) NOT NULL,
  `menu_name` varchar(255) NOT NULL,
  `price` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`menu_id`, `menu_name`, `price`) VALUES
(100, 'SOUTH INDIAN', 0),
(101, 'Idly (1-set)', 20),
(102, 'Dosa (1-set)', 35),
(103, 'Rava idly (1-set)\r\n', 30),
(104, 'Masala dosa', 50),
(105, 'Pongal', 35),
(106, 'Poori (1-set)', 30),
(107, 'Parota (1-set)', 25),
(108, 'South Indian Special Meals', 100),
(109, 'Kuzhi paniyaram (5 pcs)', 50),
(110, 'Sambar vada (1-set)', 40),
(111, 'Masala vada (1-set)', 30),
(112, 'Medu vada (1-set)', 20),
(200, 'NORTH INDIAN', 0),
(201, 'Naan (1 pcs)', 15),
(202, 'Garlic naan (1 pcs)', 25),
(203, 'Aloo paratha (1 pcs)', 20),
(204, 'Butter naan (1 pcs)', 25),
(205, 'Matar paneer ', 100),
(206, 'Paneer kulcha', 120),
(207, 'Kadai paneer', 135),
(208, 'Butter chicken', 150),
(209, 'Mutton kulcha', 170),
(210, 'Chole Bhature', 80),
(211, 'Mutton Banjara', 160),
(212, 'Mutton korma\r\n', 150),
(300, 'BIRIYANI', 0),
(301, 'Paneer Biriyani', 120),
(302, 'Hyd Mutton Dhum Biriyani', 230),
(303, 'Hyd Chicken Biriyani', 150),
(304, 'Hyd Fish & Egg Biriyani', 200),
(305, 'Hyd Egg Biriyani', 145),
(306, 'Hyd Veg Dhum Biriyani', 100),
(307, 'Thalapakaty Veg Biriyani', 120),
(308, 'Thalapakaty Mutton Biriyani', 260),
(309, 'Thalapakaty Chicken Biriyani', 170),
(310, 'Mushroom Biriyani', 130),
(311, 'Prawn Biriyani', 180),
(312, 'Thalapakaty Fish Biriyani', 200),
(400, 'CHINESE', 0),
(401, 'Tofu (5 pcs)', 75),
(402, 'Shrimp Fried rice', 180),
(403, 'Chicken Noodle Soup', 120),
(404, 'Kung pao chicken', 200),
(405, 'Chicken Dumpling (4 pcs)', 100),
(406, 'Spring roll (3 pcs)', 60),
(407, 'Chow Mein', 120),
(408, 'Veg Fried rice', 120),
(409, 'schezwan noodles', 130),
(410, 'Chicken Chow mein', 150),
(411, 'Veg Dumplings (4 pcs)', 80),
(412, 'Shrimp Chow mein', 180);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(255) NOT NULL,
  `customer_id` int(255) NOT NULL,
  `menu_id` int(255) NOT NULL,
  `quantity` int(255) NOT NULL DEFAULT 1,
  `order_status` enum('ADDED_TO_CART','CONFIRMED','PAYMENT_CONFIRMED','DELIVERED') DEFAULT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `menu_id`, `quantity`, `order_status`, `time_stamp`) VALUES
(242, 13, 305, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(243, 13, 311, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(244, 13, 304, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(245, 13, 303, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(246, 13, 309, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(247, 13, 312, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(248, 13, 302, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(249, 13, 301, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(250, 13, 306, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(251, 13, 308, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(252, 13, 310, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(253, 13, 307, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:08:18'),
(254, 13, 301, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:14:53'),
(255, 13, 310, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:14:53'),
(256, 13, 306, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:14:53'),
(257, 13, 202, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:14:53'),
(258, 13, 207, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:14:53'),
(259, 13, 207, 1, 'PAYMENT_CONFIRMED', '2020-06-04 05:17:20'),
(260, 13, 303, 2, 'PAYMENT_CONFIRMED', '2020-06-04 07:23:20');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `id` int(255) NOT NULL,
  `order_id` int(255) NOT NULL,
  `payment_type` enum('CASH_ON_DELIVERY','ONLINE_PAYMENT') NOT NULL DEFAULT 'CASH_ON_DELIVERY',
  `payment_status` enum('NOT_CONFIRMED','CONFIRMED') NOT NULL DEFAULT 'NOT_CONFIRMED',
  `time_stamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`id`, `order_id`, `payment_type`, `payment_status`, `time_stamp`) VALUES
(268, 242, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(269, 243, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(270, 244, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(271, 245, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(272, 246, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(273, 247, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(274, 248, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(275, 249, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(276, 250, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(277, 251, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(278, 252, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(279, 253, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:08:18'),
(283, 254, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:14:52'),
(284, 255, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:14:52'),
(285, 256, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:14:52'),
(286, 257, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:14:52'),
(287, 258, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 05:14:52'),
(290, 259, 'CASH_ON_DELIVERY', 'CONFIRMED', '2020-06-04 05:17:19'),
(291, 260, 'ONLINE_PAYMENT', 'CONFIRMED', '2020-06-04 07:23:20');

-- --------------------------------------------------------

--
-- Table structure for table `payment_details`
--

CREATE TABLE `payment_details` (
  `payment_id` int(255) NOT NULL,
  `customer_id` int(255) NOT NULL,
  `card_number` varchar(16) NOT NULL,
  `card_holder_name` varchar(255) NOT NULL,
  `cvv` int(3) NOT NULL,
  `exp_month` int(2) NOT NULL,
  `exp_year` int(4) NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `payment_details`
--

INSERT INTO `payment_details` (`payment_id`, `customer_id`, `card_number`, `card_holder_name`, `cvv`, `exp_month`, `exp_year`, `time_stamp`) VALUES
(15, 13, '9865789678695473', 'YOYUO', 777, 12, 25, '2020-06-04 05:08:18'),
(16, 13, '9898767654543232', 'NONO', 777, 12, 23, '2020-06-04 05:14:51'),
(17, 13, '1234123412341234', 'mano', 144, 6, 20, '2020-06-04 07:23:19');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `email_id` (`email_id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `payment_details`
--
ALTER TABLE `payment_details`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `menu_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=413;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=261;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=292;

--
-- AUTO_INCREMENT for table `payment_details`
--
ALTER TABLE `payment_details`
  MODIFY `payment_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `payment_details`
--
ALTER TABLE `payment_details`
  ADD CONSTRAINT `payment_details_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
