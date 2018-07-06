package com.suidifu.bridgewater.controller.api.remittance;

import com.suidifu.bridgewater.api.model.RemittanceAuditbillDownloadModel;
import com.suidifu.bridgewater.controller.BaseApiController;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.earth.v3.DownloadOpsFunctionCodes;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.service.FinancialContractService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/api/download")
public class DownloadApiRemittanceController extends BaseApiController {

	private static final Log logger = LogFactory
			.getLog(DownloadApiRemittanceController.class);

	@Value("#{config['remittance.auditbill.filePath']}")
	private String AUDIT_BILL_FILE_PATH = "";
	@Autowired
	private FinancialContractService financialContractService;

	/**
	 * 放款对账单下载
	 */
	@RequestMapping(value = "", params = {ApiConstant.PARAMS_FN_KEY_WITH_COMBINATORS
			+ DownloadOpsFunctionCodes.DOWNLOAD_REMITTANCE_AUDIT_BILL}, method = RequestMethod.POST)
	public @ResponseBody
	String downloadRemittanceAuditBill(
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute RemittanceAuditbillDownloadModel downloadModel) {

		try {
			if (!downloadModel.isValid()) {
				return signErrorResult(response,
						ApiResponseCode.INVALID_PARAMS,
						downloadModel.getCheckFailedMsg());
			}

			FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(downloadModel.getProductCode());
			if (financialContract == null) {
				throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
			}

			String base64FileStream = getBase64FileStream(downloadModel);
			return signSucResult(response, "base64FileStream", base64FileStream);

		} catch (Exception e) {
			e.printStackTrace();
			return signErrorResult(response, e);
		}
	}

	private String getBase64FileStream(
			RemittanceAuditbillDownloadModel downloadModel) {

		try {
			String filePath = AUDIT_BILL_FILE_PATH + downloadModel.getProductCode() + "_" + downloadModel.getSettleDate() + ".zip";

			File file = new File(filePath);
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			return new BASE64Encoder().encode(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApiException(ApiResponseCode.REMITTANCE_AUDIT_BILL_DOWNLOAD_FAIL);
		}

	}


	public static void main(String[] args) {
		String filePath = "/Users/apple/Downloads/TEST001_2017-09-16.zip";

		File file = new File(filePath);
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(file);

			byte[] buffer = new byte[(int) file.length()];
			try {
				inputFile.read(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				inputFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String encode = new BASE64Encoder().encode(buffer);
			System.out.println(encode);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
