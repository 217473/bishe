import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
    AxiosResponse,
  AxiosRequestConfig
} from 'axios';

import { ElMessage } from 'element-plus';
//axios的配置
const config = {
  baseURL: 'http://localhost:8089', //请求接口的地址
  // baseUrl:'/api',
  timeout: 10000, //请求超时时间
};
//定义返回值类型
export interface Result<T = any> {
  code: number;
  msg: string;
  data: T;
}
class Http {
  //axios实例
  private instance: AxiosInstance;
  //初始化的作用
  constructor(configs: AxiosRequestConfig) {
    //创建axios实例
    this.instance = axios.create(configs);
    //配置拦截器
    this.interceptors();
  }
  //请求发送、返回之后做处理
  private interceptors() {
    //请求发送之前的处理：请求头携带的token
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        //获取token
        let token = '';
        if (token && config) {
          config.headers!['token'] = token;
        }
        return config;
      }
    ),
      (error: any) => {
        error.data = {};
        error.data.msg = '服务器异常请联系管理员';
        return error;
      };
    //请求返回之后的处理
    this.instance.interceptors.response.use((res: AxiosResponse) => {
      if (res.data.code === 200) {
        return res.data;
      } else {
        ElMessage.error(res.data.msg || '接口报错');
        return Promise.reject(res.data.msg || '接口报错');
      }
    }),
      (error: any) => {
        console.log('进入错误');
        error.data = {};
        if (error && error.response) {
          switch (error.response.status) {
            case 400:
              error.data.msg = '错误请求';
              ElMessage.error(error.data.msg);
              break;
            case 401:
              error.data.msg = '未授权，请重新登录';
              ElMessage.error(error.data.msg);
              break;
            case 403:
              error.data.msg = '拒绝访问';
              ElMessage.error(error.data.msg);
              break;
            case 404:
              error.data.msg = '请求错误,未找到该资源';
              ElMessage.error(error.data.msg);
              break;
            case 405:
              error.data.msg = '请求方法未允许';
              ElMessage.error(error.data.msg);
              break;
            case 408:
              error.data.msg = '请求超时';
              ElMessage.error(error.data.msg);
              break;
            case 500:
              error.data.msg = '后端接口出错';
              ElMessage.error(error.data.msg);
              break;
            case 501:
              error.data.msg = '网络未实现';
              ElMessage.error(error.data.msg);
              break;
            case 502:
              error.data.msg = '网络错误';
              ElMessage.error(error.data.msg);
              break;
            case 503:
              error.data.msg = '服务不可用';
              ElMessage.error(error.data.msg);
              break;
            case 504:
              error.data.msg = '网络超时';
              ElMessage.error(error.data.msg);
              break;
            case 505:
              error.data.msg = 'http版本不支持该请求';
              ElMessage.error(error.data.msg);
              break;
            default:
              error.data.msg = `连接错误${error.response.status}`;
              ElMessage.error(error.data.msg);
          }
        } else {
          error.data.msg = '连接到服务器失败';
          ElMessage.error(error.data.msg);
        }
        return Promise.reject(error);
      };
  }
  //post请求
  post<T = Result>(url: string, data?: object): Promise<T> {
    return this.instance.post(url, data);
  }
  //put请求
  put<T = Result>(url: string, data?: object): Promise<T> {
    return this.instance.put(url, data);
  }
  //get请求
  get<T = Result>(url: string, params?: object): Promise<T> {
    return this.instance.get(url, { params });
  }
  ///delete请求
  delete<T = Result>(url: string): Promise<T> {
    return this.instance.delete(url);
  }
  //图片上传
  upload<T = Result>(url: string, params?: object): Promise<T> {
    return this.instance.post(url, params, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  }
}
export default new Http(config);
