package ${cfg.basePackage}.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DatetimeConverter  implements Converter<String, Date> {
	@Override    
	public Date convert(String source) {    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
	    dateFormat.setLenient(true);    
	    try {    
	        return dateFormat.parse(source);
	    } catch (ParseException e) {    
	    	try {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormat.parse(source);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
	    }          
	    return null;    
	}
}
