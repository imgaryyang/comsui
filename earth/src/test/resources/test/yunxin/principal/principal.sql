
SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `principal`;
DELETE FROM `t_user`;
DELETE FROM `company`;

INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, `t_user_id`, `created_time`, `creator_id`) VALUES
 ('1', 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, '2', NULL, NULL);
INSERT INTO `t_user` (`id`, `uuid`, `name`, `email`, `phone`, `company_id`, `dept_name`, `position_name`, `remark`, `financial_contract_ids`) VALUES
('2', '787c8a18-2d4e-4a49-ba38-54736c328244', '张红兵', 'zhanghongbing@hzsuidifu.com', '', '3', '研发部', '', '', NULL);
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`) VALUES
 ('3', '杭州', '杭州随地付有限公司', '随地付', 'a02c0830-6f98-11e6-bf08-00163e002839');

SET FOREIGN_KEY_CHECKS=1;