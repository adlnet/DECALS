'use strict';


//Declare app level module which depends on filters, and services
angular.module('CompetencyManager', 
[
 'ngRoute',
 'ui.bootstrap.modal',
 'ui.bootstrap.typeahead',
 'CompetencyManager.filters',
 'CompetencyManager.services',
 'CompetencyManager.directives',
 'CompetencyManager.controllers',
 'CompetencyManager.definitions',
 ]).
 config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	 $(document).foundation();
	 

	 $routeProvider.when('/login', 
			 {
		 templateUrl: 'partials/login.html', 
		 controller: 'loginController'
			 }
	 );

	 $routeProvider.when('/home', 
			 {
		 templateUrl: 'partials/home.html', 
		 controller: 'homeController'
			 }
	 );


	 $routeProvider.when('/search/:context', 
			 {
		 templateUrl: 'partials/search/search.html', 
		 controller: 'searchController'
			 }
	 );

	 $routeProvider.when('/results/:context', 
			 {
		 templateUrl: 'partials/search/results.html', 
		 controller: 'resultsController',
		 reloadOnSearch: false
			 }
	 );


	 $routeProvider.when('/view/:context/:modelId/:itemId', 
			 {
		 templateUrl: 'partials/view/viewCompetency.html', 
		 controller: 'viewController'
			 }
	 );
	 $routeProvider.when('/view/:context/:itemId', 
			 {
		 templateUrl: function(routeParams){
			 switch(routeParams.context){
			 case 'profile':
				 return 'partials/view/viewProfile.html';
			 case 'model':
				 return 'partials/view/viewModel.html';
			 default:
				 return 'partials/view/viewCompetency.html';
			 }
		 }, 
		 controller: 'viewController'
			 }
	 );


	 $routeProvider.when('/edit/profile/:profileId', 
			 {
		 templateUrl: 'partials/edit/editProfile.html', 
		 controller: 'profileEditController'
			 }
	 );
	 $routeProvider.when('/edit/competency/:modelId/:competencyId', 
			 {
		 templateUrl: 'partials/edit/editCompetency.html', 
		 controller: 'competencyEditController'
			 }
	 );
	 $routeProvider.when('/edit/model/:modelId', 
			 {
		 templateUrl: 'partials/edit/editModel.html', 
		 controller: 'modelEditController'
			 }
	 );
	 $routeProvider.when('/edit/record/:userId/:recordId', 
			 {
		 templateUrl: 'partials/edit/editRecord.html', 
		 controller: 'recordEditController'
			 }
	 );

	 $routeProvider.when('/create/profile', 
			 {
		 templateUrl: 'partials/edit/editProfile.html', 
		 controller: 'profileEditController'
			 }
	 );
	 $routeProvider.when('/create/competency', 
			 {
		 templateUrl: 'partials/edit/editCompetency.html', 
		 controller: 'competencyEditController'
			 }
	 );
	 $routeProvider.when('/create/model/', 
			 {
		 templateUrl: 'partials/edit/editModel.html', 
		 controller: 'modelEditController'
			 }
	 );
	 $routeProvider.when('/create/record', 
			 {
		 templateUrl: 'partials/edit/editRecord.html', 
		 controller: 'recordEditController'
			 }
	 );
	 $routeProvider.when('/model-:partialModelId',
			 {
		 templateUrl: 'partials/public/viewPublicModel.html',
		 controller: 'viewPublicModelController',
		 reloadOnSearch: false,
			 }
	 );

	 $routeProvider.otherwise({redirectTo: '/login'});
	 $locationProvider.html5Mode(true);

 }]).run(['$rootScope', '$location', '$window', 'appCache', 'session', 'alert', 'search', 'context', '$routeParams', 'modelItem',
          function($rootScope, $location, $window, appCache, session, alert, search, contexts, $routeParams, modelItem){
	 
	 
	 $rootScope.session = session;
	 
	 session.loadUser().then(function(user){
		 appCache.profileCache[user.id] = user;
	 }, function(error){
		 $rootScope.goLogin();
	 });
	 
	 appCache.loadCaches();

	 var pushLocation = function(){
		 var obj = {};
		 obj.path = $location.path();
		 obj.context = appCache.context;
		 obj.search = {};
		 angular.extend(obj.search, $location.search());
		 appCache.pushPrevLoc(obj)
	 }
	 
	 if(appCache.modelCache['model-default'] == undefined || appCache.modelCache['model-default'] == {}){
		 modelItem.getAllModels();
	 }

	 $rootScope.showPublicModelPage = function(modelId, competencyId){
		 pushLocation();
		 
		 alert.clearMessages();
		 $location.path("/"+modelId)
		 
		 if(competencyId != undefined){
			 competencyId = competencyId.replace(':', "");
			 $location.hash(competencyId)
		 }
	 }
	 
	 $rootScope.goLogin = function(){
		 alert.clearMessages();
		 
		 if(session.currentUser.sessionId == undefined){
			 $location.path("/login");
		 }else{
			 $rootScope.goHome()
		 }
		 
		 $location.search({})
	 }

	 $rootScope.goHome = function(){
		 alert.clearMessages();
		 pushLocation();
		 
		 $location.path("/home");
		 $location.search({});
	 }

	 $rootScope.showSearch = function(context){
		 alert.clearMessages();

		 pushLocation();

		 if(appCache.context != context){
			 search.query = "";
		 }

		 appCache.setContext(context);
		 $location.search({});
		 $location.path("/search/"+appCache.context);
	 }

	 $rootScope.showResults = function(context, query, model){
		 alert.clearMessages();

		 pushLocation();

		 appCache.setContext(context);

		 if($location.path() != "/results/"+appCache.context){			 
			 $location.path("/results/"+appCache.context);
			 var obj = {};
			 
			 if(query != undefined && query != ""){
				 obj.query=query;
			 }
			 
			 if(model != undefined && model != "" && context == contexts.competency){
				 obj.model = model;
			 }
			 $location.search(obj);
			 
		 }
	 }

	 $rootScope.showCreate = function(context){
		 if(session.currentUser.id == session.guestUser.id){
			 alert.guestUserError();
			 return;
		 }
		 alert.clearMessages();

		 pushLocation();

		 // Save User Id if Creating Record
		 var id;
		 if(appCache.context == contexts.profile){
			 id = appCache.currentItem.id;
		 }else{
			 id = "";
		 }

		 appCache.setNewItem(context);

		 $location.$$search = {};
		 $location.path("/create/"+appCache.context);

		 // Set User ID in Search Parameters if Creating a Record
		 if(context == contexts.record && appCache.prevContext == contexts.profile){
			 $location.search({'userId': id}) ;
		 }
	 }

	 $rootScope.showEdit = function(context, itemId, modelId){
		 if(session.currentUser.id == session.guestUser.id){
			 alert.guestUserError();
			 return;
		 }
		 alert.clearMessages();

		 pushLocation();

		 var location = "/edit/"+context;

		 switch(context){
		 case contexts.competency:
			 location += "/"+modelId+"/"+itemId;
			 break;
		 case contexts.model:
			 if(itemId == "model-default"){
				 alert("Cannot edit default model!");
				 return false;
			 }

		 case contexts.profile:
			 location += "/"+itemId;
			 break;
		 case contexts.record:
			 location += "/"+modelId+"/"+itemId;
			 break;
		 default:
			 break;
		 }

		 $location.path(location);
	 }

	 $rootScope.showView = function(context, itemId, modelId){
		 alert.clearMessages();

		 pushLocation();

		 var location = "/view/"+context;

		 switch(context){
		 case contexts.competency:
			 location += "/"+modelId+"/"+itemId;
			 break;
		 case contexts.model:
		 case contexts.profile:      
			 location += "/"+itemId;
			 break;
		 default:
			 break;
		 }

		 
		 search.query = $location.$$search.query;
		 search.model = $location.$$search.model instanceof Array ? $location.$$search.model : new Array($location.$$search.model);
		
		 $location.search('query', undefined)
		 $location.search('model', undefined)
		 $location.$$search = {};
		 $location.path(location);
		 
	 }

	 $rootScope.logout = function(){
		 alert.clearMessages();
		 search.clearAll();
		 appCache.clearCaches();
		 session.logout();
		 $location.path("/login");
	 }
	 
	 $rootScope.goBack = function(){
		 alert.clearMessages();

		 var prevLoc = appCache.popPrevLoc();

		 if(prevLoc == undefined){
			 $rootScope.goHome();
		 }else{
			 if(prevLoc.path.indexOf("edit") != -1 || prevLoc.path.indexOf("create") != -1 || prevLoc.path.valueOf() == $location.path().valueOf()){
				 while(prevLoc != undefined && (prevLoc.path.indexOf("edit") != -1 || prevLoc.path.indexOf("create") != -1) || prevLoc.path.valueOf() == $location.path().valueOf()){
					 prevLoc = appCache.popPrevLoc();
					 if(prevLoc == undefined)
						 break;
				 }
				 
				 if(prevLoc == undefined){
				 	$rootScope.goHome();
				 }else{
					 $location.search(prevLoc.search);
					 $location.path(prevLoc.path);
					 appCache.setContext(prevLoc.context);
				 }
			 }else{
				 appCache.setContext(appCache.prevContext);
				 $location.path(prevLoc.path)
			 }
			 
		 }
	 }
	 
	 $rootScope.changeHash = function(hash){
		 pushLocation();
		 $location.hash(hash);
	 }

	 $rootScope.objectLength = function(obj){
		 if(obj instanceof Object){
			 return Object.keys(obj).length;  
		 }else{
			 return 0;
		 }
	 }

	 $rootScope.objectKeys = function(obj){
		 if(obj instanceof Object){
			 return Object.keys(obj);
		 }else{
			 return 0;
		 }
	 }

	 $rootScope.mathCeil = function(num){
		 return Math.ceil(num);
	 }
 }]);
