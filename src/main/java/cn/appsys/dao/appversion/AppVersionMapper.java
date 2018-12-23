package cn.appsys.dao.appversion;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {
	List<AppVersion> queryAllAppVersionsByAppId(@Param("id")Integer id);
	int addAppVersion(AppVersion appVersion);
	AppVersion queryAppVersionByIdandAppId(@Param("id")Integer id,@Param("appId")Integer appId);//id对应AppInfo表中的versionId，appId对应AppInfo表中得id
	int updateAppVersion(AppVersion appVersion);
	int deleteAppVersionByAppId(@Param("appId")Integer appId);
}
