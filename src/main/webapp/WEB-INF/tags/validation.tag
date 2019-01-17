<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../jsp/includes/common_localization.jspf" %>
<%@ attribute name="key" required="true" %>
<%@ variable name-given="valid_class" scope="AT_END" %>
<%@ variable name-given="valid_icon" scope="AT_END" %>
<%@ variable name-given="isValid" variable-class="java.lang.Boolean" scope="AT_END" %>
<%@ variable name-given="errorMessage" variable-class="java.lang.String" scope="AT_END" %>
<c:set var="valid_class" value=""/>
<c:set var="valid_icon" value=""/>
<c:set var="isValid" value="${true}"/>
<c:set var="errorMessage" value=""/>
<c:if test="${not empty validation_info}">
    <c:set var="isValid" value="${validation_info.isValid(key)}"/>
    <c:set var="valid_class" value="${isValid?'success':'warning'}"/>
    <c:set var="valid_icon">
        <span class="form-control-feedback glyphicon glyphicon-${isValid?'ok':'warning-sign'} text-${valid_class}"></span>
    </c:set>
    <c:if test="${not isValid}">
        <c:set var="errorMessageKey" value="${validation_info.getErrorMessage(key)}"/>
        <c:if test="${not empty errorMessageKey}">
            <c:set var="errorMessage">
                <span class="label label-warning">
                    <fmt:message bundle="${view_messages}" key="${errorMessageKey}"/>
                </span>
            </c:set>
        </c:if>
    </c:if>
</c:if>
