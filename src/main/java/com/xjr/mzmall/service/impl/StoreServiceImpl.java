package com.xjr.mzmall.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.StoreTopDo;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.Store;
import com.xjr.mzmall.enums.GoodsStatusEnum;
import com.xjr.mzmall.mapper.GoodsMapper;
import com.xjr.mzmall.mapper.StoreMapper;
import com.xjr.mzmall.service.StoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.vo.OptionRecordsVo;
import com.xjr.mzmall.vo.StoreVo;
import com.xjr.mzmall.vo.optionRecords.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result getStoreList(String storeName, LayuiPage layuiPage) {
        Page<Store> storePage = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
        if (!Objects.isNull(storeName) && storeName != "") {
            storeQueryWrapper.like("store_name", storeName);
        }
        Page<Store> page = this.page(storePage, storeQueryWrapper);
        return Result.success(page.getRecords());
    }

    @Override
    public StoreVo getStoreVo(Integer storeId) {
        return storeMapper.getStoreVo(storeId);
    }

    @Override
    public Result getTopFive() {
        DateTime date = DateUtil.date();
        String endDay = DateUtil.format(date, "yyyy-MM-dd");
        DateTime dateTime = DateUtil.offsetDay(date, -7);
        String beginDay = DateUtil.format(dateTime, "yyyy-MM-dd");
        List<StoreTopDo> topFives = storeMapper.getTopFive(beginDay, endDay);
        ArrayList<String> xAxisData = new ArrayList<>();
        xAxisData.add(beginDay);
        for (int i = 1; i < 7; i++) {
            String format =
                    DateUtil.format(DateUtil.offsetDay(dateTime, i), "yyyy-MM-dd");
            xAxisData.add(format);
        }
        ArrayList<String> legendList = new ArrayList<>();
        ArrayList<SeriesChild> seriesChildren = new ArrayList<>();
        for (StoreTopDo topFive : topFives) {
            Integer storeId = topFive.getStoreId();
            ArrayList<Integer> list = new ArrayList<>();
            SeriesChild seriesChild = new SeriesChild();
            String storeName = storeMapper.selectById(storeId).getStoreName();
            legendList.add(storeName);
            seriesChild.setName(storeName);
            seriesChild.setType("line");
            for (String xAxisDatum : xAxisData) {
                Integer countByStore = storeMapper.getCountByStore(storeId, xAxisDatum);
                list.add(countByStore);
            }
            seriesChild.setData(list);
            seriesChildren.add(seriesChild);
        }
        OptionRecordsVo optionRecordsVo = new OptionRecordsVo();
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger("axis");
        optionRecordsVo.setTooltip(tooltip);
        Legend legend = new Legend();
        legend.setData(legendList);
        optionRecordsVo.setLegend(legend);
        Grid grid = new Grid();
        grid.setLeft("3%");
        grid.setRight("4%");
        grid.setBottom("3%");
        grid.setContainLabel(true);
        optionRecordsVo.setGrid(grid);
        Toolbox toolbox = new Toolbox();
        SaveAsImage saveAsImage = new SaveAsImage();
        Feature feature = new Feature();
        feature.setSaveAsImage(saveAsImage);
        toolbox.setFeature(feature);
        optionRecordsVo.setToolbox(toolbox);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        xAxis.setBoundaryGap(false);
        xAxis.setData(xAxisData);
        optionRecordsVo.setXAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setType("value");
        optionRecordsVo.setYAxis(yAxis);
        optionRecordsVo.setSeries(seriesChildren);
        return Result.success(optionRecordsVo);
    }

    @Override
    public BigDecimal getTotalAmount(Integer storeId) {
        return null;
    }

    @Override
    public Result blacked(Integer id) {
        this.update(new LambdaUpdateWrapper<Store>().eq(Store::getId, id)
        .set(Store::getDisabled, 1));
        goodsMapper.update(null, new LambdaUpdateWrapper<Goods>().eq(Goods::getStoreId, id)
                .set(Goods::getStatus, GoodsStatusEnum.TAKE_DOWN.getCode()));
        return Result.success();
    }

    @Override
    public Result cancelBlacked(Integer id) {
        this.update(new LambdaUpdateWrapper<Store>().eq(Store::getId, id)
        .set(Store::getDisabled, 0));
        goodsMapper.update(null, new LambdaUpdateWrapper<Goods>().eq(Goods::getStoreId, id)
                .set(Goods::getStatus, GoodsStatusEnum.SHELVES.getCode()));
        return Result.success() ;
    }
}
