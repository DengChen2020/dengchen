package cn.appsys.service.backenduser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
@Service
public class BackendUserServiceImpl implements BackendUserService{
	@Resource
	private BackendUserMapper backendUserMapper;
	@Override
	public BackendUser queryuserCode(String userCode) {
		return backendUserMapper.queryuserCode(userCode);
	}
	
}
