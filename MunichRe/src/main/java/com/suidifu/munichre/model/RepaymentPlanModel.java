package com.suidifu.munichre.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * asset_set 表 对应实体
 *
 * @author louguanyang at 2018/4/17 15:34
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentPlanModel {

    private int overdueStatus = -1;
    private int executingStatus = -1;
    private BigDecimal assetFairValue = BigDecimal.ZERO;
    private String assetSetUuid = "";
    private String contractUuid = "";
    private String lastModifiedTime = "";
    private int assetStatus = -1;
    private int activeStatus = -1;
    private int versionNo = -1;
    private String contractId = "";
    private int onAccountStatus = -1;
    private String financialContractUuid;

    public TencentAbsTrailModel convert2TencentAbsTrail(String fileType) {
        try {
            TencentAbsTrailModel model = new TencentAbsTrailModel();
            model.setFileTypeOrdinal(Integer.valueOf(fileType));
            model.setAssetSetUuid(this.getAssetSetUuid());
            model.setContractUuid(this.getContractUuid());
            model.setLastModifiedTime(this.getLastModifiedTime());
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
