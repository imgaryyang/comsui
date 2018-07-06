-- 修改1.3 其他本金利息都为''
-- 3 outlier_channel_name少了 单单商户号
-- ledger_book_shelf 加上 独立账户偿付
-- '50000.06.01',

1.借方流水管理表

非订单:  银行科目 现金流指纹 +3级科目汇总表
订单: 现金流数>=1 现金流指纹 + 订单号
              指纹      总额  本金   利息  费用  订单号
 招商银行001  cash001    115  100    10   5     null          非订单
 
 招商银行001  cash002   200  100       20   20     dingdan001       订单（现金流水=1）
 招商银行001  cash003   300  0       0   0     dingdan002       订单（现金流水>1）



2. 还款订单汇总表
订单 现金流数>=1 订单号+3级科目汇总表
            本金  利息  费用
dingdan001   455  30   15

3. 在途线上支付单明细表
第三方支付科目 ，现金流指纹+3级科目汇总表


select fc.ledger_book_no  ,CONCAT('60000.',a.account_no) ,fc.financial_contract_uuid  from financial_contract fc inner join account a on fc.capital_account_id=a.id where contract_no='';

set @ledger_book_no ='101036a7-52db-4e15-ac7c-895484a0af4c';
set @date = '2018-01-08';
set @next_date = '2018-01-09';
set @account_no='60000.1220127571120';
set @financial_contract_uuid = '9495f5f2-d306-461a-8b03-5896923dc1b3';

'3b12ac75-4c58-4375-a733-78c7810efebb'
'e0970224-5e9b-477a-82f3-33ab252545b8'
1
1.1 
select second_account_name as '银行卡', related_lv_3_asset_uuid as '指纹', business_voucher_uuid as '订单号',
sum( CASE WHEN third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') THEN
    debit_balance - credit_balance
    else 0 END ) as '总额',
sum( CASE third_account_uuid WHEN in ('60000.1000.01' THEN
    debit_balance - credit_balance
    else 0 END ) as '本金',
sum( CASE third_account_uuid WHEN '60000.1000.02' THEN
    debit_balance - credit_balance
    else 0 END ) as '利息',
sum( CASE third_account_uuid WHEN '60000.1000.03' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款服务费',
sum( CASE third_account_uuid WHEN '60000.1000.04' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款技术费',
sum( CASE third_account_uuid WHEN '60000.1000.05' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款其他费用',
sum( CASE third_account_uuid WHEN '60000.1000.06' THEN
    debit_balance - credit_balance
    else 0 END ) as '罚息',
sum( CASE third_account_uuid WHEN '60000.1000.07' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期违约金',
sum( CASE third_account_uuid WHEN '60000.1000.08' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期服务费',
sum( CASE third_account_uuid WHEN '60000.1000.09' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期其他费用'

 from ledger_book_shelf where ledger_book_no=@ledger_book_no and book_in_date >= @date and book_in_date < @next_date and second_account_uuid=@account_no and (business_voucher_uuid is null or  business_voucher_uuid='') and related_lv_3_asset_uuid is not null and related_lv_3_asset_uuid!='' and  third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') group by related_lv_3_asset_uuid,third_account_uuid,second_account_name,business_voucher_uuid;


1.2
-- 表中 cashflow=1的数据

select  ro.order_uuid from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid where ro.first_repayment_way_group IN(2,3) and po.pay_status=1 and po.pay_way=0 and ro.order_create_time>='2018-01-08' and ro.order_last_modified_time<'2018-01-09' and ro.financial_contract_uuid=@financial_contract_uuid  
group by ro.order_uuid having count(ro.order_uuid)=1;

@ro_uuid;

select second_account_name as '银行卡', related_lv_3_asset_uuid as '指纹', business_voucher_uuid as '订单号',
sum( CASE WHEN third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') THEN
    debit_balance - credit_balance
    else 0 END ) as '总额',
sum( CASE third_account_uuid WHEN '60000.1000.01' THEN
    debit_balance - credit_balance
    else 0 END ) as '本金',
sum( CASE third_account_uuid WHEN '60000.1000.02' THEN
    debit_balance - credit_balance
    else 0 END ) as '利息',
sum( CASE third_account_uuid WHEN '60000.1000.03' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款服务费',
sum( CASE third_account_uuid WHEN '60000.1000.04' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款技术费',
sum( CASE third_account_uuid WHEN '60000.1000.05' THEN
    debit_balance - credit_balance
    else 0 END ) as '贷款其他费用',
sum( CASE third_account_uuid WHEN '60000.1000.06' THEN
    debit_balance - credit_balance
    else 0 END ) as '罚息',
sum( CASE third_account_uuid WHEN '60000.1000.07' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期违约金',
sum( CASE third_account_uuid WHEN '60000.1000.08' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期服务费',
sum( CASE third_account_uuid WHEN '60000.1000.09' THEN
    debit_balance - credit_balance
    else 0 END ) as '逾期其他费用'

 from ledger_book_shelf where ledger_book_no=@ledger_book_no and book_in_date >= @date and book_in_date < @next_date and second_account_uuid=@account_no and business_voucher_uuid IN (@ro_uuid)  and  third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') group by related_lv_3_asset_uuid,third_account_uuid,second_account_name,business_voucher_uuid;


1.3 
--  表中 cashflow>1的数据

select  ro.order_uuid from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid where ro.first_repayment_way_group IN(2,3) and po.pay_status=1 and po.pay_way=0 and ro.order_create_time>='2018-01-08' and ro.order_last_modified_time<'2018-01-09' and ro.financial_contract_uuid=@financial_contract_uuid  
group by ro.order_uuid having count(ro.order_uuid)>1;

@ro_uuid;

select po.host_account_no as '银行卡', (select string_field_two from cash_flow where cash_flow_uuid=po.outlier_document_uuid) as '现金流标志', po.order_uuid as '订单号',
po.amount as '总额',
'' as '本金',
'' as '利息',
'' as '贷款服务费',
'' as '贷款技术费',
'' as '贷款其他费用',
'' as '罚息',
'' as '逾期违约金',
'' as '逾期服务费',
'' as '逾期其他费用'

 from payment_order po where po.order_uuid IN (@ro_uuid);




select bank_sequence_no as '流水号',
 (case account_side
  when 0 then '贷'
  when 1 then '借'
  end ) as '借贷标志',
transaction_amount as '发生金额',
counter_account_no as '对方账户',
counter_account_name as '对方户名',
transaction_time as '入账时间'
remark as '摘要',
other_remark as '附言' from cash_flow where related_lv_3_asset_uuid in ('') and account_side=1;
-- 注意related_lv_3_asset_uuid没有索引

 


select bank_sequence_no as '流水号',
 (case account_side
  when 0 then '贷'
  when 1 then '借'
  end ) as '借贷标志',
transaction_amount as '发生金额' from cash_flow where host_account_no= and transaction_time>=@date and  transaction_time<=@next_date;


2 
2.1


select  ro.order_uuid from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid where ro.first_repayment_way_group IN(2,3) and po.pay_status=1 and po.pay_way=0 and ro.order_create_time>='2018-01-08' and ro.order_last_modified_time<'2018-01-09' and ro.financial_contract_uuid=@financial_contract_uuid  
group by ro.order_uuid having count(ro.order_uuid)>=1;

@order_uuid;
2.2
select business_voucher_uuid as '订单号',
sum( CASE WHEN third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') THEN
    debit_balance - credit_balance
    else 0 END ) as '总额',
sum( CASE third_account_uuid WHEN '60000.1000.01' THEN
    debit_balance - credit_balance
    else 0 END) as '本金',
sum( CASE third_account_uuid WHEN '60000.1000.02' THEN
    debit_balance - credit_balance
    else 0 END) as '利息',
sum( CASE third_account_uuid WHEN '60000.1000.03' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款服务费',
sum( CASE third_account_uuid WHEN '60000.1000.04' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款技术费',
sum( CASE third_account_uuid WHEN '60000.1000.05' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款其他费用',
sum( CASE third_account_uuid WHEN '60000.1000.06' THEN
    debit_balance - credit_balance
    else 0 END) as '罚息',
sum( CASE third_account_uuid WHEN '60000.1000.07' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期违约金',
sum( CASE third_account_uuid WHEN '60000.1000.08' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期服务费',
sum( CASE third_account_uuid WHEN '60000.1000.09' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期其他费用' from ledger_book_shelf where ledger_book_no=@ledger_book_no and book_in_date >= @date and book_in_date < @next_date and business_voucher_uuid IN (@order_uuid) and third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') group by business_voucher_uuid,third_account_uuid;



3。

select CONCAT('60000.',outlier_channel_name), (case when clearing_no is not null and clearing_no!='' then CONCAT('60000.',outlier_channel_name,'_',clearing_no) else '' end) from payment_channel_information where related_financial_contract_uuid=@financial_contract_uuid;
@outlier_channel_code;

select second_account_name as '银行卡', related_lv_3_asset_uuid as '指纹', business_voucher_uuid as '订单号',
sum( CASE WHEN third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') THEN
    debit_balance - credit_balance
    else 0 END) as '总额',
sum( CASE third_account_uuid WHEN '60000.1000.01' THEN
    debit_balance - credit_balance
    else 0 END) as '本金',
sum( CASE third_account_uuid WHEN '60000.1000.02' THEN
    debit_balance - credit_balance
    else 0 END) as '利息',
sum( CASE third_account_uuid WHEN '60000.1000.03' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款服务费',
sum( CASE third_account_uuid WHEN '60000.1000.04' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款技术费',
sum( CASE third_account_uuid WHEN '60000.1000.05' THEN
    debit_balance - credit_balance
    else 0 END) as '贷款其他费用',
sum( CASE third_account_uuid WHEN '60000.1000.06' THEN
    debit_balance - credit_balance
    else 0 END) as '罚息',
sum( CASE third_account_uuid WHEN '60000.1000.07' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期违约金',
sum( CASE third_account_uuid WHEN '60000.1000.08' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期服务费',
sum( CASE third_account_uuid WHEN '60000.1000.09' THEN
    debit_balance - credit_balance
    else 0 END) as '逾期其他费用'
 from ledger_book_shelf where ledger_book_no=@ledger_book_no and book_in_date >= @date and book_in_date < @next_date and second_account_uuid IN (@outlier_channel_code)  and third_account_uuid IN ('60000.1000.01','60000.1000.02','60000.1000.03','60000.1000.04','60000.1000.05','60000.1000.06','60000.1000.07','60000.1000.08','60000.1000.09') group by related_lv_3_asset_uuid,third_account_uuid,second_account_name,business_voucher_uuid;





