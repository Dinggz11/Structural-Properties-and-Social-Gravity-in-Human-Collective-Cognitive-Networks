package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @program: its2022
 * @description: 词汇
 * @author: dinggz
 * @create: 2023-12-01 18:13
 **/
@Data
@TableName("exp_my_words")
public class MyWords {
    private String myWordId;
    //词汇
    private String word;

    //创建时间
    private LocalDateTime createTime;

    @TableField(exist = false)
    private SysUser createUser;

    //创建的用户id
    private String createUserId;

    //类型 1为系统自动创建，用于开始构建网络，2为用户自己创建
    private int type;

    //实验id,第一次实验id为1
    private Integer expId;


    /**
     * 删除标识：0为未删除，1为已删除
     */
    @TableLogic
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyWords word = (MyWords) o;
        return myWordId.equals(word.myWordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myWordId);
    }
}
