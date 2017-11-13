package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_system_config")
@SuppressWarnings("serial")
public class SystemConfig extends BaseModel {

    /**
     * 配置编号
     */
    @TableField(value = "code_")
    private String code;

    /**
     * 参数值
     */
    @TableField(value = "value_")
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
