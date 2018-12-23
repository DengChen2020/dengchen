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
import cn.appsys.pojo.DevUser;
import cn.appsys.pojo.Page;
import cn.appsys.service.appcategory.AppCategoryService;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.backenduser.BackendUserService;
import cn.appsys.service.datadictionary.DataDictionaryService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/manager")
public class ManageController {
	@Resource
	private BackendUserService backendUserService;

	@RequestMapping("/login")
	public String toLogin(){	//跳转到登录页面
		return "backendlogin";
	}

	@RequestMapping("/dologin")
	public String login(BackendUser backendUser,Model model,HttpSession session){	//执行登录
		BackendUser queryBackUser = backendUserService.queryuserCode(backendUser.getUserCode());
		if(queryBackUser==null){
			model.addAttribute("error","用户名不存在");
			return "backendlogin";
		}else{
			if(queryBackUser.getUserPassword().equals(backendUser.getUserPassword())){
				session.setAttribute(Constants.USER_SESSION,queryBackUser);
				return "backend/main";
			}else{
				model.addAttribute("error","密码不正确");
				return "backendlogin";
			}
		}
	}
	
	@RequestMapping("/logout")
	public String logOut(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
}
