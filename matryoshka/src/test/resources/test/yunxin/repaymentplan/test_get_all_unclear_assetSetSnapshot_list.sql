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
VALUES (363131, 0, 0, 1698.45, 1615.96
  , 82.49, 1698.45, '2017-05-19', NULL, 0.00
  , 0, 1, 0, NULL, '26721403-f2b5-470c-9904-42b4eb7a7995'
  , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434798863593472', 114289
  , NULL, 1, 0, NULL, 1
  , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
  , 'a4b3c2ea6870473b134f69ddcfaf7ba0', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
  , 1, 0, 0, 0, 6
  , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363132, 0, 0, 1698.45, 1629.42
    , 69.03, 1698.45, '2017-06-19', NULL, 0.00
    , 0, 1, 0, NULL, '11f70000-1af3-442a-a08d-9c16a4edc491'
    , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434798997811200', 114289
    , NULL, 2, 0, NULL, 1
    , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , 'bd6d01ddde2edd62d14f72aed013f78d', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
    , 1, 0, 0, 0, 6
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363133, 0, 0, 1698.45, 1643.00
    , 55.45, 1698.45, '2017-07-19', NULL, 0.00
    , 0, 1, 0, NULL, 'fb353ae4-9e06-4727-a75a-2a9d57b19bcf'
    , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434799266246656', 114289
    , NULL, 3, 0, NULL, 1
    , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '811b804b136501b15536ca8851609f43', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
    , 1, 0, 0, 0, 6
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363134, 0, 0, 1698.45, 1656.69
    , 41.76, 1698.45, '2017-08-19', NULL, 0.00
    , 0, 1, 0, NULL, 'd44afe2f-4fa0-4b06-8e87-b8100834656d'
    , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434799534682112', 114289
    , NULL, 4, 0, NULL, 1
    , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '8f1861bd72e32cb64ccdeaa4f79efbfd', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
    , 1, 0, 0, 0, 6
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363135, 0, 0, 1698.45, 1670.50
    , 27.95, 1698.45, '2017-09-19', NULL, 0.00
    , 0, 1, 0, NULL, '4d77c973-22dc-478c-9aac-f05b99d08064'
    , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434799668899840', 114289
    , NULL, 5, 0, NULL, 1
    , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '9eb9189828c5c1e31d56d324f01a632b', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
    , 1, 0, 0, 0, 6
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363136, 0, 0, 1698.46, 1684.43
    , 14.03, 1698.46, '2017-10-19', NULL, 0.00
    , 0, 1, 0, NULL, 'b6f9c945-2796-4dc6-a844-b21f8b446178'
    , '2017-04-20 15:02:59', '2017-04-20 15:02:59', NULL, 'ZC1678434799937335296', 114289
    , NULL, 6, 0, NULL, 1
    , 1, 0, 'write_off_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '4fccd8f75479d05bf4d34e14672e77f5', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 15:02:59', '2017-04-20 15:02:59', 0
    , 1, 0, 0, 0, 6
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363175, 0, 0, 1698.45, 1615.96
    , 82.49, 1698.45, '2017-05-19', '2017-04-20', 0.00
    , 1, 2, 1, NULL, '7b1f3386-38d8-4b3b-9ab8-113915320500'
    , '2017-04-20 16:46:40', '2017-04-20 18:29:14', NULL, 'ZC1679269736009633792', 114289
    , '2017-04-20 18:27:48', 1, 0, NULL, 882924700
    , 0, 0, 'empty_deduct_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , 'a4b3c2ea6870473b134f69ddcfaf7ba0', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 16:46:40', '2017-04-20 16:46:40', 1
    , 0, 0, 0, 0, 2
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', 0),
  (363176, 0, 0, 1698.45, 1629.42
    , 69.03, 1698.45, '2027-06-19', NULL, 0.00
    , 0, 1, 0, NULL, '07a450ea-2876-4cdb-8f2a-f620701d18d4'
    , '2017-04-20 16:46:40', '2017-04-20 16:46:40', NULL, 'ZC1679269742452084736', 114289
    , NULL, 2, 0, NULL, 882924700
    , 0, 0, 'empty_deduct_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , 'bd6d01ddde2edd62d14f72aed013f78d', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 16:46:40', '2017-04-20 16:46:40', 1
    , 0, 0, 0, 0, 0
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363177, 0, 0, 1698.45, 1643.00
    , 55.45, 1698.45, '2027-07-19', NULL, 0.00
    , 0, 1, 0, NULL, '23ae6867-2e4b-4c5b-9a2a-b4e3c0bb836b'
    , '2017-04-20 16:46:40', '2017-04-20 16:46:40', NULL, 'ZC1679269742854737920', 114289
    , NULL, 3, 0, NULL, 882924700
    , 0, 0, 'empty_deduct_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '811b804b136501b15536ca8851609f43', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 16:46:40', '2017-04-20 16:46:40', 1
    , 0, 0, 0, 0, 0
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363178, 0, 0, 3354.93, 3354.93
    , 0.00, 3354.93, '2027-07-20', NULL, 0.00
    , 0, 1, 0, NULL, '7bb10ec5-bd20-4f64-88fb-2d2f27119dc3'
    , '2017-04-20 16:46:40', '2017-04-20 16:46:40', NULL, 'ZC1679269743123173376', 114289
    , NULL, 4, 0, NULL, 882924700
    , 0, 0, 'empty_deduct_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , 'cbbba94083bfee7aa872fc83967b6498', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 16:46:40', '2017-04-20 16:46:40', 1
    , 0, 0, 0, 0, 0
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (363179, 0, 0, 1698.45, 1656.69
    , 41.76, 1698.45, '2027-08-19', NULL, 0.00
    , 0, 1, 0, NULL, '74b6af42-b5c0-441e-801b-bc83f14b4edc'
    , '2017-04-20 16:46:40', '2017-04-20 16:46:40', NULL, 'ZC1679269743525826560', 114289
    , NULL, 5, 0, NULL, 882924700
    , 0, 0, 'empty_deduct_uuid', NULL, '05cbd490-9d73-4a95-b53a-1f768877e1ca'
    , '8f1861bd72e32cb64ccdeaa4f79efbfd', 'e7b6398d328aaa5a0828014d0a940c0c', '2017-04-20 16:46:40', '2017-04-20 16:46:40', 1
    , 0, 0, 0, 0, 0
    , 0, 'cfbf5107-ee6b-44c0-8129-bc7353f35b34', '2f1a18f0-bb62-42a6-b3eb-fb7c1e91cfe3', NULL),
  (373500, 0, 0, 100.00, 90.00
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