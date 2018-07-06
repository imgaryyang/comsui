package com.zufangbao.earth.api.test.post;

import com.demo2do.core.utils.DateUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@ContextConfiguration(locations={
"classpath:/local/applicationContext-*.xml"})
public class ModifyOverDueFeePost extends BaseApiTestPost {

	@Test
	public void testModifyOverDueFeePost(){
		List<Thread> threads = new ArrayList<Thread>();
		for(int i=0;i<1;i++){
			Thread thread = new Thread(new Threadtask(i));
			threads.add(thread);
		}
		threads.forEach(f ->{f.start();try {
			f.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	
	class Threadtask implements Runnable{
		int i;
		Threadtask(int i){
			this.i = i;
		}
		@Override
		public void run() {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("fn", "200005");
//			requestParams.put("financialProductCode","G31700");
			requestParams.put("requestNo", UUID.randomUUID().toString());
			/*requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-01-17\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"test124\",\"repaymentPlanNo\":\"ZC18440915338375168\",\"overDueFeeCalcDate\":\"2017-01-17\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");*/
			//requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST00000500\",\"repaymentPlanNo\":\"ZC626151806080905216\",\"overDueFeeCalcDate\":\"2017-01-22\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");

			requestParams.put("modifyOverDueFeeDetails","[{\"financialProductCode\":\"CS0002\",\"contractUniqueId\":\"FD47\",\"repaymentPlanNo\":\"ZC166188735905148928\",\"repayScheduleNo\":\"\",\"overDueFeeCalcDate\":\""+ DateUtils.format(new Date()) +"\",\"penaltyFee\":10,\"latePenalty\":10,\"lateFee\":10,\"lateOtherCost\":10,\"totalOverdueFee\":40}]");
//			requestParams.put("modifyOverDueFeeDetails", "[{\"financialProductCode\":\"CS0001\",\"contractUniqueId\":\"overWrite13\",\"repaymentPlanNo\":\"ZC132670904422309888\",\"overDueFeeCalcDate\":\"2017-11-27\",\"penaltyFee\":30,\"latePenalty\":30,\"lateFee\":30,\"lateOtherCost\":30,\"totalOverdueFee\":120}]");
			//requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"TEST001\",\"repaymentPlanNo\":\"ZC14096339980193792\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},"
					//+ "{\"contractUniqueId\":\"test124\",\"repaymentPlanNo\":\"ZC18440915338375168\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"zshtest3213456\",\"repaymentPlanNo\":\"ZC14093931451453440\",\"overDueFeeCalcDate\":\"2017-02-06\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");
			
			//requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"wwtest--contract-1003-1003\",\"repaymentPlanNo\":\"ZC843795461589327872\",\"overDueFeeCalcDate\":\"2017-02-08\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"},{\"contractUniqueId\":\"TEST131\",\"repaymentPlanNo\":\"ZC832136444735537152\",\"overDueFeeCalcDate\":\"2017-02-08\",\"penaltyFee\":"+i+",\"latePenalty\":"+i+",\"lateFee\":"+i+",\"lateOtherCost\":"+i+",\"totalOverdueFee\":"+4*i+"}]");
			
			//requestParams.put("modifyOverDueFeeDetails", "[{\"contractUniqueId\":\"TEST002\",\"repaymentPlanNo\":\"ZC14102608024244224\",\"overDueFeeCalcDate\":\"2017-02-07\",\"penaltyFee\":1,\"latePenalty\":2,\"lateFee\":3,\"lateOtherCost\":4,\"totalOverdueFee\":10}]");
			try {
				String sr = PostTestUtil.sendPost(MODIFY_OVERDUE_FEE, requestParams, getIdentityInfoMap(requestParams));
				System.out.println(sr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

