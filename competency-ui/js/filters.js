'use strict';

/* Filters */

angular.module('CompetencyManager.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    };
  }]).
  filter('capitalize', function(){
  	return function(text) {
  		return text.charAt(0).toUpperCase() + text.slice(1);
  	}
  }).
  filter('filterObj', function(){
    return function(object, func) {
      var result = {};
      
      for(var id in object){
        if(func(object[id])){
          result[id] = object[id];
        }  
      }

      return result;
    }
  }).
  filter('orderByObj', function(){
    return function(object, func) {
      if(object != undefined){

        var comparison = function(){};

        if(func[0] == "-"){
          func = func.slice(1);

          comparison = function(first, second){
            if(first > second){
              return true;
            }else{
              return false;
            }
          }
        }else{
          if(func[0] == "+"){
            func = func.slice(1);
          }

          comparison = function(first, second){
            if(first <= second){
              return true;
            }else{
              return false;
            }
          }
        }
        
        var result = [];
        
        var keys = Object.keys(object);
        for(var id in object){
          if(result.length == 0){
            result.push(object[id]);
            continue;
          }

          for(var i in result){
            if(comparison(result[i][func], object[id][func])){
              if(i == result.length - 1){
                result.splice(i+1, 0, object[id]);
                break;
              }
            }else{
              result.splice(i, 0, object[id]);
              break;
            }
          }
          
        }

        return result;
      }

      return [];
    }
  });
