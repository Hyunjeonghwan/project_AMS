package ezen.ams.domain;

import ezen.ams.exception.NotAvailableMoneyException;

// 은행계좌 추상화
public class Account {
//	인스턴스변수
	private String accountNum;
	private String accountOwner;
	private int passwd;
	private long restMoney;
//	스태틱변수(클래스변수) 공유 되는 변수
	public final static String BANK_NAME="이젠은행"; //final 상수
//	private static int accountId = 1000;
	private static int accountId;
	
//	초기화 블록
//	주로 애플리케이션 환경설정파일의 내용을 읽어오는 코드
	static{   //static 선언시 처음에 한번만 초기화
//		static 없으면 인스턴스 생성시마다 초기화
		accountId=1000;
	}
	
	public Account() {}
	
	public Account(String accountOwner, 
			int passwd, long restMoney) {
		this.accountNum = (accountId++)+"";
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}

	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getAccountOwner() {
		return accountOwner;
	}
	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}
	public int getPasswd() {
		return passwd;
	}
	public void setPasswd(int passwd) {
		this.passwd = passwd;
	}
	public long getRestMoney() {
		return restMoney;
	}
	public void setRestMoney(long restMoney) {
		this.restMoney = restMoney;
	}
//	입금
	public long deposit(long money) throws NotAvailableMoneyException{
//		데이터검증 및 예외처리
		if(money<=0) {
			throw new NotAvailableMoneyException("금액은 0 이하일 수 없습니다.");
		}else if(money>=100000000) {
			throw new NotAvailableMoneyException("1억이상 입금할 수 없습니다.");
		}
		return restMoney += money;
	}
//	출금
	public long withdraw(long money) throws NotAvailableMoneyException{
//		데이터검증 및 예외처리
		if(money<=0) {
			throw new NotAvailableMoneyException("금액은 0 이하일 수 없습니다.");
		}else if(money>=100000000) {
			throw new NotAvailableMoneyException("1억이상 출금할 수 없습니다.");
		}else if(money>restMoney) {
			throw new NotAvailableMoneyException("잔액이 부족합니다.");
		}
		return restMoney -= money;
	}
//	비밀번호 확인
	public boolean checkPasswd(int pwd) {
		 return passwd == pwd;
	}
//	계좌 정보 출력
	public void printInfo() {
		System.out.println(accountNum+"\t\t"+accountOwner+"\t\t"+"****"+"\t\t"+restMoney);
	}
	
	public static int getAccountId(){
		return accountId;
	}

	@Override
	public String toString() {
		return accountNum+"\t\t"+accountOwner+"\t\t"+"****"+"\t\t"+restMoney;
//		return "Account [accountNum=" + accountNum + ", accountOwner=" + accountOwner + ", passwd=" + passwd
//				+ ", restMoney=" + restMoney + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
	
	
	
	
}
