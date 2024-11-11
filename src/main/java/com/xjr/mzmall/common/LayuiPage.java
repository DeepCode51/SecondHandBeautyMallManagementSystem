package com.xjr.mzmall.common;

import lombok.Data;

import java.io.Serializable;

/**
 * layui分页实体类
 */
@Data
public class LayuiPage implements Serializable {
    private static final long serialVersionUID = 1691824905333709356L;
    private Long page;   //当前页数
    private Long limit;  //每页多少条数据
}
