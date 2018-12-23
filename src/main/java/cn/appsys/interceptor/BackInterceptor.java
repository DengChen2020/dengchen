package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;

public class BackInterceptor extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		BackendUser backedUser = (BackendUser) session.getAttribute(Constants.USER_SESSION);
		if(backedUser == null){
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}
