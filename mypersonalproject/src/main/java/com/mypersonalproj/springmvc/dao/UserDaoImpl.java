package com.mypersonalproj.springmvc.dao;

import java.util.Collection;
import java.util.List;

import javax.management.Query;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.mypersonalproj.springmvc.model.User;


@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

	public User findById(int id) {
		User user = getByKey(id);
		if(user!=null){
			initializeCollection(user.getUserProfiles());
		}
		return user;
	}

	public User findBySSO(String sso) {
		System.out.println("SSO : "+sso);
		try{
			User user = (User) getEntityManager()
					.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
					.setParameter("ssoId", sso)
					.getSingleResult();
			
			if(user!=null){
				initializeCollection(user.getUserProfiles());
			}
			return user; 
		}catch(NoResultException ex){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		List<User> users = getEntityManager()
				.createQuery("SELECT u FROM User u ORDER BY u.firstName ASC")
				.getResultList();
		return users;
	}

	public void save(User user) {
		persist(user);
	}

	public void deleteBySSO(String sso) {
		User user = (User) getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
				.setParameter("ssoId", sso)
				.getSingleResult();
		delete(user);
	}
	
	//An alternative to Hibernate.initialize()
	protected void initializeCollection(Collection<?> collection) {
	    if(collection == null) {
	        return;
	    }
	    collection.iterator().hasNext();
	}

	@Override
	public boolean findUserByIdAndPassword(Integer id, String password) {
		
		try{
			User user = (User)getEntityManager().createQuery("SELECT u FROM User u WHERE u.id='"+id+"' AND u.password='"+password+"'").getSingleResult();
			if(user!=null)
				return true;
		}catch(Exception ex){
			return false;
		}
		return false;
	}
}
