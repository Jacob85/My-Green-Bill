/**
 * Created by ipeleg on 5/14/14.
 */

angular.module('userStatisticsController',[])
    .controller('StatisticsController', ['$scope', '$http',
        function userStats($scope, $http)
        {
            $scope.currMonthName = getCurrentMonthName();
            $scope.lastMonthName = getLastMonthName();

            /* Get all monthly Stats fot the user */
            $http.get('http://localhost:8080/greenbill/rest/stats/currentMonth')
                .success(function (response)
                {
                    $scope.currentMonthStats  = response;
                    $scope.currentMonthTotalExpenses =  getCurrentMonthTotalExpenses($scope.currentMonthStats);
                });

            $http.get('http://localhost:8080/greenbill/rest/stats/lastMonth')
                .success(function (response)
                {
                    $scope.lastMonthStats  = response;
                    $scope.lastMonthTotalExpenses =  getCurrentMonthTotalExpenses($scope.lastMonthStats);
                });
            /*Dounat Chart*/
            $http.get('http://localhost:8080/greenbill/rest/stats/CurrMonthByCategory')
                .success(function (response)
                {
                    $scope.dataToDounatPlot  = response;
                    generateDounatForCurrentMonthByCategory($scope.dataToDounatPlot);
                });
            /* bar Chart - 1 year*/
            $http.get('http://localhost:8080/greenbill/rest/stats/pastYear')
                .success(function (response)
                {
                    $scope.dataToBarPlot  = response;
                    generateBarChart($scope.dataToBarPlot);
                });
        }
    ]);

function generateBarChart(dataToBarPlot)
{
    Morris.Bar({
        element: 'year-stats',
        data: dataToBarPlot,
        xkey: 'Month',
        ykeys: ['Value'],
        labels: ['Expenses ', 'Series B']
    });
}

function generateDounatForCurrentMonthByCategory(dataToPlot)
{
    console.log("Array before sorting: " + dataToPlot);

    dataToPlot.sort(function(a,b){
        return b["value"] - a["value"]
    });

    console.log("Array After Sorting: " + dataToPlot);

    new Morris.Donut({
        element: 'donut-example',
        data: dataToPlot
/*        colors: [
            '#0BA462',
            '#39B580',
            '#67C69D'
        ]*/
    });
}
function getCurrentMonthTotalExpenses(currentMonthStats)
{
    var totalExpenses = 0;
    if (currentMonthStats.length == 0)
    {
        return totalExpenses;
    }
    for (index =0 ; index < currentMonthStats.length; index ++)
    {
        var stat = currentMonthStats[index];
        console.log("Stat = " + stat);
        totalExpenses += stat["amount"];
    }
    return totalExpenses;

}

function getCurrentMonthName()
{
    var d = new Date();
    var month = new Array();
    month[0] = "January";
    month[1] = "February";
    month[2] = "March";
    month[3] = "April";
    month[4] = "May";
    month[5] = "June";
    month[6] = "July";
    month[7] = "August";
    month[8] = "September";
    month[9] = "October";
    month[10] = "November";
    month[11] = "December";
    return month[d.getMonth()];
}
function getLastMonthName()
{
    var d = new Date();
    var month = new Array();
    month[0] = "January";
    month[1] = "February";
    month[2] = "March";
    month[3] = "April";
    month[4] = "May";
    month[5] = "June";
    month[6] = "July";
    month[7] = "August";
    month[8] = "September";
    month[9] = "October";
    month[10] = "November";
    month[11] = "December";
    var monthAsInt =  d.getMonth();
    if (monthAsInt == 0) //January
        return month[11];
    else
        return month[d.getMonth()-1];
}