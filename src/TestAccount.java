import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Account;
import edu.uwm.cs351.Money;
import edu.uwm.cs351.OverdraftException;

public class TestAccount extends LockedTestCase {
    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
        try {
                r.run();
                assertFalse("Exception should have been thrown",true);
        } catch (RuntimeException ex) {
                assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
        }       
    }   
	
	
	protected String exceptionThrown(Runnable r) {
		try {
			r.run();
			return "null";
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	
	/// locked tests
	
	public void test() {
		Account a = new Account("John Boyland", "1234567890", new Money(5.00), new Money(20.00));
		assertEquals(Ts(221230714), a.getMinimum().toString());
		assertEquals(Ts(1178693416), a.getCurrent().toString());
		assertEquals(Ts(63546270), a.toString());
		// Type null, or the name of the exception thrown (spelled correctly!)
		assertEquals(Ts(715194354), exceptionThrown( () -> a.adjust(new Money(-10.00), false)));
		assertEquals(Ts(1007134852), a.getCurrent().toString());
		assertEquals(Ts(920070794), exceptionThrown( () -> a.adjust(new Money(-10.00), false)));
		Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(3.14)); // 92 quadrillion dollars!! (who cares about $3.14?)
		assertEquals(Ts(408651813), exceptionThrown( () -> a.adjust(lots, false)));
		assertEquals("$10.00", a.getCurrent().toString());
		assertEquals(Ts(1712538611), exceptionThrown(() -> a.adjust(new Money(-10.00), true)));
		assertEquals(Ts(1022228848), a.getCurrent().toString());
	}
	
	
	protected Account a;
	
	/// testa-j: constructor tests
	
	public void testa() {
		a = new Account("Boyland, John");
		assertEquals("Boyland, John", a.getOwner());
		assertEquals(Money.ZERO, a.getCurrent());
		assertEquals(Money.ZERO, a.getMinimum());
		String id = a.getID();
		assertTrue(id + ".length() >= 4", id.length() >= 4);
	}
	
	public void testb() {
		a = new Account ("Trimbach, Henry", new Money(5), new Money(3.14)); // OK to start under minimum
		assertEquals("Trimbach, Henry", a.getOwner());
		assertEquals(new Money(3.14), a.getCurrent());
		assertEquals(new Money(5.0), a.getMinimum());
		String id = a.getID();
		assertTrue(id + ".length() >= 4", id.length() >= 4);
	}
	
	public void testc() {
		a = new Account ("Patel, Yash", "0042", new Money(-10000), new Money(-10)); // a credit card account
		assertEquals("Patel, Yash", a.getOwner());
		assertEquals(new Money(-10.00), a.getCurrent());
		assertEquals(new Money(-10_000.00), a.getMinimum());
		assertEquals("0042", a.getID());		
	}
	
	public void testd() {
		a = new Account("Boyland, John");
		String id1 = a.getID();
		a = new Account("Boyland, John");
		String id2 = a.getID();
		assertFalse(id1+".equals(" + id2 + ")", id1.equals(id2));
	}
	
	public void teste() {
		a = new Account ("Trimbach, Henry", new Money(5), new Money(100));
		String id1 = a.getID();
		a = new Account ("Trimbach, Henry", new Money(5), new Money(100));
		String id2 = a.getID();
		assertFalse(id1+".equals(" + id2 + ")", id1.equals(id2));		
	}
	
	public void testf() {
		assertException(NullPointerException.class, () -> new Account(null));
		assertException(NullPointerException.class, () -> new Account(null, Money.ZERO, Money.DOLLAR));
		assertException(NullPointerException.class, () -> new Account(null, "0042", Money.ZERO, Money.DOLLAR));		
	}
	
	public void testg() {
		assertException(NullPointerException.class, () -> new Account("Bovik, Harry Q.", null, Money.ZERO));
		assertException(NullPointerException.class, () -> new Account("Bovik, Harry Q.", "0042", null, Money.ZERO));		
	}
	
	public void testh() {
		assertException(NullPointerException.class, () -> new Account("Bovik, Harry Q.", Money.ZERO, null));
		assertException(NullPointerException.class, () -> new Account("Bovik, Harry Q.", "0042", Money.ZERO, null));		
	}
	
	public void testi() {
		assertException(NullPointerException.class, () -> new Account("Boyland, John", null, Money.ZERO, Money.DOLLAR));				
	}
	
	public void testj() {
		assertException(IllegalArgumentException.class, () -> new Account("Boyland, John", "042", Money.ZERO, Money.ZERO ));
	}
	
	/// testk-x test adjust
	
	public void testk() {
		a = new Account("Boyland John");
		a.adjust(Money.ZERO, false);
		assertEquals(Money.ZERO, a.getCurrent());
		assertException(OverdraftException.class, () -> a.adjust(new Money(-10), false));
		assertEquals(Money.ZERO, a.getCurrent());
	}
	
	public void testl() {
		a = new Account("Boyland John");
		try {
			a.adjust(new Money(-3.14), false);
		} catch (OverdraftException ex) {
			assertSame(a, ex.getAccount());
			assertEquals(new Money(3.14), ex.getAmount());
		}
	}
	
	public void testm() {
		a = new Account("Boyland John");
		a.adjust(new Money(-3.14), true);
		assertEquals(new Money(-3.14), a.getCurrent());
	}
	
	public void testn() {
		a = new Account("Boyland, John", new Money(5), new Money(0));
		a.adjust(new Money(3.14), false);
		assertEquals(new Money(3.14), a.getCurrent());
		assertEquals(new Money(5), a.getMinimum());
		a.adjust(new Money(0.0), false);
		a.adjust(new Money(10), false);
		assertEquals(new Money(13.14), a.getCurrent());
	}
	
	public void testo() {
		a = new Account("Boyland, John", new Money(5), new Money(1));
		try {
			a.adjust(Money.CENT.negate(), false);
			assertFalse("Adjust should have thrown an exception", true);
		} catch (OverdraftException ex) {
			assertSame(a, ex.getAccount());
			assertEquals(new Money(4.01), ex.getAmount());
		}
		assertEquals(Money.DOLLAR, a.getCurrent());
		assertEquals(new Money(5), a.getMinimum());
	}
	
	public void testp() {
		a = new Account("Boyland John", new Money(5), new Money(1));
		a.adjust(Money.CENT.negate(), true);
		assertEquals(new Money(0.99), a.getCurrent());
		assertEquals(new Money(5), a.getMinimum());
	}
	
	public void testq() {
		a = new Account("Boyland, John", new Money(0), new Money(0));
		Money max = new Money(Long.MAX_VALUE/100d);
		a.adjust(max, false);
		assertEquals(max, a.getCurrent());
		a.adjust(max.negate(), false);
		assertEquals(Money.ZERO, a.getCurrent());
		a.adjust(max.negate(), true);
		assertEquals(max.negate(), a.getCurrent());
		assertEquals(new Money(0), a.getMinimum());
	}
	
	public void testr() {
		a = new Account("Boyland, John", new Money(5), new Money(20));
		a.adjust(new Money(-15), false);		
	}
	
	public void tests() {
		a = new Account("Boyland, John", new Money(5), new Money(20));
		try {
			a.adjust(new Money(-15.01), false);
			assertFalse("Should have thrown an exception", true);
		} catch (OverdraftException ex) {
			assertSame(a, ex.getAccount());
			assertEquals(Money.CENT, ex.getAmount());
		}
	}
	
	public void testt() {
		a = new Account("Boyland, John", new Money(5), new Money(20));
		a.adjust(new Money(-15.01), true);
		assertEquals(new Money(4.99), a.getCurrent());
	}
	
	public void testu() {
		a = new Account("Boyland, John", new Money(5), new Money(20));
		a.adjust(new Money(-Long.MAX_VALUE/100d), true);
	}
	
	public void testv() {
		a = new Account("Boyland, John", new Money(5), new Money(20));
		Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(17.14));
		assertException(ArithmeticException.class, () -> a.adjust(lots, true));
	}
	
	public void testw() {
		 a = new Account("Boyland, John", new Money(-10_000.0), new Money(-1000));
		 a.adjust(new Money(-1000), false); // yes I can charge $1000 more
		 assertEquals(new Money(-2000), a.getCurrent());
		 a.adjust(Money.DOLLAR, false); // minimum payment (bad idea)
		 assertEquals(new Money(-1999), a.getCurrent());
		 try {
			 a.adjust(new Money(-8002), false);
			 assertFalse("Went over credit limit", true);
		 } catch (OverdraftException ex) {
			 assertEquals(Money.DOLLAR, ex.getAmount());
		 }
		 assertEquals(new Money(-1999), a.getCurrent());
		 a.adjust(new Money(-9995.88), true); // 
		 assertEquals(new Money(-11994.88), a.getCurrent());
	}
	
	public void testx() {
		 a = new Account("Boyland, John", new Money(-10_000.0), new Money(-1000));
		 Money lots = new Money(Long.MAX_VALUE/100d).sub(new Money(31.459));
		 assertException(ArithmeticException.class, () -> a.adjust(lots.negate(), true));				
		 assertEquals(new Money(-1000), a.getCurrent());
	}
}
