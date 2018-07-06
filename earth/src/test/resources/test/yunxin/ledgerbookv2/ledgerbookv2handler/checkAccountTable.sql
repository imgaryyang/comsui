SET FOREIGN_KEY_CHECKS=0;

DELETE FROM  t_inside_account;

INSERT INTO `t_inside_account` (`id`, `account_name`, `account_code`, `account_alias`, `parent_account_id`, `level`, `account_side`)
VALUES
	(1, 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE', '20000.05.04', '', 58, 2, 1),
	(2, '000191400205800', '60000.01', '', 24, 1, 1),
	(3, 'TRD_DEFERRED_INCOME_LOAN_SERVICE_FEE', '100000.02.01', '', 51, 2, 0),
	(4, 'FST_DEFERRED_INCOME', '100000', '', NULL, 0, 0),
	(5, 'FST_COST', '90000', '', NULL, 0, 1),
	(6, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL', '50000.06.01', '', 82, 2, 1),
	(7, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', '60000.1000.09', '', 55, 2, 1),
	(8, 'SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_REMITTANCE', '50000.02', '', 25, 1, 0),
	(9, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', '50000.06.07', '', 82, 2, 1),
	(10, 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.01.03', '', 90, 2, 1),
	(11, 'SND_OTHER_PAYABLE_CUSTODY_SAVING', '30000.03', '', 75, 1, 0),
	(12, 'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', '', 66, 1, 1),
	(13, 'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', '', 66, 1, 1),
	(14, 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', '20000.06.02', '', 84, 2, 1),
	(15, 'SND_PAYABLE_CUSTODY_LOAN_PENALTY', '30000.02', '', 75, 1, 0),
	(16, 'TRD_BANK_SAVING_GENERAL_PRINCIPAL', '60000.1000.01', '', 55, 2, 1),
	(17, 'TRD_REVENUE_OVERDUE_FEE_SERVICE_FEE', '70000.06.02', '', 95, 2, 0),
	(18, 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE', '20000.06.03', '', 84, 2, 1),
	(19, 'FST_LONGTERM_LIABILITY', '40000', '', NULL, 0, 0),
	(20, 'SND_UNFROZEN_CAPITAL_VOUCHER', '130000.02', '', 77, 1, 0),
	(21, 'TRD_BANK_SAVING_GENERAL_CUSTODY_LOAN_PENALTY', '60000.1000.12', '', 55, 2, 1),
	(22, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE', '50000.06.04', '', 82, 2, 1),
	(23, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', '60000.1000.07', '', 55, 2, 1),
	(24, 'FST_BANK_SAVING', '60000', '', NULL, 0, 1),
	(25, 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS', '50000', '', NULL, 0, 1),
	(26, 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.05.03', '', 58, 2, 1),
	(27, 'FST_REVENUE', '70000', '', NULL, 0, 0),
	(28, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PAYABLE_REPURCHASE', '50000.06.13', '', 82, 2, 1),
	(29, '000191400206128', '60000.04', '', 24, 1, 1),
	(30, 'FST_ACCOUNT_RECEIVED_IN_ADVANCE', '110000', '', NULL, 0, 0),
	(31, 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04', '', 90, 2, 1),
	(32, 'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', '', 4, 1, 0),
	(33, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE', '50000.06.03', '', 82, 2, 1),
	(34, 'SND_REVENUE_INCOME_FEE', '70000.05', '', 27, 1, 0),
	(35, '19014526016005', '60000.03', '', 24, 1, 1),
	(36, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE', '50000.06.05', '', 82, 2, 1),
	(37, 'TRD_BANK_SAVING_GENERAL_PAYABLE_REPURCHASE', '60000.1000.13', '', 55, 2, 1),
	(38, 'SND_COST_REFUND', '90000.02', '', 5, 1, 1),
	(39, 'SND_RECIEVABLE_LOAN_PENALTY', '20000.03', '', 92, 1, 1),
	(40, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', '60000.1000.08', '', 55, 2, 1),
	(41, 'FST_FINANCIAL_ASSETS_SOLD_FOR_REPURCHASE', '80000', '', NULL, 0, 0),
	(42, '955103657777777', '60000.02', '', 24, 1, 1),
	(43, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', '50000.06.08', '', 82, 2, 1),
	(44, 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '', 90, 2, 1),
	(45, 'TRD_REVENUE_INCOME_LOAN_TECH_FEE', '70000.05.02', '', 34, 2, 0),
	(46, 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', '', 66, 1, 1),
	(47, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', '60000.1000.06', '', 55, 2, 1),
	(48, 'SND_REVENUE_INTEREST', '70000.03', '', 27, 1, 0),
	(49, 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '', 90, 2, 1),
	(50, 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION', '20000.06.01', '', 84, 2, 1),
	(51, 'SND_DEFERRED_INCOME_FEE', '100000.02', '', 4, 1, 0),
	(52, 'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', '', 66, 1, 1),
	(53, 'SND_REVENUE_INVESTMENT_INCOMING', '70000.01', '', 27, 1, 0),
	(54, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', '60000.1000.04', '', 55, 2, 1),
	(55, 'SND_BANK_SAVING_GENERAL', '60000.1000', '', 24, 1, 1),
	(56, 'TRD_DEFERRED_INCOME_LOAN_OTHER_FEE', '100000.02.03', '', 51, 2, 0),
	(57, '955103657777777_INDEPENDENT_ASSETS', '60000.02.01', '', 42, 2, 1),
	(58, 'SND_RECIEVABLE_OVERDUE_LOAN_ASSET', '20000.05', '', 92, 1, 1),
	(59, 'TRD_REVENUE_INCOME_LOAN_OTHER_FEE', '70000.05.03', '', 34, 2, 0),
	(60, 'TRD_REVENUE_INCOME_LOAN_SERVICE_FEE', '70000.05.01', '', 34, 2, 0),
	(61, 'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', '', 19, 1, 0),
	(62, 'TRD_REVENUE_OVERDUE_FEE_OTHER_FEE', '70000.06.03', '', 95, 2, 0),
	(63, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', '50000.06.09', '', 82, 2, 1),
	(64, 'SND_COST_COMMISSION', '90000.03', '', 5, 1, 1),
	(65, 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '50000.01', '', 25, 1, 0),
	(66, 'FST_UNEARNED_LOAN_ASSET', '10000', '', NULL, 0, 1),
	(67, 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE', '20000.01.05', '', 90, 2, 1),
	(68, 'SND_COST_REMITTANCE_FEE', '90000.01', '', 5, 1, 1),
	(69, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', '60000.1000.05', '', 55, 2, 1),
	(70, '001053110000001', '60000.99', '', 24, 1, 1),
	(71, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TOTAL', '50000.06.10', '', 82, 2, 1),
	(72, 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE', '20000.05.01', '', 58, 2, 1),
	(73, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST', '50000.06.02', '', 82, 2, 1),
	(74, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY', '50000.06.06', '', 82, 2, 1),
	(75, 'FST_PAYABLE_ASSET', '30000', '', NULL, 0, 0),
	(76, 'SND_LIABILITIES_INDEPENDENT_WITHDRAW_DEPOSIT', '50000.04', '', 25, 1, 0),
	(77, 'FST_FROZEN_CAPITAL', '130000', '', NULL, 0, 0),
	(78, 'SND_RECIEVABLE_LOAN_GUARANTEE', '20000.02', '', 92, 1, 1),
	(79, 'SND_PAYABLE_REPURCHASE', '30000.05', '', 75, 1, 0),
	(80, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TOTAL', '60000.1000.10', '', 55, 2, 1),
	(81, 'TRD_REVENUE_OVERDUE_FEE_OBLIGATION', '70000.06.01', '', 95, 2, 0),
	(82, 'SND_LIABILITIES_INDEPENDENT_REIMBURSE', '50000.06', '', 25, 1, 1),
	(83, 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST', '20000.05.02', '', 58, 2, 1),
	(84, 'SND_RECIEVABLE_OVERDUE_FEE', '20000.06', '', 92, 1, 1),
	(85, 'TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_OTHER_FEE', '20000.05.05', '', 58, 2, 1),
	(86, 'SND_FROZEN_CAPITAL_VOUCHER', '130000.01', '', 77, 1, 0),
	(87, 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', '', 66, 1, 1),
	(88, 'FST_REPURCHASE_ASSET', '120000', '', NULL, 0, 1),
	(89, 'SND_PAYABLE_COMMISSION', '30000.06', '', 75, 1, 0),
	(90, 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', '', 92, 1, 1),
	(91, 'SND_PAYABLE_REMITTANCE_FEE', '30000.08', '', 75, 1, 0),
	(92, 'FST_RECIEVABLE_ASSET', '20000', '', NULL, 0, 1),
	(93, 'TRD_BANK_SAVING_GENERAL_INTEREST', '60000.1000.02', '', 55, 2, 1),
	(94, 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE', '60000.1000.03', '', 55, 2, 1),
	(95, 'SND_REVENUE_OVERDUE_FEE', '70000.06', '', 27, 1, 0),
	(96, 'SND_PAYABLE_CUSTODY_INTEREST', '30000.04', '', 75, 1, 0),
	(97, 'SND_LIABILITIES_INDEPENDENT_CUSTOMER_REVOKE', '50000.05', '', 25, 1, 0),
	(98, '19014526016005_INDEPENDENT_ASSETS', '60000.03.01', '', 35, 2, 1),
	(99, 'SND_LIABILITIES_INDEPENDENT_INTER_ACCOUNT_BENEFICIARY', '50000.03', '', 25, 1, 0),
	(100, 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_CUSTODY_LOAN_PENALTY', '50000.06.12', '', 82, 2, 1),
	(101, 'SND_PAYABLE_REFUND', '30000.07', '', 75, 1, 0),
	(102, 'TRD_DEFERRED_INCOME_LOAN_TECH_FEE', '100000.02.02', '', 51, 2, 0);

	
SET FOREIGN_KEY_CHECKS=1;