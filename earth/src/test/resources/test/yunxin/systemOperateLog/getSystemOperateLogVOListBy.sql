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
						  		 ('12', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '8', '2', 
						  		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						 		 ('13', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '9', '2', 
						 		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"等额本息\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:07');

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								 `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES ('6', '7', '101.231.215.146', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '13', '5', NULL, NULL, '3d0e89258c4d4d7cb394c1fd631421be,62693a10f3e64a85af487a5f0f43e0cd,5b199b2eb72d45349d0aba45e07eeb61,ab4c74c6fbc249089b6f14d60c726ff4,5cb8dc7d301d45f2b4c9015f01caf274,1f93d9b8a9f0437cbe52a61aa8f909e2,d9176929351346988304341ebab2f90e,f2b217ed18dd4172948e24bf7432a359,dd7f365378054fe6bd9eaaa4ce0c136c,eb7915347dd04337a5bf5783165a9389,fdf504ba1ab8426883a64650c75c7d4e,3d115655210848e8ae162cf8dc9ef5aa,61bacf9d50a64748b448f30c6634f6c4,6745bbfd11c0401b968f4c4d90af75cb,518306313b4d4b71ab9808bd9e32ac75,047d02bf5f82403a97e2333790ffe6aa,4484454352364b07ac7bc732298a7a39,47dfa5e020d94f72a066da3602255725,b21f82b4ed01433891549d216fccdc00,dfa9306a997649c189f0125f0f69f73b,c61afa54357c44ca90dbafa964539b05,878e1ba430334239b76c0239ee9f555d,9067e070eb6f47db99ad608a3cb4e663,74989766612b441ca0234d470eb1b046,e555dd6e88eb483aa76151697afd0c03,2bfc9fa130fb4fa49085e1767f9bad8f,2f17965a498346ddb90fb663929d5b83,394db55cffd747eebba81ab3a5e22b09,7441133721f94fea83261d6598d769ee,82183b23a3c448a2ac992d2e0d6d9389,c25eedddf03d430e849bcc77bb5a563a,af4e111efe5c488d8c232babb027f1c8,072c0cfe58764d8390d4afcd2fb85b9f,a5fca42c05674f2b998ac0c5defd7a21,a55cdbedb4ef4a49919d47271e3d1dfb,ca2a6251434d4def9173b609594a39a2,16edbd1038a04fd09dc1fdeeed39b1ac,c0203ffc45b24afc8bd36b64fcd20a7f,38fbd91e8d874b8a99e2b29fc5355d14,790cb5c3ed7f41ccb9aa1a819a733215,155037e414db4267b65215d9f38db59f,2a5c63e14d634421a961910238754e36,d9dfaeba0d434d57ab094e53769c6ab2,9453c9cfd2014086845a9f6665a25968,94c53ed601ec41cc9ac2395d579664a7,4cff7717711a4e5183505c44a39c9b3b,59c650d2da604ea8a8da1912323c9b97,27a12d4f94d5480b8fa3141afad3c9e9,fc8aae3b01a940fead4291dc8b762d46,6d710efa6be74cb2a829a4ff47f73f4d,d5a76e4393fd470ab7455446ecc3f13e,61b1e24e508b4b93be25065a4e1d9b58,af186b44adcc43079e96e415e89e6152,ce8c17ca7e364065ac2bdbf1439d5b84,907ffa0c5dd94ac3a635a755ae3b925f,af5b69fd25d64924a4e5ddab3ad2a64e,be24231f1f1f477099213684eaafc890,8595a082fe3d49ce8889b81929fa1825,7b7cdf056dd24657bb08b92b4f4af7aa,342d2059a4df4adeb327b60148f6e2ef,85b8ac3850e14e75b49fc3830fc879dd,9a6cd0b84d3f408abead3f1b543fc4f1,9b11afb5bfe247a0a4cbf50a2a4f42f7,3052d731e2fa49409fbcf69c5a533ac2,524fbff6a2184d2d82ddb0eade308b5b,55a28a93576744c388df61e32268c5a1,e7a5e1415f1c4a17aa17cd0bdf8ec4ac,c2794edd596444d483fde863fb29d78b,9db3a675873c45c7b110b66a064002d9,0a8b2b13df434b89b8badd7fe84e953e,aec142dffb164f939507687585dcd11a,69f0883bc9cc48eea99f8497aa6c78dd,1300972d094c40c5a01c46947542d7fc,9f27ed004bcc46bd923024576b486066,9ec4eeedf9e44fddb560696325f956f7,7c5e0a068acb48f7ac19521f12b77881,a9bf849c4dcc4028a0a10c74a9c2cb20,9d34aa57c79b4c7db8997809be138237,c438f320bab94b678fd5c17f4e67f38c,9ebe3bba77c442d3a7baeeafc9f49aa6,', NULL, '2016-06-16 15:45:34');

								  		
INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES ('7', '7', '101.231.215.146', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '5', '5', 
						  		 'nfqtest001 20160527 18:27:477', NULL, '{\"available\":false,\"code\":\"nfqtest001 20160527 18:27:477\",\"createTime\":1464344836000,\"financialContractId\":5,\"id\":1,\"loanBatchUuid\":\"0fdd0eac-16e7-4045-8d32-e4d1fe06772e\",\"uuid\":\"0fdd0eac-16e7-4045-8d32-e4d1fe06772e\"}', '导出还款计划nfqtest001 20160527 18:27:477', '2016-06-16 15:45:52');


INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`,
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('16', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '26', '2',
						  		 'test001', '[{\"filedName\":\"assetPackageFormat\",\"newValue\":\"锯齿形\",\"oldValue\":\"等额本息\"}]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"锯齿形\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001,资产包格式由等额本息改为锯齿形', '2016-06-16 15:46:33');
						  		 	

INSERT INTO `system_operate_log` (`id`, `user_id`, `ip`, `object_uuid`, `log_function_type`, `log_operate_type`, 
								  `key_content`, `update_content_detail`, `record_content_detail`, `record_content`, `occur_time`) 
						  VALUES 
						  		 ('17', '7', '115.197.181.57', '6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f', '27', '2', 
						  		 'test001', '[]', '{\"advaMatuterm\":3,\"advaRepoTerm\":91,\"advaStartDate\":\"2016-06-15\",\"app\":\"测试分期\",\"assetPackageFormat\":\"锯齿形\",\"company\":\"测试金融公司\",\"contractName\":\"123\",\"contractNo\":\"test001\",\"financialContractType\":\"消费贷款\",\"ledgerBookNo\":\"5372557e-761f-47f5-bfca-4a257f9c6272\",\"loanOverdueEndDay\":90,\"loanOverdueStartDay\":1,\"paymentChannel\":\"测试分期通道2\",\"thruDate\":\"2016-06-30\",\"trustsAccountName\":\"test\",\"trustsAccountNo\":\"ceshi002\",\"trustsBankNames\":\"ceshi --1\",\"uuid\":\"6a7f0cb2-2edb-4e50-99ad-1f69783a1b2f\"}', '编辑信托合约 test001', '2016-06-16 15:46:46');
						  		 
						  		 
INSERT INTO `principal` (`id`, `authority`, `name`, `password`, `start_date`, `thru_date`, 
						 `t_user_id`, `created_time`, `creator_id`, `modify_password_time`) 
				 VALUES 
				 		('7', 'ROLE_SUPER_USER', 'yunxintest001', 'e10adc3949ba59abbe56e057f20f883e', '2016-05-30 16:21:32', NULL, 
				 		 NULL, NULL, NULL, '2');
						  		 
