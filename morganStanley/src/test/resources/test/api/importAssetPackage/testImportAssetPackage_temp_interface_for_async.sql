SET FOREIGN_KEY_CHECKS=0;

DELETE FROM t_api_config WHERE  api_url='/api/v3/asyncImportAssetPackage';

-- 异步导入资产包临时调用接口
INSERT INTO t_api_config (api_url, fn_code, description, api_status) VALUES ('/api/v3/asyncImportAssetPackage', null, '异步导入资产包(临时接口)', 1);

SET FOREIGN_KEY_CHECKS=1;