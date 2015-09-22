'use strict';

/* Directives */


angular.module('CompetencyManager.directives', []).
directive('appVersion', ['version', function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
}]).
directive('ngEnter', function() {
	return function(scope, elm, attrs) {
		elm.bind("keydown keypress", function(event){
			if(event.which === 13){
				scope.$apply(function(){
					scope.$eval(attrs.ngEnter, {'event': event});
				});

				event.preventDefault();
			}  
		});
	};
}).
directive('ngEscape', function() {
	return function(scope, elm, attrs) {
		elm.bind("keydown keypress", function(event){
			if(event.which === 27){
				scope.$apply(function(){
					scope.$eval(attrs.ngEscape, {'event': event});
				});

				event.preventDefault();
			}  
		});
	};
}).
directive('ngDropdownChecklist', ['$parse', '$location', 'search', 'appCache', 'context', function($parse, $location, search, appCache, context) {
	var models = undefined;
	return {
		require:'ngModel',
		link: function(scope, elm, attrs) {
	
			$(document).bind('click', function (e) {
				var $clicked = $(e.target);
				
				if (!$clicked.parents().hasClass(elm.attr("class")) && !elm.find("#options").is(":hidden")){
					
					elm.find("#options").hide();
					
					if(elm.find("#options").is(":hidden") && appCache.context == context.competency && models != undefined){
						scope.$apply(function(){
							if($location.path().indexOf('results') != -1){
								$location.search('model', models);
								if(search.query == "" || search.query == undefined){
									search.viewAll(context.competency, search.model);
								}else{
									search.search2(search.query, context.competency, search.model);
								}
							}
								
						})
					}
				}
			});
	
			elm.find('#display').on('click', function () {
				elm.find("#options").toggle();
				
				if(elm.find("#options").is(":hidden") && models != undefined){
					scope.$apply(function(){
						if($location.path().indexOf('results') != -1){
							$location.search('model', models);
							if(search.query == "" || search.query == undefined){
								search.viewAll(context.competency, search.model);
							}else{
								search.search2(search.query, context.competency, search.model);
							}
						}
							
					})
				}
			});
			
			
			scope.$on('repeat_done', function( domainElement ) {
				elm.find('input[type="checkbox"]').on('click', function () {
					var checked = 0;
					var html = "";
					
					models = [];
					
					elm.find('input[type="checkbox"]').each(function(){
						if($(this).is(":checked")){	
							checked++;
							
							models.push($(this).val())
						}
					});
					
					if(checked == elm.find("input[type='checkbox']").length){
						//elm.find("#all").show();
						
						models = search.ALL_MODELS;
						elm.find("input[type='checkbox']").prop('checked', true)
					
						//elm.find("#all").hide();
					}
					
					scope.$apply(function(){
						$parse(attrs.ngModel).assign(scope, models);
					})
					
				});
		    } );
		}
	};
}]).
directive("ngOnRepeatDone", function() {
    return function($scope, element, attributes ) {
    	if($scope.$last){
    		$scope.$emit(attributes["onRepeatDone"] ? attributes["onRepeateDone"] : "repeat_done", element);
    	}
    }
});

