package cn.appsys.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	@Resource
	private JavaMailSender javaMailSender; 
	
	@ResponseBody
	@RequestMapping("/hello")
	public Object hello(HttpSession session) {
		/*return "<p style='color:red'>大神</p>";*/
		/*HashMap<String, String> map = new HashMap<String, String>();
		map.put("dc","大神");*/
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("15526223019@163.com");//发送者账号
		message.setTo("1661904306@qq.com");//接收者账号
		message.setSubject("验证您的Debug账号"); //邮件的标题
		int num = (int) (Math.random()*1000000); //生成六位数的随机数
		message.setText("邓郴 ，您好！\n为确保是您本人操作，请在邮件验证码输入框输入下方验证码：\n"+num+"\n此验证码5分钟内有效，如验证码失效请到网站重新操作！\n请勿向任何人泄露您收到的验证码。\n此致\nDebug");//邮件内容
		try {
			javaMailSender.send(message); //发送邮件
		} catch (MailException e) {
			return "发送失败";
		}
		session.setAttribute("CheckNum",num);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				session.removeAttribute("CheckNum");
			}
		}, 1*60*1000);
		return "发送成功";
		/*return num;*/
	}
	
	@ResponseBody
	@RequestMapping("/yzm")
	public Object yzm(HttpSession session,@RequestParam("checkCode")String code) {
		HashMap<String, String> data = new HashMap<String, String>();
		if(session.getAttribute("CheckNum")==null) {
			data.put("result","timeout");
		}else {
			if(code.equals(session.getAttribute("CheckNum"))) {
				data.put("result","true");
			}else {
				data.put("result","false");
			}
		}
		return data;
	}
}
