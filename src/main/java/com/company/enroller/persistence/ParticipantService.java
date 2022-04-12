package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.internal.TransactionImpl;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Participant findByLogin(String login) {
        try {
            Session session = connector.getSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery criteria = cb.createQuery();
            Root<Participant> participant = criteria.from(Participant.class);
            criteria.select(participant).where(cb.equal(participant.get("login"), login));

            Query<Participant> query = session.createQuery(criteria);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public void writeParticipant(Participant participant){
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(participant);
        transaction.commit();
    }

    public void updateParticipant(String login, Participant participant){
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        Participant p = findByLogin(login);
        session.evict(p);
        p.setPassword(participant.getPassword());
        session.update(p);
        transaction.commit();
    }


}
