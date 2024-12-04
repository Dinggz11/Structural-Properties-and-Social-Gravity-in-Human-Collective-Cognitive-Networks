package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.MyWords;
import com.ruoyi.system.vo.PageData;

public interface IMyWordsService extends IService<MyWords> {

    //先预设部分词汇
    void preInsert(int expId,String userId);

    /**
     * @param page:当前页面数
     * @param limit:每一页显示多少条数据
     * @param myWords:检索的问题信息
     * @return
     */
    public PageData<MyWords> getPage(int page, int limit, MyWords myWords);


    public MyWords getByMyWords(String word,Integer expId,String userId);


    MyWords getWordById(String id);

}
