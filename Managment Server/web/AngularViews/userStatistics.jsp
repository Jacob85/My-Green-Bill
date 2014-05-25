<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/14/14
  Time: 12:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="row">
        <div class="col-lg-12">
            <h2>THIS IS USER STATISTICS PAGE</h2>
        </div>
    </div>

    <hr />

    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    {{currMonthName}} Total Expenses
                </div>
                <div class="panel-body">
                    <h1 class="text-center"> ש"ח {{ currentMonthTotalExpenses }}</h1>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    {{lastMonthName}} Total Expenses
                </div>
                <div class="panel-body">
                    <h1 class="text-center"> {{ lastMonthTotalExpenses }} ש"ח</h1>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                   Year Stats
                </div>
                <div class="panel-body">
                    <div id="year-stats"></div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    {{currMonthName}} Expenses By Category
                </div>
                <div class="panel-body">
                    <div id="donut-example"></div>
                </div>
            </div>
        </div>
    </div>
