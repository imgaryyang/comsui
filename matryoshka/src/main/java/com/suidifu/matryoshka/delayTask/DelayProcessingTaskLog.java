package com.suidifu.matryoshka.delayTask;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 后置处理任务（已完成的历史任务）
 * Created by louguanyang on 2017/5/3.
 */
@Entity
@Table(name = "t_delay_processing_task_log")
public class DelayProcessingTaskLog extends BaseDelayProcessingTask {

    @Id
    @GeneratedValue
    private Long id;

    public DelayProcessingTaskLog() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
