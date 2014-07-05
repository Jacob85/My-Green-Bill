<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 5/15/14
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>


</head>
<body>
       <%
           GreenBillUser user = (GreenBillUser) request.getSession().getAttribute("user");
       %>
  <div class="inner">
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
              <input type="text" class="input-block-level" ng-model="searchstr" placeholder="filter the table..." />
              <select class="input-block-level" id="selectedFilter">
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
          <td> <a ng-attr-href="${pageContext.request.contextPath}/dashboard/downloadBill?path={{bill.fie_path}}" target="_blank"> {{linkName}} </a></td>
      </tr>

      </tbody>
      </table>
      </div>

      </div>
      </div>
      </div>
      </div>
  </div>



  <script>
      $(document).ready(function () {
          $('#dataTables-example').dataTable();
      });
  </script>
       <script>
           (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
               (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                   m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
           })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

           ga('create', 'UA-52561351-1', 'auto');
           ga('send', 'pageview');

       </script>

</body>
</html>
