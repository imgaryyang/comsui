package com.suidifu.barclays.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.barclays.factory.JobFactory;
import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.yunxin.entity.barclays.ScheduleJob;
import com.zufangbao.sun.yunxin.service.barclays.ScheduleJobService;

@Controller
@RequestMapping("/regain")
public class ReExecutionController {

	@Autowired
	ScheduleJobService scheduleJobService;

	@RequestMapping(value = "execution", method = RequestMethod.POST)
	public @ResponseBody String reExecution(@RequestParam(value = "workerUuid") String workerUuid,
			@RequestParam(value = "daysBefore", required = false) String daysBefore,
			@RequestParam(value = "pageNum", required = false) String pageNum) {

		try {
			
			if (StringUtils.isEmpty(workerUuid)) {
				return "workerUuid 不能为空!";
			}
			
			ScheduleJob scheduleJob = scheduleJobService.getScheduleJobByWorkerUuid(workerUuid);
			if (scheduleJob == null) {
				return "scheduleJob is null please check workerUuid!";
			}
			
			JobHandler jobHandler = JobFactory.newInstance(scheduleJob.getJobGroup());
			if (jobHandler == null) {
				return "jobHandler is null!";
			}
			
			Map<String, Object> workingParms = new HashMap<>();
			if (StringUtils.isNotEmpty(daysBefore)) {
				workingParms.put("daysBefore", daysBefore);
			}
			if (StringUtils.isNotEmpty(pageNum)) {
				workingParms.put("pageNum", pageNum);
			}
			workingParms.put("workerUuid", workerUuid);
			jobHandler.run(workingParms);
			
			return "执行成功!";

		} catch (Exception e) {
			e.printStackTrace();
			return "系统错误!";
		}

	}
	
}
