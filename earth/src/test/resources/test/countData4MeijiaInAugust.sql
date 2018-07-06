-- 33

SET @appId = 'meijia';

SET @dueDate = '2015-08-01';

SELECT
  g_contract.`contract_no` AS '租约号',
  g_order.`order_no`       AS '订单号',
  g_order.`total_rent`     AS '金额',
  g_customer.`name`        AS '租客名'
FROM quark.bill q_bill INNER JOIN galaxy.rent_order g_order ON q_bill.`out_lier_order_no` = g_order.`order_no`
  INNER JOIN `galaxy`.contract g_contract ON g_contract.`id` = g_order.`contract_id`
  INNER JOIN galaxy.customer g_customer ON g_contract.`customer_id` = g_customer.`id`
WHERE q_bill.bill_status = 2 AND q_bill.`app_id` = @appId AND q_bill.paid_amount > 1 AND is_deleted = FALSE AND
      g_order.`due_date` < @dueDate;

-- 36
SET @appId = 9;

SET @dueDate = '2015-08-01';

SELECT
  g_contract.`contract_no` AS '租约号',
  g_order.`order_no`       AS '订单号',
  g_order.`total_rent`     AS '金额',
  g_customer.`name`        AS '租客名',
  g_order.`order_status`

FROM galaxy.rent_order g_order INNER JOIN `galaxy`.contract g_contract ON g_contract.`id` = g_order.`contract_id`
  INNER JOIN galaxy.customer g_customer ON g_contract.`customer_id` = g_customer.`id`
WHERE g_contract.`app_id` = @appId AND g_order.`total_rent` > 1 AND g_order.`due_date` < @dueDate AND
      (g_order.`order_status` = 2 OR g_order.`order_status` = 5 OR g_order.`order_status` = 4);