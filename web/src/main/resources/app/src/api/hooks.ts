import axios, { AxiosResponse } from 'axios';
import { Token } from './models.ts';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

class FooBarApi {
  foo(): Promise<AxiosResponse<string>> {
    const url = new URL(`${api.defaults.baseURL}/foo`);
    return api.get(url.toString());
  }

  bar(): Promise<AxiosResponse<string>> {
    const url = new URL(`${api.defaults.baseURL}/bar`);
    return api.get(url.toString());
  }
}

class LoginApi {
  login(username: string, password: string): Promise<AxiosResponse<Token>> {
    const url = new URL(`${api.defaults.baseURL}/login`);

    url.searchParams.set('username', username);
    url.searchParams.set('password', password);

    return api.get(url.toString());
  }

  loginRefresh(currentRefreshToken: string): Promise<AxiosResponse<Token>> {
    const url = new URL(`${api.defaults.baseURL}/login/refresh`);

    url.searchParams.set('refreshToken', currentRefreshToken);

    return api.get(url.toString());
  }
}

const apiInstance = {
  fooBarApi: new FooBarApi(),
  loginApi: new LoginApi(),
};

const useApi = () => {
  return apiInstance;
};

export default useApi;
