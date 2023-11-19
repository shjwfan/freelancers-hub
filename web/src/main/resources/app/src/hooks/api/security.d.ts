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

export type { LoginApi };
