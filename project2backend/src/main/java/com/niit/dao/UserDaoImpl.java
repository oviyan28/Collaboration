package com.niit.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.models.User;
@Repository
@Transactional
public class UserDaoImpl implements UserDao {
	@Autowired
private SessionFactory sessionFactory;
	public void userRegistration(User user) {
	  Session session=sessionFactory.getCurrentSession();
	  session.save(user);
	}
	//new user register by entering james.s@niit.com
	//new user registers by entering adam.e@niit.com
	public boolean isEmailUnique(String email) {
		Session session=sessionFactory.getCurrentSession();
		//Get user object by using email
		//check if user already exists with the same email.
		//select * from user_s190224 where email='james.s@niit.com'
		//select * from user_s190224 where email='adame.e@niit.com'
		User user=(User)session.get(User.class,email);
		if(user==null) //if email is unique
			return true;
			else //if email is not unique
				return false;
	}
	
	public boolean isPhonenumberUnique(String phonenumber){
		//phonenumber property is not an Identifier, not annoated phonenumbe with @Id
		Session session=sessionFactory.getCurrentSession();
		//select * from user_s190224 where phonenumber='098772387'
		Query query=session.createQuery("from User where phonenumber=:pnumber");
		query.setString("pnumber", phonenumber);
		User user=(User)query.uniqueResult();
		if(user==null)//phonenumber is unique
			return true;
		else //phonenumber is duplicate
			return false;
		
	}
	public User login(User user) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from User where email=:email and password=:pwd");
		query.setString("email",user.getEmail() );
		query.setString("pwd", user.getPassword());
		User validUser=(User)query.uniqueResult();//1 user object or null value
		//1 user object - 1 rows, if email and password is correct(valid credentials)
		//null value - 0 rows, if email/password is incorrect (invalid credentials)
		return validUser;
	}
	public void updateUser(User validUser) {
		Session session=sessionFactory.getCurrentSession();
		session.update(validUser);
		
	}
	public User getUser(String email) {
		Session session=sessionFactory.getCurrentSession();
		User user=(User)session.get(User.class, email);
		return user;
	}
	public boolean isUpdatedPhonenumberUnique(String phonenumber, String email) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from User where email!=:email and phonenumber=:phonenumber");
		query.setString("email", email);
		query.setString("phonenumber", phonenumber);
		User user=(User)query.uniqueResult();
		if(user==null)
			return true;
		else
			return false;
	}

}




