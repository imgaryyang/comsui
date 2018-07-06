delete from `daily_remittance`;
delete from `daily_plan_repayment`;
delete from `daily_actual_repayment`;
delete from `daily_guarantee`;
delete from `daily_repurchase`;

INSERT INTO `daily_remittance` (`create_date`, `financial_contract_uuid`, `application_count`, `application_amount`, `plan_count`, `plan_amount`, `actual_count`, `actual_amount`, `asset_amount`, `asset_principal`, `asset_interest`, `asset_loan_service_fee`)
 VALUES ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '7', '600.00', '7', '700.00', '7', '700.00', '100.00', '300.00', '200.00', '100.00'),
        ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '500.00', '2', '400.00', '2', '400.00', '200.00', '100.00', '100.00', '0.00');

INSERT INTO `daily_plan_repayment` (`create_date`, `financial_contract_uuid`, `count`, `plan_style`, `asset_principal_value`, `asset_interest_value`, `loan_service_fee`, `loan_tech_fee`, `loan_other_fee`, `overdue_fee_penalty`, `overdue_fee_obligation`, `overdue_fee_service`, `overdue_fee_other`)
VALUES ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1', '0', '100.00', '10.00', '6.00', '4.00', '3.00', '2.00', '0.00', '0.00', '0.00'),
       ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '3', '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', NULL, '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '1', '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1', '2', '100.00', '10.00', '6.00', '4.00', '3.00', '2.00', '0.00', '0.00', '0.00');


INSERT INTO `daily_actual_repayment` (`create_date`, `business_type`, `financial_contract_uuid`, `journal_voucher_type`, `cash_flow_channel_type`, `count`, `loan_asset_principal`, `loan_asset_interest`, `loan_service_fee`, `loan_tech_fee`, `loan_other_fee`, `overdue_fee_penalty`, `overdue_fee_obligation`, `overdue_fee_service`, `overdue_fee_other`)
 VALUES ('2018-01-25', '0', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, '4', '2', '1000.00', '100.00', '10.00', '0.00', '0.00', '10.00', '10.00', '10.00', '20.00'),
	    ('2018-01-25', '0', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, '8', '5', '5000.00', '500.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00'),
	    ('2018-01-25', '1', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '5', NULL, '4', '4000.00', '400.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00'),
	    ('2018-01-25', '2', 'd2812bc5-5057-4a91-b3fd-9019506f0499', NULL, NULL, '6', '6000.00', '400.00', '10.00', '50.00', '20.00', '10.00', '20.00', '10.00', '50.00');


INSERT INTO `daily_guarantee` (`create_date`, `financial_contract_uuid`, `count`, `amount`)
VALUES ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '6', '6000.00'),
       ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '5', '5000.00');

INSERT INTO `daily_repurchase` (`create_date`, `financial_contract_uuid`, `count`, `repurchase_principal`, `repurchase_interest`, `repurchase_penalty`, `repurchase_other_charges`)
 VALUES ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '2000.00', '200.00', '500.00', '100.00');