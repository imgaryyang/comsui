SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM app;
DELETE FROM payment_channel;
DELETE FROM company;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`, `addressee`)
VALUES
  ('1', 'anmeitu', '11111db75ebb24fa0993f4fa25775023', '\0', 'http://e.zufangbao.cn', '安美途', '1', NULL);


INSERT INTO `company` (`id`, `address`, `full_name`, `short_name`, `uuid`)
VALUES
  (1, '上海', '测试金融公司', '测试金融', 'a02c02b9-6f98-11e6-bf08-00163e002839');

INSERT INTO
  `payment_channel` (`id`, `channel_name`, `user_name`, `user_password`, `merchant_id`, `cer_file_path`, `pfx_file_path`, `pfx_file_key`, `payment_channel_type`, `api_url`, `from_date`, `thru_date`)
VALUES
  ('1', '广东银联', 'user1', '123456', '123', NULL, NULL, NULL, '0', NULL, NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;