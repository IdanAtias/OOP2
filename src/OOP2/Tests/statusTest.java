package OOP2.Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import OOP2.Provided.Person;
import OOP2.Provided.Status;
import OOP2.Solution.PersonImpl;
import OOP2.Solution.StatusImpl;

public class statusTest {
	private static Status s,s2,s3;
	private static Person p, p2, p3;

	@Before
	public void setUp() throws Exception {
		p = new PersonImpl(123456789, "Tester Student");
		p2 = new PersonImpl(987654321, "Liker Student");
		p3 = new PersonImpl(111111111, "Disliker Student");
		s = new StatusImpl(p, "I have no life", 1111);
		s2 = new StatusImpl(p, "I have no Friends",1111);
		s3 = new StatusImpl(p3, "I Code all Day", 1111);
	}

	@Test
	public void statusGetIdTest() {
		assertTrue(s.getId() == 1111);
	}

	@Test
	public void statusGetPublisherTest() {
		assertTrue(s.getPublisher() == p);
	}

	@Test
	public void statusGetContentTest() {
		assertTrue(s.getContent() == "I have no life");
	}

	@Test
	public void statusLikeTest() {
		s.like(p);
		s.like(p);
		assertTrue(s.getLikesCount() == 1);
		s.like(p2);
		assertTrue(s.getLikesCount() == 2);
	}

	@Test
	public void statusUnlikeTest() {
		s.unlike(p);
		assertTrue(s.getLikesCount() == 0);
		s.like(p2);
		assertTrue(s.getLikesCount() == 1);
		s.unlike(p2);
		assertTrue(s.getLikesCount() == 0);
		s.like(p);
		s.like(p2);
		s.like(p3);
		assertTrue(s.getLikesCount() == 3);
		s.unlike(p3);
		assertTrue(s.getLikesCount() == 2);
	}

	@Test
	public void statusGetLikesCountTest() {
		assertTrue(s.getLikesCount() == 0);
		s.like(p);
		s.like(p2);
		s.like(p3);
		assertTrue(s.getLikesCount() == 3);
		s.unlike(p);
		assertTrue(s.getLikesCount() == 2);
		s.unlike(p2);
		assertTrue(s.getLikesCount() == 1);
		s.unlike(p3);
		assertTrue(s.getLikesCount() == 0);
	}
	@Test
	public void statusComareTest() {
		assertTrue(s.equals(s2));
		assertFalse(s2.equals(s3));	
	}
}
