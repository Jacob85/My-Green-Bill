<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/23/14
  Time: 8:22 PM
  To change this template use File | Settings | File Templates.
--%>

<script src="assets/css/jquery-ui.css"></script>
<link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

<script src="assets/plugins/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="js/pnotify.custom.min.js"></script>

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
                    document.getElementById("removeCompanyForm").submit();
                    new PNotify({
                        title: 'Thank You!',
                        text: 'Your changes were made and the companies were notified.',
                        type: 'success'
                    });

                    window.setTimeout(reloadPage,2500) // Wait for 2.5s and reload the page
                }).on('pnotify.cancel', function()
                {
                    new PNotify({
                        title: 'Cancel',
                        text: 'No changes were made.',
                        type: 'info'
                    });
                });
    });

</script>


<div class="row">
    <div class="col-lg-8">
        <h2>THIS IS USER REMOVE COMPANIES PAGE</h2>
    </div>
</div>

<hr />

<div class="row">

    <Button id="submitButton" style='margin-left: 15px' class="btn btn-primary"> Save </Button><br><br>

    <form id="removeCompanyForm" method="post" action="rest/company/removeUserCompanies">

        <div class="col-lg-8" ng-if="companies == 0" >
            <h3>You are not subscribed to any company</h3>
        </div>

        <div ng-repeat="company in companies" class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label>
                        <input class="checkbox1" type="checkbox" name="company" value={{company.id}} checked>
                        {{ company.name }}
                    </label>
                </div>
                <div class="panel-body">
                    <img class="company-logo" src={{company.logo_path}} />
                </div>
            </div>
        </div>

    </form>
</div>


