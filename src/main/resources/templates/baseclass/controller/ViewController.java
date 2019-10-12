package ${package.Controller};

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("${package.Controller}.ViewController")
@RequestMapping("/")
public class ViewController {

	@RequestMapping("/view/{file}")
	public String view(@PathVariable String file) throws Exception{
		System.out.println("view1 |file= /"+file);
		//加上forward关键字不受mvc解析器解析
		return file;
	}
	@RequestMapping("/view/{folder}/{file}")
	public String view(@PathVariable String folder,@PathVariable String file) throws Exception{
		System.out.println("view2 |file= /"+folder+"/"+file);
		return folder+"/"+file;
	}
	@RequestMapping("/view/{folder1}/{folder2}/{file}")
	public String view(@PathVariable String folder1,@PathVariable String folder2,@PathVariable String file) throws Exception{
		System.out.println("view3 |file= /"+folder1+"/"+folder2+"/"+file);
		return folder1+"/"+folder2+"/"+file;
	}
	@RequestMapping("/view/{folder1}/{folder2}/{folder3}/{file}")
	public String view(@PathVariable String folder1,@PathVariable String folder2,@PathVariable String folder3,@PathVariable String file) throws Exception{
		System.out.println("view4 |file= /"+folder1+"/"+folder2+"/"+folder3+"/"+file);
		return folder1+"/"+folder2+"/"+folder3+"/"+file;
	}
	
    @RequestMapping()
    public String index() throws Exception{
        return "index";
    }

    @RequestMapping("/index")
    public String index(String param) throws Exception{
        return "index";
    }

    @RequestMapping("/login")
    public String login(String param) throws Exception{
        return "login";
    }

    @RequestMapping("/admin")
    public String adminIndex(String param) throws Exception{
        return "redirect:/${cfg.loginTable}/index";
    }

    @RequestMapping(value = "/admin/login",method = {RequestMethod.GET})
    public String adminLogin(String param) throws Exception{
        return "admin/login";
    }
    
    @RequestMapping("/logout")
    public String logout(HttpSession session) throws Exception{
    	session.invalidate();
        return "redirect:/${cfg.loginTable}/index";
    }
}
