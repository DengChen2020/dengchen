package cn.appsys.dao.appinfo;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	int queryAppInfoCount(@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,
			@Param(value="categoryLevel1")Integer queryCategoryLevel1,
			@Param(value="categoryLevel2")Integer queryCategoryLevel2,
			@Param(value="categoryLevel3")Integer queryCategoryLevel3,
			@Param(value="flatformId")Integer queryFlatformId);

	List<AppInfo> queryByPage(@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,
			@Param(value="categoryLevel1")Integer queryCategoryLevel1,
			@Param(value="categoryLevel2")Integer queryCategoryLevel2,
			@Param(value="categoryLevel3")Integer queryCategoryLevel3,
			@Param(value="flatformId")Integer queryFlatformId,
			@Param(value="devId")Integer devId,
			@Param(value="from")Integer from,
			@Param(value="pageSize")Integer pageSize);	//条件查询App信息并分页

	int queryIsExistApkName(@Param("APKName")String APKName); //查询APKName是否已存在
	AppInfo queryAppInfoById(@Param("id")Integer id);
	int addAPKInfo(AppInfo appInfo);
	int updateAppInfo(AppInfo appInfo);
	int updateAppVersionId(@Param("versionId")Integer id,@Param("id")Integer appId);//versionId是AppVersion表中的id，id是AppVersion表中的AppId
	int updateLogoPathById(@Param("id")Integer id);//删除Logo图片文件
	AppInfo queryAppViewById(@Param("id")Integer id);
	int deleteAppInfoById(@Param("id")Integer id);
	int updateStatusById(@Param("status")int status,@Param("id")Integer id);
}
