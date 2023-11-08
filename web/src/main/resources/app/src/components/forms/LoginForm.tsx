import { useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import axios from 'axios';

type Credentials = {
  username: string;
  password: string;
};

// api namespace?
type Token = {
  accessToken: string;
  refreshToken: string;
};

const PATTERN = /^[a-zA-Z0-9!@#$%^&*()_+{}[\]:;<>,.?~|\\/-]{4,32}$/g;

const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Credentials>();
  const [requestResult, setRequestResult] = useState<api.RequestResult | null>(
    null,
  );
  const onSubmit: SubmitHandler<Credentials> = (credentials: Credentials) => {
    const url = new URL('/api/v1/login', window.origin);
    url.searchParams.set('username', credentials.username);
    url.searchParams.set('password', credentials.password);

    setRequestResult(api.RequestResult.Progress);
    axios
      .get<Token>(url.toString())
      .then(response => {
        if (response.status != 200) {
          throw new Error(`unsucceeded request result with status: ${status}`);
        }
        return response.data;
      })
      .then(data => {
        setRequestResult(api.RequestResult.Succeeded);
        setTimeout(() => setRequestResult(null), 10 * 1000);
      })
      .catch(error => {
        setRequestResult(api.RequestResult.UnSucceeded);
        setTimeout(() => setRequestResult(null), 10 * 1000);
        console.error(error);
      });
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
          {requestResult == api.RequestResult.Progress && (
            <div
              className='col-md-3 m-auto mt-3 alert alert-warning'
              role='alert'
            >
              Loading...
            </div>
          )}
          {requestResult == api.RequestResult.Succeeded && (
            <div
              className='col-md-3 m-auto mt-3 alert alert-success'
              role='alert'
            >
              Login succeeded.
            </div>
          )}
          {requestResult == api.RequestResult.UnSucceeded && (
            <div
              className='col-md-3 m-auto mt-3 alert alert-danger'
              role='alert'
            >
              Login unsucceeded.
            </div>
          )}
        </div>
      </div>
    </form>
  );
};

export default LoginForm;
