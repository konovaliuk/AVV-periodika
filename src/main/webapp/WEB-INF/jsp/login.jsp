<%@ include file="includes/common.jspf" %>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.login"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="jumbotron text-center">
    <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
            <form class="form-horizontal" method="post" action="login">
                <input type="hidden" name="command" value="login"/>
                <div class="form-group">
                    <label class="control-label col-sm-3" for="login">
                        <fmt:message key="label.user.login"/>
                    </label>
                    <div class="col-sm-9"><input id="login" type="text" class="form-control" name="login" value=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3" for="pwd">
                        <fmt:message key="label.user.password"/>
                    </label>
                    <div class="col-sm-9">
                        <input id="pwd" type="password" class="form-control" name="password" value="">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-default">
                            <fmt:message key="button.submit"/>
                        </button>
                    </div>
                </div>
            </form>
            <%@ include file="includes/display_message.jspf" %>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>
</body>
<%@ include file="includes/footer.jspf" %>
</html>