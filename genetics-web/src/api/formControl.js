import request from './request'

export const createControl = (data) => request.post('/form-control', data)
export const updateControl = (id, data) => request.put(`/form-control/${id}`, data)
export const deleteControl = (id) => request.delete(`/form-control/${id}`)
export const getControl = (id) => request.get(`/form-control/${id}`)
export const listControls = (params) => request.get('/form-control/list', { params })
export const listAllControls = () => request.get('/form-control/all')
