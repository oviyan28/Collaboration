package com.niit.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.JobDao;
import com.niit.dao.UserDao;
import com.niit.models.ErrorClazz;
import com.niit.models.Job;
import com.niit.models.User;
@RestController
public class JobController {
	@Autowired
private JobDao jobDao;
	@Autowired
private UserDao userDao;

//T is Type of data that will be added in the body of HttpResponse
//if Job details gets inserted successfully  - T is Job object
//if there is an exception while inserting job details  - T is ErrorClazz object
//client has to add the data in the body of the Http Request
//handler method has to read the data from the body of the request
@RequestMapping(value="/addjob",method=RequestMethod.POST)
public  ResponseEntity<?>  addJob(@RequestBody Job job,HttpSession session){
	//Check for authentication (user has logged in or not)
	String email=(String)session.getAttribute("loginId");
	if(email==null){//not logged in,user is not an authenticated user
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);//401 - login.html
	}
	//CHECK FOR AUTHORIZATION (check if the role of the logged in user is ADMIN
	User user=userDao.getUser(email);
	if(!user.getRole().equals("ADMIN")){//only admin can add a job details, Other roles are not authorized to add a job
		ErrorClazz errorClazz=new ErrorClazz(6,"Access Denied..You are not authorized to post any job details..");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);//Jobform.html, Display the error message
	}
	try{
	job.setPostedOn(new Date());
	jobDao.addJob(job);
	System.out.println("Session Id is " +session.getId());
	System.out.println("Session createdTime " + session.getCreationTime());
	System.out.println("Session Attribute loginId value is "+ session.getAttribute("loginId"));
	}catch(Exception e){
		ErrorClazz errorClazz=new ErrorClazz(1,"Job details not inserted..something went wrong.." +e.getMessage());
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);//500
	}
	return new ResponseEntity<Job>(job,HttpStatus.OK); //- Success function 1 in client side will get executed,200
	//success - Job,200 OK
	//exception - ErrorClazz,500 ISE
	//So we need 2 ResponseEntity Objects
	//in controller , in callback function, response.data -> ErrorClazz / job
	//function for success - response.data is Job , response.status - 200 OK
	//function for error - response.data is ErrorClazz , response.status - 500
	
}
@RequestMapping(value="/getalljobs",method=RequestMethod.GET)
public  ResponseEntity<?>  getAllJobs(HttpSession session){
	//Get the data from Dao
	System.out.println("Session Id is " +session.getId());
	System.out.println("Session createdTime " + session.getCreationTime());
	System.out.println("Session Attribute loginId value is "+ session.getAttribute("loginId"));
	
	String email=(String)session.getAttribute("loginId");
	if(email==null){//not logged in,user is not an authenticated user
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	List<Job> jobs=jobDao.getAllJobs();
	
	return new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
}
}




