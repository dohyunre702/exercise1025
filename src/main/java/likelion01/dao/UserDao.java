package likelion01.dao;

import likelion01.domain.User;

import java.sql.*;
import java.util.Map;
import javax.sql.DataSource; //DataSource 추가

public class UserDao {

    private ConnectionMaker cm;
    private DataSource dataSource; //DataSource 의존하게 변경

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource; //생성자 변경
    }

    public UserDao() {
        this.cm = new AwsConnectionMaker();
    }

    public UserDao(ConnectionMaker cm) {
        this.cm = cm;
    }

    public Object jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            //db 접속
            c = dataSource.getConnection(); //datasource 수정
            ps = stmt.makePreparedStatement(c);
            //query문 실행
            int status = ps.executeUpdate();
            ps.close();


        } catch (SQLException e) {
        } finally {
            //errror가 나도 실행되는 블럭
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }


    public User findById(String id) {

        Map<String, String> env = System.getenv();
        try {
            //DB접속
            Connection c = dataSource.getConnection(); //datasource 수정
            //query문 작성
            PreparedStatement pstmt = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setString(1, id);

            //query문 실행
            ResultSet rs = pstmt.executeQuery(); //ResultSet?
            User user = null;
            // rs.next(); null;
            if (rs.next()) {
                user = new User(rs.getString("id"), rs.getString("name"), rs.getString("name"));
            }
            rs.close();
            pstmt.close();
            c.close();

            //데이터 생길 때 생기는 예외 처리하기
            if(user==null) throw new EmptyResultDataAccessExcpetion(1);
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //익명 클래스
    public void add(User user) throws SQLException {
        AddStrategy addStrategy = new AddStrategy(user);
        jdbcContextWithStatementStrategy(addStrategy);
    }

    //삭제
    public void deleteAll() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = dataSource.getConnection(); //datasource 수정
            ps = new DeleteAllStrategy().makePreparedStatement(c);
            rs = ps.executeQuery();
            rs.next(); //쿼리 업데이트

            //c, ps, rs가 null일 때 리소스 반환이 불가해 서버가 down되는 치명적 문제 발생
            //connection, preparedStatement에서 에러가 나도 ps.close() c.close()를 실행하기 위한 처리
        } catch (SQLException e) {
            throw e;
        } finally {//errror가 나도 실행되는 블럭
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}

        //쿼리 count
        public int getCount() throws SQLException {
            Connection c = null;
            PreparedStatement psCnt = null;
            ResultSet rs = null;
            try {
                c = dataSource.getConnection(); //datasource 수정
                psCnt = c.prepareStatement("SELECT COUNT(*) FROM users;"); //쿼리 입력
                rs = psCnt.executeQuery();
                rs.next(); //쿼리 업데이트
                return rs.getInt(1);

            } catch (SQLException e) {
                throw e;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                    }
                    if (psCnt != null) {
                        try {
                            psCnt.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (c != null) {
                        try {
                            c.close();
                        } catch (SQLException e) {
                        }
                    }

                }
            }
        }
            public static void main (String[]args){
                UserDao userDao = new UserDao();
                //userDao.add(new User("7", "Ruru", "1234"));
                User user = userDao.findById("4");
                System.out.println(user.getName());
            }

        }