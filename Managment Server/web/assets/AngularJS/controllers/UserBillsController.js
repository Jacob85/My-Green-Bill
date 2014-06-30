/**
 * Created by Jacob on 5/15/14.
 */

var application = angular.module('userBillsController',[]);

application.controller('BillsController', ['$scope', '$http', '$location',
    function userDashboard($scope, $http, $location)
    {
        $scope.date = "Date";
        $scope.companyName = "Company Name";
        $scope.subject = "Subject";
        $scope.content = "Content";
        $scope.download = "Download File";
        $scope.linkName = "pdf";


        var queryParameter = $location.search();
        console.log("queryParameter = " + queryParameter);
        var queryString;
        if (queryParameter.companyId != null)
        {
            queryString = '/greenbill/rest/bills/fromCompany?companyId='+queryParameter.companyId;
            $scope.title = queryParameter.companyName;
        }
        else
        {
            queryString = '/greenbill/rest/bills/all';
            $scope.title = "All Bills";
        }
        console.log("run http get with string: " + queryString);
        $http.get(queryString)
            .success(function (response)
            {
                console.log(response);
                console.log(response.toString());
                if (response.error)
                {
                    console.log("Error Json");
                    //error json
                    $scope.bills = [{"date": 'NA', "company_name": 'NA', "message_subject": "NA", "fie_path": "NA"}];
                    $scope.linkName = "";
                }
                else
                {
                    console.log("Valod respons "+ response);
                    $scope.bills  = response;
                    $scope.linkName = "pdf";
                }
            })
            .error(function(data, status, headers, config) {
                console.log("We are in error heandling!!")
                console.log(data);
                console.log(status);
                console.log(headers);
                console.log(config);
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



