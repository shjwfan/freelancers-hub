import * as axios from 'axios';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import useApi, { RequestResult } from '../../hooks/api';
import { Work } from '../../hooks/api/models/business';
import Alert from '../Alert.tsx';

const WorksTable = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const [error, setError] = useState<number | null>(null);
  const [works, setWorks] = useState<Work[] | null>(null);
  const worksApi = useApi().worksApi;

  useEffect(() => {
    worksApi
      .loadWorks()
      .then(response => response.data)
      .then(works => {
        setWorks(works);
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
  }, [worksApi]);

  return (
    <div className='row'>
      <div className='col-md-12 m-auto mt-5'>
        <h2 className='permanent-marker'>Works</h2>
        <table className='table mt-5'>
          <thead>
            <tr>
              <th scope='col'>#</th>
              <th scope='col'>Work Name</th>
            </tr>
          </thead>
          <tbody>
            {works &&
              works.map((work, i) => {
                return (
                  <tr key={i}>
                    <th scope='row'>{i + 1}</th>
                    <td>{work.name}</td>
                  </tr>
                );
              })}
          </tbody>
        </table>
        {requestResult && (
          <Alert requestResult={requestResult}>
            {requestResult === RequestResult.Loading && !error && (
              <span>Loading works...</span>
            )}
            {requestResult === RequestResult.Succeeded && !error && (
              <span>Loading works succeeded.</span>
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

export default WorksTable;
