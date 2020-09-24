package cn.lena.microblog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PassageCatagory extends Model<PassageCatagory> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;


    @TableField("userId")
    private Integer userId;

    @TableField("catagoryId")
    private Integer catagoryId;

    @TableField("passageId")
    private Integer passageId;

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
