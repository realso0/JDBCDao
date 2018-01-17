package main;

import java.util.List;

import com.javaex.dao.AuthorDao;
import com.javaex.vo.AuthorVo;

public class MainApp {

	public static void main(String[] args) {
		AuthorVo vo=new AuthorVo("안녕","28년");
		
		AuthorDao aDao=new AuthorDao();
		
		aDao.insertAuthor(vo); //db
		
		List<AuthorVo> authorList=aDao.selectAuthorList();
		//aDao.selectAuthor(); //aDao는 데이터를 가져옴.
		//vo로 필드 정리해준다.
		//select는 리스트로 받는다.
		for(AuthorVo aulist : authorList) {
			System.out.println(aulist.getAuthorId()+aulist.toString());

	}

}
