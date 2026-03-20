import request from './request'

export const createTemplate = (data) => request.post('/form-template', data)
export const updateTemplate = (id, data) => request.put(`/form-template/${id}`, data)
export const publishTemplate = (id) => request.post(`/form-template/${id}/publish`)
export const deleteTemplate = (id) => request.delete(`/form-template/${id}`)
export const getTemplate = (id) => request.get(`/form-template/${id}`)
export const listTemplates = (params) => request.get('/form-template/list', { params })
