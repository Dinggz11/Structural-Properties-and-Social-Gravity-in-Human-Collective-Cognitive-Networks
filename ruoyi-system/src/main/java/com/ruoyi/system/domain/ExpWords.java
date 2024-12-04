package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: its2022
 * @description: 实验预设词汇
 * @author: dinggz
 * @create: 2023-12-01 18:47
 **/
@Data
@TableName("exp_words")
public class ExpWords {

    //主键
    private String id;
    //预设词汇
    private String word;
    //实验id
    private int expId;
    //词汇数
    private int wordCount;

}
