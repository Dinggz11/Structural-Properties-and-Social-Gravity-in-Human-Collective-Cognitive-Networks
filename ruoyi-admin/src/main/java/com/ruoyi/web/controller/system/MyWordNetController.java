package com.ruoyi.web.controller.system;

import cn.hutool.json.JSONArray;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ExpWords;
import com.ruoyi.system.domain.MyWords;
import com.ruoyi.system.domain.MyWordsRelation;
import com.ruoyi.system.service.IExpWordsService;
import com.ruoyi.system.service.IMyWordsRelationService;
import com.ruoyi.system.service.IMyWordsService;
import com.ruoyi.system.vo.MyWordVo;
import com.ruoyi.system.vo.NetWork;
import com.ruoyi.system.vo.PageData;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: its2022
 * @description: 词汇网络控制点
 * @author: dinggz
 * @create: 2023-12-01 18:29
 **/
@RestController
@RequestMapping("wordnet")
public class MyWordNetController extends BaseController {

    private static String MODULE_PATH = "wordnet/";
    @Resource
    private IExpWordsService expWordsService;

    @Resource
    private IMyWordsService myWordsService;

    @Resource
    private IMyWordsRelationService myWordsRelationService;


    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        return getDataTable(this.expWordsService.getExpList());
    }

    @GetMapping("/{expId}")
    public ModelAndView network(ModelAndView modelAndView, Model model,@PathVariable Integer expId) {
        SysUser currentUser = getSysUser();
        this.myWordsService.preInsert(expId,currentUser.getUserId().toString());
        model.addAttribute("expId",expId);
        modelAndView.setViewName(MODULE_PATH + "index");
        return modelAndView;
    }

    @GetMapping("getNetWork/{expId}")
    public NetWork getNetWork(@PathVariable  Integer expId) {
        SysUser user = getSysUser();
        String[] nodesAndedges = this.myWordsRelationService.getNodesByExpId(expId, user.getUserId().toString());
        NetWork netWork = new NetWork();
        JSONArray nodesArr = new JSONArray("[" + nodesAndedges[0]+"]");
        JSONArray edgesArr = new JSONArray("[" + nodesAndedges[1]+"]");
        netWork.setNodes(nodesArr);
        netWork.setEdges(edgesArr);
        return netWork;
    }

    /**
     * 删除实验数据，重新开始
     * @param expId
     * @param modelAndView
     * @param model
     * @return
     */
    @GetMapping("deleteMyNetWork/{expId}")
    public ModelAndView deleteMyNetWork(@PathVariable  Integer expId,ModelAndView modelAndView, Model model) {
        SysUser currentUser = getSysUser();
        this.myWordsRelationService.removeExpData(expId,currentUser.getUserId().toString());
        this.myWordsService.preInsert(expId,currentUser.getUserId().toString());
        modelAndView.setViewName("redirect:/"+MODULE_PATH+expId);
        return modelAndView;
    }



    @GetMapping("add")
    public ModelAndView add(ModelAndView modelAndView,Model model,Integer expId,String id) {
        model.addAttribute("expId",expId);
        if (id!=null&&id.length()>0){
            MyWords myWords = this.myWordsService.getWordById(id);
            model.addAttribute("myWords",myWords);
        }
        modelAndView.setViewName(MODULE_PATH + "add");
        return modelAndView;
    }

    /**
     * 保存词汇网络
     * @param myKo
     * @return
     */
    @PostMapping("save")
    public AjaxResult save(@RequestBody MyWordVo myKo) {
        myKo.setUserId(getSysUser().getUserId().toString());
        myKo.setCreateTime(LocalDateTime.now());
        boolean result =myWordsRelationService.saveMyWordsRelation(myKo);
        return toAjax(result);
    }

    /**
     * 维护我的网络
     * @param modelAndView
     * @param model
     * @param expId
     * @return
     */
    @GetMapping("maintainMyNetwork")

    public ModelAndView maintainMyNetwork(ModelAndView modelAndView, Model model, Integer expId) {
        model.addAttribute("expId",expId);
        modelAndView.setViewName(MODULE_PATH + "maintainMyNetwork");
        return modelAndView;
    }

    @GetMapping("myNetWork")
    public PageData<MyWordsRelation> data(PageDomain pageDomain, MyWordsRelation netWork) {
        SysUser sysUser = getSysUser();
        netWork.setCreateUserId(sysUser.getUserId().toString());
        return  this.myWordsRelationService.getPage(pageDomain.getPageNum(),pageDomain.getPageSize(),netWork);
    }

    /**
     * 修改我的词汇网络
     * @param modelAndView
     * @param id
     * @param model
     * @return
     */
    @GetMapping("editMyNetwork")
    public ModelAndView editMyNetwork(ModelAndView modelAndView, String id,Model model) {
        modelAndView.addObject("myWordRelation", myWordsRelationService.getById(id));
        modelAndView.setViewName(MODULE_PATH + "editMyNetwork");
        return modelAndView;
    }

    /**
     * Describe: 更新我的网络
     * Return 执行结果
     */
    @PostMapping("updateMyNetwork")
    public AjaxResult updateMyNetwork(@RequestBody MyWordVo myKo) {
        boolean result = myWordsRelationService.updateMyNetWork(myKo);
        return toAjax(result);
    }

    /**
     * 删除我的探究网络
     * @param id
     * @return
     */
    @DeleteMapping("removeMyNetwork/{id}")
    public AjaxResult removeMyNetwork(@PathVariable String id) {
        MyWordsRelation myWordsRelation = this.myWordsRelationService.getById(id);
        boolean result = myWordsRelationService.removeMyNetwork(id);
        return toAjax(result);
    }

}
