<%@ page isErrorPage="true" %>
<%@ include file="includes/common.jspf" %>
<html>
<head>
    <title><fmt:message key="title.site"/><fmt:message key="title.error"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="jumbotron text-center">
    <div class="row">
        <div class="col-sm-12">
            <h1><fmt:message key="title.error"/></h1>
            <p><fmt:message key="message.error404"/></p>
        </div>
    </div>
</div>
</body>
</html>
