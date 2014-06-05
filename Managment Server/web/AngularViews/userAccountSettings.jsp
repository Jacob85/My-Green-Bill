<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/13/14
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
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
                    User Information
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-8">
                            <form role="form">
                                <div class="form-group">
                                    <label>Forward E-mail</label>
                                    <input class="form-control">
                                    <p class="help-block">The E-mail address to which your bills will be forwarded to</p>
                                </div>
                            </form>
                        </div>
                    </div>
                    <Button id="submitButton" style='float: right' class="btn btn-primary"> Save </Button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
