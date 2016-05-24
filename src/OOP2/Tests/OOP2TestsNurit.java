package OOP2.Tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.ConnectionDoesNotExistException;
import OOP2.Provided.FaceOOP;
import OOP2.Provided.Person;
import OOP2.Provided.PersonAlreadyInSystemException;
import OOP2.Provided.PersonNotInSystemException;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Provided.StatusIterator;
import OOP2.Solution.FaceOOPImpl;
import OOP2.Solution.PersonImpl;
import OOP2.Solution.StatusImpl;

public class OOP2TestsNurit {
	
	/**********************
	 *  Setup
	 *********************/
	@Rule  //timeout - 1 min
	public Timeout globalTimeout = new Timeout(6000000);
	
	private static FaceOOP fo;
	private static Person pA, pB, pC, pD, pE, pfA, pfB, pfC, pfD, pfE;	
	@Before
	public void setUp() throws Exception {
		try{
			fo = new FaceOOPImpl();
			pA = new PersonImpl(1, "alice");
			pB = new PersonImpl(2, "bob");
			pC = new PersonImpl(3, "carrie");
			pD = new PersonImpl(4, "Dan");
			pE = new PersonImpl(5, "Ella");
			
			pfA = fo.joinFaceOOP(10, "Annabelle");
			pfB = fo.joinFaceOOP(20, "Barney");
			pfC = fo.joinFaceOOP(30, "Cara");
			pfD = fo.joinFaceOOP(40, "Drew");
			pfE = fo.joinFaceOOP(50, "Ethan");
			
		}
		catch (Exception e)	{ fail(); }			
	}
	
	/**********************
	 * Status Tests
	 *********************/
	
	/*
	 * Tests the basic status get functions.
	 */
	@Test
	public void basicStatusGet()
	{
		Status sA = new StatusImpl(pA, "Woo hoo!!!!", 3);
		assertTrue(sA.getId().equals(3));
		assertTrue(sA.getContent().equals("Woo hoo!!!!"));
		assertTrue(sA.getPublisher().equals(pA));
	}
	
	/*
	 * Tests liking and unliking statuses.
	 */
	@Test
	public void statusLikesTest()
	{
		Status s1 = new StatusImpl(pA, "Mumble mumble mumble.", 1);	

		assertTrue(s1.getLikesCount().equals(0));
		s1.like(pA);
		assertTrue(s1.getLikesCount().equals(1));
		s1.like(pA);
		assertTrue(s1.getLikesCount().equals(1));
		s1.like(pB);
		assertTrue(s1.getLikesCount().equals(2));
		s1.like(pC);
		assertTrue(s1.getLikesCount().equals(3));
		s1.like(pD);
		assertTrue(s1.getLikesCount().equals(4));
		s1.like(pA);
		assertTrue(s1.getLikesCount().equals(4));
		
		
		s1.unlike(pB);
		assertTrue(s1.getLikesCount().equals(3));
		s1.unlike(pA);
		s1.like(pB);
		assertTrue(s1.getLikesCount().equals(3));
		s1.unlike(pA);
		assertTrue(s1.getLikesCount().equals(3));
		s1.unlike(pC);
		s1.unlike(pA);
		assertTrue(s1.getLikesCount().equals(2));
		s1.unlike(pB);
		s1.unlike(pD);
		assertTrue(s1.getLikesCount().equals(0));
		s1.like(pB);
		assertTrue(s1.getLikesCount().equals(1));
	}
	
	@Test
	public void statusEqualsTest()
	{
		
		Person pA1 = new PersonImpl(1, "alice");		
		Person pA2 = new PersonImpl(2, "alice");
		
		
		Status sA = new StatusImpl(pA, "Hello", 1);
		Status sA1 = new StatusImpl(pA, "Goodbye", 1);
		Status sA2 = new StatusImpl(pA1, "Hello", 1);
		Status sA3 = new StatusImpl(pA, "I'm tired", 2);
		Status sA4 = new StatusImpl(pA2, "And I hate grading HW", 1);

		try{
			assertFalse(sA.equals(null));
			assertFalse(sA.equals(pA));
			assertFalse(sA.equals(5));
			assertTrue(sA.equals(sA1));
			assertTrue(sA.equals(sA2));
			assertFalse(sA.equals(sA3));
			assertFalse(sA.equals(sA4));
				
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	/**********************
	 * Person Tests
	 *********************/
	
	/*
	 * Tests the basic Person get functions.
	 */
	@Test
	public void basicPersonTest()
	{
		assertTrue(pA.getName().equals("alice"));
		assertTrue(pA.getId().equals(1));
		assertTrue(new PersonImpl(0, "Al").getName().equals("Al"));
		assertTrue(new PersonImpl(0, "Al").getId().equals(0));
	}
	
	/*
	 * Tests statuses returned from calls to the postStatus function.
	 */
	@Test
	public void postStatusTest()
	{
		Status sA1 = pA.postStatus("Something highly political and uninformed");
		Status sB1 = pB.postStatus("Pictures of dogs");		
		Status sA2 = pA.postStatus("Something really offensive.  This is your cue to block me...");
		assertTrue(sA1.getPublisher().equals(pA));
		assertTrue(sA1.getContent().equals("Something highly political and uninformed"));
		assertTrue(sA1.getId().equals(0));
		assertTrue(sA1.getLikesCount().equals(0));
		
		
		assertTrue(sA2.getId().equals(1));
		assertTrue(sA2.getLikesCount().equals(0));
		assertTrue(sA2.getPublisher().equals(pA));
		assertFalse(sA2.equals(sA1));
		
		assertTrue(sB1.getId().equals(0));
		assertTrue(sB1.getPublisher().equals(pB));
	}
	
	/*
	 * test addition of friends. Checks that Exceptions are thrown when necessary. 
	 */
	@Test
	public void addFriendsTest()
	{
		try {
			pA.addFriend(pB);
		} catch (Exception e) {
			fail();
		}
		
		try {
			pA.addFriend(pC);
		} catch (Exception e) {
			fail();
		}
		
		try {
			pA.addFriend(pD);
		} catch (Exception e) {
			fail();
		}
		
		boolean thrown = false;
		try {
			pA.addFriend(pB);
		} catch (ConnectionAlreadyExistException e)
		{
			thrown = true;
		}catch (Exception e)
		{
			fail();
		}
		
		assertTrue(thrown);
		
		thrown = false;		
		try {
			pA.addFriend(pA);
		} catch (SamePersonException e)
		{
			thrown = true;
		}catch (Exception e)
		{
			fail();
		}
		assertTrue(thrown);
		
		try{
		/*	Decided to allow them to make 2-way connections here if it helps them.
		 	pB.addFriend(pA);
			pC.addFriend(pA);
			pD.addFriend(pA);*/
			
			pB.addFriend(pC);
			pD.addFriend(pC);
			pC.addFriend(pD);			
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	public void getFriendsTest()
	{
		Collection<Person> pAFriends = pA.getFriends();
		assertTrue(pAFriends.isEmpty());
		
		try{
			pA.addFriend(pB);
		}
		catch(Exception e) {fail();}
		pAFriends = pA.getFriends();
		assertTrue(pAFriends.size() == 1);
		assertTrue(pAFriends.contains(pB));
		
		try{
			pA.addFriend(pC);
			pA.addFriend(pD);
		}catch(Exception e) {fail();}
		pAFriends = pA.getFriends();
		assertTrue(pAFriends.size() == 3);
		assertTrue(pAFriends.contains(pB) && pAFriends.contains(pC) && pAFriends.contains(pD));
		
		assertTrue(pE.getFriends().isEmpty());
		try{
			pE.addFriend(pA);
		}catch(Exception e) {fail();}
		assertTrue(pE.getFriends().size() == 1);
		assertTrue(pE.getFriends().contains(pA));		
	}
	

	@Test
	public void getStatusesRecentTest()
	{
		Status[] pAStats = new Status[50];
		for (int i = 0; i < 50; i++)
		{
			pAStats[i] = pA.postStatus("Hello " + i);
		}
		
		//check iteration over the structure
		int count = 50;
		for (Status s : pA.getStatusesRecent())
		{
			count--;
			assertTrue(s.equals(pAStats[count]));			
		}
		
		//now check another iteration, see that the second iteration changes nothing, and that there are exactly the right number of statuses.
		count = 50;
		Iterator<Status> statIt = pA.getStatusesRecent().iterator();
		while(count > 0)
		{
			count--;
			assertTrue(statIt.hasNext());
			assertTrue(pAStats[count].equals(statIt.next()));
		}	
		assertFalse(statIt.hasNext());
		
		//check that likes do not change statuses
		for (int i = 0; i < 50; i++)
		{
			//older statuses have more likes.  shouldn't make any difference in later iterations.
			// this should change the actual statuses...
			for (int j = 50; j > i; j--)
				pAStats[i].like(new PersonImpl(j, "person"+j));
		}
		
		count = 50;
		for (Status s : pA.getStatusesRecent())
		{
			count--;
			assertTrue(s.equals(pAStats[count]));
			assertTrue(s.getLikesCount().equals(50 - count));	//this is to make sure the liking worked. You should be returning the actual statuses....		
		}
		
		for (Status s: pB.getStatusesRecent())
		{
			fail(); //should not be any such statuses.
		}
	}
	
	/*
	 * get statuses according to popularity.  Basic test - see that this works for a few iterations,
	 * Try without likes, with ordered likes from old to young, and with mixed numbers of likes.
	 */
	@Test
	public void getStatusesPopularTest()
	{
		Status[] pAStats = new Status[50];
		for (int i = 0; i < 50; i++)
		{
			pAStats[i] = pA.postStatus("Hello " + i);
		}
		
		//check iteration over the structure. No likes means same order as getStatusesRecent.
		int count = 50;
		for (Status s : pA.getStatusesPopular())
		{
			count--;
			assertTrue(s.equals(pAStats[count]));			
		}
		
		//add likes - gives a mix of equal and different like numbers
		for (int i = 0; i < 25; i++)
		{
			for (int j = 25; j>i; j--)
			{
				pAStats[2*i].like(new PersonImpl(j, "person"+j));
				pAStats[2*i+1].like(new PersonImpl(j, "person"+j));
			}
		}
				
		//check that iteration gives correct result
		count = 0;
		Iterator<Status> statIt = pA.getStatusesPopular().iterator();
		while (statIt.hasNext())
		{
			assertTrue(statIt.hasNext());
			assertTrue(statIt.next().equals(pAStats[2*count+1]));
			
			assertTrue(statIt.hasNext());
			assertTrue(statIt.next().equals(pAStats[2*count]));

			count++;
		}
		assertTrue(count == 25);
		
		//now add likes so order will be from oldest to youngest and check iteration again
		for (int i = 0; i < 50; i++)
		{
			for (int j = 0; j <51 -i; j++)
			{
				pAStats[i].like(new PersonImpl(1000+j, "Person"+1000+j));
			}
		}
		
		count = 0;
		for (Status s: pA.getStatusesPopular())
		{
			assertTrue(s.equals(pAStats[count]));
			count++;
		}
		
		for (Status s: pB.getStatusesPopular())
		{
			fail(); //should not be any such statuses.
		}
	}
	
	/* 
	 * Checks both kinds of iterators together, two iterators iterating at once, changing the number of likes
	 * in the recent iterator (legal, as the number of likes has no bearing on the order of the recent iterator).
	 * 
	 * Also a bit of a stress test
	 */
	@Test
	public void trickyStatIterators()
	{
		final int NUM_STATS = 50;
		Status[] pAStats = new Status[NUM_STATS];
		for (int i = 0; i < NUM_STATS; i++)
		{
			pAStats[i] = pA.postStatus("Hello " + i);
		}
		
		//several iterables over the same structure, (statusesRecent) each should run independently of the others.
		int count1 = NUM_STATS;
		for (Status s1: pA.getStatusesRecent())
		{
			count1--;
			assertTrue(pAStats[count1].equals(s1));	
			
			int count2 = NUM_STATS;
			for (Status s2: pA.getStatusesRecent())
			{
				count2 --;
				assertTrue(pAStats[count2].equals(s2));
				
				
				//now using iterators to same iterable collection
				Iterable<Status> recent = pA.getStatusesRecent();
				Iterator<Status> it1 = recent.iterator();
				for (int i = 0; i < NUM_STATS; i++)
				{
					assertTrue(pAStats[NUM_STATS-i-1].equals(it1.next()));
					Iterator<Status> it2 = recent.iterator();
					for (int j = 0; j < NUM_STATS; j++)
					{
						assertTrue(it2.next().equals(pAStats[NUM_STATS-j-1]));
					}
					assertFalse(it2.hasNext());
				}
				assertFalse(it1.hasNext());
			}
			assertTrue(count2==0);			
		}
		assertTrue(count1==0);
		
	
		
		int count = 0;
		//like during getStatusesRecent.  Shouldn't have any effect.
		Iterator<Status> it = pA.getStatusesRecent().iterator();
		while (count < NUM_STATS)
		{
			assertTrue(it.hasNext());
			Status s = it.next();
			
			for (int i = 0; i < count; i++)
			{
				s.like(new PersonImpl(i+1000, "person"+i+1000));
			}
			assertTrue(s.equals(pAStats[NUM_STATS-count-1]));
			count++;
		}
		assertFalse(it.hasNext());
		
		// now check two getStatusesPopular iterables at once, the likes from the previous loop should make the order
		// of iteration be from the oldest to the youngest status.
		count1 = 0;
		for (Status s1: pA.getStatusesPopular())
		{
			assertTrue(pAStats[count1].equals(s1));
			assertTrue(s1.getLikesCount().equals(NUM_STATS-count1 - 1));		

			count1++;
			int count2 = 0;
			for (Status s2: pA.getStatusesPopular())
			{
				assertTrue(pAStats[count2].equals(s2));
				assertTrue(s2.getLikesCount().equals(NUM_STATS-count2 - 1));	
				count2 ++;
				
				//same check using two iterators from same iterable.
				Iterable<Status> popular = pA.getStatusesPopular();
				Iterator<Status> it1 = popular.iterator();
				for (int i = 0; i < NUM_STATS; i++)
				{
					Status s3 = it1.next();
					assertTrue(pAStats[i].equals(s3));
					assertTrue(s3.getLikesCount().equals(NUM_STATS- i - 1));
					Iterator<Status> it2 = popular.iterator();
					for (int j = 0; j < NUM_STATS; j++)
					{
						Status s4 = it2.next();
						assertTrue(s4.equals(pAStats[j]));
						assertTrue(s4.getLikesCount().equals(NUM_STATS- j - 1));
					}
					assertFalse(it2.hasNext());
				}
				assertFalse(it1.hasNext());
			}
			assertTrue(count2==NUM_STATS);			
		}
		assertTrue(count1==NUM_STATS);
		
		//lastly recent and popular together.
		Iterator<Status> itRecent = pA.getStatusesRecent().iterator();
		Iterator<Status> itPop = pA.getStatusesPopular().iterator();
		
		for (int i = 0; i < NUM_STATS; i++)
		{
			assertTrue(itRecent.hasNext());
			assertTrue(itPop.hasNext());
			
			assertTrue(itRecent.next().equals(pAStats[NUM_STATS - i - 1]));
			assertTrue(itPop.next().equals(pAStats[i]));
		}
		
		assertFalse(itRecent.hasNext());
		assertFalse(itPop.hasNext());
			
	}
	
	/*
	 * Tests the compareTo function you had to implement for Person
	 */
	@Test
	public void comparableTest()
	{
		assertTrue(pA.compareTo(pB) < 0);
		assertTrue(pB.compareTo(pA) > 0);
		assertTrue(pA.compareTo(pA) == 0);
		assertTrue(pA.compareTo(new PersonImpl(1, "Aliza")) == 0);
		assertTrue((new PersonImpl(1, "Lizzie")).compareTo(pA) == 0);
	}
	
	@Test
	public void personEqualsTest()
	{
		//Person Equals		
		Person pA1 = new PersonImpl(1, "alice");
		Person pA2 = new PersonImpl(1, "anna");
		Person pA3 = new PersonImpl(2, "alice");
		try{
			assertFalse(pA.equals(null));
			assertFalse(pA.equals(new StatusImpl(pA, "hi there", 0)));
			assertFalse(pA.equals(5));
			assertTrue(pA.equals(pA));
			assertTrue(pA.equals(pA1));
			assertTrue(pA.equals(pA2));
			assertFalse(pA.equals(pA3));
				
		}
		catch (Exception e)
		{
			fail();
		}
	}
	

	/**********************
	 * FaceOOP Tests
	 *********************/

	/*
	 * Tests size function.
	 */
	@Test
	public void foSizeTest()
	{
		//try empty implementation
		FaceOOP fo1 = new FaceOOPImpl();
		assertTrue(fo1.size() == 0);
		
		//add some elements, see that size changes accordingly.
		try{
			fo1.joinFaceOOP(1, "abel");
		}catch (Exception e) {fail();}
		assertTrue(fo1.size() == 1);
			
		try{
			fo1.joinFaceOOP(2, "Benny");
			fo1.joinFaceOOP(3, "Galit");
			fo1.joinFaceOOP(4, "Dikla");
			fo1.joinFaceOOP(5, "Harel");
			fo1.joinFaceOOP(6, "Vered");
			fo1.joinFaceOOP(7, "Ziv");
		}catch (Exception e) {fail();}
		assertTrue(fo1.size() == 7);
		assertTrue(fo.size() == 5);
	}
	
	/*
	 * Test the join function
	 */
	@Test
	public void foJoinTest()
	{
		try{
			fo.joinFaceOOP(100, "Centarian");
		} catch (Exception e) {fail();}
		
		//try to add someone already in system. Name shouldn't matter
		boolean caught = false;
		try {
			fo.joinFaceOOP(20, "Barney");
		}catch (PersonAlreadyInSystemException e)
		{
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			fo.joinFaceOOP(30, "Someone elxe");
		}catch (PersonAlreadyInSystemException e)
		{
			caught = true;
		}
		assertTrue(caught);
	}
	
	
	@Test
	public void getUserTest()
	{
		//get existing users, ensure they are found.
		try{
			assertTrue(fo.getUser(10).equals(pfA));
			assertTrue(fo.getUser(20).equals(pfB));
			assertTrue(fo.getUser(30).equals(pfC));
			assertTrue(fo.getUser(40).equals(pfD));
			assertTrue(fo.getUser(50).equals(pfE));			
		}catch (Exception e) {fail();}
		
		//try to get a user not in the system
		boolean caught = false;
		try{
			fo.getUser(0);
		}catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void addFriendshipTest()
	{
		try
		{
			fo.addFriendship(pfA, pfB);
		} catch(Exception e) {fail();}
		assertTrue(pfA.getFriends().contains(pfB));
		assertTrue(pfB.getFriends().contains(pfA));
		assertTrue(pfA.getFriends().size() == pfB.getFriends().size() && pfB.getFriends().size() == 1);
		
		
		//now try to add the same friendship
		boolean caught = false;
		try
		{
			fo.addFriendship(pfA, pfB);
		} catch(ConnectionAlreadyExistException e)
		{
			caught = true;
		} catch (Exception e){ fail();}
		assertTrue(caught);
		
		//Again, the other way around
		caught = false;
		try
		{
			fo.addFriendship(pfB, pfA);
		} catch(ConnectionAlreadyExistException e)
		{
			caught = true;
		} catch (Exception e){ fail();}
		assertTrue(caught);
		
		//transitive/circular friendships cause no problems
		try
		{
			fo.addFriendship(pfA, pfC);
			fo.addFriendship(pfB, pfC);
		} catch(Exception e) {fail();}
		assertTrue(pfA.getFriends().contains(pfB) && pfA.getFriends().contains(pfC));
		assertTrue(pfB.getFriends().contains(pfA)&& pfB.getFriends().contains(pfC));
		assertTrue(pfA.getFriends().size() == pfB.getFriends().size() && 
				   pfB.getFriends().size() == pfC.getFriends().size() &&
				   pfC.getFriends().size() == 2);
		
		//Try adding friendship to same person
		caught = false;
		try
		{
			fo.addFriendship(pfA, pfA);
		} catch(SamePersonException e)
		{
			caught = true;
		} catch (Exception e){ fail();}
		assertTrue(caught);
		
		//Try adding friendship to someone not in system
		caught = false;
		try
		{
			fo.addFriendship(pfA, pA);
		} catch(PersonNotInSystemException e)
		{
			caught = true;
		} catch (Exception e){ fail();}
		assertTrue(caught);
		
		caught = false;
		try
		{
			fo.addFriendship(pA, pfA);
		} catch(PersonNotInSystemException e)
		{
			caught = true;
		} catch (Exception e){ fail();}
		assertTrue(caught);
		
		//two possible errors. Make sure one is thrown:
		caught = false;
		try
		{
			fo.addFriendship(pA, pA);
		} catch (PersonNotInSystemException e)
		{
			caught = true;
		} catch (SamePersonException e)
		{
			caught = true;
		}catch (Exception e){ fail();}
		
		assertTrue(caught);
		
	}
	
	
	/*
	 * Tests iterator of faceOOP class.
	 */
	@Test
	public void faceOOPIteratorTest()
	{		
		Iterator<Person> foIt = fo.iterator();
		assertTrue(foIt.hasNext() && foIt.next().equals(pfA));
		assertTrue(foIt.hasNext() && foIt.next().equals(pfB));
		assertTrue(foIt.hasNext() && foIt.next().equals(pfC));
		assertTrue(foIt.hasNext() && foIt.next().equals(pfD));
		assertTrue(foIt.hasNext() && foIt.next().equals(pfE));
		assertFalse(foIt.hasNext());
		
		Person pNew1 = null, pNew2 = null, pNew3 = null, pNew4 = null, pNew5 = null, pNew6 = null, pNew7 = null, pNew8= null;
		try{
			pNew1 = fo.joinFaceOOP(0, "Bron");
			pNew2 = fo.joinFaceOOP(100, "Darla");
			pNew3 = fo.joinFaceOOP(-1, "Archibald");
			pNew4 = fo.joinFaceOOP(15, "Mandy");
			pNew5 = fo.joinFaceOOP(25, "Indy");
			pNew6 = fo.joinFaceOOP(250000, "Indy");
			pNew7 = fo.joinFaceOOP(45, "Artie");
			pNew8 = fo.joinFaceOOP(33, "Ivan");
		}catch (Exception e) {fail();}
		
		Person[] personArr = {pNew3, pNew1, pfA, pNew4, pfB, pNew5, pfC, pNew8, pfD, pNew7, pfE, pNew2, pNew6};
		foIt = fo.iterator();		
		int count = 0;
		while (count < personArr.length)
		{
			assertTrue(foIt.hasNext() && foIt.next().equals(personArr[count]));
			count++;
			int count2 = 0;
			Iterator<Person> foIt2 = fo.iterator();
			while (count2 < personArr.length)
			{
				assertTrue(foIt2.hasNext() && foIt2.next().equals(personArr[count2]));
				count2++;
				int count3 = 0;
				Iterator<Person> foIt3 = fo.iterator();
				while (count3 < personArr.length)
				{
					assertTrue(foIt3.hasNext() && foIt3.next().equals(personArr[count3]));
					count3++;
					int count4 = 0;
					Iterator<Person> foIt4 = fo.iterator();
					while (count4 < personArr.length)
					{
						assertTrue(foIt4.hasNext() && foIt4.next().equals(personArr[count4]));
						count4++;						
					}
					assertFalse(foIt4.hasNext());
				}
				assertFalse(foIt3.hasNext());
			}
			assertFalse(foIt2.hasNext());
		}
		assertFalse(foIt.hasNext());
		
	}
	
	/*
	 * Test get feed by recent.
	 * 
	 * Tests no friends, friends with no statuses, a mix of status/non-status friends, 2nd degree friends with statuses.
	 * Also tests that adding likes doesn't change the iterations
	 */
	@Test
	public void getFeedRecentTest()
	{
		StatusIterator feedIt = null;
		
		//try to get feed for a person not in the system
		boolean caught = false;
		try{
			feedIt = fo.getFeedByRecent(new PersonImpl(4,"Dima"));
		}catch(PersonNotInSystemException e){
			caught = true;
		}		
		assertTrue(caught);
		
		Status[] pAStats = new Status[8];
		// get feed for person with no friends.  Add statuses for this person, shouldn't change anything, as the feed doesn't include
		// the person's own statuses
		try{
			pAStats[7] = pfA.postStatus("Sing a song of sixpence");
			pAStats[6] = pfA.postStatus("A pocket full of rye");
			pAStats[5] = pfA.postStatus("Four and twenty blackbirds");
			pAStats[4] = pfA.postStatus("Baked in a pie");
			pAStats[3] = pfA.postStatus("When the pie was opened");
			pAStats[2] = pfA.postStatus("The birds began to sing");
			pAStats[1] = pfA.postStatus("Now wasn't that a dainty dish");
			pAStats[0] = pfA.postStatus("To set before the king!");
			feedIt = fo.getFeedByRecent(pfA);
		}catch(Exception e){fail();}
		
		assertFalse(feedIt.hasNext());
		
		Status[] pEStats = new Status[4];
		// add friendships, but no statuses for any friends of pfA.  pfA's feed should be empty. (doesn't show own statuses, or
		// statuses of pfE, which isn't a direct friend.
		try{
			fo.addFriendship(pfA, pfB);
			fo.addFriendship(pfA, pfC);
			fo.addFriendship(pfA, pfD);
			fo.addFriendship(pfB, pfD);
			fo.addFriendship(pfE, pfB);
			pEStats[3] = pfE.postStatus("Hark, Hark, the dogs do bark");
			pEStats[2] = pfE.postStatus("The beggars are coming to town");
			pEStats[1] = pfE.postStatus("Some in rags, and some in bags");
			pEStats[0] = pfE.postStatus("And some in velvet gowns");
			feedIt = fo.getFeedByRecent(pfA);
		}catch(Exception e){ fail();}
		assertFalse(feedIt.hasNext());
		
		
		//test feed of pfB - has 3 neighbors, one of which has no statuses
		try{
			feedIt = fo.getFeedByRecent(pfB);
		}catch(Exception e) {fail();}

		for (Status s: pAStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		for (Status s: pEStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertFalse(feedIt.hasNext());
		
		// now add a status for pfD.  Should appear between pfA's and pfE's
		Status sD = pfD.postStatus("I also want to be seen!!");
		try{
			feedIt = fo.getFeedByRecent(pfB);
		}catch(Exception e) {fail();}

		for (Status s: pAStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertTrue(feedIt.hasNext() && feedIt.next().equals(sD));
		for (Status s: pEStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertFalse(feedIt.hasNext());
		
	
		//like some statuses and make sure this doesn't change the recent iterator
		for (int i = 0; i < pAStats.length; i++)
		{
			if (i %2 == 0)
			{
				pAStats[i].like(pfA);
				pAStats[i].like(pfB);
				pAStats[i].like(pfC);				
			}
			if (i % 3 == 0)
			{
				pAStats[i].like(pfD);
				pAStats[i].like(pfE);
			}
		}
		sD.like(pA);sD.like(pB);sD.like(pC);sD.like(pD);sD.like(pE);sD.like(pfA);sD.like(pfB);
		pEStats[0].like(pA);pEStats[0].like(pB);pEStats[0].like(pC);pEStats[0].like(pD);pEStats[0].like(pE);pEStats[0].like(pfA);
		
		try{
			feedIt = fo.getFeedByRecent(pfB);
		}catch(Exception e) {fail();}

		for (int i = 0; i < pAStats.length; i++)
		{
			assertTrue(feedIt.hasNext());
			Status s = feedIt.next();
			assertTrue(s.equals(pAStats[i]));
			if (i %2 == 0)
			{
				if (i % 3 == 0)
					assertTrue(s.getLikesCount() == 5);
				else
					assertTrue(s.getLikesCount() == 3);				
			}
			else if (i % 3 == 0)
			{
				assertTrue(s.getLikesCount() == 2);
			}
		}
		assertTrue(feedIt.hasNext());
		Status sdTest = feedIt.next();
		assertTrue(sdTest.equals(sD) && sdTest.getLikesCount()==7);
		for (Status s: pEStats)
		{
			assertTrue(feedIt.hasNext());
			Status sNext = feedIt.next();
			if (s.equals(pEStats[0])) assertTrue(sNext.getLikesCount() == 6);
			assertTrue(sNext.equals(s));
		}
		assertFalse(feedIt.hasNext());
		
		//finally, let a few iterators run in tandem.  create joint array first to make this easier... The ugly way, because I can't be bothered.
		Status[] jointArr = new Status[pAStats.length + pEStats.length + 1];
		int i = 0;
		for (Status s: pAStats)
		{
			jointArr[i++] = s;
		}
		jointArr[i++] = sD;
		for (Status s: pEStats)
		{
			jointArr[i++] = s;
		}
		
		
		try{
			feedIt = fo.getFeedByRecent(pfB);		
			int count = 0;
			while (count < jointArr.length)
			{
				assertTrue(feedIt.hasNext() && feedIt.next().equals(jointArr[count]));
				count++;
				int count2 = 0;
				StatusIterator feedIt2 = fo.getFeedByRecent(pfB);
				while (count2 < jointArr.length)
				{
					assertTrue(feedIt2.hasNext() && feedIt2.next().equals(jointArr[count2]));
					count2++;
					int count3 = 0;
					StatusIterator feedIt3 = fo.getFeedByRecent(pfB);
					while (count3 < jointArr.length)
					{
						assertTrue(feedIt3.hasNext() && feedIt3.next().equals(jointArr[count3]));
						count3++;
						
						// iterate over pfA's in the middle - should only see pfD's statuses.
						StatusIterator feedIt4 = fo.getFeedByRecent(pfA);
						assertTrue(feedIt4.hasNext() && feedIt4.next().equals(sD));
						assertFalse(feedIt4.hasNext());
					}
					assertFalse(feedIt3.hasNext());
				}
				assertFalse(feedIt2.hasNext());
			}
			assertFalse(feedIt.hasNext());
		} 
		catch(Exception e) 
		{
			fail();
		}
		
	}
	
	/*
	 * similar to feedByRecent test, only adding likes to rearrange the order.
	 */
	@Test
	public void getFeedPopularTest()
	{
		StatusIterator feedIt = null;
		
		//try to get feed for a person not in the system
		boolean caught = false;
		try{
			feedIt = fo.getFeedByPopular(new PersonImpl(4,"Dima"));
		}catch(PersonNotInSystemException e){
			caught = true;
		}		
		assertTrue(caught);
		
		Status[] pAStats = new Status[8];
		Status[] jointRecent = new Status[13]; //for later. Ugly, I know.
		// get feed for person with no friends.  Add statuses for this person, shouldn't change anything, as the feed doesn't include
		// the person's own statuses
		try{
			jointRecent[7] = pAStats[0] = pfA.postStatus("Sing a song of sixpence");
			jointRecent[6] = pAStats[1] = pfA.postStatus("A pocket full of rye");
			jointRecent[5] = pAStats[2] = pfA.postStatus("Four and twenty blackbirds");
			jointRecent[4] = pAStats[3] = pfA.postStatus("Baked in a pie");
			jointRecent[3] = pAStats[4] = pfA.postStatus("When the pie was opened");
			jointRecent[2] = pAStats[5] = pfA.postStatus("The birds began to sing");
			jointRecent[1] = pAStats[7] = pfA.postStatus("Now wasn't that a dainty dish");
			jointRecent[0] = pAStats[6] = pfA.postStatus("To set before the king!");
			
			for (int i = 0; i < pAStats.length; i++)
			{
				for (int j = i; j < pAStats.length - 2; j++)
					pAStats[i].like(new PersonImpl(j+ 10, "Henry"));
			}
			feedIt = fo.getFeedByPopular(pfA);
		}catch(Exception e){fail();}
		
		assertFalse(feedIt.hasNext());
		
		Status[] pEStats = new Status[4];
		// add friendships, but no statuses for any friends of pfA.  pfA's feed should be empty. (doesn't show own statuses, or
		// statuses of pfE, which isn't a direct friend.
		try{
			fo.addFriendship(pfA, pfB);
			fo.addFriendship(pfA, pfC);
			fo.addFriendship(pfA, pfD);
			fo.addFriendship(pfB, pfD);
			fo.addFriendship(pfE, pfB);
			jointRecent[12] = pEStats[1] = pfE.postStatus("Hark, Hark, the dogs do bark");
			jointRecent[11] = pEStats[0] = pfE.postStatus("The beggars are coming to town");
			jointRecent[10] = pEStats[3] = pfE.postStatus("Some in rags, and some in bags");
			jointRecent[9] = pEStats[2] = pfE.postStatus("And some in velvet gowns");
			
			pEStats[0].like(pA);
			pEStats[0].like(pB);
			pEStats[1].like(pC);
			pEStats[1].like(pD);
			feedIt = fo.getFeedByPopular(pfA);
		}catch(Exception e){ fail();}
		assertFalse(feedIt.hasNext());
		
		
		//test feed of pfB - has 3 neighbors, one of which has no statuses
		try{
			feedIt = fo.getFeedByPopular(pfB);
		}catch(Exception e) {fail();}

		for (Status s: pAStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		for (Status s: pEStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertFalse(feedIt.hasNext());
		
		// now add a status for pfD.  Should appear between pfA's and pfE's
		Status sD = pfD.postStatus("I also want to be seen!!");
		jointRecent[8] = sD; 
		try{
			feedIt = fo.getFeedByPopular(pfB);
		}catch(Exception e) {fail();}

		for (Status s: pAStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertTrue(feedIt.hasNext() && feedIt.next().equals(sD));
		for (Status s: pEStats)
		{
			assertTrue(feedIt.hasNext() && feedIt.next().equals(s));
		}
		assertFalse(feedIt.hasNext());
		
		//finally, let a few iterators run in tandem.  create joint array first to make this easier... The ugly way, because I can't be bothered.
		Status[] jointArr = new Status[pAStats.length + pEStats.length + 1];
		int i = 0;
		for (Status s: pAStats)
		{
			jointArr[i++] = s;
		}
		jointArr[i++] = sD;
		for (Status s: pEStats)
		{
			jointArr[i++] = s;
		}
		
		
		//note that we also test recent along with popular here.  Shouldn't bother each other
		try{
			feedIt = fo.getFeedByPopular(pfB);		
			int count = 0;
			while (count < jointArr.length)
			{
				assertTrue(feedIt.hasNext() && feedIt.next().equals(jointArr[count]));
				count++;
				int count2 = 0;
				StatusIterator feedIt2 = fo.getFeedByPopular(pfB);
				StatusIterator recentIt = fo.getFeedByRecent(pfB);
				while (count2 < jointArr.length)
				{
					assertTrue(feedIt2.hasNext() && feedIt2.next().equals(jointArr[count2]));
					assertTrue(recentIt.hasNext() && recentIt.next().equals(jointRecent[count2]));
					count2++;
					int count3 = 0;
					StatusIterator feedIt3 = fo.getFeedByPopular(pfB);
					while (count3 < jointArr.length)
					{
						assertTrue(feedIt3.hasNext() && feedIt3.next().equals(jointArr[count3]));
						count3++;
						
						// iterate over pfA's in the middle - should only see pfD's statuses.
						StatusIterator feedIt4 = fo.getFeedByPopular(pfA);
						assertTrue(feedIt4.hasNext() && feedIt4.next().equals(sD));
						assertFalse(feedIt4.hasNext());
					}
					assertFalse(feedIt3.hasNext());
				}
				assertFalse(feedIt2.hasNext());
			}
			assertFalse(feedIt.hasNext());
		} 
		catch(Exception e) 
		{
			fail();
		}		
	}
	
	@Test
	public void rankTest()
	{
		Person pF = null, pM = null;
		try{
			pA = fo.joinFaceOOP(1, "Amy");
			pB = fo.joinFaceOOP(2, "Billy");
			pC = fo.joinFaceOOP(3, "Corey");
			pD = fo.joinFaceOOP(4, "Darren");
			pE = fo.joinFaceOOP(5, "Emile");
			pF = fo.joinFaceOOP(6, "Francie");
			pM = fo.joinFaceOOP(600, "Millie");
			
			fo.addFriendship(pfA, pfB);
			fo.addFriendship(pfB, pfC);
			fo.addFriendship(pfC, pfD);
			fo.addFriendship(pfD, pfE);
			fo.addFriendship(pA, pfE);
			fo.addFriendship(pA, pfB);
			fo.addFriendship(pfD, pM);
			
			fo.addFriendship(pB, pC);
			fo.addFriendship(pC, pD);
			fo.addFriendship(pB, pD);
			fo.addFriendship(pE, pD);
			fo.addFriendship(pE, pB);
		} catch (Exception e) {fail();}
		
		//first test some connections between unconnected elements:
		boolean caught = false;
		try{
			fo.rank(pfA, pF);
		} 
		catch (ConnectionDoesNotExistException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pF, pE);
		} 
		catch (ConnectionDoesNotExistException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pA, pE);
		} 
		catch (ConnectionDoesNotExistException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pB, pfE);
		} 
		catch (ConnectionDoesNotExistException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		//now with people not in the system.
		Person pG = new PersonImpl(9, "Gerard");
		Person pH = new PersonImpl(100, "Harvey");
		caught = false;
		try{
			fo.rank(pH, pG);
		} 
		catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pG, pH);
		} 
		catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pG, pG);
		} 
		catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pH, pA);
		} 
		catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		caught = false;
		try{
			fo.rank(pE, pG);
		} 
		catch (PersonNotInSystemException e)
		{
			caught = true;
		}
		catch (Exception e) {fail();}
		assertTrue(caught);
		
		
		// Now test the ranks:
		try{
			assertTrue(fo.rank(pfA, pfA)==0);
			assertTrue(fo.rank(pfC, pfC)==0);
			assertTrue(fo.rank(pfE, pfE)==0);
			assertTrue(fo.rank(pA, pA)==0);
			assertTrue(fo.rank(pB, pB)==0);
			assertTrue(fo.rank(pE, pE)==0);
			assertTrue(fo.rank(pF, pF)==0);
			
			assertTrue(fo.rank(pfD, pM)==1);
			assertTrue(fo.rank(pA, pfB)==1);
			assertTrue(fo.rank(pfE, pA)==1);
			assertTrue(fo.rank(pfA, pfB)==1);
			assertTrue(fo.rank(pfB, pfC)==1);
			assertTrue(fo.rank(pfC, pfB)==1);
			assertTrue(fo.rank(pM, pfD)==1);
			assertTrue(fo.rank(pB, pD)==1);
			assertTrue(fo.rank(pD, pB)==1);
			assertTrue(fo.rank(pE, pD)==1);
			assertTrue(fo.rank(pD, pC)==1);
			assertTrue(fo.rank(pC, pB)==1);
			assertTrue(fo.rank(pB, pC)==1);
			
			assertTrue(fo.rank(pfA, pA)==2);
			assertTrue(fo.rank(pA, pfA)==2);
			assertTrue(fo.rank(pA, pfC)==2);
			assertTrue(fo.rank(pfC, pM)==2);
			assertTrue(fo.rank(pM, pfC)==2);
			assertTrue(fo.rank(pM, pfE)==2);
			assertTrue(fo.rank(pfB, pfE)==2);
			assertTrue(fo.rank(pfB, pfD)==2);
			assertTrue(fo.rank(pfD, pfB)==2);
			assertTrue(fo.rank(pfE, pfB)==2);
			assertTrue(fo.rank(pE, pC)==2);
			assertTrue(fo.rank(pC, pE)==2);
			
			assertTrue(fo.rank(pfA, pfE)==3);
			assertTrue(fo.rank(pfE, pfA)==3);
			assertTrue(fo.rank(pfB, pM)==3);
			assertTrue(fo.rank(pM, pfB)==3);
			assertTrue(fo.rank(pfA, pfD)==3);
			assertTrue(fo.rank(pfD, pfA)==3);
			
			assertTrue(fo.rank(pfA, pM)==4);
		}
		catch (Exception e) {fail();}
	}

	
	/*
	 * General Tests
	 */
	
	/* 
	 * Checks correct usage of equals function:
	 * Checks that equals is used within functions that are defined as doing something given the same
	 * person.  You should use equals and not ==, as the definition for "same person" in this exercise
	 * was the definition of equals, and not of ==.
	 * 
	 * Used like/unlike of status to test this.
	 */
	@Test
	public void equalsUsedTest()
	{
		Person pA2 = new PersonImpl(1, "Anne");
		Person pA3 = new PersonImpl(1, "Aaron");
		
		Status s1 = new StatusImpl(pA, "hello", 1);
		
		//pA, pA2, and pA3 should all be counted as the same person while liking/unliking - they are considered
		// the same person by the system.
		assertTrue(s1.getLikesCount().equals(0));
		s1.like(pA);
		assertTrue(s1.getLikesCount().equals(1));
		s1.like(pA2);
		assertTrue(s1.getLikesCount().equals(1));
		s1.unlike(pA3);
		assertTrue(s1.getLikesCount().equals(0));
	}
	
	
}
