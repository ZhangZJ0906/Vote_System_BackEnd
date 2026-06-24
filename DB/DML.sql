-- ==============================
-- 線上投票系統 DML
-- 初始測試資料
-- ==============================

USE voting_system;

-- ------------------------------
-- 初始使用者（密碼皆為 bcrypt hash）
-- ------------------------------
INSERT INTO `users` (`id`, `username`, `password`, `email`, `role`) VALUES
(1 '小明',   '$2a$10$NdUpAd4iGImkO5nRGy7On.TAZEIzNweX3LosqLVTujyU7hxqPKahW', 'ming@example.com',   'user'),
(2, '張力',   '$2a$10$OoExKHDy7C6XiphP4rhaseY9brkULVwNKq966NxKyUJHSWj1yPVMW', 'john@gmail.com',  'admin');

