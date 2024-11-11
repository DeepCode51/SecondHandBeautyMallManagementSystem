package com.xjr.mzmall.DO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintOrderDo {
    private Integer id;
    private Integer orderId;
    private String complaintReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer status;
    private String username;
    private String userPhone;
}
