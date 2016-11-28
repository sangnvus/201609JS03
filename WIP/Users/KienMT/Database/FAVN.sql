-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 27, 2016 at 09:12 AM
-- Server version: 5.7.16-0ubuntu0.16.04.1
-- PHP Version: 7.0.8-0ubuntu0.16.04.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `FAVN`
--

-- --------------------------------------------------------

--
-- Table structure for table `ambulances`
--

CREATE TABLE `ambulances` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `team` varchar(255) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `answers`
--

CREATE TABLE `answers` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `callers`
--

CREATE TABLE `callers` (
  `id` int(11) NOT NULL,
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
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `callers`
--

INSERT INTO `callers` (`id`, `phone`, `injury_id`, `symptom`, `latitude`, `longitude`, `status`, `isDeleted`, `dispatcher_user_id`, `ambulance_user_id`, `updated_at`, `created_at`) VALUES
(2, '123', 1, '123', '123', '123', NULL, NULL, NULL, NULL, '2016-11-24 09:39:00', '2016-11-24 09:39:00'),
(3, '01694639816', 6, NULL, '21.0125115', '105.5254107', NULL, NULL, NULL, NULL, '2016-11-24 16:49:38', '2016-11-24 16:49:38'),
(4, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 18:21:28', '2016-11-24 18:21:28'),
(5, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 18:22:28', '2016-11-24 18:22:28'),
(6, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:39:43', '2016-11-24 20:39:43'),
(7, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:39:43', '2016-11-24 20:39:43'),
(8, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:41:12', '2016-11-24 20:41:12'),
(9, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:52:49', '2016-11-24 20:52:49'),
(10, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:52:57', '2016-11-24 20:52:57'),
(11, '01694639816', 1, NULL, '21.0124604', '105.5259602', NULL, NULL, NULL, NULL, '2016-11-24 20:54:21', '2016-11-24 20:54:21');

-- --------------------------------------------------------

--
-- Table structure for table `centers`
--

CREATE TABLE `centers` (
  `id` int(11) NOT NULL,
  `center_name` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `centers`
--

INSERT INTO `centers` (`id`, `center_name`, `phone`, `email`, `address`, `isDeleted`, `updated_at`, `created_at`) VALUES
(1, '115 Hà Nội', '04 115 115', '115HN@gmail.com', 'Ba Đình - Hà Nội', NULL, NULL, NULL),
(2, '115 Hồ Chí Minh', '09 115 115', '115HCM@gmail.com', 'Quận 1 - Tp. HCM', NULL, NULL, NULL),
(3, '115 Đà Nẵng', '05 115 115', '115DN@gmail.com', 'Ngô Quyền - Đà nẵng', NULL, NULL, NULL),
(4, '115 Cần Thơ', '06 115 115', '115CanTho@gmail.com', 'Trung Nghĩa - Cần Thơ', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `faqs`
--

CREATE TABLE `faqs` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `injurie_id` int(11) NOT NULL,
  `question` varchar(500) DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `health_facilities`
--

CREATE TABLE `health_facilities` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `vincity` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `injuries`
--

CREATE TABLE `injuries` (
  `id` int(11) NOT NULL,
  `injury_name` varchar(255) DEFAULT NULL,
  `symptom` varchar(500) DEFAULT NULL,
  `priority` varchar(15) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `injuries`
--

INSERT INTO `injuries` (`id`, `injury_name`, `symptom`, `priority`, `image`, `isDeleted`, `updated_at`, `created_at`) VALUES
(1, 'Dị ứng / Sốc phản vệ', 'Dị ứng / Sốc phản vệ', 'Cao', NULL, NULL, '2016-11-23 08:05:13', '2016-11-23 08:05:13'),
(2, 'Hen suyễn', 'Hen suyễn', 'Cao', NULL, NULL, '2016-11-23 08:05:24', '2016-11-23 08:05:24'),
(3, 'Chảy máu', 'Chảy máu', 'Cao', NULL, NULL, '2016-11-23 08:05:38', '2016-11-23 08:05:38'),
(4, 'Gãy xương', 'Gãy xương', 'Cao', NULL, NULL, '2016-11-23 08:05:49', '2016-11-23 08:05:49'),
(5, 'Bỏng', 'Bỏng', 'Cao', NULL, NULL, '2016-11-23 08:05:58', '2016-11-23 08:05:58'),
(6, 'Nghẹn', 'Nghẹn', 'Cao', NULL, NULL, '2016-11-23 08:06:04', '2016-11-23 08:06:04'),
(7, 'Choáng / Chấn thương đầu', 'Choáng / Chấn thương đầu', 'Cao', NULL, NULL, '2016-11-23 08:06:15', '2016-11-23 08:06:15'),
(8, 'Hạ đường huyết', 'Hạ đường huyết', 'Cao', NULL, NULL, '2016-11-23 08:06:24', '2016-11-23 08:06:24'),
(9, 'Hoảng loạn', 'Hoảng loạn', 'Cao', NULL, NULL, '2016-11-23 08:06:33', '2016-11-23 08:06:33'),
(10, 'Đau tim', 'Đau tim', 'Cao', NULL, NULL, '2016-11-23 08:06:39', '2016-11-23 08:06:39'),
(11, 'Sốc nhiệt', 'Sốc nhiệt', 'Cao', NULL, NULL, '2016-11-23 08:06:47', '2016-11-23 08:06:47'),
(12, 'Hạ thân nhiệt', 'Hạ thân nhiệt', 'Cao', NULL, NULL, '2016-11-23 08:06:59', '2016-11-23 08:06:59'),
(13, 'Viêm màng não', 'Viêm màng não', 'Cao', NULL, NULL, '2016-11-23 08:07:07', '2016-11-23 08:07:07'),
(14, 'Ngộ độc', 'Ngộ độc', 'Cao', NULL, NULL, '2016-11-23 08:07:14', '2016-11-23 08:07:14'),
(15, 'Co giật / Động kinh', 'Co giật / Động kinh', 'Cao', NULL, NULL, '2016-11-23 08:07:20', '2016-11-23 08:07:20'),
(16, 'Côn trùng đốt/cắn', 'Côn trùng đốt/cắn', 'Cao', NULL, NULL, '2016-11-23 08:07:25', '2016-11-23 08:07:25'),
(17, 'Rắn độc cắn', 'Rắn độc cắn', 'Cao', NULL, NULL, '2016-11-23 08:07:31', '2016-11-23 08:07:31'),
(18, 'Động vật cắn', 'Động vật cắn', 'Cao', NULL, NULL, '2016-11-23 08:07:38', '2016-11-23 08:07:38'),
(19, 'Trật khớp và bong gân', 'Trật khớp và bong gân', 'Cao', NULL, NULL, '2016-11-23 08:07:45', '2016-11-23 08:07:45'),
(20, 'Đột quỵ', 'Đột quỵ', 'Cao', NULL, NULL, '2016-11-23 08:07:53', '2016-11-23 08:07:53'),
(21, 'Bất tỉnh', 'Bất tỉnh', 'Cao', NULL, NULL, '2016-11-23 08:08:12', '2016-11-23 08:08:12'),
(22, 'Bất tỉnh và còn thở', 'Bất tỉnh và còn thở', 'Cao', NULL, NULL, '2016-11-23 08:08:18', '2016-11-23 08:08:18'),
(23, 'Bất tỉnh và ngừng thở', 'Bất tỉnh và ngừng thở', 'Cao', NULL, NULL, '2016-11-23 08:08:26', '2016-11-23 08:08:26');

-- --------------------------------------------------------

--
-- Table structure for table `instructions`
--

CREATE TABLE `instructions` (
  `id` int(11) NOT NULL,
  `injury_id` int(11) NOT NULL,
  `step` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `isMakeCall` tinyint(1) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `audio` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `instructions`
--

INSERT INTO `instructions` (`id`, `injury_id`, `step`, `content`, `isMakeCall`, `image`, `audio`, `isDeleted`, `updated_at`, `created_at`) VALUES
(1, 1, 1, 'Dị ứng / Sốc phản vệ', NULL, NULL, NULL, NULL, '2016-11-23 08:05:13', '2016-11-23 08:05:13'),
(2, 2, 1, 'Hen suyễn', NULL, NULL, NULL, NULL, '2016-11-23 08:05:24', '2016-11-23 08:05:24'),
(3, 3, 1, 'Chảy máu', NULL, NULL, NULL, NULL, '2016-11-23 08:05:38', '2016-11-23 08:05:38'),
(4, 4, 1, 'Gãy xương', NULL, NULL, NULL, NULL, '2016-11-23 08:05:49', '2016-11-23 08:05:49'),
(5, 5, 1, 'Bỏng', NULL, NULL, NULL, NULL, '2016-11-23 08:05:58', '2016-11-23 08:05:58'),
(6, 6, 1, 'Nghẹn', NULL, NULL, NULL, NULL, '2016-11-23 08:06:04', '2016-11-23 08:06:04'),
(7, 7, 1, 'Choáng / Chấn thương đầu', NULL, NULL, NULL, NULL, '2016-11-23 08:06:15', '2016-11-23 08:06:15'),
(8, 8, 1, 'Hạ đường huyết', NULL, NULL, NULL, NULL, '2016-11-23 08:06:24', '2016-11-23 08:06:24'),
(9, 9, 1, 'Hoảng loạn', NULL, NULL, NULL, NULL, '2016-11-23 08:06:33', '2016-11-23 08:06:33'),
(10, 10, 1, 'Đau tim', NULL, NULL, NULL, NULL, '2016-11-23 08:06:39', '2016-11-23 08:06:39'),
(11, 11, 1, 'Sốc nhiệt', NULL, NULL, NULL, NULL, '2016-11-23 08:06:47', '2016-11-23 08:06:47'),
(12, 12, 1, 'Hạ thân nhiệt', NULL, NULL, NULL, NULL, '2016-11-23 08:06:59', '2016-11-23 08:06:59'),
(13, 13, 1, 'Viêm màng não', NULL, NULL, NULL, NULL, '2016-11-23 08:07:07', '2016-11-23 08:07:07'),
(14, 14, 1, 'Ngộ độc', NULL, NULL, NULL, NULL, '2016-11-23 08:07:14', '2016-11-23 08:07:14'),
(15, 15, 1, 'Co giật / Động kinh', NULL, NULL, NULL, NULL, '2016-11-23 08:07:20', '2016-11-23 08:07:20'),
(16, 16, 1, 'Côn trùng đốt/cắn', NULL, NULL, NULL, NULL, '2016-11-23 08:07:25', '2016-11-23 08:07:25'),
(17, 17, 1, 'Rắn độc cắn', NULL, NULL, NULL, NULL, '2016-11-23 08:07:31', '2016-11-23 08:07:31'),
(18, 18, 1, 'Động vật cắn', NULL, NULL, NULL, NULL, '2016-11-23 08:07:38', '2016-11-23 08:07:38'),
(19, 19, 1, 'Trật khớp và bong gân', NULL, NULL, NULL, NULL, '2016-11-23 08:07:45', '2016-11-23 08:07:45'),
(20, 20, 1, 'Đột quỵ', NULL, NULL, NULL, NULL, '2016-11-23 08:07:53', '2016-11-23 08:07:53'),
(21, 21, 1, 'Bất tỉnh', NULL, NULL, NULL, NULL, '2016-11-23 08:08:12', '2016-11-23 08:08:12'),
(22, 22, 1, 'Bất tỉnh và còn thở', NULL, NULL, NULL, NULL, '2016-11-23 08:08:18', '2016-11-23 08:08:18'),
(23, 23, 1, 'Bất tỉnh và ngừng thở', NULL, NULL, NULL, NULL, '2016-11-23 08:08:26', '2016-11-23 08:08:26');

-- --------------------------------------------------------

--
-- Table structure for table `learning_instructions`
--

CREATE TABLE `learning_instructions` (
  `id` int(11) NOT NULL,
  `injury_id` int(11) NOT NULL,
  `step` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `explain` varchar(500) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `audio` varchar(100) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `learning_instructions`
--

INSERT INTO `learning_instructions` (`id`, `injury_id`, `step`, `content`, `explain`, `image`, `audio`, `isDeleted`, `updated_at`, `created_at`) VALUES
(1, 1, 1, 'Dị ứng / Sốc phản vệ', 'Dị ứng / Sốc phản vệ', NULL, NULL, NULL, '2016-11-23 08:05:13', '2016-11-23 08:05:13'),
(2, 2, 1, 'Hen suyễn', 'Hen suyễn', NULL, NULL, NULL, '2016-11-23 08:05:24', '2016-11-23 08:05:24'),
(3, 3, 1, 'Chảy máu', 'Chảy máu', NULL, NULL, NULL, '2016-11-23 08:05:38', '2016-11-23 08:05:38'),
(4, 3, 2, 'Chảy máu', 'Chảy máu', NULL, NULL, NULL, '2016-11-23 08:05:38', '2016-11-23 08:05:38'),
(5, 4, 1, 'Gãy xương', 'Gãy xương', NULL, NULL, NULL, '2016-11-23 08:05:49', '2016-11-23 08:05:49'),
(6, 5, 1, 'Bỏng', 'Bỏng', NULL, NULL, NULL, '2016-11-23 08:05:58', '2016-11-23 08:05:58'),
(7, 6, 1, 'Nghẹn', 'Nghẹn', NULL, NULL, NULL, '2016-11-23 08:06:04', '2016-11-23 08:06:04'),
(8, 7, 1, 'Choáng / Chấn thương đầu', 'Choáng / Chấn thương đầu', NULL, NULL, NULL, '2016-11-23 08:06:15', '2016-11-23 08:06:15'),
(9, 8, 1, 'Hạ đường huyết', 'Hạ đường huyết', NULL, NULL, NULL, '2016-11-23 08:06:24', '2016-11-23 08:06:24'),
(10, 9, 1, 'Hoảng loạn', 'Hoảng loạn', NULL, NULL, NULL, '2016-11-23 08:06:33', '2016-11-23 08:06:33'),
(11, 10, 1, 'Đau tim', 'Đau tim', NULL, NULL, NULL, '2016-11-23 08:06:39', '2016-11-23 08:06:39'),
(12, 11, 1, 'Sốc nhiệt', 'Sốc nhiệt', NULL, NULL, NULL, '2016-11-23 08:06:47', '2016-11-23 08:06:47'),
(13, 12, 1, 'Hạ thân nhiệt', 'Hạ thân nhiệt', NULL, NULL, NULL, '2016-11-23 08:06:59', '2016-11-23 08:06:59'),
(14, 13, 1, 'Viêm màng não', 'Viêm màng não', NULL, NULL, NULL, '2016-11-23 08:07:07', '2016-11-23 08:07:07'),
(15, 14, 1, 'Ngộ độc', 'Ngộ độc', NULL, NULL, NULL, '2016-11-23 08:07:14', '2016-11-23 08:07:14'),
(16, 15, 1, 'Co giật / Động kinh', 'Co giật / Động kinh', NULL, NULL, NULL, '2016-11-23 08:07:20', '2016-11-23 08:07:20'),
(17, 16, 1, 'Côn trùng đốt/cắn', 'Côn trùng đốt/cắn', NULL, NULL, NULL, '2016-11-23 08:07:25', '2016-11-23 08:07:25'),
(18, 17, 1, 'Rắn độc cắn', 'Rắn độc cắn', NULL, NULL, NULL, '2016-11-23 08:07:31', '2016-11-23 08:07:31'),
(19, 18, 1, 'Động vật cắn', 'Động vật cắn', NULL, NULL, NULL, '2016-11-23 08:07:38', '2016-11-23 08:07:38'),
(20, 19, 1, 'Trật khớp và bong gân', 'Trật khớp và bong gân', NULL, NULL, NULL, '2016-11-23 08:07:45', '2016-11-23 08:07:45'),
(21, 20, 1, 'Đột quỵ', 'Đột quỵ', NULL, NULL, NULL, '2016-11-23 08:07:53', '2016-11-23 08:07:53'),
(22, 21, 1, 'Bất tỉnh', 'Bất tỉnh', NULL, NULL, NULL, '2016-11-23 08:08:12', '2016-11-23 08:08:12'),
(23, 22, 1, 'Bất tỉnh và còn thở', 'Bất tỉnh và còn thở', NULL, NULL, NULL, '2016-11-23 08:08:18', '2016-11-23 08:08:18'),
(24, 23, 1, 'Bất tỉnh và ngừng thở', 'Bất tỉnh và ngừng thở', NULL, NULL, NULL, '2016-11-23 08:08:26', '2016-11-23 08:08:26');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `noti_type_id` int(11) NOT NULL,
  `type_id` int(11) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `notification_types`
--

CREATE TABLE `notification_types` (
  `id` int(11) NOT NULL,
  `noti_type_name` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `id` int(11) NOT NULL,
  `injury_id` int(11) DEFAULT NULL,
  `asker` varchar(45) DEFAULT NULL,
  `asker_email` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `count_answer` int(11) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `role_name`, `isDeleted`, `updated_at`, `created_at`) VALUES
(1, 'admin', NULL, NULL, NULL),
(2, 'expert', NULL, NULL, NULL),
(3, 'dispatcher', NULL, NULL, NULL),
(4, 'ambulance', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
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
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `center_id`, `role_id`, `password`, `name`, `phone`, `email`, `address`, `remember_token`, `isDeleted`, `updated_at`, `created_at`) VALUES
(7, 'admin', 1, 1, '$2y$10$T2ORq/DkUvO1LSqMY1DTHu81DVHUR5knER9xqPMr9Bi3KRD6lgmBG', 'Mai Trung Kiên', '0169 463 9816', 'kienmt@gmail.com', 'Hà Nội', 'SKKtJXgKQXYXPhn2LsHbmrPO5hSrTDNuIrd5M54h4YCtAtaEzZydb81jLYo3', NULL, '2016-11-24 22:29:12', NULL),
(8, 'expert', 2, 2, '$2y$10$fy6YXLI/iYMmq8UpBaxKeulk/KL8Hg37gI.jf/.dEhrQoaAEcxTq6', 'Nguyễn Thu Hà', '0165 599 3368', 'hantse03246@fpt.edu.vn', 'Tokyo', NULL, NULL, NULL, NULL),
(9, 'dispatcher', 3, 3, '$2y$10$tiZ79omQsRGiVGguZCkQxeJcrWpZtUduKglVijNKWTHBtQoy2lLhW', 'Đàm Huy Hùng', '0906 060906', 'hunggia@gmail.com', 'Hưng Yên', 'Ur41VDHSksJvUPbrbfbFxCYDNCcPK0Wc0oKpLvRFDt04Ct7WiZEdBThLoNTX', NULL, '2016-11-23 03:51:55', NULL),
(10, 'ambulance', 4, 4, '$2y$10$z9QeOsIqfeNMMjyOQaSbaOQXMR5.SCHIpVKr77dN1NudVkXoFzRMa', 'Nguyễn Tuấn Tú', '0908 090608 ', 'tutuan@gmail.com', 'Singapore', NULL, NULL, NULL, NULL),
(11, '1', NULL, 1, '$2y$10$BdqeIjuDOtBbkasKl47yYud5uu0J0RVHfs0ZmvCtlG1OpRPWYe6cy', 'xe 115', '0123456789', 'xe115@gmail.com', 'ha noi', NULL, NULL, '2016-11-26 00:47:24', '2016-11-26 00:47:24');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ambulances`
--
ALTER TABLE `ambulances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ambulance_users1_idx` (`user_id`);

--
-- Indexes for table `answers`
--
ALTER TABLE `answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ANSWER_QUESTION1_idx` (`question_id`),
  ADD KEY `fk_answers_users1_idx` (`user_id`);

--
-- Indexes for table `callers`
--
ALTER TABLE `callers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_CALLER_INJURY1_idx` (`injury_id`),
  ADD KEY `fk_callers_users1_idx` (`dispatcher_user_id`),
  ADD KEY `fk_callers_users2_idx` (`ambulance_user_id`);

--
-- Indexes for table `centers`
--
ALTER TABLE `centers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `faqs`
--
ALTER TABLE `faqs`
  ADD PRIMARY KEY (`id`,`user_id`),
  ADD KEY `fk_faqs_users1_idx` (`user_id`),
  ADD KEY `fk_faqs_injuries1_idx` (`injurie_id`);

--
-- Indexes for table `health_facilities`
--
ALTER TABLE `health_facilities`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `injuries`
--
ALTER TABLE `injuries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `instructions`
--
ALTER TABLE `instructions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_INSTRUCTION_INJURY1_idx` (`injury_id`);

--
-- Indexes for table `learning_instructions`
--
ALTER TABLE `learning_instructions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_learning_instructions_injuries1_idx` (`injury_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_notification_notification_type1_idx` (`noti_type_id`);

--
-- Indexes for table `notification_types`
--
ALTER TABLE `notification_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_QUESTION_INJURY1_idx` (`injury_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username_UNIQUE` (`username`),
  ADD KEY `fk_USER_ROLE1_idx` (`role_id`),
  ADD KEY `fk_USER_CENTER1_idx` (`center_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ambulances`
--
ALTER TABLE `ambulances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `answers`
--
ALTER TABLE `answers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `callers`
--
ALTER TABLE `callers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `centers`
--
ALTER TABLE `centers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `health_facilities`
--
ALTER TABLE `health_facilities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `injuries`
--
ALTER TABLE `injuries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `instructions`
--
ALTER TABLE `instructions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- AUTO_INCREMENT for table `learning_instructions`
--
ALTER TABLE `learning_instructions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `notification_types`
--
ALTER TABLE `notification_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `ambulances`
--
ALTER TABLE `ambulances`
  ADD CONSTRAINT `fk_ambulance_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `fk_ANSWER_QUESTION1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_answers_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `callers`
--
ALTER TABLE `callers`
  ADD CONSTRAINT `fk_CALLER_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_callers_users1` FOREIGN KEY (`dispatcher_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_callers_users2` FOREIGN KEY (`ambulance_user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `faqs`
--
ALTER TABLE `faqs`
  ADD CONSTRAINT `fk_faqs_injuries1` FOREIGN KEY (`injurie_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_faqs_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `instructions`
--
ALTER TABLE `instructions`
  ADD CONSTRAINT `fk_INSTRUCTION_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `learning_instructions`
--
ALTER TABLE `learning_instructions`
  ADD CONSTRAINT `fk_learning_instructions_injuries1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `fk_notification_notification_type1` FOREIGN KEY (`noti_type_id`) REFERENCES `notification_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `fk_QUESTION_INJURY1` FOREIGN KEY (`injury_id`) REFERENCES `injuries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_USER_CENTER1` FOREIGN KEY (`center_id`) REFERENCES `centers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_USER_ROLE1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
