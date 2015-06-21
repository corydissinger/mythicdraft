package com.cd.mythicdraft.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
		sessionFactory = sf;
	}	
	
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
