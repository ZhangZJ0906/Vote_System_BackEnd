-- ==============================
-- 線上投票系統 DDL
-- 資料表結構定義
-- ==============================

CREATE DATABASE IF NOT EXISTS voting_system
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE voting_system;

-- ------------------------------
-- 1. 使用者表
-- ------------------------------
CREATE TABLE `users` (
  `id`         bigint       NOT NULL AUTO_INCREMENT,
  `username`   varchar(50)  NOT NULL,
  `password`   varchar(255) NOT NULL COMMENT 'bcrypt hash',
  `email`      varchar(100) DEFAULT NULL,
  `role`       enum('admin','user') NOT NULL DEFAULT 'user',
  `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_username` (`username`),
  UNIQUE KEY `uq_users_email`    (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用者資料';

-- ------------------------------
-- 2. 投票題目表
-- ------------------------------
CREATE TABLE `vote_questions` (
  `id`          int          NOT NULL AUTO_INCREMENT COMMENT '題目ID',
  `title`       varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投票主題名稱',
  `description` text         COLLATE utf8mb4_unicode_ci COMMENT '投票活動描述',
  `is_active`   tinyint(1)   DEFAULT '1'                COMMENT '是否開放投票 (1:開放, 0:關閉)',
  `created_at`  timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------
-- 3. 投票選項表
-- ------------------------------
CREATE TABLE `vote_items` (
  `id`          bigint      NOT NULL AUTO_INCREMENT,
  `question_id` int         NOT NULL COMMENT '所屬題目ID',
  `name`        varchar(100) NOT NULL COMMENT '項目名稱',
  `description` text                  COMMENT '項目說明',
  `vote_count`  bigint      NOT NULL DEFAULT '0' COMMENT '累積票數（由 SP 維護）',
  `is_deleted`  tinyint(1)  NOT NULL DEFAULT '0' COMMENT '軟刪除：0=正常 1=已刪除',
  `created_at`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_vote_items_is_deleted` (`is_deleted`),
  KEY `fk_items_question`         (`question_id`),
  CONSTRAINT `fk_items_question` FOREIGN KEY (`question_id`)
    REFERENCES `vote_questions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票項目';

-- ------------------------------
-- 4. 投票紀錄表
-- ------------------------------
CREATE TABLE `vote_records` (
  `id`           bigint    NOT NULL AUTO_INCREMENT,
  `user_id`      bigint    NOT NULL,
  `vote_item_id` bigint    NOT NULL,
  `created_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_vote` (`user_id`, `vote_item_id`) COMMENT '防同一人重複投同一項目',
  KEY `fk_vr_item` (`vote_item_id`),
  CONSTRAINT `fk_vr_item` FOREIGN KEY (`vote_item_id`)
    REFERENCES `vote_items` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_vr_user` FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票紀錄';

-- ==============================
-- Stored Procedures
-- ==============================

DELIMITER $$

-- 使用者登入
CREATE PROCEDURE sp_login_user(
    IN p_email    VARCHAR(100),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM users
    WHERE email = p_email COLLATE utf8mb4_unicode_ci;
END$$

-- 使用者註冊
CREATE PROCEDURE sp_register_user(
    IN  p_email    VARCHAR(100),
    IN  p_password VARCHAR(255),
    IN  p_username VARCHAR(50),
    OUT p_result   VARCHAR(50)
)
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE email = p_email COLLATE utf8mb4_unicode_ci) THEN
        SET p_result = 'EMAIL_EXISTS';
    ELSE
        INSERT INTO users (email, password, username)
        VALUES (p_email, p_password, p_username);
        SET p_result = 'SUCCESS';
    END IF;
END$$

-- 檢查 Email 是否存在
CREATE PROCEDURE sp_check_user_email(
    IN p_email VARCHAR(100)
)
BEGIN
    SELECT COUNT(email) FROM users
    WHERE email = p_email;
END$$

-- 新增投票題目
CREATE PROCEDURE sp_create_vote_question(
    IN  p_title       VARCHAR(100),
    IN  p_description TEXT,
    OUT p_new_id      BIGINT
)
BEGIN
    INSERT INTO vote_questions (title, description)
    VALUES (p_title, p_description);
    SET p_new_id = LAST_INSERT_ID();
END$$

-- 新增投票選項
CREATE PROCEDURE sp_create_vote_item(
    IN p_question_id INT,
    IN p_item_name   VARCHAR(255)
)
BEGIN
    INSERT INTO vote_items (question_id, name)
    VALUES (p_question_id, p_item_name);
END$$

-- 刪除投票題目（含選項）
CREATE PROCEDURE sp_delete_vote_question(
    IN p_question_id INT
)
BEGIN
    DELETE FROM vote_items    WHERE question_id = p_question_id;
    DELETE FROM vote_questions WHERE id          = p_question_id;
END$$

-- 查詢所有投票題目與選項
CREATE PROCEDURE sp_get_all_votes()
BEGIN
    SELECT
        q.id          AS question_id,
        q.title       AS question_title,
        q.description AS question_description,
        i.id          AS item_id,
        i.name        AS item_name,
        i.vote_count  AS vote_count
    FROM vote_questions q
    LEFT JOIN vote_items i ON q.id = i.question_id
    ORDER BY q.id DESC, i.id ASC;
END$$

-- 批次投票（含 Transaction）
CREATE PROCEDURE sp_cast_vote_batch(
    IN p_item_ids TEXT,
    IN p_user_id  BIGINT
)
BEGIN
    DECLARE v_id    BIGINT;
    DECLARE v_pos   INT DEFAULT 1;
    DECLARE v_len   INT;
    DECLARE v_comma INT;
    DECLARE v_token VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;

    SET v_len = CHAR_LENGTH(p_item_ids);

    WHILE v_pos <= v_len DO
        SET v_comma = LOCATE(',', p_item_ids, v_pos);
        IF v_comma = 0 THEN
            SET v_token = SUBSTRING(p_item_ids, v_pos);
            SET v_pos   = v_len + 1;
        ELSE
            SET v_token = SUBSTRING(p_item_ids, v_pos, v_comma - v_pos);
            SET v_pos   = v_comma + 1;
        END IF;

        SET v_id = CAST(TRIM(v_token) AS UNSIGNED);

        INSERT IGNORE INTO vote_records (user_id, vote_item_id)
        VALUES (p_user_id, v_id);

        IF ROW_COUNT() > 0 THEN
            UPDATE vote_items
            SET vote_count = vote_count + 1
            WHERE id = v_id AND is_deleted = 0;
        END IF;
    END WHILE;

    COMMIT;
END$$

-- 查詢使用者投票歷史
CREATE PROCEDURE sp_get_user_vote_history(
    IN p_user_id INT
)
BEGIN
    SELECT
        q.id          AS question_id,
        q.title       AS question_title,
        i.id          AS item_id,
        i.name        AS item_name,
        r.created_at  AS voted_at
    FROM vote_records r
    JOIN vote_items    i ON r.vote_item_id = i.id
    JOIN vote_questions q ON i.question_id  = q.id
    WHERE r.user_id = p_user_id
    ORDER BY r.created_at DESC;
END$$

DELIMITER ;