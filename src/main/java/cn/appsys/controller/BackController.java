package cn.appsys.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.Page;
import cn.appsys.service.appcategory.AppCategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.Constants;
@Controller
@RequestMapping("/backend")
public class BackController {
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppVersionService appVersionService;
	
	@RequestMapping("/list")
	public String list(Model model,HttpSession session,@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="pageIndex",required=false)Integer currentPageNo,
			@RequestParam(value="pageSize",required=false)Integer pageSize){
		Page pages = new Page();
		pages.setPageSize(5);
		pages.setTotalCount(appInfoService.queryAppInfoCount(querySoftwareName,1, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId));
		if(currentPageNo==null || currentPageNo==0){
			currentPageNo=1;
		}
		pages.setCurrentPageNo(currentPageNo);
		BackendUser backUser = (BackendUser) session.getAttribute(Constants.USER_SESSION);
		List<AppInfo> appInfoList= appInfoService.queryByPage(querySoftwareName,1,queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,backUser.getId(),(pages.getCurrentPageNo()-1)*pages.getPageSize(),pages.getPageSize());
		model.addAttribute("appInfoList",appInfoList);
		model.addAttribute("pages",pages);
		List<DataDictionary> statusList = this.getDataDictionarieList("APP_STATUS");
		List<DataDictionary> flatFormList = this.getDataDictionarieList("APP_FLATFORM");
		session.setAttribute("statusList", statusList);
		session.setAttribute("flatFormList", flatFormList);
		List<AppCategory> categoryLevel1List = appCategoryService.queryInfoByParentId(null);
		if(queryCategoryLevel2!=null){
			List<AppCategory> categoryLevel2List = appCategoryService.queryInfoByParentId(queryCategoryLevel1);
			model.addAttribute("categoryLevel2List", categoryLevel2List);
		}
		if(queryCategoryLevel3!=null){
			List<AppCategory> categoryLevel3List = appCategoryService.queryInfoByParentId(queryCategoryLevel2);
			model.addAttribute("categoryLevel3List", categoryLevel3List);
		}
		model.addAttribute("categoryLevel1List",categoryLevel1List);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);
		return "backend/applist";
	}

	public List<DataDictionary> getDataDictionarieList(String typeCode){	//根据typeCode获取信息
		List<DataDictionary> dataDictionaries = dataDictionaryService.queryInfoByTypeCode(typeCode);
		return dataDictionaries;
	}
	
	@RequestMapping("/categorylevellist.json")	//查询一级二级三级分类菜单
	@ResponseBody
	public List<AppCategory> categorylevellist(@RequestParam("pid")Integer parentId){
		List<AppCategory> categoryLevelList = appCategoryService.queryInfoByParentId(parentId);
		return categoryLevelList;
	} 

	@RequestMapping("/datadictionarylist.json")	//发送Ajax输出所属平台
	@ResponseBody
	public List<DataDictionary> datadictionarylist(@RequestParam("tcode")String typeCode){
		return this.getDataDictionarieList(typeCode);
	}
	
	@RequestMapping("/check/{aid}/{vid}")	//跳转到审核APP页面
	public String tocheck(@PathVariable("aid")Integer aid,@PathVariable("vid")Integer vid,Model model){
		AppInfo appInfo = appInfoService.queryAppViewById(aid);	//当前APP信息
		AppVersion appVersion = appVersionService.queryAppVersionByIdandAppId(vid, aid);//根据当前版本id查询最新当前app的最新版本信息
		model.addAttribute("appInfo",appInfo);
		model.addAttribute("appVersion",appVersion);
		return "backend/appcheck";
	}
	
	@RequestMapping("/check/checksave")	//执行审核操作
	public String checkSave(@Param("status")Integer status,@Param("id")Integer id){
		appInfoService.updateStatusById(status, id);
		return "redirect:../list";
	}
}
