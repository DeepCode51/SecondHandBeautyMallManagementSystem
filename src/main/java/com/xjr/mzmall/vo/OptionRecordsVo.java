package com.xjr.mzmall.vo;

import com.xjr.mzmall.vo.optionRecords.*;
import lombok.Data;

import java.util.List;

@Data
public class OptionRecordsVo {
    private Tooltip tooltip;
    private Legend legend;
    private Grid grid;
    private Toolbox toolbox;
    private XAxis xAxis;
    private YAxis yAxis;
    private List<SeriesChild> series;
}
