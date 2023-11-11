import { useEffect, useState } from 'react';
import { RequestResult } from '../../hooks/api/models';
import useApi from '../../hooks/api';

const FooBarForm = () => {
  const [fooRequestResult, setFooRequestResult] =
    useState<RequestResult | null>(null);
  const [barRequestResult, setBarRequestResult] =
    useState<RequestResult | null>(null);
  const fooBarApi = useApi().fooBarApi;

  const request = (
    foobar: 'foo' | 'bar',
    onSucceed: () => void,
    onUnSucceed: () => void,
  ) => {
    let promise;
    switch (foobar) {
      case 'foo': {
        promise = fooBarApi.foo();
        break;
      }
      case 'bar': {
        promise = fooBarApi.bar();
        break;
      }
    }
    promise
      .then(response => {
        if (response.status != 200) {
          throw new Error(
            `unsucceeded request result with status: ${response.status}`,
          );
        }
        return response.data;
      })
      .then(() => {
        onSucceed();
      })
      .catch(error => {
        onUnSucceed();
        console.debug(error);
      });
  };

  useEffect(() => {
    setFooRequestResult(RequestResult.Progress);
    request(
      'foo',
      () => {
        setFooRequestResult(RequestResult.Succeeded);
        setTimeout(() => setFooRequestResult(null), 10 * 1000);
      },
      () => {
        setFooRequestResult(RequestResult.UnSucceeded);
        setTimeout(() => setFooRequestResult(null), 10 * 1000);
      },
    );
  }, []);

  useEffect(() => {
    setBarRequestResult(RequestResult.Progress);
    request(
      'bar',
      () => {
        setBarRequestResult(RequestResult.Succeeded);
        setTimeout(() => setBarRequestResult(null), 10 * 1000);
      },
      () => {
        setBarRequestResult(RequestResult.UnSucceeded);
        setTimeout(() => setBarRequestResult(null), 10 * 1000);
      },
    );
  }, []);

  return (
    <div className='row'>
      <div className='col-md-12 m-auto mt-5 text-center'>
        <h2>Foo</h2>
        {fooRequestResult == RequestResult.Progress && (
          /* warning alert */
          <div className='alert alert-warning mt-3' role='alert'>
            Loading foo...
          </div>
        )}
        {fooRequestResult == RequestResult.Succeeded && (
          /* success alert */
          <div className='alert alert-success mt-3' role='alert'>
            Foo succeeded.
          </div>
        )}
        {fooRequestResult == RequestResult.UnSucceeded && (
          /* danger alert */
          <div className='alert alert-danger mt-3' role='alert'>
            Foo unsucceeded.
          </div>
        )}
      </div>
      <div className='col-md-12 m-auto mt-5 text-center'>
        <h2>Bar</h2>
        {barRequestResult == RequestResult.Progress && (
          /* warning alert */
          <div className='alert alert-warning mt-3' role='alert'>
            Loading bar...
          </div>
        )}
        {barRequestResult == RequestResult.Succeeded && (
          /* success alert */
          <div className='alert alert-success mt-3' role='alert'>
            Bar succeeded.
          </div>
        )}
        {barRequestResult == RequestResult.UnSucceeded && (
          /* danger alert */
          <div className='alert alert-danger mt-3' role='alert'>
            Bar unsucceeded.
          </div>
        )}
      </div>
    </div>
  );
};

export default FooBarForm;
