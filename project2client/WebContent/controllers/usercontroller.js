/**
 * UserCtrl
 * To get UserService=
 * function($http){var userService={},userService.userRegistrion=function(user),return userService}
 */
app.controller('UserCtrl',
		function($scope,UserService,$location,$rootScope,$cookieStore){
	$scope.userRegistration=function(user){
		//Call a function in a service and pass the user object to that function
		UserService.userRegistration(user).then(
				function(response){//change the path to /login
					$location.path('/login')
				},
				function(response){//Display the error message in registrationform.html
					//Send the data from controller to the view
					//Data is ErrorClazz object
					$scope.error=response.data//response.data is ErrorClazz object
					
				})
	}
	$scope.login=function(user){//user object in json fmt -> {'email':'...','password':'...'}
		UserService.login(user).then(function(response){
			$rootScope.user=response.data//User object -> user variable we are using it index.html
			//to restore the user details in $rootScope variable user, storing it in cookieStore
			//Store the user details in cookie ,remove the details from the cookie once the user logout
			$cookieStore.put('userDetails',response.data)
			$location.path('/home')
		},function(response){
			$scope.error=response.data//response.data is ErrorClazz object from Restful service (middleware)
		})
	}
	//This statement has to get executed only for logged in user 
	//(login and registration pages this statement should not get executed)
	if($rootScope.user!=undefined){
	UserService.getUser().then(function(response){
		$scope.user=response.data
	},function(response){
		console.log(response.status)
		console.log(response.data)
	})
	}
	
	$scope.updateUserProfile=function(user){//updated user details from view
		UserService.updateUserProfile(user).then(
				function(response){
					alert('Updated user details successfully..')
					$rootScope.user=user
					$cookieStore.put('userDetails',user)
					$location.path('/home')
				},function(response){
					if(response.status==401)
						$location.path('/login')
					//if status is 500, display the error message in updateuserprofile.html
					$scope.error=response.data	//ErrorClazz object from middleware 
				})
	}
})


