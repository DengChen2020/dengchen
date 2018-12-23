package cn.appsys.dao.appcategory;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	List<AppCategory> queryInfoByParentId(@Param("parentId")Integer parentId);
}