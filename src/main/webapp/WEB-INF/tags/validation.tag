<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="key" required="true" %>
<%@ variable name-given="valid_class" scope="AT_END" %>
<%@ variable name-given="valid_icon" scope="AT_END" %>
<%@ variable name-given="isValid" variable-class="java.lang.Boolean" scope="AT_END" %>
<c:set var="valid_class" value=""/>
<c:set var="valid_icon" value=""/>
<c:set var="isValid" value="${true}"/>
<c:if test="${not empty validation_info}">
    <c:set var="isValid" value="${validation_info.getOrDefault(key, true)}"/>
    <c:set var="valid_class"
           value="has-feedback has-${isValid?'success':'warning'}"/>
    <c:set var="valid_icon"
           value="<span class='form-control-feedback glyphicon glyphicon-${
           isValid?'ok':'warning-sign'
           }'></span>"/>
</c:if>
