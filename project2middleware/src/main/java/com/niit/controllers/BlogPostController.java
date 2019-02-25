package com.niit.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.BlogPostDao;
import com.niit.dao.UserDao;
import com.niit.models.BlogPost;
import com.niit.models.ErrorClazz;
import com.niit.models.User;

@RestController
public class BlogPostController {
	@Autowired
private BlogPostDao blogPostDao;
	@Autowired
private UserDao userDao;
@RequestMapping(value="/addblog",method=RequestMethod.POST)
public ResponseEntity<?> addBlogPost(HttpSession session,@RequestBody BlogPost blogPost){
	//Check for Authenticated- only logged user can post a blog 
	String email=(String)session.getAttribute("loginId");
	if(email==null){//if the user is not yet logged in,user is not an authenticated user
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	blogPost.setPostedOn(new Date());
	User author=userDao.getUser(email);
	blogPost.setAuthor(author);//Value for FK author_email refers to email column in User table
	//Who is  the author? -> logged in user
	//Email of the logged in user -> session.getAttribute("loginId"),it is in the variable "email"
	//Get user object using email
	try{
		if(author.getRole().equals("ADMIN"))
			blogPost.setApproved(true);
	blogPostDao.addBlogPost(blogPost);
	}catch(Exception e){
		ErrorClazz errorClazz=new ErrorClazz(7,"Unable to insert blogpost.."+e.getMessage());
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<Void>(HttpStatus.OK);
}
@RequestMapping(value="/blogsapproved",method=RequestMethod.GET)
public ResponseEntity<?> getBlogsApproved(HttpSession session){
	String email=(String)session.getAttribute("loginId");
	if(email==null){
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	List<BlogPost> blogsApproved=blogPostDao.getBlogsApproved();
	return new ResponseEntity<List<BlogPost>>(blogsApproved,HttpStatus.OK);
}
@RequestMapping(value="/blogswaitingforapproval",method=RequestMethod.GET)
public ResponseEntity<?> getBlogsWaitingForApproval(HttpSession session){
	//CHECK FOR AUTHENTICATION
	String email=(String)session.getAttribute("loginId");
	if(email==null){
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");//login.html
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	//CHECK FOR AUTHORIZATION
	//CHECK IF THE REQUEST FROM ADMIN (LOGGED IN USER IS ADMIN)
	User user=userDao.getUser(email);
	if(!user.getRole().equals("ADMIN")){//Display the error message in the blogswaitingforapproval.html
		ErrorClazz errorClazz=
			new ErrorClazz(7,"Access Denied.. You are not authorized to view the blogs waiting for approval");
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	//if request is from an Admin
	List<BlogPost> blogsWaitingForApproval=blogPostDao.getBlogsWaitingForApproval();
	return new ResponseEntity<List<BlogPost>>(blogsWaitingForApproval,HttpStatus.OK);
}
@RequestMapping(value="/getblog/{blogPostId}")
public ResponseEntity<?> getBlog(HttpSession session,@PathVariable int blogPostId){
	//CHECK FOR AUTHENTICATION
	String email=(String)session.getAttribute("loginId");
	if(email==null){
		ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");//login.html
		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
	}
	BlogPost blogPost=blogPostDao.getBlog(blogPostId);//view -> controller -> service -> middleware -> dao
	return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
}
@RequestMapping(value="/approveblogpost",method=RequestMethod.PUT)
public ResponseEntity<?> approveBlogPost(HttpSession session,@RequestBody BlogPost blogPost){
	//CHECK FOR AUTHENTICATION
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");//login.html
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		//CHECK FOR AUTHORIZATION
		//CHECK IF THE REQUEST FROM ADMIN (LOGGED IN USER IS ADMIN)
		User user=userDao.getUser(email);
		if(!user.getRole().equals("ADMIN")){//Display the error message in the blogswaitingforapproval.html
			ErrorClazz errorClazz=
				new ErrorClazz(7,"Access Denied.. You are not authorized to view the blogs waiting for approval");
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		blogPost.setApproved(true);
		blogPostDao.approveBlogPost(blogPost);
		return new ResponseEntity<Void>(HttpStatus.OK);
}
@RequestMapping(value="/rejectblogpost",method=RequestMethod.PUT)
public ResponseEntity<?> rejectBlogPost(HttpSession session,@RequestBody BlogPost blogPost){
	//CHECK FOR AUTHENTICATION
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClazz errorClazz=new ErrorClazz(5,"Please login..");//login.html
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		//CHECK FOR AUTHORIZATION
		//CHECK IF THE REQUEST FROM ADMIN (LOGGED IN USER IS ADMIN)
		User user=userDao.getUser(email);
		if(!user.getRole().equals("ADMIN")){//Display the error message in the blogswaitingforapproval.html
			ErrorClazz errorClazz=
				new ErrorClazz(7,"Access Denied.. You are not authorized to view the blogs waiting for approval");
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		blogPostDao.rejectBlogPost(blogPost);
		return new ResponseEntity<Void>(HttpStatus.OK);
}

}



