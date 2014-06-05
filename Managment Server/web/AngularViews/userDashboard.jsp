<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/14/14
  Time: 12:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%GreenBillUser user = (GreenBillUser) request.getSession().getAttribute("user");%>
    <title></title>
</head>
<body>
    <div class="row">
        <div class="col-lg-12">
            <h2><%=user!= null? user.getFirstName() + " " + user.getLastName() : "" +" Dashboard"%></h2>
        </div>
    </div>
    <div class="row" ng-controller="StatisticsController">
        <div class="col-lg-6">
            <div class="panel panel-info">
                <div class="panel-heading">
                   <h3 style="display: inline"> {{currMonthName}} Total Expenses </h3>
                </div>
                <div class="panel-body">
                    <h1 class="text-center"> {{ currentMonthTotalExpenses }} ₪</h1>
                </div>
                <div class="panel-footer">
                    <a href="#/User/Statistics">see more statistics</a>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 style="display: inline">  {{currMonthName}} Expenses Top Category </h3>
                </div>
                <div class="panel-body">
                    <h1 class="text-center">Top Category Spent: {{ topCategorySpent }}</h1>
                </div>
                <div class="panel-footer">
                    <a href="#/User/Statistics">see more statistics</a>
                </div>
            </div>
        </div>
    </div>
    <div class="inner" ng-controller="BillsController">
        <div class="row">
            <div class="col-lg-12">
                <h2 class="ti"> {{title}} </h2>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 style="display: inline"> <%=user != null ? user.getFirstName() + " " + user.getLastName() : ""%> Bills </h3>
                        <div style="float: right;">
                            Filter the table:
                            <input type="text" ng-model="searchstr" placeholder="filter the table..." />
                            <select id="selectedFilter">
                                <option value="date">{{ date }}</option>
                                <option value="companyName" selected>{{ companyName }}</option>
                                <option value="subject">{{ subject }}</option>
                                <!-- <option value="content">{{ content }}</option> -->

                            </select>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">


                            <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                <tr>
                                    <th>{{ date }}</th>
                                    <th>{{ companyName }}</th>
                                    <th>{{ subject}}</th>
                                    <!-- <th>{{ content }}</th> -->
                                    <th>{{ download }}</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="bill in bills | sercher:searchstr" class="gradeA">
                                    <td>{{bill.date}}</td>
                                    <td>{{bill.company_name}}</td>
                                    <td>{{bill.message_subject}}</td>
                                    <!-- <td>{{bill.message_content}}</td> -->
                                    <td> <a ng-attr-href="${pageContext.request.contextPath}/dashboard/downloadBill?path={{bill.fie_path}}" target="_blank"> pdf </a></td>
                                </tr>

                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
