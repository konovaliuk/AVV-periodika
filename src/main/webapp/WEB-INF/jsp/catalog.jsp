<%@ include file="includes/common.jspf" %>
<c:set var="path_to_images" value="../../images/periodicals/"/>
<c:set var="display_periodicals" value="${not empty periodicals}"/>
<c:set var="display_category_info" value="${not empty category_info}"/>
<c:set var="display_catalog_info" value="${not display_category_info}"/>
<jsp:useBean id="categoryTypeInfo" class="model.CategoryTypeBean">
    <jsp:setProperty name="categoryTypeInfo" property="language" value="${language}"/>
    <jsp:setProperty name="categoryTypeInfo" property="type" value="${category_info.type}"/>
</jsp:useBean>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.catalog"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid">
    <div class="row content">
        <div class="col-sm-2 sidenav">
            <%@ include file="includes/menu_side_categories.jspf" %>
        </div>
        <div class="col-sm-9 text-left">
            <%@ include file="includes/display_message.jspf" %>
            <div class="container-fluid">
                <c:if test="${display_catalog_info}">
                    <div class="row">
                        <div class="col-sm-9">
                            <h1><fmt:message key="text.catalog.welcome"/></h1>
                        </div>
                        <div class="col-sm-3">
                            <c:if test="${admin_mode}">
                                <%@ include file="includes/menu_admin_catalog.jspf" %>
                            </c:if>
                        </div>
                    </div>
                </c:if>
                <c:if test="${display_category_info}">
                    <%@ include file="includes/block_category.jspf" %>
                </c:if>
                <c:if test="${display_periodicals}">
                    <hr/>
                        <%@ include file="includes/list_periodicals.jspf" %>
                    <hr/>
                    <div class="row">
                        <div class="col-sm-12">
                            <%@ include file="includes/menu_pagination.jspf" %>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="col-sm-1 sidenav">
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>
