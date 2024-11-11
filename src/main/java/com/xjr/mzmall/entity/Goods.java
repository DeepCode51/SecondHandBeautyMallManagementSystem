package com.xjr.mzmall.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编号
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Integer goodsId;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 店铺编号
     */
    private Integer storeId;

    /**
     * 展示封面
     */
    private String cover;

    /**
     * 商品描述
     */
    private String goodDesc;

    /**
     * 分类编号
     */
    private Integer categoryId;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 新旧程度 0：全新 1：95新 2：9新 3：85新 4：8新 5：7新 6：6新
     */
    private Integer newLevel;

    /**
     * 商品状态 0 上架 1 下架
     */
    private Integer status;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品详情图片
     */
    private String detailImg;

    @TableField(exist = false)
    private List<String> detailImgList;
}
