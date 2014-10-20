package data;

public class Transaction {
	private int transactionID;
	private int fileID;
	private String reason;
	private int time;
	private int withdrawal;
	private int woltlabID;
	private float balance;
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	
	public int getFileID() {
		return fileID;
	}
	
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public boolean isWithdrawal() {
		return withdrawal == 1;
	}
	
	public void setWithdrawal(int withdrawal) {
		this.withdrawal = withdrawal;
	}
	
	public int getWoltlabID() {
		return woltlabID;
	}
	
	public void setWoltlabID(int woltlabID) {
		this.woltlabID = woltlabID;
	}
	
	public float getBalance() {
		return balance;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
}
