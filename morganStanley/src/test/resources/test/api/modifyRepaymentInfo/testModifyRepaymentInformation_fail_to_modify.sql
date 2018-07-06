SET FOREIGN_KEY_CHECKS = 0;

USE galaxy_autotest_yunxin;

DELETE FROM contract;
DELETE FROM contract_account;
DELETE FROM company;
DELETE FROM app;
DELETE FROM house;
DELETE FROM customer;
DELETE FROM principal;
DELETE FROM t_interface_repayment_information_log;
DELETE FROM t_api_config;

INSERT INTO t_api_config (api_url, description, api_status)
VALUES
  ('/api/v3/modifyRepaymentInformation', '变更还款信息', 1);

INSERT INTO principal (id, authority, name, password, start_date, thru_date, t_user_id, created_time, creator_id, modify_password_time)
VALUES (1, 'ROLE_SUPER_USER', 'zhanghongbing', '376c43878878ac04e05946ec1dd7a55f', NULL, NULL, 2, NULL, NULL, 1);
INSERT INTO company (id, address, full_name, short_name, uuid)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');
INSERT INTO app (id, app_id, app_secret, is_disabled, host, name, company_id, addressee)
VALUES
  (1, 'qyb', NULL, 1, NULL, '测试商户yqb', 1, NULL);
INSERT INTO house (id, address, app_id)
VALUES
  (1, 'newyork', '1');
INSERT INTO customer (id, app_id)
VALUES
  (1, '1');
INSERT INTO contract (id, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, state)
VALUES
  (323, '12345678', '2016-04-17', '云信信2016-78-DK(ZQ2016042522479)', NULL, '1', '0.00', '1', '1', '1', NULL,
   '2016-06-15 22:04:59', '0.1560000000', '0', '0', '3', '2', '1200.00', '0.0005000000', '4');

INSERT INTO contract_account (id, pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, city, from_date, thru_date)
VALUES
  (323, '6217000000000003006', NULL, '323', '测试用户1', '中国邮政储蓄银行', NULL, NULL, '403', '安徽省', '亳州', '2016-07-13 10:03:25',
   '2016-07-13 10:21:26'),
  (348, 'bankAccount', NULL, '323', '测试用户1', '中国邮政储蓄银行', NULL, NULL, '604', '安徽省', '亳州', '2016-07-13 10:21:26',
   '2900-01-01 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;