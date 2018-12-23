package cn.appsys.service.appcategory;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import cn.appsys.dao.appcategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
@Service
public class AppCategoryServiceImpl implements AppCategoryService{
	@Resource
	private AppCategoryMapper appCategoryMapper;

	@Override
	public List<AppCategory> queryInfoByParentId(Integer parentId) {
		return appCategoryMapper.queryInfoByParentId(parentId);
	}

}
