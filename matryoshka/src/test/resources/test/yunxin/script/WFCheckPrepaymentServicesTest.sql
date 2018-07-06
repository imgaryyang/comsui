SET FOREIGN_KEY_CHECKS=0;

DELETE  FROM contract;
INSERT INTO contract (uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid) VALUES ('4f833cd2-ed15-4639-8a27-97c10479a689', '3b346052-64f5-4517-882b-90f70f4a0fc9', '2016-08-01', '2016-236-DK(hk457999388261286810)号', '2019-01-01', null, 0.00, 3, 10007, 10165, null, '2016-09-21 16:02:45', 0.1100000000, 0, 0, 3, 2, 0.03, 0.2240000000, 1, null, 2, 'd2812bc5-5057-4a91-b3fd-9019506f0499', 0, 'b70afa9d-7b09-4c6e-aafd-e191ccb48d6b');


SET FOREIGN_KEY_CHECKS=1;