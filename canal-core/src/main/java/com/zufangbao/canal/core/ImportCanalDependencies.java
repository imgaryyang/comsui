package com.zufangbao.canal.core;

import org.springframework.context.annotation.Import;

import com.zufangbao.canal.core.config.PropertiesConfig;
import com.zufangbao.canal.core.rowprocesser.SimpleRowProcessor;

@Import({ SimpleRowProcessor.class, CanalTask.class, PropertiesConfig.class, CanalClient.class })
public class ImportCanalDependencies {

}
