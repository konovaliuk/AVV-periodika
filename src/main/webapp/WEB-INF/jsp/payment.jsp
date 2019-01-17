<%@ include file="includes/common.jspf" %>
<c:set var="path_to_images" value="../../images/periodicals/"/>
<c:set var="payment" value="${temp_payment_info}"/>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.payment"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid bg-grey">
    <div class="row content">
        <div class="col-sm-12 text-left">
            <%@ include file="includes/display_message.jspf" %>
            <div class="container-fluid">
                <h1 class="text-uppercase"><fmt:message key="text.cabinet.payment_information"/></h1>
                <hr/>
                <div class="row">
                    <div class="col-sm-3 text-right">
                        <fmt:message key="label.payment.date"/>
                    </div>
                    <div class="col-sm-9">
                        <strong id="localDate"></strong> (${payment.date})
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3 text-right">
                        <fmt:message key="label.payment.sum"/>
                    </div>
                    <div class="col-sm-9">
                        <span class="sum">${payment.sum}</span> <fmt:message key="currency.uah"/>
                    </div>
                </div>
                <hr/>
                <c:set var="subscriptions_list" value="${payment.subscriptions}"/>
                <h3><fmt:message key="text.subscriptions"/> (${subscriptions_list.size()}):</h3>
                <div class="row">
                    <div class="col-sm-12">
                        <%@ include file="includes/list_subscriptions_view.jspf" %>
                    </div>
                </div>
                <c:if test="${edit_mode and (empty payment.id) and (empty paymentId)}">
                    <form class="form" method="post">
                        <button type="submit" name="command" value="payment_save"
                                class="btn btn-success btn-lg"><fmt:message key="button.payment_checkout"/>
                        </button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
<script>
    function convertUTCDateToLocalDate(date) {
        return new Date(date).toLocaleString();
    }

    $(document).ready(function () {
        $("#localDate").text(convertUTCDateToLocalDate('${payment.date}'));
    })
</script>
</html>
