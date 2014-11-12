package data;

public class Transaction {
	private int transactionID = 0;
	private int fileID = 0;
	private String reason = "";
	private int time = 0;
	private int withdrawal = 0;
	private int woltlabID = 0;
	private float balance = 0.f;

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
