<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../jsp/includes/common_localization.jspf" %>
<%@ attribute name="fieldName" required="true" %>
<%@ attribute name="fieldValue" required="true" %>
<%@ attribute name="pattern" required="false" %>
<tags:validation key="${fieldName}"/>
<div class="form-group ${valid_class}">
    <label class="control-label col-sm-3" for="${fieldName}">
        <fmt:message key="label.user.${fieldName}"/>
    </label>
    <div class="col-sm-9">
        <input id="${fieldName}" type="text" class="form-control"
                <c:if test="${not empty pattern}">
                    pattern="<fmt:message key="pattern.${pattern}.regexp"/>"
                    title="<fmt:message key="pattern.${pattern}.description"/>"
                    <c:if test="${not isValid}">
                        placeholder="<fmt:message key="pattern.${pattern}.description"/>"
                    </c:if>
                </c:if>
               name="${fieldName}" value="${fieldValue}"/>
        ${valid_icon}
    </div>
</div>
