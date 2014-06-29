<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/23/14
  Time: 8:22 PM
  To change this template use File | Settings | File Templates.
--%>
<link href="assets/css/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

<!-- <script src="assets/plugins/jquery-2.1.1.min.js"></script> -->
<script src="js/pnotify.custom.min.js" type="text/javascript" ></script>

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

    function reloadPage()
    {
        window.location.reload();
    }

    $("#submitButton").click(function(e)
    {
        PNotify.prototype.options.styling = "bootstrap3";
        (new PNotify({
            title: 'Confirmation Needed',
            text: 'Are you sure?',
            icon: 'glyphicon glyphicon-question-sign',
            hide: false,
            confirm: {
                confirm: true
            },
            buttons: {
                closer: false,
                sticker: false
            },
            history: {
                history: false
            }
        })).get().on('pnotify.confirm', function()
                {
                    document.getElementById("selectCompanyForm").submit();
                    new PNotify({
                        title: 'Thank You!',
                        text: 'Your changes were made and the companies were notified.',
                        type: 'success'
                    });

                    window.setTimeout(reloadPage,2500) // Wait for 2.5s and reload the page
                }).on('pnotify.cancel', function()
                {
                    new PNotify({
                        title: 'Nothing to do',
                        text: 'No changes were made.',
                        type: 'info'
                    });
                });
    });

</script>

<div class="row">
    <div class="col-lg-8">
        <h2>SELECT NEW COMPANIES PAGE</h2>
    </div>
</div>

<hr />

<div class="row">

    <Button id="submitButton" style='margin-left: 15px' class="btn btn-primary"> Save </Button>

    Select All? <input id="selectAll" type="checkbox"><br><br>

    <form id="selectCompanyForm" method="post" action="rest/company/addUserCompanies">
        <div class="col-lg-8" ng-if="otherCompanies == 0" >
            <h3>You are not subscribed to any company</h3>
        </div>

        <div ng-repeat="company in otherCompanies" class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label>
                        <input class="checkbox1" type="checkbox" name="company" value={{company.id}}>
                        {{ company.name }}
                    </label>
                </div>
                <div class="panel-body">
                    <img class="company-logo" ng-src={{company.logo_path}} />
                </div>
            </div>
        </div>

    </form>
</div>


