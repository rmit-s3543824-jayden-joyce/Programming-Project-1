import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import model.Shares;
import model.TradingAcc;
import model.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestTrAcc {

	TradingAcc trAcc;
	Shares testShare;
	
	@Before
	public void Before() throws Exception {
		trAcc = new TradingAcc("testing");
		trAcc.setCurrBal(TradingAcc.INIT_BAL);
		testShare = new Shares("TST", "testCompany", new BigDecimal(5.5));
	}
	
	@Test
	public void testBuy() {
		Transaction trans = trAcc.buyShares(testShare);
		assertNotNull(trans);
		assertEquals(trans.getID(), "testing");
		assertEquals(trans.getASXcode(), "TST");
		assertEquals(trans.getCompName(), "testCompany");
		assertEquals(trans.getTransType(), Transaction.TransType.BUYING);
	}
	
	//@Test
	public void testBuyFail() {
		trAcc.setCurrBal(new BigDecimal(2));
		Transaction trans = trAcc.buyShares(testShare);
		assertNull(trans);
	}
	
	//@Test
	public void testSell() {
		trAcc.buyShares(testShare);
		Transaction trans = trAcc.sellShares(testShare);
		assertNotNull(trans);
		assertEquals(trans.getID(), "testing");
		assertEquals(trans.getASXcode(), "TST");
		assertEquals(trans.getCompName(), "testCompany");
		assertEquals(trans.getTransType(), Transaction.TransType.SELLING);
	}
	
	//@Test
	public void testSellFail() {
		Transaction trans = trAcc.sellShares(testShare);
		assertNull(trans);
	}

	@After
	public void After() throws Exception {
	}

	

}
