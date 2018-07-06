DELETE FROM loan_batch
WHERE id = (SELECT loan_batch_id
            FROM asset_package
            WHERE contract_id = (SELECT id
                                 FROM contract
                                 WHERE unique_id = 'FANT8888'));
DELETE FROM rent_order
WHERE asset_set_id IN (SELECT id
                       FROM asset_set
                       WHERE contract_id = (SELECT id
                                            FROM contract
                                            WHERE unique_id = 'FANT8888'));
DELETE FROM asset_set
WHERE contract_id = (SELECT id
                     FROM contract
                     WHERE unique_id = 'FANT8888');
DELETE FROM asset_package
WHERE contract_id = (SELECT id
                     FROM contract
                     WHERE unique_id = 'FANT8888');
DELETE FROM contract_account
WHERE contract_id = (SELECT id
                     FROM contract
                     WHERE unique_id = 'FANT8888');
DELETE FROM customer
WHERE id = (SELECT customer_id
            FROM contract
            WHERE unique_id = 'FANT8888');
DELETE FROM house
WHERE id = (SELECT house_id
            FROM contract
            WHERE unique_id = 'FANT8888');
DELETE FROM ledger_book_shelf
WHERE contract_uuid = (SELECT uuid
                       FROM contract
                       WHERE unique_id = 'FANT8888');
DELETE FROM t_remittance_application_detail
WHERE remittance_application_uuid IN (SELECT remittance_application_uuid
                                      FROM t_remittance_application
                                      WHERE contract_unique_id = 'FANT8888');
DELETE FROM t_remittance_plan
WHERE remittance_application_uuid IN (SELECT remittance_application_uuid
                                      FROM t_remittance_application
                                      WHERE contract_unique_id = 'FANT8888');
DELETE FROM source_document
WHERE source_document_uuid IN (SELECT source_document_uuid
                               FROM source_document_detail
                               WHERE contract_unique_id = 'FANT8888');
DELETE FROM t_remittance_plan_exec_log
WHERE remittance_application_uuid IN (SELECT remittance_application_uuid
                                      FROM t_remittance_application
                                      WHERE contract_unique_id = 'FANT8888');
DELETE FROM journal_voucher
WHERE source_document_uuid IN (SELECT source_document_uuid
                               FROM source_document_detail
                               WHERE contract_unique_id = 'FANT8888');
DELETE FROM source_document_resource
WHERE source_document_uuid IN (SELECT source_document_uuid
                               FROM source_document_detail
                               WHERE contract_unique_id = 'FANT8888');
DELETE FROM t_voucher
WHERE contract_no = (SELECT contract_no
                     FROM contract
                     WHERE unique_id = 'FANT8888');
DELETE FROM contract
WHERE contract_no = 'FANT8888';
DELETE FROM t_prepayment_application
WHERE unique_id = 'FANT8888';
DELETE FROM t_remittance_application
WHERE contract_unique_id = 'FANT8888';
DELETE FROM t_deduct_application
WHERE contract_unique_id = 'FANT8888';
DELETE FROM t_deduct_application_detail
WHERE contract_unique_id = 'FANT8888';
DELETE FROM t_deduct_plan
WHERE contract_unique_id = 'FANT8888';
DELETE FROM source_document_detail
WHERE contract_unique_id = 'FANT8888';
