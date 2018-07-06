SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `cash_flow`;
DELETE FROM `source_document`;
DELETE FROM `contract`;
DELETE FROM `repurchase_doc`;


INSERT INTO `contract` VALUES
  (376, '1fc6689c-5fb8-11e6-8a0b-0050568ad186', '95cf2dd9-1234-11e7-b556-005056960acf', '2016-05-13',
        '云信信2016-78-DK(ZQ2016042622579)', NULL, 1, 0.00, 1, 377, 377, NULL, '2016-06-03 15:00:03', 0.1560000000, 0, 0,
                                                                      5, 2, 80000.00, 0.0005000000, 1, NULL, 4,
   'ae07d211-2c10-43ed-9924-3d7988db6fe4', 0, '7fa538b9-c8b7-4400-bd34-ddd3caa3bcd2');
INSERT INTO `cash_flow` VALUES
  (2791581, '230e6232-51db-474f-b329-600d13ddceb4', 0, NULL, NULL, '955103657777777', '云南国际信托有限公司',
            '7329410182600051012', '南京特易有信金融信息咨询有限公司', NULL, '{\"bankCode\":\"\",\"bankName\":\"中信银行南京中山东路支行\"}', 1,
                                                                                                                  '2017-04-19 14:43:39',
                                                                                                                  66250.96,
                                                                                                                  9167453.97,
                                                                                                                  '',
                                                                                                                  'G8698800105783C',
                                                                                                                  '史维学还款',
                                                                                                                  '',
                                                                                                                  NULL,
                                                                                                                  '',
    66250.96, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `repurchase_doc` VALUES
  (312, 'a3fafc06-0323-49ef-845f-8eeecfd0ce4c', 'ae07d211-2c10-43ed-9924-3d7988db6fe4', 66250.96, '2017-04-14',
        '2017-04-18', 3, '2017-04-14 00:30:04', NULL, '2017-04-14 00:30:04', 376, '云信信2016-78-DK(ZQ2016042622579)', 1,
                                                                                  '农分期',
                                                                                  '7fa538b9-c8b7-4400-bd34-ddd3caa3bcd2',
                                                                                  '史维学', NULL,
                                                                                  '[\"d25eafc3-4cec-4944-bba3-82324b84b0f6\",\"eeba6d2e-5b96-4137-8365-090a4820fd64\",\"c3b2bfeb-5166-4043-bb86-4850eb043d2a\"]',
                                                                                  0, NULL, 1, 0, NULL, 91, 3,
                                                                                              '{\"amount\":66250.96,\"repurchaseInterest\":5200.00,\"repurchaseInterestAlgorithm\":\"outstandingInterest\",\"repurchaseInterestExpression\":\"5200.00\",\"repurchaseOtherCharges\":0,\"repurchaseOtherChargesExpression\":\"\",\"repurchasePenalty\":1050.96,\"repurchasePenaltyAlgorithm\":\"outstandingPenaltyInterest\",\"repurchasePenaltyExpression\":\"1050.96\",\"repurchasePrincipal\":60000.00,\"repurchasePrincipalAlgorithm\":\"outstandingPrincipal\",\"repurchasePrincipalExpression\":\"60000.00\"}',
                                                                                              60000.00,
                                                                                              'outstandingPrincipal',
                                                                                              5200.00,
                                                                                              'outstandingInterest',
                                                                                              1050.96,
   'outstandingPenaltyInterest', 0.00, NULL);
INSERT INTO `source_document` VALUES
  (114153, 1, '8f131953-8f7d-44f6-af82-62598d532001', 1, '2017-04-21 17:23:13', NULL, 1, 1, 66250.96,
           '230e6232-51db-474f-b329-600d13ddceb4', '2017-04-19 14:43:39', '7329410182600051012', '南京特易有信金融信息咨询有限公司',
                                                                          '955103657777777', '云南国际信托有限公司', NULL, 1,
                                                                          'G8698800105783C', '史维学还款', 66250.96, 3,
    '史维学还款', 1, '{\"remark\":\"史维学\"}', 'FST_LIABILITIES_OF_INDEPENDENT_ACCOUNTS',
    'SND_LIABILITIES_INDEPENDENT_CUSTOMER_DEPOSIT', '', NULL, 0, 'a2901056-c30d-4d06-ad6b-e2489cd7cefb', '1',
    'ce0fe83a-a3be-4077-bdf0-6ab3d20235ec', '50000', '50000.01', '', 0, 0, NULL, 'ae07d211-2c10-43ed-9924-3d7988db6fe4',
    'CZ1691160561585733632', '1', '农分期', 'VACC1040764997502918656', '2017-04-21 17:23:13');


SET FOREIGN_KEY_CHECKS = 1;