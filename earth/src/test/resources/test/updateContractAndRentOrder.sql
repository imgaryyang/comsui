-- 设置订单足额
UPDATE `rent_order`
SET `rent_order`.payment_status = 0
WHERE `rent_order`.total_rent = `rent_order`.paid_rent;
-- 设置订单不足额
UPDATE `rent_order`
SET `rent_order`.payment_status = 1
WHERE `rent_order`.total_rent > `rent_order`.paid_rent;
-- 设置订单超额
UPDATE `rent_order`
SET `rent_order`.payment_status = 2
WHERE `rent_order`.total_rent < `rent_order`.paid_rent;