/**
 * 
 */
package com.zufangbao.earth.cucumber.demo.steps;

import org.springframework.stereotype.Component;

/**
 * @author wukai
 *
 */
@Component("timeHandler")
public class TimeHandlerImpl implements TimeHandler {

	/* (non-Javadoc)
	 * @see com.zufangbao.earth.cucumber.demo.steps.TimeHandler#getSystemTimeMillis()
	 */
	@Override
	public long getSystemTimeMillis() {
		// TODO Auto-generated method stub
		return System.currentTimeMillis();
	}

}
