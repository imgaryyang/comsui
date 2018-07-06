package com.suidifu.hathaway;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.suidifu.hathaway.job.JobFactoryTest;
import com.suidifu.hathaway.job.handler.JobStageHandlerTest;
import com.suidifu.hathaway.job.service.JobServiceTest;
import com.suidifu.hathaway.job.service.StageTaskHandlerTest;
import com.suidifu.hathaway.redis.service.GenericRedisServiceTest;
import com.suidifu.hathaway.task.service.TaskServiceTest;
import com.suidifu.hathaway.util.ReflectionUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({
	
	JobFactoryTest.class,
	JobStageHandlerTest.class,
	JobServiceTest.class,
	StageTaskHandlerTest.class,
	GenericRedisServiceTest.class,
	TaskServiceTest.class,
	ReflectionUtilsTest.class,
	
	
	
})
public class AllTestOfWukai {

}
