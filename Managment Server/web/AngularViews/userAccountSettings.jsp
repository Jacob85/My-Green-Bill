<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    GreenBillUser user = (GreenBillUser) session.getAttribute("user");
    if (user == null)
    {
        System.out.println(user);
        response.sendRedirect("authenticate/login");
        return;
    }
%>

<link href="assets/css/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

<script src="assets/plugins/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="js/pnotify.custom.min.js"></script>

<script language="javascript" type="text/javascript">

    function reloadPage()
    {
        window.location.reload();
    }

    $("#submitButton").click(function(e)
    {
        PNotify.prototype.options.styling = "bootstrap3";
        (new PNotify({
            title: 'Confirmation Needed',
            text: 'You\'re about to change the address in which you\'ll receive your bills,\nAre you sure?',
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
                    document.getElementById("setNewAddressForm").submit();
                    new PNotify({
                        title: 'Thank You!',
                        text: 'Your new forward address was set.',
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
    <div class="col-lg-12">
        <h2>THIS IS USER ACCOUNT SETTINGS PAGE</h2>
    </div>
</div>
<hr />

<div class="row">
    <div class="col-lg-8">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 style="display: inline"> User Information</h3>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-8">
                        <form id="setNewAddressForm" role="form" method="get" action="${pageContext.request.contextPath}/dashboard/setNewForwardAddress">
                            <div class="form-group">
                                <label>Forward E-mail</label>
                                <input class="form-control" value="<% out.write(user.getForwardEmail()); %>" name="newAddress">
                                <p class="help-block">The E-mail address to which your bills will be forwarded to</p>
                            </div>
                        </form>
                        <Button id="submitButton" style='margin-left: 15px' class="btn btn-primary"> Save </Button><br><br>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
