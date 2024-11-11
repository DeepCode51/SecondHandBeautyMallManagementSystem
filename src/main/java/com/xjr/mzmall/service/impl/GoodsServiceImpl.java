package com.xjr.mzmall.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.GoodsTopFiveDo;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.GoodsListDTO;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.Store;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.GoodsStatusEnum;
import com.xjr.mzmall.enums.ResultEnum;
import com.xjr.mzmall.mapper.GoodsMapper;
import com.xjr.mzmall.mapper.StoreMapper;
import com.xjr.mzmall.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.utils.CommonUtils;
import com.xjr.mzmall.vo.GoodsVo;
import com.xjr.mzmall.vo.OptionRecordsVo;
import com.xjr.mzmall.vo.optionRecords.Legend;
import com.xjr.mzmall.vo.optionRecords.SeriesChild;
import com.xjr.mzmall.vo.optionRecords.XAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO, HttpServletRequest request) {
        Long curPage = layuiPage.getPage();
        Long pageLimit = layuiPage.getLimit();
        if (Objects.isNull(pageLimit)) {
            pageLimit = 10l;
        }
        if (Objects.isNull(curPage)) {
            curPage = 1l;
        }
        Page<GoodsVo> goodsPage = new Page<>(curPage, pageLimit);
        User user = (User) request.getSession().getAttribute("user");
        goodsListDTO.setUserId(user.getUserId());
        IPage<GoodsVo> goodsList = goodsMapper.getGoodsList(goodsPage, goodsListDTO);
        return Result.success(ResultEnum.SUCCESS.getMessage(), goodsList.getRecords(), goodsList.getTotal());
    }

    @Override
    public Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO) {
        Page<GoodsVo> goodsVoPage = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage<GoodsVo> goodsList2 = goodsMapper.getGoodsList2(goodsVoPage, goodsListDTO);
        return Result.success(goodsList2.getRecords());
    }

    @Override
    public Result createGoods(Goods goods, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Store store = storeMapper
                .selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getUserId, user.getUserId()));
        goods.setStoreId(store.getId());
        goods.setStatus(0);
        this.save(goods);
        return Result.success();
    }

    @Override
    public Result updateGoods(Goods goods) {
        if (goods.getStock() == 0) {
            goods.setStatus(GoodsStatusEnum.TAKE_DOWN.getCode());
        }
        this.updateById(goods);
        return Result.success();
    }

    @Override
    public Result deleteGoods(String ids) {
        List<Integer> list = CommonUtils.StringsToList(ids);
        this.removeByIds(list);
        return Result.success();
    }

    @Override
    public List<List<Goods>> getRecommendGoods(int total, int num) {
        List<Goods> goods = goodsMapper.randomGetGoods(total);
        return CommonUtils.averageAssign(goods, num);
    }

    @Override
    public List<List<Goods>> getGuessYouWillLikeGoods(int total, int num) {
        return CommonUtils.averageAssign(goodsMapper.randomGetGoods(total), num);
    }

    @Override
    public List<Goods> getGoodsRandom(int num) {
        return goodsMapper.randomGetGoods(num);
    }

    @Override
    public Integer goodsCount(Integer userId) {
        return goodsMapper.getGoodsCount(userId);
    }

    @Override
    public Result getGoodsTopFive(Integer userId) {
        DateTime date = DateUtil.date();
        String endDay = DateUtil.format(date, "yyyy-MM-dd");
        DateTime dateTime = DateUtil.offsetDay(date, -7);
        String beginDay = DateUtil.format(dateTime, "yyyy-MM-dd");
        List<GoodsTopFiveDo> goodsFiveDo = goodsMapper.getGoodsFiveDo(userId, beginDay, endDay);
        ArrayList<String> xAxisData = new ArrayList<>();
        ArrayList<String> legendList = new ArrayList<>();
        ArrayList<SeriesChild> seriesChildren = new ArrayList<>();
        xAxisData.add(beginDay);
        for (int i = 1; i < 7; i++) {
            String format =
                    DateUtil.format(DateUtil.offsetDay(dateTime, i), "yyyy-MM-dd");
            xAxisData.add(format);
        }
        for (GoodsTopFiveDo goodsTopFiveDo : goodsFiveDo) {
            legendList.add(goodsTopFiveDo.getGoodsName());
            Integer goodsId = goodsTopFiveDo.getGoodsId();
            ArrayList<Integer> list = new ArrayList<>();
            SeriesChild seriesChild = new SeriesChild();
            seriesChild.setName(goodsTopFiveDo.getGoodsName());
            seriesChild.setType("line");
            for (String xAxisDatum : xAxisData) {
                Integer goodsCountByGoodsId = goodsMapper.getGoodsCountByGoodsId(goodsId, xAxisDatum);
                if (Objects.isNull(goodsCountByGoodsId)) {
                    goodsCountByGoodsId = 0;
                }
                list.add(goodsCountByGoodsId);
            }
            seriesChild.setData(list);
            seriesChildren.add(seriesChild);
        }
        OptionRecordsVo optionRecordsVo = new OptionRecordsVo();
        Legend legend = new Legend();
        legend.setData(legendList);
        optionRecordsVo.setLegend(legend);
        XAxis xAxis = new XAxis();
        xAxis.setType("category");
        xAxis.setBoundaryGap(false);
        xAxis.setData(xAxisData);
        optionRecordsVo.setXAxis(xAxis);
        optionRecordsVo.setSeries(seriesChildren);
        return Result.success(optionRecordsVo);
    }
}
