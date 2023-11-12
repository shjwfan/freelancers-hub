import * as axios from 'axios';
import { Token } from './models';

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

const withAccessToken = (config: axios.InternalAxiosRequestConfig) => {
  const accessToken = localStorage.accessToken as string;
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
};

axiosInstance.interceptors.request.use(
  config => {
    if (!permitAll(config)) {
      withAccessToken(config);
    }
    return config;
  },
  async (error: axios.AxiosError) => Promise.reject(error),
);

let refreshPromise: Promise<axios.InternalAxiosRequestConfig> | null = null;

axiosInstance.interceptors.response.use(
  response => response,
  async (error: axios.AxiosError) => {
    const config = error.config as axios.InternalAxiosRequestConfig;

    if (!permitAll(config) && error.response?.status == 403) {
      if (!refreshPromise) {
        const currentRefreshToken = localStorage.refreshToken as string;
        if (currentRefreshToken) {
          refreshPromise = new Promise<axios.InternalAxiosRequestConfig>(
            (resolve, reject) => {
              localStorage.accessToken = '';
              localStorage.refreshToken = '';

              apiInstance.loginApi
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

                  resolve(config);
                })
                .catch(error => reject(error))
                .finally(() => (refreshPromise = null));
            },
          );

          return refreshPromise
            .then(config => axiosInstance(withAccessToken(config)))
            .catch(error => Promise.reject(error));
        }
      } else {
        return refreshPromise
          .then(config => axiosInstance(withAccessToken(config)))
          .catch(error => Promise.reject(error));
      }
    }

    return Promise.reject(error);
  },
);

const useApi = () => apiInstance;

export default useApi;
