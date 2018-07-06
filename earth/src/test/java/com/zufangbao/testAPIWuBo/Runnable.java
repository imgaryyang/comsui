package com.zufangbao.testAPIWuBo;

import com.zufangbao.sun.utils.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Cool on 2017/7/18.
 */
public class Runnable {
    TestMethod testMethod =new TestMethod();
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    String uniqueId = "";
    List<String>list = new ArrayList<>();
        public void testRun(){
                new Thread(new java.lang.Runnable() {
                    @Override
                    public void run() {
                        for (int i=0;i<10;i++){
                            String uniqueId = UUID.randomUUID().toString();
                            list.add(uniqueId);
                        }
                        for (int j = 0; j <10 ; j++) {
                            System.out.println(Thread.currentThread().getName()+"--"+j);
                            for (String e:list) {
                                //prepaymentCucumberMethod.makeLoan2("G31700",e,"10000");
                            }
                        }
                        //prepaymentCucumberMethod.importAssetPackage2("100000","10000","G31700",list,"0","","","","");
                    }
                }).start();
        }
}
