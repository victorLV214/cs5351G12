CREATE TABLE sys_project (
  project_id              BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '项目ID',
  project_name            VARCHAR(100)    NOT NULL                   COMMENT '项目名称',
  project_code            VARCHAR(64)     NOT NULL                   COMMENT '项目编码',
  description             TEXT                                       COMMENT '项目描述',
  start_date              DATE            NOT NULL                   COMMENT '开始日期',
  expected_end_date       DATE                                       COMMENT '预计结束日期',
  actual_end_date         DATE                                       COMMENT '实际结束日期',
  project_manager_id      BIGINT(20)                                 COMMENT '项目负责人ID',
  priority                TINYINT         NOT NULL DEFAULT 2         COMMENT '优先级（1低 2中 3高）',
  status                  VARCHAR(20)     NOT NULL DEFAULT '规划中'   COMMENT '项目状态',
  completion_percentage   DECIMAL(5,2)    DEFAULT 0                  COMMENT '完成百分比',
  budget                  DECIMAL(10,2)                              COMMENT '项目预算',
  actual_cost             DECIMAL(10,2)                              COMMENT '实际花费',
  create_by               VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time             DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by               VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time             DATETIME        NOT NULL                   COMMENT '更新时间',
  remark                  VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  del_flag                CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (project_id),
  UNIQUE KEY idx_project_name (project_name),
  UNIQUE KEY idx_project_code (project_code),
  KEY idx_project_manager (project_manager_id),
  CONSTRAINT fk_project_manager FOREIGN KEY (project_manager_id) REFERENCES sys_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='项目表';


CREATE TABLE sys_requirement (
  requirement_id        BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '需求ID',
  project_id            BIGINT(20)      NOT NULL                   COMMENT '项目ID',
  title                 VARCHAR(200)    NOT NULL                   COMMENT '需求标题',
  description           TEXT                                       COMMENT '需求详细描述',
  priority              TINYINT         NOT NULL DEFAULT 2         COMMENT '优先级（1低 2中 3高）',
  status                VARCHAR(20)     NOT NULL DEFAULT '待处理'   COMMENT '需求状态',
  assigned_to           BIGINT(20)                                 COMMENT '负责人ID',
  due_date              DATE                                       COMMENT '预期完成日期',
  completed_date        DATE                                       COMMENT '实际完成日期',
  type                  VARCHAR(50)                                COMMENT '需求类型',
  estimated_effort      DECIMAL(5,2)                               COMMENT '估计工作量',
  actual_effort         DECIMAL(5,2)                               COMMENT '实际工作量',
  version               VARCHAR(20)                                COMMENT '版本号',
  create_by             VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time           DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by             VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time           DATETIME        NOT NULL                   COMMENT '更新时间',
  remark                VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  del_flag              CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (requirement_id),
  KEY idx_project_id (project_id),
  KEY idx_assigned_to (assigned_to),
  CONSTRAINT fk_requirement_project FOREIGN KEY (project_id) REFERENCES sys_project (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_requirement_user FOREIGN KEY (assigned_to) REFERENCES sys_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='需求表';

CREATE TABLE sys_defect (
  defect_id              BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '缺陷ID',
  project_id             BIGINT(20)      NOT NULL                   COMMENT '项目ID',
  title                  VARCHAR(200)    NOT NULL                   COMMENT '缺陷标题',
  description            TEXT                                       COMMENT '缺陷详细描述',
  severity               TINYINT         NOT NULL                   COMMENT '严重程度（1低 2中 3高 4严重）',
  priority               TINYINT         NOT NULL                   COMMENT '优先级（1低 2中 3高 4紧急）',
  status                 VARCHAR(20)     NOT NULL                   COMMENT '缺陷状态',
  reported_by            BIGINT(20)      NOT NULL                   COMMENT '报告人ID',
  assigned_to            BIGINT(20)                                 COMMENT '负责人ID',
  resolved_at            DATETIME                                   COMMENT '解决时间',
  closed_at              DATETIME                                   COMMENT '关闭时间',
  expected_fix_version   VARCHAR(20)                                COMMENT '预期修复版本',
  actual_fix_version     VARCHAR(20)                                COMMENT '实际修复版本',
  reproduction_steps     TEXT                                       COMMENT '重现步骤',
  environment            VARCHAR(100)                               COMMENT '环境信息',
  related_requirement_id BIGINT(20)                                 COMMENT '相关需求ID',
  create_by              VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time            DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by              VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time            DATETIME        NOT NULL                   COMMENT '更新时间',
  remark                 VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  del_flag               CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (defect_id),
  KEY idx_project_id (project_id),
  KEY idx_reported_by (reported_by),
  KEY idx_assigned_to (assigned_to),
  KEY idx_related_requirement (related_requirement_id),
  CONSTRAINT fk_defect_project FOREIGN KEY (project_id) REFERENCES sys_project (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_defect_reported_by FOREIGN KEY (reported_by) REFERENCES sys_user (user_id) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT fk_defect_assigned_to FOREIGN KEY (assigned_to) REFERENCES sys_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_defect_requirement FOREIGN KEY (related_requirement_id) REFERENCES sys_requirement (requirement_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='缺陷表';

CREATE TABLE sys_work_item (
  work_item_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '工作项ID',
  project_id             BIGINT(20)      NOT NULL                   COMMENT '项目ID',
  iteration_id           BIGINT(20)                                 COMMENT '迭代ID',
  title                  VARCHAR(200)    NOT NULL                   COMMENT '工作项标题',
  description            TEXT                                       COMMENT '工作项详细描述',
  type                   VARCHAR(50)     NOT NULL                   COMMENT '工作项类型',
  status                 VARCHAR(20)     NOT NULL                   COMMENT '工作项状态',
  priority               TINYINT         NOT NULL                   COMMENT '优先级（1低 2中 3高 4紧急）',
  assigned_to            BIGINT(20)                                 COMMENT '负责人ID',
  start_date             DATE                                       COMMENT '开始日期',
  due_date               DATE                                       COMMENT '预期完成日期',
  completed_date         DATE                                       COMMENT '实际完成日期',
  estimated_effort       DECIMAL(5,2)                               COMMENT '估计工作量',
  actual_effort          DECIMAL(5,2)                               COMMENT '实际工作量',
  parent_work_item_id    BIGINT(20)                                 COMMENT '父工作项ID',
  related_requirement_id BIGINT(20)                                 COMMENT '相关需求ID',
  related_defect_id      BIGINT(20)                                 COMMENT '相关缺陷ID',
  create_by              VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time            DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by              VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time            DATETIME        NOT NULL                   COMMENT '更新时间',
  remark                 VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  del_flag               CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (work_item_id),
  KEY idx_project_id (project_id),
  KEY idx_iteration_id (iteration_id),
  KEY idx_assigned_to (assigned_to),
  KEY idx_parent_work_item (parent_work_item_id),
  KEY idx_related_requirement (related_requirement_id),
  KEY idx_related_defect (related_defect_id),
  CONSTRAINT fk_work_item_project FOREIGN KEY (project_id) REFERENCES sys_project (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_work_item_iteration FOREIGN KEY (iteration_id) REFERENCES sys_iteration (iteration_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_work_item_assigned_to FOREIGN KEY (assigned_to) REFERENCES sys_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_work_item_parent FOREIGN KEY (parent_work_item_id) REFERENCES sys_work_item (work_item_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_work_item_requirement FOREIGN KEY (related_requirement_id) REFERENCES sys_requirement (requirement_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_work_item_defect FOREIGN KEY (related_defect_id) REFERENCES sys_defect (defect_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='工作项表';

CREATE TABLE sys_iteration (
  iteration_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '迭代ID',
  project_id             BIGINT(20)      NOT NULL                   COMMENT '项目ID',
  name                   VARCHAR(100)    NOT NULL                   COMMENT '迭代名称',
  description            TEXT                                       COMMENT '迭代详细描述',
  start_date             DATE            NOT NULL                   COMMENT '开始日期',
  end_date               DATE            NOT NULL                   COMMENT '结束日期',
  status                 VARCHAR(20)     NOT NULL                   COMMENT '迭代状态',
  goal                   TEXT                                       COMMENT '迭代目标',
  planned_story_points   INT                                        COMMENT '计划完成的故事点数',
  completed_story_points INT                                        COMMENT '实际完成的故事点数',
  velocity               DECIMAL(5,2)                               COMMENT '团队速度',
  retrospective          TEXT                                       COMMENT '迭代回顾总结',
  create_by              VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time            DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by              VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time            DATETIME        NOT NULL                   COMMENT '更新时间',
  remark                 VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  del_flag               CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (iteration_id),
  KEY idx_project_id (project_id),
  CONSTRAINT fk_iteration_project FOREIGN KEY (project_id) REFERENCES sys_project (project_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='迭代表';

CREATE TABLE sys_task (
  task_id                BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '任务ID',
  project_id             BIGINT(20)      NOT NULL                   COMMENT '项目ID',
  title                  VARCHAR(255)    NOT NULL                   COMMENT '任务标题',
  description            TEXT                                       COMMENT '任务描述',
  status                 VARCHAR(50)     NOT NULL                   COMMENT '任务状态',
  priority               TINYINT(4)      NOT NULL                   COMMENT '任务优先级（0：低，1：中，2：高，3：紧急）',
  assigned_to            BIGINT(20)                                 COMMENT '负责人ID',
  due_date               DATE                                       COMMENT '截止日期',
  estimated_hours        DECIMAL(6,2)                               COMMENT '预估工时',
  actual_hours           DECIMAL(6,2)                               COMMENT '实际工时',
  parent_task_id         BIGINT(20)                                 COMMENT '父任务ID',
  tags                   VARCHAR(500)                               COMMENT '标签（JSON格式）',
  iteration_id           BIGINT(20)                                 COMMENT '迭代ID',
  complexity             TINYINT(4)                                 COMMENT '复杂度（0：简单，1：中等，2：复杂）',
  dependencies           TEXT                                       COMMENT '依赖任务（JSON格式）',
  completed_at           DATETIME                                   COMMENT '完成时间',
  is_archived            TINYINT(1)      NOT NULL DEFAULT 0         COMMENT '是否归档（0否 1是）',
  create_by              VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time            DATETIME        NOT NULL                   COMMENT '创建时间',
  update_by              VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time            DATETIME        NOT NULL                   COMMENT '更新时间',
  del_flag               CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (task_id),
  KEY idx_project_id (project_id),
  KEY idx_assigned_to (assigned_to),
  KEY idx_parent_task_id (parent_task_id),
  KEY idx_iteration_id (iteration_id),
  CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES sys_project (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_task_assigned_to FOREIGN KEY (assigned_to) REFERENCES sys_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_task_parent FOREIGN KEY (parent_task_id) REFERENCES sys_task (task_id) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_task_iteration FOREIGN KEY (iteration_id) REFERENCES sys_iteration (iteration_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='任务表';