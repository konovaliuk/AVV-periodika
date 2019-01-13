<%@ include file="includes/common.jspf" %>
<c:set var="path_to_images" value="../../images/periodicals/"/>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.cabinet"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid">
    <div class="row content">
        <div class="col-sm-12 text-left">
            <%@ include file="includes/display_message.jspf" %>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-8">
                        <h2 class="text-uppercase"><fmt:message key="text.personal_information"/></h2>
                    </div>
                    <div class="col-sm-3">
                        <%@ include file="includes/menu_user_edit.jspf" %>
                    </div>
                    <div class="col-sm-1">
                    </div>
                </div>
                <hr/>
                <div class="row">
                    <div class="col-sm-1">
                    </div>
                    <div class="col-sm-6">
                        <div class="tab-content">
                            <div id="view" class="tab-pane fade${not edit_mode ? ' in active':''}">
                                <%@ include file="includes/block_user_view.jspf" %>
                            </div>
                            <div id="edit" class="tab-pane fade${edit_mode ? ' in active':''}">
                                <c:set var="current_form_action" value="cabinet"/>
                                <c:set var="current_form_command" value="user_save"/>
                                <%@ include file="includes/block_user_edit.jspf" %>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-5">
                    </div>
                </div>
                <h2 class="text-uppercase"><fmt:message key="text.subscriptions"/></h2>
                <hr/>
                <c:if test="${not empty subscriptions_saved and subscriptions_saved.size()>0}">
                    <h3><fmt:message key="text.subscriptions.saved"/> (${subscriptions_saved.size()}):</h3>
                    <div class="row">
                        <div class="col-sm-12">
                            <%@ include file="includes/block_subscriptions_saved.jspf" %>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty subscriptions_active and subscriptions_active.size()>0}">
                    <h3>Active subscriptions (${subscriptions_active.size()}):</h3>
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Periodical</th>
                                    <th>Start</th>
                                    <th>End</th>
                                    <th>Quantity</th>
                                    <th>Cost</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="sub" items="${subscriptions_active}">
                                    <tr>
                                        <td>${sub.periodicalId}</td>
                                        <td>${sub.periodStart}</td>
                                        <td>${sub.periodEnd}</td>
                                        <td>${sub.quantity}</td>
                                        <td>${sub.sum}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty subscriptions_finished and subscriptions_finished.size()>0}">
                    <h3>Finished subscriptions (${subscriptions_finished.size()}):</h3>
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Periodical</th>
                                    <th>Start</th>
                                    <th>End</th>
                                    <th>Quantity</th>
                                    <th>Cost</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="sub" items="${subscriptions_finished}">
                                    <tr>
                                        <td>${sub.periodicalId}</td>
                                        <td>${sub.periodStart}</td>
                                        <td>${sub.periodEnd}</td>
                                        <td>${sub.quantity}</td>
                                        <td>${sub.sum}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>
