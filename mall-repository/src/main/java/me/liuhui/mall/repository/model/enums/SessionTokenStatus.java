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
public enum SessionTokenStatus {
    /**
     *
     */
    VALID(1, "有效"), INVALID(2, "无效");
    private final Integer code;
    private final String desc;


    SessionTokenStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
