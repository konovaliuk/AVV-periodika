package controller;

import common.LoggerLoader;
import model.User;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_ADMIN_ID;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;

public class CommandSecurity {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandSecurity.class);
    private static final Map<UserTypeEnum, Set<CommandEnum>> USER_COMMANDS_MAP;

    static {
        Map<UserTypeEnum, Set<CommandEnum>> aMap = new HashMap<>();
        EnumSet<CommandEnum> aSet =
                EnumSet.of(CommandEnum.EMPTY,
                           CommandEnum.LANGUAGE,
                           CommandEnum.NAV_JSP,
                           CommandEnum.NAV_MAIN,
                           CommandEnum.NAV_CATALOG,
                           CommandEnum.NAV_PERIODICAL,
                           CommandEnum.LOGIN,
                           CommandEnum.REGISTER);
        aMap.put(UserTypeEnum.GUEST, Collections.unmodifiableSet(aSet));
        aSet = EnumSet.of(CommandEnum.EMPTY,
                          CommandEnum.LANGUAGE,
                          CommandEnum.NAV_MAIN,
                          CommandEnum.USER_SAVE,
                          CommandEnum.NAV_CATALOG,
                          CommandEnum.NAV_PERIODICAL,
                          CommandEnum.NAV_CABINET,
                          CommandEnum.NAV_SUBSCRIPTION,
                          CommandEnum.SUBSCRIPTION_NEW,
                          CommandEnum.SUBSCRIPTION_SAVE,
                          CommandEnum.SUBSCRIPTION_DELETE,
                          CommandEnum.LOGOUT);
        aMap.put(UserTypeEnum.CLIENT, Collections.unmodifiableSet(aSet));
        aSet = EnumSet.copyOf(aSet);
        aSet.add(CommandEnum.CATEGORY_NEW);
        aSet.add(CommandEnum.CATEGORY_SAVE);
        aSet.add(CommandEnum.CATEGORY_DELETE);
        aSet.add(CommandEnum.PERIODICAL_NEW);
        aSet.add(CommandEnum.PERIODICAL_SAVE);
        aSet.add(CommandEnum.PERIODICAL_DELETE);
        aMap.put(UserTypeEnum.ADMIN, Collections.unmodifiableSet(aSet));
        USER_COMMANDS_MAP = Collections.unmodifiableMap(aMap);
    }

    public static Command validateCommandRequest(SessionRequestContent context, CommandEnum command) {
        UserTypeEnum userType = UserTypeEnum.GUEST;
        User user = context.getCurrentUser();
        if (user != null && user.getUserTypeId() != null) {
            if (RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID) == user.getUserTypeId()) {
                userType = UserTypeEnum.CLIENT;
            }
            if (RM_DAO_USER_TYPE.getLong(USER_TYPE_ADMIN_ID) == user.getUserTypeId()) {
                userType = UserTypeEnum.ADMIN;
            }
        }
        if (USER_COMMANDS_MAP.containsKey(userType) && USER_COMMANDS_MAP.get(userType).contains(command)) {
            return command.getCommand();
        }
        return CommandEnum.EMPTY.getCommand();
    }

    private enum UserTypeEnum {
        GUEST,
        CLIENT,
        ADMIN
    }


}
