SET FOREIGN_KEY_CHECKS=0;

delete from `ledger_book`;
delete from `ledger_book_shelf`;

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`)
VALUES
	(1, 'YUNXIN_AMEI_ledger_book', '14', NULL, NULL);


INSERT INTO `ledger_book_shelf` (`account_side`,`id`, `ledger_book_owner_id`, `ledger_book_no`, `ledger_uuid`, `related_lv_1_asset_uuid`, `debit_balance`, `credit_balance`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `first_party_id`, `second_party_id`, `third_party_id`, `contract_id`, `journal_voucher_uuid`, `amortized_date`, `business_voucher_uuid`, `source_document_uuid`, `life_cycle`, `backward_ledger_uuid`, `forward_ledger_uuid`, `batch_serial_uuid`, `book_in_date`, `carried_over_date`)
VALUES
	(0,2, 14, 'YUNXIN_AMEI_ledger_book', 'test_ledger_1', 'sss',  0, 1, 'FST_RECIEVABLE_ASSET', '20000', '', '', '', '', '', '', '', NULL, '', NULL, '', NULL, NULL, '', '', '', NULL, NULL),
	(0,4, 14, 'YUNXIN_AMEI_ledger_book', 'test_ledger_2', 'sss',  0, 1, 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_COLLECTION', '20000.05', '', '', '', '', '', NULL, '', NULL, '', NULL, NULL, '', '', '', NULL, NULL),
	(0,5, 14, 'YUNXIN_AMEI_ledger_book', 'test_ledger_3', 'sss',  0, 1, 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_COLLECTION', '20000.05', 'TRD_RECIEVABLE_COLLECTION_OVERDUE_LOAN', '20000.05.01', '', '', '', NULL, '', NULL, '', NULL, NULL, '', '', '', NULL, NULL);


SET FOREIGN_KEY_CHECKS=1;