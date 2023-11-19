import * as axios from 'axios';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import useApi, { RequestResult } from '../../hooks/api';
import { Pack } from '../../hooks/api/models/business';
import Alert from '../Alert.tsx';

const PacksTable = () => {
  const [requestResult, setRequestResult] = useState<RequestResult | null>(
    null,
  );
  const [error, setError] = useState<number | null>(null);
  const [packs, setPacks] = useState<Pack[] | null>(null);
  const packsApi = useApi().packsApi;

  useEffect(() => {
    packsApi
      .loadPacks()
      .then(response => response.data)
      .then(packs => {
        setPacks(packs);
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
  }, [packsApi]);

  return (
    <div className='row'>
      <div className='col-md-12 m-auto mt-5'>
        <h2 className='permanent-marker'>Packs</h2>
        <table className='table mt-5'>
          <thead>
            <tr>
              <th scope='col'>#</th>
              <th scope='col'>Pack Name</th>
            </tr>
          </thead>
          <tbody>
            {packs &&
              packs.map((pack, i) => {
                return (
                  <tr key={i}>
                    <th scope='row'>{i + 1}</th>
                    <td>{pack.name}</td>
                  </tr>
                );
              })}
          </tbody>
        </table>
        {requestResult && (
          <Alert requestResult={requestResult}>
            {requestResult === RequestResult.Loading && !error && (
              <span>Loading packs...</span>
            )}
            {requestResult === RequestResult.Succeeded && !error && (
              <span>Loading packs succeeded.</span>
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

export default PacksTable;
