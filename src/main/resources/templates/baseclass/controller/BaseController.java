package ${package.Controller};

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("${package.Controller}.BaseController")
@RequestMapping("${cfg.suffixUri}/base")
public class BaseController {
	@Autowired
	protected ServletContext servletContext;
	
	@RequestMapping("/goto/{folder}/{file}")
	public String goUrl(@PathVariable String folder,@PathVariable String file) throws Exception{
		System.out.println("goUrl.folder|file= /"+folder+"/"+file);
		return folder+"/"+file;
	}
	@RequestMapping("/goto/{file}")
	public String goUrl(@PathVariable String file) throws Exception{
		System.out.println("goUrl.folder|file= /"+file);
		//加上forward关键字不受mvc解析器解析
		return file;
	}
}
