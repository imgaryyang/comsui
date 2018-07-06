SET FOREIGN_KEY_CHECKS=0;


delete from `app`;
delete from `company`;
delete from `customer`;


INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`,`create_time`,`last_modify_time`)
VALUES
	(1,'xiaoyu','70991db75ebb24fa0993f4fa25775022',00000000,'http://beta.demo2do.com/jupiter/','寓见',1,null,null),
	(2,'youpark','123456',00000000,'','优帕克',2,null,null);
INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`, `legal_person`, `business_licence`) 
VALUES 
('1', '上海', 'fullName1', 'shortName1', '11', NULL, NULL);

INSERT INTO `customer` (`id`, `account`, `mobile`, `name`, `source`, `app_id`, `customer_uuid`, `customer_type`, `customer_style`) 
VALUES 
('1', NULL, NULL, '测试用户1', 'C74211', '1', '81cbfcab-aac4-4c92-9580-0b1d3d5b768f', '0',null);


	
SET FOREIGN_KEY_CHECKS=1;