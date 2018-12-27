<%@ include file="../jsp/includes/common_localization.jspf" %>
<%@ attribute name="editing_mode" required="false" %>
<%@ attribute name="show_view" required="false" %>
<%@ attribute name="show_edit" required="false" %>
<%@ attribute name="show_menu" required="false" %>
<%@ attribute name="dropDownContent" fragment="true" %>
<ul class="nav nav-pills pull-right">
    <c:if test="${show_view}">
        <li ${not editing_mode ? 'class="active"':''}>
            <a data-toggle="pill" href="#view" class="sharp-corners">
                <span class="glyphicon glyphicon-eye-open"></span>&nbsp;
            </a>
        </li>
    </c:if>
    <c:if test="${show_edit}">
        <li ${editing_mode ? 'class="active"':''}>
            <a data-toggle="pill" href="#edit" class="sharp-corners">
                <span class="glyphicon glyphicon-pencil"></span>&nbsp;
            </a>
        </li>
    </c:if>
    <c:if test="${show_menu}">
        <li>
            <a class="dropdown-toggle sharp-corners" data-toggle="dropdown">
                <span class="glyphicon glyphicon-menu-hamburger"></span>&nbsp;
            </a>
            <!-- Dropdown menu -->
            <div class="dropdown-menu">
                <jsp:invoke fragment="dropDownContent"/>
            </div>
        </li>
    </c:if>
</ul>
