import { useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import useApi, { RequestResult } from '../../../hooks/api';
import * as axios from 'axios';
import Alert from '../../Alert.tsx';
import { EMAIL_PATTERN, PASSWORD_PATTERN } from '../utils.ts';

const AskPasswordResetThroughEmailForm = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const [error, setError] = useState<number | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors },
    getValues,
  } = useForm<{
    email: string;
    actualPassword: string;
    actualPasswordRepeat: string;
  }>();
  const passwordResetApi = useApi().passwordResetApi;

  const onSubmit: SubmitHandler<{
    email: string;
    actualPassword: string;
    actualPasswordRepeat: string;
  }> = ({ email, actualPassword }) => {
    setRequestResult(RequestResult.Loading);

    passwordResetApi
      .askPasswordResetThroughEmail(
        email,
        actualPassword,
        '/app/password-reset/email/confirm',
        '/app/password-reset/email/discard',
      )
      .then(response => response.data)
      .then(() => {
        setError(null);
        setRequestResult(RequestResult.Succeeded);
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
          <h2 className='permanent-marker'>Reset password</h2>
        </div>
      </div>
      <div className='row'>
        <div className='col-md-3 m-auto'>
          <div className='form-group mt-5'>
            <label className='form-label text-secondary' htmlFor='email-input'>
              Email:
            </label>
            <input
              className={`form-control ${errors.email ? 'is-invalid' : ''}`}
              id='email-input'
              placeholder='Enter email'
              {...register('email', {
                required: true,
                pattern: EMAIL_PATTERN,
              })}
            />
            {errors.email && (
              <div className={'invalid-feedback'}>Mail is invalid.</div>
            )}
          </div>
          <div className='form-group mt-5'>
            <label
              className='form-label text-secondary'
              htmlFor='actual-password-input'
            >
              Actual Password:
            </label>
            <input
              className={`form-control ${
                errors.actualPassword ? 'is-invalid' : ''
              }`}
              id='actual-password-input'
              placeholder='Enter actual password'
              type='password'
              {...register('actualPassword', {
                required: true,
                pattern: PASSWORD_PATTERN,
              })}
            />
            {errors.actualPassword && (
              <div className={'invalid-feedback'}>
                Actual password is required, must consist of only English
                alphabet and/or numbers and/or symbols and be between 4 and 32
                characters.
              </div>
            )}
          </div>
          <div className='form-group mt-5'>
            <label
              className='form-label text-secondary'
              htmlFor='actual-password-repeat-input'
            >
              Actual password repeat:
            </label>
            <input
              className={`form-control ${
                errors.actualPasswordRepeat ? 'is-invalid' : ''
              }`}
              id='actual-password-repeat-input'
              placeholder='Enter actual password repeat'
              type='password'
              {...register('actualPasswordRepeat', {
                required: true,
                validate: (_, formValues) => {
                  return (
                    formValues.actualPassword ===
                    formValues.actualPasswordRepeat
                  );
                },
              })}
            />
            {errors.actualPasswordRepeat && (
              <div className={'invalid-feedback'}>
                Actual password repeat is required, must consist of only English
                alphabet and/or numbers and/or symbols and be between 4 and 32
                characters.
              </div>
            )}
          </div>
          <button className='btn btn-primary mt-3' type='submit'>
            Ask
          </button>
          <div className='text-secondary mt-2'>
            Remembered your password? <Link to={'/login'}> Login.</Link>
          </div>
          {requestResult && (
            <Alert requestResult={requestResult}>
              {requestResult === RequestResult.Loading && (
                <span>Password reset link creating...</span>
              )}
              {requestResult === RequestResult.Succeeded && (
                <span>
                  Password reset link creating succeeded. The link has been sent
                  to {getValues().email}. Please follow the link to reset your
                  password.
                </span>
              )}
              {requestResult === RequestResult.UnSucceeded && error && (
                <span>
                  Password reset link creating unsucceeded.
                  {error == 404
                    ? ` Email ${getValues().email} was not found.`
                    : ''}
                </span>
              )}
            </Alert>
          )}
        </div>
      </div>
    </form>
  );
};

export default AskPasswordResetThroughEmailForm;
