SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `financial_contract_configuration`;


INSERT INTO `financial_contract_configuration` (`id`, `uuid`, `financial_contract_uuid`, `code`, `content`)
VALUES
  ('3', '194d8ffd-9085-42a1-8363-a5cc51998e48', 'c01c-43c2-8e50-606cc3a16d28', '4', '0'),
  ('4', '188dfca1-3be3-11e7-ab82-525400dbb013', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '1');


SET FOREIGN_KEY_CHECKS = 1;