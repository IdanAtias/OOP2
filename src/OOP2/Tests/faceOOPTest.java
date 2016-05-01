package OOP2.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.ConnectionDoesNotExistException;
import OOP2.Provided.FaceOOP;
import OOP2.Provided.PersonAlreadyInSystemException;
import OOP2.Provided.PersonNotInSystemException;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Provided.StatusIterator;
import OOP2.Solution.FaceOOPImpl;

public class faceOOPTest {
	private static FaceOOP f;
	@Before
	public void setUp() throws Exception {
		f = new FaceOOPImpl();
		f.joinFaceOOP(123456789, "Tester Student");
		f.joinFaceOOP(987654321, "Liker Student");
		f.joinFaceOOP(111111111, "Disliker Student");
		f.joinFaceOOP(100, "Atudai Student");
	}

	@Test
	public void faceOOPJoinFaceOOPTest() {
		try {
			assertTrue(f.joinFaceOOP(31171111, "Arik Zeevi").getId() == 31171111);
		} catch (PersonAlreadyInSystemException e) {
			fail("Unexpected exception thrown");
		}
		try {
			f.joinFaceOOP(31171111, "Yarden Gerbi");
			fail("Added same person");
		} catch (PersonAlreadyInSystemException e) {
		}
	}

	@Test
	public void faceOOPSizeTest() {
		assertTrue(f.size() == 4);
	}

	@Test
	public void faceOOPGetUserTest() {
		try {
			assertTrue(f.getUser(123456789).getName() == "Tester Student");
		} catch (PersonNotInSystemException e) {
			fail("Person not found");
		}
		try {
			f.getUser(12345789);
			fail("Found non existing person");
		} catch (PersonNotInSystemException e) {
		}
	}

	@Test
	public void faceOOPaddFriendshipTest() {
		try {
			f.addFriendship(f.getUser(123456789), f.getUser(987654321));
			f.addFriendship(f.getUser(111111111), f.getUser(100));
			f.addFriendship(f.getUser(123456789), f.getUser(100));
			assertTrue(f.getUser(123456789).getFriends().size() == 2);
			assertTrue(f.getUser(100).getFriends().size() == 2);
			assertTrue(f.getUser(987654321).getFriends().size() == 1);
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
		try {
			f.addFriendship(f.getUser(100), f.getUser(111111111));
			fail("Added existing connection");
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
		}
		try {
			f.addFriendship(f.getUser(100), f.getUser(100));
			fail("Added same person");
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
		}
		try {
			f.addFriendship(f.getUser(100), f.getUser(3));
			fail("Added non existing person");
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
		}
	}

	@Test
	public void faceOOGetFeedByRecentTest() {
		Status[] s = new Status[8];
		try {
			f.addFriendship(f.getUser(111111111), f.getUser(123456789));
			f.addFriendship(f.getUser(111111111), f.getUser(987654321));
			f.addFriendship(f.getUser(111111111), f.getUser(100));
			s[0] = f.getUser(111111111).postStatus("I have no life");
			s[1] = f.getUser(123456789).postStatus("I have no Friends");
			s[2] = f.getUser(987654321).postStatus("I Code all Day");
			s[3] = f.getUser(100).postStatus("I dont know what to do");
			s[4] = f.getUser(123456789).postStatus("Want to jump from amado");
			s[5] = f.getUser(100).postStatus("I dont know how to live");
			s[6] = f.getUser(123456789).postStatus("I dont know what to write");
			s[7] = f.getUser(987654321).postStatus("Status");
			StatusIterator i = f.getFeedByRecent(f.getUser(111111111));
			i.next().equals(s[5]);
			i.next().equals(s[3]);
			i.next().equals(s[6]);
			i.next().equals(s[4]);
			i.next().equals(s[2]);
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
	}

	@Test
	public void faceOOGetFeedByPopularTest() {
		Status[] s = new Status[8];
		try {
			f.addFriendship(f.getUser(111111111), f.getUser(123456789));
			f.addFriendship(f.getUser(111111111), f.getUser(987654321));
			f.addFriendship(f.getUser(111111111), f.getUser(100));
			s[0] = f.getUser(111111111).postStatus("I have no life");
			s[1] = f.getUser(123456789).postStatus("I have no Friends");
			s[2] = f.getUser(987654321).postStatus("I Code all Day");
			s[3] = f.getUser(100).postStatus("I dont know what to do");
			s[4] = f.getUser(123456789).postStatus("Want to jump from amado");
			s[5] = f.getUser(100).postStatus("I dont know how to live");
			s[6] = f.getUser(123456789).postStatus("I dont know what to write");
			s[7] = f.getUser(987654321).postStatus("Status");
			s[0].like(f.getUser(111111111));
			s[0].like(f.getUser(123456789));
			s[1].like(f.getUser(100));
			s[1].like(f.getUser(123456789));
			s[2].like(f.getUser(123456789));
			s[2].like(f.getUser(100));
			s[2].like(f.getUser(987654321));
			s[6].like(f.getUser(111111111));
			s[6].like(f.getUser(100));
			s[6].like(f.getUser(987654321));
			s[6].like(f.getUser(123456789));
			s[7].like(f.getUser(100));
			s[5].like(f.getUser(100));
			StatusIterator i = f.getFeedByPopular(f.getUser(111111111));
			i.next().equals(s[5]);
			i.next().equals(s[3]);
			i.next().equals(s[6]);
			i.next().equals(s[1]);
			i.next().equals(s[4]);
			i.next().equals(s[2]);
			i.next().equals(s[7]);
		} catch (PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
	}
	@Test
	public void faceOOPGetRankTest() {
		try {
			f.joinFaceOOP(5, "Five");
			f.joinFaceOOP(6, "Six");
			f.joinFaceOOP(7, "Seven");
			f.joinFaceOOP(8, "Eight");
			f.joinFaceOOP(0, "Not Connected");
			f.addFriendship(f.getUser(111111111), f.getUser(123456789));
			f.addFriendship(f.getUser(111111111), f.getUser(987654321));
			f.addFriendship(f.getUser(111111111), f.getUser(7));
			f.addFriendship(f.getUser(123456789), f.getUser(100));
			f.addFriendship(f.getUser(100), f.getUser(5));
			f.addFriendship(f.getUser(5), f.getUser(987654321));
			f.addFriendship(f.getUser(5), f.getUser(6));
			f.addFriendship(f.getUser(6), f.getUser(7));
			f.addFriendship(f.getUser(7), f.getUser(8));
		} catch (PersonAlreadyInSystemException | PersonNotInSystemException | SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
		try {
			assertTrue(f.rank(f.getUser(111111111), f.getUser(8)) == 2);
			assertTrue(f.rank(f.getUser(123456789), f.getUser(7)) == 2);
			assertTrue(f.rank(f.getUser(100), f.getUser(987654321)) == 2);
			assertTrue(f.rank(f.getUser(123456789), f.getUser(6)) == 3);
			assertTrue(f.rank(f.getUser(5), f.getUser(5)) == 0);
			assertTrue(f.rank(f.getUser(0), f.getUser(0)) == 0);
		} catch (PersonNotInSystemException | ConnectionDoesNotExistException e) {
			fail("Unexpected exception thrown");			
		}
		try {
			f.rank(f.getUser(0), f.getUser(5));
			fail("Found connection that does not exists");
		} catch ( ConnectionDoesNotExistException e) {
		} catch (PersonNotInSystemException e) {
			fail("Wrong exception");
		}
		try {
			f.rank(f.getUser(0), f.getUser(11));
			fail("Found Person that does not exists");
		} catch (PersonNotInSystemException e) {
		} catch (ConnectionDoesNotExistException e) {
			fail("Wrong exception");
		}	
	}
}
