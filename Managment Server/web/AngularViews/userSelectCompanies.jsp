<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/23/14
  Time: 8:22 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="row">
    <div class="col-lg-8">
        <h2>THIS IS USER SELECT COMPANIES PAGE</h2>
    </div>
</div>

<div class="row">
    <form name="selectCompanyForm" action="http://localhost:8080/greenbill/rest/company/updateUserCompanies" method="post">
        <input style="margin-left: 15px" type="submit" value="Save" class="btn btn-primary"><br><br>
        <div ng-repeat="company in companies" class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <input type="checkbox" name="company" value={{company.id}} >
                    {{ company.name }}
                </div>
                <div class="panel-body">
                    value = {{ company.id }}
                </div>
            </div>
        </div>
    </form>
</div>
