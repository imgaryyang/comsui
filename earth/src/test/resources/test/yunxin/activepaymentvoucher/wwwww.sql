SET FOREIGN_KEY_CHECKS = 0;

delete from third_party_pay_voucher_batch;


INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('1', 'a78d48ab-0950-11e7-9677-c83a35ce563d', '666214124124', 'a78d48d6-0950-11e7-9677-c83a35ce563d', 'a78d48d0-0950-11e7-9677-c83a35ce563d', '2017-03-15 15:32:52', '10');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('2', '8ecf06f1-0951-11e7-9677-c83a35ce563d',  '8ecf070f-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '8ecf0712-0951-11e7-9677-c83a35ce563d', '2016-01-15 15:32:56', '20');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('3', '8f3cfcd0-0951-11e7-9677-c83a35ce563d',  '8f3cfcff-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '8f3cfd02-0951-11e7-9677-c83a35ce563d', '2016-02-15 15:32:59', '30');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('4', '8f88a9d6-0951-11e7-9677-c83a35ce563d',  '8f88a9f2-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '8f88a9f5-0951-11e7-9677-c83a35ce563d', '2017-03-15 15:33:01', '40');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('5', '8fb98977-0951-11e7-9677-c83a35ce563d',  '8fb9898f-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '8fb98992-0951-11e7-9677-c83a35ce563d', '2017-03-15 15:33:02', '50');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('6', '8ff42272-0951-11e7-9677-c83a35ce563d',  '8ff42292-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '8ff42295-0951-11e7-9677-c83a35ce563d', '2017-06-15 15:33:04', '60');
INSERT INTO `third_party_pay_voucher_batch` (`id`, `batch_uuid`, `request_no`, `financial_contract_uuid`, `financial_contract_no`, `create_time`, `size`) VALUES ('7', '921c9b2a-0951-11e7-9677-c83a35ce563d',  '921c9b5b-0951-11e7-9677-c83a35ce563d', '111cfcfb-0951-11e7-9677-c83a35ce563d', '921c9b5e-0951-11e7-9677-c83a35ce563d', '2017-07-15 15:33:05', '70');

SET FOREIGN_KEY_CHECKS = 1;