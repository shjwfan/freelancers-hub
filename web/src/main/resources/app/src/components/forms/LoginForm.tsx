import { useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import useApi, { RequestResult } from '../../hooks/api';
import * as axios from 'axios';
import Alert from '../Alert.tsx';
import { PASSWORD_PATTERN, USERNAME_PATTERN } from './utils.ts';

type Credentials = {
  username: string;
  password: string;
};

const LoginForm = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const [error, setError] = useState<number | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Credentials>();
  const loginApi = useApi().loginApi;

  const onSubmit: SubmitHandler<Credentials> = (credentials: Credentials) => {
    setRequestResult(RequestResult.Loading);

    localStorage.accessToken = '';
    localStorage.refreshToken = '';

    loginApi
      .login(credentials.username, credentials.password)
      .then(response => response.data)
      .then(data => {
        setRequestResult(RequestResult.Succeeded);

        localStorage.accessToken = data.accessToken;
        localStorage.refreshToken = data.refreshToken;
      })
      .catch((error: axios.AxiosError) => {
        setError(error.response?.status ?? null);
        setRequestResult(RequestResult.UnSucceeded);
      })
      .finally(() =>
        setTimeout(() => {
          setError(null);
          setRequestResult(null);
        }, 10 * 1000),
      );
  };

  return (
    <form onSubmit={e => void handleSubmit(onSubmit)(e)}>
      <div className='row'>
        <div className='col-md-3 m-auto mt-5 text-center'>
          <h2 className='permanent-marker'>Login</h2>
        </div>
      </div>
      <div className='row'>
        <div className='col-md-3 m-auto'>
          <div className='form-group mt-5'>
            <label
              className='form-label text-secondary'
              htmlFor='username-input'
            >
              Username:
            </label>
            <input
              className={`form-control ${errors.username ? 'is-invalid' : ''}`}
              id='username-input'
              placeholder='Enter username'
              {...register('username', {
                required: true,
                pattern: USERNAME_PATTERN,
              })}
            />
            {errors.username && (
              <div className={'invalid-feedback'}>
                Username is required, must consist of only English alphabet
                and/or numbers and/or symbols and be between 4 and 32
                characters.
              </div>
            )}
          </div>
          <div className='form-group mt-5'>
            <label
              className='form-label text-secondary'
              htmlFor='password-input'
            >
              Password:
            </label>
            <input
              className={`form-control ${errors.password ? 'is-invalid' : ''}`}
              id='password-input'
              placeholder='Enter password'
              type='password'
              {...register('password', {
                required: true,
                pattern: PASSWORD_PATTERN,
              })}
            />
            {errors.password && (
              <div className={'invalid-feedback'}>
                Password is required, must consist of only English alphabet
                and/or numbers and/or symbols and be between 4 and 32
                characters.
              </div>
            )}
          </div>
          <button className='btn btn-primary mt-3' type='submit'>
            Login
          </button>
          {requestResult && (
            <Alert requestResult={requestResult}>
              {requestResult === RequestResult.Loading && !error && (
                <span>Loading...</span>
              )}
              {requestResult === RequestResult.Succeeded && !error && (
                <span>Login succeeded.</span>
              )}
              {requestResult === RequestResult.UnSucceeded && error === 403 && (
                <span>Login unsucceeded. Bad credentials.</span>
              )}
            </Alert>
          )}
        </div>
      </div>
    </form>
  );
};

export default LoginForm;
