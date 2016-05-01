package OOP2.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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
	public FaceOOPImpl() {
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
		
		public FriendsIterator(Set<Person> users) {
			this.users = new ArrayList<Person>(users);
			Collections.sort(this.users, new PersonComparator());
			curr_user = null;
			next_user = null;
		}
		@Override public boolean hasNext() {
			boolean isNotLast = false;
			for (Person user: users){
				//if its the first time we use hasNext or user is not last we return 'true':
				if ((curr_user == null && next_user == null) || isNotLast){
					next_user = user; // updating field
					return true;
				}
				if (user.equals(curr_user)){
					isNotLast = true;
				}
			}
			return false;
		}
		@Override public Person next() {
			curr_user = hasNext() ? next_user:null;
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

	@Override
	public Integer rank(Person source, Person target)
			throws PersonNotInSystemException, ConnectionDoesNotExistException {
		
		return null;
	}

}
