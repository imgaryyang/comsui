UPDATE rent_order
SET rent_order.`collection_bill_service_notice_status` = 0, rent_order.`transfer_status` = 0
WHERE rent_order.`bill_life_cycle` = 3;