package kr.co.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.blog.beans.BoardInfoBean;
import kr.co.blog.dao.TopMenuDAO;

@Service
public class TopMenuService {

	@Autowired
	private TopMenuDAO topMenuDAO;
	
	public List<BoardInfoBean> getTopMenuList() {
		List<BoardInfoBean> topMenuList = topMenuDAO.getTopMenuList();
		return topMenuList;
	}
}
