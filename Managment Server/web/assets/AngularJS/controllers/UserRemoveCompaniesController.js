/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userRemoveCompaniesController',[])
    .controller('RemoveCompaniesController', ['$scope', '$http',
        function removeCompanies ($scope, $http)
        {
            $http.get('/greenbill/rest/company/forUser')
                .success(function (response)
                {
                    $scope.companies  = response;
                });
        }
    ]);
