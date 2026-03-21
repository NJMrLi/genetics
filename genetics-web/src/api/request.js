import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.response.use(
  response => {
    const data = response.data
    if (data.code !== 0) {
      console.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message))
    }
    return data
  },
  error => {
    console.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
