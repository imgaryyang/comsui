DELETE FROM contract;
DELETE FROM financial_contract;
DELETE FROM asset_set;
DELETE FROM journal_voucher;
DELETE FROM t_remittance_application;
DELETE FROM customer;
DELETE FROM contract_account;
DELETE FROM customer_person;
TRUNCATE TABLE ledger_book_shelf;
DELETE FROM asset_set_extra_charge;


INSERT INTO contract (id, uuid, unique_id, begin_date, contract_no, end_date, asset_type, month_fee, app_id, customer_id, house_id, actual_end_date, create_time, interest_rate, payment_day_in_month, payment_frequency, periods, repayment_way, total_amount, penalty_interest, active_version_no, repayment_plan_operate_logs, state, financial_contract_uuid, interest_rate_cycle, customer_uuid, repaymented_periods, completion_status, date_field_one, long_field_one, long_field_two, string_field_one, string_field_two, string_field_three, decimal_field_one, decimal_field_two)
VALUES (12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', 'acc38ad1-e20b-4c6a-873f-57d71ba1de05', '2018-01-22',
                  'acc38ad1-e20b-4c6a-873f-57d71ba1de05', '2099-01-01', NULL, 0.00, 9, 271667, 271802, NULL,
                                                                                                       '2018-01-22 15:32:31',
                                                                                                       0.0000000000, 0,
                                                                                                       0, 1, 2, 3000.00,
                                                                                                       0.0005000000,
                                                                                                       -1870907127,
  NULL, 2, 'c420576a-4d5b-4d45-b880-2fd5508cc2db', 1, 'a64c257a-8c41-44d4-9f35-b933028401b6', 0, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL);

INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004410, 'a1dfddf0-74fd-4fc5-b474-775b3a1adaa6', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '85929fc4-19b9-48bd-b88c-8cd53ce01ffa', '2018-02-22', '2018-01-22 15:32:31', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-02-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956321181696',
                                                                                         'fc7bd626-37a5-4b47-8f84-4780543f3098',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004411, '1deaf19b-dc61-470d-b6ac-06ca90301604', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               '85929fc4-19b9-48bd-b88c-8cd53ce01ffa',
                                                                               '2018-02-22', '2018-01-22 15:32:31', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-02-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956321181696',
                                                                                     'fc7bd626-37a5-4b47-8f84-4780543f3098',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004412, 'a20a5556-bd39-49e2-8699-853824532267', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  'e55ef815-6e18-4bc5-b2a4-4230ef8343df', '2018-03-22', '2018-01-22 15:32:31', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-03-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956333764608',
                                                                                         'a27f4aa5-f674-4dc2-94be-a71f8af00afa',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004413, '5a580439-81a8-4b80-87dd-9b25ea15a628', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               'e55ef815-6e18-4bc5-b2a4-4230ef8343df',
                                                                               '2018-03-22', '2018-01-22 15:32:31', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-03-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956333764608',
                                                                                     'a27f4aa5-f674-4dc2-94be-a71f8af00afa',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004414, '97ba5e39-610e-4e18-b58d-d10b3f6fc5e0', 0.00, 1000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '60ea2735-dd42-41b4-8a6d-20c88bc378fc', '2018-04-22', '2018-01-22 15:32:31', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-04-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956350541824',
                                                                                         '0b2d0831-6796-4777-ba94-a9fd8b1ad319',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004415, '858de05d-b3e3-44be-b1d8-8b2bc816e23c', 1000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               '60ea2735-dd42-41b4-8a6d-20c88bc378fc',
                                                                               '2018-04-22', '2018-01-22 15:32:31', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-04-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956350541824',
                                                                                     '0b2d0831-6796-4777-ba94-a9fd8b1ad319',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004416, '460f89ce-014d-4789-84ec-99c4ee8ee75b', 1000.00, 0.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  'cab84c13-2414-4038-a978-c91d55650998', '2018-02-22', '2018-01-22 15:33:00', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-02-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956321181696',
                                                                                         'fc7bd626-37a5-4b47-8f84-4780543f3098',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004417, 'fd34142f-f1b4-43e5-ac3a-925f1a1411b1', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               'cab84c13-2414-4038-a978-c91d55650998',
                                                                               '2018-02-22', '2018-01-22 15:33:00', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-02-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956321181696',
                                                                                     'fc7bd626-37a5-4b47-8f84-4780543f3098',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004418, '4c6c891d-7ce7-46d5-9a27-b25ba872cbc3', 1000.00, 0.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '5bc6bfbf-63a7-453f-a6eb-0e6ab11f0564', '2018-03-22', '2018-01-22 15:33:00', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-03-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956333764608',
                                                                                         'a27f4aa5-f674-4dc2-94be-a71f8af00afa',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004419, 'd993645b-ec99-496e-bd6f-44abd9b58173', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               '5bc6bfbf-63a7-453f-a6eb-0e6ab11f0564',
                                                                               '2018-03-22', '2018-01-22 15:33:00', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-03-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956333764608',
                                                                                     'a27f4aa5-f674-4dc2-94be-a71f8af00afa',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004420, '947c3c8d-4132-48ac-b7d8-90bafcb3bc39', 1000.00, 0.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  'a4d5d05a-5eeb-410d-9c2d-bde7a0bfa890', '2018-04-22', '2018-01-22 15:33:00', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-04-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152839956350541824',
                                                                                         '0b2d0831-6796-4777-ba94-a9fd8b1ad319',
                                                                                         NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004421, '63d1652a-16b8-4a3c-81fe-6957d81777c3', 0.00, 1000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               'a4d5d05a-5eeb-410d-9c2d-bde7a0bfa890',
                                                                               '2018-04-22', '2018-01-22 15:33:00', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-04-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152839956350541824',
                                                                                     '0b2d0831-6796-4777-ba94-a9fd8b1ad319',
                                                                                     NULL, NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004422, '677820b7-95b1-4193-92a3-c2716cdf3008', 0.00, 3000.00, 'FST_LONGTERM_LIABILITY', '40000', 0,
                  'SND_LONGTERM_LIABILITY_ABSORB_SAVING', '40000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004423, '5706def7-069f-4655-bc1c-b8fb8e1f4a36', 0.00, 1.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004424, '5f3d07cf-8f19-4fe5-9712-826c4def7ed0', 0.00, 2.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_SERVICE_FEE', '100000.02.01',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004425, 'c1abaf1b-1acd-45b5-9e0d-5a7349eb7fe0', 3.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                              'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                              NULL, NULL, NULL,
                                                                              '3a04fa14-b5d2-40b1-827b-178f6f43ad62',
                                                                              '2018-01-22', '2018-01-22 15:33:01', '',
                                                                              NULL, 12483592,
                                                                                    '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                    '2018-01-22 00:00:00', '',
                                                                                    'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                    '11', 1, 'ZC152840079217958912',
                                                                                    '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                    '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004426, '3634be6d-d0e6-407b-8a45-771f4d141148', 3000.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               '3a04fa14-b5d2-40b1-827b-178f6f43ad62',
                                                                               '2018-01-22', '2018-01-22 15:33:01', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-01-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152840079217958912',
                                                                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                     '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004427, '18a517bf-5db7-4849-b37e-842e05037526', 0.00, 4.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_OTHER_FEE', '100000.02.03',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004428, '8ba4be90-5689-431b-b2fe-760f4c1468a1', 0.00, 3.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_TECH_FEE', '100000.02.02',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004429, '08cfa282-902e-4a14-a71c-25fb0b501cc4', 4.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL, NULL,
                                                                               '3a04fa14-b5d2-40b1-827b-178f6f43ad62',
                                                                               '2018-01-22', '2018-01-22 15:33:01', '',
                                                                               NULL, 12483592,
                                                                                     '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                     '2018-01-22 00:00:00', '',
                                                                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                     '11', 1, 'ZC152840079217958912',
                                                                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                     '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004430, 'b049d383-1fd8-4720-a549-7caea44076a1', 1.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                              'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                              NULL, NULL, NULL,
                                                                              '3a04fa14-b5d2-40b1-827b-178f6f43ad62',
                                                                              '2018-01-22', '2018-01-22 15:33:01', '',
                                                                              NULL, 12483592,
                                                                                    '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                    '2018-01-22 00:00:00', '',
                                                                                    'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                    '11', 1, 'ZC152840079217958912',
                                                                                    '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                    '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004431, '17f2b70f-0542-4405-9cf0-9c60f8262064', 2.00, 0.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL, NULL,
  '3a04fa14-b5d2-40b1-827b-178f6f43ad62', '2018-01-22', '2018-01-22 15:33:01', '', NULL, 12483592,
                                                                                         '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                         '2018-01-22 00:00:00', '',
                                                                                         'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                         '11', 1,
                                                                                         'ZC152840079217958912',
                                                                                         '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                         '', NULL, NULL, NULL, '');
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004432, '75d67441-e2fd-4171-9d0e-9153fd1ff6fc', 1.00, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_INTEREST_ESTIMATE', '100000.01', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL,
  '5a78935a-6573-424b-95f6-70bbbd870562', NULL, 'd23223f3-9a44-4dc1-8c67-0a712fab1380', '2018-01-22',
  '2018-01-22 15:33:01', NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004433, '5a78935a-6573-424b-95f6-70bbbd870562', 0.00, 1.00, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INTEREST',
                  '70000.03', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b', '', NULL, NULL,
                                          '75d67441-e2fd-4171-9d0e-9153fd1ff6fc',
                                          'd23223f3-9a44-4dc1-8c67-0a712fab1380', '2018-01-22', '2018-01-22 15:33:01',
                                          NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                      '2018-01-22 00:00:00', NULL,
                                                      'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1,
                                                      'ZC152840079217958912', '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                      '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004434, '790a72c7-6176-4631-ab00-174eae42a114', 0.00, 1.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_INTEREST', '10000.01', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                              'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                              NULL,
                                                                              '1d0e3a7e-cd40-4457-b814-da6c320ff904',
                                                                              NULL,
                                                                              '9578dd4b-e726-41df-a2d0-1254695d4d7b',
                                                                              '2018-01-22', '2018-01-22 15:33:01', NULL,
                                                                              NULL, 12483592,
                                                                                    '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                    '2018-01-22 00:00:00', NULL,
                                                                                    'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                    '11', 1, 'ZC152840079217958912',
                                                                                    '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                    '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004435, '1d0e3a7e-cd40-4457-b814-da6c320ff904', 1.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL,
  '790a72c7-6176-4631-ab00-174eae42a114', '9578dd4b-e726-41df-a2d0-1254695d4d7b', '2018-01-22', '2018-01-22 15:33:01',
  NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
              'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
              '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004436, '65951d20-c0ce-439a-8162-1403a489d161', 0.00, 2.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', '10000.03', NULL, NULL,
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL,
  '9aa7372d-9b10-40f4-a794-51a69de3018d', NULL, '1b17c3f2-155e-40ab-b620-2a43a22d79d7', '2018-01-22',
  '2018-01-22 15:33:01', NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004437, '9aa7372d-9b10-40f4-a794-51a69de3018d', 2.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.01.03',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL,
  '65951d20-c0ce-439a-8162-1403a489d161', '1b17c3f2-155e-40ab-b620-2a43a22d79d7', '2018-01-22', '2018-01-22 15:33:01',
  NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
              'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
              '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004438, 'f1996b17-4f26-407c-9075-57091ce1afd2', 0.00, 4.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_OTHER_FEE', '10000.05', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL,
                                                                               '240bb533-c453-49f5-a1f7-8686dd6e0679',
                                                                               NULL,
                                                                               '25bc3b9b-c643-4e0f-abb2-10304bbc93a8',
                                                                               '2018-01-22', '2018-01-22 15:33:01',
                                                                               NULL, NULL, 12483592,
                                                                                           '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                           '2018-01-22 00:00:00', NULL,
                                                                                           'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                           '11', 1,
                                                                                           'ZC152840079217958912',
                                                                                           '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                           '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004439, '240bb533-c453-49f5-a1f7-8686dd6e0679', 4.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE', '20000.01.05',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL,
  'f1996b17-4f26-407c-9075-57091ce1afd2', '25bc3b9b-c643-4e0f-abb2-10304bbc93a8', '2018-01-22', '2018-01-22 15:33:01',
  NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
              'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
              '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004440, '49ef5bc2-185e-4a0c-80c8-f81f83dbab7a', 0.00, 3000.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_PRINCIPLE', '10000.02', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL,
                                                                               'b0501733-4779-46b3-8ca5-bf7b504ffccf',
                                                                               NULL,
                                                                               '4bf3940a-cf68-4d17-8f4e-5f28fc3cccac',
                                                                               '2018-01-22', '2018-01-22 15:33:01',
                                                                               NULL, NULL, 12483592,
                                                                                           '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                           '2018-01-22 00:00:00', NULL,
                                                                                           'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                           '11', 1,
                                                                                           'ZC152840079217958912',
                                                                                           '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                           '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004441, 'b0501733-4779-46b3-8ca5-bf7b504ffccf', 3000.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL,
  '49ef5bc2-185e-4a0c-80c8-f81f83dbab7a', '4bf3940a-cf68-4d17-8f4e-5f28fc3cccac', '2018-01-22', '2018-01-22 15:33:01',
  NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
              'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
              '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004442, '97c807ba-d53f-4887-b4e9-8232291ae392', 0.00, 3.00, 'FST_UNEARNED_LOAN_ASSET', '10000', 1,
                  'SND_UNEARNED_LOAN_ASSET_TECH_FEE', '10000.04', NULL, NULL, '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                              'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                              NULL,
                                                                              '793a3226-7e0e-42e2-9a9a-d24bf7a9228b',
                                                                              NULL,
                                                                              '3a171b43-35a4-4abb-a71c-ac58a8ca2233',
                                                                              '2018-01-22', '2018-01-22 15:33:01', NULL,
                                                                              NULL, 12483592,
                                                                                    '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                    '2018-01-22 00:00:00', NULL,
                                                                                    'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                    '11', 1, 'ZC152840079217958912',
                                                                                    '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                    '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004443, '793a3226-7e0e-42e2-9a9a-d24bf7a9228b', 3.00, 0.00, 'FST_RECIEVABLE_ASSET', '20000', 1,
                  'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL, NULL,
  '97c807ba-d53f-4887-b4e9-8232291ae392', '3a171b43-35a4-4abb-a71c-ac58a8ca2233', '2018-01-22', '2018-01-22 15:33:01',
  NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
              'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
              '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004444, '709d673c-06cc-4070-9898-89d0d4120c56', 4.00, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_OTHER_FEE', '100000.02.03',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL,
  '1d7f3dd8-9739-4125-8133-a88b19bd021a', NULL, '56b56536-334e-4d70-ae93-144a7a1b7ae4', '2018-01-22',
  '2018-01-22 15:33:01', NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  (20004445, '1d7f3dd8-9739-4125-8133-a88b19bd021a', 0.00, 4.00, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INCOME_FEE',
             '70000.05', 'TRD_REVENUE_INCOME_LOAN_OTHER_FEE', '70000.05.03', '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                             'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                             NULL, NULL,
                                                                             '709d673c-06cc-4070-9898-89d0d4120c56',
                                                                             '56b56536-334e-4d70-ae93-144a7a1b7ae4',
                                                                             '2018-01-22', '2018-01-22 15:33:01', NULL,
                                                                             NULL, 12483592,
                                                                                   '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                   '2018-01-22 00:00:00', NULL,
                                                                                   'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                   '11', 1, 'ZC152840079217958912',
                                                                                   '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                   '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004446, 'de9a694a-d5ca-4311-bae8-25db22a655a5', 2.00, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_SERVICE_FEE', '100000.02.01',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL,
  '7441b4f0-529c-4b29-ba23-2d7363c5c75d', NULL, '7a8fe918-3bbb-4865-b141-49d188e6d64d', '2018-01-22',
  '2018-01-22 15:33:01', NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  (20004447, '7441b4f0-529c-4b29-ba23-2d7363c5c75d', 0.00, 2.00, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INCOME_FEE',
             '70000.05', 'TRD_REVENUE_INCOME_LOAN_SERVICE_FEE', '70000.05.01', '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                               'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                               NULL, NULL,
                                                                               'de9a694a-d5ca-4311-bae8-25db22a655a5',
                                                                               '7a8fe918-3bbb-4865-b141-49d188e6d64d',
                                                                               '2018-01-22', '2018-01-22 15:33:01',
                                                                               NULL, NULL, 12483592,
                                                                                           '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                           '2018-01-22 00:00:00', NULL,
                                                                                           'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                           '11', 1,
                                                                                           'ZC152840079217958912',
                                                                                           '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                           '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES (20004448, 'da0af406-e514-4fd3-b91b-ebdc35796ba2', 3.00, 0.00, 'FST_DEFERRED_INCOME', '100000', 0,
                  'SND_DEFERRED_INCOME_FEE', '100000.02', 'TRD_DEFERRED_INCOME_LOAN_TECH_FEE', '100000.02.02',
  '9714eb42-6c52-467b-8ade-7e71d8f58d0b', 'a64c257a-8c41-44d4-9f35-b933028401b6', NULL,
  '1c6d2970-e668-4f33-b65f-b6cd78d87644', NULL, '45b7bf9b-0549-47a1-a3e2-96a3ee9b5da0', '2018-01-22',
  '2018-01-22 15:33:01', NULL, NULL, 12483592, '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', '2018-01-22 00:00:00', NULL,
                                     'a0ea9406-eee9-478d-9a99-445058e7ecc9', '11', 1, 'ZC152840079217958912',
                                     '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '', NULL, NULL, NULL, NULL);
INSERT INTO ledger_book_shelf (id, ledger_uuid, debit_balance, credit_balance, first_account_name, first_account_uuid, account_side, second_account_name, second_account_uuid, third_account_name, third_account_uuid, first_party_id, second_party_id, third_party_id, forward_ledger_uuid, backward_ledger_uuid, batch_serial_uuid, amortized_date, book_in_date, business_voucher_uuid, carried_over_date, contract_id, contract_uuid, default_date, journal_voucher_uuid, ledger_book_no, ledger_book_owner_id, life_cycle, related_lv_1_asset_outer_idenity, related_lv_1_asset_uuid, related_lv_2_asset_outer_idenity, related_lv_2_asset_uuid, related_lv_3_asset_outer_idenity, related_lv_3_asset_uuid, source_document_uuid)
VALUES
  (20004449, '1c6d2970-e668-4f33-b65f-b6cd78d87644', 0.00, 3.00, 'FST_REVENUE', '70000', 0, 'SND_REVENUE_INCOME_FEE',
             '70000.05', 'TRD_REVENUE_INCOME_LOAN_TECH_FEE', '70000.05.02', '9714eb42-6c52-467b-8ade-7e71d8f58d0b',
                                                                            'a64c257a-8c41-44d4-9f35-b933028401b6',
                                                                            NULL, NULL,
                                                                            'da0af406-e514-4fd3-b91b-ebdc35796ba2',
                                                                            '45b7bf9b-0549-47a1-a3e2-96a3ee9b5da0',
                                                                            '2018-01-22', '2018-01-22 15:33:01', NULL,
                                                                            NULL, 12483592,
                                                                                  '54978e11-43c7-4dae-9f7e-ef6d6c2dced1',
                                                                                  '2018-01-22 00:00:00', NULL,
                                                                                  'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                  '11', 1, 'ZC152840079217958912',
                                                                                  '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                  '', NULL, NULL, NULL, NULL);

INSERT INTO financial_contract (id, asset_package_format, adva_matuterm, adva_start_date, contract_no, contract_name, app_id, company_id, adva_repo_term, thru_date, capital_account_id, financial_contract_type, loan_overdue_start_day,
                                loan_overdue_end_day, payment_channel_id, ledger_book_no, financial_contract_uuid, sys_normal_deduct_flag,
                                sys_overdue_deduct_flag, sys_create_penalty_flag, sys_create_guarantee_flag, unusual_modify_flag,
                                sys_create_statement_flag, transaction_limit_per_transcation, transaction_limit_per_day,
                                remittance_strategy_mode, app_account_uuids, allow_online_repayment, allow_offline_repayment,
                                allow_advance_deduct_flag, adva_repayment_term, penalty, overdue_default_fee, overdue_service_fee,
                                overdue_other_fee, create_time, last_modified_time, repurchase_approach, repurchase_rule,
                                repurchase_algorithm, day_of_month, pay_for_go, repurchase_principal_algorithm,
                                repurchase_interest_algorithm, repurchase_penalty_algorithm, repurchase_other_charges_algorithm,
                                repayment_check_days, repurchase_cycle, days_of_cycle, temporary_repurchases,
                                allow_freewheeling_repayment, capital_party, other_party, contract_short_name, financial_type,
                                remittance_object) VALUES
  (100125, 0, 0, '2015-01-01 00:00:00', 'WB123', 'WB123', 9, 11, 6, '2019-06-01 00:00:00', 340, 0, 5, 5, NULL,
                                                                                                'a0ea9406-eee9-478d-9a99-445058e7ecc9',
                                                                                                'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                                1, 1, 1, 0, 0, 1, NULL,
                                                                                                            NULL, 0,
                                                                                                            'null', 1,
                                                                                                            1, 1, 4, '',
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     NULL,
                                                                                                                     '2017-12-15 16:03:35',
                                                                                                                     '2018-01-22 14:24:04',
                                                                                                                     2,
                                                                                                                     NULL,
                                                                                                                     '',
                                                                                                                     NULL,
    0, '', '', '', '', -1, NULL, NULL, NULL, 0, '[9]', '[9]', 'WB', 0, 1);

INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563812, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-02-22', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'fc7bd626-37a5-4b47-8f84-4780543f3098',
                                                                                      '2018-01-22 15:32:31',
                                                                                      '2018-01-22 15:33:00', NULL,
                                                                                      'ZC152839956321181696', 12483592,
                                                                                      NULL, 1, 0, NULL, 1, 2, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            '435376352014b9784a616433cc1fb3d4',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 15:32:31', '2018-01-22 15:32:31', 0, 0, 0, 0, 0, 0, 0,
        'a64c257a-8c41-44d4-9f35-b933028401b6', '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', NULL,
        '0fd2a78a-a8df-4333-be98-a6bccc0208b2', 0, NULL, NULL);
INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563813, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-03-22', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      'a27f4aa5-f674-4dc2-94be-a71f8af00afa',
                                                                                      '2018-01-22 15:32:31',
                                                                                      '2018-01-22 15:33:00', NULL,
                                                                                      'ZC152839956333764608', 12483592,
                                                                                      NULL, 2, 0, NULL, 1, 2, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            '8e0b40e3fa8e85ed8b4343caedf77752',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 15:32:31', '2018-01-22 15:32:31', 0, 0, 0, 0, 0, 0, 0,
        'a64c257a-8c41-44d4-9f35-b933028401b6', '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', NULL,
        'd1e4a150-0909-4571-8ae4-c9050cfeedfe', 0, NULL, NULL);
INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563814, 0, 0, 1000.00, 1000.00, 0.00, 1000.00, '2018-04-22', NULL, 0.00, 0, 1, 0, NULL,
                                                                                      '0b2d0831-6796-4777-ba94-a9fd8b1ad319',
                                                                                      '2018-01-22 15:32:31',
                                                                                      '2018-01-22 15:33:00', NULL,
                                                                                      'ZC152839956350541824', 12483592,
                                                                                      NULL, 3, 0, NULL, 1, 2, 0,
                                                                                            'empty_deduct_uuid', NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            '8d4c84c2e330ee3ccf7148dc159872c3',
  '00bfd64b58361d989ac8bf13dccc3c9b', '2018-01-22 15:32:31', '2018-01-22 15:32:31', 0, 0, 0, 0, 0, 0, 0,
        'a64c257a-8c41-44d4-9f35-b933028401b6', '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', NULL,
        '3109d4fa-6743-4e05-9d24-4329ed14f938', 0, NULL, NULL);
INSERT INTO asset_set (id, guarantee_status, settlement_status, asset_fair_value, asset_principal_value, asset_interest_value, asset_initial_value, asset_recycle_date, confirm_recycle_date, refund_amount, asset_status, on_account_status, repayment_plan_type, last_valuation_time, asset_uuid, create_time, last_modified_time, comment, single_loan_contract_no, contract_id, actual_recycle_date, current_period, overdue_status, overdue_date, version_no, active_status, sync_status, active_deduct_application_uuid, repurchase_status, financial_contract_uuid, asset_finger_print, asset_extra_fee_finger_print, asset_finger_print_update_time, asset_extra_fee_finger_print_update_time, plan_type, write_off_reason, can_be_rollbacked, time_interval, deduction_status, executing_status, executing_status_bak, customer_uuid, contract_uuid, contract_funding_status, version_lock, order_payment_status, repay_schedule_no, outer_repayment_plan_no)
VALUES (12563815, 0, 0, 3010.00, 3000.00, 1.00, 3001.00, '2018-01-22', NULL, 0.00, 0, 1, 1, '2018-01-22 15:33:01',
                                                                                      '99354aa3-16f5-4e99-81ac-fe9e0d6987d0',
                                                                                      '2018-01-22 15:33:00',
                                                                                      '2018-01-22 15:33:01', '1',
                                                                                      'ZC152840079217958912', 12483592,
                                                                                      NULL, 1, 0, NULL, -1870907127, 0,
                                                                                            0,
                                                                                            'dabc0a32-db59-40e6-8ee4-a774f892ba76',
                                                                                            NULL,
                                                                                            'c420576a-4d5b-4d45-b880-2fd5508cc2db',
                                                                                            NULL, NULL,
                                                                                                  '2018-01-22 15:33:00',
                                                                                                  '2018-01-22 15:33:00',
                                                                                                  1, 0, 1, 1, 2, 1, 0,
        'a64c257a-8c41-44d4-9f35-b933028401b6', '54978e11-43c7-4dae-9f7e-ef6d6c2dced1', NULL,
        '97f5548b-4810-4964-812d-e5d3aa438f53', 0, NULL, '');

INSERT INTO contract_account (id, pay_ac_no, bankcard_type, contract_id, payer_name, bank, bind_id, id_card_num, bank_code, province, province_code, city, city_code, standard_bank_code, from_date, thru_date, virtual_account_uuid, bank_card_status, contract_account_uuid, contract_account_type)
VALUES (279841, '6214855712106520', NULL, 12483592, 'WUBO', '邮政储蓄 ', NULL, '320301198502169142', NULL, '北京市', '110000',
        '北京市', '110100', 'C10403', '2018-01-22 15:32:31', '2900-01-01 00:00:00', NULL, 1,
        'ee1686159cb84b89b280f11566fa0951', 0);

INSERT INTO t_remittance_application (id, remittance_application_uuid, request_no, financial_contract_uuid, financial_contract_id, financial_product_code, contract_unique_id, contract_no, planned_total_amount, actual_total_amount, auditor_name, audit_time, notify_url, plan_notify_number, actual_notify_number, remittance_strategy, remark, transaction_recipient, execution_status, execution_remark, create_time, creator_name, ip, last_modified_time, opposite_receive_date, remittance_id, total_count, actual_count, version_lock, check_request_no, check_status, check_retry_number, check_send_time, notify_status, int_field_1, int_field_2, int_field_3, string_field_1, string_field_2, string_field_3, decimal_field_1, decimal_field_2, decimal_field_3)
VALUES (238692, 'e0afd103-dbb3-4b09-9f7e-dc1bdd3a5b70', 'a533469d-3600-466c-a298-23338a88777f',
                'c420576a-4d5b-4d45-b880-2fd5508cc2db', 100125, 'WB123', 'acc38ad1-e20b-4c6a-873f-57d71ba1de05',
                'acc38ad1-e20b-4c6a-873f-57d71ba1de05', 3000.00, 3000.00, 'auditorName1', '2016-08-20 00:00:00',
                                                                                          'http://101.52.128.166/Loan/PaidNotic',
                                                                                          3, 2, 0, '交易备注', 1, 2, NULL,
                                                                                          '2018-01-22 15:30:09',
  't_test_zfb', '115.197.186.160, 120.26.102.180', '2018-01-22 15:32:11', '2018-01-22 15:30:15', NULL, 1, 1,
  'd0c57ea1-b22b-495f-8bb3-24d200b90a1e', 'e65864db-cf1b-46ec-a3a0-c3159a967eac', 4, 3, '2018-01-22 15:30:09', 1, NULL,
                                                                                     NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL);

INSERT INTO customer (id, account, mobile, name, source, app_id, customer_uuid, customer_type, customer_style, status, id_type)
VALUES (271667, '320301198502169142', NULL, 'WUBO', 'f3c67b4d-78f7-4db0-8be4-ca587c21ef54', 9,
                'a64c257a-8c41-44d4-9f35-b933028401b6', 0, 0, 0, 0);


INSERT INTO asset_set_extra_charge (id, asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES (955064, '9b91a293-5233-451e-879e-c59940ece49e', '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '2018-01-22 15:33:00',
                '2018-01-22 15:33:00', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE',
                '10000.05', NULL, NULL, 4.00);
INSERT INTO asset_set_extra_charge (id, asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES (955065, 'b0d8c611-2206-4ac0-aae5-b401e19f35db', '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '2018-01-22 15:33:00',
                '2018-01-22 15:33:00', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE',
                '10000.03', NULL, NULL, 2.00);
INSERT INTO asset_set_extra_charge (id, asset_set_extra_charge_uuid, asset_set_uuid, create_time, last_modify_time, first_account_name, first_account_uuid, second_account_name, second_account_uuid, third_account_name, third_account_uuid, account_amount)
VALUES (955066, 'a4f02bad-37ca-46b1-a577-a61ce2dbd033', '99354aa3-16f5-4e99-81ac-fe9e0d6987d0', '2018-01-22 15:33:00',
                '2018-01-22 15:33:00', 'FST_UNEARNED_LOAN_ASSET', '10000', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE',
                '10000.04', NULL, NULL, 3.00);