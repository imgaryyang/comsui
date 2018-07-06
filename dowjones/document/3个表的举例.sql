1.1
SELECT
  second_account_name     AS '银行卡',
  related_lv_3_asset_uuid AS '指纹',
  business_voucher_uuid   AS '订单号',
  sum(CASE WHEN third_account_uuid IN
                ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
    THEN
      debit_balance - credit_balance
      ELSE 0 END)         AS '总额',
  sum(CASE third_account_uuid
      WHEN '60000.1000.01'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '本金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.02'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '利息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.03'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.04'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款技术费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.05'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款其他费用',
  sum(CASE third_account_uuid
      WHEN '60000.1000.06'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '罚息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.07'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期违约金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.08'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.09'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期其他费用'

FROM ledger_book_shelf
WHERE ledger_book_no = '101036a7-52db-4e15-ac7c-895484a0af4c' AND book_in_date >= '2018-01-08' AND
      book_in_date < '2018-01-09' AND second_account_uuid = '60000.1220127571120' AND
      (business_voucher_uuid IS NULL OR business_voucher_uuid = '') AND related_lv_3_asset_uuid IS NOT NULL AND
      related_lv_3_asset_uuid != '' AND third_account_uuid IN
                                        ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
GROUP BY related_lv_3_asset_uuid, third_account_uuid, second_account_name, business_voucher_uuid;

1.2


SELECT ro.order_uuid
FROM repayment_order ro INNER JOIN payment_order po ON ro.order_uuid = po.order_uuid
WHERE ro.first_repayment_way_group IN (2, 3) AND po.pay_status = 1 AND po.pay_way = 0 AND
      ro.order_create_time >= '2018-01-08' AND ro.order_last_modified_time < '2018-01-09' AND
      ro.financial_contract_uuid = '9495f5f2-d306-461a-8b03-5896923dc1b3'
GROUP BY ro.order_uuid
HAVING count(ro.order_uuid) = 1;

-- ro.order_uuid为
('01aeae1e-d42e-487a-b6fa-0f28ce1568b4',
  '0977ac3f-14b1-4322-903b-04150ff210d5',
  '23e5554d-f13d-4e25-8a77-adb156a09e1c',
  '69f8a2ab-b456-4bcf-a71b-f9bc17103460',
  'ba315d26-2817-46ba-b718-4f06178d00dd',
  'e3dab059-cdb8-4668-9960-da8d3889122a',
 'f499762b-6e15-4c8a-81c0-9aa386eab65e',
 'fb839d57-115e-4d57-bbf4-f1f7bf83dc04')

SELECT
  second_account_name     AS '银行卡',
  related_lv_3_asset_uuid AS '指纹',
  business_voucher_uuid   AS '订单号',
  sum(CASE WHEN third_account_uuid IN
                ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
    THEN
      debit_balance - credit_balance
      ELSE 0 END)         AS '总额',
  sum(CASE third_account_uuid
      WHEN '60000.1000.01'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '本金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.02'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '利息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.03'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.04'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款技术费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.05'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款其他费用',
  sum(CASE third_account_uuid
      WHEN '60000.1000.06'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '罚息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.07'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期违约金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.08'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.09'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期其他费用'

FROM ledger_book_shelf
WHERE ledger_book_no = '101036a7-52db-4e15-ac7c-895484a0af4c' AND book_in_date >= '2018-01-08' AND
      book_in_date < '2018-01-09' AND second_account_uuid = '60000.1220127571120' AND
      business_voucher_uuid IN ('01aeae1e-d42e-487a-b6fa-0f28ce1568b4',
                                '0977ac3f-14b1-4322-903b-04150ff210d5',
                                '23e5554d-f13d-4e25-8a77-adb156a09e1c',
                                '69f8a2ab-b456-4bcf-a71b-f9bc17103460',
                                'ba315d26-2817-46ba-b718-4f06178d00dd',
                                'e3dab059-cdb8-4668-9960-da8d3889122a',
                                'f499762b-6e15-4c8a-81c0-9aa386eab65e',
                                'fb839d57-115e-4d57-bbf4-f1f7bf83dc04') AND third_account_uuid IN
                                                                            ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
GROUP BY related_lv_3_asset_uuid, third_account_uuid, second_account_name, business_voucher_uuid;


1.3

SELECT ro.order_uuid
FROM repayment_order ro INNER JOIN payment_order po ON ro.order_uuid = po.order_uuid
WHERE ro.first_repayment_way_group IN (2, 3) AND po.pay_status = 1 AND po.pay_way = 0 AND
      ro.order_create_time >= '2018-01-08' AND ro.order_last_modified_time < '2018-01-09' AND
      ro.financial_contract_uuid = '9495f5f2-d306-461a-8b03-5896923dc1b3'
GROUP BY ro.order_uuid
HAVING count(ro.order_uuid) > 1;

('4d40e031-75e9-484b-bb27-701f111b5171',
  '603bd115-b4bc-4ddf-ab81-3171ec9e5f38',
  '797268f4-d774-4996-8377-76a01f2eca99',
  '98c4c780-540f-4431-ad83-b225f58999d6')

-- 1.3 的结果
SELECT
  po.host_account_no                                AS '银行卡',
  (SELECT string_field_two
   FROM cash_flow
   WHERE cash_flow_uuid = po.outlier_document_uuid) AS '现金流标志',
  po.amount                                         AS '总额',
  po.order_uuid                                     AS '订单号'
FROM payment_order po
WHERE po.order_uuid IN ('4d40e031-75e9-484b-bb27-701f111b5171',
                        '603bd115-b4bc-4ddf-ab81-3171ec9e5f38',
                        '797268f4-d774-4996-8377-76a01f2eca99',
                        '98c4c780-540f-4431-ad83-b225f58999d6');


2

SELECT ro.order_uuid
FROM repayment_order ro INNER JOIN payment_order po ON ro.order_uuid = po.order_uuid
WHERE ro.first_repayment_way_group IN (2, 3) AND po.pay_status = 1 AND po.pay_way = 0 AND
      ro.order_create_time >= '2018-01-08' AND ro.order_last_modified_time < '2018-01-09' AND
      ro.financial_contract_uuid = '9495f5f2-d306-461a-8b03-5896923dc1b3'
GROUP BY ro.order_uuid
HAVING count(ro.order_uuid) >= 1;
results:
('01aeae1e-d42e-487a-b6fa-0f28ce1568b4',
'0977ac3f-14b1-4322-903b-04150ff210d5',
'23e5554d-f13d-4e25-8a77-adb156a09e1c',
'4d40e031-75e9-484b-bb27-701f111b5171',
'603bd115-b4bc-4ddf-ab81-3171ec9e5f38',
'69f8a2ab-b456-4bcf-a71b-f9bc17103460',
'797268f4-d774-4996-8377-76a01f2eca99',
'98c4c780-540f-4431-ad83-b225f58999d6',
'ba315d26-2817-46ba-b718-4f06178d00dd',
'e3dab059-cdb8-4668-9960-da8d3889122a',
'f499762b-6e15-4c8a-81c0-9aa386eab65e',
'fb839d57-115e-4d57-bbf4-f1f7bf83dc04')


SELECT
  business_voucher_uuid AS '订单号',
  sum(CASE WHEN third_account_uuid IN
                ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
    THEN
      debit_balance - credit_balance
      ELSE 0 END)       AS '总额',
  sum(CASE third_account_uuid
      WHEN '60000.1000.01'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '本金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.02'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '利息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.03'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '贷款服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.04'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '贷款技术费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.05'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '贷款其他费用',
  sum(CASE third_account_uuid
      WHEN '60000.1000.06'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '罚息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.07'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '逾期违约金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.08'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '逾期服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.09'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)       AS '逾期其他费用'
FROM ledger_book_shelf
WHERE ledger_book_no = '101036a7-52db-4e15-ac7c-895484a0af4c' AND book_in_date >= '2018-01-08' AND
      book_in_date < '2018-01-09' AND business_voucher_uuid IN ('01aeae1e-d42e-487a-b6fa-0f28ce1568b4',
  '0977ac3f-14b1-4322-903b-04150ff210d5',
  '23e5554d-f13d-4e25-8a77-adb156a09e1c',
  '4d40e031-75e9-484b-bb27-701f111b5171',
  '603bd115-b4bc-4ddf-ab81-3171ec9e5f38',
  '69f8a2ab-b456-4bcf-a71b-f9bc17103460',
  '797268f4-d774-4996-8377-76a01f2eca99',
  '98c4c780-540f-4431-ad83-b225f58999d6',
  'ba315d26-2817-46ba-b718-4f06178d00dd',
  'e3dab059-cdb8-4668-9960-da8d3889122a',
  'f499762b-6e15-4c8a-81c0-9aa386eab65e',
                                                                'fb839d57-115e-4d57-bbf4-f1f7bf83dc04') AND
      third_account_uuid IN
      ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
GROUP BY business_voucher_uuid, third_account_uuid;

-- 3
--3.1
SELECT
  CONCAT('60000.', outlier_channel_name),
  (CASE WHEN clearing_no IS NOT NULL AND clearing_no != ''
    THEN CONCAT('60000.', outlier_channel_name, '_', clearing_no)
   ELSE '' END)
FROM payment_channel_information
WHERE related_financial_contract_uuid = '9495f5f2-d306-461a-8b03-5896923dc1b3';

results:
'60000.11002923034501',
'60000.001053110000001',
'60000.001053110000001_001',
'60000.002762',
'60000.002762_001',
'60000.600033029'

最终结果

SELECT
  second_account_name     AS '银行卡',
  related_lv_3_asset_uuid AS '指纹',
  business_voucher_uuid   AS '订单号',
  sum(CASE WHEN third_account_uuid IN
                ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
    THEN
      debit_balance - credit_balance
      ELSE 0 END)         AS '总额',
  sum(CASE third_account_uuid
      WHEN '60000.1000.01'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '本金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.02'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '利息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.03'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.04'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款技术费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.05'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '贷款其他费用',
  sum(CASE third_account_uuid
      WHEN '60000.1000.06'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '罚息',
  sum(CASE third_account_uuid
      WHEN '60000.1000.07'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期违约金',
  sum(CASE third_account_uuid
      WHEN '60000.1000.08'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期服务费',
  sum(CASE third_account_uuid
      WHEN '60000.1000.09'
        THEN
          debit_balance - credit_balance
      ELSE 0 END)         AS '逾期其他费用'
FROM ledger_book_shelf
WHERE ledger_book_no = '101036a7-52db-4e15-ac7c-895484a0af4c' AND book_in_date >= '2018-01-08' AND
      book_in_date < '2018-01-09' AND second_account_uuid IN ('60000.11002923034501',
                                                              '60000.001053110000001',
                                                              '60000.001053110000001_001',
                                                              '60000.002762',
                                                              '60000.002762_001',
                                                              '60000.600033029') AND third_account_uuid IN
                                                                                     ('60000.1000.01', '60000.1000.02', '60000.1000.03', '60000.1000.04', '60000.1000.05', '60000.1000.06', '60000.1000.07', '60000.1000.08', '60000.1000.09')
GROUP BY related_lv_3_asset_uuid, third_account_uuid, second_account_name, business_voucher_uuid;





