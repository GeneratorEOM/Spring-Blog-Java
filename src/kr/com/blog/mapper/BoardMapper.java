package kr.com.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import kr.co.blog.beans.ContentBean;

public interface BoardMapper {

	// insert �������� ����Ǳ����� ���� ����Ǽ� content_idx �� ���� ������ �� �ִ�
	@SelectKey(statement = "SELECT content_seq.nextval FROM dual", keyProperty = "content_idx", before = true, resultType = Integer.class)
	
	@Insert("INSERT INTO content_table(content_idx, content_subject, content_text, "
		  + "content_file, content_writer_idx, content_board_idx, content_date) "
		  + "VALUES (#{content_idx}, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, "
		  + "#{content_writer_idx}, #{content_board_idx}, sysdate)")
	void addContentInfo(ContentBean writeContentBean);
	
	@Select("SELECT board_info_name FROM board_info_table WHERE board_info_idx = #{board_info_idx}")
	String getBoardInfoName(int board_info_idx);
	
	@Select("SELECT a1.content_idx, a1.content_subject, a2.user_name AS content_writer_name, "
		  + "TO_CHAR(a1.content_date, 'YYYY-MM-DD') AS content_date "
		  + "FROM content_table a1 JOIN user_table a2 "
		  + "ON a1.content_writer_idx = a2.user_idx "
		  + "WHERE a1.content_board_idx = #{content_board_idx} "
		  + "ORDER BY a1.content_idx DESC")
	List<ContentBean> getContentList(int board_info_idx, RowBounds rowBounds);
	
	@Select("SELECT a2.user_name AS content_writer_name, TO_CHAR(a1.content_date, 'YYYY-MM-DD') AS content_date, "
		  + "		a1.content_subject, a1.content_text, a1.content_file, a1.content_writer_idx "
		  + "FROM content_table a1 JOIN user_table a2 "
		  + "ON a1.content_writer_idx = a2.user_idx "
//		  + "WHERE a1.content_board_idx = #{content_board_idx} "
//		  + "WHERE a1.content_board_idx = 1 "
		  + "WHERE a1.content_idx = ${content_idx}")
	ContentBean getContentInfo(int content_idx);
	
	@Update("UPDATE content_table "
		  + "SET content_subject = #{content_subject}, content_text = #{content_text}, "
		  + "content_file = #{content_file, jdbcType=VARCHAR} "
		  + "WHERE content_idx = #{content_idx}")
	void modifyContentInfo(ContentBean modifyContentBean);
	
	@Delete("DELETE FROM content_table WHERE content_idx = #{content_idx}")
	void deleteContentInfo(int content_idx);
	
	@Select("SELECT count(*) FROM content_table WHERE content_board_idx = #{content_board_idx}")
	int getContentCnt(int content_board);
	
}
