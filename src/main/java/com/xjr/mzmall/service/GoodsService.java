package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.GoodsListDTO;
import com.xjr.mzmall.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjr.mzmall.vo.OptionRecordsVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
public interface GoodsService extends IService<Goods> {
    Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO, HttpServletRequest request);
    Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO);
    Result createGoods(Goods goods, HttpServletRequest request);
    Result updateGoods(Goods goods);
    Result deleteGoods(String ids);
    /**
     * 获取total条数据平均分成num等份
     * @param total
     * @param num
     * @return
     */
    List<List<Goods>> getRecommendGoods(int total, int num);
    /**
     * 获取total条数据平均分成num等份
     * @param total
     * @param num
     * @return
     */
    List<List<Goods>> getGuessYouWillLikeGoods(int total, int num);
    List<Goods> getGoodsRandom(int num);

    Integer goodsCount(Integer userId);

    Result getGoodsTopFive(Integer userId);
}
