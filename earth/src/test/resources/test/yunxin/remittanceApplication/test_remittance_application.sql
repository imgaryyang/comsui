SET FOREIGN_KEY_CHECKS=0;


delete from `financial_contract`;
delete from  ledger_book_shelf;
delete from  t_remittance_application_detail;
delete from `t_remittance_application`;
delete from  `contract`;





INSERT INTO `financial_contract` (`financial_contract_uuid`,`adva_repayment_term`,`id`, `adva_matuterm`, `contract_no`, `contract_name`, `app_id`, `company_id`,   `adva_repo_term`,`loan_overdue_end_day`,`loan_overdue_start_day`,`payment_channel_id`,`ledger_book_no`,`asset_package_format`,`financial_contract_type`,`unusual_modify_flag`) VALUES 
('financial_contract_uuid_1','0','1', 3, 'DCF-NFQ-LR903A', '云南信托', 1, 14, 30,90,1,1,'yunxin_ledger_book',1,0,0);

INSERT INTO `t_remittance_application_detail` (`priority_level`,`id`,`remittance_application_detail_uuid`,`remittance_application_uuid`,`financial_contract_uuid`,`actual_total_amount`) VALUES (1,'1','34dhf2312fash3fgsd32f','347fsdhjhfygfdjskjfdsl','financial_contract_uuid_1',20000);

INSERT INTO `t_remittance_application` (`plan_notify_number`,`actual_notify_number`,`id`,`financial_contract_uuid`,`financial_contract_id`,`actual_total_amount`,`remittance_application_uuid`,`request_no`,`contract_unique_id`,`contract_no`) VALUES (10,10,'1','financial_contract_uuid_1','1',1000,'347fsdhjhfygfdjskjfdsl','33','contract_unique_id_1','contract_no_1');

insert into `contract`(`id`,`uuid`,`unique_id`,`contract_no`,`app_id`,`customer_id`,`house_id`,`payment_day_in_month`,`payment_frequency`,`periods`)values(1,'contract_uuid_1','contract_unique_id_1','contract_no_1',123,123,123,321,1,12);

SET FOREIGN_KEY_CHECKS=1;