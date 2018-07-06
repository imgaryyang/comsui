delete from `system_operate_log`;
delete from `principal`;

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('11', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2', 
						  		 'test001', '[{\"filedName\":\"uuid\",\"newValue\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\",\"oldValue\":\"\"},{\"filedName\":\"assetPackageFormat\",\"newValue\":\"等额本息\",\"oldValue\":\"锯齿形\"}]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001,信托合同UUID由改为6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f,资产包格式由锯齿形改为等额本息', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('12', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2', 
						  		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						 		 ('13', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2', 
						 		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
								 ('14', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2', 
								'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

								  		
INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								 `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						 		 ('15', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2',
						 		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`,
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('16', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2',
						  		 'test001', '[{\"filedName\":\"assetPackageFormat\",\"newValue\":\"锯齿形\",\"oldValue\":\"等额本息\"}]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"锯齿形\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001,资产包格式由等额本息改为锯齿形', '2016-06-16 15:46:33');
						  		 	

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('17', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '7', '2', 
						  		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"锯齿形\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:46');
						  		 
						  		 
INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, 
						 `t_user_id`, `created_time`, `creator_id`, `modify_password_time`) 
				 VALUES 
				 		('7', 'ROLE_SUPER_USER', 'yunxintest001', 'e10adc3949ba59abbe56e057f20f883e', '2016-05-30 16:21:32', NULL, 
				 		 NULL, NULL, NULL, '2');
						  		 
