package cn.lena.microblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Catagory extends Model<Catagory> {

    private static final long serialVersionUID = 1L;

    @TableField("userId")
    private Integer userId;

    @TableField("catagoryId")
    private Integer catagoryId;

    @TableField("catagoryName")
    private String catagoryName;

    @TableField("passageNum")
    private Integer passageNum;

    @TableField("isDisplay")
    private Integer isDisplay;

    @TableField("isTop")
    private Integer isTop;

    @TableLogic
    private Integer deleted;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
