package com.xjr.mzmall.vo.optionRecords;

import lombok.Data;

import java.util.List;

@Data
public class XAxis {
    private String type;
    private Boolean boundaryGap;
    private List<String> data;
}
