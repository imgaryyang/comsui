package com.zufangbao.earth.yunxin.api.swagger;
/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * Swagger配置
 * <p>
 * @author louguanyang on 2017/3/3.
 */
@EnableSwagger2
@ComponentScan(basePackages = {"com.zufangbao.earth.yunxin.api.controller","com.zufangbao.earth.web.controller.update"})
public class SwaggerConfig {


    public static final int SYSTEM_ERROR = 10001;
    /** 接口不存在 **/
    public static final int API_NOT_FOUND = 10002;
    /** 接口已关闭 **/
    public static final int API_UNAVAILABLE = 10003;
    /** 功能代码未指定或该请求接口下不存在此功能代码！**/
    public static final int INVALID_FN_CODE = 10004;
    /** 验签失败 **/
    public static final int SIGN_VERIFY_FAIL = 10005;
    /** 缺少商户号merId或商户密钥secret **/
    public static final int SIGN_MER_CONFIG_ERROR = 10006;
    /** 系统繁忙，请稍后再试 **/
    public static final int SYSTEM_BUSY = 10007;

    private static final String TEST_MER_ID = "t_test_zfb";
    private static final String TEST_SECRET = "123456";

    @Bean
    public Docket customDocket() {
        Parameter merId = new ParameterBuilder()
                .name("merId")
                .description("接口请求商户编号")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(TEST_MER_ID)
                .required(true)
                .build();
        Parameter secret = new ParameterBuilder()
                .name("secret")
                .description("接口请求商户唯一标识")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(TEST_SECRET)
                .required(true)
                .build();
        Parameter sign = new ParameterBuilder()
                .name("sign")
                .description("请求参数签名")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("")
                .required(false)
                .build();

        List<Parameter> headers = Arrays.asList(merId, secret, sign);
        return new Docket(DocumentationType.SWAGGER_2)
//                .enableUrlTemplating(true)
                .globalOperationParameters(headers)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("五维金融接口文档")
                .description("五维金融接口文档V2.0")
//                .termsOfServiceUrl("http://blog.didispace.com/"SwaggerConfig)
                .contact(new Contact("楼关杨", "http://www.5veda.net", "louguanyang@hzsuidifu.com"))
                .version("2.0")
                .build();
    }

    private Predicate<String> paths() {
        return Predicates.or(
        		PathSelectors.regex("/api/v2.*"),
        		PathSelectors.regex("/pre/api/modify.*"),
        		PathSelectors.regex("/pre/api/update-cache.*"),
        		PathSelectors.regex("/update/.*"));
    }



}
