import request from './request'

export const createInstance = (data) => request.post('/form-instance/create', data)
export const saveInstance = (id, data) => request.put(`/form-instance/${id}/save`, data)

/**
 * 统一动作执行接口
 * @param {number} id - 服务单实例ID
 * @param {object} data - 动作执行数据
 * @param {string} data.action - 动作编码 (如: submit, auditPass, auditReject)
 * @param {string} [data.remark] - 备注
 * @param {object} [data.actionFormData] - 动作表单数据
 */
export const executeAction = (id, data) => request.post(`/form-instance/${id}/execute`, data)

export const getInstance = (id) => request.get(`/form-instance/${id}`)
export const listInstances = (params) => request.get('/form-instance/list', { params })
export const getOrderStatusOptions = () => request.get('/form-instance/order-status/options')

// 工作流相关接口
export const getAvailableActions = (id) => request.get(`/form-instance/${id}/available-actions`)
