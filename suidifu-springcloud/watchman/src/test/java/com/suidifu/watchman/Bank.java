package com.suidifu.watchman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author louguanyang at 2018/1/9 10:49
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@org.jetbrains.annotations.TestOnly
public class Bank implements Serializable {

    private static final long serialVersionUID = -4153927533288080102L;
    @Id
    @GeneratedValue
    private Long id;

    private String bankCode;

    private String bankName;

}
