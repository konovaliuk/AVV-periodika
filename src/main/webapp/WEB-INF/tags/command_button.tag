<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ attribute name="path" required="true" %>
<ctg:is-current-path path="${path}"/>
<li ${isCurrentPath ?'class="active"':''}>
    <a href="${path}">
        <jsp:doBody/>
    </a>
</li>
