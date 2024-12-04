package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.SequenceUtil;
import com.ruoyi.system.domain.ExpWords;
import com.ruoyi.system.domain.MyWords;
import com.ruoyi.system.service.IExpWordsService;
import com.ruoyi.system.mapper.MyWordsMapper;
import com.ruoyi.system.service.IMyWordsService;
import com.ruoyi.system.vo.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: its2022
 * @description: 词汇服务
 * @author: dinggz
 * @create: 2023-12-01 18:27
 **/
@Service
public class MyWordsServiceImpl extends ServiceImpl<MyWordsMapper, MyWords> implements IMyWordsService {

    @Resource
    private IExpWordsService expWordsService;

    /**
     * 预先增加预设词汇
     * @param expId
     * @param userId
     */
    @Override
    public void preInsert(int expId,String userId) {
        //查询当前用户是否已经有相关实验词汇
        QueryWrapper<MyWords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exp_id",expId);
        queryWrapper.eq("create_user_id",userId);
        int count = this.baseMapper.selectCount(queryWrapper);
        if(count==0){
            //如果当前用户还没有相关词汇
            QueryWrapper<ExpWords> expWordsQueryWrapper= new QueryWrapper<>();
            expWordsQueryWrapper.eq("exp_id",expId);
            List<ExpWords> expWords = this.expWordsService.list(expWordsQueryWrapper);
            for (ExpWords expWord:expWords
                 ) {
                MyWords myWords =new MyWords();
                myWords.setWord(expWord.getWord());
                myWords.setCreateTime(LocalDateTime.now());
                myWords.setCreateUserId(userId);
                myWords.setType(1);
                myWords.setExpId(expId);
                myWords.setMyWordId(SequenceUtil.makeStringId());
                myWords.setDeleted(false);
                this.baseMapper.insert(myWords);
            }

        }
    }

    @Override
    public PageData<MyWords> getPage(int page, int limit, MyWords myWords) {
        PageData<MyWords> collegePageData = new PageData<>();
        Page pageInfo = new Page(page,limit);
        QueryWrapper<MyWords> queryWrapper = new QueryWrapper<>();
        if(myWords.getWord()!=null&&myWords.getWord().length()>0){
            queryWrapper.like("word",myWords.getWord());
        }
        if(myWords.getExpId()!=null){
            queryWrapper.eq("exp_id",myWords.getExpId());
        }
        IPage ipage = this.page(pageInfo,queryWrapper);
        collegePageData.setCount(ipage.getTotal());//总记录数
        collegePageData.setData(ipage.getRecords());//当前分页的记录
        collegePageData.setCode(0);//正确的分页响应code为0
        collegePageData.setMsg("我的词汇分页");
        return collegePageData;
    }

    @Override
    public MyWords getByMyWords(String word,Integer expId,String userId) {
        QueryWrapper<MyWords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("word",word);
        queryWrapper.eq("exp_id",expId);
        queryWrapper.eq("create_user_id",userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public MyWords getWordById(String id) {
        QueryWrapper<MyWords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("my_word_id",id);
        return this.getOne(queryWrapper);
    }
}
