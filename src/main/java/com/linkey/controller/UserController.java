package com.linkey.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkey.entity.User;
import com.linkey.entity.vo.PageData;
import com.linkey.entity.vo.ResultData;
import com.linkey.service.UserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linkey
 * @since 2019-03-09
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/saveOrUpdate")//更新不置空
	public @ResponseBody ResultData saveOrUpdate(@RequestParam(value="id",required=false) Object id,User user){
		ResultData resultData = new ResultData();
		int i = userService.saveOrUpdate(id, user);
		if(i > 0){
			resultData.setCode(-1);
		}
		resultData.setData("count", i);
		return resultData;
	}
	
	@RequestMapping("/update")//允许置空
	public @ResponseBody ResultData update(User user){
		ResultData resultData = new ResultData();
		int i = userService.updateById(user);
		if(i > 0){
			resultData.setCode(-1);
		}
		resultData.setData("count", i);
		return resultData;
	}
	
	@RequestMapping("/remove")
	public @ResponseBody ResultData remove(Object id){
		ResultData resultData = new ResultData();
		int i = userService.removeById(id);
		if(i > 0){
			resultData.setCode(-1);
		}
		resultData.setData("count", i);
		return resultData;
	}
	
	@RequestMapping("/findAll")
	public @ResponseBody ResultData findAll(){
		ResultData resultData = new ResultData();
		List<User> users = null;
		try {
			users = userService.findList(null);
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("users", users);
		return resultData;
	}

	@RequestMapping("/findById")
	public @ResponseBody ResultData findById(Object id){
		ResultData resultData = new ResultData();
		User user = null;
		try {
			user = userService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		resultData.setData("user", user);
		return resultData;
	}
	
	@RequestMapping("/findPage")
	public @ResponseBody ResultData findPage(PageData<User> pageData,User condition){
		ResultData resultData = new ResultData();
		
		List<User> users = null;
		try {
			pageData = userService.findPage(pageData, condition);
			users = pageData.getList();
		} catch (Exception e) {
			e.printStackTrace();
			resultData.setCode(-1);
		}
		
		resultData.setData("users", users);
		resultData.setData("pageData", pageData);
		return resultData;
	}
}
