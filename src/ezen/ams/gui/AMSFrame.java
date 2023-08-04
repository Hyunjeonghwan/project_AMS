package ezen.ams.gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;

import ezen.ams.app.AMS;
import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.AccountType;
import ezen.ams.domain.JdbcAccountRepository;
import ezen.ams.domain.MinusAccount;

/**
 * 
 * @author 현정환
 *
 */

public class AMSFrame extends Frame {
	
	GridBagLayout grid;
	GridBagConstraints con;
	
	Button btnSearch, btnDelete, btnSearch2, btnRegist, btnTsearch;
	Choice choiceAccount;
	TextField textAccount, textOwner, textPassword, textInputMoney, textBorrowMoney;
	Label labelAccount, labelNum, labelOwner, labelPassword, labelInputMoney, labelBorrowMoney, labelList, labelWon, labelToday;
	TextArea contentsArea;
	
	private AccountRepository repository;
	
	
	public AMSFrame() {}
	public AMSFrame(String title) {
		super(title);
		try {
			repository = new JdbcAccountRepository();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		
		btnSearch = new Button("조회");
		btnDelete = new Button("삭제");
		btnSearch2 = new Button("검색");
		btnRegist = new Button("신규등록");
		btnTsearch = new Button("전체조회");
		
		choiceAccount = new Choice();
		AccountType[] accountTypes = AccountType.values();
		for (AccountType accountType : accountTypes) {
			choiceAccount.add(accountType.getName());
		}
		
		textAccount = new TextField();
		textOwner = new TextField();
		textPassword = new TextField();
		textInputMoney = new TextField();
		textBorrowMoney = new TextField();
		
		labelAccount = new Label("계좌종류", Label.CENTER);
		labelNum = new Label("계좌번호", Label.CENTER);
		labelOwner = new Label("예금주명", Label.CENTER);
		labelPassword = new Label("비밀번호", Label.CENTER);
		labelInputMoney = new Label("입금금액", Label.CENTER);
		labelBorrowMoney = new Label("대출금액", Label.CENTER);
		labelList = new Label("계좌목록", Label.CENTER);
		labelWon = new Label("(단위 : 원)", Label.CENTER);
		
		contentsArea = new TextArea();
		labelToday = new Label("",Label.CENTER);
		
	}
	
	public void init() {
		grid = new GridBagLayout();
		con = new GridBagConstraints();
		setLayout(grid);
		con.fill = GridBagConstraints.BOTH;

		addCom(labelAccount, 0, 0, 1, 1);
	    addCom(labelNum, 0, 1, 1, 1);
	    addCom(labelOwner, 0, 2, 1, 1);
	    addCom(labelPassword, 0, 3, 1, 1);
	    addCom(labelInputMoney, 2, 3, 1, 1);
	    addCom(labelBorrowMoney, 0, 4, 1, 1);
	    addCom(labelList, 0, 6, 1, 1);
	    addCom(labelWon, 4, 6, 1, 1);
	    
	    addCom(choiceAccount, 1, 0, 1, 1);
	    
	    addCom(textAccount, 1, 1, 1, 1);
	    addCom(textOwner, 1, 2, 1, 1);
	    addCom(textPassword, 1, 3, 1, 1);
	    addCom(textBorrowMoney, 1, 4, 1, 1);
	    addCom(textInputMoney, 3, 3, 2, 1);
	    
	    addCom(btnSearch, 2, 1, 1, 1);
	    addCom(btnDelete, 3, 1, 1, 1);
	    addCom(btnSearch2, 2, 2, 1, 1);
	    addCom(btnRegist, 2, 4, 1, 1);
	    addCom(btnTsearch, 3, 4, 1, 1);
	    
	    addCom(contentsArea, 0, 8, MAXIMIZED_BOTH, MAXIMIZED_BOTH);
	    addCom(labelToday, 3, 0, 2, 1);
		
	}
	
	private void addCom(Component c, int x, int y, int w, int h) {
		con.gridx=x;
		con.gridy=y;
		con.gridwidth=w;
		con.gridheight=h;
		
		con.weightx=1;
		con.weighty=0;
		
		grid.setConstraints(c, con);
		con.insets=new Insets(5, 5, 5, 5);
		add(c);
	}
	
	private void setToday() {
		labelToday.setBackground(Color.blue);
		labelToday.setForeground(Color.white);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String format = String.format("%1$tF %1$tT (%1$ta)", Calendar.getInstance());
					labelToday.setText(format);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void printHeader() {
		contentsArea.append("===============================================================================================\n");
		contentsArea.append("계좌타입\t계좌번호\t예금주\t\t비밀번호\t잔액\t\t대출금액\n");
		contentsArea.append("===============================================================================================\n");
	}
	
	public void allList() {
		contentsArea.setText("");
		printHeader();
		List<Account> list = repository.getAccounts();
		list.sort(Comparator.comparing(Account::getAccountNum));
		for (Account account : list) {
			if(account instanceof MinusAccount) {
				contentsArea.append("[마이너스계좌]\t" + account.toString() + "\n");
			}else {
				contentsArea.append("[입출금계좌]\t" + account.toString() + "\n");
			}
		}
	}
	
	public void selectAccountType(AccountType accountType) {
		
		
		if(accountType.toString()=="GENERAL_ACCOUNT") {
			textBorrowMoney.setEnabled(false);
		}else{
			textBorrowMoney.setEnabled(true);
		}
		
		
//		switch (accountType) {
//		case GENERAL_ACCOUNT:
//			textBorrowMoney.setEnabled(false); break;	
//		}
	}
	
	public void addAccount() {
		Account account = null;
		
		// 편의상 정상 입력되었다 가정
		String accountOwner = textOwner.getText();
		int password =Integer.parseInt(textPassword.getText());
		long inputMoney = Long.parseLong(textInputMoney.getText());
		
		String selectedItem = choiceAccount.getSelectedItem();
		// 입출금 계좌 등록
		if(selectedItem.equals(AccountType.GENERAL_ACCOUNT.getName())) {
			account = new Account(accountOwner, password, inputMoney);
		} else if(selectedItem.equals(AccountType.MINUS_ACCOUNT.getName())){
			long borrowMoney = Long.parseLong(textBorrowMoney.getText());
			account = new MinusAccount(accountOwner, password, inputMoney, borrowMoney);
		}
		
		repository.addAccount(account);
		JOptionPane.showMessageDialog(this, "정상 등록 처리되었습니다.");		
	}
	
	public void clearText() {
		textAccount.setText("");
		textOwner.setText("");
		textPassword.setText("");
		textInputMoney.setText("");
		textBorrowMoney.setText("");
	}
	
	public void removeAccount() {
		String accountNum = textAccount.getText();
		repository.removeAccount(accountNum);
		JOptionPane.showMessageDialog(this, "정상 삭제 처리되었습니다.");
	}
	
	public void searchAccount() {
		
		contentsArea.setText("");
		
		printHeader();
		
		String accountNum = textAccount.getText();
		
		Account account = null;
		account=repository.searchAccount(accountNum);
//		contentsArea.append(account.toString() + "\n");
		if(account instanceof MinusAccount) {
			contentsArea.append("[마이너스계좌]\t" + account.toString() + "\n");
		}else {
			contentsArea.append("[입출금계좌]\t" + account.toString() + "\n");
		}
		JOptionPane.showMessageDialog(this, "계좌번호로 검색하였습니다.");
		
	}
	
	public void searchAccountOwner() {
		
		contentsArea.setText("");
		printHeader();
		String accountOwner = textOwner.getText();
		
		List<Account> account = null;
		account = repository.searchAccountByOwner(accountOwner);
//		contentsArea.append(account.toString() + "\n");
		if(account instanceof MinusAccount) {
			contentsArea.append("[마이너스계좌]\t" + account.toString() + "\n");
		}else {
			contentsArea.append("[입출금계좌]\t" + account.toString() + "\n");
		}
		JOptionPane.showMessageDialog(this, "예금주명으로 검색하였습니다.");
	}
	
	
	public void eventHandling() {
		
		class ActionHandler implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				if(eventSource == btnRegist) {
					addAccount();
					allList();
					clearText();
				} else if(eventSource == btnTsearch) {
					allList();
					clearText();
				} else if(eventSource == btnDelete) {
					removeAccount();
					allList();
					clearText();
				} else if(eventSource == btnSearch) {
					searchAccount();
					clearText();
				} else if(eventSource == btnSearch2) {
					searchAccountOwner();
					clearText();
				}
			}
		}
		ActionListener actionListener = new ActionHandler();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
//		창 열릴 때
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
//				allList();
				setToday();
			}
		});
		
		// 계좌 등록
		btnRegist.addActionListener(actionListener);
		// 계좌 선택
		choiceAccount.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					if(choiceAccount.getSelectedItem().equals("입출금계좌")) {
						selectAccountType(AccountType.GENERAL_ACCOUNT);					
					}else if(choiceAccount.getSelectedItem().equals("마이너스계좌")) {
						selectAccountType(AccountType.MINUS_ACCOUNT);
					}
				}
				
			}
		});
		
		btnDelete.addActionListener(actionListener);
		btnSearch.addActionListener(actionListener);
		btnSearch2.addActionListener(actionListener);
		
		// 전체계좌 조회
		btnTsearch.addActionListener(actionListener);		
		
	}
	
	
	
	public void exit() {
		((JdbcAccountRepository)repository).close();
		setVisible(false);
		dispose();
		System.exit(0);
	}
	

	
	
}
