SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `repurchase_doc`;


INSERT INTO `repurchase_doc`(`repurchase_doc_uuid` ,`financial_contract_uuid` ,`amount`,`repo_start_date`,`repo_end_date`,`repo_days`,`creat_time` ,`verification_time` ,`last_modifed_time`,`contract_id`,`contract_no` ,`app_id`,`app_name` ,`customer_uuid`,`customer_name`,`executing_asset_set_uuids`,`asset_set_uuids` ,`repurchase_status`, `repurchase_principal`,`repurchase_principal_algorithm`,`repurchase_interest`,`repurchase_interest_algorithm`,`repurchase_penalty`,`repurchase_penalty_algorithm`,`repurchase_other_charges`,`repurchase_other_charges_algorithm`,`amount_detail`) VALUES
('repurchase_doc_uuid_000','d2812bc5-5057-4a91-b3fd-9019506f0499',1600,'2016-12-29','2016-12-29',1,'2016-12-29 19:10:07',NULL,'2016-12-29 19:10:07',310052,'wwtest--contract-30203',3,'测试商户ppd','c9620cb8-b7a2-4748-a770-5ee7b238564f','测试员',NULL,'[\"273e9dd9-79e9-454e-a75a-b7ffcba82b0f\"]',0,'800.00','outstandingPrincipal','800.00','outstandingInterest','0.00','outstandingPenaltyInterest','0',NULL,'{\"amount\":1600.00,\"repurchaseInterest\":800.00,\"repurchaseInterestAlgorithm\":\"outstandingInterest\",\"repurchaseInterestExpression\":\"800.00\",\"repurchaseOtherCharges\":0,\"repurchaseOtherChargesExpression\":\"\",\"repurchasePenalty\":0.00,\"repurchasePenaltyAlgorithm\":\"outstandingPenaltyInterest\",\"repurchasePenaltyExpression\":\"0\",\"repurchasePrincipal\":800.00,\"repurchasePrincipalAlgorithm\":\"outstandingPrincipal\",\"repurchasePrincipalExpression\":\"800.00\"}'
);

SET FOREIGN_KEY_CHECKS=1;
