<!doctype html>
<html ng-app="MyABCData">
<head>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1" /> 
	<title>my ABC data</title>
	<link rel="shortcut icon" type="image/x-icon" href="myabcdata.ico" />
	
		
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-resource.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-route.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-touch.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-animate.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-aria.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-messages.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular-sanitize.min.js"></script>
	
	<script src="src/ui-bootstrap/ui-bootstrap-tpls-2.5.0.min.js"></script>
	<script src="src/ui-bootstrap/mask.min.js"></script>
	
	<script src="src/ui-grid/ui-grid.core.min.js"></script>
 	<script src="src/ui-grid/ui-grid.resize-columns.min.js"></script>
	<script src="src/ui-grid/ui-grid.expandable.min.js"></script>
	<script src="src/ui-grid/ui-grid.selection.min.js"></script>
	<script src="src/ui-grid/ui-grid.pinning.min.js"></script>
	
	<script src="src/myabcdata/main.module.js"></script>
	<script src="src/myabcdata/main.config.js"></script>
	<script src="src/myabcdata/shared/header/header.component.js"></script>
	<script src="src/myabcdata/shared/header/header.controller.js"></script>
	<script src="src/myabcdata/shared/model/user.model.js"></script>
	<script src="src/myabcdata/shared/model/observed.model.js"></script>
	<script src="src/myabcdata/components/home/home.controller.js"></script>
	<script src="src/myabcdata/components/signin/signin.controller.js"></script>
	<script src="src/myabcdata/components/register/register.controller.js"></script>
	<script src="src/myabcdata/components/settings/observedsettings.component.js"></script>
	<script src="src/myabcdata/components/settings/observedsettings.controller.js"></script>
	<script src="src/myabcdata/components/settings/settings.controller.js"></script>
	<script src="src/myabcdata/components/entry/entry.model.js"></script>
	<script src="src/myabcdata/components/entry/entry.controller.js"></script>
	<script src="src/myabcdata/components/log/log.controller.js"></script>
	<script src="src/myabcdata/components/reset/reset.controller.js"></script>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
	<!--  <link rel="stylesheet" href="css/bootstrap.min.css"> -->
	<link rel="stylesheet" href="src/ui-grid/ui-grid.css">
	<link rel="stylesheet" media="screen and (min-width: 482px)" href="css/main.css">
	<link rel="stylesheet" media="screen and (max-width: 481px)" href="css/smallmain.css">
	
</head>
<body>
<header></header>

<div id="contentArea" ng-view></div>
</body>
</html>
