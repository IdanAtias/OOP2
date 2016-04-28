package OOP2.Tests;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.Person;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Solution.PersonImpl;
import OOP2.Solution.StatusImpl;;

public class NivTestStatusAndPerson {

	/* *********************************************
	 ****************** SETUP***********************
	 ***********************************************/
	Person pA, pB, pC;
	Status sA, sB, sC, sD, sE;
	String stA, stB, stC, stD, stE, nA, nB, nC;

	/**
	 * Sets up basic Person's and Statuses, for test those classes. This also
	 * functions as a test for the basic cases of the constructors. Should this
	 * fail, no test is guaranteed run properly.
	 */
	@Before
	public void basicSetup() {
		stA = "I'm new here";
		stB = "A first message";
		stC = "My second message";
		stD = "ALL IN CAPITAL LETTERS!!!";
		stE = "now there are no capital letters";
		nA = "Anne";
		nB = "Ben";
		nC = "Cody";
		pA = new PersonImpl(1, nA);
		pB = new PersonImpl(2, nB);
		pC = new PersonImpl(3, nC);
		sA = new StatusImpl(pA, stA, 1);
		sB = new StatusImpl(pB, stB, 1);
		sC = new StatusImpl(pA, stC, 2);
		sD = new StatusImpl(pC, stD, 992);
		sE = new StatusImpl(pC, stE, 7);
	}

	/* ********************************************* 
	 **************TESTS FOR EQUALS()***************
	 ***********************************************/

	/**
	 * A basic test for equals() 2 statuses are equals if they have the same
	 * publisher and the same id
	 */
	@Test
	public void StatusTestEquals() {
		assertTrue(sA.equals(sA));
		assertTrue(!sA.equals(sB));
		assertTrue(!sA.equals(sC));
		Status sA2 = new StatusImpl(pA, "A diffrent Message", 1);
		assertTrue(sA.equals(sA2));
	}

	/**
	 * A basic test for equals() 2 persons should be equals if they have the
	 * same id.
	 */
	@Test
	public void PersontestEquals() {
		Person pA2 = new PersonImpl(1, "A diffrent name");
		assertTrue(pA.equals(pA));
		assertTrue(pA.equals(pA2));
		assertTrue(!pA.equals(pB));
	}

	/**
	 * A basic test for compareTo() Person A should be greater than person B if
	 * his id is greater.
	 */
	@Test
	public void PersonTestCompareTo() {
		assertTrue(pA.compareTo(pA) == 0);
		assertTrue(pA.compareTo(pB) < 0);
		assertTrue(pB.compareTo(pA) > 0);
		Person pA2 = new PersonImpl(1, "Andrew");
		assertTrue(pA.compareTo(pA2) == 0);
	}

	/* ********************************************* 
	 ************TESTS FOR STATUSIMPL***************
	 ***********************************************/

	/**
	 * A basic test for getId()
	 */
	@Test
	public void StatusTestGetId() {
		assertTrue(sA.getId().equals(1));
		assertTrue(sB.getId().equals(1));
		assertTrue(sC.getId().equals(2));
		assertTrue(sD.getId().equals(992));
		assertTrue(sE.getId().equals(7));
		assertTrue(sB.getId().equals(sA.getId()));
	}

	/**
	 * A basic test for getPublisher() using Person's equals function
	 */
	@Test
	public void StatusTestGetPublisher() {
		assertTrue(sA.getPublisher().equals(pA));
		assertTrue(sB.getPublisher().equals(pB));
		assertTrue(sC.getPublisher().equals(pA));
		assertTrue(sD.getPublisher().equals(pC));
		assertTrue(sE.getPublisher().equals(pC));
		assertTrue(sE.getPublisher().equals(sD.getPublisher()));
		assertTrue(sA.getPublisher().equals(sC.getPublisher()));
		assertTrue(!sA.getPublisher().equals(sB.getPublisher()));
	}

	/**
	 * A basic test for getContent() using String.equals()
	 */
	@Test
	public void StatusTestGetContent() {
		assertTrue(sA.getContent().equals(stA));
		assertTrue(sB.getContent().equals(stB));
		assertTrue(sC.getContent().equals(stC));
		assertTrue(sD.getContent().equals(stD));
		assertTrue(sE.getContent().equals(stE));
	}

	/**
	 * A basic test for the three liking functions- like(), dislike() and
	 * getLikesCount(). Tests cases such as the same person liking a status
	 * twice and a person unliking without liking the status before
	 */
	@Test
	public void StatusTestlikes() {
		// testing the like count of a new status
		assertTrue(sA.getLikesCount().equals(0));
		// testing that a person liking a second time doesn't affect the count
		sB.like(pA);
		assertTrue(sB.getLikesCount().equals(1));
		sB.like(pA);
		assertTrue(sB.getLikesCount().equals(1));
		// test that the publisher can like and multiple likes at once
		sC.like(pA);
		sC.like(pB);
		sC.like(pC);
		assertTrue(sC.getLikesCount().equals(3));
		// testing unlike without liking
		sD.like(pA);
		sD.unlike(pB);
		assertTrue(sD.getLikesCount().equals(1));
		// testing unlike after liking twice
		sE.like(pA);
		sE.like(pA);
		sE.unlike(pA);
		assertTrue(sE.getLikesCount().equals(0));
	}

	/**
	 * A stress test for like, unlike and getLikesCount, liking with 4000 people
	 * and then unlinking with them. Tests that getLikesCount always returns the
	 * correct value.
	 */
	@Test
	public void StatusStressTestLikes() {
		Person[] arr = new PersonImpl[4000];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new PersonImpl(i, "test person");
			sA.like(arr[i]);
			assertTrue(sA.getLikesCount().equals(i + 1));
		}
		for (int i = arr.length - 1; i >= 0; i--) {
			sA.unlike(arr[i]);
			assertTrue(sA.getLikesCount().equals(i));
		}
	}

	/* *********************************************
	 ************ TESTS FOR PERSONIMPL**************
	 ***********************************************/

	/**
	 * A basic test for getId()
	 */
	@Test
	public void PersonTestGetId() {
		assertTrue(pA.getId().equals(1));
		assertTrue(pB.getId().equals(2));
		assertTrue(pC.getId().equals(3));
	}

	/**
	 * A basic test for getName() using String.equals()
	 */
	@Test
	public void PersonTestGetName() {
		assertTrue(pA.getName().equals(nA));
		assertTrue(pB.getName().equals(nB));
		assertTrue(pC.getName().equals(nC));
	}

	/**
	 * A basic test for the constructor's edge cases, such as a null name. The
	 * constructor of PersonImpl should always succeed. The test assumes getId()
	 * and getName() work correctly
	 */
	@Test
	public void PersonTestImpl() {
		Person pnull = new PersonImpl(1, null);
		Person pzero = new PersonImpl(0, "name");
		Person pnegative = new PersonImpl(-3, "name");
		assertTrue(pnull.getName() == null);
		assertTrue(pnull.getId().equals(1));
		assertTrue(pzero.getName().equals("name"));
		assertTrue(pzero.getId().equals(0));
		assertTrue(pnegative.getName().equals("name"));
		assertTrue(pnegative.getId().equals(-3));
	}

	/**
	 * A basic test for postStatus() Tests to see the indexing of the posts is
	 * in publishing order and that the content is what was sent to the
	 * function. Uses the Status interface and assumes it works properly
	 */
	@Test
	public void PersonTestPostStatus() {
		// Tests that the statuse's getters return as expected
		Status s1 = pA.postStatus("content");
		assertTrue(s1.getId().equals(0));
		assertTrue(s1.getPublisher().equals(pA));
		assertTrue(s1.getContent().equals("content"));
		/*
		 * Tests that indexing increases as expected And also tests that
		 * s2.equals(sA) returns true (As they have the same publisher and id
		 */
		Status s2 = pA.postStatus("second");
		assertTrue(s2.getId().equals(1));
		assertTrue(s2.equals(sA));
		// Tests that statuses posted for different Person's have different
		// indexing.
		Status sB = pB.postStatus("status");
		assertTrue(sB.getId().equals(0));
	}

	/**
	 * A stress test for posting lots of statuses. Posts 4000 statuses for a
	 * single person. Tests that the index proggrecces as expected
	 */
	@Test
	public void StressTestPostStatus() {
		for (int i = 0; i < 4000; i++) {
			Status tmp = pA.postStatus("test status");
			assertTrue(tmp.getId().equals(i));
		}
	}

	/**
	 * A basic test for addFriend() and it's exceptions. Tests all cases that
	 * throw exceptions as well as proper functioning. Also ensures that
	 * addFriend test whether a person is the same person using equals and not ==
	 * 
	 * Does not assume whether addFriend() is symmetrical or not, according to TA
	 * in charge both options will be accepted
	 */
	@Test
	public void PersonTestAddFriend() {
		// should not throw
		try {
			pA.addFriend(pB);
			pA.addFriend(pC);
			pB.addFriend(pC);
		} catch (Exception e) {
			fail();
		}
		Boolean caught = false;
		// should throw SamePersonException
		try {
			pA.addFriend(pA);
		} catch (SamePersonException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		caught = false;
		try {
			Person pA2 = new PersonImpl(1, nA);
			pA.addFriend(pA2);
		} catch (SamePersonException e) {
			caught = true;
		} catch (Exception e) {
			
		}
		assertTrue(caught);
		caught = false;
		// should throw ConnectionAlreadyExistException
		try {
			pA.addFriend(pB);
		} catch (ConnectionAlreadyExistException e) {
			caught = true;
		} catch (Exception e) {
			
		}
		assertTrue(caught);
		caught = false;
		try {
			Person pB2 = new PersonImpl(2, nB);
			pA.addFriend(pB2);
		} catch (ConnectionAlreadyExistException e) {
			caught = true;
		} catch (Exception e) {

		} finally {
			assertTrue(caught);
		}
	}

	/**
	 * A basic test for getFriends()
	 */
	@Test
	public void PersonTestGetFriends() {
		Collection<Person> friendsA = pA.getFriends();
		assertTrue(friendsA.isEmpty());
		try {
			pA.addFriend(pB);
			pA.addFriend(pC);
			pC.addFriend(pA);
		} catch (Exception e) {
			fail();
		}
		friendsA = pA.getFriends();
		assertFalse(friendsA.isEmpty());
		assertTrue(friendsA.size() == 2);
		Iterator<Person> itr = friendsA.iterator();
		while (itr.hasNext()) {
			Person tmp = itr.next();
			assertTrue(tmp.equals(pB) || tmp.equals(pC));
		}
		Collection<Person> friendsC = pC.getFriends();
		assertTrue(friendsC.size() == 1);
		itr = friendsC.iterator();
		while (itr.hasNext()) {
			Person tmp = itr.next();
			assertTrue(tmp.equals(pA));
		}
	}

	/**
	 * A stress test for adding lots of friend to a single person. Adds a 4000
	 * friends to a single person Tests that no exceptions are thrown and that
	 * the size of the collection returned by getFriends grows consistently
	 */
	@Test
	public void PersonStressTestFriends() {
		for (int i = 1000; i <= 5000; i++) {
			Person tmp = new PersonImpl(i, "test person");
			try {
				pA.addFriend(tmp);
			} catch (Exception e) {
				fail();
			}
			Collection<Person> tmpColl = pA.getFriends();
			assertTrue(tmpColl.size() == i - 999);
		}
	}

	/**
	 * A basic test for getStatusesRecent() Tests that the iteration order is
	 * correct and that iterating multiple times doesn't change the structure
	 */
	@Test
	public void PersonTestGetStatusesRecent() {
		Status[] s = new Status[10];
		for (int i = 0; i < s.length; i++) {
			s[i] = pA.postStatus(((Integer) (i + 1)).toString() + "'th status");
			assertTrue(s[i].getId().equals(i));
		}
		Iterable<Status> col = pA.getStatusesRecent();
		Iterator<Status> itr = col.iterator();
		int i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
		itr = col.iterator();
		i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
	}

	/**
	 * A basic test for getStatusesPopular() Tests that the iteration order is
	 * correct and that iterating multiple times doesn't change the structure
	 */
	@Test
	public void PersonTestGetStatusesPopular() {
		Status[] s = new Status[10];
		for (int i = 0; i < s.length; i++) {
			s[i] = pA.postStatus(((Integer) (i + 1)).toString() + "'th status");
			assertTrue(s[i].getId().equals(i));
		}
		Iterable<Status> col = pA.getStatusesPopular();
		Iterator<Status> itr = col.iterator();
		int i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
		itr = col.iterator();
		i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
		// tests that after adding likes, order changes accordingly
		s[0].like(pB);
		s[1].like(pA);
		s[1].like(pB);
		col = pA.getStatusesPopular();
		itr = col.iterator();
		assertTrue(s[1].equals(itr.next()));
		assertTrue(s[0].equals(itr.next()));
		i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
		s[1].unlike(pB);
		col = pA.getStatusesPopular();
		itr = col.iterator();
		assertTrue(s[1].equals(itr.next()));
		assertTrue(s[0].equals(itr.next()));
		i = s.length - 1;
		while (itr.hasNext()) {
			assertTrue(s[i].equals(itr.next()));
			i--;
		}
	}

	/**
	 * A basic test for using both getStatuses at once, making sure that their
	 * sorting doesn't harm each other
	 */
	@Test
	public void PersonTestGetStatusesTogether() {
		Status s1 = pA.postStatus("first status");
		Status s2 = pA.postStatus("second status");
		s1.like(pB);
		Iterable<Status> colR = pA.getStatusesRecent();
		Iterable<Status> colP = pA.getStatusesPopular();
		Iterator<Status> itrR = colR.iterator();
		Iterator<Status> itrP = colP.iterator();
		assertTrue(itrR.next().equals(s2));
		assertTrue(itrR.next().equals(s1));
		assertFalse(itrR.hasNext());
		assertTrue(itrP.next().equals(s1));
		assertTrue(itrP.next().equals(s2));
		assertFalse(itrP.hasNext());
	}

	/**
	 * A basic test for using getStatusesRecent/Popular while iterating over it.
	 */
	@Test
	public void PersonTestUsingMultipleIterators(){
		Status s1 = pA.postStatus("first status");
		Status s2 = pA.postStatus("second status");
		s1.like(pB);
		Iterable<Status> colR = pA.getStatusesRecent();
		Iterator<Status> itrR = colR.iterator();
		assertTrue(itrR.next().equals(s2));
		colR = pA.getStatusesRecent();
		assertTrue(itrR.next().equals(s1));
		assertFalse(itrR.hasNext());
		Iterable<Status> colP = pA.getStatusesPopular();
		Iterator<Status> itrP = colP.iterator();
		assertTrue(itrP.next().equals(s1));
		colP = pA.getStatusesPopular();
		assertTrue(itrP.next().equals(s2));
		assertFalse(itrP.hasNext());
	}
	
	/**
	 * A stress test for the getStatuses functions, testing how they work with
	 * lots of statuses and likes and dislikes.
	 */
	@Test
	public void PersonStessTestGetStatuses() {
		Status[] s = new Status[1000];
		for (int i = 0; i < s.length; i++) {
			s[i] = pA.postStatus(((Integer) (i + 1)).toString() + "'th status");
			assertTrue(s[i].getId().equals(i));
		}
		Iterable<Status> colR = pA.getStatusesRecent(), colP = pA.getStatusesPopular();
		Iterator<Status> itrR = colR.iterator(), itrP = colP.iterator();
		while (itrR.hasNext() && itrP.hasNext()) {
			// no likes yet so both should iterate at the same order
			assertTrue(itrR.next().equals(itrP.next()));
		}
		// both should finish running at the same time
		assertFalse(itrR.hasNext());
		assertFalse(itrP.hasNext());
		for (int i = 100; i < 200; i++) {
			s[i].like(pA);
		}
		for (int i = 300; i < 400; i++) {
			s[i].like(pA);
			s[i].like(pB);
		}
		colR = pA.getStatusesRecent();
		colP = pA.getStatusesPopular();
		itrR = colR.iterator();
		itrP = colP.iterator();
		// iterating over statuses with 2 likes
		for (int i = 399; i >= 300; i--) {
			assertTrue(itrP.next().equals(s[i]));
			assertTrue(itrR.hasNext());
		}
		// iterating over statuses with 1 like
		for (int i = 199; i >= 100; i--) {
			assertTrue(itrP.next().equals(s[i]));
			assertTrue(itrR.hasNext());
		}
		// should only iterate over 200 statuses so far
		assertTrue(itrR.hasNext());
		// making sure itrR still works fine
		for (int i = 999; i >= 0; i--) {
			assertTrue(itrR.next().equals(s[i]));
		}
		assertFalse(itrR.hasNext());
		for (int i = 300; i < 350; i++) {
			s[i].unlike(pA);
		}
		colR = pA.getStatusesRecent();
		colP = pA.getStatusesPopular();
		itrR = colR.iterator();
		itrP = colP.iterator();
		// iterating over statuses with 2 likes
		for (int i = 399; i >= 350; i--) {
			assertTrue(itrP.next().equals(s[i]));
			assertTrue(itrR.hasNext());
		}
		// iterating over statuses with 1 like
		for (int i = 349; i >= 300; i--) {
			assertTrue(itrP.next().equals(s[i]));
			assertTrue(itrR.hasNext());
		}
		for (int i = 199; i >= 100; i--) {
			assertTrue(itrP.next().equals(s[i]));
			assertTrue(itrR.hasNext());
		}
		// should only iterate over 200 statuses so far
		assertTrue(itrR.hasNext());
		// making sure itrR still works fine
		for (int i = 999; i >= 0; i--) {
			assertTrue(itrR.next().equals(s[i]));
		}
		assertFalse(itrR.hasNext());
	}

}