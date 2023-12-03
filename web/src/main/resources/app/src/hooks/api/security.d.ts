import * as axios from 'axios';
import { Token } from './models/security';

type LoginApi = {
  login: (
    username: string,
    password: string,
  ) => Promise<axios.AxiosResponse<Token>>;
  loginRefresh: (
    currentRefreshToken: string,
  ) => Promise<axios.AxiosResponse<Token>>;
};

type PasswordResetApi = {
  askPasswordResetThroughEmail: (
    email: string,
    actualPassword: string,
    confirmRedirect: string,
    discardRedirect: string,
  ) => Promise<axios.AxiosResponse<void>>;
};

export type { LoginApi, PasswordResetApi };
