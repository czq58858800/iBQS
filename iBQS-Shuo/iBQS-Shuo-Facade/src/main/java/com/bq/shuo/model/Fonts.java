package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_fonts")
@SuppressWarnings("serial")
public class Fonts extends BaseModel {

    /**
     * 封面
     */
	@TableField("cover_")
	private String cover;
    /**
     * 编号
     */
	@TableField("code_")
	private String code;
    /**
     * 字体名称
     */
	@TableField("name_")
	private String name;
    /**
     * 字体文件
     */
	@TableField("font_")
	private String font;
    /**
     * 语言
     */
	@TableField("lang_")
	private String lang;


	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}