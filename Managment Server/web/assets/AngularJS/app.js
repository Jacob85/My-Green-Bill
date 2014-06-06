/**
 * Created by ipeleg on 5/13/14.
 */
var app = angular.module('app', [
    'ngRoute',
    'userCompaniesController',
    'userStatisticsController',
    'userDashboardController',
    'userBillsController',
    'userSelectCompaniesController',
    'userSettingsController'
]);

app.config(['$routeProvider', function ($routeProvider) {
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
        .when('/User/Companies/Select',
        {
            controller: 'SelectCompaniesController',
            templateUrl: 'AngularViews/userSelectCompanies.jsp'
        })
        .when('/User/Settings',
        {
            controller: 'SettingsController',
            templateUrl: 'AngularViews/userAccountSettings.jsp'
        })
        .when('/User/Statistics',
        {
            controller: 'StatisticsController',
            templateUrl: 'AngularViews/userStatistics.jsp'
        })
        .when('/User/Bills',
        {
            controller: 'BillsController',
            templateUrl: 'AngularViews/userBills.jsp'
        })

    $routeProvider.otherwise({
        redirectTo: '/'
    })
}]);