package OOP2.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.ConnectionDoesNotExistException;
import OOP2.Provided.FaceOOP;
import OOP2.Provided.Person;
import OOP2.Provided.PersonAlreadyInSystemException;
import OOP2.Provided.PersonNotInSystemException;
import OOP2.Provided.SamePersonException;
import OOP2.Provided.Status;
import OOP2.Provided.StatusIterator;

public class FaceOOPImpl implements FaceOOP {

	final Set<Person> users = new HashSet<Person>();
	private class PersonComparator implements Comparator<Person> {
		public int compare(Person p1, Person p2) {
			return p1.getId() - p2.getId();
		}
	}

	/**
	 * Constructor - receives no parameters and initializes the system.
	 */
	public FaceOOPImpl() { //checking pull request / sync??
	}

	@Override
	public Person joinFaceOOP(Integer id, String name) throws PersonAlreadyInSystemException {
		Person user = new PersonImpl(id, name);
		if (users.contains(user)) {
			throw (new PersonAlreadyInSystemException());
		}
		users.add(user);
		return user;
	}

	@Override
	public int size() {
		return users.size();
	}

	@Override
	public Person getUser(Integer id) throws PersonNotInSystemException {
		Person temp = new PersonImpl(id, "check");
		if (!users.contains(temp))
			throw (new PersonNotInSystemException());
		for (Person p : users) {
			if (p.equals(temp))
				return p;
		}
		return null; // shouldn't get to here..
	}

	@Override
	public void addFriendship(Person p1, Person p2)
			throws PersonNotInSystemException, SamePersonException, ConnectionAlreadyExistException {
		if (!users.contains(p1) || !users.contains(p2)) {
			throw (new PersonNotInSystemException());
		}
		p1.addFriend(p2);
		p2.addFriend(p1);
	}
	
	class FriendsIterator implements Iterator<Person>{
		final List<Person> users;
		Person curr_user; //holds curr user in 'users' collection
		Person next_user; //holds next user in 'users' collection
		boolean isNotLast;/*ch5*/
		
		public FriendsIterator(Set<Person> users) {
			this.users = new ArrayList<Person>(users);
			Collections.sort(this.users, new PersonComparator());
			curr_user = null;
			next_user = null;
			isNotLast = false; /*ch4*/
		}
		@Override public boolean hasNext() {
			if (next_user != null && curr_user == null) return true; //ch1
			for (Person user: users){
				//if its the first time we use hasNext or user is not last we return 'true':
				if ((curr_user == null && next_user == null && isNotLast /*ch2*/) || isNotLast){
					next_user = user; // updating field
					return true;
				}
				if (user.equals(curr_user)){
					isNotLast = true;
				}
			}
			isNotLast = false; /*ch3*/
			return false;
		}
		@Override public Person next() {
			curr_user = (hasNext()) ? next_user:null;
			if (curr_user == null) next_user = null; /*ch4*/
			return curr_user;
		}
		
		@Override
		public void remove() {
			// No implementation needed
		}
	}
	
	@Override
	public Iterator<Person> iterator() {
		return new FriendsIterator(users);
	}

		
	class StatusIteratorImpl implements StatusIterator {
		List<Person> friends;
		Person curr_friend = null;
		Person next_friend = null;
		Iterable<Status> next_friend_statuses = null;
		boolean byRecent; //if true then iterating over statuses from new to old. else - from most popular to least popular.
		Iterator<Status> status_it = null;
		
		public StatusIteratorImpl(Collection<Person> friends, boolean byRecent) {
			this.friends = new ArrayList<Person>(friends);
			Collections.sort(this.friends, new PersonComparator());
			for (Person friend: friends){
				curr_friend = friend;
				status_it = ((byRecent) ? curr_friend.getStatusesRecent():curr_friend.getStatusesPopular()).iterator();
				break;
			}
			this.byRecent = byRecent;
		}

		@Override public boolean hasNext() {
			if (curr_friend == null || status_it == null) return false;
			if (status_it.hasNext()) return true;
			boolean isNotLast = false;
			for (Person friend : friends){
					if (isNotLast){
							next_friend_statuses = (byRecent) ? friend.getStatusesRecent():friend.getStatusesPopular();
							if (next_friend_statuses.iterator().hasNext()){
								status_it = next_friend_statuses.iterator();
								next_friend = friend;
								return true;
							}
					}
					if (curr_friend.equals(friend)){
						isNotLast = true;
					}
			}
			return false;
		}
		
		@Override public Status next() {
			if (status_it.hasNext()){
				return status_it.next(); //curr friend has more statuses
			}
			if (hasNext()){ //curr friend has no more statues. call to has next also changes status iterator
				curr_friend = next_friend;
				return status_it.next();
			}
			status_it = null;
			return null;
		}
		
		@Override
		public void remove() {
			// No implementation needed
		}
	}
	
	@Override
	public StatusIteratorImpl getFeedByRecent(Person p) throws PersonNotInSystemException {
		if (!users.contains(p))
			throw new PersonNotInSystemException();
		return new StatusIteratorImpl(p.getFriends(), true);
	}

	@Override
	public StatusIterator getFeedByPopular(Person p) throws PersonNotInSystemException {
		if (!users.contains(p))
			throw new PersonNotInSystemException();
		return new StatusIteratorImpl(p.getFriends(), false);
	}
	
	class BFSEmulator {
		//updates all the reachable vertexes online (during the BFS run) 
		HashMap<Person,Integer> usersVertexes = new HashMap<Person,Integer>(); 
		//the queue for the BFS Algorithm
		LinkedList<Person> bfsQueue = new LinkedList<Person>();
		Person source, target;
		
		//constructor
		BFSEmulator(Person source, Person target) {
			this.source = source;
			this.target = target;
			bfsQueue.add(source);
			usersVertexes.put(source, 0);
		}
		
		int getDistance (Person p) {
			Integer d = usersVertexes.get(p);
			if (d!=null) {
				return d;
			}
			return -1;
		}
		
		Integer runBfs() throws ConnectionDoesNotExistException {
			if (source.equals(target)) return 0;
			while (!bfsQueue.isEmpty()) {
				Person parent= bfsQueue.getFirst();
				bfsQueue.removeFirst();
				int parent_d = this.getDistance(parent);
				for (Person friend : parent.getFriends()){
					if (!usersVertexes.containsKey(friend)) {
						usersVertexes.put(friend,parent_d+1);
						bfsQueue.addLast(friend);
					}
				}
			}
			int target_d = this.getDistance(target);
			if (target_d < 0)
				throw new ConnectionDoesNotExistException();
			return target_d;
		}
	}
	
	@Override
	public Integer rank(Person source, Person target)
			throws PersonNotInSystemException, ConnectionDoesNotExistException {
		if (!users.contains(source) || !users.contains(target)) {
			throw new PersonNotInSystemException();
		}
		BFSEmulator bfs = new BFSEmulator(source, target);
		return bfs.runBfs();
	}
}
