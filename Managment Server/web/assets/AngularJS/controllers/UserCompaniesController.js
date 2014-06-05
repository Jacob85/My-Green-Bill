/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userCompaniesController',[])
    .controller('CompaniesController', ['$scope', '$http',
        function getUserCompaniesList($scope, $http)
        {
            $http.get('/greenbill/rest/company/forUser')
                .success(function (response)
                {
                    $scope.companies  = response;
                });
        }
    ]);