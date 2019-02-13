app.controller('JobCtrl',function($scope,JobService,$location){//JobService is an Service object with two functions addJob(),getAllJobs()
   $scope.show=false;
	$scope.addJob=function(){
		JobService.addJob($scope.job).then(
		function(response){//from restful service, if client get the response with status code in the range 200 to 2xx 
			alert('Job details inserted successfully...')		
		    $scope.job={}
			$location.path('/getalljobs')
		},function(response){//from restful service, if client gets the response with status code in the range 400 to 4xx /500 to 5xx 
			console.log(response.status)
			if(response.status==400)
				$scope.message="CLIENT ERROR... BAD REQUEST"
				else
			$scope.error=response.data// ErrorClazz object in JSON representation 
			//{'errorCode':1,'errorMessage':'Job details not inserted..something went wrong..'}
		})
	}
	
	JobService.getAllJobs().then(
			function(response){
				//response.data is  Array of Job objects
				$scope.jobs=response.data
				console.log(response.status)
			},function(response){
				console.log(response.status)
			})
			
	$scope.showDetails=function(jobId){
		
		//toggle the value from false to true,true to false,false to true
		$scope.show=!$scope.show
		$scope.jobId=jobId
	}
})



