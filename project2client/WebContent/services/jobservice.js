app.factory('JobService',function($http){
  //create an object, add functions, return object
	
	var jobService={}
	//addJob in service   <- addJob in controller   <-  jobform.html
	jobService.addJob=function(job){//get job object from JobCtrl ,JObCtrl will get it from view
		//$http.post(url,data)
		var url="http://localhost:8080/project2middleware/addjob"
		//call restful service
		return $http.post(url,job)//job in JSON format, will get added in the body of Http request
	}
	
	jobService.getAllJobs=function(){
		//how to get all jobs in service? 
		//Get all jobs by call a restful service
		var url="http://localhost:8080/project2middleware/getalljobs"
		return $http.get(url)
	}
	
	return jobService;
	
	
})
