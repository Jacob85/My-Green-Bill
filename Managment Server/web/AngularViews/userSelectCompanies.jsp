<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/23/14
  Time: 8:22 PM
  To change this template use File | Settings | File Templates.
--%>
<script src="assets/css/jquery-ui.css"></script>
<script src="assets/plugins/jquery-2.0.3.min.js"></script>


<script language="javascript" type="text/javascript">

    $("#selectAll").click(function(event) {  //on click
        if(this.checked)
        { // check select status
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"
            });
        }
        else
        {
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"
            });
        }
    });

    $("#selectCompanyForm").submit(function(e)
    {
        var postData = $(this).serializeArray();
        var formURL = $(this).attr("action");
        $.ajax(
                {
                    url : formURL,
                    type: "POST",
                    data : postData,
                    success:function(data, textStatus, jqXHR)
                    {
                        window.location.reload(true); // Reloading the page from the server
                    },
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        //if fails
                    }
                });
        e.preventDefault(); //STOP default action
        e.unbind(); //unbind. to stop multiple form submit.
    });
</script>

<div class="row">
    <div class="col-lg-8">
        <h2>THIS IS USER SELECT COMPANIES PAGE</h2>
    </div>
</div>

<hr />

<div class="row">
    <form id="selectCompanyForm" method="post" action="rest/company/addUserCompanies">
        <input id="submitButton" style="margin-left: 15px" type="submit" value="Save" class="btn btn-primary">
        <input id="selectAll" type="checkbox" style="margin-left: 15px"> Select All?<br><br>

        <div class="col-lg-8" ng-if="otherCompanies == 0" >
            <h3>You are not subscribed to any company</h3>
        </div>

        <div ng-repeat="company in otherCompanies" class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <input class="checkbox1" type="checkbox" name="company" value={{company.id}}>
                    {{ company.name }}
                </div>
                <div class="panel-body">
                    <img class="company-logo" src={{company.logo_path}} />
                </div>
            </div>
        </div>

    </form>
</div>


