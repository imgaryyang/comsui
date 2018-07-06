/**
 *
 */
package com.suidifu.bridgewater.notify.server;

import java.util.Map;

/**
 * @author wsh
 */
public interface ConfigHandler {

	String getNotifyGroupNameBy(String finanContractUuid);

	Map<String, Integer> getNotifyServerGroupConfig();

	Map<String, Integer> getNotifyServerGroupConfigForMq();

}
