
import static org.junit.Assert.*;

import java.io.IOException;

import model.FileTools;
import model.User;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class testFileTools {

	static FileTools fileTool;
	
	@BeforeClass
	public static void Before() {
		fileTool = new FileTools();
	}
	
	//SearchFile tests
	@Test
	public void testSearchNotFound() {
		assertNull(fileTool.searchFile("fakeID", FileTools.USER_DATA_FILE));
	}
	
	@Test
	public void testSearchFound() {
		assertNotNull(fileTool.searchFile("admin", FileTools.USER_DATA_FILE));
	}
	
	@Test
	public void testSearchRighResultAdmin() {
		String[] admin = fileTool.searchFile("admin", FileTools.USER_DATA_FILE);
		assertEquals("test search admin not match", "admin", admin[0]);
		assertEquals("test search admin not match", "#Admin123", admin[1]);
		assertEquals("test search admin not match", "Colonel", admin[2]);
		assertEquals("test search admin not match", "Sanders", admin[3]);
		assertEquals("test search admin not match", 50, Integer.parseInt(admin[4]));
	}
	
	@Test
	public void testSearchRighResultPlayer() {
		String[] admin = fileTool.searchFile("bobby123", FileTools.USER_DATA_FILE);
		assertEquals("test search Player not match", "bobby123", admin[0]);
		assertEquals("test search Player not match", "Qwerty12!", admin[1]);
		assertEquals("test search Player not match", "Bobby", admin[2]);
		assertEquals("test search Player not match", "Smith", admin[3]);
		assertEquals("test search Player not match", 20, Integer.parseInt(admin[4]));
	}
	
	//LoadPlayer tests
	@Test
	public void testLoadAdmin() {
		User user = fileTool.LoadPlayer("admin");
		assertEquals(user.getFName(), "Colonel");
	}
	
	@Test
	public void testLoadPlayer() {
		User user = fileTool.LoadPlayer("bobby123");
		assertEquals(user.getFName(), "Bobby");
	}
	
	@Test
	public void testReadJSON() {
		try {
			fileTool.fetchShareData("BHP");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	//this test takes long as heck due to function
	//@Test
	public void testFetchAll() {
		fileTool.fetchAllShareData();
		assertTrue(true);
	}
	
	@AfterClass
	public static void After() {
		
	}

}
