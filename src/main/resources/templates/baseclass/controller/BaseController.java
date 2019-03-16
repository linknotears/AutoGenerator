package ${package.Controller};

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/base")
public class BaseController {
	@Autowired
	protected ServletContext servletContext;
	
	@RequestMapping("/goto/{folder}/{file}")
	public String goUrl(@PathVariable String folder,@PathVariable String file) throws Exception{
		System.out.println("goUrl.folder|file= "+folder+"/"+file);
		return "forward:"+folder+"/"+file;
	}
	@RequestMapping("/goto/{file}")
	public String goUrl(@PathVariable String file) throws Exception{
		System.out.println("goUrl.folder|file= /"+file);
		return "forward:"+file;
	}
}
