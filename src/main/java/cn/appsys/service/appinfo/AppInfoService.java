package cn.appsys.service.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	int queryAppInfoCount(String querySoftwareName,
			Integer queryStatus,
			Integer queryCategoryLevel1,
			Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,
			Integer queryFlatformId);

	List<AppInfo> queryByPage(String querySoftwareName,
			Integer queryStatus,
			Integer queryCategoryLevel1,
			Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,
			Integer queryFlatformId,
			Integer devId,
			Integer from,
			Integer pageSize);	//条件查询App信息并分页
	int queryIsExistApkName(String APKName);
	
	AppInfo queryAppInfoById(Integer id);
	
	int addAPKInfo(AppInfo appInfo);
	
	int updateAppInfo(AppInfo appInfo);
	int updateAppVersionId(Integer id,Integer appId);//versionId是AppVersion表中的id，id是AppVersion表中的AppId
	int updateLogoPathById(Integer id);//删除Logo图片文件
	AppInfo queryAppViewById(Integer id);
	int deleteAppInfoById(Integer id);
	int updateStatusById(int status,Integer id);
}
