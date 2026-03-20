import request from './request'

export const getCountries = () => request.get('/basic/countries')
