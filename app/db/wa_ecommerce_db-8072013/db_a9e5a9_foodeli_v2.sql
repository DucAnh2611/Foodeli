-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: mysql5049.site4now.net
-- Generation Time: Sep 20, 2023 at 10:14 PM
-- Server version: 8.0.34
-- PHP Version: 7.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_a9e5a9_foodeli`
--
CREATE DATABASE IF NOT EXISTS `db_a9e5a9_foodeli` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `db_a9e5a9_foodeli`;

-- --------------------------------------------------------

--
-- Table structure for table `cancel_reason`
--

CREATE TABLE IF NOT EXISTS `cancel_reason` (
  `ReasonId` int NOT NULL AUTO_INCREMENT,
  `ReasonName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ReasonId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cancel_reason`
--

INSERT INTO `cancel_reason` (`ReasonId`, `ReasonName`) VALUES
(1, 'Waiting for long time'),
(2, 'Wrong address'),
(3, 'The price is not reasonable'),
(4, 'I want to order something else'),
(5, 'I just want to cancel'),
(6, 'Other reason');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE IF NOT EXISTS `cart` (
  `UserId` int NOT NULL,
  `ProductId` int NOT NULL,
  `quantity` int NOT NULL,
  `CreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `UserId` (`UserId`),
  KEY `ProductId` (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `CategoryID` int NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `CatetoryImage` mediumblob,
  `CategoryParentId` int DEFAULT NULL,
  PRIMARY KEY (`CategoryID`),
  KEY `ParentID` (`CategoryParentId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`CategoryID`, `CategoryName`, `CatetoryImage`, `CategoryParentId`) VALUES
(1, 'Rice dishes', NULL, NULL),
(2, 'Fiber dishes', NULL, NULL),
(3, 'Foreign dishes', NULL, NULL),
(4, 'Snacks', NULL, NULL),
(5, 'Water', NULL, NULL),
(6, 'Seafood', NULL, NULL),
(7, 'Rice with sauce', NULL, 1),
(8, 'Fried rice', NULL, 1),
(9, 'Office meal', NULL, 1),
(10, 'Vegetarian rice', NULL, 1),
(11, 'Hot sticky rice', NULL, 1),
(12, 'Dry food', NULL, 2),
(13, 'With broth', NULL, 2),
(14, 'Thai', NULL, 3),
(15, 'Japan', NULL, 3),
(16, 'China', NULL, 3),
(17, 'Other', NULL, 3),
(18, 'Coffee', NULL, 5),
(19, 'Milk tea', NULL, 5),
(20, 'Tea', NULL, 5),
(21, 'Bottled water', NULL, 5),
(22, 'Unripe food', NULL, 6),
(23, 'Cooked food', NULL, 6);

-- --------------------------------------------------------

--
-- Table structure for table `method_payment_app`
--

CREATE TABLE IF NOT EXISTS `method_payment_app` (
  `MethodId` int NOT NULL AUTO_INCREMENT,
  `MethodType` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `MethodCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MethodModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`MethodId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `method_payment_app`
--

INSERT INTO `method_payment_app` (`MethodId`, `MethodType`, `MethodCreateAt`, `MethodModified`) VALUES
(1, 'Cash', '2023-09-05 14:27:47', '2023-09-05 14:27:47'),
(2, 'Visa', '2023-09-05 14:27:52', '2023-09-05 14:27:52'),
(3, 'FoodeliPay', '2023-09-05 14:28:04', '2023-09-05 14:28:04'),
(4, 'Master Card', '2023-09-05 17:25:44', '2023-09-05 17:25:44'),
(5, 'PayPal', '2023-09-05 17:25:55', '2023-09-05 17:25:55');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `OrderUserID` int NOT NULL,
  `OrderShipName` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `OrderShipAddressId` int NOT NULL,
  `OrderPhone` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `OrderShippingFee` float NOT NULL,
  `OrderTax` float NOT NULL,
  `VoucherId` int DEFAULT NULL,
  `OrderEmail` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `OrderDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `OrderShipped` tinyint(1) NOT NULL DEFAULT '0',
  `OrderStateId` int NOT NULL,
  `CheckoutMethodId` int NOT NULL,
  `OrderTotal` float NOT NULL,
  `Payed` int NOT NULL DEFAULT '0',
  `Confirm` tinyint(1) DEFAULT '0',
  `CreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`OrderID`),
  KEY `orderUserId` (`OrderUserID`),
  KEY `orderUserAddress` (`OrderShipAddressId`),
  KEY `orderState` (`OrderStateId`),
  KEY `CheckoutMethodId` (`CheckoutMethodId`),
  KEY `VoucherId` (`VoucherId`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_cancelled`
--

CREATE TABLE IF NOT EXISTS `order_cancelled` (
  `OrderId` int NOT NULL,
  `CancelReasonId` int NOT NULL,
  `CancelReasonElse` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CancelDetail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  KEY `OrderId` (`OrderId`,`CancelReasonId`),
  KEY `CancelReasonId` (`CancelReasonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE IF NOT EXISTS `order_details` (
  `OrderID` int NOT NULL,
  `ProductID` int NOT NULL,
  `ProductName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `ProductPrice` float NOT NULL,
  `ProductQuantity` float NOT NULL,
  `ProductUnit` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  KEY `OrderId` (`OrderID`),
  KEY `ProductId` (`ProductID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_state`
--

CREATE TABLE IF NOT EXISTS `order_state` (
  `StateId` int NOT NULL AUTO_INCREMENT,
  `StateContent` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`StateId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE IF NOT EXISTS `products` (
  `ProductID` int NOT NULL AUTO_INCREMENT,
  `ShopId` int NOT NULL,
  `ProductName` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `ProductPrice` float NOT NULL,
  `ProductUnit` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `ProductShortDesc` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `ProductLongDesc` longtext COLLATE utf8mb4_general_ci NOT NULL,
  `ProductTimeStart` int NOT NULL,
  `ProductTimeFinish` int NOT NULL,
  `ProductCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ProductLastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ProductStock` float DEFAULT NULL,
  `Deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`ProductID`),
  KEY `ShopID` (`ShopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `product_category`
--

CREATE TABLE IF NOT EXISTS `product_category` (
  `ProductId` int NOT NULL,
  `CategoryId` int NOT NULL,
  KEY `ProductId` (`ProductId`,`CategoryId`),
  KEY `CategoryId` (`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `product_image`
--

CREATE TABLE IF NOT EXISTS `product_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `image` longblob NOT NULL,
  `Deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `product_review`
--

CREATE TABLE IF NOT EXISTS `product_review` (
  `ReviewId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `ProductId` int NOT NULL,
  `ReviewTitle` text COLLATE utf8mb4_general_ci,
  `ReviewDesc` mediumtext COLLATE utf8mb4_general_ci,
  `ReviewCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ReviewRating` int NOT NULL,
  PRIMARY KEY (`ReviewId`),
  KEY `userId` (`UserId`),
  KEY `product_inventory` (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `review_image`
--

CREATE TABLE IF NOT EXISTS `review_image` (
  `ReviewImageId` int NOT NULL AUTO_INCREMENT,
  `ReviewId` int NOT NULL,
  `Image` longblob NOT NULL,
  `ReviewCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ReviewImageId`),
  KEY `ReviewId` (`ReviewId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shop`
--

CREATE TABLE IF NOT EXISTS `shop` (
  `ShopId` int NOT NULL AUTO_INCREMENT,
  `ShopAvatar` longblob,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `desc` longtext COLLATE utf8mb4_general_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ShopId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shop_permission`
--

CREATE TABLE IF NOT EXISTS `shop_permission` (
  `PermissionId` int NOT NULL AUTO_INCREMENT,
  `PermissionType` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `CreateNewProduct` tinyint(1) NOT NULL DEFAULT '0',
  `ChangeProductInfo` tinyint(1) NOT NULL DEFAULT '0',
  `AdjustEmployee` tinyint(1) NOT NULL DEFAULT '0',
  `AdjustShopInfomation` tinyint(1) NOT NULL DEFAULT '0',
  `DeleteShop` int NOT NULL,
  `CreateVoucher` int DEFAULT '0',
  `PermissionCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `PermissionLastModify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`PermissionId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shop_permission`
--

INSERT INTO `shop_permission` (`PermissionId`, `PermissionType`, `CreateNewProduct`, `ChangeProductInfo`, `AdjustEmployee`, `AdjustShopInfomation`, `DeleteShop`, `CreateVoucher`, `PermissionCreateAt`, `PermissionLastModify`) VALUES
(1, 'Owner', 1, 1, 1, 1, 1, 1, '2023-09-07 00:33:48', '2023-09-07 00:33:48'),
(2, 'Employee', 0, 0, 0, 0, 0, 0, '2023-09-07 00:34:13', '2023-09-07 00:34:13'),
(3, 'Employee Manager', 0, 0, 1, 0, 0, 0, '2023-09-07 00:34:41', '2023-09-07 00:34:41'),
(4, 'Product Manager', 1, 1, 0, 0, 0, 0, '2023-09-07 00:35:47', '2023-09-07 00:35:47'),
(5, 'Shop Manager', 1, 1, 0, 1, 0, 1, '2023-09-07 00:36:01', '2023-09-07 00:36:01');

-- --------------------------------------------------------

--
-- Table structure for table `shop_user`
--

CREATE TABLE IF NOT EXISTS `shop_user` (
  `ShopId` int NOT NULL,
  `UserId` int NOT NULL,
  `PermissionId` int NOT NULL,
  `CreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `ShopId` (`ShopId`),
  KEY `UserId` (`UserId`),
  KEY `permissionId` (`PermissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `UserEmail` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `UserPassword` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `UserFullName` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `UserPhone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `UserAvatar` longblob,
  `UserBirthday` date NOT NULL,
  `UserRegistrationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UserLastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`),
  KEY `UserID` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

CREATE TABLE IF NOT EXISTS `user_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `Deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_method`
--

CREATE TABLE IF NOT EXISTS `user_method` (
  `UserMethodId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `MethodId` int NOT NULL,
  `UserMethodNumber` int DEFAULT NULL,
  `UserMethodExpireAt` date DEFAULT NULL,
  `UserMethodDesc` char(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserMethodId`),
  KEY `UserId` (`UserId`,`MethodId`),
  KEY `MethodId` (`MethodId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_voucher`
--

CREATE TABLE IF NOT EXISTS `user_voucher` (
  `UserId` int NOT NULL,
  `VoucherId` int NOT NULL,
  `VoucherRecieveDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `VoucherRemain` int NOT NULL,
  KEY `UserId` (`UserId`),
  KEY `VoucherId` (`VoucherId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `voucher`
--

CREATE TABLE IF NOT EXISTS `voucher` (
  `VoucherId` int NOT NULL AUTO_INCREMENT,
  `VoucherCode` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `VoucherText` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `VoucherTitle` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `VoucherDesc` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
  `VoucherMinPrice` float NOT NULL,
  `VoucherDiscount` float NOT NULL,
  `VoucherMaxDiscount` float NOT NULL,
  `VoucherLimit` int NOT NULL,
  `VoucherCreateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `VoucherLastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `VoucherShopOwner` int DEFAULT NULL,
  `VoucherExpire` int NOT NULL,
  `VoucherTarget` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Deleted` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`VoucherId`),
  KEY `VoucherShopOwner` (`VoucherShopOwner`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`ProductId`) REFERENCES `products` (`ProductID`),
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `ParentID` FOREIGN KEY (`CategoryParentId`) REFERENCES `category` (`CategoryID`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`CheckoutMethodId`) REFERENCES `user_method` (`UserMethodId`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`VoucherId`) REFERENCES `voucher` (`VoucherId`),
  ADD CONSTRAINT `orderState` FOREIGN KEY (`OrderStateId`) REFERENCES `order_state` (`StateId`),
  ADD CONSTRAINT `orderUserAddress` FOREIGN KEY (`OrderShipAddressId`) REFERENCES `user_address` (`id`),
  ADD CONSTRAINT `orderUserId` FOREIGN KEY (`OrderUserID`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `order_cancelled`
--
ALTER TABLE `order_cancelled`
  ADD CONSTRAINT `order_cancelled_ibfk_1` FOREIGN KEY (`CancelReasonId`) REFERENCES `cancel_reason` (`ReasonId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `order_cancelled_ibfk_2` FOREIGN KEY (`OrderId`) REFERENCES `orders` (`OrderID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `OrderId` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  ADD CONSTRAINT `ProductId` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `ShopID` FOREIGN KEY (`ShopId`) REFERENCES `shop` (`ShopId`);

--
-- Constraints for table `product_category`
--
ALTER TABLE `product_category`
  ADD CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`ProductId`) REFERENCES `products` (`ProductID`) ON DELETE CASCADE,
  ADD CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`CategoryId`) REFERENCES `category` (`CategoryID`);

--
-- Constraints for table `product_image`
--
ALTER TABLE `product_image`
  ADD CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`ProductID`) ON DELETE CASCADE;

--
-- Constraints for table `product_review`
--
ALTER TABLE `product_review`
  ADD CONSTRAINT `product_inventory` FOREIGN KEY (`ProductId`) REFERENCES `products` (`ProductID`) ON DELETE CASCADE,
  ADD CONSTRAINT `userId` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `review_image`
--
ALTER TABLE `review_image`
  ADD CONSTRAINT `ReviewId` FOREIGN KEY (`ReviewId`) REFERENCES `product_review` (`ReviewId`);

--
-- Constraints for table `shop_user`
--
ALTER TABLE `shop_user`
  ADD CONSTRAINT `permissionId` FOREIGN KEY (`PermissionId`) REFERENCES `shop_permission` (`PermissionId`),
  ADD CONSTRAINT `shop_user_ibfk_1` FOREIGN KEY (`ShopId`) REFERENCES `shop` (`ShopId`),
  ADD CONSTRAINT `shop_user_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `user_address`
--
ALTER TABLE `user_address`
  ADD CONSTRAINT `user_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`UserID`);

--
-- Constraints for table `user_method`
--
ALTER TABLE `user_method`
  ADD CONSTRAINT `user_method_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserID`),
  ADD CONSTRAINT `user_method_ibfk_2` FOREIGN KEY (`MethodId`) REFERENCES `method_payment_app` (`MethodId`);

--
-- Constraints for table `user_voucher`
--
ALTER TABLE `user_voucher`
  ADD CONSTRAINT `user_voucher_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserID`),
  ADD CONSTRAINT `voucherId` FOREIGN KEY (`VoucherId`) REFERENCES `voucher` (`VoucherId`);

--
-- Constraints for table `voucher`
--
ALTER TABLE `voucher`
  ADD CONSTRAINT `voucher_ibfk_1` FOREIGN KEY (`VoucherShopOwner`) REFERENCES `shop` (`ShopId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
