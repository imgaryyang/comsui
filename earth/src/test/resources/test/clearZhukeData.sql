-- 清理驻客数据

SET @appId = 4;

/* ---  1 */

SET @contractNo = 'ZKCGY012411A';

SET @houseId := (SELECT contract.`house_id`
                 FROM contract
                 WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET @customerId := (SELECT contract.`customer_id`
                    FROM contract
                    WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET @contractId := (SELECT contract.`id`
                    FROM contract
                    WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET foreign_key_checks = 0;

DELETE FROM house
WHERE house.`id` = @houseId;

DELETE FROM `contract_partical`
WHERE contract_partical.`contract_id` = @contractId;

DELETE FROM customer
WHERE customer.`id` = @customerId;

DELETE FROM rent_order
WHERE rent_order.`contract_id` = @contractId;

DELETE FROM contract
WHERE contract.`id` = @contractId;

SET foreign_key_checks = 1;

/* ---  2  ZKCGY012318A*/

SET foreign_key_checks = 0;

DELETE FROM rent_order
WHERE rent_order.`contract_id` IN (468, 838, 839, 840, 848);

DELETE FROM customer
WHERE customer.`id` IN (528, 898, 899, 900, 908);

DELETE FROM house
WHERE house.`id` IN (470, 840, 841, 842, 850);

DELETE FROM contract
WHERE contract.id IN (468, 838, 839, 840, 848);

SET foreign_key_checks = 1;

/* ---  3 */

SET @appId = 4;

SET @contractNo = 'ZKCGY012117A';

SET @houseId := (SELECT contract.`house_id`
                 FROM contract
                 WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET @customerId := (SELECT contract.`customer_id`
                    FROM contract
                    WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET @contractId := (SELECT contract.`id`
                    FROM contract
                    WHERE contract.`contract_no` = @contractNo AND contract.app_id = @appId);

SET foreign_key_checks = 0;

DELETE FROM house
WHERE house.`id` = @houseId;

DELETE FROM `contract_partical`
WHERE contract_partical.`contract_id` = @contractId;

DELETE FROM customer
WHERE customer.`id` = @customerId;

DELETE FROM rent_order
WHERE rent_order.`contract_id` = @contractId;

DELETE FROM contract
WHERE contract.`id` = @contractId;

SET foreign_key_checks = 1;

