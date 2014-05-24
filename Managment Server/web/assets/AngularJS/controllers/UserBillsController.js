/**
 * Created by Jacob on 5/15/14.
 */

var application = angular.module('userBillsController',[]);

    application.controller('BillsController', ['$scope', '$http',
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
                    $scope.bills  = response;
                });

        }
    ]);

    application.filter('sercher', function(){
        return function(array, str){
            if(!str){
                return array;
            }
            var result = [];
            str = str.toUpperCase();
            angular.forEach(array, function(bill){

                //var conceptName = $('#selectedFilter').find(":selected").text();
                var conceptName = $('#selectedFilter').val();
                console.log("Search By String: " + conceptName);
                switch (conceptName)
                {
                    case "date":
                    {
                        if(bill.date.toUpperCase().indexOf(str) !== -1){
                            result.push(bill);
                        }
                        break;
                    }
                    case "companyName":
                    {
                        if(bill.company_name.toUpperCase().indexOf(str) !== -1){
                            result.push(bill);
                        }
                        break;
                    }
                    case "subject":
                    {
                        if(bill.message_subject.toUpperCase().indexOf(str) !== -1){
                            result.push(bill);
                        }
                        break;
                    }
                    case "content":
                    {
                        if(bill.message_content.toUpperCase().indexOf(str) !== -1){
                            result.push(bill);
                        }
                        break;
                    }
                }

            });
            return result;
        };
    });



