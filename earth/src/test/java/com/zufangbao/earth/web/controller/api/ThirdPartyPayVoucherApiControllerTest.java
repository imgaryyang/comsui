package com.zufangbao.earth.web.controller.api;

import com.zufangbao.earth.web.controller.thirdPartVoucher.ThirdPartyPayVoucherApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zfj on 17-7-6.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={

        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml"

})
@Transactional
@Rollback(value = false)
//@TransactionConfiguration(defaultRollback = false)
@WebAppConfiguration(value="webapp")
public class ThirdPartyPayVoucherApiControllerTest {
    @Autowired
    ThirdPartyPayVoucherApiController thirdPartyPayVoucherApiController;

    @Test
    public void reUploadTest(){
        System.out.println(thirdPartyPayVoucherApiController.reUpload("111f"));
    }
}
