package com.genetics.controller;

import com.genetics.common.Result;
import com.genetics.enums.CountryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础数据接口（枚举值等）
 */
@RestController
@RequestMapping("/api/basic")
@RequiredArgsConstructor
public class BasicDataController {

    /**
     * 获取支持的国家列表
     */
    @GetMapping("/countries")
    public Result<List<Map<String, String>>> countries() {
        List<Map<String, String>> list = Arrays.stream(CountryCode.values())
                .map(c -> Map.of(
                        "code", c.getCode(),
                        "nameCn", c.getNameCn(),
                        "nameEn", c.getNameEn()
                ))
                .collect(Collectors.toList());
        return Result.success(list);
    }
}
