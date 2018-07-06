package com.suidifu.bridgewater.controller;

import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.BasicTask;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.task.DynamicBusinessTask;
import com.zufangbao.sun.service.task.DynamicBusinessTaskService;

@RestController
@RequestMapping("/task")
public class TaskController{
	
	private static Log logger = LogFactory.getLog(TaskController.class);
	
	private final static Map<String, BasicTask> TASKS = new HashMap<String, BasicTask>(12);
	
	private final static Map<String, ScheduledFuture<?>> SCHEDULED_FUTURE = new HashMap<String, ScheduledFuture<?>>(16);
	
	private final static int POOL_SIZE = 64;

	private final ConcurrentTaskScheduler ct = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(POOL_SIZE));
	
	@Autowired
	private JsonViewResolver jsonViewResolver;
	
	@Autowired
	private DynamicBusinessTaskService dynamicBusinessTaskService;
	
	/**
	 * 查看任务配置文件
	 */
	@RequestMapping(value = "show")
	public String show(HttpServletResponse response) {
		try {
			List<DynamicBusinessTask> tasks = dynamicBusinessTaskService.getDynamicBusinessTaskListByServerGroup(DynamicBusinessTask.SERVER_GROUP_REMITTANCE);
			return jsonViewResolver.sucJsonResult("tasks", tasks);
		} catch (Exception e) {
			logger.error("读取任务配置异常！");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("读取任务配置异常！");
		}
	}
	
	/**
	 * 加载任务配置文件，启动任务
	 */
	@RequestMapping(value = "startup")
	public String startup(HttpServletResponse response){
		try {
			String serverHost = IpUtil.getMachineIP();
			logger.info("#task startup, serverHost: " + serverHost);
			
			//防止并发启动
			synchronized(this) {
				ScheduledThreadPoolExecutor executor =  (ScheduledThreadPoolExecutor) ct.getConcurrentExecutor();
				
//				if(MapUtils.isNotEmpty(SCHEDULED_FUTURE) || CollectionUtils.isNotEmpty(executor.getQueue())) {
//					return jsonViewResolver.errorJsonResult("任务启动失败，请确认是否存在运行中的任务！");
//				}
				
				if(MapUtils.isNotEmpty(SCHEDULED_FUTURE)){
					return jsonViewResolver.errorJsonResult("任务启动失败，请确认是否存在运行中的任务！");
				}
				for (Runnable runnableTask : executor.getQueue()) {
					ScheduledFuture<?> scheduledFuture = (ScheduledFuture<?>)runnableTask;
					if(!scheduledFuture.isCancelled()){
						return jsonViewResolver.errorJsonResult("任务启动失败，请确认是否存在运行中的任务！");
					}
				}
				
				List<DynamicBusinessTask> dbTasks = dynamicBusinessTaskService.getDynamicBusinessTaskListByServerGroup(DynamicBusinessTask.SERVER_GROUP_REMITTANCE);
				
				long invalid = dbTasks.stream().filter(task -> !task.isValid()).count();
				if(invalid > 0) {
					return jsonViewResolver.errorJsonResult("任务启动失败，请检查任务配置！");
				}
  				Map<String, String> taskStatus = new HashMap<String, String>();
				for (DynamicBusinessTask dbTask : dbTasks) {
					//跳过未启用的任务
					if(!dbTask.isEnabled()) {
						continue;
					}
					
					//跳过主机地址不一致的任务
					if(!StringUtils.equals(serverHost, dbTask.getServerHost())) {
						continue;
					}
					
					BasicTask runnableTask = BasicTask.convertToRunnableTask(dbTask);
					ScheduledFuture<?> scheduledFuture;
					if(runnableTask.getPeriodicTrigger() != 0L) {
						scheduledFuture = ct.schedule(runnableTask, new PeriodicTrigger(runnableTask.getPeriodicTrigger()));  
					}else {
						scheduledFuture = ct.schedule(runnableTask, new CronTrigger(runnableTask.getCronExpression()));
					}
					SCHEDULED_FUTURE.put(runnableTask.getTaskId(), scheduledFuture);  
					TASKS.put(runnableTask.getTaskId(), runnableTask);  
					taskStatus.put(runnableTask.getTaskId(), "启动成功！");
				}
				logger.info("queue:" + JsonUtils.toJsonString(executor.getQueue()));
				return jsonViewResolver.sucJsonResult("taskStatus", taskStatus);
			}
		} catch (Exception e) {
			logger.error("任务启动异常［"+e.getMessage()+"］");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("任务启动异常［"+e.getMessage()+"］");
		}
	}
	
	/**
	 * 停止所有任务
	 */
	@RequestMapping(value = "stop-all")
	public String stopAll(HttpServletResponse response) {
		try {
			String[] taskIds = new String[SCHEDULED_FUTURE.size()];
			SCHEDULED_FUTURE.keySet().toArray(taskIds);

			Map<String, String> taskStatus = new HashMap<String, String>();
			for (String taskId : taskIds) {
				if(!removeTask(taskId)) {
					return jsonViewResolver.errorJsonResult("#停止所有任务，taskId［"+taskId+"］移除失败！");
				}
				logger.info("#停止所有任务，taskId［"+ taskId +"］移除成功");
				taskStatus.put(taskId, "移除成功！");
			}
			ScheduledThreadPoolExecutor executor =  (ScheduledThreadPoolExecutor) ct.getConcurrentExecutor();
			BlockingQueue<Runnable> taskQueue = executor.getQueue();
			if(CollectionUtils.isNotEmpty(taskQueue)) {
				for (Runnable runnableTask : taskQueue) {
					ScheduledFuture<?> scheduledFuture = (ScheduledFuture<?>)runnableTask;
					if (!scheduledFuture.isCancelled()) {
						scheduledFuture.cancel(true);
					}
				}
			}
			logger.info("queue:" + JsonUtils.toJsonString(executor.getQueue()));
			return jsonViewResolver.sucJsonResult("taskStatus", taskStatus);
		} catch (Exception e) {
			logger.error("#停止所有任务，发生异常" + e.getMessage());
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("#停止所有任务，发生异常" + e.getMessage());
		}
	}
	
	/**
	 * 停止指定任务
	 */
	@RequestMapping(value = "stop")
	public String stop(String taskId, HttpServletResponse response) {
		try {
			if (StringUtils.isBlank(taskId) || !removeTask(taskId)) {
				return jsonViewResolver.errorJsonResult("#停止指定任务，taskId［"+taskId+"］移除失败！");
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#停止指定任务，taskId［"+taskId+"］发生异常" + e.getMessage());
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("#停止指定任务，taskId［"+taskId+"］发生移除异常" + e.getMessage());
		}
	}
	
	private boolean removeTask(String taskId) {
		BasicTask basicTask = TASKS.remove(taskId);
		ScheduledFuture<?> scheduledFuture = SCHEDULED_FUTURE.remove(taskId);
		if (basicTask == null || scheduledFuture == null) {
			return false;
		} else {
			if (!scheduledFuture.isCancelled()) {
				scheduledFuture.cancel(true);
			}
			return true;
		}
	}

	/**
	 * 展示当前任务状态
	 * @param response
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletResponse response) {
		try {
			Map<String, Object> taskWorkStatus = getTaskWorkStaus();
			return jsonViewResolver.sucJsonResult("taskWorkStatus", taskWorkStatus, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("查询任务运行状态异常！");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询任务运行状态异常！");
		}
	}

	private Map<String, Object> getTaskWorkStaus() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("taskInfos", TASKS);
		result.put("taskStatus", SCHEDULED_FUTURE);
		ScheduledThreadPoolExecutor executor =  (ScheduledThreadPoolExecutor) ct.getConcurrentExecutor();
		result.put("taskQueue", executor.getQueue());
		return result;
	}

}
