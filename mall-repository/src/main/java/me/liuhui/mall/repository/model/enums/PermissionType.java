package me.liuhui.mall.repository.model.enums;

import lombok.Getter;

/**
 * Created on 2020/10/14 19:36
 * <p>
 * Description: [TODO]
 * <p>
 * Company: []
 *
 * @author [清远]
 */
@Getter
public enum PermissionType {
    /**
     *
     */
    MENU(1, "菜单"), BUTTON(2, "按钮");
    private final Integer code;
    private final String desc;


    PermissionType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
