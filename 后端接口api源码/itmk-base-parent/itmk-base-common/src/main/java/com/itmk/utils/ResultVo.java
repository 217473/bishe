package com.itmk.utils;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回值类型
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ResultVo<T> {
    private String msg;
    private int code;
    private T data;
}
