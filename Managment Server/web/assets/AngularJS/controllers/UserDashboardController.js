/**
 * Created by ipeleg on 5/14/14.
 */

angular.module('userDashboardController',[])
    .controller('DashboardController', ['$scope', '$http',
        function userDashboard($scope, $http)
        {
            $scope.title = 'IDAN';
        }
    ]);
