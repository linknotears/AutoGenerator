package ${package.Controller};

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller("${package.Controller}.BaseController")
@RequestMapping("${cfg.suffixUri}base")
public class BaseController {
	@Autowired
	protected ServletContext servletContext;
	
	public String getDateNo(String dateNo,Integer preZeroCount){
        preZeroCount = preZeroCount != null? preZeroCount : 4;
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String oldDateNoPrefix = null;
        if(dateNo != null) {
            oldDateNoPrefix = dateNo.substring(0,8);
        }
        String newDateNoPrefix = sf.format(now);
        int squence =  1;
        if(newDateNoPrefix.equals(oldDateNoPrefix)){
            squence =  Integer.parseInt(dateNo.substring(8)) + 1;
            String squNo = String.format("%0"+preZeroCount+"d",squence);
            dateNo = newDateNoPrefix + squNo;
        }else{
            String squNo = String.format("%0"+preZeroCount+"d",squence);
            dateNo = newDateNoPrefix + squNo;
        }
        return dateNo;
    }
	
    public String getSquNo(String squNo,Integer preZeroCount){
        preZeroCount = preZeroCount != null? preZeroCount : 4;
        int squence = Integer.parseInt(squNo) + 1;
        squNo = String.format("%0"+preZeroCount+"d",squence);
        return squNo;
    }
}
