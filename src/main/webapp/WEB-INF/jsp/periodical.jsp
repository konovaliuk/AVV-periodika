<%@ include file="includes/common.jspf" %>
<c:set var="path_to_images" value="../../images/periodicals/"/>
<jsp:useBean id="categoryTypeInfo" class="model.CategoryTypeBean">
    <jsp:setProperty name="categoryTypeInfo" property="language" value="${language}"/>
    <jsp:setProperty name="categoryTypeInfo" property="type" value="${periodical_info.categoryType}"/>
</jsp:useBean>
<c:set var="temp_periodical_info" value="${requestScope.temp_periodical_info}"/>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <c:out value="${periodical_info.title}"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60" data-locale="${language}">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid">
    <div class="row content">
        <div class="col-sm-2 sidenav">
            <%@ include file="includes/menu_side_categories.jspf" %>
        </div>
        <div class="col-sm-10 text-left bg-grey">
            <%@ include file="includes/display_message.jspf" %>
            <div class="container-fluid">
                <c:choose>
                    <c:when test="${admin_mode and not empty temp_periodical_info}">
                        <div class="row">
                            <div class="col-sm-8">
                                <h1 class="text-uppercase"><c:out value="${periodical_info.title}"/></h1>
                            </div>
                            <div class="col-sm-3">
                                <%@ include file="includes/menu_admin_periodical.jspf" %>
                            </div>
                            <div class="col-sm-1">
                            </div>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="tab-content">
                                <div id="view" class="tab-pane fade${not edit_mode ? ' in active':''}">
                                    <%@ include file="includes/block_periodical_view.jspf" %>
                                </div>
                                <div id="edit" class="tab-pane fade${edit_mode ? ' in active':''}">
                                    <%@ include file="includes/block_periodical_edit.jspf" %>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <div class="col-sm-12">
                                <h1><c:out value="${periodical_info.title}"/></h1>
                            </div>
                        </div>
                        <hr/>
                        <div class="row">
                            <%@ include file="includes/block_periodical_view.jspf" %>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>
