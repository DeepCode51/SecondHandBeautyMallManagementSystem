package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjr.mzmall.vo.StoreVo;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
public interface StoreService extends IService<Store> {
    Result getStoreList(String storeName, LayuiPage layuiPage);
    StoreVo getStoreVo(Integer storeId);
    Result getTopFive();
    BigDecimal getTotalAmount(Integer storeId);

    Result blacked(Integer id);

    Result cancelBlacked(Integer id);
}
