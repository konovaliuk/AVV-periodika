<%@ include file="includes/common.jspf" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title><fmt:message key="title.site"/><fmt:message key="title.error"/></title>
</head>
<body>
Request from ${pageContext.errorData.requestURI} is failed
<br/>
Servlet name: ${pageContext.errorData.servletName}
<br/>
Status code: ${pageContext.errorData.statusCode}
<br/>
Exception: ${pageContext.exception}
<br/>
Message from exception: ${pageContext.exception.message}
</body>
</html>