package ezen.ams.domain;

import ezen.ams.exception.NotAvailableMoneyException;

public class MinusAccount extends Account {
//	부모클래스에 없는 필드나 메소드 추가
	private long borrowMoney;
	
	public MinusAccount() {
//		컴파일시 부모클래스의 디폴트생성자를 호출하는 super(); 자동추가
//		super();
	}
	public MinusAccount(String accountOwner, int passwd, long restMoney, long borrowMoney) {
//		부모클래스의 생성자 호출을 이용한 초기화
		super(accountOwner, passwd, restMoney);
		this.borrowMoney = borrowMoney;
	}
	
	public long getBorrowMoney() {
		return borrowMoney;
	}
	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
//	필요에 따라 부모클래스의 메소드 재정의(overridng)
	public long getRestMoney() {
		return super.getRestMoney() - borrowMoney;
	}
	@Override
	public String toString() {
		return super.toString()+"\t\t"+borrowMoney;
	}
	
	@Override
	public long withdraw(long money) throws NotAvailableMoneyException {
		if(money <= 0) {
			throw new NotAvailableMoneyException("대출금액은 0 이하일 수 없습니다.");
		}
		return borrowMoney += money;
	}

	
	
}