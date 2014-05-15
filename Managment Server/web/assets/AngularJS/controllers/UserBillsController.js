/**
 * Created by Jacob on 5/15/14.
 */




angular.module('userBillsController',[])
    .controller('BillsController', ['$scope', '$http',
        function userDashboard($scope, $http)
        {
            $scope.date = "Date";
            $scope.companyName = "Company Name";
            $scope.subject = "Subject";
            $scope.content = "Content";
            $scope.download = "Download File";

            $http.get('http://localhost:8080/greenbill/rest/bills/all')
                .success(function (response)
                {
                    console.log(response);
                    $scope.bills  = response;
                });

        }
    ]);

