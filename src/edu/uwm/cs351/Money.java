package edu.uwm.cs351;

/**
 * A class to represent a number of dollars and cents.
 * Negative numbers represent debts.
 * The absolute value of cents cannot exceed {@Link Long#MAX_VALUE}.
 * The methods in this class will throw an {@link ArithmeticException}
 * if there is underflow or overflow, rather than rolling around.
 */
public class Money implements Comparable<Money> {
	private final long cents;
	
	private Money(long cents) {
		if (cents == Long.MIN_VALUE) throw new ArithmeticException("too small");
		this.cents = cents;
	}
	
	public static final Money ZERO = new Money(0);
	public static final Money CENT = new Money(1);
	public static final Money DOLLAR = new Money(100);
	
	/**
	 * Create a money object representing the given number of dollars.
	 * @exception ArithmeticException if the number of cents
	 * is outside of the range [-Long.MAX_VALUE, Long.MAX_VALUE].
	 * @param amt number of dollars
	 */
	public Money(double amt) {
		// TODO (not simple)
		if (amt*100 > (double)Long.MAX_VALUE) throw new ArithmeticException();
		if (amt*100 < (double)Long.MIN_VALUE) throw new ArithmeticException();
		this.cents = Math.round(amt*100);
	}
	
	/**
	 * Return the money as a double value.
	 * @return money as a certain number of dollars.
	 */
	public double asDouble() {
		return cents / 100d;
	}
	
	/**
	 * Return whether this money is positive: not zero and not negative. 
	 * @return whether positive
	 */
	public boolean isPositive() {
		if(cents > 0) return true;
		else return false;
	}
	
	/**
	 * Return whether this money is negative: not zero and not positive. 
	 * @return whether negative
	 */
	public boolean isNegative() {
		if(cents < 0) return true;
		else return false;
	}
	
	/**
	 * Convert money into a debt, or the other way around.
	 * @return inverse amount of money.
	 */
	public Money negate() {
		return new Money(-this.cents);
		//return null; // TODO
	}
	
	/**
	 * Return the sum of two amounts.
	 * @param other another amount, not null
	 * @exception ArithmeticException if underflow or overflow results
	 * @return sum amount, never null
	 */
	public Money add(Money other) {
		//double result = this.cents + other.cents;
		//return new Money(result);
		
		//long result = this.cents + other.cents;
		long result = Math.addExact(this.cents, other.cents); //plzzzzzz be right
		Money results = new Money(result); // TODO (Do not use doubles!).
		System.out.print(results.cents+"\n"); //test 48 keeps changing to 1 to large for money(long)?
		return results;
		
	}
	
	/**
	 * Return the difference of two amounts, which will
	 * be negative if the other amount is greater than this.
	 * @param other another amount, must not be null
	 * @exception ArithmeticException if underflow or overflow results
	 * @return difference amount, never null
	 */
	public Money sub(Money other) {
		
		return this.add(new Money(other.cents).negate());
		//return null; // TODO, as with add, or use add and negate
	}
	
	/**
	 * Return the result of multiplying by the given scalar value.
	 * @param amt scalar amount to multiply with
	 * @return new amount
	 */
	public Money mul(double amt) {
		return new Money(this.cents*amt/100);
	}
	
	/**
	 * Return the ratio of two amounts of money.
	 * @param other other amount, must not be null
	 * @return ration of the two
	 */
	public double div(Money other) {
		System.out.print(this.cents/other.cents);
		Money r = new Money(this.cents/other.cents);
		return (long)(r.cents);
	}
	
	@Override // implementation
	public String toString() {
		String str = String.format("%d",Math.abs(this.cents));
		if (str.length() >= 3)
			str = new StringBuilder(str).insert(str.length()-2, ".").toString();
		else if(str.length() == 2) {
			str = new StringBuilder(str).insert(str.length()-2, ".").toString();
			str = String.format("0%s", str);
		}
		else if(str.length() == 1) {
			str = new StringBuilder(str).insert(0, ".0").toString();
			str = String.format("0%s", str);
		}
		str = String.format("$%s", str);
		if (this.isNegative())
			str = String.format("(%s)", str);
		return str;
	}
	
	@Override // implementation
	public boolean equals(Object obj) {
		return (this.hashCode() == obj.hashCode());
	}
	
	@Override // implementation
	public int hashCode() {
		return Long.valueOf(cents).hashCode();
	}
	
	@Override // required
	public int compareTo(Money other) {
		// TODO
		return 0;
	}
}
