import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Account;
import edu.uwm.cs351.Money;
import edu.uwm.cs351.OverdraftException;
import edu.uwm.cs351.Transaction;

public class TestTransaction  extends LockedTestCase {
    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
        try {
                r.run();
                assertFalse("Exception should have been thrown",true);
        } catch (RuntimeException ex) {
                assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
        }       
    }   
	
	
	protected String result(Supplier<?> r) {
		try {
			return "" + r.get();
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	/// locked tests
	
	public void test() {
		Account a1 = new Account("Pat Lee", "12345", new Money(5), new Money(20));
		Account a2 = new Account("Sandy Roy", "ABCDE", new Money(-1000), new Money(10.00));
		Transaction t1 = new Transaction("groceries", a1, a2, new Money(25));
		// type the name of the exception thrown, or string version of return value:
		assertEquals(Ts(914703724), result(() -> t1.perform(false)));
		Transaction t2 = new Transaction("groceries", a2, a1, new Money(25));
		assertEquals(Ts(192504150), result( () -> t2.perform(false)));
		assertEquals(Ts(1687899401), a1.getCurrent().toString());
		Transaction t3 = new Transaction("credit card payment", a1, null, new Money(50));
		// What kind of transaction is t3 ?  Also Account prints as <owner> <last4chars>
		assertEquals(Ts(1445418934), t3.toString());
		assertEquals(Ts(1195894263), result( () -> t3.perform(true)));
		assertEquals(Ts(60547824), a1.getCurrent().toString());
	}

	
	protected Account a1, a2;
	protected Transaction t;
	
	/// textCn: constructor tests
	
	public void testC0() {
		t = new Transaction(null, new Account ("John Boyland"), Money.DOLLAR);
		t = new Transaction(new Account("John Boyland"), null, Money.CENT);
	}
	
	public void testC1() {
		t = new Transaction("Bonus",null, new Account ("John Boyland"), Money.DOLLAR);
		t = new Transaction("test fee", new Account("John Boyland"), null, Money.CENT);		
	}
	
	public void testC2() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t = new Transaction(a1, a2, new Money(20.00));
		t = new Transaction("payment", a1, a2, new Money(20.00));
	}
	
	public void testC3() {
		Money lots = new Money(Long.MAX_VALUE/100d);
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t = new Transaction(a1, a2, lots);
		t = new Transaction(null, a2, lots);
		t = new Transaction(a1, null, lots);
	}
	
	public void testC5() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		assertException(NullPointerException.class, () -> new Transaction(null, a1, a2, Money.DOLLAR));
	}
	
	public void testC6() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		assertException(IllegalArgumentException.class, () -> new Transaction(null, null, Money.DOLLAR));
		assertException(IllegalArgumentException.class, () -> new Transaction("not going anywhere", a1, a1, Money.DOLLAR));
	}
	
	public void testC7() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		assertException(NullPointerException.class, () -> new Transaction("nothing", a1, a2, null));
		
	}
	
	public void testC8() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		assertException(IllegalArgumentException.class, () -> new Transaction("nothing", a1, a2, Money.DOLLAR.negate()));		
	}
	
	public void testC9() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		assertException(IllegalArgumentException.class, () -> new Transaction("nothing", a1, a2, Money.ZERO));		
	}
	
	
	/// testEn: equals tests

	
	public void testE0() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.81));
		Transaction t2 = new Transaction(new String("payment"), a1, a2, new Money(25.81));
		assertTrue(t.equals(t2));
	}

	public void testE1() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.80)); // slightly less
		Transaction t2 = new Transaction(new String("payment"), a1, a2, new Money(25.81));
		assertFalse(t.equals(t2));
	}
	
	public void testE2() {
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("interest", a2, null, new Money(9.36));
		Transaction t2 = new Transaction(new String("interest"), a2, null, new Money(9.36));
		assertTrue(t.equals(t2));		
	}
	
	public void testE3() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		t =  new Transaction("interest", null, a1, new Money(0.03));
		Transaction t2 = new Transaction(new String("interest"), null, a1, new Money(0.0314));
		assertTrue(t.equals(t2));	
	}

	public void testE4() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("Aa", a1, a2, new Money(25.81)); 
		Transaction t2 = new Transaction("BB", a1, a2, new Money(25.81));
		assertFalse(t.equals(t2));
	}
	
	public void testE5() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		Account a1a = new Account("John Boyland", new Money(5), new Money(100));
		Transaction t1 = new Transaction(a1, a2, Money.DOLLAR);
		Transaction t2 = new Transaction(null, a2, Money.DOLLAR);
		Transaction t3 = new Transaction(a1, null, Money.DOLLAR);
		Transaction t4 = new Transaction(a1a, a2, Money.DOLLAR);
		assertFalse(t1.hashCode() == t2.hashCode());
		assertFalse(t1.hashCode() == t2.hashCode());
		assertFalse(t1.equals(t4));
		assertFalse(t2.equals(t1));
		assertFalse(t2.equals(t3));
		assertFalse(t3.equals(t1));
		assertFalse(t3.equals(t2));
	}
	
	public void testE6() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("", a1, a2, new Money(25.81)); 
		Transaction t2 = new Transaction(a1, a2, new Money(25.81));
		assertTrue(t.equals(t2));
	}
	
	public void testE7() {
		Money magic = new Money(10_995_116_280.74);
		Money pittance = new Money(0.42);
		assertEquals(pittance.hashCode(), magic.hashCode());
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		t =  new Transaction(null, a1, pittance); 
		Transaction t2 = new Transaction(null, a1, magic);
		assertFalse(t.equals(t2));
	}
	
	public void testE9() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.81));
		assertFalse(t.equals(null));
		assertFalse(t.equals((Object)new Money(25.81)));
		assertFalse(t.equals(new Object()));
	}
	
	
	/// testHn: hashcode tests
	
	public void testH0() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.81));
		Transaction t2 = new Transaction(new String("payment"), a1, a2, new Money(25.81));
		assertEquals(t.hashCode(), t2.hashCode());
	}

	public void testH1() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.80)); // slightly less
		Transaction t2 = new Transaction(new String("payment"), a1, a2, new Money(25.81));
		assertFalse(t.hashCode() == t2.hashCode());
	}
	
	public void testH2() {
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("interest", a2, null, new Money(9.36));
		Transaction t2 = new Transaction(new String("interest"), a2, null, new Money(9.36));
		assertEquals(t.hashCode(), t2.hashCode());		
	}
	
	public void testH3() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		t =  new Transaction("interest", null, a1, new Money(0.03));
		Transaction t2 = new Transaction(new String("interest"), null, a1, new Money(0.0314));
		assertEquals(t.hashCode(), t2.hashCode());	
	}

	public void testH4() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("A", a1, a2, new Money(25.81)); 
		Transaction t2 = new Transaction("B", a1, a2, new Money(25.81));
		assertFalse(t.hashCode() == t2.hashCode());
	}
	
	public void testH5() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		Account a1a = new Account("John Boyland", new Money(5), new Money(100));
		Transaction t1 = new Transaction(a1, a2, Money.DOLLAR);
		Transaction t2 = new Transaction(null, a2, Money.DOLLAR);
		Transaction t3 = new Transaction(a1, null, Money.DOLLAR);
		Transaction t4 = new Transaction(a1a, a2, Money.DOLLAR);
		assertFalse(t1.hashCode() == t2.hashCode());
		assertFalse(t1.hashCode() == t3.hashCode());
		assertFalse(t1.hashCode() == t4.hashCode());
		assertFalse(t2.hashCode() == t1.hashCode());
		assertFalse(t2.hashCode() == t3.hashCode());
		assertFalse(t3.hashCode() == t1.hashCode());
		assertFalse(t3.hashCode() == t2.hashCode());
	}
	
	public void testH6() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction("", a1, a2, new Money(25.81)); 
		Transaction t2 = new Transaction(a1, a2, new Money(25.81));
		assertTrue(t.hashCode() == t2.hashCode());
	}
	
	public void testH7() {
		Money loads = new Money(655.78);
		Money pittance = new Money(0.42);
		assertFalse(pittance.hashCode() == loads.hashCode());
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		t =  new Transaction(null, a1, pittance); 
		Transaction t2 = new Transaction(null, a1, loads);
		assertFalse(t.hashCode() == t2.hashCode());
	}
	
	
	/// testPn: Perform tests
	
	public void testP0() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction(a1, a2, new Money(25.81)); 
		assertEquals(Money.ZERO, t.perform(false));
		assertEquals(new Money(74.19), a1.getCurrent());
		assertEquals(new Money(-119.86), a2.getCurrent());
	}
	
	public void testP1() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		t =  new Transaction(a1, null, new Money(25.81)); 
		assertEquals(new Money(25.81), t.perform(false));
		assertEquals(new Money(74.19), a1.getCurrent());
	}
	
	public void testP2() {
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction(null, a2, new Money(25.81)); 
		assertEquals(new Money(-25.81), t.perform(false));
		assertEquals(new Money(-119.86), a2.getCurrent());
	}
	
	public void testP3() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction(a1, a2, new Money(125.81)); 
		assertEquals(Money.ZERO, t.perform(true));
		assertEquals(new Money(-25.81), a1.getCurrent());
		assertEquals(new Money(-19.86), a2.getCurrent());
	}
	
	public void testP4() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		t =  new Transaction(a1, a2, new Money(125.81)); 
		try {
			t.perform(false);
			assertFalse("Should have thrown an exception", true);
		} catch (OverdraftException ex) {
			assertSame(a1, ex.getAccount());
			assertEquals(new Money(30.81), ex.getAmount());
		}
		assertEquals(new Money(100.00), a1.getCurrent());
		assertEquals(new Money(-145.67), a2.getCurrent());
	}
	
	public void testP5() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(-145.67));
		Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(25.81));
		t =  new Transaction(a1, a2, lots);
		t.perform(true);
		assertEquals(new Money(100.00).sub(lots), a1.getCurrent());
		assertEquals(lots.sub(new Money(145.67)), a2.getCurrent());
	}
	
	public void testP6() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(145.67));
		Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(25.81));
		t =  new Transaction(a1, a2, lots);
		try {
			t.perform(false);
			assertFalse("Should have thrown an exception", true);
		} catch (OverdraftException ex) {
			assertEquals(lots.sub(new Money(95)), ex.getAmount());
		}
		assertEquals(new Money(100.00), a1.getCurrent());
		assertEquals(new Money(145.67), a2.getCurrent());		
	}
	
	public void testP7() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", new Money(-1000), new Money(145.67));
		Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(25.81));
		t =  new Transaction(a1, a2, lots);
		assertException(ArithmeticException.class, () -> t.perform(true));
		assertEquals(new Money(100.00), a1.getCurrent());
		assertEquals(new Money(145.67), a2.getCurrent());		
	}
	
	public void testP8() {
		a1 = new Account("John Boyland", new Money(5), new Money(100));
		a2 = new Account("Scrooge McDuck", new Money(0), new Money(Long.MAX_VALUE/100d).sub(new Money(34.56)));
		t = new Transaction(a1, a2, new Money(80.00));
		assertException(ArithmeticException.class, () -> t.perform(false));
		assertEquals(new Money(100.00), a1.getCurrent());		
	}
	
	
	/// testTn: toString tests
	
	public void testT0() {
		a1 = new Account("John Boyland", "0006018735", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", "2500123456789012", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", a1, a2, new Money(25.81)); 
		assertEquals("TRANSFER from John Boyland 8735 to Henry Trimbach 9012: $25.81 payment", t.toString());
	}
	
	public void testT1() {
		a1 = new Account("John Boyland", "0006018735", new Money(5), new Money(100));
		t =  new Transaction("payment", a1, null, new Money(25.81)); 
		assertEquals("WITHDRAWAL from John Boyland 8735: $25.81 payment", t.toString());
	}
	
	public void testT2() {
		a2 = new Account("Henry Trimbach", "2500123456789012", new Money(-1000), new Money(-145.67));
		t =  new Transaction("payment", null, a2, new Money(25.81)); 
		assertEquals("DEPOSIT to Henry Trimbach 9012: $25.81 payment", t.toString());
	}
	
	public void testT3() {
		a1 = new Account("John Boyland", "0006018735", new Money(5), new Money(100));
		a2 = new Account("Henry Trimbach", "2500123456789012", new Money(-1000), new Money(-145.67));
		t =  new Transaction(a1, a2, new Money(25.81)); 
		assertEquals("TRANSFER from John Boyland 8735 to Henry Trimbach 9012: $25.81 ", t.toString());
	}
}
