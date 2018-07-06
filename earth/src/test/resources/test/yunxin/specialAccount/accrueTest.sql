SET FOREIGN_KEY_CHECKS = 0 ;

delete from `ledger_book_shelf`;
delete from `ledger_book`;

INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`, `ledger_book_version`) VALUES 
('1', 'ledger_book_no_1', '1', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1 ;