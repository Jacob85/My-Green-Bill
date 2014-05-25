/**
 * Created by ipeleg on 5/13/14.
 */

angular.module('userSelectCompaniesController',[])
    .controller('SelectCompaniesController', ['$scope', '$http',
        function getCompaniesList ($scope, $http)
        {
            $http.get('http://localhost:8080/greenbill/rest/company/allOtherCompanies')
                .success(function (response)
                {
                    console.log(response);
                    $scope.otherCompanies  = response;
                });
        }
    ]);