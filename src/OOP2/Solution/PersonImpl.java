package OOP2.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import OOP2.Provided.Person;
import OOP2.Provided.Status;
import OOP2.Provided.ConnectionAlreadyExistException;
import OOP2.Provided.SamePersonException;

public class PersonImpl implements Person {
	Integer id;
	String name;
	List<Status> statuses = new ArrayList<Status>();
	Integer statusCounter = 0;
	Set<Person> friends = new HashSet<Person>();
	
	/*comparators*/
	private class StatusComparatorByDate implements Comparator<Status> {
		public int compare(Status s1, Status s2) {
			// assumes that the statuses have the same publisher (this person).
			return s2.getId() - s1.getId();
		}
	}
	private class StatusComparatorByLikes implements Comparator<Status> {
		public int compare(Status s1, Status s2) {
			// assumes that the statuses have the same publisher (this person).
			if (s1.getLikesCount() == s2.getLikesCount()){
				return s2.getId() - s1.getId();
			}
			return s2.getLikesCount() - s1.getLikesCount();
		}
	}
	
	/**
	 * Constructor receiving person's id and name.
	 */
	public PersonImpl(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Status postStatus(String content) {
		Status status = new StatusImpl(this, content, statusCounter);
		if (!statuses.contains(status)) {
			statusCounter++;
			statuses.add(status);
		}
		return status;
	}

	@Override
	public void addFriend(Person p) throws SamePersonException, ConnectionAlreadyExistException {
		if (this.equals(p)) {
			throw new SamePersonException();
		}
		if (friends.contains(p)) {
			throw new ConnectionAlreadyExistException();
		}
		friends.add(p);
	}

	@Override
	public Collection<Person> getFriends() {
		return new HashSet<Person>(friends);
	}

	@Override
	public Iterable<Status> getStatusesRecent() {
		List<Status> sortedList = new ArrayList<Status>(statuses);
		Collections.sort(sortedList, new StatusComparatorByDate());
		return sortedList;
	}

	@Override
	public Iterable<Status> getStatusesPopular() {
		List<Status> sortedList = new ArrayList<Status>(statuses);
		Collections.sort(sortedList, new StatusComparatorByLikes());
		return sortedList;
	}

	@Override
	public int compareTo(Person o) {
		return id - o.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PersonImpl)) {
			return false;
		}
		PersonImpl other = (PersonImpl) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
