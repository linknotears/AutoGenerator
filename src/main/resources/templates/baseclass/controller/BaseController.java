package ${package.Controller};

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("${package.Controller}.BaseController")
@RequestMapping("${cfg.suffixUri}base")
public class BaseController {
	@Autowired
	protected ServletContext servletContext;
}
