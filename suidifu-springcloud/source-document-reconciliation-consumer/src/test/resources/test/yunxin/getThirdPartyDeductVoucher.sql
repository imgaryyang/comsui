SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `journal_voucher`;

INSERT INTO `journal_voucher` (`related_bill_contract_info_lv_3`, `journal_voucher_type`) VALUES
  ('1111111', 7),
  ('2222222', 2);

SET FOREIGN_KEY_CHECKS = 1;