package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BookVo;

public class BookDao {
	
	public void insertBook(BookVo vo1) {
		// 0. import java.sql.*;
				Connection conn = null; // 연결 잘됬는지 여부 때문에 필요
				PreparedStatement pstmt = null; // 쿼리문 관련
				// ResultSet rs = null; //select문 할때만 필요

				try {
					// 1. JDBC 드라이버 (Oracle) 로딩
					Class.forName("oracle.jdbc.driver.OracleDriver");

					// 2. Connection 얻어오기
					String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 연결된 ip주소, 포트
					conn = DriverManager.getConnection(url, "webdb", "webdb"); // 연결 url, 아이디, 비밀번호

					// 3. SQL문 준비 / 바인딩 / 실행
					String query = "INSERT INTO book VALUES (seq_book_id.nextval, ?, ?, ?, ?)"; // 문자열화 시켜줌. 바뀌는 값인 부분은 물음표로 지정
					pstmt = conn.prepareStatement(query);

					pstmt.setString(1, vo1.getTitle()); // 첫번째 물음표에 String(문자열) 값 입력
					pstmt.setString(2, vo1.getPubs()); // 두 번째 물음표에 들어갈 값 입력
					pstmt.setString(3, vo1.getPubDate());
					pstmt.setInt(4, vo1.getAuthorId());

					int count = pstmt.executeUpdate(); // 따로 적어준 값들을 조합해주어, DB로 날려주게 됨, int값을 반환하는 메소드(insert, update, delete)
					// 4.결과처리
					System.out.println(count + "건 저장완료");// 현재 우리는 insert 1개를 실행 하였음

				} catch (ClassNotFoundException e) {
					System.out.println("error: 드라이버 로딩 실패 - " + e);
				} catch (SQLException e) {
					System.out.println("error:" + e);
				} finally {
					// 5. 자원정리
					try {
						// if (rs != null) {
						// rs.close();
						// }
						if (pstmt != null) {
							pstmt.close();
						}
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException e) {
						System.out.println("error:" + e);
					}

				}

	}

	public List<BookVo> selectBookList() {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 이전까지는 while문에서 괄호 밖으로 나가면 값이 사라짐. 이를 해결하기 위해, 밖에서 리스트 생성
		List<BookVo> bookList = new ArrayList<BookVo>();

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 얻어오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 연결된 ip주소, 포트
			conn = DriverManager.getConnection(url, "webdb", "webdb"); // 연결 url, 아이디, 비밀번호

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " select a.author_id, " + 
							"        a.author_name, " + 
							"        a.author_desc, " + 
							"        b.book_id, " + 
							"        b.title, " + 
							"        b.pubs, " + 
							"        b.pub_date " + 
							" from author a, book b " + 
							" where a.author_id=b.author_id "; // *로 필드명을 모두 지시하면 안된다.(문자열화 시켜줌. 바뀌는 값인 부분은 물음표로 지정)
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery(); // 결과값으로 쿼리의 주소값을 rs에 저장하였고, rs객체가 생성되었음.

			// 4.결과처리
			while (rs.next()) { // 처음 시작시 커서가 맨 윗줄(열이름이 있는 행)에서 다음 행으로 이동, 다음 행이 있는 동안에 while문을 수행
				// 리스트의 각 인덱스 값들은 각 객체에 들어가야 하므로, 인덱스마다 객체를 생성
				BookVo vo1 = new BookVo();
				
				int authorId = rs.getInt("author_id");
				String authorName = rs.getString("author_name");
				String authorDesc = rs.getString("author_desc");
				int bookId = rs.getInt("book_id");
				String title = rs.getString("title");
				String pubs = rs.getString("pubs");
				String pubDate = rs.getString("pub_date");
				
				vo1.setAuthorId(authorId); // 각 인덱스마다 값 입력
				vo1.setAuthorName(authorName);
				vo1.setAuthorDesc(authorDesc);
				vo1.setBookId(bookId);
				vo1.setTitle(title);
				vo1.setPubs(pubs);
				vo1.setPubDate(pubDate);
				
				bookList.add(vo1); // 리스트와 각 객체를 연결

				System.out.println(authorId + " " + authorName + " " + authorDesc); // 한 줄씩 내려가면서 authorName값을 출력한다.

			}

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		return bookList; // list 결과값 출력
	}
}
