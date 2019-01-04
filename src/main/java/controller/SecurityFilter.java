package controller;

import common.LoggerLoader;
import model.UserInfo;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_ADMIN_ID;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;
import static common.ViewConstants.ATTR_NAME_USER;

public class SecurityFilter {
    private static final Logger LOGGER = LoggerLoader.getLogger(SecurityFilter.class);
    private static final Map<UserTypeEnum, Set<CommandsEnum>> USER_COMMANDS_MAP;

    static {
        Map<UserTypeEnum, Set<CommandsEnum>> aMap = new HashMap<>();
        EnumSet<CommandsEnum> aSet =
                EnumSet.of(CommandsEnum.EMPTY,
                           CommandsEnum.NAV_JSP,
                           CommandsEnum.NAV_MAIN,
                           CommandsEnum.NAV_CATALOG,
                           CommandsEnum.NAV_PERIODICAL,
                           CommandsEnum.LOGIN,
                           CommandsEnum.REGISTER);
        aMap.put(UserTypeEnum.GUEST, Collections.unmodifiableSet(aSet));
        aSet = EnumSet.of(CommandsEnum.EMPTY,
                          CommandsEnum.NAV_MAIN,
                          CommandsEnum.NAV_CATALOG,
                          CommandsEnum.NAV_PERIODICAL,
                          CommandsEnum.LOGOUT);
        aMap.put(UserTypeEnum.CLIENT, Collections.unmodifiableSet(aSet));
        aSet = EnumSet.copyOf(aSet);
        aSet.add(CommandsEnum.CATEGORY_NEW);
        aSet.add(CommandsEnum.CATEGORY_SAVE);
        aSet.add(CommandsEnum.CATEGORY_DELETE);
        aSet.add(CommandsEnum.PERIODICAL_NEW);
        aSet.add(CommandsEnum.PERIODICAL_SAVE);
        aSet.add(CommandsEnum.PERIODICAL_DELETE);
        aMap.put(UserTypeEnum.ADMIN, Collections.unmodifiableSet(aSet));
        USER_COMMANDS_MAP = Collections.unmodifiableMap(aMap);
    }

    public static Command validateCommandRequest(SessionRequestContent context, CommandsEnum command) {
        UserTypeEnum userType = UserTypeEnum.GUEST;
        if (context.existSessionAttribute(ATTR_NAME_USER)) {
            UserInfo user = (UserInfo) context.getSessionAttribute(ATTR_NAME_USER);
            if (user != null && user.getUserTypeId() != null) {
                if (RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID) == user.getUserTypeId()) {
                    userType = UserTypeEnum.CLIENT;
                }
                if (RM_DAO_USER_TYPE.getLong(USER_TYPE_ADMIN_ID) == user.getUserTypeId()) {
                    userType = UserTypeEnum.ADMIN;
                }
            }
        }
        if (USER_COMMANDS_MAP.containsKey(userType) && USER_COMMANDS_MAP.get(userType).contains(command)) {
            return command.getCommand();
        }
        return CommandsEnum.EMPTY.getCommand();
    }

    private enum UserTypeEnum {
        GUEST,
        CLIENT,
        ADMIN
    }


}
