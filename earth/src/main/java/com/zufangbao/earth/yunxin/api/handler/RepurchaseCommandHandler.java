package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.command.RepurchaseCommandModel;
import com.zufangbao.sun.entity.repurchase.RepurchaseApiResponse;

public interface RepurchaseCommandHandler {

    RepurchaseApiResponse batchRepurchaseCommand(RepurchaseCommandModel model, String ip);

    RepurchaseApiResponse undoRepurchaseCommand(RepurchaseCommandModel model, String ip);
}
