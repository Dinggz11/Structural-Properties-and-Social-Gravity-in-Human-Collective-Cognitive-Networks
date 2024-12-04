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
 * @description: 我的词汇关系网络
 * @author: dinggz
 * @create: 2022-05-14 00:38
 **/
@TableName(value = "exp_my_word_relation",resultMap = "baseResultMap")
@Data
public class MyWordsRelation {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyWordsRelation myNetWork = (MyWordsRelation) o;
        return fromId.equals(myNetWork.fromId) && toId.equals(myNetWork.toId) && relation.equals(myNetWork.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, relation);
    }

    //词汇网络的起点词汇
    private String id;

    //词汇网络的结点词汇
    private String fromId;

    //实验id
    private Integer expId;

    @TableField(exist = false)
    private MyWords from;

    @TableField(exist = false)
    private SysUser createUser;

    private String createUserId;

    private String toId;

    @TableField(exist = false)
    private MyWords to;

    private LocalDateTime createTime;

    private String relation;

    @TableLogic
    private Boolean deleted;

}
