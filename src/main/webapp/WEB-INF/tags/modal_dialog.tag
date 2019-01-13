<%@ include file="../jsp/includes/common_localization.jspf" %>
<%@ attribute name="modalId" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="command" required="true" %>
<%@ attribute name="commandName" required="true" %>
<%@ attribute name="additionalControls" fragment="true" %>
<div class="modal fade" id="${modalId}" role="dialog">
    <div class="modal-dialog modal-sm">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4><span class="glyphicon glyphicon-question-sign">
                                </span> ${title}</h4>
            </div>
            <div class="modal-footer">
                <form method="post" action="execute">
                    <jsp:invoke fragment="additionalControls"/>
                    <button type="submit" name="command" value="${command}"
                            class="btn btn-success pull-left">
                        <span class="glyphicon glyphicon-trash">
                        </span> ${commandName}
                    </button>
                    <button type="submit" class="btn btn-danger btn-default"
                            data-dismiss="modal">
                        <span class="glyphicon glyphicon-remove"></span> <fmt:message key="button.cancel"/>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
