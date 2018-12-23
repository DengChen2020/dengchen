package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;

public class SysInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		DevUser devUser = (DevUser) session.getAttribute(Constants.DEVUSER_SESSION);
		if(devUser == null){
			response.sendRedirect(request.getContextPath()+"/403.html");
			return false;
		}
		return true;
	}
}
