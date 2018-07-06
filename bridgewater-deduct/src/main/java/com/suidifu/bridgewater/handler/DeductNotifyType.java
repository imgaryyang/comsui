package com.suidifu.bridgewater.handler;

public enum DeductNotifyType {
	COMMON("enum.remittance.notify.type-COMMON")
	
	;
	
	private String key;
	
	private DeductNotifyType(String key) {
		this.key=key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public static DeductNotifyType fromKey(String key){

        for(DeductNotifyType item : DeductNotifyType.values()){

            if(key.equals(item.getKey())){

                return item;
            }
        }
        return null;
    }
}
