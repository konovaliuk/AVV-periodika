<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../jsp/includes/common_localization.jspf" %>
<%@ attribute name="fieldName" required="true" %>
<%@ attribute name="labelPrefix" required="true" %>
<%@ attribute name="fieldValue" required="true" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="pattern" required="false" %>
<%@ attribute name="required" required="false" %>
<c:set var="req_attribute" value=""/>
<c:if test="${not empty required}">
    <c:set var="req_attribute">required=${required}</c:set>
</c:if>
<tags:validation key="${fieldName}"/>
<div class="form-group has-feedback has-${valid_class}">
    <label class="control-label col-sm-3" for="${fieldName}">
        <fmt:message key="label.user.${fieldName}"/> ${required eq "true" ? '*':''}
    </label>
    <div class="col-sm-9">
        <input id="${fieldName}" type="${not empty type ? type : 'text'}" class="form-control"
                <c:if test="${not empty pattern}">
                    pattern="<fmt:message key="pattern.${pattern}.regexp"/>"
                    title="<fmt:message key="pattern.${pattern}.description"/>"
                    placeholder="<fmt:message key="pattern.${pattern}.description"/>"
                </c:if>
               name="${fieldName}"
               value="${fieldValue}" ${req_attribute}/>
        ${valid_icon}
        ${errorMessage}
    </div>
</div>
