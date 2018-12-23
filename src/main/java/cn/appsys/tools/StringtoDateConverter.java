package cn.appsys.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringtoDateConverter implements Converter<String,Date>{
	
	private String datePattern;
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	@Override
	public Date convert(String dateString) {
		Date date = null;
		try {
			date = new SimpleDateFormat(datePattern).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
