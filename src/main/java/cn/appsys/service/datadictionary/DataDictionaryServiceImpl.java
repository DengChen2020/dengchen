package cn.appsys.service.datadictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.datadictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{
	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	@Override
	public List<DataDictionary> queryInfoByTypeCode(String typeCode) {
		return dataDictionaryMapper.queryInfoByTypeCode(typeCode);
	}

}
