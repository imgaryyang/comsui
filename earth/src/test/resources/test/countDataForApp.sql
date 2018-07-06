SELECT sum(g_order.`total_rent`) AS '金额'
FROM rent_order AS g_order INNER JOIN contract g_contract ON g_contract.`id` =

                                                             g_order.`contract_id`
WHERE g_contract.`app_id` = 8 AND (g_order.`order_status` = 2 OR g_order.`order_status` = 5 OR

                                   g_order.`order_status` = 4);