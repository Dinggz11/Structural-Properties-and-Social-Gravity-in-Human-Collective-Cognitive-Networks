package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SequenceUtil;
import com.ruoyi.system.domain.MyWords;
import com.ruoyi.system.domain.MyWordsRelation;
import com.ruoyi.system.mapper.MyWordsRelationMapper;
import com.ruoyi.system.service.IMyWordsRelationService;
import com.ruoyi.system.service.IMyWordsService;
import com.ruoyi.system.vo.MyWordVo;
import com.ruoyi.system.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: its2022
 * @description: 我的词汇网络关系
 * @author: dinggz
 * @create: 2023-12-01 19:33
 **/
@Service
public class MyWordsRelationServiceImpl extends ServiceImpl<MyWordsRelationMapper, MyWordsRelation> implements IMyWordsRelationService {

    @Resource
    private IMyWordsService myWordsService;

    @Override
    public PageData<MyWordsRelation> getPage(int page, int limit, MyWordsRelation myWordsRelation) {
        PageData<MyWordsRelation> collegePageData = new PageData<>();
        Page pageInfo = new Page(page, limit);
        QueryWrapper<MyWordsRelation> queryWrapper = new QueryWrapper<>();
        if (myWordsRelation.getCreateUserId() != null) {
            queryWrapper.eq("create_user_id", myWordsRelation.getCreateUserId());
        }
        if (myWordsRelation.getExpId() != null) {
            queryWrapper.eq("exp_id", myWordsRelation.getExpId());

        }

        IPage ipage = this.page(pageInfo, queryWrapper);
        collegePageData.setCount(ipage.getTotal());//总记录数
        collegePageData.setData(ipage.getRecords());//当前分页的记录
        collegePageData.setCode(0);//正确的分页响应code为0
        collegePageData.setMsg("我的网络分页");
        return collegePageData;
    }

    @Override
    public boolean saveMyWordsRelation(MyWordVo myWordVo) {
        String from = myWordVo.getFrom();
        LocalDateTime localDateTime = LocalDateTime.now();
        MyWords fromWords = null;
        if (myWordVo.getFromId() != null) {
            //输入节点不为空 同时类型为用户
            fromWords = this.myWordsService.getById(myWordVo.getFromId());
            fromWords.setCreateTime(localDateTime);
            fromWords.setExpId(myWordVo.getExpId());
            fromWords.setWord(from);
            this.myWordsService.updateById(fromWords);
        } else {
            fromWords = this.myWordsService.getByMyWords(from, myWordVo.getExpId(), myWordVo.getUserId());
            if (fromWords == null) {
                fromWords = new MyWords();
                fromWords.setMyWordId(SequenceUtil.makeStringId());
                fromWords.setCreateUserId(myWordVo.getUserId());
                fromWords.setCreateTime(localDateTime);
                fromWords.setExpId(myWordVo.getExpId());
                fromWords.setType(2);
                fromWords.setWord(from.trim());
                this.myWordsService.save(fromWords);
            }
        }
        String to = myWordVo.getTo();
        MyWords toMyword = this.myWordsService.getByMyWords(to, myWordVo.getExpId(), myWordVo.getUserId());
        if (toMyword == null) {
            toMyword = new MyWords();
            toMyword.setMyWordId(SequenceUtil.makeStringId());
            toMyword.setCreateUserId(myWordVo.getUserId());
            toMyword.setExpId(myWordVo.getExpId());
            toMyword.setCreateTime(localDateTime);
            toMyword.setWord(to.trim());
            toMyword.setType(2);
            this.myWordsService.save(toMyword);
        }

        //保存我的网络
        MyWordsRelation netWork = new MyWordsRelation();
        netWork.setFromId(fromWords.getMyWordId());
        netWork.setCreateTime(localDateTime);
        netWork.setExpId(myWordVo.getExpId());
        netWork.setRelation(myWordVo.getRelation().trim());
        netWork.setToId(toMyword.getMyWordId());
        netWork.setCreateUserId(myWordVo.getUserId());
        return this.save(netWork);
    }

    @Override
    public boolean updateMyNetWork(MyWordVo myKo) {
        MyWords fromMyWords= this.myWordsService.getById(myKo.getFromId());
        if(fromMyWords.getType()!=1){
            fromMyWords.setWord(myKo.getFrom().trim());
            fromMyWords.setCreateTime(LocalDateTime.now());
            this.myWordsService.updateById(fromMyWords);
        }
        MyWords toMyWords= this.myWordsService.getById(myKo.getToId());
        if(toMyWords.getType()!=1){
            toMyWords.setWord(myKo.getTo().trim());
            toMyWords.setCreateTime(LocalDateTime.now());
            this.myWordsService.updateById(toMyWords);
        }
        //更新知识节点关系
        MyWordsRelation myNetWork = this.getById(myKo.getId());
        myNetWork.setRelation(myKo.getRelation().trim());
        return this.updateById(myNetWork);
    }

    @Override
    public boolean updateMyWordsRelation(MyWordVo myWordVo) {
        MyWords fromMyWords = this.myWordsService.getById(myWordVo.getFromId());
        fromMyWords.setType(2);
        fromMyWords.setWord(myWordVo.getFrom());
        this.myWordsService.updateById(fromMyWords);
        MyWords toMyWords = this.myWordsService.getById(myWordVo.getToId());
        toMyWords.setType(2);
        toMyWords.setWord(myWordVo.getTo());
        this.myWordsService.updateById(toMyWords);

        //更新知识节点关系
        MyWordsRelation myWordsRelation = this.getById(myWordVo.getId());
        myWordsRelation.setRelation(myWordVo.getRelation());
        return this.updateById(myWordsRelation);
    }

    @Override
    @Transactional
    public void removeExpData(Integer expId, String userId) {
        //删除关联关系
        this.baseMapper.deleteRelationByExpIdAndUserId(expId,userId);

        //删除用户词汇
        QueryWrapper<MyWords> myWordsQueryWrapper = new QueryWrapper<>();
        myWordsQueryWrapper.eq("create_user_id",userId);
        this.myWordsService.remove(myWordsQueryWrapper);
    }

    @Override
    @Transactional
    public boolean removeMyNetwork(String id) {
        MyWordsRelation myWordsRelation = this.getById(id);
        String fromId = myWordsRelation.getFromId();
        String toId = myWordsRelation.getToId();
        Integer expId = myWordsRelation.getExpId();
        //删掉关系
        this.removeById(id);
        //删掉用户自己的节点
        MyWords fromMyWords = this.myWordsService.getById(fromId);
        MyWords toMyWords = this.myWordsService.getById(toId);

        //用户自己创建的节点
        if(fromMyWords.getType()==2){
            int fromMyWordsCount = this.baseMapper.getCountByWordId(fromId,expId);
            if(fromMyWordsCount==0){
                //即当前网络关系中没有这个节点相关的信息了
                this.myWordsService.removeById(fromId);
            }
        }
        //用户自己创建的节点
        if(toMyWords.getType()==2){
            int toMyWordsCount = this.baseMapper.getCountByWordId(toId,expId);
            if(toMyWordsCount==0){
                //即当前网络关系中没有这个节点相关的信息了
                this.myWordsService.removeById(toId);
            }
        }
        return true;
    }

    @Override
    public String[] getNodesByExpId(Integer expId, String userId) {
        QueryWrapper<MyWordsRelation> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("create_user_id", userId);
        }
        if (expId != null) {
            queryWrapper.eq("exp_id", expId);
        }
        List<MyWordsRelation> myWordsRelations = this.list(queryWrapper);
        Set<MyWordsRelation> netWorkSet = new HashSet<>();
        netWorkSet.addAll(myWordsRelations);

        StringBuffer nodes = new StringBuffer();
        StringBuffer edges = new StringBuffer();
        Set<MyWords> knowledges = new HashSet<>();
        Set<String> koIds = new HashSet<>();

        QueryWrapper<MyWords> myWordsQueryWrapper = new QueryWrapper<>();
        myWordsQueryWrapper.eq("exp_id",expId);
        myWordsQueryWrapper.eq("create_user_id",userId);
        List<MyWords> myWordsList = this.myWordsService.list(myWordsQueryWrapper);
        for (MyWords myWords : myWordsList
        ) {
            if (!knowledges.contains(myWords)) {
                if (myWords.getType() == 1) {
                    //color: "rgb(255,168,7)"
                    nodes.append("{id:'" + myWords.getMyWordId() + "',label:'" + myWords.getWord() + "',color:'#66CC33'},");
                } else {
                    nodes.append("{id:'" + myWords.getMyWordId() + "',label:'" + myWords.getWord() + "'},");
                }
                if (!koIds.contains(myWords.getMyWordId())) {
                    knowledges.add(myWords);
                    koIds.add(myWords.getMyWordId());
                }
            }
        }

        for (MyWordsRelation myWordsRelation : netWorkSet
        ) {
            //处理节点
            MyWords fromMyWords = myWordsRelation.getFrom();
            MyWords toMyWords = myWordsRelation.getTo();
            if (!knowledges.contains(fromMyWords)) {
                if (fromMyWords.getType() == 1) {
                    nodes.append("{id:'" + fromMyWords.getMyWordId() + "',label:'" + fromMyWords.getWord() + "',color:'#66CC33'},");
                } else {
                    nodes.append("{id:'" + fromMyWords.getMyWordId() + "',label:'" + fromMyWords.getWord() + "'},");
                }
                if (!koIds.contains(fromMyWords.getMyWordId())) {
                    knowledges.add(fromMyWords);
                    koIds.add(fromMyWords.getMyWordId());
                }
            }
            if (!knowledges.contains(toMyWords)) {
                if (toMyWords.getType() == 1) {
                    nodes.append("{id:'" + toMyWords.getMyWordId() + "',label:'" + toMyWords.getWord() + "',color:'#66CC33'},");
                } else {
                    nodes.append("{id:'" + toMyWords.getMyWordId() + "',label:'" + toMyWords.getWord() + "'},");
                }
                if (!koIds.contains(toMyWords.getMyWordId())) {
                    knowledges.add(toMyWords);
                    koIds.add(toMyWords.getMyWordId());
                }
            }
            //处理边
            edges.append("{from:'" + fromMyWords.getMyWordId() + "',to:'" + toMyWords.getMyWordId() + "',label:'" + myWordsRelation.getRelation() + "',arrows: 'to' },");
        }
        return new String[]{nodes.toString(), edges.toString()};
    }

    @Override
    public Page<SysUser> selectUserPage(Integer page, Integer limit, Integer expId) {
        return null;
    }
}
