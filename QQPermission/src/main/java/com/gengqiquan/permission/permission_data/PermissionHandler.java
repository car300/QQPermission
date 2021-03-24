package com.gengqiquan.permission.permission_data;

import java.util.HashMap;

/**
 * 添加类注释
 *
 * @author panfei.pf
 * @since 2021/3/18 11:17
 */
public class PermissionHandler {

    private static final HashMap<String, String> permissionMap = new HashMap<>();

    public static String getPermissionDescription(String permission) {
        if (permissionMap.isEmpty()) {
            needHandle();
        }
        String desc = permissionMap.get(permission);
        if (desc == null || desc.isEmpty()) {
            desc = "相关的权限";
        }
        return desc;
    }

    /**
     * 填充数据
     */
    private static void needHandle() {

        //位置
        insert2Map(Permission.Group.LOCATION, Constant.DESC_LOCATION);
        //相机
        insert2Map(Permission.Group.CAMERA, Constant.DESC_CAMERA);
        // 麦克风
        insert2Map(Permission.Group.MICROPHONE, Constant.DESC_MICROPHONE);
        // 存储
        insert2Map(Permission.Group.STORAGE, Constant.DESC_STORAGE);
        // 联系人
        insert2Map(Permission.Group.CONTACTS, Constant.DESC_CONTACT);
        // 电话
        insert2Map(Permission.Group.PHONE, Constant.DESC_PHONE);

    }

    /**
     * 具体填充方法
     *
     * @param group
     * @param value
     */
    private static void insert2Map(String[] group, String value) {
        for (String s : group) {
            permissionMap.put(s, value);
        }
    }

}
