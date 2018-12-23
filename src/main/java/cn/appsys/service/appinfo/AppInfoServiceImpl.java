package cn.appsys.service.appinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.pojo.AppInfo;
@Service
public class AppInfoServiceImpl implements AppInfoService{
	@Resource
	private AppInfoMapper appInfoMapper;

	@Override
	public int queryAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId) {
		return appInfoMapper.queryAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId);
	}

	@Override
	public List<AppInfo> queryByPage(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId,Integer devId, Integer from, Integer pageSize) {
		return appInfoMapper.queryByPage(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,devId, from, pageSize);
	}

	@Override
	public int queryIsExistApkName(String APKName) {
		return appInfoMapper.queryIsExistApkName(APKName);
	}

	@Override
	public int addAPKInfo(AppInfo appInfo) {
		return appInfoMapper.addAPKInfo(appInfo);
	}

	@Override
	public AppInfo queryAppInfoById(Integer id) {
		return appInfoMapper.queryAppInfoById(id);
	}

	@Override
	public int updateAppInfo(AppInfo appInfo) {
		return appInfoMapper.updateAppInfo(appInfo);
	}

	@Override
	public int updateAppVersionId(Integer id, Integer appId) {
		return appInfoMapper.updateAppVersionId(id, appId);
	}

	@Override
	public int updateLogoPathById(Integer id) {
		return appInfoMapper.updateLogoPathById(id);
	}

	@Override
	public AppInfo queryAppViewById(Integer id) {
		return appInfoMapper.queryAppViewById(id);
	}

	@Override
	public int deleteAppInfoById(Integer id) {
		return appInfoMapper.deleteAppInfoById(id);
	}

	@Override
	public int updateStatusById(int status, Integer id) {
		return appInfoMapper.updateStatusById(status, id);
	}
	
}
