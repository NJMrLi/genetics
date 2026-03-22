import request from './request'

export function listWorkflowActions() {
  return request({
    url: '/workflow/actions/list',
    method: 'get'
  })
}

export function saveWorkflowAction(data) {
  return request({
    url: '/workflow/actions',
    method: 'post',
    data
  })
}

export function deleteWorkflowAction(id) {
  return request({
    url: `/workflow/actions/${id}`,
    method: 'delete'
  })
}
