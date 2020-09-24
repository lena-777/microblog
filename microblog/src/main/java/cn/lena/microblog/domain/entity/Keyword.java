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
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Keyword extends Model<Keyword> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "keywordId", type = IdType.AUTO)
    private Integer keywordId;

    @TableField("keywordName")
    private String keywordName;

    @TableField("passageNum")
    private Integer passageNum;

    @TableLogic
    private Integer deleted;

    @Override
    protected Serializable pkVal() {
        return this.keywordId;
    }

}
