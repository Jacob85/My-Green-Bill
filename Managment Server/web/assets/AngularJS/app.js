/**
 * Created by ipeleg on 5/13/14.
 */
var app = angular.module('app', [
    'ngRoute',
    'userProfileController',
    'userStatisticsController',
    'userDashboardController'
]);

app.config(['$routeProvider',  function ($routeProvider) {
    $routeProvider
        .when('/',
        {
            controller: 'DashboardController',
            templateUrl: 'AngularViews/userDashboard.jsp'
        })
        .when('/User/Companies',
        {
            controller: 'CompaniesController',
            templateUrl: 'AngularViews/userCompanies.jsp'
        })
        .when('/User/Settings',
        {
            controller: 'CompaniesController',
            templateUrl: 'AngularViews/userAccountSettings.jsp'
        })
        .when('/User/Statistics',
        {
            controller: 'StatisticsController',
            templateUrl: 'AngularViews/userStatistics.jsp'
        })

    $routeProvider.otherwise({
        redirectTo: '/'
    })
}]);