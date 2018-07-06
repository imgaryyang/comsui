delete from `t_interface_import_asset_package`;



INSERT INTO `t_interface_import_asset_package` (`id`, `fn_code`, `request_no`, `request_data`, `create_time`, `ip`)
VALUES
	(3, '200004', 'Mon Aug 08 11:45:22 CST 2016', '{\"contractDetails\":[{\"bankCode\":\"C10102\",\"bankOfTheCity\":\"110100\",\"bankOfTheProvince\":\"330000\",\"effectDate\":\"2016-8-1\",\"expiryDate\":\"2099-01-01\",\"financialProductCode\":\"G00000\",\"iDCardNo\":\"330683199403062411\",\"interestRateCycle\":1,\"loanContractNo\":\"contractNo1\",\"loanCustomerName\":\"郑航波\",\"loanCustomerNo\":\"customerNo1\",\"loanPeriods\":2,\"loanRates\":\"0.156\",\"loanTotalAmount\":\"4000.00\",\"penalty\":\"0.0005\",\"repaymentAccountNo\":\"23456787654323456\",\"repaymentPlanDetails\":[{\"otheFee\":\"0.00\",\"repaymentDate\":\"2016-09-04\",\"repaymentInterest\":\"20.00\",\"repaymentPrincipal\":\"2000.00\"},{\"otheFee\":\"0.00\",\"repaymentDate\":\"2016-10-04\",\"repaymentInterest\":\"20.00\",\"repaymentPrincipal\":\"2000.00\"}],\"repaymentWay\":1,\"subjectMatterassetNo\":\"234567\",\"uniqueId\":\"34567890\"}],\"thisBatchContractsTotalAmount\":\"4000.00\",\"thisBatchContractsTotalNumber\":1}', '2016-08-08 11:45:27', '115.197.185.49');
