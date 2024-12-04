package com.ruoyi.system.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: its2022
 * @description:
 * @author: dinggz
 * @create: 2023-12-01 19:48
 **/
@Data
public class MyWordVo {
    private String id;

    private String fromId;

    private String from;

    private String to;

    private String toId;

    private Integer expId;

    private String relation;

    private String userId;

    private LocalDateTime createTime;
}
