DELETE FROM `contract`;

insert into `contract` 
( `repayment_plan_operate_logs`, `contract_no`, `house_id`, `interest_rate`,
    `asset_type`, `create_time`, `end_date`, `penalty_interest`, `uuid`, `actual_end_date`, `unique_id`, 
       `payment_day_in_month`, `customer_id`, `app_id`, `month_fee`, `state`, `financial_contract_uuid`, `id`, `payment_frequency`, 
         `active_version_no`, `repayment_way`, `total_amount`, `begin_date`, `periods`)
    values ( null, '2016-78-DK(ZQ2016042522479)', '162', '0.1560000000', '1', '2016-05-27 18:27:16', null, '0.0005000000', 
      'a0afc961-5fa8-11e6-b2c2-00163e002839', null, null, '0', '1', '1', '0.00', '2', '2d380fe1-7157-490d-9474-12c5a9901e29',
         '1', '0', '1', '2', '1200.00', '2016-04-17', '1');