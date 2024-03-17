CREATE DATABASE `concentrate` /*!40100 DEFAULT CHARACTER SET utf8 */;

-- concentrate.`action` definition

CREATE TABLE `action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL DEFAULT 'rect' COMMENT '前端框架logicflow node类型',
  `x` int(11) NOT NULL COMMENT '横坐标',
  `y` int(11) NOT NULL COMMENT '纵坐标',
  `text` varchar(3000) NOT NULL DEFAULT '{}' COMMENT 'logicflow node文案信息JSON',
  `baseType` varchar(100) DEFAULT NULL COMMENT 'logicflow node基本类型',
  `properties` varchar(18000) DEFAULT '{}',
  `createAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createBy` varchar(100) NOT NULL,
  `updateBy` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


-- concentrate.chat definition

CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `elementId` bigint(20) NOT NULL COMMENT '节点ID，可能是routeid 可能是actionid',
  `chatType` varchar(100) NOT NULL DEFAULT 'action' COMMENT '对话类型 action 节点 route 连线',
  `topics` varchar(100) NOT NULL COMMENT '对话主题',
  `model` varchar(100) NOT NULL COMMENT '使用哪个模型',
  `frequency_penalty` varchar(100) DEFAULT NULL,
  `logprobs` tinyint(1) DEFAULT NULL,
  `top_logprobs` varchar(100) DEFAULT NULL,
  `max_tokens` varchar(100) DEFAULT NULL COMMENT '返回字数限制',
  `presence_penalty` tinyint(4) DEFAULT NULL,
  `response_format` json DEFAULT NULL,
  `seed` tinyint(4) DEFAULT NULL,
  `stop` varchar(10) DEFAULT NULL,
  `temperature` float DEFAULT NULL COMMENT 'between 0 and 2',
  `top_p` float DEFAULT NULL COMMENT 'between 0 and 1',
  `createAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createBy` varchar(100) NOT NULL,
  `updateBy` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


-- concentrate.chatmessage definition

CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(6000) NOT NULL COMMENT '内容',
  `role` varchar(100) NOT NULL COMMENT '角色',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名，区别同一个对话，不同的人员',
  `createAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createBy` varchar(100) NOT NULL,
  `updateBy` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


-- concentrate.gptflow definition

CREATE TABLE `gptflow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `flowName` varchar(256) NOT NULL,
  `parentId` bigint(20) NOT NULL DEFAULT '0',
  `createAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createBy` varchar(100) NOT NULL,
  `updateBy` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


-- concentrate.route definition

CREATE TABLE `route` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flowId` bigint(20) NOT NULL,
  `type` varchar(100) NOT NULL DEFAULT 'polyline',
  `sourceNodeId` bigint(20) NOT NULL COMMENT '上级节点',
  `targetNodeId` bigint(20) NOT NULL COMMENT '下级节点',
  `properties` mediumtext COMMENT '属性JSON',
  `pointsList` mediumtext NOT NULL COMMENT '链接点JSONArray',
  `startPoint` varchar(200) NOT NULL COMMENT '开始坐标JSON',
  `endPoint` varchar(200) NOT NULL COMMENT '结束坐标JSON',
  `createAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createBy` varchar(100) NOT NULL,
  `updateBy` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COMMENT='会话工作流连线，表述两通回话之间的输入输出，以及上下步骤的关联关系';
