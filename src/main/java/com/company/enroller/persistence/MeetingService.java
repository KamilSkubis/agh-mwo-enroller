package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Map;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Meeting getMeetingById(String meetingId) {
		try{
		Session session = connector.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meeting> criteriaQuery = builder.createQuery(Meeting.class);
		Root<Meeting> meetingRoot = criteriaQuery.from(Meeting.class);
		meetingRoot.fetch("participants", JoinType.LEFT);
		criteriaQuery.
				select(meetingRoot).
				where(builder.equal(meetingRoot.get("id"), meetingId));

		Query<Meeting> meetingQuery = session.createQuery(criteriaQuery);
			return meetingQuery.getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}

	public void addMeeting(Meeting meeting) {
		Session session = connector.getSession();
		Transaction t = session.beginTransaction();
		session.save(meeting);
		t.commit();
	}


	public void deleteMeeting(Meeting meeting) {
		Session session = connector.getSession();
		Transaction t = session.beginTransaction();
		session.delete(meeting);
		t.commit();

	}

	public void updateData(Map<String, String> updates, Meeting meeting) {

		Session session = connector.getSession();
		Transaction t = session.beginTransaction();
		session.evict(meeting);

		if(updates.containsKey("title")){
			meeting.setTitle(updates.get("title"));
		}
		if(updates.containsKey("description")){
			meeting.setDescription(updates.get("description"));
		}
		if(updates.containsKey("date")){
			meeting.setDate(updates.get("date"));
		}
		if(updates.containsKey("participant")){
			ParticipantService participantService = new ParticipantService();
			Participant participant = participantService.findByLogin(updates.get("participant"));
			meeting.addParticipant(participant);
		}
		session.update(meeting);
		t.commit();

	}

	public void deleteParticipantFromMeeting(Meeting meeting, Participant participant) {
		Session session = connector.getSession();
		Transaction t = session.beginTransaction();
		session.evict(meeting);
		meeting.getParticipants().remove(participant);
		session.update(meeting);
		t.commit();
	}
}
