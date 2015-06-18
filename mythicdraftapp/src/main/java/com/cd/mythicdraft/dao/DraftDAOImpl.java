package com.cd.mythicdraft.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cd.mythicdraft.model.Draft;

@Repository(value = "draftDao")
public class DraftDAOImpl implements DraftDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	@Override
	public boolean getDuplicateHash(String md5Hash) {
		return false;
	}

	@Override
	public void addDraft(Draft draft) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(draft);
	}

}
