package edu.uwm.cs351;

import java.util.Random;

/**
 * A bank account.
 * An account has a minimum balance that normally the current balance should never go below.
 */
public class Account {
	private final String owner;
	private final String id;
	private Money minimum;
	private Money current;
	
	/**
	 * This method is used to generate a (mostly) unique account number
	 * with at least four digit.
	 * @return a string with length >= 4
	 */
	private static String genID() {
		Random r = new Random();
		return "000" + r.nextLong();
	}
	
	/**
	 * Create a new account for the given owner
	 * with a zero minimum and a zero new balance.
	 * @param owner representation of owner, must not be null
	 */
	public Account(String owner) {
		if (owner == null) throw new NullPointerException();
		this.id = genID();
		this.owner = owner;
		this.minimum = new Money(0);
		this.current = new Money(0);
		// Call a different constructor with this(...)  does that even mean?????
	}
	
	/**
	 * Create a new account with the given minimum and current abalnces
	 * @param owner representation of the owner, must not be null
	 * @param minimum minimum balance, must not be null
	 * @param current current balance, must not be null 
	 */
	public Account(String owner, Money minimum, Money current) {
		// TODO: call a different constructor ith this(...)
		if (owner == null || minimum == null || current == null) throw new NullPointerException();
		this.owner = owner;
		this.id = genID();
		this.minimum = minimum;
		this.current = current;
	}
	
	/**
	 * Create a account with all information.
	 * @param owner representation of owner, must not be null
	 * @param id account number.  Must have at least four characters
	 * @param min minimum balance for this account, must not be null
	 * @param initial initial balance of the account, must not be null
	 */
	public Account(String owner, String id, Money min, Money initial) {
		if (owner == null || id == null || min == null || initial == null) throw new NullPointerException();
		if (id.length() <4) throw new IllegalArgumentException();
		this.owner = owner;
		this.id = id;
		this.minimum = min;
		this.current = initial;
	}
	
	/**
	 * Return the owner of the account
	 * @return owner information
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Return the ID of the account.
	 * This should be kept confidential.
	 * @return account ID
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Return the minimum balance of the account
	 * @return minimum balance
	 */
	public Money getMinimum() {
		return minimum;
	}
	
	/**
	 * Return the current balance of the account
	 * @return current balance
	 */
	public Money getCurrent() {
		return current;
	}
	
	@Override // implementation
	public String toString() {
		return owner + " " + id.substring(id.length()-4);
	}
	
	/**
	 * Adjust an account by the amount given.
	 * If this amount would bring the balance below the minimum
	 * then unless it was a positive adjustment or the adjustment is forced,
	 * then an overdraft exception is thrown.
	 * @param amount money to add to this account; if negative, then the adjustment is a withdrawal.
	 * @param force force the adjustment to not throw an overdraft exception
	 * @throws OverdraftException if reducing the current balance below the minimum.
	 * In this case, the account balance is not affected.
	 * @throws ArithmeticException if the new value would underflow/overflow.
	 * No change is made to the account.
	 */
	public void adjust(Money amount, boolean force) throws OverdraftException, ArithmeticException {
		if (this.minimum.compareTo(this.current.add(amount)) == 1 && force == false) throw new OverdraftException(this,amount);
		this.current = this.current.add(amount);
		}
}
