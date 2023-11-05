import { SubmitHandler, useForm } from 'react-hook-form';
import axios from 'axios';

type Credentials = {
  username: string;
  password: string;
};

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
  const onSubmit: SubmitHandler<Credentials> = (credentials: Credentials) => {
    const url = new URL('/api/v1/login', window.origin);
    url.searchParams.set('username', credentials.username);
    url.searchParams.set('password', credentials.password);

    axios
      .get<Token>(url.toString())
      .then(response => response.data)
      .then(data => console.log(data))
      .catch(error => console.error(error));
  };

  return (
    <form onSubmit={e => void handleSubmit(onSubmit)(e)}>
      <div className='row'>
        <div className='col-3 m-auto mt-5 text-center'>
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
        <div className='col-3 m-auto'>
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
                Username is required, must consist of only English alphabet
                and/or numbers and/or symbols and be between 4 and 32
                characters.
              </div>
            )}
          </div>
          <button className='btn btn-primary mt-3' type='submit'>
            Login
          </button>
        </div>
      </div>
    </form>
  );
};

export default LoginForm;
