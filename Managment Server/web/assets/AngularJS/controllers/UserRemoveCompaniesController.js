/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userRemoveCompaniesController',[])
    .controller('RemoveCompaniesController', ['$scope', '$http',
        function getCompaniesList ($scope, $http)
        {
            $http.get('http://localhost:8080/greenbill/rest/company/forUser')
                .success(function (response)
                {
                    $scope.companies  = response;
                });
        }
    ]);
