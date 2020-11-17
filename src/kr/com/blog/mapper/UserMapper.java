package kr.com.blog.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.blog.beans.UserBean;

public interface UserMapper {
	
	@Select ("SELECT user_name " +
			 "FROM user_table " + 
			 "WHERE user_id = #{user_id}")
	String checkUserIdExist(String user_id);
	
	@Insert("INSERT INTO user_table (user_idx, user_name, user_id, user_pw) " +
	        "VALUES (user_seq.nextval, #{user_name}, #{user_id}, #{user_pw})")
	void addUserInfo(UserBean jounUserBean);
	
	@Select("SELECT user_idx, user_id "
		  + "FROM user_table "
		  + "WHERE user_id = #{user_id} and user_pw = #{user_pw}")
	UserBean getLoginUserInfo(UserBean tempLoginUserBean);
	
	@Select("SELECT user_id, user_name "
		  + "FROM user_table "
		  + "WHERE user_idx = #{user_idx}")
	UserBean getModifyUserInfo(int user_idx);
	
	@Update("UPDATE user_table SET user_pw = #{user_pw} WHERE user_idx = #{user_idx}")
	void modifyUserBean(UserBean modifyUserBean);
}
