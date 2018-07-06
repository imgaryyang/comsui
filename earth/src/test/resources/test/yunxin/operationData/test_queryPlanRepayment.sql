delete from `daily_plan_repayment`;

INSERT INTO `daily_plan_repayment` (`create_date`, `financial_contract_uuid`, `count`, `plan_style`, `asset_principal_value`, `asset_interest_value`, `loan_service_fee`, `loan_tech_fee`, `loan_other_fee`, `overdue_fee_penalty`, `overdue_fee_obligation`, `overdue_fee_service`, `overdue_fee_other`)
VALUES ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1', '0', '100.00', '10.00', '6.00', '4.00', '3.00', '2.00', '0.00', '0.00', '0.00'),
       ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '3', '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-24', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '1', '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '4', '1', '200.00', '20.00', '5.00', '3.00', '4.00', '6.00', '0.00', '0.00', '0.00'),
       ('2018-01-25', 'd2812bc5-5057-4a91-b3fd-9019506f0499', '1', '2', '100.00', '10.00', '6.00', '4.00', '3.00', '2.00', '0.00', '0.00', '0.00');
