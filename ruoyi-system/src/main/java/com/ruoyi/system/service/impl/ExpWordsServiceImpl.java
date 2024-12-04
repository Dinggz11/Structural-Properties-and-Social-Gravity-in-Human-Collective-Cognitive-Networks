package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.ExpWords;
import com.ruoyi.system.mapper.ExpWordsMapper;
import com.ruoyi.system.service.IExpWordsService;
import com.ruoyi.system.vo.Exp;
import com.ruoyi.system.vo.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: its2022
 * @description: 实验预设词汇
 * @author: dinggz
 * @create: 2023-12-01 18:53
 **/
@Service
public class ExpWordsServiceImpl extends ServiceImpl<ExpWordsMapper, ExpWords> implements IExpWordsService {
    @Resource
    private ExpWordsMapper expWordsMapper;

    @Override
    public List<Exp> getExpList() {
        List<Integer> exps = this.expWordsMapper.getExp();
        List<Exp> exps1 = new ArrayList<>();
        for (Integer exp:exps
             ) {
            Exp e = new Exp();

            QueryWrapper<ExpWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exp_id",exp);
            List<ExpWords> expWords = this.list(queryWrapper);
            String words = "";
            for (ExpWords eword : expWords){
                words = words + eword.getWord()+",";
            }
            e.setWordCount(expWords.size());
            e.setWords(words);
            e.setExpId(exp);
            exps1.add(e);
        }
        return exps1;
    }

    @Override
    public PageData<ExpWords> getPage(int page, int limit, ExpWords expWords) {
        PageData<ExpWords> collegePageData = new PageData<>();
        Page pageInfo = new Page(page, limit);
        QueryWrapper<ExpWords> queryWrapper = new QueryWrapper<>();
        if(expWords.getWord()!=null){
            queryWrapper.like("word",expWords.getWord());
        }
        IPage ipage = this.page(pageInfo, queryWrapper);
        collegePageData.setCount(ipage.getTotal());//总记录数
        collegePageData.setData(ipage.getRecords());//当前分页的记录
        collegePageData.setCode(0);//正确的分页响应code为0
        collegePageData.setMsg("我的网络分页");
        return collegePageData;
    }
}
