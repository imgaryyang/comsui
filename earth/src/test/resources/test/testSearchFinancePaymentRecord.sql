SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM finance_payment_record;
DELETE FROM rent_order;
DELETE FROM contract;
DELETE FROM customer;
DELETE FROM receive_order_map;
DELETE FROM asset_package;
DELETE FROM house;
DELETE FROM principal;
DELETE FROM app_arrive_record;
DELETE FROM transaction_record;
DELETE FROM transaction_record_log;
DELETE FROM `company`;
DELETE FROM `app`;

INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`, `contract_life_cycle`, `contract_type`)
VALUES
  ('13', '2015-04-01', 'KXHY1#1803', '0', '33000.00', '2016-03-31', NULL, '', '16500.00', '\0', '1', NULL, NULL, '2',
   '106', '13', '0', '1', '0');
INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`, `contract_life_cycle`, `contract_type`)
VALUES
  ('14', '2014-11-01', 'HNHY3#602', '0', '25200.00', '2015-10-31', NULL, '', '12600.00', '\0', '1', NULL, NULL, '2',
   '107', '14', '0', '6', '0');
INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`, `contract_life_cycle`, `contract_type`)
VALUES ('80', '2015-03-06', 'CC-60168', '0', '3400.00', '2016-03-05', NULL, NULL, '1700.00', '\0', '1', NULL, NULL, '1',
        '141', '83', '0', '1', '0');
INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`, `contract_life_cycle`, `contract_type`)
VALUES
  ('411', '2015-01-01', 'CC-61679', '0', '0.00', '2015-12-31', NULL, NULL, '1000.00', '\0', '1', NULL, NULL, '1', '471',
   '413', '0', '3', '0');
INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`, `contract_life_cycle`, `contract_type`)
VALUES
  ('1080', '2015-07-26', 'CC-62150', '0', '0.00', '2016-07-25', NULL, '', '1400.00', '\0', '2', NULL, NULL, '1', '1140',
   '1076', '0', '1', '0');


INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES ('108', '2015-05-01', NULL, NULL, 'KXHY1#1803-20150402-2', '2', '16500.00', '2015-05-13 15:17:34', '\0', NULL,
               '16500.00', '13', '106', '0', NULL, '59ee73b1-f54d-416c-b23b-12cdaa660e05', '0', NULL,
                           'youpark_KXHY1#1803-20150402-2_108', NULL, NULL, NULL,
                                                                            '47c76809-2f69-4fdb-9a02-04824b9daa1a', '3',
                                                                            '1', '3', '13', '0', '0', '58', NULL);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES ('120', '2015-05-01', NULL, NULL, 'HNHY3#602-20150402-7', '2', '12600.00', '2015-04-30 14:15:36', '\0', NULL,
               '12600.00', '14', '107', '0', NULL, '59ee73b1-f54d-416c-b23b-12cdaa660e05', '0', NULL,
                           'youpark_HNHY3#602-20150402-7_120', NULL, NULL, NULL, '47c76809-2f69-4fdb-9a02-04824b9daa1a',
                                                                           '3', '1', '3', '14', '0', '0', '58', NULL);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  ('384', '2015-04-05', '2015-05-04', NULL, 'CC-60168-2', '2', '1700.00', '2015-06-24 15:30:49', '\0', '2015-04-05',
          '1700.00', '80', '141', '0', NULL, '9dfca839-5d8e-419d-8e9c-50fad3683b9e', '0', NULL, 'xiaoyu_CC-60168-2_384',
                     NULL, NULL, NULL, 'ed8cb46d-525b-47e4-abae-419bae28dc61', '3', '1', '3', '13', '0', '0', '-1',
                                 NULL);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES ('8925', '2015-12-04', '2015-12-04', NULL, 'CC-62150-3-repo', '2', '8032.67', '2015-12-04 15:25:00', '\0',
                '2015-12-04', '8032.67', '1080', '1140', '0', NULL, NULL, '0', NULL, 'xiaoyu_CC-62150-3_7340',
                                         '8032.67', '1', NULL, NULL, '3', '3', '3', '657', '1', '0', '61', NULL);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES ('385', '2015-05-05', '2015-06-04', NULL, 'CC-60168-3', '2', '462.50', '2015-06-24 15:30:49', '\0', '2015-05-05',
               '462.50', '80', '141', '0', NULL, '9dfca839-5d8e-419d-8e9c-50fad3683b9e', '0', NULL,
                         'xiaoyu_CC-60168-3_385', NULL, NULL, NULL, 'ed8cb46d-525b-47e4-abae-419bae28dc61', '3', '1',
                                                              '3', '13', '0', '0', '-1', NULL);
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`, `contract_payment_amount`, `contract_payment_type`, `doubt_record`, `virtual_account_unique_id`, `bill_life_cycle`, `collection_bill_life_cycle`, `collection_bill_capital_status`, `asset_package_id`, `collection_bill_service_notice_status`, `collection_bill_frozen_status`, `factoring_contract_id`, `audit_bill_id`)
VALUES
  ('2922', '2015-03-01', NULL, NULL, 'CC-61679-3', '1', '0.17', NULL, '\0', NULL, '1000.00', '411', '471', '0', NULL,
                                                                                             '9dfca839-5d8e-419d-8e9c-50fad3683b9e',
                                                                                             '1', NULL,
                                                                                             'xiaoyu_CC-61679-3_2922',
                                                                                             NULL, NULL, NULL,
                                                                                                         'ed8cb46d-525b-47e4-abae-419bae28dc61',
                                                                                                         '2', '2', '0',
                                                                                                         '-1', '1', '0',
                                                                                                         '-1', NULL);


INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES
  ('66', '1', '16500.00', '1', '2015-05-13 15:17:34', '108', NULL, NULL, NULL, NULL, NULL, '2015-05-13 15:17:34', NULL);
INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES
  ('41', '1', '12600.00', '1', '2015-04-30 14:15:36', '120', NULL, NULL, NULL, NULL, NULL, '2015-04-30 14:15:36', NULL);
INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES ('694', '9', '1700.00', NULL, '2015-06-24 15:30:49', '384', NULL, NULL, NULL, NULL, NULL, '2015-06-24 15:30:49',
        NULL);
INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES
  ('2442', '9', '8032.67', NULL, '2015-12-04 15:25:00', '8925', '479', NULL, NULL, NULL, NULL, '2015-12-04 15:25:00',
   NULL);
INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES
  ('695', '9', '462.50', NULL, '2015-06-24 20:30:49', '385', NULL, NULL, NULL, NULL, NULL, '2015-06-24 20:30:49', NULL);
INSERT INTO `finance_payment_record` (`id`, `creator_id`, `pay_money`, `payment_no`, `payment_time`, `order_id`, `batch_pay_record_id`, `bank_name`, `card_no`, `payee_name`, `uuid`, `interest_time`, `interest_adjust_note`)
VALUES ('37', '1', '26000.00', '1', '2015-04-27 15:26:48', '2922', NULL, NULL, NULL, NULL, NULL, '2015-04-27 15:26:48',
        NULL);


INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES
  ('151', '00000452059224352', '2', '2015-05-13 15:17:20', NULL, 'KXHY1#1803-20150402-2', '16500.00', NULL, NULL, '2',
          '2', '3', '1', 'bb6c31550bcc4bc0bf1c47396bc8dffd');
INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES
  ('125', '4100620115300481', '2', '2015-04-30 14:15:22', NULL, 'HNHY3#602-20150402-7', '12600.00', NULL, NULL, '2',
          '2', '3', '1', '15e344da10e9402689336b6d4778138a');
INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES ('85', NULL, '1', '2015-04-18 09:11:16', '589231259652654080', 'CC-60168-2', '1700.00',
              '20150418085734589231259652654080', '2015041800001000130048973116', '2', '1', '2', '1',
        '49d7a3772ad7471fa85f6e6f292ebb03');
INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES
  ('3164', NULL, '1', '2015-12-04 15:25:00', NULL, 'CC-62150-3-repo', '8032.67', NULL, NULL, '2', '1', '1', '1', NULL);
INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES ('147', NULL, '1', '2015-05-11 23:22:00', '597782600499004416', 'CC-60168-3', '462.50',
               '20150511111734597782600499004416', '2015051100001000130051125017', '2', '1', '2', '1',
        'bd04047adccc4ef79927bfc4d37f47dc');
INSERT INTO `transaction_record` (`id`, `card_no`, `channel`, `last_modified_time`, `merchant_payment_no`, `order_no`, `pay_money`, `request_no`, `trade_no`, `transaction_record_status`, `app_id`, `payment_institution_id`, `transaction_clearing_status`, `unique_transaction_id`)
VALUES ('487', NULL, '1', '2015-07-06 16:42:42', '617976546311209984', 'CC-61679-3', '0.01',
               '20150706044105617976546311209984', '2015070600001000340058684569', '2', '1', '2', '1',
        '79a40b0a7c494d6b84af32ab999b554c');


INSERT INTO `house` (`id`, `address`, `area`, `bulid_area`, `city`, `community`, `community_detail`, `hall`, `house_status`, `house_type`, `province`, `room`, `app_id`)
VALUES ('13', '汇川路88号1#1803', NULL, '0', NULL, '凯欣豪苑1#1803', NULL, '0', '1', '1', NULL, '0', '2');
INSERT INTO `house` (`id`, `address`, `area`, `bulid_area`, `city`, `community`, `community_detail`, `hall`, `house_status`, `house_type`, `province`, `room`, `app_id`)
VALUES ('14', '徐汇区淮海中路183弄3号602室', NULL, '0', NULL, '汇宁花园3#602', NULL, '0', '1', '1', NULL, '0', '2');
INSERT INTO `house` (`id`, `address`, `area`, `bulid_area`, `city`, `community`, `community_detail`, `hall`, `house_status`, `house_type`, `province`, `room`, `app_id`)
VALUES ('83', 'SH-600108-A', NULL, '0', NULL, 'SH-600108-A', NULL, '0', '1', '2', NULL, '0', '1');
INSERT INTO `house` (`id`, `address`, `area`, `bulid_area`, `city`, `community`, `community_detail`, `hall`, `house_status`, `house_type`, `province`, `room`, `app_id`)
VALUES ('1076', '宝山区顾村顾北东路155弄195号202室', NULL, '0', NULL, 'SH-600643-A', NULL, '0', '1', '2', NULL, '0', '1');
INSERT INTO `house` (`id`, `address`, `area`, `bulid_area`, `city`, `community`, `community_detail`, `hall`, `house_status`, `house_type`, `province`, `room`, `app_id`)
VALUES ('413', '小寓测试专用房源地址', NULL, '0', NULL, '小寓测试专用房源编号', NULL, '0', '1', '2', NULL, '0', '1');


INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`)
VALUES ('37', 'ROLE_SUPER_USER', NULL, 'zhenghangbo', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL);


INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('209', '2015-05-27 16:45:40', '1', '1', '185');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('175', '2015-05-13 15:17:20', '1', '1', '151');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('101', '2015-04-18 08:58:03', '141', '0', '85');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('102', '2015-04-18 09:11:16', '141', '1', '85');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('223', '2015-06-02 11:47:51', '1', '2', '85');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('3164', '2015-09-29 16:06:59', '9', '1', '1750');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('147', '2015-04-30 12:20:45', '1', '1', '124');
INSERT INTO `transaction_record_log` (`id`, `modified_time`, `operator_id`, `transaction_record_operate_status`, `transaction_record_id`)
VALUES ('487', '2015-06-16 10:52:01', '228', '1', '319');


INSERT INTO `receive_order_map` (`id`, `amount`, `association_type`, `create_time`, `is_deducted`, `pay_time`, `app_arrive_record_id`, `order_id`, `receive_order_map_status`)
VALUES ('70', '16500.00', '1', '2015-05-13 15:17:20', '', NULL, '290', '108', '0');
INSERT INTO `receive_order_map` (`id`, `amount`, `association_type`, `create_time`, `is_deducted`, `pay_time`, `app_arrive_record_id`, `order_id`, `receive_order_map_status`)
VALUES ('36', '0.00', '0', '2015-04-28 21:50:01', '\0', NULL, '120', '120', '0');
INSERT INTO `receive_order_map` (`id`, `amount`, `association_type`, `create_time`, `is_deducted`, `pay_time`, `app_arrive_record_id`, `order_id`, `receive_order_map_status`)
VALUES ('40', '12600.00', '1', '2015-04-30 14:15:22', '', NULL, '174', '120', '0');
INSERT INTO `app_arrive_record` (`id`, `amount`, `arrive_record_status`, `pay_ac_no`, `receive_ac_no`, `remark`, `serial_no`, `time`, `app_id`, `drcrf`, `pay_name`, `summary`, `vouh_no`, `operate_remark`, `cash_flow_uid`, `detail_data`, `cash_flow_channel_type`, `transaction_type`, `partner_id`)
VALUES ('174', '12600.00', '2', '4100620115300481', '1001300219000027827', 'Huining, 3-602 May',
               '2015-04-30-14.05.14.747603', '2015-04-30 14:05:14', '2', '2', 'HENGALEXANDRE', 'Huining, 3-602 May',
        '0', NULL, NULL, NULL, '0', '0', NULL);
INSERT INTO `app_arrive_record` (`id`, `amount`, `arrive_record_status`, `pay_ac_no`, `receive_ac_no`, `remark`, `serial_no`, `time`, `app_id`, `drcrf`, `pay_name`, `summary`, `vouh_no`, `operate_remark`, `cash_flow_uid`, `detail_data`, `cash_flow_channel_type`, `transaction_type`, `partner_id`)
VALUES ('290', '16500.00', '2', '00000452059224352', '1001300219000027827', '', '2015-05-13-14.28.23.345608',
               '2015-05-13 14:28:23', '2', '2', '上海化耀国际贸易有限公司', '汇川路88弄1号1803七?', '0', NULL, NULL, NULL, '0', '0',
        NULL);

INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('141', NULL, NULL, NULL, '欧献科', NULL, '寓见', '1');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('106', NULL, NULL, NULL, '上海化耀国际贸易有限公司', NULL, '优帕克', '2');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('107', NULL, NULL, NULL, '吴林飞', NULL, '优帕克', '2');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('1140', NULL, NULL, NULL, '章德生', NULL, '寓见', '1');
INSERT INTO `customer` (`id`, `account`, `city`, `mobile`, `name`, `province`, `source`, `app_id`)
VALUES ('471', NULL, NULL, '', '小寓测试专用', NULL, '寓见', '1');

INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`)
VALUES
  ('13', '2015-04-03 00:00:00', '', NULL, NULL, '2015-04-02 00:00:00', '2015-04-03 00:00:00', '138600.00', '138600.00',
         '198000.00', NULL, '13', '58', NULL);
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`)
VALUES
  ('14', '2015-04-03 00:00:00', '', NULL, NULL, '2015-04-02 00:00:00', '2015-04-03 00:00:00', '61740.00', '61740.00',
         '88200.00', NULL, '14', '58', '2015-11-24 20:25:04');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`)
VALUES
  ('657', '2015-10-20 00:00:00', '', NULL, NULL, '2015-10-20 20:16:40', '2015-10-20 00:00:00', '7840.00', '7840.00',
          '11200.00', NULL, '1080', '61', '2015-12-04 16:46:45');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`)
VALUES
  ('658', '2015-10-20 00:00:00', '', NULL, NULL, '2015-10-20 20:16:40', '2015-10-20 00:00:00', '7840.00', '7840.00',
          '11200.00', NULL, '80', '61', '2015-12-04 16:46:45');
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`, `thru_date`)
VALUES
  ('659', '2015-10-20 00:00:00', '', NULL, NULL, '2015-10-20 20:16:40', '2015-10-20 00:00:00', '7840.00', '7840.00',
          '11200.00', NULL, '411', '61', '2015-12-04 16:46:45');

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`, `payee_account_name`, `payee_account_no`, `payee_bank_name`)
VALUES
  (2, 'youpark', '123456', 00000000, '', '优帕克', 4,
      'guanzhishi@zufangbao.com;zhangjianming@zufangbao.com;jinyin@zufangbao.com;lixu@zufangbao.com;lucy__ni@163.com;zhaolin@dcfco.cn;xuxiaodong@dcfco.cn;zhouming@dcfco.cn;3115855910@qq.com;jackli@dcfco.cn;zhukai@zufangbao.com',
      NULL, NULL, NULL),
  (1, 'xiaoyu', '70991db75ebb24fa0993f4fa25775022', 00000000, 'http://beta.demo2do.com/jupiter/', '寓见', 5,
      'yang.linkui@yujianjia.com;jackli@dcfco.cn;liangzhiping@dcfco.cn;zhouming@dcfco.cn;zhangyan@yujianjia.com;wang.jingfang@yujianjia.com;zhou.songqi@yujianjia.com;chen.li@yujianjia.com;bao.zhongting@yujianjia.com;zhangjianming@zufangbao.com;guanzhishi@zufangbao.com;lixu@zufangbao.com;zheng.jianbo@yujianjia.com;zhukai@zufangbao.com;',
      NULL, NULL, NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `bank_name`, `card_no`, `name`)
VALUES
  (8, '上海', '柯罗芭', '柯罗芭', NULL, NULL, NULL),
  (5, '上海', '小寓科技', '小寓', NULL, NULL, NULL),
  (4, '上海', '上海优帕克投资管理有限公司', '优帕克', NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;