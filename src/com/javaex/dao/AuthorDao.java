package com.javaex.dao;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.javaex.vo.AuthorVo;

//AuthorDao 생성
public class AuthorDao {
	// 디폴트 생성자 이용

	public void insertAuthor(AuthorVo vo) {
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
			String query = "INSERT INTO author VALUES (seq_author_id.nextval, ?, ?)"; // 문자열화 시켜줌. 바뀌는 값인 부분은 물음표로 지정
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getAuthorName()); // 첫번째 물음표에 String(문자열) 값 소한준 입력
			pstmt.setString(2, vo.getAuthorDesc()); // 매개변수로 받는 변수 이름을 적어준다.

			int count = pstmt.executeUpdate(); // 따로 적어준 값들을 조합해주어, DB로 날려주게 됨, int값을 반환하는 메소드
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

	public List<AuthorVo> selectAuthorList() {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 이전까지는 while문에서 괄호 밖으로 나가면 값이 사라짐. 이를 해결하기 위해, 밖에서 리스트 생성
		List<AuthorVo> authorList = new ArrayList<AuthorVo>();

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 얻어오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // 연결된 ip주소, 포트
			conn = DriverManager.getConnection(url, "webdb", "webdb"); // 연결 url, 아이디, 비밀번호

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " select author_id, " + " author_name, " + " author_desc " + // 콤마(,)와 띄어주는 것 안해주면 에러가 발생함.
					" from author "; // *로 필드명을 모두 지시하면 안된다.(문자열화 시켜줌. 바뀌는 값인 부분은 물음표로 지정)
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery(); // 결과값으로 쿼리의 주소값을 rs에 저장하였고, rs객체가 생성되었음.

			// 4.결과처리
			while (rs.next()) { // 처음 시작시 커서가 맨 윗줄(열이름이 있는 행)에서 다음 행으로 이동, 다음 행이 있는 동안에 while문을 수행
				// 리스트의 각 인덱스 값들은 각 객체에 들어가야 하므로, 인덱스마다 객체를 생성
				AuthorVo vo = new AuthorVo();
				int authorId = rs.getInt("author_id");
				String authorName = rs.getString("author_name");
				String authorDesc = rs.getString("author_desc");
				vo.setAuthorId(authorId); // 각 인덱스마다 값 입력
				vo.setAuthorName(authorName);
				vo.setAuthorDesc(authorDesc);

				authorList.add(vo); // 리스트와 각 객체를 연결

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
		return authorList; // list 결과값 출력
	}
}
