package com.zufangbao.earth.web.controller.reapymentInfo;

import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.RepaymentInformationApiHandler;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author 作者 zhenghangbo
* @version 创建时间：Oct 1, 2016 8:47:28 PM
* 类说明
*/

@Controller
@RequestMapping("modifyContractAccount")
public class ModifyRepaymentInfoController extends BaseController{

	@Autowired
	private ContractAccountService contractAccountService;

	@Autowired
	private ProvinceService  provinceService;
	@Autowired
	private ContractService  contractService;

	@Autowired
	private CityService      cityService;

	@Autowired
	private RepaymentInformationApiHandler repaymentInformationApiHandler;

	@Autowired
	private SignUpHandler signUpHandler;





	@RequestMapping(value ="/repaymentInfo/modify")
	public @ResponseBody  String modifyReapymentInfo(@Secure Principal principal, HttpServletRequest request, ModifyRepaymentInfoRequestModel requestModel){

		try {
			if(!requestModel.checkData()){
				return  jsonViewResolver.errorJsonResult("数据错误！！");
			}

			Map<String, Province> provincesMap = provinceService.getCacheProvinces();
            Map<String, City> citysMap = cityService.getCacheCitys();
            Province  province = provincesMap.get(requestModel.getProvinceCode());
			City  city =  citysMap.get(requestModel.getCityCode());
			if(city ==null || province ==null){
				return jsonViewResolver.errorJsonResult("系统错误，变更还款信息失败!!!");
			}
			Contract contract = contractService.load(Contract.class, requestModel.getContractId());
			ContractAccount vaildContractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);

			Customer customer = contract.getCustomer();

			String bankName = StringUtils.EMPTY;

			//后期优化
			if(!signUpHandler.judgeZhongHangByfinancialContractCode(contract.getFinancialContractUuid())){
				bankName = BankCoreCodeMapSpec.coreBankMap.get(requestModel.getBankCode());
			}else{
				bankName = ZhonghangResponseMapSpec.ENERGENCY_BANK_NAME_FOR_SHORT.get(requestModel.getBankCode());
			}

			//同步进行签约
			repaymentInformationApiHandler.modifyRepaymentInfoByRule(requestModel.getPayerName(), requestModel.getBankCode(), requestModel.getBankAccount(),
					bankName, province.getCode(), city.getCode(), vaildContractAccount,
                    IpUtil.getIpAddress(request), principal, requestModel.getIdCardNum(), customer, requestModel.getMobile());
            return jsonViewResolver.sucJsonResult();

		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误，变更还款信息失败!!!");
		}

	}



}
