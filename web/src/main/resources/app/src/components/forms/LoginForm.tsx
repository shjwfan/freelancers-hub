import { useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { RequestResult } from '../../hooks/api/models';
import useApi from '../../hooks/api';

type Credentials = {
  username: string;
  password: string;
};

const PATTERN = /^[a-zA-Z0-9!@#$%^&*()_+{}[\]:;<>,.?~|\\/-]{4,32}$/g;

const LoginForm = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Credentials>();
  const loginApi = useApi().loginApi;

  const onSubmit: SubmitHandler<Credentials> = (credentials: Credentials) => {
    setRequestResult(RequestResult.Progress);

    localStorage.accessToken = '';
    localStorage.refreshToken = '';

    loginApi
      .login(credentials.username, credentials.password)
      .then(response => {
        if (response.status != 200) {
          throw new Error(
            `unsucceeded request result with status: ${response.status}`,
          );
        }
        return response.data;
      })
      .then(data => {
        setRequestResult(RequestResult.Succeeded);

        localStorage.accessToken = data.accessToken;
        localStorage.refreshToken = data.refreshToken;
      })
      .catch(error => {
        setRequestResult(RequestResult.UnSucceeded);

        console.debug(error);
      })
      .finally(() => setTimeout(() => setRequestResult(null), 10 * 1000));
  };

  return (
    <form onSubmit={e => void handleSubmit(onSubmit)(e)}>
      <div className='row'>
        <div className='col-md-3 m-auto mt-5 text-center'>
          <h2>Login</h2>
          <span className='text-secondary'>
            No account yet?{' '}
            <a className='link-offset-1' href='#'>
              Create one
            </a>
          </span>
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
                pattern: PATTERN,
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
                pattern: PATTERN,
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
          {requestResult == RequestResult.Progress && (
            /* warning alert */
            <div className='alert alert-warning mt-3' role='alert'>
              Loading...
            </div>
          )}
          {requestResult == RequestResult.Succeeded && (
            /* success alert */
            <div className='alert alert-success mt-3' role='alert'>
              Login succeeded.
            </div>
          )}
          {requestResult == RequestResult.UnSucceeded && (
            /* danger alert */
            <div className='alert alert-danger mt-3' role='alert'>
              Login unsucceeded.
            </div>
          )}
        </div>
      </div>
    </form>
  );
};

export default LoginForm;
