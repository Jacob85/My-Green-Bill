/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userSelectCompaniesController',[])
    .controller('SelectCompaniesController', ['$scope', '$http',
        function getCompaniesList ($scope, $http)
        {
            $http.get('/greenbill/rest/company/allOtherCompanies')
                .success(function (response)
                {
                    $scope.otherCompanies  = response;
                });
        }
    ]);