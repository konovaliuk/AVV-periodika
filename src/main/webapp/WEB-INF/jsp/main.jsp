<%@ include file="includes/common.jspf" %>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.main"/></title>
    <%--<c:set var="url">${pageContext.request.requestURL}</c:set>--%>
    <%--<base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/">--%>
    <%--<base href="http://localhost:8087/periodika/">--%>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="jumbotron text-center">
    <h1><fmt:message key="title.site"/></h1>
    <p><fmt:message key="title.site_slogan"/></p>
</div>
<div class="container-fluid">
    <%@ include file="includes/display_message.jspf" %>
    <%@ include file="includes/list_categories.jspf" %>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>
