const LoginForm = () => {
  return (
    <>
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
              className='form-control'
              id='username-input'
              placeholder='Enter username'
            />
          </div>
          <div className='form-group mt-5'>
            <label
              className='form-label text-secondary'
              htmlFor='password-input'
            >
              Password:
            </label>
            <input
              className='form-control'
              id='password-input'
              placeholder='Enter password'
              type='password'
            />
          </div>
          <button className='btn btn-primary mt-3' type='submit'>
            Login
          </button>
        </div>
      </div>
    </>
  );
};

export default LoginForm;
