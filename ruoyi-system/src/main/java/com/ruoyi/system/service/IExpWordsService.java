package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.ExpWords;
import com.ruoyi.system.vo.Exp;
import com.ruoyi.system.vo.PageData;

import java.util.List;

public interface IExpWordsService extends IService<ExpWords> {

    //得到实验数据
    List<Exp> getExpList();

    public PageData<ExpWords> getPage(int page, int limit, ExpWords expWords);

}
