package OOP2.Tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.Person;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Solution.PersonImpl;

public class personTest {
	private static Person p1, p2, p3, p4;

	@Before
	public void setUp() throws Exception {
		p1 = new PersonImpl(123456789, "Tester Student");
		p2 = new PersonImpl(987654321, "Liker Student");
		p3 = new PersonImpl(111111111, "Disliker Student");
		p4 = new PersonImpl(100, "Atudai Student");
	}

	@Test
	public void personGetIdTest() {
		assertTrue(p3.getId() == 111111111);
	}

	@Test
	public void personGetNameTest() {
		assertTrue(p1.getName() == "Tester Student");
	}

	@Test
	public void personPostStatusTest() {
		Status s1 = p1.postStatus("I have no life");
		Status s2 = p2.postStatus("I have no Friends");
		Status s3 = p3.postStatus("I Code all Day");
		Status s4 = p1.postStatus("I have no life");
		assertTrue(s1.getPublisher().equals(p1));
		assertTrue(s2.getContent() == "I have no Friends");
		assertTrue(s3.getLikesCount() == 0);
		assertFalse(s4.equals(s1));
	}

	@Test
	public void personAddFriendTest() {
		try {
			p1.addFriend(p2);
		} catch (SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
		try {
			p1.addFriend(p1);
			fail("Added himself as a friend");
		} catch (SamePersonException e) {
		} catch (ConnectionAlreadyExistException e) {
			fail("Wrong exception thrown");
		}
		try {
			p1.addFriend(p2);
			fail("Added existing friend");
		} catch (SamePersonException e) {
			fail("Wrong exception thrown");
		} catch (ConnectionAlreadyExistException e) {
		}
	}

	@Test
	public void personGetFriendTest() {
		try {
			p1.addFriend(p2);
			p1.addFriend(p3);
			p1.addFriend(p4);
			p3.addFriend(p2);
		} catch (SamePersonException | ConnectionAlreadyExistException e) {
			fail("Unexpected exception thrown");
		}
		Collection<Person> c = p1.getFriends();
		assertTrue(c.contains(p2) && c.contains(p3) && c.contains(p4));
		assertTrue(p3.getFriends().contains(p2));
	}

	@Test
	public void personGetStatusesRecentTest() {
		Status[] s = new Status[4];
		s[0] = p1.postStatus("I have no life");
		s[1] = p1.postStatus("I have no Friends");
		s[2] = p1.postStatus("I Code all Day");
		s[3] = p1.postStatus("I have no life");
		int i = 3;
		for (Status c : p1.getStatusesRecent()) {
			assertTrue(c.equals(s[i--]));
		}
	}
	@Test
	public void personGetStatusesPopularTest() {
		Status[] s = new Status[4];
		s[0] = p1.postStatus("I have no life");
		s[1] = p1.postStatus("I have no Friends");
		s[2] = p1.postStatus("I Code all Day");
		s[3] = p1.postStatus("I have no life at all");
		s[2].like(p1);
		s[2].like(p2);
		s[2].like(p3);
		s[2].like(p4);
		s[3].like(p1);
		s[3].like(p2);
		s[3].like(p3);
		Iterator<Status> i = p1.getStatusesPopular().iterator();
			assertTrue(i.next().equals(s[2]));
			assertTrue(i.next().equals(s[3]));
			assertTrue(i.next().equals(s[1]));
			assertTrue(i.next().equals(s[0]));
	}
}
