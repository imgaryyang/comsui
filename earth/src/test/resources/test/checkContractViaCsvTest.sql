SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM rent_order;
DELETE FROM contract;
DELETE FROM customer;
DELETE FROM house;
DELETE FROM app;

INSERT INTO `app` (`id`, `app_id`, `app_secret`, `is_disabled`, `host`, `name`, `company_id`) VALUES
  ('1', 'xiaoyu', '70991db75ebb24fa0993f4fa25775022', b'0', 'http://beta.demo2do.com/jupiter/', '寓见', '5'),
  ('2', 'youpark', '123456', b'0', '', '优帕克', '4');

SET FOREIGN_KEY_CHECKS = 0;