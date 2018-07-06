package com.suidifu.munichre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 腾讯ABS变更轨迹文件 RPC 请求 Model
 *
 * @author louguanyang at 2018/4/17 15:15
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TencentAbsTrailModel {

    private int fileTypeOrdinal;
    private String assetSetUuid;
    private String contractUuid;
    private String[] validFinancialContractNos;
    private String lastModifiedTime;

    public Object[] convert2Params(String fcUuidList) {
        try {
            Object[] params = new Object[5];
            params[0] = this.getFileTypeOrdinal();
            params[1] = this.getAssetSetUuid();
            params[2] = this.getContractUuid();
            params[3] = fcUuidList.split(",");
            params[4] = this.getLastModifiedTime();
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0];
        }
    }
}
