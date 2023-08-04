package ezen.ams.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RDB를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체
 * 
 * @author 현정환
 *
 */

public class JdbcAccountRepository implements AccountRepository{
	
//   나중에 property 파일로 변경
   private static String driver = "oracle.jdbc.driver.OracleDriver";
   private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
   private static String userid = "hr";
   private static String password = "hr";

   private Connection con;
   
   public JdbcAccountRepository() throws Exception {
      Class.forName(driver);
      con = DriverManager.getConnection(url, userid, password);
   }
   
   /**
    * 전체계좌 목록수 반환
    * @return 목록수
    * @throws SQLException 
    */
   public int getCount() {
      int count = 0;
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT count(*) count")
        .append(" FROM account");
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         pstmt = con.prepareStatement(sb.toString());
         rs = pstmt.executeQuery(); // select, show 등의 문을 실행
         if(rs.next()) {
            count = rs.getInt("count");
         }
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (Exception e) {

         }
      }
      return count;
   }
   /**
    * 
    * @return 전체계좌목록
    */
   public List<Account> getAccounts() {
      List<Account> list = null;
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT account_num, account_owner, passwd, restmoney, borrowmoney")
        .append(" FROM account");
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         con = DriverManager.getConnection(url,userid,password);
         pstmt = con.prepareStatement(sb.toString());
         rs = pstmt.executeQuery();
         list = new ArrayList<Account>();
         
         while(rs.next()) {
            String accountNum = rs.getString("account_num");
            String accountOwner = rs.getString("account_owner");
            int passwd = rs.getInt("passwd");
            Long restMoney = rs.getLong("restmoney");
            Long borrowMoney = rs.getLong("borrowmoney");
            if (borrowMoney != 0) {
               MinusAccount ma = new MinusAccount();
               ma.setAccountNum(accountNum);
               ma.setAccountOwner(accountOwner);
               ma.setPasswd(passwd);
               ma.setRestMoney(restMoney);
               ma.setBorrowMoney(borrowMoney);
               list.add(ma);
            } else {
               Account account = new Account();
               account.setAccountNum(accountNum);
               account.setAccountOwner(accountOwner);
               account.setPasswd(passwd);
               account.setRestMoney(restMoney);
               list.add(account);
            }
         }
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if(rs != null) rs.close();
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
         } catch (Exception e) {

         }
      }
      return list;
   }
   /**
    * 신규계좌 등록
    * @param account
    * @return 등록여부
    */
   public boolean addAccount(Account account) {
      boolean ok = false;
      StringBuilder sb = new StringBuilder();
      sb.append(" INSERT INTO account(account_num, account_owner, passwd, restmoney, borrowmoney)")
        .append(" VALUES(account_num_seq.NEXTVAL,?,?,?,?)");
      
      PreparedStatement pstmt = null;
      
      try {
         con = DriverManager.getConnection(url,userid,password);
         con.setAutoCommit(false);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, account.getAccountOwner());
         pstmt.setInt(2, account.getPasswd());
         pstmt.setLong(3, account.getRestMoney());
         
         if(account instanceof MinusAccount) {
            MinusAccount ma = (MinusAccount) account;
            pstmt.setLong(4, ma.getBorrowMoney());
         }else {
            pstmt.setLong(4, 0);
         }
         
         pstmt.executeUpdate(); // create, drop, insert, delete, update 등의 문을 처리
         con.commit();
         ok = true;
      } catch (SQLException e) {
         try {
            con.rollback();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
      } finally {
         try {
            if (pstmt != null)
               pstmt.close();
            if (con != null)
               con.close();
         } catch (Exception e) {
         }
         
      }
      
      return ok;
   }
   /**
    * 
    * @param accountNum 검색 계좌번호
    * @return 검색된 계좌
    */
   public Account searchAccount(String accountNum) {
      Account account = null;
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT account_num, account_owner, passwd, restmoney, borrowmoney")
        .append(" FROM account")
        .append(" WHERE account_num = ?");
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
         con = DriverManager.getConnection(url,userid,password);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, accountNum);
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            String acNum = rs.getString("account_num");
            String accountOwner = rs.getString("account_owner");
            int passwd = rs.getInt("passwd");
            Long restmoney = rs.getLong("restmoney");
            Long borrowmoney = rs.getLong("borrowmoney");
            
            if(borrowmoney != 0) {
               MinusAccount ma = new MinusAccount();
               ma.setAccountNum(acNum);
               ma.setAccountOwner(accountOwner);
               ma.setPasswd(passwd);
               ma.setRestMoney(restmoney);
               ma.setBorrowMoney(borrowmoney);
               account = ma;
            }else {
               account = new Account();
               account.setAccountNum(acNum);
               account.setAccountOwner(accountOwner);
               account.setPasswd(passwd);
               account.setRestMoney(restmoney);
               return account;
            }
         }
         
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if(rs != null) rs.close();
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
         } catch (Exception e) {

         }
      }
      return account;
   }
   
   /**
    * 
    * @param accountOwner 검색 예금주명
    * @return 검색된 예금주
    */
   public List<Account> searchAccountByOwner(String accountOwner) {
      List<Account> list = null;
      StringBuilder sb = new StringBuilder();
      sb.append(" SELECT account_num, account_owner, passwd, restmoney, borrowmoney")
        .append(" FROM account")
        .append(" WHERE account_owner = ?");
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         con = DriverManager.getConnection(url,userid,password);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, accountOwner);
         rs = pstmt.executeQuery();
         list = new ArrayList<>();
         
         while(rs.next()) {
            String accountNum = rs.getString("account_num");
            String acOwner = rs.getString("account_owner");
            int passwd = rs.getInt("passwd");
            Long restmoney = rs.getLong("restmoney");
            Long borrowmoney = rs.getLong("borrowmoney");
            
            if(borrowmoney != 0) {
               MinusAccount ma = new MinusAccount();
               ma.setAccountNum(accountNum);
               ma.setAccountOwner(acOwner);
               ma.setPasswd(passwd);
               ma.setRestMoney(restmoney);
               ma.setBorrowMoney(borrowmoney);
               list.add(ma);
            }else {
               Account account = new Account();
               account.setAccountNum(accountNum);
               account.setAccountOwner(acOwner);
               account.setPasswd(passwd);
               account.setRestMoney(restmoney);
               list.add(account);
            }
         }
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if(rs != null) rs.close();
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
         } catch (Exception e) {

         }
      }
      return list;
   }
   
   /**
    * 계좌삭제
    * @return
    */
   public boolean removeAccount(String accountNum) {
      boolean ok = false;
      StringBuilder sb = new StringBuilder();
      sb.append(" DELETE FROM account")
        .append(" WHERE account_num = ?");
      
      PreparedStatement pstmt = null;
      try {
         con = DriverManager.getConnection(url,userid,password);
         pstmt = con.prepareStatement(sb.toString());
         pstmt.setString(1, accountNum);
         pstmt.executeUpdate();
         ok = true;
      } catch (Exception e) {
         // 컴파일 예외를 런타임 예외로 변환
         throw new RuntimeException(e.getMessage());
      } finally {
         try {
            if(pstmt != null) pstmt.close();
            if(con != null) con.close();
         } catch (Exception e) {

         }
      }
      return ok;
   }
   
   public void close() {
	   if(con != null) {
		   try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   }
   }
   
}