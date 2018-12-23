package cn.appsys.service.devuser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.devuser.DevUserMapper;
import cn.appsys.pojo.DevUser;

@Service
public class DevUserServiceImpl implements DevUserService{
	@Resource
	private DevUserMapper devMapper;
	
	@Override
	public DevUser queryDevCode(String devCode) {
		return devMapper.queryDevCode(devCode);
	}
	
}
