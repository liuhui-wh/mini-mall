package me.liuhui.mall.repository.model.enums;

import lombok.Getter;

/**
 * Created on 2020/10/27 17:28
 * <p>
 * Description: [TODO]
 * <p>
 * Company: []
 *
 * @author [清远]
 */
@Getter
public enum UserStatus {
    /**
     *
     */
    NORMAL(1, "正常"), FROZEN(2, "冻结");
    private final Integer code;
    private final String desc;


    UserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
