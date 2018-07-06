delete from `daily_actual_repayment`;


INSERT INTO `daily_actual_repayment` (`create_date`, `business_type`, `financial_contract_uuid`, `journal_voucher_type`, `cash_flow_channel_type`, `count`, `loan_asset_principal`, `loan_asset_interest`, `loan_service_fee`, `loan_tech_fee`, `loan_other_fee`, `overdue_fee_penalty`, `overdue_fee_obligation`, `overdue_fee_service`, `overdue_fee_other`)
 VALUES ('2018-01-25', '0', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, '4', '2', '1000.00', '100.00', '10.00', '0.00', '0.00', '10.00', '10.00', '10.00', '20.00'),
	    ('2018-01-25', '0', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, '8', '5', '5000.00', '500.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00'),
	    ('2018-01-25', '1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '5', NULL, '4', '4000.00', '400.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00'),
	    ('2018-01-25', '2', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, NULL, '6', '6000.00', '400.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00');
