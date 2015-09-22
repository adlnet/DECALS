'use strict';

/* Services */


//Demonstrate how to register services
//In this case it is a simple value service.
angular.module('CompetencyManager.services', ['CompetencyManager.definitions']).
value('evidenceValueType', {
	URI: "URI",
	String: "String",
	Filename:"File"
}).
value('newItem', {
	'competency': {
		id: "N/A",
		modelId: "model-default",
		title: "",
		descriptions: [],
		levels: {":true": {id: ":true", name: "True", rank: 1}, ":false": {id: ":false", name:"False", rank: 0}},
		relationships: {},
	}, 
	'model': {
		id: "N/A",
		name: "",
		description: "",
		levels: {},
		allLevels: {":true": {id: ":true", name: "True", rank: 1}, ":false": {id: ":false", name:"False", rank: 0}},
		accessControl:{admin:[], user:[], agent:[], type:"public"}
	}, 
	'profile': {
		id: "",
		password: "",
		firstName: "",
		lastName: "",
	},
	'record': {
		competencyModelId: "model-default",
		competencyId:"",
		levelId: "",
		validationIds: [],
		validations: {},
	},
	'validation': {
		agentId: "",
		confidence: "",
		date: "",
		evidenceIds: [],
		evidences: {},
	},
	'evidence': {
		type: "",
		date: "",
		description: "",
		valueType:"String",
		value:"",
		valueFile:undefined,
		result: "",
	}
}).

value('context', {
	competency: 'competency', 
	model: 'model', 
	profile: 'profile',
	record: 'record',
}).

value('competencyRelationships', {
	"Narrows To": ":narrower",
	"Broadens To": ":broader",
	"Required For": ":requiredFor",
	"Requires": ":requires",
	"Desired For": ":desiredFor",
	"Desires": ":desires",
	"Enabled By": ":enabledBy",
	"Enables": ":enables",
	"Equivalent To": ":sameAs",
	"Related To": ":related",
}).

factory('search', ['$rootScope', '$q', 'appCache', 'context', 'modelItem', 'competencyItem', 'userItem', 'alert',
                   function($rootScope, $q, appCache, contexts, modelItem, competencyItem, userItem, alert){
	
	var ALL_MODELS = ["all"];

	var prevSearchedContext = "";
	var prevQuery = "";
	var prevModels = [];
	
	var query = "";
	var model = "";

	var results = {};

	var resultLength = 0;
	
	var search = function(query, context, model){
		var search = this;
		
		this.searchPromise(query, context, model).then(function(results){
			for(var id in results){
				if(search.results[id] == null)
					search.results[id] = {};
				
				for(var field in results[id]){
					search.results[id][field] = results[id][field]
				}
				
				search.resultLength = Object.keys(results).length;    

				if(search.resultLength == 0)
					search.resultLength = -1;
			}
		}, function(error){
			alert.setErrorMessage(error);
		}, function(tempResults){
			for(var id in tempResults){
				if(search.results[id] == null)
					search.results[id] = {};
				
				for(var field in tempResults[id]){
					search.results[id][field] = tempResults[id][field]
				}
				
				search.resultLength = Object.keys(tempResults).length;    
			}
		})
		
	}
	
	var searchPromise = function(query, context, model){
		if(query != null){
			this.query = query;
		}else if(this.query == "" && this.query == undefined){
			if(this.prevQuery != "" && this.prevQuery != undefined){
				this.query = this.prevQuery;
			}else{
				var deferred = $q.defer();
				
				setTimeout(function(){
					deferred.reject("Cannot Search on Empty String");
				})
				
				return deferred.promise;
			}
		}
		
		if(this.prevQuery != this.query || this.prevSearchedContext != context || this.prevModels != model){
			this.results = {};
			this.resultLength = 0;
			
			if(model == "" || model == undefined || model.length == 0){
				this.model = this.ALL_MODELS;
			}else{
				this.model = model;
			}
			this.prevModels = model;
			this.prevSearchedContext = context;
			this.prevQuery = this.query;

			this.results = {};
			this.resultLength = 0;

			switch(context){
			case contexts.competency:
				return competencyItem.searchCompetencyPromise(this.query, this.model);
				break;
			case contexts.model:
				return modelItem.searchModelsPromise(this.query);
				break;
			case contexts.profile:
				return userItem.searchUsersPromise(this.query);
				break;
			}
		}else{
			var deferred = $q.defer();
			
			var search = this;
			setTimeout(function(){
				deferred.resolve(search.result);
			})
			
			return deferred.promise;
		}
		
	}
	
	var viewAll = function(context, model){
		var search = this;
		
		this.viewAllPromise(context, model).then(function(results){
			if(search.model == model || context == contexts.profile){
				for(var id in results){
					if(search.results[id] == null)
						search.results[id] = {};
					
					for(var field in results[id]){
						search.results[id][field] = results[id][field]
					}
				}
				
				search.resultLength = Object.keys(results).length;    

				if(search.resultLength == 0)
					search.resultLength = -1;
			}
		}, function(error){
			alert.setErrorMessage(error);
		}, function(tempResults){
			for(var id in tempResults){
				if(search.results[id] == null)
					search.results[id] = {};
				
				for(var field in tempResults[id]){
					search.results[id][field] = tempResults[id][field]
				}
			}
			
			search.resultLength = Object.keys(tempResults).length;    
		})
	}
	
	var viewAllPromise = function(context, model){
		this.query = "";
		this.prevSearchedContext = context;
		this.model = (model == undefined || model == "" || model.length == 0) ? this.ALL_MODELS : model;
		this.results = {};
		this.resultLength = 0;
		
		
		var search = this;
		
		switch(context){
		case contexts.competency:
			return competencyItem.getAllCompetencies(this.model)
			break;
		case contexts.model:
			return modelItem.getAllModels();
			break;
		case contexts.profile:
			return userItem.getAllUsers();
			break;
		}
	}

	var setResults = function(results, context){

		this.results = results;
		this.resultLength = Object.keys(results).length;    

		if(this.resultLength == 0)
			this.resultLength = -1;
	}

	var removePreviousSearch = function(context){
		if(this.prevSearchedContext == context){
			this.prevSearchedContext = "";
			this.prevQuery = "";
		}
	}
	
	var clearAll = function(){
		this.prevSearchedContext = "";
		this.prevQuery = "";
		this.query = "";
		
		this.results = {};
		this.resultLength = 0;
	}

	return {
		ALL_MODELS: ALL_MODELS,
		
		prevSearchedContext: prevSearchedContext,
		prevQuery: prevQuery,
		prevModels: prevModels,
		
		query: query,
		model: model,
		
		results: results,
		resultLength: resultLength,
		runSearch: search,

		search2:search,
		searchPromise: searchPromise,
		
		viewAll: viewAll,
		viewAllPromise: viewAllPromise,
		
		setResults: setResults,
		clearResults: removePreviousSearch,
		
		clearAll: clearAll,
	}
}]).

factory('session', ['$rootScope', '$q', '$http', '$routeParams', 'apiURL', 'dataObjectName', 'guestUser',
                           function($rootScope, $q, $http, $routeParams, apiURL, dataObjectName, guestUserDefinition){
	var guestUser = {id: "", password: ""};
	if(guestUserDefinition.exists){
		guestUser.id = guestUserDefinition.userId;
		guestUser.password = guestUserDefinition.password;
	}
	
	var currentUser = {id: ""};
	
	var loginMessage = {code:"", msg:""};
	
	var sessionUser = function(user, userId){
		this.id = userId;

		this.modelId = user.modelId ? user.modelId : user.modelid;  
		this.dateCreated = user.dateCreated ? user.dateCreated : user.datecreated;

		this.firstName = user.firstName ? user.firstName : user.firstname;
		this.lastName = user.lastName ? user.lastName : user.lastname;

		this.email = user.email;

		this.records = {};
		this.competencies = {};

		this.sessionId = user.sessionId;
	}
	
	var login = function(userId, password){
		var save = this.saveUser;

		var curUser = this.currentUser;

		var deferred = $q.defer();

		var obj = {'userId': userId, 'password': password};

		var data = new FormData();

		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "session/login", data,
		{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
		}).success(function(data, status, headers, config){
			var user = new sessionUser(data, userId);

			for(var i in user){
				curUser[i] = user[i];
			}

			save(user);

			deferred.resolve(user);
		}).error(function(data, status, headers, config){
			deferred.reject(data);
		});

		return deferred.promise;
	}

	var logout = function(){
		this.currentUser = {id: ""};
		this.saveUser(this.currentUser);
	}
	
	var saveUser = function(user){
		if(localStorage){
			localStorage['currentUser'] = JSON.stringify(user);
		}
	}

	var loadUser = function(){
		var login = this;
		
		var deferred = $q.defer();
		
		if(localStorage){
			if(localStorage['currentUser'] != undefined){
				this.currentUser = JSON.parse(localStorage['currentUser']);
			}
		}
		
		if(this.currentUser.sessionId != undefined && this.currentUser.sessionId != ""){
			var save = this.saveUser;
			var curUser = this.currentUser;
			
			var obj = {sessionId: this.currentUser.sessionId}
			var data = new FormData();
			data.append(dataObjectName, JSON.stringify(obj));
			
			$http.post(apiURL + "session/userInfo", data,
			{
				headers: {'Content-Type': undefined},
				transformRequest: function(data){ return data; }
			}).success(function(data, status, headers, config){
				var user = new sessionUser(data, data["userId"]);
				
				for(var i in user){
					curUser[i] = user[i];
				}
				curUser["sessionId"] = obj.sessionId;
				
				deferred.resolve(curUser);
			}).error(function(data, status, headers, config){
				console.log(data)
				
				var noUser = {id:""};
				
				for(var i in curUser){
					curUser[i] = noUser[i];
				}
				save(curUser);
				
				login.loginMessage = data;
				deferred.reject(data);
			});
		}
		
		return deferred.promise;
	}
	
	var checkSession = function(returnToLoginFunc){
		var curUser = this.currentUser;
		
		if($routeParams.competencySessionId != undefined){
			this.currentUser.sessionId = $routeParams.competencySessionId;
			saveUser(this.currentUser);
			this.loadUser().then(function(result){
				saveUser(curUser);
			}, function(error){
				returnToLoginFunc()
			});
		}else if(this.currentUser.sessionId == undefined){
			returnToLoginFunc()
			return;
		}
	}
	
	return {
		guestUser: guestUser,
		currentUser: currentUser,
		
		loginMessage: loginMessage,
		
		login: login,
		logout: logout,
		
		saveUser: saveUser,
		loadUser: loadUser,
		
		checkSession: checkSession
	}
}]).

factory('alert', ['$rootScope', 'errorCode', 'session',
                    function($rootScope, errorCode, session){
	var errorMessage = "";
	var warningMessage = "";
	
	var setErrorMessage = function(error, data){
		this.warningMessage = "";
		if(error.code != undefined){
			switch(error.code){
			case errorCode.emptyParam:
				break;
			case errorCode.badValue:
				break;
			case errorCode.existence:
				break;
			case errorCode.defaultObject:
				break
			case errorCode.access:
				if(error.msg.indexOf("<read>") != -1){
					$rootScope.goBack();
				}
				break;
			case errorCode.login:
			case errorCode.expired:
				session.currentUser = {id: ""};
				$rootScope.goLogin();
				break;
			}
			
			this.errorMessage = error.msg;
		}else{
			this.errorMessage = error;
		}
	}
	
	var guestUserError = function(){
		this.errorMessage = "Guest User only has Read Access to Models, Competencies and Records";
	}
	
	var setWarningMessage = function(message){
		this.errorMessage = "";
		this.warningMessage = message;
	}
	
	var clearMessages = function(){
		this.errorMessage = "";
		this.warningMessage = "";
	}
	
	return {
		errorMessage: errorMessage,
		warningMessage: warningMessage,
		
		setErrorMessage: setErrorMessage,
		setWarningMessage: setWarningMessage,
		
		guestUserError: guestUserError,
		
		clearMessages: clearMessages,
	}
}]).

factory('appCache', ['$rootScope', '$q', '$http', 'apiURL', 'dataObjectName', 'context', 'newItem', 'session', 'modelItem', 'userItem', 'competencyItem', 'recordItem', "levelItem", 
                     function($rootScope, $q, $http, apiURL, dataObjectName, contexts, newItem, session, modelItem, userItem, competencyItem, recordItem, levelItem){

	var currentUser = {id: ""};

	var context = undefined;
	var prevContext = undefined;

	var prevLocs = [];

	var currentItemId = undefined;
	var currentModelId = undefined;

	var currentItem = {};
	var currentModel = {};

	var editedItem = {};

	var relationshipCache = {};

	var searchCache = {};
	var viewCache = {};

	var loading = false;

	var setContext = function(context){
		if(Object.keys(contexts).indexOf(context) == -1){
			console.log("cannot set context: "+context);
			return;
		}
		this.prevContext = this.context;
		this.context = context;
	};

	var pushPrevLoc = function(prevLoc){
		prevLocs.push(prevLoc);
	}

	var popPrevLoc = function(){
		return prevLocs.pop();
	}

	var setCurrentItem = function(context, itemId, modelId){
		var deferred = $q.defer();
		var currentItem = this.currentItem;
		
		if(this.currentItemId != itemId || (this.currentModelId == undefined || this.currentModelId != modelId) ){
			this.currentItem = {};
			currentItem = this.currentItem;
	
			if( context == undefined || context == "" ||itemId == undefined || itemId == ""){
				setTimeout(function(){ deferred.reject("error setting current")}, 10);
				return deferred.promise;
			}
			this.setContext(context); 
	
			this.currentItemId = itemId;
	
			
			this.currentItem.id = itemId;
	
			var current = this;
	
			this.loading = true;
	
			switch(context){
			case contexts.competency:
				competencyItem.getCompetency(itemId, modelId).
				then(function(result){
					if(current.context == contexts.competency){
						if(currentItem.id == itemId){
							for(var compId in result){
								if(compId == itemId){
									for(var i in result[compId]){
										currentItem[i] = result[compId][i];
									}
									deferred.resolve(currentItem);
								}
							}
							deferred.reject("Unknown Error");
						}else{
							deferred.reject("changed item");  
						}
					}else{
						deferred.reject("changed context");
					}
				}, function(error){
					console.log(error);
					deferred.reject(error);
				}, function(tempResult){
					if(current.context == contexts.competency){
						if(currentItem.id == itemId){
							for(var compId in tempResult){
								if(compId == itemId){
									for(var i in tempResult[compId]){
										currentItem[i] = tempResult[compId][i];
									}  
									deferred.notify(currentItem);
								}   
							}
						}else{
							deferred.reject("changed item");  
						}
					}else{
						deferred.reject("changed context");
					}
				}).finally(function(){
					if(current.context = contexts.competency){         
						current.loading = false;
					}
				});
	
				if(modelId != undefined){
					this.currentModelId = modelId;
					var curModel = this.currentModel;
	
					modelItem.getModel(modelId).
					then(function(modelData){
						for(var i in modelData){
							curModel[i] = modelData[i];   
						}
	
						curModel.getLevels();
					}, function(error){
						console.log(error);
					}, function(tempResult){
						for(var i in tempResult){
							curModel[i] = tempResult[i];
						}
					});
				}
	
				break;
			case contexts.model:
				modelItem.getModel(itemId).
				then(function(modelData){
					if(current.context = contexts.model){         
						if(currentItem.id == itemId){
							for(var i in modelData){
								currentItem[i] = modelData[i];    
							}
	
							currentItem.getLevels();
	
							deferred.resolve(currentItem)
						}else{
							deferred.reject("changed item");  
						}
					}else{
						deferred.reject("changed context");
					}
				}, function(error){
					deferred.reject(error);
				}, function(tempResult){
					if(current.context == contexts.model){
						if(currentItem.id = itemId){
							for(var i in tempResult){
								currentItem[i] = tempResult[i];
							}
							deferred.notify(tempResult);
						}else{
							deferred.reject("changed item");
						}
					}else{
						deferred.reject("changed context");
					}
				}).finally(function(){
					if(current.context = contexts.model){         
						current.loading = false;
					}
				});
				break;
			case contexts.profile:
				userItem.getUser(itemId).then(function(userData){
					if(current.context == contexts.profile){
						if(currentItem.id = itemId){
							for(var i in userData){
								currentItem[i] = userData[i];    
							}
		
							deferred.resolve(currentItem)
						}else{
							deferred.reject("changed item");
						}
					}else{
						deferred.reject("changed context");
					}
	
				}, function(error){
					deferred.reject(error);
				}, function(tempResult){
					if(current.context == contexts.profile){
						if(currentItem.id = itemId){
							for(var i in tempResult){
								currentItem[i] = tempResult[i];    
							}
							deferred.notify(tempResult);    
						}else{
							deferred.reject("changed item");
						}
	
					}else{
						deferred.reject("changed context");
					}
	
				}).finally(function(){
					if(current.context = contexts.profile){         
						current.loading = false;
					}
				});
				break;
			case contexts.record:
	
				recordItem.getRecord(itemId, modelId).then(function(recordData){
					if(current.context == contexts.record){
						if(currentItem.id == itemId){
							for(var i in recordData){
								currentItem[i] = recordData[i];
							}
	
							deferred.resolve(currentItem);
						}else{
							deferred.reject("changed item");
						}
					}else{
						deferred.reject("changed context");
					}
				}, function(error){
					console.log(error);
				}, function(tempRecord){
					if(current.context == contexts.record){
						if(currentItem.id == itemId){
							for(var i in tempRecord){
								currentItem[i] = tempRecord[i];
							}
	
							deferred.resolve(currentItem);
						}else{
							deferred.reject("changed item");
						}
					}else{
						deferred.reject("changed context");
					}
				});
			}
		}else{
			setTimeout(function(){
				deferred.resolve(currentItem);
			})
		}
			
		return deferred.promise;
	}

	var setNewItem = function(context){
		this.setContext(context); 

		this.currentItem = {};
		if(newItem[context].validations != undefined)
			newItem[context].validations = {};
		
		angular.extend(this.currentItem, newItem[context]);
		

		this.currentItemId = undefined;

		this.currentModel = {};
		this.currentModelId = undefined;
	}

	var startEdit = function(context, itemId, modelId){
		var deferred = $q.defer();

		this.editedItem = {};
		var editedItem = this.editedItem;

		this.setCurrentItem(context, itemId, modelId).
		then(function(result){
			angular.extend(editedItem, result);

			deferred.resolve(editedItem);
		}, function(error){
			console.log(error);
			deferred.reject(error);
		}, function(tempResult){
			angular.extend(editedItem, tempResult);
			deferred.notify(editedItem);
		});

		return deferred.promise;
	}

	var startCreate = function(context){
		this.editedItem = {};

		this.setNewItem(context);
		angular.extend(this.editedItem, this.currentItem);
	}

	
	var clearCaches = function(){
		
		competencyItem.competencyCache = {};
		modelItem.modelCache = {}
		userItem.userCache = {};
		levelItem.levelCache = {};
		
		this.competencyCache = competencyItem.competencyCache; 
		this.modelCache = modelItem.modelCache;
		this.userCache = userItem.userCache;
		this.levelCache = levelItem.levelCache;
		
		this.prevLocs = {};
	}

	var saveCaches = function(){

	}

	var loadCaches = function(){
	}

	return {
		context: context,
		prevContext: prevContext,

		prevLocs: prevLocs,
		pushPrevLoc: pushPrevLoc,
		popPrevLoc: popPrevLoc,

		currentUser: session.currentUser,

		currentItemId: currentItemId,
		currentModelId: currentModelId,

		currentItem: currentItem,
		currentModel: currentModel,

		editedItem: editedItem,

		competencyCache: (function(){ return competencyItem.competencyCache;})(),
		modelCache: (function(){ return modelItem.modelCache; })(),
		profileCache: (function(){ return userItem.userCache;})(),
		levelCache:(function(){ return levelItem.levelCache;})(),

		setContext: setContext,
		setCurrentItem: setCurrentItem,
		setNewItem: setNewItem,

		startEdit: startEdit,
		startCreate: startCreate,

		saveCaches: saveCaches,
		loadCaches: loadCaches,
		clearCaches: clearCaches,

		loading: loading,
	}
}]).

factory('competencyItem', ['$http', '$q', 'levelItem', 'dataObjectName', 'apiURL', 'competencyRelationships', 'modelItem', 'session',
                           function($http, $q, levelItem, dataObjectName, apiURL, competencyRelationships, modelItem, session){

	var competencyCache = {};
	var competencyCacheDefer = {};


	var competency = function(competency, competencyId, modelId){
		this.id = competencyId;
		this.modelId = modelId;
		var div = document.createElement('div'); div.innerHTML = competency[":competencyTitle"][0];
		this.title = div.firstChild.nodeValue;
		this.uri = competency.uri;

		this.descriptions = [];
		for(var i in competency[":competencyDescription"]){
			var descriptionObj = {};
			descriptionObj.text = competency[":competencyDescription"][i];
			this.descriptions.push(descriptionObj);
		}

		this.levelIds = competency[":competencyLevels"];

		this.levels = {};
		var levels = this.levels;
		for(var i in competency[":competencyLevels"]){
			var levelId = competency[":competencyLevels"][i];
			levels[levelId] = {id:""};

			levelItem.getLevel(modelId, levelId).then(function(level){
				for(var i in level){
					levels[level.id][i] = level[i];   
				}

			}, function(error){
				console.log(error);
			}, function(tempLevel){
				for(var i in tempLevel){
					levels[tempLevel.id][i] = tempLevel[i];   
				}
			});
		}

		this.relationships = {};

		var relationships = {};

		angular.extend(relationships, competency);
		delete relationships[":competencyTitle"];
		delete relationships[":competencyDescription"];
		delete relationships[":competencyLevels"];

		for(var propId in relationships){
			switch(propId){
			case competencyRelationships["Broadens To"]:
				this.relationships["Broadens To"] = relationships[propId];
			break;
			case competencyRelationships["Narrows To"]:
				this.relationships["Narrows To"] = relationships[propId];
			break;
			case competencyRelationships["Required For"]:
				this.relationships["Required For"] = relationships[propId];
			break;
			case competencyRelationships["Requires"]:
				this.relationships["Requires"] = relationships[propId];
			break;
			case competencyRelationships["Enabled By"]:
				this.relationships["Enabled By"] = relationships[propId];
			break;
			case competencyRelationships["Enables"]:
				this.relationships["Enables"] = relationships[propId];
			break;
			case competencyRelationships["Desires"]:
				this.relationships["Desires"] = relationships[propId];
			break;
			case competencyRelationships["Desired For"]:
				this.relationships["Desired For"] = relationships[propId];
			break;
			case competencyRelationships["Related To"]:
				this.relationships["Related To"] = relationships[propId];
			break;
			case competencyRelationships["Equivalent To"]:
				for(var i in relationships[propId]){
					if(relationships[propId][i] != this.id){
						if(this.relationships["Equivalent To"] == undefined){
							this.relationships["Equivalent To"] = [];
						}
						this.relationships["Equivalent To"].push(relationships[propId][i]);
					}
				}
			break;
			default:
				break;
			} 
		}
	}

	
	var getCompetency = function(competencyId, modelId){
		//TODO: Needs to use cache appropriately.
		var deferred = $q.defer();

		var cache = this.competencyCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.competencyId = competencyId;
		obj.modelId = modelId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		if(competencyCacheDefer[modelId] == undefined){
			competencyCacheDefer[modelId] = {};
		}
		
		if(competencyId instanceof Array){
			if(competencyId[0] == "")
				return deferred.promise;
			
			if(this.competencyCache[modelId] != undefined){
				var cachedAll = true;
				var result = {};
				for(var i in competencyId){
					var id = competencyId[i];
					if(this.competencyCache[modelId][id] != undefined && this.competencyCache[modelId][id].title != undefined){
						result[id] = this.competencyCache[modelId][id]
					}else{
						cachedAll = false;
						//this.competencyCache[modelId][id] = {};
					}
				}
				
				if(cachedAll){
					setTimeout(function(){
						deferred.resolve(result);  
					}, 10);
					return deferred.promise;
				}
			}else{
				this.competencyCache[modelId] = {};
				for(var i in competencyId){
					var id = competencyId[i];
					this.competencyCache[modelId][id] = {};
				}
			}
		}else{
			if(this.competencyCache[modelId] == undefined){
				this.competencyCache[modelId] = {};
				this.competencyCache[modelId][competencyId] = {};
				competencyCacheDefer[modelId] = {};
				competencyCacheDefer[modelId][competencyId] = [];
			}else if(this.competencyCache[modelId][competencyId] == undefined){
				this.competencyCache[modelId][competencyId] = {};
				competencyCacheDefer[modelId][competencyId] = [];
			}else if(competencyCacheDefer[modelId][competencyId] != undefined){
				competencyCacheDefer[modelId][competencyId].push(deferred);
				return deferred.promise;
			}else if(this.competencyCache[modelId][competencyId] != undefined){
				var result = {};
				result[competencyId] = this.competencyCache[modelId][competencyId];

				setTimeout(function(){
					deferred.resolve(result);  
				}, 10);
				return deferred.promise;
			}
		}
		

		var competencyItem = this;
		
		$http.post(apiURL + "read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var compId in data){
						var comp = {};

						if(data[compId].uri != undefined){
							var uri = data[compId].uri;

							var hashLoc = uri.indexOf("#");
							var slashLoc = uri.indexOf("model-");

							var mId = uri.substr(slashLoc, hashLoc-slashLoc);

							comp = new competency(data[compId], compId, mId);  
						}else{
							comp = new competency(data[compId], compId, modelId);  
						}

						if(mId != modelId){
							competencyItem.competencyCache[modelId][comp.id] = undefined;
						}
						
						if(competencyItem.competencyCache[mId] == undefined){
							competencyItem.competencyCache[mId] = {};
						}
						if(competencyItem.competencyCache[mId][comp.id] == undefined){
							competencyItem.competencyCache[mId][comp.id] = {};
						}
						
						for(var i in comp){
							competencyItem.competencyCache[mId][comp.id][i] = comp[i]; 
						}

						if (!(competencyId instanceof Array) && competencyCacheDefer[modelId][competencyId] != undefined)
							for (var i in competencyCacheDefer[modelId][competencyId])
								competencyCacheDefer[modelId][competencyId][i].resolve(result);
						competencyCacheDefer[modelId][competencyId] = undefined;
						result[compId] = comp;
					}

					deferred.resolve(result);

				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var getAllCompetencies = function(modelId){
		var deferred = $q.defer();
		
		var cache = this.competencyCache;
		
		var obj = {sessionId: session.currentUser.sessionId};
		if(modelId != undefined && modelId[0] != "all"){
			obj.modelId = modelId;
		}
		
		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var result = {};

		$http.post(apiURL + "all", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}
		).success(function(data, status, headers, config){

			var result = {};
			
			for(var modelId in data){
				for(var compId in data[modelId]){
					result[compId] = new competency(data[modelId][compId], compId, modelId);

					if(cache[modelId] == undefined){
						cache[modelId] = {};
					}

					cache[modelId][compId] = result[compId];
				}
			}

			deferred.resolve(result);
		}).error(function(data, status, headers, config){
			deferred.reject(data);
		});
		
		var tempResult = {};
		
		if(modelId instanceof Array){
			if(modelId[0] == "all"){
				modelId = Object.keys(this.competencyCache);
			}
			
			for(var idx in modelId){
				for(var id in this.competencyCache[modelId[idx]]){
					if(this.competencyCache[modelId[idx]][id] != undefined)
						tempResult[id] = this.competencyCache[modelId[idx]][id];
				}
			}
		}else{
			for(var id in this.competencyCache[modelId]){
				if(this.competencyCache[modelId][id] != undefined)
					tempResult[id] = this.competencyCache[modelId][id];
			}
		}
		
		setTimeout(function(){
			deferred.notify(tempResult);
		}, 10)
		
		return deferred.promise;
	}
	
	var prevQuery = "";
	var prevResult = {};
	
	var searchCompetencyPromise = function(query, modelId){
		var cache = this.competencyCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;
		if(modelId != undefined && modelId[0] != "all"){
			obj.modelId = modelId;
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		$http.post(apiURL + "query/searchCompetencies", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}
		).success(function(data, status, headers, config){
			var result = [];
			for(var modelId in data){
				for(var compId in data[modelId]){
					var newComp = new competency(data[modelId][compId], compId, modelId);

					if(cache[modelId] == undefined){
						cache[modelId] = {};
					}

					cache[modelId][newComp.id] = newComp;
					result.push(newComp);
				}
			}

			prevResult = result;
			prevQuery = query;
			
			deferred.resolve(result);

		}).error(function(data, status, headers, config){
			deferred.reject(data);
		});

		if(query == prevQuery){
			setTimeout(function(){
				deferred.notify(prevResult);
			}, 10)
		}
		
		return deferred.promise;
	}

	var searchCompetency = function(query, modelId, callbackObj){
		var cache = this.competencyCache;
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;
		if(modelId != undefined && modelId[0] != "all"){
			obj.modelId = modelId;
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var result = {};

		$http.post(apiURL + "query/searchCompetencies", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}
		).success(function(data, status, headers, config){

			for(var modelId in data){
				for(var compId in data[modelId]){
					result[compId] = new competency(data[modelId][compId], compId, modelId);

					if(cache[modelId] == undefined){
						cache[modelId] = {};
					}

					cache[modelId][compId] = result[compId];
				}
			}

			if(callbackObj.setResults){
				callbackObj.setResults(result);
			}

			//callbackObj.setResults(result, 'competency');
		}).error(function(data, status, headers, config){
			alert("error searching competencies")
		});

		return result;
	}


	var createCompetency = function(newData){
		var cache = this.competencyCache;
		
		var deferred = $q.defer();

		var importedDeferrer = $q.defer();

		var levelDeferrer = $q.defer();
		var newLevelCount = 0;
		var newLevelsMade = 0;
		
		var obj = {sessionId: session.currentUser.sessionId};
		for(var prop in newData){
			switch(prop){
			case 'title':
				obj.competencyTitle = newData[prop];
				break;
			case 'descriptions':
				obj.competencyDescription = [];
				for(var i in newData[prop]){
					obj.competencyDescription.push(newData[prop][i].text);  
				}

				break;
			case 'levels':
				obj.competencyLevels = [];  
				for(var levelId in newData[prop]){
					if(levelId.indexOf(":") == 0){
						obj.competencyLevels.push(levelId);
					}else{
						newLevelCount++;
						
						levelItem.createLevel(newData.modelId, newData[prop][levelId]).then(function(levelObj){
							obj.competencyLevels.push(levelObj.id);
							
							newLevelsMade++;
							
							if(newLevelsMade == newLevelCount){
								levelDeferrer.resolve();
							}
						}, function(error){
							levelDeferrer.reject();
						})
					}
				}
				break;
			case 'relationships':
				if(obj.competencyRelationships == undefined){
					obj.competencyRelationships = {};
				}
				var imports = 0;
				var imported = 0;

				for(var relId in newData[prop]){
					obj.competencyRelationships[competencyRelationships[relId]] = [];
					for(var idx in newData[prop][relId]){
						obj.competencyRelationships[competencyRelationships[relId]].push(newData[prop][relId][idx].id);

						if(newData[prop][relId][idx].modelId != newData.modelId && modelItem.modelCache[newData.modelId].imports.indexOf(newData[prop][relId][idx].modelId) == -1){
							imports++;
							modelItem.addImport(newData.modelId, newData[prop][relId][idx].modelId).then(function(data){
								imported++;
								if(imported == imports){
									importedDeferrer.resolve();
								}
							}, function(error){
								importedDeferrer.reject(error);
							});
						}
					}
				}
				break;
			default:
				console.log(prop);
			}
		}
		
		var modelId = newData.modelId;
		obj.modelId = modelId;

		var data = new FormData();

		if(obj.competencyRelationships == undefined || imports == imported){
			setTimeout(function(){
				importedDeferrer.resolve();
			}, 10)
		}
		
		if(newLevelCount == 0){
			setTimeout(function(){
				levelDeferrer.resolve();
			})
		}

		var importingDone = false;
		var levelsCreated = false;
		var bothDeferrer = $q.defer();
		
		importedDeferrer.promise.then(function(){
			importingDone = true;
			
			if(levelsCreated){
				bothDeferrer.resolve();
			}
		});
		levelDeferrer.promise.then(function(){
			levelsCreated = true;
			
			if(importingDone){
				bothDeferrer.resolve();
			}
		});
		
		var competencyItem = this;
		
		bothDeferrer.promise.then(function(){
			data.append(dataObjectName, JSON.stringify(obj));
			$http.post(apiURL + "create", data,
					{
				headers: {'Content-Type': undefined},
				transformRequest: function(data){ return data; }
					}
			).success(function(data, status, headers, config){
				for(var i in competencyItem.competencyCache){
					competencyItem.competencyCache[i] = {};
				}
				for(var i in cache){
					cache[i] = {};
				}
				for(var i in competencyCache){
					competencyCache[i] = {};
				}
				
				var result = {};

				// TODO: INVALIDATE CACHE OR UPDATE THE RELATIONSHIPS
				
				for(var id in data){
					var newComp = new competency(data[id], id, modelId);

					result[id] = newComp;
				}

				for(var id in result){
					if(cache[result[id].modelId] == undefined){
						cache[result[id].modelId] = {};
						cache[result[id].modelId][result[id].id] = {}
					}else if(cache[result[id].modelId][result[id].id] == undefined){
						cache[result[id].modelId][result[id].id] = {}
					}
					
					for(var i in result[id]){
						cache[result[id].modelId][result[id].id][i] = result[id][i];
					}
				}
				
				deferred.resolve(result);

			}).error(function(data, status, headers, config){
				deferred.reject(data);
			});
		}, function(error){
			deferred.reject(error);
		});

		return deferred.promise;
	}

	var editCompetency = function(competencyId, newData){
		//TODO: Needs to invalidate cache.

		var cache = this.competencyCache;
		
		var deferred = $q.defer(); 
		if(competencyId == undefined || newData.modelId == undefined){
			deferred.reject("CompetencyId or ModelId not specified");
			return;
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.competencyId = competencyId;
		obj.modelId = newData.modelId;

		var importDeferrer = $q.defer();
		var imports = 0;
		var imported = 0;

		for(var prop in newData){
			switch(prop){
			case 'title':
				obj.competencyTitle = newData[prop];
				break;
			case 'descriptions':
				obj.competencyDescription = [];
				for(var i in newData[prop]){
					obj.competencyDescription.push(newData[prop][i].text);  
				}
				break;
			case 'levels':
				obj.competencyLevels = [];  
				for(var levelId in newData[prop]){
					obj.competencyLevels.push(levelId);
				}
				break;
			case 'relationships':

				if(obj.competencyRelationships == undefined){
					obj.competencyRelationships = {};
				}

				

				for(var relId in newData[prop]){
					obj.competencyRelationships[competencyRelationships[relId]] = [];
					for(var idx in newData[prop][relId]){
						if(newData[prop][relId][idx] instanceof Object){
							obj.competencyRelationships[competencyRelationships[relId]].push(newData[prop][relId][idx].id);

							if(newData[prop][relId][idx].modelId != newData.modelId){
								imports++;
								modelItem.addImport(newData.modelId, newData[prop][relId][idx].modelId).then(function(data){
									imported++;
									if(imported == imports){
										importDeferrer.resolve();
									}
								}, function(error){
									if(error.code == "default"){
										importDeferrer.reject("Cannot Add Relationship to Competency Outside of Default Model");
									}
									importDeferrer.reject(error)
								});
							}
						}else if(newData[prop][relId][idx].length != 0){
							obj.competencyRelationships[competencyRelationships[relId]].push(newData[prop][relId][idx]);
						}
						
					}
				}
				break;
			default:
				console.log(prop);
			break;
			}
		}

		for(var relId in competencyRelationships){
			if(obj.competencyRelationships[competencyRelationships[relId]] == undefined){
				obj.competencyRelationships[competencyRelationships[relId]] = [];
			}
		}

		
		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		if(obj.competencyRelationships == undefined || imports == imported){
			setTimeout(function(){
				importDeferrer.resolve();
			}, 10)
		}

		var competencyItem = this;
		importDeferrer.promise.then(function(){
			$http.post(apiURL + "update", data,
					{
				headers: {'Content-Type': undefined},
				transformRequest: function(data){ return data; }
					}
			).success(function(data, status, headers, config){
				competencyItem.competencyCache = {};
				cache = {};
				competencyCache = {};
				
				var result = new competency(data, competencyId, newData.modelId);

				cache[result.modelId] = {};
				cache[result.modelId][result.id] = {};
				// TODO: INVALIDATE CACHE OR UPDATE THE RELATIONSHIPS
				
				for(var i in result){
					cache[result.modelId][result.id][i] = result[i];
				}
				
				for(var i in cache[result.modelId][result.id]){
					if(result[i] == undefined){
						delete cache[result.modelId][result.id][i];
					}
				}
				
				deferred.resolve(result);

			}).error(function(data, status, headers, config){
				deferred.reject(data);
			});
		}, function(error){
			deferred.reject(error);
		})


		return deferred.promise;
	}

	return {
		competencyCache: competencyCache,

		getCompetency: getCompetency,
		getAllCompetencies: getAllCompetencies,
		
		makeLocalCompetency: function(data, competencyId, modelId){ 
			return new competency(data, competencyId, modelId);
		},

		searchCompetencyPromise:searchCompetencyPromise,
		searchCompetency:searchCompetency,

		createCompetency: createCompetency,
		editCompetency: editCompetency,
	}

}]).

factory('modelItem', ['$http', '$q', 'levelItem', 'dataObjectName', 'apiURL', 'session', 
                      function($http, $q, levelItem, dataObjectName, apiURL, session){

	var modelCache = {};
	var modelLevelCacheDefer = {};


	var model = function(model){
		this.id = model.ontologyId;
		this.name = model.name;

		this.description = model.description;
		this.uri = model.uri;
		this.dateCreated = model.dateCreated;
		if(model.imports instanceof Array){
			this.imports = model.imports;
		}else{
			this.imports = JSON.parse(model.imports);
		}
		

		var levels = {};
		this.levels = levels;
		this.accessControl = model.accessControl;

		this.levelIds = model.defaultLevels;
		for(var idx in model.defaultLevels){
			this.levels[model.defaultLevels[idx]] = {}
		}

		this.getLevels = function(){  
			for(var idx in model.defaultLevels){
				if(levelItem.levelCache[this.id] == undefined || levelItem.levelCache[this.id][model.defaultLevels[idx]] == undefined){

					levelItem.getLevel(this.id, model.defaultLevels[idx]).
					then(function(level){
						for(var i in level){
							levels[level.id][i] = level[i];   
						}
					}, function(error){
						console.log(error);
					}, function(tempLevel){
						for(var i in tempLevel){
							levels[level.id][i] = tempLevel[i];   
						}
					});

					if(levelItem.levelCache[this.id] == undefined){
						levelItem.levelCache[this.id] = {};
					}
					levelItem.levelCache[this.id][model.defaultLevels[idx]] = this.levels[model.defaultLevels[idx]];
				}else{
					var level = levelItem.levelCache[this.id][model.defaultLevels[idx]];
					for(var i in level){
						levels[model.defaultLevels[idx]][i] = level[i];   
					}
				}

			} 
		}

	}

	var getModel = function(modelId){
		//TODO: Needs to use cache appropriately.

		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.modelId = modelId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		if(this.modelCache[modelId] == undefined){
			this.modelCache[modelId] = {name:"", id:"", levels:{}, allLevels: {}};
		}else{
			setTimeout(function(){
				deferred.notify(cache[modelId]);
			}, 10);

		}

		$http.post(apiURL + "model/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = new model(data);

					for(var i in result){
						cache[modelId][i] = result[i]
					}

					deferred.resolve(cache[modelId]);
				}).error(function(data, status, headers, config){
					delete(cache[modelId])
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var searchModels = function(query, callbackObj){
		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "query/searchModels", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var idx in data){
						result[data[idx].ontologyId] = new model(data[idx])
						cache[data[idx].ontologyId] = result[data[idx].ontologyId];

					}

					callbackObj.setResults(result, 'model');
				}).error(function(data, status, headers, config){
					alert('error searching models');
				})
	}
	
	var prevResult = {};
	var prevQuery = "";
	
	var searchModelsPromise = function(query){
		var deferred = $q.defer();
		var cache = this.modelCache;
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "query/searchModels", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var idx in data){
						result[data[idx].ontologyId] = new model(data[idx])
						cache[data[idx].ontologyId] = result[data[idx].ontologyId];

					}

					prevResult = result;
					prevQuery = query;
					
					deferred.resolve(result);
				}).error(function(data, status, headers, config){
					alert('error searching models');
				})
				
		if(prevQuery == query){
			setTimeout(function(){
				deferred.notify(prevResult);
			}, 10)
		}
				
		return deferred.promise;
	}

	var getAllLevels = function(modelId){
		//TODO: Needs to use cache appropriately.

		if(modelId == undefined || modelId == "N/A"){
			modelId = "model-default";
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.modelId = modelId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		var cache = this.modelCache;

		if(this.modelCache[modelId] == undefined){
			this.modelCache[modelId] = {};
		}
		
		if(	this.modelCache[modelId] != undefined &&
			this.modelCache[modelId].allLevels != undefined && 
			Object.keys(this.modelCache[modelId].allLevels).length != 0 &&
			modelLevelCacheDefer[modelId] == undefined)
		{
			setTimeout(function(){
				deferred.resolve(cache[modelId].allLevels);  
			}, 10);
			return deferred.promise;
		}
		else if (this.modelCache[modelId].allLevels == undefined)
		{
			this.modelCache[modelId].allLevels = {};
			modelLevelCacheDefer[modelId] = [];
		}
		else if (modelLevelCacheDefer[modelId] == undefined){
			modelLevelCacheDefer[modelId] = [];
		}
		else
		{
			modelLevelCacheDefer[modelId].push(deferred);
			return deferred.promise;
		}

		$http.post(apiURL + "level/all", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var idx in data){
						result[idx] = levelItem.makeLocalLevel(data[idx], idx);

						cache[modelId].allLevels[idx] = {};
						for(var i in result[idx]){
							cache[modelId].allLevels[idx][i] = result[idx][i];  
						}

						if (modelLevelCacheDefer[modelId] != undefined)
							for (var i in modelLevelCacheDefer[modelId])
							{
								modelLevelCacheDefer[modelId][i].resolve(result);
							}
						modelLevelCacheDefer[modelId] = undefined;
					}
					deferred.resolve(result);
				}).error(function(data, status, headers, config){

					deferred.reject(data);
				})

				return deferred.promise;
	}

	var editModel = function(modelData){
		//TODO: Needs to invalidate cache.

		var basicsDone = false;
		var accessDone = false;
		
		if(modelData.id == undefined){
			alert("Cannot modify model without modelId");
		}
		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.modelId = modelData.id;


		for(var i in modelData){
			switch(i){
			case "name":
				obj.modelName = modelData[i];
				break;
			case "description":
				obj.modelDescription = modelData[i];
				break;
			case "levels":
				obj.modelDefaultLevels = Object.keys(modelData[i]);
				break;
			default:
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		$http.post(apiURL + "model/update", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var modelItem = new model(data);

					if(cache[modelItem.id] == undefined){
						cache[modelItem.id] = {};
					}

					for(var i in modelItem){
						cache[modelItem.id][i] = modelItem[i];
					}

					basicsDone = true;
					if(basicsDone && accessDone){
						deferred.resolve(cache[modelItem.id]);
					}
					
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.modelId = modelData.id;
		
		for(var i in modelData.accessControl){
			switch(i){
			case "type":
				obj.modelPrivacy = modelData.accessControl.type;
				break
			case "admin":
				obj.modelAdmin = modelData.accessControl.admin;
				break;
			case "user":
				obj.modelUser = modelData.accessControl.user;
				break;
			case "agent":
				obj.modelAgent = modelData.accessControl.agent;
				break;
			default:
				break;
			}
		}
		
		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "model/updateAccessControl", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var modelItem = new model(data);

					if(cache[modelItem.id] == undefined){
						cache[modelItem.id] = {};
					}

					for(var i in modelItem){
						cache[modelItem.id][i] = modelItem[i];
					}

					accessDone = true;
					if(basicsDone && accessDone){
						deferred.resolve(cache[modelItem.id]);
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});
		
		return deferred.promise;
	}

	var createModel = function(modelData){
		if(modelData.name == undefined){
			alert("Cannot create an un-named model");
		}
		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};

		for(var i in modelData){
			switch(i){
			case "name":
				obj.modelName = modelData[i];
				break;
			case "description":
				obj.modelDescription = modelData[i];
				break;
			default:
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		$http.post(apiURL + "model/create", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var modelItem = new model(data);

					cache[modelItem.id] = modelItem;

					deferred.resolve(cache[modelItem.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;

	}

	var getAllModels = function(){
		//TODO: Needs to use cache appropriately.


		var deferred = $q.defer();

		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));
		
		$http.post(apiURL + "model/all", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){

					for(var modelId in data){
						if(cache[modelId] == undefined){
							cache[modelId] = {};
						}
						var modelObj = new model(data[modelId]);

						for(var i in modelObj){
							cache[modelId][i] = modelObj[i]
						}
						deferred.notify(cache);
					}
					deferred.resolve(cache);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				})

				deferred.notify(cache);

		return deferred.promise;
	}

	var addImport = function(modelId, importId){
		//TODO: Needs to use cache appropriately.

		if(modelId == undefined || importId == undefined){
			alert("error importing model, modelId or importId not defined");
		}
		var cache = this.modelCache;

		var obj = {sessionId: session.currentUser.sessionId};

		obj.modelId=modelId
		obj.importId=importId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		if(cache[modelId].imports.indexOf(importId) != -1){
			setTimeout(function(){
				deferred.resolve(cache[modelId]);
			}, 10)

			return deferred.promise;
		}

		$http.post(apiURL + "model/addImport", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var modelItem = new model(data);

					cache[modelItem.id] = modelItem;

					deferred.resolve(cache[modelItem.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	return {
		modelCache: modelCache,

		getModel: getModel,
		makeLocalModel: function(data){return new model(data, this);},

		searchModels: searchModels,
		searchModelsPromise: searchModelsPromise,

		getAllLevels: getAllLevels,
		getAllModels: getAllModels,

		editModel: editModel,
		createModel: createModel,

		addImport:addImport,
	}

}]).

factory('userItem', ['$http', '$q', 'dataObjectName', 'apiURL', 'modelItem', 'competencyItem', 'recordItem', 'session', 'alert',
                     function($http, $q, dataObjectName, apiURL, modelItem, competencyItem, recordItem, session, alert){

	var userCache = {};

	var user = function(user, userId){
		this.id = userId;

		this.modelId = user.modelId ? user.modelId : user.modelid;  
		this.dateCreated = user.dateCreated ? user.dateCreated : user.datecreated;

		this.firstName = user.firstName ? user.firstName : user.firstname;
		this.lastName = user.lastName ? user.lastName : user.lastname;

		this.email = user.email;

		this.records = {};
		this.competencies = {};
	}

	var getUser = function(userId){
		//TODO: Needs to use cache appropriately.


		var userRead = false;
		var recordRead = false;
		var compRead = false;

		var cache = this.userCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		if(this.userCache[userId] == undefined){
			this.userCache[userId] = {};
			this.userCache[userId].records = {};
		}else{
			setTimeout(function(){
				deferred.notify(cache[userId]);
			});
		}

		$http.post(apiURL + "user/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = new user(data, userId);

					for(var i in result){
						cache[userId][i] = result[i];
					}

					if(recordRead && compRead){
						deferred.resolve(cache[userId]);
					}else{
						userRead = true;
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		$http.post(apiURL + "record/all", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var id in data){
						if(data[id] instanceof Object && data[id].code == undefined){
							cache[userId].records[id] = recordItem.makeLocalRecord(data[id], id, userId);
						}	
					}

					if(userRead && compRead){
						deferred.resolve(cache[userId]);
					}else{
						recordRead = true;
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		$http.post(apiURL + "record/readCompetencies", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var modelId in data){
						if(data[modelId] instanceof Object && data[modelId].code == undefined){
							for(var compId in data[modelId]){
								if(competencyItem.competencyCache[modelId] == undefined){
									competencyItem.competencyCache[modelId] = {};
								}
								if(competencyItem.competencyCache[modelId][compId] == undefined){
									competencyItem.competencyCache[modelId][compId] = {};
								}

								competencyItem.competencyCache[modelId][compId] = competencyItem.makeLocalCompetency(data[modelId][compId], compId, modelId);

								cache[userId].competencies[compId] = competencyItem.competencyCache[modelId][compId];
							}
						}else{
							alert.setWarningMessage("This user may have additional records that are not visible to you");
						}
					}

					if(recordRead && userRead){
						deferred.resolve(cache[userId]);
					}else{
						compRead = true;
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}
	
	var getAllUsers = function(){
		var cache = this.userCache;

		var deferred = $q.defer();
		
		var obj = {sessionId: session.currentUser.sessionId};
		
		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "user/all", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var userId in data){
						result[userId] = new user(data[userId], userId);

						cache[userId] = result[userId];
					}

					deferred.resolve(result);
				}).error(function(data, status, headers, config){
					alert(data);
				});
		
		return deferred.promise;
	}

	var searchUsers = function(query, callbackObj){
		var cache = this.userCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "query/searchUsers", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = {};

					for(var userId in data){
						result[userId] = new user(data[userId], userId);

						cache[userId] = result[userId];
					}

					callbackObj.setResults(result, 'profile');
				}).error(function(data, status, headers, config){
					alert('error searching users');
				});
	}
	
	var prevQuery;
	var prevResult;
	
	var searchUsersPromise = function(query){
		var cache = this.userCache;
		
		var deferred = $q.defer();
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.query = query;
		
		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));
		
		$http.post(apiURL + "query/searchUsers", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var result = [];

					for(var userId in data){
						result.push(new user(data[userId], userId));

						cache[userId] = result[userId];
					}

					prevQuery = query;
					prevResult = result;
					
					deferred.resolve(result);
				}).error(function(data, status, headers, config){
					deferred.reject(error)
				});
		
		if(prevQuery == query){
			setTimeout(function(){
				deferred.notify(prevResult);
			}, 10)
		}
		
		return deferred.promise;
	}

	var createUser = function(userData){
		var cache = this.userCache;
		var deferred = $q.defer();

		var obj = {sessionId: session.currentUser.sessionId};

		for(var i in userData){
			switch(i){
			case "id":
				obj.userId = userData[i];
				break;
			case "password":
				obj.password = userData[i];
				break;
			case "firstName":
				obj.firstName = userData[i];
				break;
			case "lastName":
				obj.lastName = userData[i];
				break;
			case "email":
				obj.email = userData[i];
				break;
			default:
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "user/create", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var userItem = new user(data, data.userId);

					cache[userItem.id] = userItem;

					deferred.resolve(cache[userItem.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});      

		return deferred.promise;
	}

	var editUser = function(userData){
		//TODO: Needs to invalidate cache.


		var cache = this.userCache;
		var deferred = $q.defer();

		var obj = {sessionId: session.currentUser.sessionId};

		for(var i in userData){
			switch(i){
			case "id":
				obj.userId = userData[i];
				break;
			case "firstName":
				obj.firstName = userData[i];
				break;
			case "lastName":
				obj.lastName = userData[i];
				break;
			case "email":
				obj.email = userData[i];
				break;
			default:
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "user/update", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var userItem = new user(data, data.userId);

					cache[userItem.id] = userItem;

					deferred.resolve(cache[userItem.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});      

		return deferred.promise;
	}
	
	var editPassword = function(userData){
		var deferred = $q.defer();

		var obj = {sessionId: session.currentUser.sessionId};

		for(var i in userData){
			switch(i){
			case "id":
				obj.userId = userData[i];
				break;
			case "password":
				obj.password = userData[i];
			default:
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "user/resetPassword", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					deferred.resolve();
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});      

		return deferred.promise;
	}

	return {
		userCache: userCache,

		getUser: getUser,
		getAllUsers: getAllUsers,
		makeLocalUser: function(data, userId){ return new user(data, userId); },

		searchUsers:searchUsers,
		searchUsersPromise:searchUsersPromise,

		createUser: createUser,
		editUser: editUser,
		
		editPassword: editPassword,
	}

}]).

factory('levelItem', ['$http', '$q', 'dataObjectName', 'apiURL', 'session',
                      function($http, $q, dataObjectName, apiURL, session){

	var levelCache = {};
	var levelCacheDefer = {};

	var level =  function(level, levelId){
		this.id = levelId;

		this.name = level[":competencyLevelName"][0];

		this.rank = level[":competencyLevelRank"][0];

		this.description = level[":competencyLevelDescription"][0];
	}

	var getLevel = function(modelId, levelId){
		var cache = this.levelCache;

		var deferred = $q.defer();

		if(this.levelCache[levelId] == undefined){
			this.levelCache[levelId] = {};
			levelCacheDefer[levelId] = [];
		}else{
			if (levelCacheDefer[levelId] != undefined)
			{
				levelCacheDefer[levelId].push(deferred);
				return deferred.promise;
			}
			else
			{
				setTimeout(function(){
					deferred.resolve(cache[levelId]);
				});
				return deferred.promise;
			}
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.levelId = levelId;
		obj.modelId = modelId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "level/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var idx in data){
						var levelItem = new level(data[idx], levelId);
						for(var i in levelItem){
							cache[levelId][i] = levelItem[i];
						}
						if (levelCacheDefer[levelId] != undefined)
							for (var i in levelCacheDefer[levelId])
								levelCacheDefer[levelId][i].resolve(cache[levelId]);
						levelCacheDefer[levelId] = undefined;
						deferred.resolve(cache[levelId]);
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var createLevel = function(modelId, levelData){
		if(modelId == undefined){
			alert("Cannot create Level, modelId undefined");
			return;
		}
		var cache = this.levelCache;

		var obj = {sessionId: session.currentUser.sessionId};
		obj.modelId = modelId;

		for(var i in levelData){
			switch(i){
			case "name":
				obj.levelName = levelData[i];
				break;
			case "rank":
				obj.levelRank = levelData[i];
				break;
			case "description":
				obj.levelDescription = levelData[i];
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		var deferred = $q.defer();

		$http.post(apiURL + "level/create", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var levelId in data){
						var levelItem = new level(data[levelId], levelId);

						cache[levelId] = {};
						for(var i in levelItem){
							cache[levelId][i] = levelItem[i];
						}

						deferred.resolve(cache[levelId]);
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var getCompetencyLevels = function(modelId, competencyId){

	}

	return {
		levelCache: levelCache,

		getLevel: getLevel,
		makeLocalLevel: function(data, levelId){ return new level(data, levelId); },

		createLevel: createLevel
	};

}]).

factory('recordItem', ['$http', '$q', 'dataObjectName', 'apiURL', 'levelItem', 'competencyItem', 'modelItem', 'validationItem', 'session',
                       function($http, $q, dataObjectName, apiURL, levelItem, competencyItem, modelItem, validationItem, session){

	var recordCache = {};

	var record =  function(data, recordId, userId){
		this.id = recordId;
		this.userId = userId;

		var competencyModelId = data[":recordCompetencyModel"] == undefined ? 'model-default' : data[":recordCompetencyModel"][0];
		this.competencyModelId = competencyModelId;
		
		var competencyId = data[':recordCompetency'][0];
		this.competencyId = competencyId;

		//competencyItem.getCompetency(this.competencyId, this.competencyModelId);
		//modelItem.getAllLevels(this.competencyModelId);

		this.levelId = data[':recordLevel'][0];

		var validationIds = data[":recordValidation"] ? data[":recordValidation"] : [];
		this.validationIds = validationIds;

		var validations = {};
		this.validations = validations;

		this.getCompetency = function(){
			var deferred = $q.defer();
			competencyItem.getCompetency(competencyId, competencyModelId).then(function(competency){
				deferred.resolve(competency)
			}, function(error){
				alert.setErrorMessage(error);
			});
			
			return deferred.promise;
		}
		
		this.getValidations = function(){
			for(var i in validationIds){
				validations[validationIds[i]] = {};
				validationItem.getValidation(validationIds[i], recordId, userId).then(
						function(validation){
							for(var i in validation){
								validations[validation.id][i] = validation[i];
							}
						}, function(error){
							console.log(error);
						}, function(tempValidation){
							for(var i in tempValidation){
								validations[tempValidation.id][i] = tempValidation[i];
							}
						});
			}
		}
		

		this.confidence = data[":recordConfidence"] == undefined ? "N/A" : data[':recordConfidence'][0];

		this.lastUpdated = data[":recordUpdated"][0];
	}

	var getRecord = function(recordId, userId){
		//TODO: Needs to use cache appropriately.
		var cache = this.recordCache;

		var deferred = $q.defer();

		if(this.recordCache[userId] == undefined){
			this.recordCache[userId] = {};  
		}

		if(this.recordCache[userId][recordId] == undefined){
			this.recordCache[userId][recordId] = {};
		}else{
			setTimeout(function(){
				deferred.notify(cache[userId][recordId]);
			})
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.recordId = recordId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var idx in data){
						var recordItem = new record(data[idx], recordId, userId);

						for(var i in recordItem){
							cache[userId][recordId][i] = recordItem[i];
						}

						deferred.resolve(cache[userId][recordId]);
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}


	var createRecord = function(userId, recordData){
		var cache = this.recordCache;

		var deferred = $q.defer();

		if(this.recordCache[userId] == undefined){
			this.recordCache[userId] = {};  
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;

		var validations = undefined;
		for(var i in recordData){
			switch(i){
			case "competencyId":
				obj.competencyId = recordData[i];
				break;
			case "competencyModelId":
				obj.modelId = recordData[i];
				break;
			case "levelId":
				obj.levelId = recordData[i];
				break;
			case "validations":
				validations = recordData[i];
				break;
			default:
				console.log("unused in record creation: "+i);
			console.log(recordData[i]);
			console.log("");

			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/create", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var id in data){
						var recordItem = new record(data[id], id, userId);

						if(cache[userId][id] == undefined){
							cache[userId][id] = {};
						}

						for(var i in recordItem){
							cache[userId][id][i] = recordItem[i];
						}

						if(Object.keys(validations).length == 0){
							deferred.resolve(cache[userId][id]);
						}

						var validationCount = Object.keys(validations).length;
						var validationNum = 0;
						for(var idx in validations){
							var newVal = validations[idx];
							validationItem.createValidation(userId, recordItem.id, newVal.agentId, newVal.confidence, newVal.evidenceIds).then(function(newValidation){
								if(cache[userId][id].validationIds == undefined){
									cache[userId][id].validationIds = [];
								}
								if(cache[userId][id].validations == undefined){
									cache[userId][id].validations = {};
								}

								cache[userId][id].validationIds.push(newValidation.id);
								cache[userId][id].validations[newValidation.id] = newValidation;

								validationNum++;

								if(validationCount >= validationNum){
									deferred.resolve(cache[userId][id]);      
								}
							}, function(error){
								console.log(error);
							})
						}


					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var editRecord = function(userId, recordId, recordData){
		//TODO: Needs to invalidate cache.

		var deferred = $q.defer();

		setTimeout(function(){
			deferred.resolve();
		})

		return deferred.promise;
	}

	return {
		recordCache: recordCache,

		makeLocalRecord: function(data, recordId, userId){ 
			if(recordCache[userId] == undefined){
				recordCache[userId] = {};
			}
			var newRec = new record(data, recordId, userId);

			recordCache[userId][recordId] = newRec;

			return recordCache[userId][recordId];
		},

		getRecord: getRecord,

		createRecord: createRecord,
		editRecord: editRecord,
	};

}]).

factory('validationItem', ['$http', '$q', 'dataObjectName', 'apiURL', 'session',
                           function($http, $q, dataObjectName, apiURL, session){

	var validationCache = {};
	var validationCacheDefer = {};
	var evidenceCache = {};

	var validation =  function(data, validationId, recordId, userId){
		this.id = validationId;
		this.userId = userId;
		this.recordId = recordId

		this.agentId = data[":validationAgent"][0];

		this.confidence = data[":validationConfidence"][0];
		this.date = data[":validationDate"][0];

		this.evidenceIds = data[":validationEvidence"];
		if(this.evidenceIds == undefined || this.evidenceIds[0] == ""){
			this.evidenceIds = [];
		}

		this.evidences = {};
	}

	var evidence = function(data, userId){
		this.id = data.evidenceId;
		this.userId = userId;

		this.type = data.type;
		this.date = data.date;
		this.result = data.result;
		this.description = data.description;

		if(data.string != undefined){
			this.value = data.string;
			this.valueType = "String";
		}else if(data.uri != undefined){
			this.value = data.uri;
			this.valueType = "URI";
		}else if(data.fileId != undefined){
			this.value = data.fileId;
			this.valueType = "Filename";
		}else{
			console.log("error: creating evidence, no value or valueType");
		}
	}


	var getValidation = function(validationId, recordId, userId){
		//TODO: Needs to use cache appropriately.

		var cache = this.validationCache;

		var deferred = $q.defer();

		if(this.validationCache[userId] == undefined){
			this.validationCache[userId] = {};
			validationCacheDefer[userId] = {}; 
			validationCacheDefer[userId][validationId] = [];  
		}
		else if(validationCacheDefer[userId][validationId] != undefined)
		{
			validationCacheDefer[userId][validationId].push(deferred);
			return deferred.promise;
		}
		else if(this.validationCache[userId][validationId] == undefined){
			this.validationCache[userId][validationId] = {};
			validationCacheDefer[userId][validationId] = []; 
		}
		else
		{
			setTimeout(function(){
				deferred.resolve(cache[userId][validationId]);
			});
			return deferred.promise;
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.validationId = validationId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					for(var id in data){
						var validationItem = new validation(data[id], id, recordId, userId);

						cache[userId][validationId] = {};
						for(var i in validationItem){
							cache[userId][validationId][i] = validationItem[i];
						}
						
						if (validationCacheDefer[userId][validationId] != undefined)
							for (var i in validationCacheDefer[userId][validationId])
								validationCacheDefer[userId][validationId][i].resolve(cache[userId][validationId]);
						validationCacheDefer[userId][validationId] = undefined;
						deferred.resolve(cache[userId][validationId]);
					}
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var checkAgentBeforeCreateValidation = function(agentId){
		var deferred = $q.defer();

		var obj = {sessionId: session.currentUser.sessionId};
		obj.agentId = agentId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/agent/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					deferred.resolve();
				}).error(function(data, status, headers, config){
					console.log(data);
					console.log('trying to create agent..');

					if(agentId.indexOf("user-", 0) == 0){
						var agentUserId = agentId.substr(5, agentId.length-1);
						obj.agentName = "User: '"+agentUserId+"'";
						obj.agentDescription = "Competency Management User with User ID '"+agentUserId+"'";
						obj.agentType = "User";
					}else{
						obj.agentName=agentId;
						obj.agentDescription="N/A";
						obj.agentType="N/A";
					}

					data = new FormData();
					data.append(dataObjectName, JSON.stringify(obj));

					$http.post(apiURL + "record/validation/agent/create", data,
							{
						headers: {'Content-Type': undefined},
						transformRequest: function(data){ return data; }
							}).success(function(data, status, headers, config){
								deferred.resolve();
							}).error(function(data, status, headers, config){
								deferred.reject(data);
							});

				});

		return deferred.promise;
	}

	var createValidation = function(userId, recordId, agentId, confidence, evidenceIds){
		var deferred = $q.defer();

		if(userId == undefined || userId == ""){
			setTimeout(function(){
				deferred.reject("Cannot Create Validation without userId");
			}, 10);
		}

		if(recordId == undefined || recordId == ""){
			setTimeout(function(){
				deferred.reject("Cannot Create Validation without recordId");
			}, 10);
		}

		if(agentId == undefined || agentId == ""){
			setTimeout(function(){
				deferred.reject("Cannot Create Validation without agentId");
			}, 10);
		}

		if(confidence == undefined || confidence == ""){
			setTimeout(function(){
				deferred.reject("Cannot Create Validation without confidence value");
			}, 10);
		}

		var vCache = this.validationCache;
		var eCache = this.evidenceCache;

		checkAgentBeforeCreateValidation(agentId).then(function(){
			var obj = {sessionId: session.currentUser.sessionId};
			obj.userId = userId;
			obj.recordId = recordId;
			obj.confidence = confidence;
			obj.agentId = agentId;

			var data = new FormData();

			if(evidenceIds == undefined || evidenceIds.length >= 1){
				// Create With Evidence Id List
				obj.evidenceIds = evidenceIds;
				data.append(dataObjectName, JSON.stringify(obj));

				$http.post(apiURL + "record/validation/createWithEvidenceIds", data,
						{
					headers: {'Content-Type': undefined},
					transformRequest: function(data){ return data; }
						}).success(function(data, status, headers, config){
							var val;
							for(var valId in data){
								val = new validation(data[valId], valId, recordId, userId);  
							}

							deferred.resolve(val);
						}).error(function(data, status, headers, config){
							deferred.reject(data);
						});
			}else{
				// Create Without Evidences
				data.append(dataObjectName, JSON.stringify(obj));

				$http.post(apiURL + "record/validation/createNoEvidence", data,
						{
					headers: {'Content-Type': undefined},
					transformRequest: function(data){ return data; }
						}).success(function(data, status, headers, config){
							var val;
							for(var valId in data){
								val = new validation(data[valId], valId, recordId, userId);  
							}

							deferred.resolve(val);
						}).error(function(data, status, headers, config){
							deferred.reject(data);
						});

			}
		}, function(error){

		});

		return deferred.promise;
	}

	var updateValidationConfidence = function(userId, recordId, validationId, confidence){
		//TODO: Needs to invalidate cache.

		var cache = this.validationCache;

		var deferred = $q.defer();

		if(this.validationCache[userId] == undefined){
			this.validationCache[userId] = {};  
		}

		if(this.validationCache[userId][validationId] == undefined){
			this.validationCache[userId][validationId] = {};
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.validationId = validationId;
		obj.confidence = confidence;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/updateConfidence", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){

					var validationItem = new validation(data, validationId, recordId, userId);

					for(var i in validationItem){
						cache[userId][validationId][i] = validationItem[i];
					}

					deferred.resolve(cache[userId][validationId]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}


	var createUnattachedEvidence = function(evidenceData, userId){
		//TODO: Needs to invalidate cache.

		var cache = this.evidenceCache;

		var deferred = $q.defer();

		var data = new FormData();
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		for(var key in evidenceData){
			switch(key){
			case "date":
				obj.evidenceDate = evidenceData[key];
				break;
			case "description":
				obj.evidenceDescription = evidenceData[key];
				break;
			case "valueType":
				switch(evidenceData[key]){
				case "URI":
					obj.evidenceURI = evidenceData["value"];
					break;
				case "String":
					obj.evidenceString = evidenceData["value"];
					break;
				case "Filename":
					if(evidenceData["valueFile"] == undefined){
						
						setTimeout(function(){
							deferred.reject("Couldn't Find a File to Upload as Evidence!");
						}, 10);
						
						return deferred.promise;
					}else{
						obj.evidenceFileName = evidenceData["valueFile"].name;
						data.append(obj.evidenceFileName, evidenceData["valueFile"]);
					}
					
					break;  
				}
				break;
			case "type":
				obj.evidenceType = evidenceData[key];
				break;
			case "result":
				obj.evidenceResult = evidenceData[key];
				break;
			default:
				console.log(key);
			break;
			}
		}

		
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/evidence/create", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var ev = new evidence(data, userId);

					cache[ev.id] = ev;

					deferred.resolve(cache[ev.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var addEvidenceToValidation = function(evidenceData, validationId, userId){
		//TODO: Needs to invalidate cache.

		var vCache = this.validationCache;
		var eCache = this.evidenceCache;

		var deferred = $q.defer();

		var data = new FormData();
		
		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.validationId = validationId;
		for(var key in evidenceData){
			switch(key){
			case "date":
				obj.evidenceDate = evidenceData[key];
				break;
			case "description":
				obj.evidenceDescription = evidenceData[key];
				break;
			case "valueType":
				switch(evidenceData[key]){
				case "URI":
					obj.evidenceURI = evidenceData["value"];
					break;
				case "String":
					obj.evidenceString = evidenceData["value"];
					break;
				case "Filename":
					if(evidenceData["valueFile"] == undefined){
						
						setTimeout(function(){
							deferred.reject("Couldn't Find a File to Upload as Evidence!");
						}, 10);
						
						return deferred.promise;
					}else{
						obj.evidenceFileName = evidenceData["valueFile"].name;
						data.append(obj.evidenceFileName, evidenceData["valueFile"]);
					}
					
					break;  
				}
				break; 
			case "type":
				obj.evidenceType = evidenceData[key];
				break;
			case "result":
				obj.evidenceResult = evidenceData[key];
				break;
			default:
				console.log(key);
			break;
			}
		}

		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/evidence/add", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var ev = new evidence(data, userId);

					eCache[ev.id] = ev;

					deferred.resolve(eCache[ev.id]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var getEvidence = function(userId, evidenceId){
		//TODO: Needs to use cache appropriately.

		var cache = this.evidenceCache;

		var deferred = $q.defer();

		if(this.evidenceCache[evidenceId] == undefined){
			this.evidenceCache[evidenceId] = {};  
		}else{
			setTimeout(function(){
				deferred.notify(cache[evidenceId]);
			})
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.evidenceId = evidenceId;

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/evidence/read", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var evidenceItem = new evidence(data, userId);

					for(var i in evidenceItem){
						cache[evidenceId][i] = evidenceItem[i];
					}

					deferred.resolve(cache[evidenceId]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	var updateEvidence = function(evidenceData, userId, evidenceId){
		//TODO: Needs to invalidate cache.

		var cache = this.evidenceCache;

		var deferred = $q.defer();

		if(this.evidenceCache[evidenceId] == undefined){
			this.evidenceCache[evidenceId] = {};  
		}

		var obj = {sessionId: session.currentUser.sessionId};
		obj.userId = userId;
		obj.evidenceId = evidenceId;

		for(var key in evidenceData){
			switch(key){
			case "description":
				obj.evidenceDescription = evidenceData[key];
				break;
			case "type":
				obj.evidenceType = evidenceData[key];
				break;
			}
		}

		var data = new FormData();
		data.append(dataObjectName, JSON.stringify(obj));

		$http.post(apiURL + "record/validation/evidence/update", data,
				{
			headers: {'Content-Type': undefined},
			transformRequest: function(data){ return data; }
				}).success(function(data, status, headers, config){
					var evidenceItem = new evidence(data, userId);

					for(var i in evidenceItem){
						cache[evidenceId][i] = evidenceItem[i];
					}

					deferred.resolve(cache[evidenceId]);
				}).error(function(data, status, headers, config){
					deferred.reject(data);
				});

		return deferred.promise;
	}

	return {
		validationCache: validationCache,
		evidenceCache: evidenceCache,

		makeLocalValidation: function(data, validationId, recordId, userId){ 
			if(validationCache[userId] == undefined){
				validationCache[userId] = {};
			}
			var newValidation = new validation(data, validationId, recordId, userId);

			recordCache[userId][validationId] = newValidation;

			return recordCache[userId][validationId];
		},

		getValidation: getValidation,
		updateValidationConfidence:updateValidationConfidence,

		createValidation:createValidation,

		createUnattachedEvidence: createUnattachedEvidence,
		addEvidenceToValidation: addEvidenceToValidation,

		getEvidence: getEvidence,
		updateEvidence: updateEvidence,
	};

}]);


