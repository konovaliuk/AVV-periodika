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
            <form class="form-horizontal" method="post" action="register">
                <input type="hidden" name="command" value="register"/>
                <tags:form_field_registration fieldValue="${temp_user_info.firstName}"
                                              fieldName="first_name" pattern="name"/>
                <tags:form_field_registration fieldValue="${temp_user_info.middleName}"
                                              fieldName="middle_name" pattern="name"/>
                <tags:form_field_registration fieldValue="${temp_user_info.lastName}"
                                              fieldName="last_name" pattern="name"/>
                <tags:form_field_registration fieldValue="${temp_user_info.email}"
                                              fieldName="email"/>
                <tags:form_field_registration fieldValue="${temp_user_info.phone}"
                                              fieldName="phone"/>
                <tags:form_field_registration fieldValue="${temp_user_info.address}"
                                              fieldName="address"/>
                <tags:form_field_registration fieldValue="${temp_user_info.login}"
                                              fieldName="login"/>
                <tags:form_field_registration fieldValue="${temp_user_info.password}"
                                              fieldName="password"/>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-default">
                            <fmt:message key="button.submit"/>
                        </button>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-4">
        </div>
    </div>
</div>
</body>
<%@ include file="includes/footer.jspf" %>
</html>