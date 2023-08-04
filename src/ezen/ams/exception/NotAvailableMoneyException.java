package ezen.ams.exception;

/**
 * 사용자 정의 예외 클래스
 * @author 현정환
 *
 */

public class NotAvailableMoneyException extends Exception{
//	String message; 기본탑재
	private int errorCode;
	
	public NotAvailableMoneyException(){}
	
	public NotAvailableMoneyException(String message){
		super(message);
	}
	
	public NotAvailableMoneyException(String message, int errorCode){
		super(message);
		this.errorCode = errorCode;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
		public String toString() {
			return getMessage();
		}
	
}
