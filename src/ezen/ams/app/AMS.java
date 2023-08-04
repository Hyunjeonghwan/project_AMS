package ezen.ams.app;

import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.JdbcAccountRepository;
import ezen.ams.domain.MinusAccount;
import ezen.ams.gui.AMSFrame;
import ezen.ams.domain.Account;

public class AMS {
	

	public static void main(String[] args) {
		AMSFrame frame = new AMSFrame("EZEN-BANK AMS");
		frame.init();
		frame.eventHandling();
		frame.pack();
//		frame.setResizable(false);
		frame.setVisible(true);
		
	}

}