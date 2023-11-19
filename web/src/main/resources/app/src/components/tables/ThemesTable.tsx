import * as axios from 'axios';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import useApi, { RequestResult } from '../../hooks/api';
import { Theme } from '../../hooks/api/models/business';
import Alert from '../Alert.tsx';

const ThemesTable = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const [error, setError] = useState<number | null>(null);
  const [themes, setThemes] = useState<Theme[] | null>(null);
  const themesApi = useApi().themesApi;

  useEffect(() => {
    themesApi
      .loadThemes()
      .then(response => response.data)
      .then(themes => {
        setThemes(themes);
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
  }, [themesApi]);

  return (
    <div className='row'>
      <div className='col-md-12 m-auto mt-5'>
        <h2 className='permanent-marker'>Themes</h2>
        <table className='table mt-5'>
          <thead>
            <tr>
              <th scope='col'>#</th>
              <th scope='col'>Theme Name</th>
            </tr>
          </thead>
          <tbody>
            {themes &&
              themes.map((theme, i) => {
                return (
                  <tr key={i}>
                    <th scope='row'>{i + 1}</th>
                    <td>{theme.name}</td>
                  </tr>
                );
              })}
          </tbody>
        </table>
        {requestResult && (
          <Alert requestResult={requestResult}>
            {requestResult === RequestResult.Loading && !error && (
              <span>Loading themes...</span>
            )}
            {requestResult === RequestResult.Succeeded && !error && (
              <span>Loading themes succeeded.</span>
            )}
            {requestResult === RequestResult.UnSucceeded && error === 403 && (
              <span>
                Forbidden request, you need to <Link to={'login'}>login</Link>{' '}
                or <Link to={'login'}>re-login</Link> as admin user.
              </span>
            )}
          </Alert>
        )}
      </div>
    </div>
  );
};

export default ThemesTable;
