package OOP2.Tests;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.ConnectionDoesNotExistException;
import OOP2.Provided.FaceOOP;
import OOP2.Provided.Person;
import OOP2.Provided.PersonAlreadyInSystemException;
import OOP2.Provided.PersonNotInSystemException;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Solution.FaceOOPImpl;
import OOP2.Solution.PersonImpl;

public class NivTestFaceOOP {

	/*
	 * A test for FaceOOP You should only run this test once you are sure Person
	 * and Status work properly
	 */

	/*
	 * *********************************************
	 *******************SETUP***********************
	 ***********************************************/
	FaceOOP fo;
	Person pA, pB, pC, pD, pE;

	/**
	 * A setup for the parameters used by most tests, this setup also work as a
	 * basic test for joinFaceOOP. Should joinFaceOOP throw an exception during
	 * it's run, Assert.fail will be called with the message "setup failed" and
	 * all tests would fail. If no exception is thrown but joinFaceOOP or
	 * FaceOOPImpl don't work properly, all tests behaviour is undefined.
	 */
	@Before
	public void basicSetup() {
		fo = new FaceOOPImpl();
		pA = null;
		pB = null;
		pC = null;
		pD = null;
		pE = null;
		try {
			pA = fo.joinFaceOOP(1, "Anne");
			pB = fo.joinFaceOOP(2, "Ben");
			pC = fo.joinFaceOOP(3, "Cody");
			pD = fo.joinFaceOOP(4, "Dor");
			pE = fo.joinFaceOOP(5, "elad");
		} catch (PersonAlreadyInSystemException e) {
			fail("setup failed");
		}
	}

	/* ***************************************************
	 ****************TEST FOR FACEOOP*********************
	 *****************************************************/

	/**
	 * A basic test for joinFaceOOP()'s exceptions Basic tests for successful
	 * adding is covered by the setup
	 */
	@Test
	public void FaceOOPTestJoinFaceOPPException() {
		// should throw PersonAlreadyInSystemException
		Boolean caught = false;
		try {
			fo.joinFaceOOP(1, "a diffrent name");
		} catch (PersonAlreadyInSystemException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	/**
	 * A basic test for size() Tests an empty FaceOOP, the initialized fo whose
	 * size should be 5 and that added to fo updated it's size well
	 */
	@Test
	public void FaceOOPTestSize() {
		assertTrue((new FaceOOPImpl()).size() == 0);
		assertTrue(fo.size() == 5);
		try {
			fo.joinFaceOOP(8, "a name");
		} catch (Exception e) {
			fail();
		}
		assertTrue(fo.size() == 6);
	}

	/**
	 * A basic test for getUser() and it's exception
	 */
	@Test
	public void FaceOOPTestGetUser() {
		Boolean caught = false;
		try {
			fo.getUser(-7);
		} catch (PersonNotInSystemException e) {
			caught = true;
		}
		assertTrue(caught);
		try {
			assertTrue(pA.equals(fo.getUser(1)));
		} catch (PersonNotInSystemException e) {
			fail();
		}
	}

	/**
	 * A basic test for addFriendship() and it's exceptions Tests that the
	 * friendship is successfully added to the Persons sent to it
	 */
	@Test
	public void FaceOOPTestAddFriendship() {
		try {
			fo.addFriendship(pA, pB);
		} catch (Exception e) {
			fail();
		}
		Collection<Person> colA = pA.getFriends();
		Collection<Person> colB = pB.getFriends();
		assertTrue(colA.contains(pB));
		assertTrue(colB.contains(pA));
		Boolean caught = false;
		// should throw ConnectionAlreadyExistException
		try {
			fo.addFriendship(pA, pB);
		} catch (ConnectionAlreadyExistException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		caught = false;
		// should throw SamePersonException
		try {
			fo.addFriendship(pA, pA);
			;
		} catch (SamePersonException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		Person notInSystem = new PersonImpl(99, "noah");
		caught = false;
		// should throw PersonNotInSystemException
		try {
			fo.addFriendship(pA, notInSystem);
		} catch (PersonNotInSystemException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		caught = false;
		// should throw PersonNotInSystemException
		try {
			fo.addFriendship(notInSystem, pA);
		} catch (PersonNotInSystemException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
	}

	/**
	 * A basic test for rank() and it's exceptions Used on a small graph with
	 * few vertexes and edges, this test will not test complexity, only
	 * correctness on small graphs
	 */
	@Test
	public void FaceOOPTestRank() {
		Boolean caught = false;
		try {
			fo.rank(pA, pB);
		} catch (ConnectionDoesNotExistException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		Person notInSystem = new PersonImpl(-3, "not in system");
		caught = false;
		try {
			fo.rank(notInSystem, pB);
		} catch (PersonNotInSystemException e) {
			caught = true;
		} catch (Exception e) {
		}
		assertTrue(caught);
		try {
			fo.addFriendship(pA, pB);
			fo.addFriendship(pA, pC);
			fo.addFriendship(pC, pE);
			fo.addFriendship(pD, pC);
		} catch (Exception e) {
			fail("addFriendship() failed");
		}
		try {
			// testing that all 25 possible combinations to call for rank()
			// return the value they should
			assertTrue(fo.rank(pA, pA) == 0);
			assertTrue(fo.rank(pA, pB) == 1);
			assertTrue(fo.rank(pA, pC) == 1);
			assertTrue(fo.rank(pA, pD) == 2);
			assertTrue(fo.rank(pA, pE) == 2);
			assertTrue(fo.rank(pB, pA) == 1);
			assertTrue(fo.rank(pB, pB) == 0);
			assertTrue(fo.rank(pB, pC) == 2);
			assertTrue(fo.rank(pB, pD) == 3);
			assertTrue(fo.rank(pB, pE) == 3);
			assertTrue(fo.rank(pC, pA) == 1);
			assertTrue(fo.rank(pC, pB) == 2);
			assertTrue(fo.rank(pC, pC) == 0);
			assertTrue(fo.rank(pC, pD) == 1);
			assertTrue(fo.rank(pC, pE) == 1);
			assertTrue(fo.rank(pD, pA) == 2);
			assertTrue(fo.rank(pD, pB) == 3);
			assertTrue(fo.rank(pD, pC) == 1);
			assertTrue(fo.rank(pD, pD) == 0);
			assertTrue(fo.rank(pD, pE) == 2);
			assertTrue(fo.rank(pE, pA) == 2);
			assertTrue(fo.rank(pE, pB) == 3);
			assertTrue(fo.rank(pE, pC) == 1);
			assertTrue(fo.rank(pE, pD) == 2);
			assertTrue(fo.rank(pE, pE) == 0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * A stress test for joinFaceOPP() adding up 4000 members to faceOPP and
	 * using various functions to change its state. Tests The following
	 * function: joinFaceOOP() and size() getUser() and addFriendship() Rank()
	 * 
	 * The message sent by fail contains the names of the functions that could
	 * cause it to fail 
	 * 
	 * This test calls rank() a lot of times. Should you not implement it with BFS, it will take a
	 * long time to run. An average run time should be no more then 3-5 seconds on a decent computer.
	 * We tested this on three computers and it ran in 0.7s, 1.7s and 2.6s
	 */
	@Test
	public void FaceOOPStressTestBasicFunctionality() {
		for (int i = fo.size() + 1; i <= 4000; i++) {
			try {
				fo.joinFaceOOP(i, "test name");
			} catch (PersonAlreadyInSystemException e) {
				fail("joinFaceOOP");
			}
			assertTrue("size()", fo.size() == i);
		}
		assertTrue("size()", fo.size() == 4000);
		// turns the graph into a single long line
		for (int i = 1; i < fo.size(); i++) {
			try {
				Person p1 = fo.getUser(i), p2 = fo.getUser(i + 1);
				fo.addFriendship(p1, p2);
			} catch (Exception e) {
				fail("getUser() or addFriendship()");
			}
		}
		Person first = null;
		try {
			first = fo.getUser(1);
		} catch (Exception e) {
			fail("getUser()");
		}
		for (int i = 1; i <= fo.size(); i++) {
			try {
				Person current = fo.getUser(i);
				// the graph is a line, so the distance to the first person
				// should be equal to i
				assertTrue(fo.rank(current, first) == i-1);
			} catch (Exception e) {
				fail("getUser() or rank()");
			}
		}
		// making the graph more complex
		for (int i = 2; i < fo.size(); i++) {
			try {
				Person current = fo.getUser(i);
				for (int j = 2 * i + 7; j < fo.size(); j = j + i*i) {
					try {
						fo.addFriendship(fo.getUser(j), current);
					} catch (Exception e) {
						// always adding friends with greater id than current so
						// we can't add a connection that already exists
						fail("getUser() or addFriendship()");
					}
				}
			} catch (Exception e) {
				fail("getUser() or addFriendship()");
			}
		}
		// making sure the size didn't change while adding friendships
		assertTrue("size()", fo.size() == 4000);
		try {
			// yet another connection we entered last time
			assertTrue("rank()", fo.rank(fo.getUser(2), fo.getUser(2 * 2 + 7)) == 1);
		} catch (Exception e) {
			fail("getUser() or rank()");
		}
		try {
			// the maximum rank of users i and j is |i-j| because this is a
			// line.
			assertTrue("rank()", fo.rank(fo.getUser(7), fo.getUser(832)) <= 832 - 7);
		} catch (Exception e) {
			fail("getUser() or rank()");
		}
	}

	/**
	 * A stress testing how all functions that receive id as an input handle a
	 * negative id. All function should work properly even with a negative id.
	 * Assumes size() works properly
	 */
	@Test
	public void FaceOOPTestNegativeUseId() {
		int size = fo.size();
		try {
			fo.joinFaceOOP(-1, "a name");
			assertTrue(size +1 == fo.size());
			size = fo.size();
			fo.joinFaceOOP(-7, "another name");
			assertTrue(size +1 == fo.size());
			size = fo.size();
			fo.joinFaceOOP(0, "zero");
			assertTrue(size +1 == fo.size());
			size = fo.size();
		} catch (Exception e) {
			fail("joinFaceOOP()");
		}
		try {
			fo.getUser(-1);
			fo.getUser(-7);
			fo.getUser(0);
		} catch (Exception e) {
			fail("getUser()");
		}
	}

	/**
	 * A basic test for getFeedByRecent, tests basic cases such as a person with no friends
	 * and a friend with no statuses, and tests that likes don't affect the order.
	 * Also tests that it only shows statuses of friends, and not of friends of friends or of self.
	 */
	@Test
	public void FaceOOPTestGetFeedByRecent(){
		Iterator<Status> itr = null;
		//testing PersonNotInSystemException
		Person notInSystem = new PersonImpl(-7,"Avater");
		Boolean caught = false;
		try{
			itr = fo.getFeedByRecent(notInSystem);
		}catch(PersonNotInSystemException e){
			caught = true;
		}		
		assertTrue(caught);
		//testing a person with no friends
		try{
			itr = fo.getFeedByRecent(pA);
		}catch(Exception e){
			fail();
		}
		assertFalse(itr.hasNext()); //pA has no friends, so empty feed
		//testing a friend with no statues
		try{
			fo.addFriendship(pD, pE);
		}catch(Exception e){
			fail();
		}
		try{
			itr = fo.getFeedByRecent(pD);
		}catch(Exception e){
			fail();
		}
		assertFalse(itr.hasNext());
		//posting some statuses
		Status sA1 = pA.postStatus("A first status");
		Status sA2 = pA.postStatus("Another status");
		Status sB = pB.postStatus("B's status");
		//adding friendships
		try{
			fo.addFriendship(pC, pB);
			fo.addFriendship(pA, pB);
		}catch (Exception e){
			fail();
		}
		//testing that feed only shows posts of direct friends
		try{
			itr = fo.getFeedByRecent(pC);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.hasNext());
		assertTrue(itr.next().equals(sB));
		assertFalse(itr.hasNext());
		//testing that feed doesn't show statuses posted by the user
		try{
			itr = fo.getFeedByRecent(pA);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.hasNext());
		assertTrue(itr.next().equals(sB));
		assertFalse(itr.hasNext());
		//testing itr's order of a person's statuses
		try{
			itr = fo.getFeedByRecent(pB);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sA2));
		assertTrue(itr.next().equals(sA1));
		assertFalse(itr.hasNext());
		//testing how likes affect itr's order (they shouldn't)
		sA1.like(pD);
		sA1.like(pA);
		sA2.like(pB);
		sA1.like(pE);
		sA1.unlike(pA);
		try{
			itr = fo.getFeedByRecent(pB);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sA2));
		assertTrue(itr.next().equals(sA1));
		assertFalse(itr.hasNext());
		//testing order between friends
		try{
			fo.addFriendship(pC, pA);
		}catch (Exception e){
			fail();
		}
		Status sC = pC.postStatus("C's status");
		try{
			itr = fo.getFeedByRecent(pA);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sB));
		assertTrue(itr.next().equals(sC));
		assertFalse(itr.hasNext());
	}
	
	/**
	 * A basic test for getFeedByPopular, tests basic cases such as a person with no friends
	 * and a friend with no statuses, and tests that likes don't affect the order.
	 * Also tests that it only shows statuses of friends, and not of friends of friends or of self.
	 */
	@Test
	public void FaceOOPTestGetFeedByPopular(){
		Iterator<Status> itr = null;
		//A PersonNotInSystemException
		Person notInSystem = new PersonImpl(-7,"Avater");
		Boolean caught = false;
		try{
			itr = fo.getFeedByPopular(notInSystem);
		}catch(PersonNotInSystemException e){
			caught = true;
		}
		//A person with no friends
		assertTrue(caught);
		try{
			itr = fo.getFeedByPopular(pA);
		}catch(Exception e){
			fail();
		}
		assertFalse(itr.hasNext()); //pA has no friends, so empty feed
		//testing a friend with no statues
		try{
			fo.addFriendship(pD, pE);
		}catch(Exception e){
			fail();
		}
		try{
			itr = fo.getFeedByPopular(pD);
		}catch(Exception e){
			fail();
		}
		//posting some statuses
		Status sA1 = pA.postStatus("A first status");
		Status sA2 = pA.postStatus("Another status");
		Status sB = pB.postStatus("B's status");
		assertFalse(itr.hasNext());
		//adding friendships
		try{
			fo.addFriendship(pC, pB);
			fo.addFriendship(pA, pB);
		}catch (Exception e){
			fail();
		}
		//testing that feed only shows posts of direct friends
		try{
			itr = fo.getFeedByPopular(pC);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.hasNext());
		assertTrue(itr.next().equals(sB));
		assertFalse(itr.hasNext());
		//testing that feed doesn't show statuses posted by the user
		try{
			itr = fo.getFeedByPopular(pA);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.hasNext());
		assertTrue(itr.next().equals(sB));
		assertFalse(itr.hasNext());
		//testing itr's order of a person's statuses
		try{
			itr = fo.getFeedByPopular(pB);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sA2));
		assertTrue(itr.next().equals(sA1));
		assertFalse(itr.hasNext());
		//testing how likes affect itr's order (they should)
		sA1.like(pD);
		sA1.like(pA);
		sA2.like(pB);
		sA1.like(pE);
		sA1.unlike(pA);
		try{
			itr = fo.getFeedByPopular(pB);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sA1));
		assertTrue(itr.next().equals(sA2));
		assertFalse(itr.hasNext());
		//testing order between friends
		try{
			fo.addFriendship(pC, pA);
		}catch (Exception e){
			fail();
		}
		Status sC = pC.postStatus("C's status");
		try{
			itr = fo.getFeedByPopular(pA);
		}catch(Exception e){
			fail();
		}
		assertTrue(itr.next().equals(sB));
		assertTrue(itr.next().equals(sC));
		assertFalse(itr.hasNext());
	}

	/**
	 * A basic test for FaceOOP.iterator() and it's order of going over people.
	 * Also tests the case of a person with a negative id
	 */
	@Test
	public void FaceOOPTestIterator(){
		//Basic test, all positive
		Iterator<Person> itr = fo.iterator();
		assertTrue(itr.next().equals(pA));
		assertTrue(itr.next().equals(pB));
		assertTrue(itr.next().equals(pC));
		assertTrue(itr.next().equals(pD));
		assertTrue(itr.next().equals(pE));
		assertFalse(itr.hasNext());
		//Adding negative and zero id's
		Person pZero = null, pNegative = null;
		try{
			pZero = fo.joinFaceOOP(0,"zero");
			pNegative = fo.joinFaceOOP(-7,"Jonathan");
		}catch(Exception e){
			fail();
		}
		itr = fo.iterator();
		assertTrue(itr.next().equals(pNegative));
		assertTrue(itr.next().equals(pZero));
		assertTrue(itr.next().equals(pA));
		assertTrue(itr.next().equals(pB));
		assertTrue(itr.next().equals(pC));
		assertTrue(itr.next().equals(pD));
		assertTrue(itr.next().equals(pE));
		assertFalse(itr.hasNext());
	}
	
}