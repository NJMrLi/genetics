import request from './request'

export const createInstance = (data) => request.post('/form-instance/create', data)
export const saveInstance = (id, data) => request.put(`/form-instance/${id}/save`, data)
export const submitInstance = (id) => request.post(`/form-instance/${id}/submit`)
export const updateOrderStatus = (id, orderStatusId) =>
  request.put(`/form-instance/${id}/order-status`, null, { params: { orderStatusId } })
export const getInstance = (id) => request.get(`/form-instance/${id}`)
export const listInstances = (params) => request.get('/form-instance/list', { params })
export const getOrderStatusOptions = () => request.get('/form-instance/order-status/options')

// 工作流相关接口
export const getAvailableActions = (id) => request.get(`/form-instance/${id}/available-actions`)
export const executeTransition = (id, data) =>
  request.post(`/form-instance/${id}/transition`, data)
