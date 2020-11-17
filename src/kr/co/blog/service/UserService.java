package kr.co.blog.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.blog.beans.UserBean;
import kr.co.blog.dao.UserDAO;

@Service
public class UserService {

	@Autowired
	UserDAO userDAO;
	
	@Resource(name = "loginUserBean")
	private UserBean loginUserBean;
	
	public boolean userCheckIdExist(String user_id) {
		
		String userCheck = userDAO.checkUserIdExist(user_id);
			
		if(userCheck == null) {			
			return true;			
		}else {			
			return false;
		}		 
	}
	
	public void addUserInfo(UserBean joinUserBean) {
		userDAO.addUserInfo(joinUserBean);
	}
	
	public void getLoginUserInfo(UserBean tempLoginUserBean) {
		
		UserBean tempLoginUserBean2 = userDAO.getLoginUserInfo(tempLoginUserBean);
		
		if(tempLoginUserBean2 != null) {
			loginUserBean.setUser_idx(tempLoginUserBean2.getUser_idx());
			loginUserBean.setUser_name(tempLoginUserBean2.getUser_name());
			loginUserBean.setUserLogin(true);
		}
	}
	
	public void getModifyUserBean(UserBean modifyUserBean) {
		
		UserBean tempModifyUserBean = userDAO.getModifyUserInfo(loginUserBean.getUser_idx()); 
		
		modifyUserBean.setUser_idx(tempModifyUserBean.getUser_idx());
		modifyUserBean.setUser_id(tempModifyUserBean.getUser_id());
		modifyUserBean.setUser_name(tempModifyUserBean.getUser_name());

	}
	
	public void modifyUserInfo(UserBean modifyUserBean) {
		modifyUserBean.setUser_idx(loginUserBean.getUser_idx());
		userDAO.modifyUserInfo(modifyUserBean);
	}
}

