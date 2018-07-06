SET FOREIGN_KEY_CHECKS=0;

delete from `financial_contract_configuration`;

INSERT INTO `financial_contract_configuration` (`id`, `uuid`, `financial_contract_uuid` ) 
VALUES ('1', 'uuid1', 'financial_contract_uuid1' );


SET FOREIGN_KEY_CHECKS=1;