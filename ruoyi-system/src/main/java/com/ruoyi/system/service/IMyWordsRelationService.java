package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.MyWordsRelation;
import com.ruoyi.system.vo.MyWordVo;
import com.ruoyi.system.vo.PageData;

public interface IMyWordsRelationService extends IService<MyWordsRelation> {
    public PageData<MyWordsRelation> getPage(int page, int limit, MyWordsRelation myWordsRelation);

    public boolean saveMyWordsRelation(MyWordVo myWordVo);

    public boolean updateMyNetWork(MyWordVo myKo);

    public boolean updateMyWordsRelation(MyWordVo myWordVo);

    /**
     * 删除个人实验数据
     * @param expId
     */
    public void removeExpData(Integer expId,String userId);

    /**
     * 删除我的网络
     * @param id
     * @return
     */
    public boolean removeMyNetwork(String id);



    /**
     * 根据实验id，获取实验词汇网络
     * @param expId
     * @return
     */
    public String[] getNodesByExpId(Integer expId,String userId);

    public Page<SysUser> selectUserPage(Integer page, Integer limit, Integer expId);
}
