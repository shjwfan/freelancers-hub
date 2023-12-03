import * as axios from 'axios';
import { LoginApi, PasswordResetApi } from './security';
import { PacksApi, ThemesApi, WorksApi } from './business';

enum RequestResult {
  Loading = 'Loading',
  Succeeded = 'Succeeded',
  UnSucceeded = 'UnSucceeded',
}

const PERMIT_ALL_URLS = [
  '/api/v1/login',
  '/api/v1/login/refresh',
  '/api/v1/password-reset/email/ask',
];

const permitAll = (config: axios.InternalAxiosRequestConfig) => {
  return (
    PERMIT_ALL_URLS.find(
      permitAllUrl => config.url?.indexOf(permitAllUrl) != -1,
    ) != null
  );
};

const axiosInstance: axios.AxiosInstance = axios.default.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL as string) ?? window.origin,
});

const api: {
  loginApi: LoginApi;
  passwordResetApi: PasswordResetApi;
  packsApi: PacksApi;
  themesApi: ThemesApi;
  worksApi: WorksApi;
} = {
  loginApi: {
    login: (username: string, password: string) => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/login`);

      return axiosInstance.post(url.toString(), { username, password });
    },
    loginRefresh: (currentRefreshToken: string) => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/login/refresh`);

      return axiosInstance.post(url.toString(), { currentRefreshToken });
    },
  },
  passwordResetApi: {
    askPasswordResetThroughEmail: (
      email: string,
      actualPassword: string,
      confirmRedirect: string,
      discardRedirect: string,
    ) => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/password-reset/email/ask`);

      return axiosInstance.post(url.toString(), {
        email,
        actualPassword,
        confirmRedirect,
        discardRedirect,
      });
    },
  },
  packsApi: {
    loadPacks: () => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/packs`);

      return axiosInstance.get(url.toString());
    },
  },
  themesApi: {
    loadThemes: () => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/themes`);

      return axiosInstance.get(url.toString());
    },
  },
  worksApi: {
    loadWorks: () => {
      const baseURL = axiosInstance.defaults.baseURL ?? window.origin;
      const url = new URL(`${baseURL}/api/v1/works`);

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

              api.loginApi
                .loginRefresh(currentRefreshToken)
                .then(response => response.data)
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

const useApi = () => api;

export { RequestResult };
export default useApi;
