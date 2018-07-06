SET FOREIGN_KEY_CHECKS=0;

delete from `virtual_account`;


INSERT INTO `virtual_account` (`id`, `total_balance`, `virtual_account_uuid`, `version`, `owner_uuid`, `owner_name`, `create_time`, `last_update_time`) VALUES 
('1', '58035.20', '9126313e-f89d-4222-847c-4e36331cb787', '7d3aad51-05f1-4896-abff-caee93afca79','company_customer_uuid_1', '', '2016-08-24 21:27:10', '2016-08-27 16:57:09');


SET FOREIGN_KEY_CHECKS=1;