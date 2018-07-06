SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM app;
DELETE FROM payment_institution;
DELETE FROM app_account;
DELETE FROM app_payment_config;
DELETE FROM app_service_config;
DELETE FROM payment_institution_param;
DELETE FROM landlord;
DELETE FROM house;
DELETE FROM customer;
DELETE FROM contract;
DELETE FROM rent_order;
DELETE FROM company;
DELETE FROM factoring_contract;
DELETE FROM asset_package;
DELETE FROM transaction_record;
DELETE FROM finance_payment_record;
DELETE FROM settle_record;
DELETE FROM settle_item;
DELETE FROM charge;
DELETE FROM order_payment;
DELETE FROM receive_order_map;
DELETE FROM contract;
DELETE FROM app_account;
DELETE FROM transaction_record;
DELETE FROM app_arrive_record;
DELETE FROM payment_agreement;
DELETE FROM account;


INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`)
VALUES
  (493, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-14', 0, '1600', NULL, '\0', '2015-9-7', 14500.00, 96, 177,
   0, '953fb78f2d6f6ea9cb10a82d399e4464');
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`)
VALUES
  (495, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-15', 1, '14500.00', NULL, '\0', '2015-9-7', 14500.00, 96,
   177, 0, '953fb78f2d6f6ea9cb10a82d399e4464');
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`, `collection_bill_capital_status`)
VALUES
  (496, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-16', 2, '14500.00', NULL, '\0', '2015-9-7', 14500.00, 96,
   177, 0, '953fb78f2d6f6ea9cb10a82d399e4464', 2);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`, `collection_bill_capital_status`)
VALUES
  (497, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-17', 3, '1600', NULL, '\0', '2015-9-7', 14500.00, 96, 177,
   0, '953fb78f2d6f6ea9cb10a82d399e4464', 2);

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`, `collection_bill_capital_status`)
VALUES
  (498, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-18', 2, '14501.00', NULL, '\0', '2015-9-7', 14500.00, 96,
   177, 0, '953fb78f2d6f6ea9cb10a82d399e4464', 2);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `virtual_account_unique_id`, `collection_bill_capital_status`)
VALUES
  (499, '2015-9-7', '2016-10-6', NULL, 'YCHT7#1201-20150508-19', 2, '14490.00', NULL, '\0', '2015-9-7', 14500.00, 96,
   177, 0, '953fb78f2d6f6ea9cb10a82d399e4464', 2);

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  (5596, '2016-01-25', NULL, NULL, 'RJ00117-20150826-7', 2, NULL, NULL, 00000000, NULL, 10000.00, 823, 883, 0, NULL,
                                                                                                  '59ee73b1-f54d-416c-b23b-12cdaa660e05',
                                                                                                  1, NULL,
                                                                                                  'anxin_RJ00117-20150826-7_5596',
                                                                                                  30000.00, 1, NULL,
                                                                                                               '9a7ff4f9-2992-4e6c-b202-17e40ce4bbf3',
                                                                                                               2, 1, 1,
                                                                                                               480, 1,
                                                                                                               0, 63,
                                                                                                               NULL);

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  (5853, '2016-07-24', '2016-08-29', NULL, 'ct-0287-12', 3, 0.00, NULL, 00000000, '2016-07-30', 1170.00, 856, 916, NULL,
                                                                                                         NULL,
                                                                                                         '953fb78f2d6f6ea9cb10a82d399e4464',
                                                                                                         1, NULL,
                                                                                                         'meijia_ct-0287-12_5853',
                                                                                                         NULL, NULL,
    NULL, '36f1c341-ab36-4ec0-bea8-26fd385b9114', 2, 1, 1, -1, 1, 0, -1, NULL);

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  (5852, '2016-06-24', '2016-07-29', NULL, 'ct-0287-11', 0, 0.00, NULL, 00000000, '2016-06-30', 1170.00, 856, 916, NULL,
                                                                                                         NULL,
                                                                                                         '953fb78f2d6f6ea9cb10a82d399e4464',
                                                                                                         1, NULL,
                                                                                                         'meijia_ct-0287-11_5852',
                                                                                                         NULL, NULL,
    NULL, '36f1c341-ab36-4ec0-bea8-26fd385b9114', 3, 3, 1, -1, 1, 0, -1, NULL);

INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  (5851, '2016-05-24', '2016-06-29', NULL, 'ct-0287-10', 0, 0.00, NULL, 00000000, '2016-05-30', 1170.00, 96, 916, NULL,
                                                                                                         NULL,
                                                                                                         '953fb78f2d6f6ea9cb10a82d399e4464',
                                                                                                         1, NULL,
                                                                                                         'meijia_ct-0287-10_5851',
                                                                                                         NULL, NULL,
    NULL, '36f1c341-ab36-4ec0-bea8-26fd385b9114', 2, 3, 1, -1, 1, 0, -1, NULL);


INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`)
VALUES
  ('96', '2014-11-01', 'YCHT7#1201-20150508-17', '0', '25200.00', '2015-10-31', NULL, '', '12600.00', '\0', '0', NULL,
   NULL, '4', '113', '20');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`)
VALUES
  (1, 'xiaoyu', '70991db75ebb24fa0993f4fa25775022', 00000000, 'http://beta.demo2do.com/jupiter/', '寓见', 5),
  (2, 'youpark', '123456', 00000000, '', '优帕克', 4),
  (3, 'test4Zufangbao', '2e85ae999845f25faf8ea7b514ee0aca', 00000000, 'http://e.zufangbao.cn', '租房宝测试账号', 9),
  (4, 'zhuke', 'cb742d55634a532060ac7387caa8d242', b'0', 'http://zkroom.com/', '杭州驻客网络技术有限公司', 6);

INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('152', NULL, NULL, NULL, '上海化耀国际贸易有限公司', NULL, '优帕克', '3');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('107', NULL, NULL, NULL, '吴林飞', NULL, '优帕克', '2');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('108', NULL, NULL, NULL, 'R.B', NULL, '优帕克', '2');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('109', NULL, NULL, NULL, 'OWENBOYDALEXANDER', NULL, '优帕克', '2');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('137', NULL, NULL, NULL, '伊朗马汉航空公司上海代表处', NULL, '优帕克', '2');

SET FOREIGN_KEY_CHECKS = 1;