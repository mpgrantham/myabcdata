angular
	.module('MyABCData')
    .config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/home', {
                templateUrl: 'src/myabcdata/components/home/home.html',
                controller: 'HomeCtrl'
            }).
            when('/signin', {
                templateUrl: 'src/myabcdata/components/signin/signin.html',
                controller: 'SignInCtrl'
            }).
            when('/register', {
                templateUrl: 'src/myabcdata/components/register/register.html',
                controller: 'RegisterCtrl'
            }).
            when('/reset/:key', {
                templateUrl: 'src/myabcdata/components/reset/reset.html',
                controller: 'ResetCtrl'
            }).
            when('/entry', {
                templateUrl: 'src/myabcdata/components/entry/entry.html',
                controller: 'EntryCtrl'
            }).
            when('/entry/Observed/:observedId/Incident/:incidentId', {
                templateUrl: 'src/myabcdata/components/entry/entry.html',
                controller: 'EntryCtrl'
            }).
            when('/log', {
                templateUrl: 'src/myabcdata/components/log/log.html',
                controller: 'LogCtrl'
            }).
            when('/settings', {
                templateUrl: 'src/myabcdata/components/settings/settings.html',
                controller: 'SettingsCtrl'
            }).
            otherwise({
                redirectTo: '/home'
            });
    }]);