SET @appId = 9;

/* ---  1 */

SET @contractNo = 'ct-0052';

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

/* --- 2 */

SET @contractNo = 'ct-0058';

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

/* --- 3 */

SET @contractNo = 'ct-0307';

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

/* ---4 */

SET @contractNo = 'ct-0105';

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

/* ---5 */

SET @contractNo = 'ct-0323';

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