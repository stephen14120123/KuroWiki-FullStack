-- ============================================================
-- Oracle 26ai DDL：创建用户表
-- 注意：USER 是 Oracle 保留字，必须使用双引号包裹
-- ============================================================

-- 创建用户表
CREATE TABLE "USER" (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- 自增主键
    username    VARCHAR2(50)  NOT NULL UNIQUE,                     -- 用户名（唯一）
    password    VARCHAR2(100) NOT NULL,                            -- 密码
    nickname    VARCHAR2(50),                                      -- 昵称
    role        NUMBER(1) DEFAULT 0 NOT NULL,                     -- 角色：0-普通用户, 1-管理员
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP               -- 创建时间
);

-- 添加表注释
COMMENT ON TABLE "USER" IS '用户表';
COMMENT ON COLUMN "USER".id IS '用户ID（自增主键）';
COMMENT ON COLUMN "USER".username IS '用户名（唯一）';
COMMENT ON COLUMN "USER".password IS '密码';
COMMENT ON COLUMN "USER".nickname IS '昵称';
COMMENT ON COLUMN "USER".role IS '角色：0-普通用户, 1-管理员';
COMMENT ON COLUMN "USER".create_time IS '注册时间';

-- 创建用户名索引（加速登录查询）
CREATE INDEX idx_user_username ON "USER"(username);

-- 插入默认管理员账号（密码: admin123）
INSERT INTO "USER" (username, password, nickname, role)
VALUES ('admin', 'admin123', '系统管理员', 1);

-- 插入测试普通用户（密码: 123456）
INSERT INTO "USER" (username, password, nickname, role)
VALUES ('player1', '123456', '漂泊者一号', 0);

COMMIT;

-- 插入初始管理员账号 (admin / admin123)
INSERT INTO "USER" (username, password, nickname, role)
VALUES ('admin', 'admin123', '超级管理员', 1);

-- 别忘了提交事务
COMMIT;
