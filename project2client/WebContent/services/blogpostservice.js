/**
 * 
 */
app.factory('BlogPostService',function($http){
	var blogPostService={}
	var BASE_URL ="http://localhost:8080/project2middleware"
	
	//service(blogPost)   <- controller (blogPost)  <- view (user)
	blogPostService.addBlogPost=function(blogPost){//from frontend controller,get blogpost from controller
		//Service to ?middleware
		return $http.post(BASE_URL + "/addblog",blogPost)  //returning the response to the frontend controller 
	}
	
	blogPostService.getBlogsApproved=function(){
	 	return $http.get(BASE_URL + "/blogsapproved")
	}
	
	blogPostService.getBlogsWaitingForApproval=function(){
		return $http.get(BASE_URL + "/blogswaitingforapproval")
	}
	
	
	return blogPostService;
})