package com.ruoyi.system.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: exam
 * @description:分页数据model
 * @author: 丁国柱
 * @create: 2021-04-05 16:01
 */
@Data
public class PageData<T> {
    private int code;
    private long count;
    private List<T> data;
    private String msg;
}