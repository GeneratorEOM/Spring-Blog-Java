package kr.co.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.blog.beans.BoardInfoBean;
import kr.co.blog.beans.ContentBean;
import kr.co.blog.service.MainService;
import kr.co.blog.service.TopMenuService;

@Controller
public class MainController {
	
	@Autowired
	private TopMenuService topMenuService;
	
	@Autowired
	private MainService mainService;
	
	@GetMapping("/main")
	public String main(Model model) {
		
		ArrayList<List<ContentBean>> list = new ArrayList<List<ContentBean>>();
		
		for(int i=1;i<=4;i++) {
			List<ContentBean> list1 = mainService.getMainList(i);
			list.add(list1);
		}
		model.addAttribute("list", list);
		
		List<BoardInfoBean> board_list = topMenuService.getTopMenuList();
		model.addAttribute("board_list", board_list);
		
		return "main";
	}
	
	
}
