package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.devuser.DevUserService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/dev")
public class DevController {
	@Resource
	private DevUserService devUserService;

	@RequestMapping("/login")
	public String login(){
		return "devlogin";
	}

	@RequestMapping("dologin")
	public String dologin(@RequestParam("devCode")String devCode,@RequestParam("devPassword")String devPassword,Model model,HttpSession session){
		DevUser devUser = devUserService.queryDevCode(devCode);
		if(devUser==null){
			model.addAttribute("error","用户名不存在");
			return "devlogin";
		}else{
			if(!devUser.getDevPassword().equals(devPassword)){
				model.addAttribute("devCode",devCode);
				model.addAttribute("error","密码错误");
				return "devlogin";
			}else{
				session.setAttribute(Constants.DEVUSER_SESSION,devUser);
				System.out.println(Constants.DEVUSER_SESSION);
				return "developer/main";
			}
		}
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){ 
		session.removeAttribute(Constants.DEVUSER_SESSION);
		return "devlogin";
	}
}
