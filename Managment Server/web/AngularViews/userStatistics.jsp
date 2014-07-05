<%@ page import="com.mygreenbill.common.GreenBillUser" %>
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
        <%
            GreenBillUser user = (GreenBillUser) session.getAttribute("user");
        %>
        <h2><%=user!= null? user.getFirstName() + " " + user.getLastName() : "" +" Statistics"%></h2>
    </div>
</div>

<hr />

<div class="row">
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 style="display: inline">{{currMonthName}} Total Expenses </h3>
            </div>
            <div class="panel-body">
                <h1 class="text-center"> {{ currentMonthTotalExpenses }} ₪</h1>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 style="display: inline">{{lastMonthName}} Total Expenses</h3>
            </div>
            <div class="panel-body">
                <h1 class="text-center"> {{ lastMonthTotalExpenses }} ₪</h1>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 style="display: inline">  Year Stats</h3>
            </div>
            <div class="panel-body">
                <div id="year-stats"></div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 style="display: inline">{{currMonthName}} Expenses By Category </h3>
            </div>
            <div class="panel-body">
                <div id="donut-example"></div>
            </div>
        </div>
    </div>
</div>
<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

    ga('create', 'UA-52561351-1', 'auto');
    ga('send', 'pageview');

</script>
