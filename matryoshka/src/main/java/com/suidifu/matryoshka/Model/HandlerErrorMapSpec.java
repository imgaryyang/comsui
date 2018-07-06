package com.suidifu.matryoshka.Model;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
public class HandlerErrorMapSpec {


   public class ErrorCode{


       /** 成功 **/
       public static final int SUCCESS = 0;

       public static final int SYSTEM_ERROR = 10001;

       public static final int API_NOT_FOUND = 10002;
       /** 无效参数 **/
       public static final int INVALID_PARAMS = 20001;

   }

}
