package edu.uwm.cs351;

/**
 * Exception thrown when an account goes below the minimum.
 */
public class OverdraftException extends RuntimeException {

	/**
	 * Keep Eclipse Happy
	 */
	private static final long serialVersionUID = 1L;
	
	private final Account account;
	private final Money amount;
	
	/**
	 * Create an overdraft exception with given information
	 * @param account the account overdrawn
	 * @param amount the amount account would need to have added to bring it to the minimum,
	 * always positive
	 */
	public OverdraftException(Account account, Money amount) {
		super("Overdraft of " + account + " by " + amount);
		if (!amount.isPositive()) throw new IllegalArgumentException("overdrawn amount must be positive");
		this.account = account;
		this.amount = amount;
	}
	
	/**
	 * Return the account that is overdrawn.
	 * @return overdrawn account
	 */
	public Account getAccount() {
		return account;
	}
	
	/**
	 * Return the amount of the overdraft
	 * @return how much the account would be go below the minimum,
	 * always a positive amount of money.
	 */
	public Money getAmount() {
		return amount;
	}
}
