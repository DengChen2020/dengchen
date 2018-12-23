package cn.appsys.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.pojo.Page;
import cn.appsys.service.appcategory.AppCategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.Constants;
@Controller
@RequestMapping("/app")
public class AppInfoController {
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private DataDictionaryService dataDictionaryService;
	@Resource
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;


	@RequestMapping("/list")	//条件分页查询App信息
	public String list(Model model,HttpSession session,@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) Integer queryStatus,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="pageIndex",required=false)Integer currentPageNo,
			@RequestParam(value="pageSize",required=false)Integer pageSize){
		Page pages = new Page();
		pages.setPageSize(5);
		pages.setTotalCount(appInfoService.queryAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId));
		if(currentPageNo==null || currentPageNo==0){
			currentPageNo=1;
		}
		pages.setCurrentPageNo(currentPageNo);
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEVUSER_SESSION);
		List<AppInfo> appInfoList= appInfoService.queryByPage(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,devUser.getId(),(pages.getCurrentPageNo()-1)*pages.getPageSize(),pages.getPageSize());
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
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("queryFlatformId", queryFlatformId);
		return "developer/appinfolist";
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

	@RequestMapping("/apkexist.json")	//查询APK名称是否已存在
	@ResponseBody
	public HashMap<String,String> isExistAPKName(@RequestParam("APKName")String APKName){	//判断APKName是否已存在
		HashMap<String, String> data = new HashMap<String,String>();
		if(APKName==null || APKName==""){
			data.put("APKName","empty");
		}else{
			int result = appInfoService.queryIsExistApkName(APKName);
			if(result>0){
				data.put("APKName","exist");
			}else{
				data.put("APKName","noexist");
			}
		}
		return data;
	}

	@RequestMapping("/appinfoadd")
	public String toAddAppInfo(){	//跳转到新增App信息页面
		return "developer/appinfoadd";
	}

	@RequestMapping("/infoaddsave")	//执行新增APP信息操作
	public String addAPPInfo(AppInfo appInfo,@RequestParam("a_logoPicPath")MultipartFile attach,HttpServletRequest request){
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			int filesize = 50000;
			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError","* 上传大小不得超过 50KB");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg") ||
					prefix.equalsIgnoreCase("png") ||
					prefix.equalsIgnoreCase("jpeg")){
				String fileName = appInfo.getAPKName()+".jpg";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError","* 上传失败！");
					return "developer/appinfoadd";
				}
				appInfo.setLogoLocPath(path+File.separator+fileName);
				appInfo.setLogoPicPath("/"+path.substring(path.lastIndexOf(request.getContextPath().substring(1))).replace("\\","/")+"/"+fileName);
			}else{
				request.setAttribute("fileUploadError","* 上传图片格式不正确");
				return "developer/appinfoadd";
			}
		}
		DevUser devUser = (DevUser)request.getSession().getAttribute(Constants.DEVUSER_SESSION);
		appInfo.setCreatedBy(devUser.getId());
		appInfo.setCreationDate(new Date());
		appInfo.setDevId(devUser.getId());
		int result = appInfoService.addAPKInfo(appInfo);
		if(result<=0){
			return "developer/appinfoadd";
		}
		return "redirect:list";
	}

	@RequestMapping("/appversionadd/{id}")	//跳转到App版本新增页面
	public String toAppVersionAddd(@PathVariable Integer id,Model model){	//跳转到新增版本页面
		List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(id);
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(id);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion",appVersion);
		return "developer/appversionadd";
	}

	@RequestMapping("/versionsave")	//App版本新增
	public String versionsave(AppVersion appVersion,@RequestParam("a_downloadLink")MultipartFile attach,HttpServletRequest request,Model model){
		AppInfo appInfo = appInfoService.queryAppInfoById(appVersion.getAppId());
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			int filesize = 500000000;
			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError","* 上传大小不得超过 500MB");
				List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appVersion.getAppId());
				model.addAttribute("appVersionList",appVersionList);
				model.addAttribute("appVersion",appVersion);
				return "developer/appversionadd";
			}else if(prefix.equalsIgnoreCase("apk")){
				String fileName = appInfo.getAPKName()+"-"+appVersion.getVersionNo()+".apk";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError","* 上传失败！");
					List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appVersion.getAppId());
					model.addAttribute("appVersionList",appVersionList);
					model.addAttribute("appVersion",appVersion);
					return "developer/appversionadd";
				}
				appVersion.setApkLocPath(path+File.separator+fileName);
				appVersion.setDownloadLink("/"+path.substring(path.lastIndexOf(request.getContextPath().substring(1))).replace("\\","/")+"/"+fileName);
				appVersion.setApkFileName(fileName);
			}else{
				request.setAttribute("fileUploadError","* 上传图片格式不正确");
				List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appVersion.getAppId());
				model.addAttribute("appVersionList",appVersionList);
				model.addAttribute("appVersion",appVersion);
				return "developer/appversionadd";
			}
		}
		DevUser devUser = (DevUser)request.getSession().getAttribute(Constants.DEVUSER_SESSION);
		appVersion.setCreatedBy(devUser.getId());
		appVersion.setCreationDate(new Date());
		int result = appVersionService.addAppVersion(appVersion);
		if(result<=0){
			List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appVersion.getAppId());
			model.addAttribute("appVersionList",appVersionList);
			model.addAttribute("appVersion",appVersion);
			return "developer/appversionadd";
		}
		appInfoService.updateAppVersionId(appVersion.getId(),appVersion.getAppId());//修改AppVersion新增的版本为最新版本
		return "redirect:list";
	}

	@RequestMapping("/appversionmodify/{vid}/{aid}")	//vid是版本id，aid是软件id，跳转到App版本修改页面
	public String toAppVersionModify(Model model,@PathVariable("vid")Integer id,@PathVariable("aid")Integer appId){
		AppVersion appVersion = appVersionService.queryAppVersionByIdandAppId(id, appId);
		List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appId);
		model.addAttribute("appVersionList",appVersionList);
		model.addAttribute("appVersion", appVersion);
		return "developer/appversionmodify";
	}

	@RequestMapping("/appversionmodifysave")	//App版本修改
	public String appVersionModifySave(AppVersion updateAppVersion,Model model,HttpServletRequest request){
		DevUser devUser = (DevUser)request.getSession().getAttribute(Constants.DEVUSER_SESSION);
		updateAppVersion.setModifyBy(devUser.getId());
		updateAppVersion.setModifyDate(new Date());
		int result = appVersionService.updateAppVersion(updateAppVersion);
		if(result<=0){
			AppVersion appVersion = appVersionService.queryAppVersionByIdandAppId(updateAppVersion.getId(),updateAppVersion.getAppId());
			model.addAttribute("appVersion", appVersion);
			List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appVersion.getAppId());
			model.addAttribute("appVersionList",appVersionList);
			return "developer/appversionmodify";
		}
		return "redirect:list";
	}

	@RequestMapping("/appinfomodify/{id}") //跳转到App信息修改页面
	public String toAppinfomodify(Model model,@PathVariable("id")Integer id){
		AppInfo appInfo = appInfoService.queryAppViewById(id);
		model.addAttribute("appInfo",appInfo);
		return "developer/appinfomodify";
	}

	@RequestMapping("/delfile.json")	//删除AppLogo图片
	@ResponseBody
	public HashMap<String,String> delFile(@Param("id")Integer id){
		HashMap<String,String> data = new HashMap<String,String>();
		AppInfo appInfo = appInfoService.queryAppInfoById(id);
		File logoImg = new File(appInfo.getLogoLocPath());
		if(logoImg.exists()){
			logoImg.delete();
		}
		int result = appInfoService.updateLogoPathById(id);
		if(result>0){
			data.put("result","success");
		}else{
			data.put("result","failed");
		}
		return data;
	}

	@RequestMapping("/infomodifysave")	//APP信息修改
	public String infoModifySave(AppInfo updateAppInfo,Model model,@RequestParam("attach")MultipartFile attach,HttpServletRequest request){
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+java.io.File.separator+"uploadfiles");
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			int filesize = 50000;
			if(attach.getSize()>filesize){
				request.setAttribute("fileUploadError","* 上传大小不得超过 500KB");
				return "developer/appinfoadd";
			}else if(prefix.equalsIgnoreCase("jpg") ||
					prefix.equalsIgnoreCase("png") ||
					prefix.equalsIgnoreCase("jpeg") ||
					prefix.equalsIgnoreCase("pneg")){
				String fileName = updateAppInfo.getAPKName()+".jpg";
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError","* 上传失败！");
					AppInfo appInfo = appInfoService.queryAppInfoById(updateAppInfo.getId());
					model.addAttribute("appInfo",appInfo);
					return "developer/appinfomodify";
				}
				updateAppInfo.setLogoLocPath(path+File.separator+fileName);
				updateAppInfo.setLogoPicPath("/"+path.substring(path.lastIndexOf(request.getContextPath().substring(1))).replace("\\","/")+"/"+fileName);
			}else{
				request.setAttribute("fileUploadError","* 上传图片格式不正确");
				AppInfo appInfo = appInfoService.queryAppInfoById(updateAppInfo.getId());
				model.addAttribute("appInfo",appInfo);
				return "developer/appinfomodify";
			}
		}
		DevUser devUser = (DevUser)request.getSession().getAttribute(Constants.DEVUSER_SESSION);
		updateAppInfo.setModifyBy(devUser.getId());
		updateAppInfo.setModifyDate(new Date());
		int result = appInfoService.updateAppInfo(updateAppInfo);
		if(result<=0){
			AppInfo appInfo = appInfoService.queryAppInfoById(updateAppInfo.getId());
			model.addAttribute("appInfo",appInfo);
			return "developer/appinfomodify";
		}
		return "redirect:list";
	}

	@RequestMapping("/appview/{appinfoid}")	//跳转到查询App信息，查出App信息，和历史版本信息
	public String appView(@PathVariable("appinfoid")Integer appinfoid,Model model){
		AppInfo appInfo = appInfoService.queryAppViewById(appinfoid);
		model.addAttribute("appInfo", appInfo);
		List<AppVersion> appVersionList = appVersionService.queryAllAppVersionsByAppId(appinfoid);
		model.addAttribute("appVersionList",appVersionList);
		return "developer/appinfoview";
	}

	@RequestMapping("/delapp.json")	//删除App信息及App的历史版本
	@ResponseBody
	public HashMap<String, String> deleteApp(@RequestParam("id")Integer id){
		HashMap<String, String> data = new HashMap<String,String>();
		int result = appInfoService.deleteAppInfoById(id);
		if(id==null || id==0){
			data.put("delResult","notexist");
		}else{
			if(result>0){
				appVersionService.deleteAppVersionByAppId(id);
				data.put("delResult","true");
			}else{
				data.put("delResult","false");
			}
		}
		return data;
	}

	@RequestMapping(value="/sale.json/{status}/{id}",method=RequestMethod.PUT)	//上架下架
	@ResponseBody
	public Object Sale(@PathVariable("status")Integer status,@PathVariable("id")Integer id){
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("errorCode", "0");
		int result;
		try {
			result = appInfoService.updateStatusById(status, id);
			if(result>0){
				data.put("resultMsg","success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("resultMsg","failed");
		}
		return data;
	}
}
