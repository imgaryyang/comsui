
SET FOREIGN_KEY_CHECKS=0;

delete from `financial_contract_config`;

INSERT INTO `financial_contract_config` (`id`, `financial_contract_uuid`, `business_type`, `payment_channel_uuids_for_credit`, `payment_channel_uuids_for_debit`, `credit_payment_channel_mode`, `debit_payment_channel_mode`, `payment_channel_router_for_credit`, `payment_channel_router_for_debit`, `payment_channel_config_for_credit`, `payment_channel_config_for_debit`)
VALUES ('2', 'beb90aa6-5cba-4535-b783-57f0801ed7c0', '0', '[\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"ebcaaf81-707b-11e6-bf08-00163e002839\"]', '[\"ebcaaf81-707b-11e6-bf08-00163e002839\"]', '1', '0', 'f893e430-ef53-44e4-b354-72a94638b7e6', 'ebcaaf81-707b-11e6-bf08-00163e002839', '{\"C10102\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10103\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10104\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10105\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10301\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10302\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10303\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10304\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10305\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10306\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10308\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10310\":\"ebcaaf81-707b-11e6-bf08-00163e002839\",\"C10403\":\"ebcaaf81-707b-11e6-bf08-00163e002839\",\"C10802\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10828\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10868\":\"f893e430-ef53-44e4-b354-72a94638b7e6\",\"C10912\":\"f893e430-ef53-44e4-b354-72a94638b7e6\"}', NULL);

SET FOREIGN_KEY_CHECKS=1;
