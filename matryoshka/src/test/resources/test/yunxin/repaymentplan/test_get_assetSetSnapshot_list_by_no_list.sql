SET @@FOREIGN_KEY_CHECKS = 0;

DELETE FROM galaxy_autotest_yunxin.asset_set;

INSERT INTO `asset_set` (`id`, `guarantee_status`, `settlement_status`, `asset_fair_value`, `asset_principal_value`
  , `asset_interest_value`, `asset_initial_value`, `asset_recycle_date`, `confirm_recycle_date`, `refund_amount`
  , `asset_status`, `on_account_status`, `repayment_plan_type`, `last_valuation_time`, `asset_uuid`
  , `create_time`, `last_modified_time`, `comment`, `single_loan_contract_no`, `contract_id`
  , `actual_recycle_date`, `current_period`, `overdue_status`, `overdue_date`, `version_no`
  , `active_status`, `sync_status`, `active_deduct_application_uuid`, `repurchase_status`, `financial_contract_uuid`
  , `asset_finger_print`, `asset_extra_fee_finger_print`, `asset_finger_print_update_time`, `asset_extra_fee_finger_print_update_time`, `plan_type`
  , `write_off_reason`, `can_be_rollbacked`, `time_interval`, `deduction_status`, `executing_status`
  , `executing_status_bak`, `customer_uuid`, `contract_uuid`, `contract_funding_status`)
VALUES (373500, 0, 0, 100.00, 90.00
    , 1.00, 91.00, '2017-04-22', '2017-04-09', 0.00
    , 1, 2, 0, NULL, '259ef4d2-95d0-478a-b2e3-e9fe3ec1dc0d'
    , '2017-04-21 17:50:40', '2017-04-21 17:53:02', NULL, 'ZC1691381619185422336', 124365
    , '2017-04-09 10:00:00', 1, 0, NULL, 1
    , 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
    , '12f2d1ab43432e896004fab5eeac44b2', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
    , 0, 0, 0, 0, 2
    , 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', 0),
  (373501, 0, 0, 10010.00, 10000.00
    , 1.00, 10001.00, '2027-05-16', NULL, 0.00
    , 0, 1, 0, NULL, '922b2d45-da2c-4b6b-87a2-30a5303a5da0'
    , '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381619856510976', 124365
    , NULL, 2, 0, NULL, 1
    , 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
    , 'baed3060329832c3b3c8df6337e1bf35', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
    , 0, 0, 0, 0, 0
    , 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', NULL),
  (373502, 0, 0, 9920.00, 9910.00
    , 1.00, 9911.00, '2027-12-18', NULL, 0.00
    , 0, 1, 0, NULL, '1dcb05fd-8602-4030-b45b-180b4d0b8281'
    , '2027-04-21 17:50:40', '2017-04-21 17:50:40', NULL, 'ZC1691381620527599616', 124365
    , NULL, 3, 0, NULL, 1
    , 0, 0, 'empty_deduct_uuid', NULL, 'b674a323-0c30-4e4b-9eba-b14e05a9d80a'
    , 'eefa7bbe6cca45afba129716475512ec', '513521f46449e01fe6b0e9dc3ee9035b', '2017-04-21 17:50:40', '2017-04-21 17:50:40', 0
    , 0, 0, 0, 0, 0
    , 0, 'c714fe88-8ed3-45e9-9807-59ca5bd37ae3', 'be834b15-56a7-4175-b926-64c90869a2f0', NULL);
SET @@FOREIGN_KEY_CHECKS = 1;