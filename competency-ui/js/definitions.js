// Definitions of important values for the Competency Manager
angular.module('CompetencyManager.definitions', []).

value('apiURL', 'http://localhost:9722/api/custom/competency/').

value('guestUser', {
	exists: true,
	userId: 'guest',
	password: 'password'
}).

value('version', '0.5').

value('dataObjectName', 'competency').

value('defaultModelId', 'model-default').

value('errorCode', {
	'emptyParam': 'empty',
	'badValue': 'value',
	'existence': 'exist',
	'defaultObject': 'default',
	'login': 'login',
	'access': 'access',
	'sessionExpired': 'expired',
});

