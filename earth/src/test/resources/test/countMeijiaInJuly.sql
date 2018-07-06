SELECT

  q_transaction.`out_lier_order_no` AS '订单号',

  q_transaction.`pay_money`         AS '支付金额',


  q_transaction.`trade_no`          AS '银联交易流水号'
-- sum(`pay_money`)

FROM quark.transaction_record q_transaction INNER JOIN galaxy.rent_order g_order
    ON q_transaction.`out_lier_order_no` = g_order.`order_no`
WHERE q_transaction.app_id = 'meijia' AND

      q_transaction.last_modified_time > '2015-07-27' AND

      q_transaction.last_modified_time < '2015-07-28' AND (q_transaction.transaction_record_status = 2) AND
      q_transaction.pay_money > 1;