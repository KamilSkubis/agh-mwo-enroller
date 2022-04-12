package com.company.enroller;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.DatabaseConnector;
import org.hibernate.Session;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Iterator;
import java.util.List;

public class TestMain {

    public static void main(String[] args) {
        DatabaseConnector db = DatabaseConnector.getInstance();
        Session s = db.getSession();
        String hql = "FROM Participant";
        Query q = s.createQuery(hql);
        List res  = q.list();
        for (Object participant : res) {
            Participant m = (Participant) participant;
            System.out.println(m.getLogin() + " " + m.getPassword());
        }


        System.out.println("_________");

        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery criteria = cb.createQuery();
        Root<Participant> i =criteria.from(Participant.class);
        criteria.select(i).where(cb.equal(i.get("login"),"user10"));

        Query<Participant> q2 = s.createQuery(criteria);
        List<Participant> participants2 = q2.getResultList();

        for (Object p : participants2) {
            Participant m = (Participant) p;
            System.out.println(m.getLogin() + " " + m.getPassword());
        }








    }
}
