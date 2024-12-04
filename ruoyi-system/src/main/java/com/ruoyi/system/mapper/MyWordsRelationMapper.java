package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.MyWordsRelation;

public interface MyWordsRelationMapper extends BaseMapper<MyWordsRelation> {

    /**
     * 根据词汇id，查询关系列表中有多少记录
     * @param wordId
     * @return
     */
    int getCountByWordId(String wordId,Integer expId);


    /**
     * 根据用户id和实验id删除实验数据
     * @param expId
     * @param userId
     */
    void deleteRelationByExpIdAndUserId(Integer expId,String userId);


}
