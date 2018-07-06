package com.suidifu.morganstanley.handler.impl;

import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.BatchProcessHandler;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;

public abstract class BaseBatchProcessHandlerImpl implements BatchProcessHandler {

    public void noNeedSend(FileRepository fileRepository) throws MorganStanleyException {
        if (fileRepository == null) {
            throw new MorganStanleyException("fileRepository is null.");
        }
        if (!fileRepository.isNeedSend()) {
            throw new MorganStanleyException("fileRepository 非(已处理, 未发送)的, 跳过发送.");
        }
    }
}

