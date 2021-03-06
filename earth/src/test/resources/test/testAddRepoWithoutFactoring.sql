SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `factoring_contract`;
INSERT INTO `factoring_contract` (`id`, `adva_matuterm`, `adva_pct`, `adva_start_date`, `adva_term`, `ar_amt`, `ar_cost_rate`, `ar_deposit_amt`, `ar_deposit_amt_aval`, `ar_deposit_amt_input_days`, `ar_deposit_amt_input_type`, `ar_ldgamt`, `borrower_name`, `contract_no`, `cost_deduct_type`, `deposit_deduct_type`, `int_accuretype`, `int_rate`, `latest_settle_date`, `lender_ac_bk_name`, `lender_ac_no`, `lender_name`, `otsd_amt`, `otsd_amt_aval`, `settle_pct`, `app_id`, `company_id`, `nominal_rate`, `payment_agreement_id`)
VALUES
  ('60', '7', 0.70, '2015-06-02 21:47:05', '0', 0.00, 0.00, 0.00, 0.00, '0', '1', 0.00, '寓见', 'DCF-XY-FZR905A', '2',
                                                                                  '2', '2', 0.1966003200, NULL, NULL,
                                                                                  NULL, '鼎程（上海）商业保理有限公司', 0.00, 0.00,
   1.00, '1', '2', 0.0850000000, '2');


DELETE FROM `contract`;
INSERT INTO `contract` (`id`, `begin_date`, `contract_no`, `contract_status`, `deposit`, `end_date`, `interrupt_date`, `interrupt_reason`, `month_fee`, `non_deposit_guarantee_way`, `payment_instrument`, `renewal`, `url`, `app_id`, `customer_id`, `house_id`, `transfer_status`)
VALUES
  ('131', '2015-04-08', 'CC-60512', '0', 2400.00, '2016-04-07', NULL, '', 1200.00, b'0', '0', NULL, NULL, '1', '192',
   '134', '0');

DELETE FROM `asset_package`;
INSERT INTO `asset_package` (`id`, `adva_start_date`, `is_available`, `bank_account`, `bank_name`, `create_time`, `latest_settle_date`, `otsd_amt`, `otsd_amt_aval`, `rec_transfee_total_amt`, `remove_time`, `contract_id`, `factoring_contract_id`)
VALUES
  ('96', '2015-06-26 00:00:00', b'1', NULL, NULL, '2015-07-01 20:06:27', '2015-06-26 00:00:00', 8400.00, 8400.00,
         12000.00, NULL, '131', '61');

DELETE FROM `rent_order`;
INSERT INTO `rent_order` (`id`, `due_date`, `end_date`, `late_fee`, `order_no`, `order_status`, `paid_rent`, `payout_time`, `is_settled`, `start_date`, `total_rent`, `contract_id`, `customer_id`, `repayment_type`, `user_upload_param`, `unique_particle_id`, `transfer_status`, `modify_time`, `unique_bill_id`)
VALUES
  ('877', '2015-06-08', NULL, NULL, 'CC-60512-3', '4', 1200.00, '2015-06-26 16:32:33', b'0', NULL, 1200.00, '131',
   '192', '1', NULL, NULL, '0', '2015-06-26 16:32:33', NULL),
  ('878', '2015-07-08', NULL, NULL, 'CC-60512-4', '0', 0.00, '2015-07-09 18:07:29', b'0', NULL, 1200.00, '131', '192',
   '0', NULL, NULL, '0', NULL, NULL),
  ('879', '2015-08-08', NULL, NULL, 'CC-60512-5', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('880', '2015-09-08', NULL, NULL, 'CC-60512-6', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('881', '2015-10-08', NULL, NULL, 'CC-60512-7', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('882', '2015-11-08', NULL, NULL, 'CC-60512-8', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('883', '2015-12-08', NULL, NULL, 'CC-60512-9', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('884', '2016-01-08', NULL, NULL, 'CC-60512-10', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('885', '2016-02-08', NULL, NULL, 'CC-60512-11', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL),
  ('886', '2016-03-08', NULL, NULL, 'CC-60512-12', '0', 0.00, NULL, b'0', NULL, 1200.00, '131', '192', '0', NULL, NULL,
   '0', NULL, NULL);

DELETE FROM `app_payment_config`;
INSERT INTO `app_payment_config` (`id`, `channel`, `app_id`, `payment_institution_id`) VALUES
  ('1', '1', '1', '2');

DELETE FROM `repo`;
DELETE FROM `transaction_record`;


SET FOREIGN_KEY_CHECKS = 1;