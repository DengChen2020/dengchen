package cn.appsys.service.appversion;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
@Service
public class AppVersionServiceImpl implements AppVersionService{
	@Resource
	private AppVersionMapper appVersionMapper;
	@Override
	public List<AppVersion> queryAllAppVersionsByAppId(Integer id) {
		return appVersionMapper.queryAllAppVersionsByAppId(id);
	}
	@Override
	public int addAppVersion(AppVersion appVersion) {
		return appVersionMapper.addAppVersion(appVersion);
	}
	@Override
	public AppVersion queryAppVersionByIdandAppId(Integer id, Integer appId) {
		return appVersionMapper.queryAppVersionByIdandAppId(id, appId);
	}
	@Override
	public int updateAppVersion(AppVersion appVersion) {
		return appVersionMapper.updateAppVersion(appVersion);
	}
	@Override
	public int deleteAppVersionByAppId(Integer appId) {
		return appVersionMapper.deleteAppVersionByAppId(appId);
	}
	
}
