-- ==============================
-- 線上投票系統 DDL
-- DB: MySQL 8.0+
-- ==============================

CREATE DATABASE IF NOT EXISTS voting_system
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE voting_system;

-- ------------------------------
-- 1. 使用者表
-- ------------------------------
CREATE TABLE users (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)   NOT NULL,
    password    VARCHAR(255)  NOT NULL COMMENT 'bcrypt hash',
    email       VARCHAR(100),
    role        ENUM('admin','user') NOT NULL DEFAULT 'user',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_username (username),
    UNIQUE KEY uq_users_email    (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用者資料';

-- ------------------------------
-- 2. 投票項目表
-- ------------------------------
CREATE TABLE vote_items (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL COMMENT '項目名稱',
    description TEXT                   COMMENT '項目說明',
    vote_count  BIGINT        NOT NULL DEFAULT 0 COMMENT '累積票數（由 SP 維護）',
    is_deleted  TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '軟刪除：0=正常 1=已刪除',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_vote_items_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票項目';

-- ------------------------------
-- 3. 投票紀錄表（中間表）
-- ------------------------------
CREATE TABLE vote_records (
    id            BIGINT    NOT NULL AUTO_INCREMENT,
    user_id       BIGINT    NOT NULL,
    vote_item_id  BIGINT    NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_user_vote (user_id, vote_item_id) COMMENT '防同一人重複投同一項目',
    CONSTRAINT fk_vr_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_vr_item FOREIGN KEY (vote_item_id)
        REFERENCES vote_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票紀錄';

-- ==============================
-- Stored Procedures
-- ==============================

DELIMITER $$

-- 1. 新增投票項目
CREATE PROCEDURE sp_create_vote_item(
    IN  p_name        VARCHAR(100),
    IN  p_description TEXT,
    OUT p_new_id      BIGINT
)
BEGIN
    INSERT INTO vote_items (name, description)
    VALUES (p_name, p_description);
    SET p_new_id = LAST_INSERT_ID();
END$$

-- 2. 更新投票項目
CREATE PROCEDURE sp_update_vote_item(
    IN p_id          BIGINT,
    IN p_name        VARCHAR(100),
    IN p_description TEXT
)
BEGIN
    UPDATE vote_items
    SET name = p_name, description = p_description
    WHERE id = p_id AND is_deleted = 0;
END$$

-- 3. 軟刪除投票項目
CREATE PROCEDURE sp_delete_vote_item(
    IN p_id BIGINT
)
BEGIN
    UPDATE vote_items
    SET is_deleted = 1
    WHERE id = p_id;
END$$

-- 4. 查詢所有有效投票項目（含票數）
CREATE PROCEDURE sp_get_all_vote_items()
BEGIN
    SELECT id, name, description, vote_count, created_at
    FROM vote_items
    WHERE is_deleted = 0
    ORDER BY vote_count DESC, id ASC;
END$$

-- 5. 使用者投票（支援多選，含 Transaction）
--    p_item_ids: 逗號分隔的項目 ID 字串，例如 '1,3,5'
CREATE PROCEDURE sp_cast_votes(
    IN  p_user_id   BIGINT,
    IN  p_item_ids  TEXT,
    OUT p_result    VARCHAR(50)
)
BEGIN
    DECLARE v_id     BIGINT;
    DECLARE v_done   INT DEFAULT 0;
    DECLARE v_pos    INT DEFAULT 1;
    DECLARE v_len    INT;
    DECLARE v_comma  INT;
    DECLARE v_token  VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_result = 'ERROR';
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

        -- 插入紀錄（若已投過則忽略，UNIQUE KEY 保護）
        INSERT IGNORE INTO vote_records (user_id, vote_item_id)
        VALUES (p_user_id, v_id);

        -- 同步更新 vote_count
        IF ROW_COUNT() > 0 THEN
            UPDATE vote_items
            SET vote_count = vote_count + 1
            WHERE id = v_id AND is_deleted = 0;
        END IF;
    END WHILE;

    COMMIT;
    SET p_result = 'OK';
END$$

DELIMITER ;

-- ==============================
-- 初始資料 (DML)
-- ==============================

INSERT INTO users (username, password, role) VALUES
('admin', '$2a$12$placeholder_bcrypt_hash', 'admin');

INSERT INTO vote_items (name, description) VALUES
('選項 A', '第一個投票選項'),
('選項 B', '第二個投票選項'),
('選項 C', '第三個投票選項');