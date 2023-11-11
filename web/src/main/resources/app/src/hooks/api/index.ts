import * as axios from 'axios';
import { Token } from './models';

const MAX_RETRY_COUNT = 3;
const PERMIT_ALL_URLS = ['/api/v1/login', '/api/v1/login/refresh'];

const permitAll = (config: axios.InternalAxiosRequestConfig) => {
  return (
    PERMIT_ALL_URLS.find(
      permitAllUrl => config.url?.indexOf(permitAllUrl) != -1,
    ) != null
  );
};

export type FooBarApi = {
  foo: () => Promise<axios.AxiosResponse<string>>;
  bar: () => Promise<axios.AxiosResponse<string>>;
};

export type LoginApi = {
  login: (
    username: string,
    password: string,
  ) => Promise<axios.AxiosResponse<Token>>;
  loginRefresh: (
    currentRefreshToken: string,
  ) => Promise<axios.AxiosResponse<Token>>;
};

export type ApiInstance = {
  fooBarApi: FooBarApi;
  loginApi: LoginApi;
};

const axiosInstance: axios.AxiosInstance = axios.default.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL as string) ?? window.origin,
});

const apiInstance: ApiInstance = {
  fooBarApi: {
    foo: () => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/foo`);
      return axiosInstance.get(url.toString());
    },
    bar: () => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/bar`);
      return axiosInstance.get(url.toString());
    },
  },
  loginApi: {
    login: (username: string, password: string) => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/login`);

      url.searchParams.set('username', username);
      url.searchParams.set('password', password);

      return axiosInstance.get(url.toString());
    },
    loginRefresh: (currentRefreshToken: string) => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/login/refresh`);

      url.searchParams.set('refreshToken', currentRefreshToken);

      return axiosInstance.get(url.toString());
    },
  },
};

axiosInstance.interceptors.request.use(
  config => {
    if (!permitAll(config)) {
      const accessToken = localStorage.accessToken as string;
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    }
    return config;
  },
  async (error: axios.AxiosError) => Promise.reject(error),
);

axiosInstance.interceptors.response.use(
  response => response,
  async (error: axios.AxiosError) => {
    const config = error.config as axios.InternalAxiosRequestConfig & {
      retryCount: number;
    };

    const currentRefreshToken = localStorage.refreshToken as string;
    if (!currentRefreshToken) {
      config.retryCount = config.retryCount ?? 1;
      if (config.retryCount < MAX_RETRY_COUNT) {
        config.retryCount++;
        return axiosInstance(config);
      }
    }

    if (
      !permitAll(config) &&
      currentRefreshToken &&
      error.response?.status === 403
    ) {
      localStorage.accessToken = '';
      localStorage.refreshToken = '';

      return apiInstance.loginApi
        .loginRefresh(currentRefreshToken)
        .then(response => {
          if (response.status != 200) {
            throw new Error(
              `unsucceeded login refresh request result with status: ${response.status}`,
            );
          }
          return response.data;
        })
        .then(data => {
          localStorage.accessToken = data.accessToken;
          localStorage.refreshToken = data.refreshToken;

          config.headers.Authorization = `Bearer ${data.accessToken}`;

          return axiosInstance(config);
        })
        .catch((error: axios.AxiosError) => Promise.reject(error));
    }
    return Promise.reject(error);
  },
);

const useApi = () => apiInstance;

export default useApi;
