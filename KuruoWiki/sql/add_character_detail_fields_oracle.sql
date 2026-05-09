-- Oracle 26ai DDL：为角色表添加详情页扩展字段
-- 适配 Oracle 数据库（使用 CLOB 存储大文本）

ALTER TABLE character_info ADD backstory CLOB;
COMMENT ON COLUMN character_info.backstory IS '背景故事';

ALTER TABLE character_info ADD skills CLOB;
COMMENT ON COLUMN character_info.skills IS '技能信息（JSON格式）';

ALTER TABLE character_info ADD build_guide CLOB;
COMMENT ON COLUMN character_info.build_guide IS '养成建议';

-- 示例数据（可选）
UPDATE character_info SET
    backstory = '来自索拉里斯的神秘共鸣者，拥有操控衍射之力的天赋。曾在一次意外中觉醒了强大的共鸣能力，从此踏上了探索世界真相的旅途。',
    skills = '[{"name":"共鸣技能","desc":"释放衍射能量，对前方敌人造成大量伤害"},{"name":"共鸣解放","desc":"进入强化状态，大幅提升攻击力和暴击率，持续15秒"},{"name":"变奏技能","desc":"切换角色时释放能量波动，对周围敌人造成范围伤害"},{"name":"延奏技能","desc":"角色退场后留下能量场，持续对范围内敌人造成伤害"}]',
    build_guide = '推荐武器：千古洑流（5星长刃）' || CHR(10) || '推荐声骸套装：沉日劫明 4件套' || CHR(10) || '主词条优先级：暴击率 > 暴击伤害 > 攻击力%'
WHERE id = 1;
COMMIT;
