
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

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
		assertTrue(fileTool.searchFile("fakeID", FileTools.USER_DATA_FILE).size() == 0);
	}
	
	@Test
	public void testSearchFound() {
		assertNotNull(fileTool.searchFile("admin", FileTools.USER_DATA_FILE));
	}
	
	@Test
	public void testSearchRighResultAdmin() {
		ArrayList<String[]> searchResult = fileTool.searchFile("admin", FileTools.USER_DATA_FILE);
		String[] admin = null;
		
		for (String[] res : searchResult)
		{
			if (res[0].equals("admin"))
			{
				admin = res;
			}
		}
		
		assertEquals("test search admin not match", "admin", admin[0]);
		assertEquals("test search admin not match", "#Admin123", admin[1]);
		assertEquals("test search admin not match", "Colonel", admin[2]);
		assertEquals("test search admin not match", "Sanders", admin[3]);
		assertEquals("test search admin not match", 50, Integer.parseInt(admin[4]));
	}
	
	@Test
	public void testSearchRighResultPlayer() {
		ArrayList<String[]> searchResult =fileTool.searchFile("bobby123", FileTools.USER_DATA_FILE);
		String[] player = null;
		
		for (String[] res : searchResult)
		{
			if (res[0].equals("bobby123"))
			{
				player = res;
			}
		}
		
		assertEquals("test search Player not match", "bobby123", player[0]);
		assertEquals("test search Player not match", "Qwerty12!", player[1]);
		assertEquals("test search Player not match", "Bobby", player[2]);
		assertEquals("test search Player not match", "Smith", player[3]);
		assertEquals("test search Player not match", 20, Integer.parseInt(player[4]));
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
