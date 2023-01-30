

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Money;

public class TestMoney extends LockedTestCase {
    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
        try {
                r.run();
                assertFalse("Exception should have been thrown",true);
        } catch (RuntimeException ex) {
                assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
        }       
    }   

    protected static void assertNegative(String message, int value) {
    	assertTrue(message + " should be negative: " + value, value < 0);
    }

    protected static void assertPositive(String message, int value) {
    	assertTrue(message + " should be positive: " + value, value > 0);
    }
    
    
    /// Locked tests
    
    public void test() {
    	// Read assignment for how money is convert to a string!
    	assertEquals(Ts(1278706206), Money.CENT.toString());
    	assertEquals(Ts(957149427), new Money(123.456789).toString());
    	assertEquals(Ts(1584097500), new Money(1000.01).negate().toString());
    	assertEquals(Ts(1081127597), new Money(10.00).sub(new Money(12.34)).toString());
    	// Check homework for mul and div (make sure you know what type they return!)
    	assertEquals(Ts(326464501), "" + Money.DOLLAR.mul(-1.234));
    	assertEquals(Ts(797582914), "" + Money.DOLLAR.div(new Money(-0.08)));
    }

    
    // 92 quadrillion dollars is the largest amount we can represent, or more precisely:
	private static final double MAX_MONEY = 92_233_720_368_547_758.07 ;
	// A double cannot represent the previous value precisely; the best we can do is:
	private static final double MAX_CHECK = 92_233_720_368_547_760.0;
	
	
	/// test0x: tests of the constructor
	
	public void test00() {
		assertEquals(0.0, new Money(0.0).asDouble());
	}
	
	public void test01() {
		assertEquals(0.01, new Money(0.01).asDouble());
	}
	
	public void test02() {
		assertEquals(0.20, new Money(0.2).asDouble());
	}
	
	public void test03() {
		assertEquals(0.33, new Money(1.0/3).asDouble());
	}
	
	public void test04() {
		assertEquals(4.0, new Money(4).asDouble());
	}
	
	public void test05() {
		assertEquals(1.0, new Money(0.999).asDouble());
	}
	
	public void test06() {
		assertEquals(-314.16, new Money(-314.159).asDouble());
	}
	
	public void test07() {
		assertEquals(MAX_CHECK, MAX_MONEY); // demonstrates lack of precision
		assertEquals(MAX_CHECK, new Money(MAX_MONEY).asDouble());
		assertEquals(-MAX_CHECK, new Money(-MAX_MONEY).asDouble());
	}
	
	public void test08() {
		assertException(ArithmeticException.class, () -> new Money(MAX_MONEY+10));
		assertException(ArithmeticException.class, () -> new Money(MAX_MONEY*1.5));
	}
	
	public void test09() {
		assertException(ArithmeticException.class, () -> new Money(-MAX_MONEY-10));		
		assertException(ArithmeticException.class, () -> new Money(-MAX_MONEY*1.5));		
	}
	
	
	/// test1x: tests of isPositive and isNegative
	
	public void test20() {
		assertFalse(Money.ZERO.isPositive());
		assertFalse(Money.ZERO.isNegative());
	}
	
	public void test21() {
		assertTrue(Money.CENT.isPositive());
		assertTrue(Money.DOLLAR.isPositive());
	}
	
	public void test22() {
		assertFalse(new Money(-0.01).isPositive());
		assertFalse(new Money(-1_000_000).isPositive());
	}
	
	public void test23() {
		assertTrue(new Money(MAX_MONEY).isPositive());
		assertFalse(new Money(-MAX_MONEY).isPositive());
	}
	
	public void test24() {
		assertFalse(Money.CENT.isNegative());
		assertFalse(Money.DOLLAR.isNegative());
	}
	
	public void test25() {
		assertTrue(new Money(-0.01).isNegative());
		assertTrue(new Money(-1_000_000).isNegative());
	}
	
	public void test26() {
		assertFalse(new Money(MAX_MONEY).isNegative());
		assertTrue(new Money(-MAX_MONEY).isNegative());
	}
	
	
	/// test3x: test equals
	
	public void test30() {
		assertEquals(Money.ZERO, Money.ZERO);
		assertFalse(Money.ZERO.equals(Money.DOLLAR));
		assertFalse(Money.CENT.equals(Money.ZERO));
	}
	
	public void test31() {
		assertTrue(new Money(0).equals(Money.ZERO));
		assertEquals(Money.ZERO, new Money(0));
	}
	
	public void test32() {
		assertTrue(new Money(1.00).equals(Money.DOLLAR));
		assertTrue(new Money(1.00).equals(new Money(1.0001)));
	}
	
	public void test33() {
		assertTrue(new Money(3.14).equals(new Money(3.14)));
		assertTrue(new Money(1_000_000.01).equals(new Money(1_000_000.01)));
		assertTrue(new Money(1_000_000_000_000.0).equals(new Money(1_000_000_000_000.0)));
	}
	
	public void test34() {
		assertFalse(new Money(1_000_000_000_000.0).equals(new Money(1_000_000_000_000.01)));		
	}
	
	public void test35() {
		assertFalse(new Money(1_000_000_000_000.0).equals(new Money(-1_000_000_000_000.0)));				
	}
	
	public void test36() {
		assertTrue(new Money(MAX_MONEY).equals(new Money(MAX_CHECK)));
		assertTrue(new Money(-MAX_MONEY).equals(new Money(-MAX_CHECK)));
	}

	public void test37() {
		assertFalse(new Money(3.14).equals(null));
		assertFalse(new Money(3.14).equals(Money.ZERO));
	}

	// the following tests should pass because the code we gave you should work

	public void test38() {
		assertEquals(new Money(3.14).hashCode(), new Money(3.14).hashCode());
		assertFalse(Money.CENT.hashCode() == Money.DOLLAR.hashCode());
	}
	
	public void test39() {
		assertEquals(new Money(12345678.90).hashCode(), new Money(12345678.90).hashCode());
		assertFalse(new Money(12345678.90).hashCode() == new Money(12345678.91).hashCode());
	}

	
	/// test4x: test add
	
	public void test40() {
		Money one = Money.DOLLAR.add(Money.ZERO);
		assertTrue(one != Money.DOLLAR);
		assertEquals(1.0, one.asDouble());
	}
	
	public void test41() {
		assertEquals(2.0, Money.DOLLAR.add(Money.DOLLAR).asDouble());
		assertEquals(1.0, Money.DOLLAR.asDouble());
	}
	
	public void test42() {
		assertEquals(1000.01, new Money(1000).add(Money.CENT).asDouble());
	}
	
	public void test43() {
		assertEquals(-999.99, new Money(-1000).add(Money.CENT).asDouble());
	}
	
	public void test44() {
		assertEquals(new Money(-999.99), Money.CENT.add(new Money(-1000)));
	}
	
	public void test45() {
		assertEquals(new Money(-3), new Money(-1).add(new Money(-2)));
	}
	
	// the following tests may fail if you are using doubles to do the work
	
	public void test46() {
		Money lots = new Money(MAX_MONEY);
		Money less = lots.add(new Money(-0.01));
		assertFalse(lots.equals(less));
		assertEquals(lots, less.add(Money.CENT));
	}
	
	public void test47() {
		Money lots = new Money(MAX_MONEY);
		assertException(ArithmeticException.class, () -> lots.add(Money.CENT));
		assertException(ArithmeticException.class, () -> lots.add(Money.DOLLAR));
	}
	
	public void test48() {
		Money bad = new Money(-MAX_MONEY);
		assertEquals(bad, bad.add(Money.ZERO)); //fails (-max money issue)
		Money almostAsBad = bad.add(Money.CENT);
		assertFalse(bad.equals(almostAsBad));
		assertEquals(bad, almostAsBad.add(new Money(-0.01)));
	}
	
	public void test49() {
		Money bad = new Money(-MAX_MONEY);
		assertException(ArithmeticException.class, () -> bad.add(new Money(-0.01)));
		assertException(ArithmeticException.class, () -> bad.add(new Money(-1)));		
	}
	
	
	/// test5x: tests of negate and sub
	
	public void test50() {
		assertEquals(Money.ZERO, Money.ZERO.negate());
	}
	
	public void test51() {
		assertEquals(new Money(-3.14), new Money(3.14).negate());
	}
	
	public void test52() {
		assertEquals(new Money(1000.01), new Money(-1000.01).negate());
	}
	
	public void test53() {
		assertEquals(new Money(-MAX_MONEY), new Money(MAX_MONEY).negate()); //fails (-max money issue)
		assertEquals(new Money(MAX_MONEY), new Money(-MAX_MONEY).negate()); // gos to tostring?? v *4
	}
	
	
	public void test54() {
		assertEquals(Money.ZERO, Money.DOLLAR.sub(Money.DOLLAR));
	}
	
	public void test55() {
		assertEquals(new Money(999.99), new Money(1000).sub(Money.CENT));
	}
	
	public void test56() {
		assertEquals(new Money(20.00), new Money(15).sub(new Money(-5)));
	}
	
	public void test57() {
		Money lots = new Money(MAX_MONEY);
		Money less = lots.sub(Money.CENT);
		assertFalse(lots.equals(less));
		assertEquals(lots, less.add(Money.CENT));
	}
	
	public void test58() {
		Money bad = new Money(-MAX_MONEY);
		assertException(ArithmeticException.class, () -> bad.sub(Money.CENT));
	}
	
	public void test59() {
		Money b0 = new Money(-9.2233720368547748E16);
		Money b1 = b0.sub(Money.CENT);
		assertFalse(b0.equals(b1));
		Money b2 = b0.sub(Money.DOLLAR);
		assertFalse(b1.equals(b2));
		Money b3 = b0.sub(new Money(10.00));
		assertFalse(b3.equals(b2));
		Money b4 = b0.sub(new Money(10.10));
		assertFalse(b3.equals(b4));
		Money b5 = b0.sub(new Money(10.20));
		assertFalse(b5.equals(b4));
		Money b6 = b0.sub(new Money(10.23));
		assertFalse(b5.equals(b6));
		assertEquals(new Money(-MAX_MONEY), b6); //fails (-max money issue)
	}
	
	
	/// test6x: tests of toString
	
	public void test60() {
		assertEquals("$0.00", Money.ZERO.toString());
	}
	
	public void test61() {
		assertEquals("$0.01", Money.CENT.toString());
		assertEquals("$0.06", new Money(0.061).toString());
	}
	
	public void test62() {
		assertEquals("$0.10", new Money(0.1).toString());
		assertEquals("$0.62", new Money(0.62).toString());
	}
	
	public void test63() {
		assertEquals("$1.00", Money.DOLLAR.toString());
		assertEquals("$6.30", new Money(6.3).toString());
	}
	
	public void test64() {
		assertEquals("($0.01)", Money.CENT.negate().toString());
		assertEquals("($0.64)", new Money(-0.64).toString());
	}
	
	public void test65() {
		assertEquals("($0.90)", new Money(-.9).toString());
		assertEquals("($6.50)", new Money(-6.5).toString());
	}
	
	public void test66() {
		assertEquals("$1000.01", new Money(1000.01).toString());
		assertEquals("$600600.60", new Money(600600.600).toString());
	}
	
	public void test67() {
		Money max = new Money(MAX_MONEY);
		assertEquals("$92233720368547758.07", max.toString());
		assertEquals("$92233720368547757.06", max.sub(new Money(1.01)).toString());
	}
	
	public void test68() {
		Money min = new Money(-MAX_MONEY);
		assertEquals("($92233720368547758.07)", min.toString());
		assertEquals("($92233720368547757.06)", min.add(new Money(1.01)).toString());
	}
	
	public void test69() {
		Money tenQuad = new Money(10_000_000_000_000_000.0);
		assertEquals("$10000000000000000.00", tenQuad.toString());
		Money twentyQuad = tenQuad.add(tenQuad);
		assertEquals("$20000000000000000.00", twentyQuad.toString());
		Money fortyQuad = twentyQuad.add(twentyQuad);
		assertEquals("$40000000000000000.00", fortyQuad.toString());
		Money eightyQuad = fortyQuad.add(fortyQuad);
		assertEquals("$80000000000000000.00", eightyQuad.toString());
		Money more = eightyQuad.negate().add(Money.CENT);
		assertEquals("($79999999999999999.99)", more.toString());
	}
	
	
	/// test7x: tests of mul
	
	public void test70() {
		assertEquals(0.0, Money.ZERO.mul(888.90).asDouble());
	}
	
	public void test71() {
		assertEquals(0.0, new Money(1000.71).mul(0.0).asDouble());
	}
	
	public void test72() {
		assertEquals(0.07, new Money(72).mul(0.001).asDouble());
	}
	
	public void test73() {
		assertEquals(Money.CENT, Money.DOLLAR.mul(0.0072));
	}
	
	public void test74() {
		assertEquals(new Money(1_000_000_000_000.01), Money.DOLLAR.mul(1_000_000_000_000.014));
	}
	
	public void test75() {
		assertEquals(new Money(-MAX_MONEY), new Money(MAX_MONEY).mul(-1));
	}
	
	public void test76() {
		assertEquals(new Money(MAX_MONEY), new Money(-MAX_MONEY).mul(-1));
	}
	
	
	/// test8x: tests of div
	
	public void test80() {
		assertEquals(0.0, Money.ZERO.div(new Money(808.08)));
	}
	
	public void test81() {
		assertEquals(1000.81, new Money(1000.81).div(Money.DOLLAR));
	}
	
	public void test82() {
		assertEquals(1.0, new Money(12345678.90).div(new Money(12345678.90)));
	}
	
	public void test83() {
		assertEquals(50.0, Money.DOLLAR.div(new Money(0.02)));
	}
	
	public void test84() {
		assertEquals(3.333333333333333333, new Money(10).div(new Money(3)));
	}

	public void test85() {
		assertEquals(-2.0, new Money(-10).div(new Money(5)));
	}
	
	public void test86() {
		assertEquals(17.2, new Money(-86).div(new Money(-5)));
	}
	
	public void test87() {
		assertEquals(Double.POSITIVE_INFINITY, Money.DOLLAR.div(Money.ZERO));
	}
	
	public void test88() {
		assertTrue(Double.isNaN(Money.ZERO.div(Money.ZERO)));
	}
	
	public void test89() {
		assertEquals(1E18, new Money(1E16).div(Money.CENT));
	}
	
	
	/// test9x: tests of compareTo
	
	public void test90() {
		assertEquals(0, Money.ZERO.compareTo(Money.ZERO));
	}
	
	public void test91() {
		assertNegative("", Money.ZERO.compareTo(Money.CENT));
		assertNegative("", Money.ZERO.compareTo(new Money(MAX_MONEY)));
	}
	
	public void test92() {
		assertPositive("", Money.ZERO.compareTo(Money.CENT.negate()));
		assertPositive("", Money.ZERO.compareTo(new Money(-MAX_MONEY)));
	}
	
	public void test93() {
		assertNegative("", Money.CENT.compareTo(Money.DOLLAR));
		assertPositive("", Money.CENT.compareTo(Money.DOLLAR.negate()));
	}

	public void test94() {
		assertNegative("", new Money(-3.14).compareTo(new Money(1.01)));
		assertPositive("", new Money(-0.94).compareTo(new Money(-1000.01)));
	}
	
	public void rest95() {
		assertNegative("", Money.ZERO.compareTo(new Money(1L << 40)));
		assertPositive("", new Money((1L << 40)/100d).compareTo(Money.ZERO));
	}
	
	public void test96() {
		assertEquals(0, new Money(MAX_MONEY).compareTo(new Money(MAX_MONEY)));
		assertEquals(0, new Money(-MAX_MONEY).compareTo(new Money(-MAX_MONEY)));
	}
	
	public void test97() {
		assertNegative("", new Money(-MAX_MONEY).compareTo(new Money(MAX_MONEY)));
		assertPositive("", new Money(MAX_MONEY).compareTo(new Money(-MAX_MONEY)));
	}
}
