/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.codec;

/**
 * XML元素类型
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2024年3月17日
 */
public enum XMLElementType {
	UNKNOWN,
	/** 序言 */
	PROLOG,
	/** 文档类型 */
	DOCTYPE,
	/** 注释 */
	COMMENT,
	/** 常规 */
	NORMAL;
}