<%@ include file="includes/common.jspf" %>
<c:set var="path_to_images" value="../../images/periodicals/"/>
<c:set var="sub" value="${temp_subscription_info}"/>
<!DOCTYPE html>
<html lang="${language_code}">
<head>
    <title><fmt:message key="title.site"/> - <fmt:message key="title.subscription"/></title>
    <%@ include file="includes/common_head.jspf" %>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<%@ include file="includes/header.jspf" %>
<div class="container-fluid bg-grey">
    <div class="row content">
        <div class="col-sm-12 text-left">
            <%@ include file="includes/display_message.jspf" %>
            <div class="container-fluid">
                <h1 class="text-uppercase"><c:out value="${sub.periodical.title}"/></h1>
                <hr/>
                <div class="row">
                    <div class="col-sm-3">
                        <div class="thumbnail">
                            <a href="periodical?periodicalId=${sub.periodicalId}">
                                <img class="img-responsive"
                                     src="${path_to_images}<tags:image_file id="${sub.periodicalId}"/>"
                                     alt="${sub.periodical.title}" style="width:100%"/>
                            </a>
                        </div>
                    </div>
                    <div class="col-sm-9">
                        <p><c:out value="${sub.periodical.description}"/></p>
                        <p><strong><fmt:message key="label.periodical.periodicity"/> </strong>
                            ${sub.periodical.issuesPerPeriod} <fmt:message key="text.periodical.issues"/> /
                            ${sub.periodical.minSubscriptionPeriod} <fmt:message key="text.periodical.months"/>
                        </p>
                        <hr/>
                        <%@ include file="includes/block_price_calculator.jspf" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="includes/footer.jspf" %>
</body>
</html>
