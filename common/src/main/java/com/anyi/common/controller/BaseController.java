package com.anyi.common.controller;

import com.anyi.common.result.ResponseDTO;

/**
 * web层通用数据处理
 *
 * @author Jianpan
 */
public class BaseController {


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResponseDTO<?> response(int rows) {
        return rows > 0 ? ResponseDTO.success() : ResponseDTO.ofError();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected ResponseDTO<?> response(boolean result) {
        return result ? ResponseDTO.success() : ResponseDTO.ofError();
    }


}
