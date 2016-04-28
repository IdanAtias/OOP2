package OOP2.Solution;

import OOP2.Provided.Person;
import OOP2.Provided.Status;

import java.util.HashSet; //TODO - CHECK IF POSSIBLE
import java.util.Set; 


public class StatusImpl implements Status {
	Person publisher;
	String content;
	Integer id;
	Set<Person> likers; //holds persons that liked the status
	/*
	 * A constructor that receives the status publisher, the text of the status
	 *  and the id of the status.
	 */
	public StatusImpl(Person publisher, String content, Integer id)
	{
		//TODO - check if we need 'new' for those fields.
		this.publisher = publisher;
		this.content = content;
		this.id = id;
		this.likers = new HashSet<Person>();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Person getPublisher() {
		return publisher;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void like(Person p) {
		likers.add(p);
	}

	@Override
	public void unlike(Person p) {
		likers.remove(p);
	}

	@Override
	public Integer getLikesCount() {
		return likers.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StatusImpl)) {
			return false;
		}
		StatusImpl other = (StatusImpl) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (publisher == null) {
			if (other.publisher != null) {
				return false;
			}
		} else if (!publisher.equals(other.publisher)) {
			return false;
		}
		return true;
	}


	
}
