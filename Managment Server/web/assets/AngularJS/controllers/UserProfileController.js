/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userProfileController',[])
    .controller('CompaniesController', ['$scope', '$http',
        function getUserCompaniesList($scope, $http)
        {
            $http.get('http://localhost:8080/greenbill/rest/company/forUser')
                .success(function (response)
                {
                    console.log(response);
                    $scope.companies  = response;
                });
        }
    ]);