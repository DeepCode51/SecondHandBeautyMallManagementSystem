package com.xjr.mzmall.vo.optionRecords;

import lombok.Data;

import java.util.List;

@Data
public class SeriesChild {
    private String name;
    private String type;
    private List<Integer> data;
}
