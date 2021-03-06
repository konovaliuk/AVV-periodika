<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ include file="includes/common.jspf" %>
<c:if test="${not empty temp_user_info}">
    <c:set var="temp_user_info" value="${temp_user_info}" scope="request"/>
    <c:remove var="temp_user_info" scope="session"/>
</c:if>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.login"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid">
    <%@ include file="includes/display_message.jspf" %>
    <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-6">
            <h1><fmt:message key="text.registration_form"/></h1>
            <hr/>
            <c:set var="current_form_action" value="register"/>
            <c:set var="current_form_command" value="register"/>
            <%@ include file="includes/block_user_edit.jspf" %>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>