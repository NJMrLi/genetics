package com.genetics.common.constant;

import org.apache.commons.lang3.StringUtils;

public class ServeStateOptRecordDescription {

    public static final String SERVER_LIST_GENERATE_PROCESS_NAME = "生成服务单";


    /**
     * 记录待审核状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getAuitingOperationLog(String customString) {
        return "您已提交资料，正在为您审核中";
    }

    /**
     * 记录待递交状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getHandingOperationLog(String customString) {
        if (StringUtils.isNotBlank(customString)) {
            return "审核成功:" + customString;
        }
        return "审核成功，请耐心等候!";
    }

    /**
     * 记录组织审核中状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getOrgnHandingOperationLog(String customString) {
        if (StringUtils.isNotBlank(customString)) {
            return "[组织审核中] " + customString;
        }
        return "所有材料已递交组织，请耐心等待";
    }

    /**
     * 记录税局处理状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getTaxHandingOperationLog(String customString) {
        return "[税局处理] " + customString;
    }

    /**
     * 记录已完成状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getSuccessOperationLog(String customString) {
        if (StringUtils.isNotBlank(customString)) {
            return "已完成:" + customString;
        }
        return "服务已完成！";
    }

    /**
     * 记录已驳回状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getBackedOperationLog(String customString) {
        if(StringUtils.isBlank(customString)){
            return "申请已驳回，原因请见“服务单详情 → 审核意见”。";
        }
        return "审核意见: " + customString;
    }

    /**
     * 记录已终止状态的操作日志
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getCancledOperationLog(String customString) {
        return "流程已终止，终止原因: " + customString;
    }


    /**
     * 记录待提交
     *
     * @param customString 自定义日志信息
     * @return 拼接后的操作日志
     */
    public static String getGenerateServListOperationLog(String customString) {
        return "服务单已生成，请您提交相关资料" + customString;
    }


}
