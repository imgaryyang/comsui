SET FOREIGN_KEY_CHECKS=0;

delete from `ledger_book`;
delete from `ledger_book_shelf`;



INSERT INTO `ledger_book` (`id`, `ledger_book_no`, `ledger_book_orgnization_id`, `book_master_id`, `party_concerned_ids`) VALUES 
('1', 'YUNXIN_AMEI_ledger_book', '1', '1', '');


SET FOREIGN_KEY_CHECKS=1;
