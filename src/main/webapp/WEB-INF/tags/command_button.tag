<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ attribute name="path" required="true" %>
<ctg:is-current-path path="${context_path}${path}"/>
<li ${isCurrentPath ?'class="active"':''}>
    <a href="${context_path}${path}">
        <jsp:doBody/>
    </a>
</li>
