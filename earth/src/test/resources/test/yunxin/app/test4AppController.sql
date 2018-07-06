SET FOREIGN_KEY_CHECKS=0;

delete from `app`;
delete from `company`;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`, `create_time`, `last_modify_time`)
VALUES 
('1', 'nongfenqi1', '', 1, '', '测试分期1', '1', NULL, NULL, NULL),
('2', 'nongfenqi2', '', 1, '', '测试分期1', '2', NULL, NULL, NULL),
('3', 'nongfenqi3', '', 1, '', '测试分期2', '1', NULL, NULL, NULL),
('4', 'nongfenqi4', '', 1, '', '测试分期1', '1', NULL, NULL, NULL),
('5', 'nongfenqi5', '', 1, '', '测试分期5', '5', NULL, NULL, NULL),
('6', 'nongfenqi6', '', 1, '', '测试分期6', '6', NULL, NULL, NULL),
('7', 'nongfenqi7', '', 1, '', '测试分期7', '7', NULL, NULL, NULL),
('8', 'nongfenqi8', '', 1, '', '测试分期8', '8', NULL, NULL, NULL);

INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`)
VALUES
('1', '上海', 'fullName1', 'shortName1', '11', NULL, NULL),
('2', '上海', 'fullName2', 'shortName2', '22', NULL, NULL),
('3', '上海', 'fullName3', 'shortName3', '33', NULL, NULL),
('4', '上海', 'fullName4', 'shortName4', '44', NULL, NULL),
('5', '上海', 'fullName5', 'shortName5', '55', NULL, NULL),
('6', '上海', 'fullName6', 'shortName6', '66', NULL, NULL),
('7', '上海', 'fullName7', 'shortName7', '77', NULL, NULL),
('8', '上海', 'fullName8', 'shortName8', '88', NULL, NULL);

SET FOREIGN_KEY_CHECKS=1;