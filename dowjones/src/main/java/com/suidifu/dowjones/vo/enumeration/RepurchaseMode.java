package com.suidifu.dowjones.vo.enumeration;

public enum RepurchaseMode {
    M1,    //不包含已回购但包含回购中的合同
    M2,    //不包含已回购、回购中的合同
    M3    //包含已回购、回购中的合同
}