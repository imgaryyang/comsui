SET FOREIGN_KEY_CHECKS=0;

DELETE FROM `t_account_template`;

INSERT INTO `t_account_template` (`id`, `account_side`, `amortized_date`, `backward_ledger_uuid`, `batch_serial_uuid`, `book_in_date`, `business_voucher_uuid`, `carried_over_date`, `contract_id`, `contract_uuid`, `create_time`, `credit_balance`, `debit_balance`, `default_date`, `event_type`, `first_account_name`, `first_account_uuid`, `first_party_id`, `forward_ledger_uuid`, `journal_voucher_uuid`, `last_modified_time`, `ledger_book_no`, `ledger_book_owner_id`, `life_cycle`, `merchant_no`, `related_lv_1_asset_outer_idenity`, `related_lv_1_asset_uuid`, `related_lv_2_asset_outer_idenity`, `related_lv_2_asset_uuid`, `related_lv_3_asset_outer_idenity`, `related_lv_3_asset_uuid`, `second_account_name`, `second_account_uuid`, `second_party_id`, `source_document_uuid`, `third_account_name`, `third_account_uuid`, `third_party_id`)
VALUES
	(1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '${asset.InterestValue}', '${asset.PrincipalValue}', NULL, 0, 'FST_UNEARNED_LOAN_ASSET', '74a9ce4d-cafc-407d-b013-987077541bdb', NULL, NULL, NULL, NULL, '74a9ce4d-cafc-407d-b013-987077541bdc', NULL, NULL, 'merchant_no', NULL, NULL, NULL, NULL, NULL, NULL, 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '74a9ce4d-cafc-407d-b013-987077541bdd', NULL, NULL, 'TRD_REVENUE_OVERDUE_FEE_OBLIGATION', '74a9ce4d-cafc-407d-b013-987077541bdce', NULL);

SET FOREIGN_KEY_CHECKS=1;