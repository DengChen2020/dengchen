package cn.appsys.service.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	List<AppCategory> queryInfoByParentId(Integer parentId);
}
