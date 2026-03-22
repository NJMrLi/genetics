import request from '@/utils/request'

export function listWorkflowActions() {
  return request({
    url: '/api/workflow/actions/list',
    method: 'get'
  })
}

export function saveWorkflowAction(data) {
  return request({
    url: '/api/workflow/actions',
    method: 'post',
    data
  })
}

export function deleteWorkflowAction(id) {
  return request({
    url: `/api/workflow/actions/${id}`,
    method: 'delete'
  })
}
