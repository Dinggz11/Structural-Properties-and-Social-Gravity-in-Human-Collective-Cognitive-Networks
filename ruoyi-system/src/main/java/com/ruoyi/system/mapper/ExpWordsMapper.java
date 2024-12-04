package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ExpWords;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExpWordsMapper extends BaseMapper<ExpWords> {

    @Select("select distinct(exp_id) from exp_words order by exp_id asc")
    List<Integer> getExp();

    @Select("select count(word) from exp_words where exp_id=#{expId}")
    int wordCount(int expId);

}
