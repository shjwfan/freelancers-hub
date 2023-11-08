import { useEffect, useState } from 'react';
import axios from 'axios';

const FooBarForm = () => {
  const [fooRequestResult, setFooRequestResult] =
    useState<api.RequestResult | null>(null);
  const [barRequestResult, setBarRequestResult] =
    useState<api.RequestResult | null>(null);

  const request = (
    foobar: 'foo' | 'bar',
    onSucceed: () => void,
    onUnSucceed: () => void,
  ) => {
    const url = new URL(foobar, window.origin);
    axios
      .get<string>(url.toString())
      .then(response => {
        if (response.status != 200) {
          throw new Error(`unsucceeded request result with status: ${status}`);
        }
        return response.data;
      })
      .then(data => {
        onSucceed();
      })
      .catch(error => {
        onUnSucceed();
        console.error(error);
      });
  };

  useEffect(() => {
    request(
      'foo',
      () => {
        setFooRequestResult(api.RequestResult.Succeeded);
        setTimeout(() => setFooRequestResult(null), 10 * 1000);
      },
      () => {
        setFooRequestResult(api.RequestResult.UnSucceeded);
        setTimeout(() => setFooRequestResult(null), 10 * 1000);
      },
    );
  }, []);

  useEffect(() => {
    request(
      'bar',
      () => {
        setBarRequestResult(api.RequestResult.Succeeded);
        setTimeout(() => setBarRequestResult(null), 10 * 1000);
      },
      () => {
        setBarRequestResult(api.RequestResult.UnSucceeded);
        setTimeout(() => setBarRequestResult(null), 10 * 1000);
      },
    );
  }, []);

  return (
    <div className='row'>
      <div className='col-md-3 m-auto mt-5 text-center'>
        <h2>Foo</h2>
      </div>
      {fooRequestResult == api.RequestResult.Progress && (
        <div className='col-md-3 m-auto mt-3 alert alert-warning' role='alert'>
          Loading foo...
        </div>
      )}
      {fooRequestResult == api.RequestResult.Succeeded && (
        <div className='col-md-3 m-auto mt-3 alert alert-success' role='alert'>
          Foo succeeded.
        </div>
      )}
      {fooRequestResult == api.RequestResult.UnSucceeded && (
        <div className='col-md-3 m-auto mt-3 alert alert-danger' role='alert'>
          Foo unsucceeded.
        </div>
      )}
      <div className='col-md-3 m-auto mt-5 text-center'>
        <h2>Bar</h2>
      </div>
      {barRequestResult == api.RequestResult.Progress && (
        <div className='col-md-3 m-auto mt-3 alert alert-warning' role='alert'>
          Loading bar...
        </div>
      )}
      {barRequestResult == api.RequestResult.Succeeded && (
        <div className='col-md-3 m-auto mt-3 alert alert-success' role='alert'>
          Bar succeeded.
        </div>
      )}
      {barRequestResult == api.RequestResult.UnSucceeded && (
        <div className='col-md-3 m-auto mt-3 alert alert-danger' role='alert'>
          Bar unsucceeded.
        </div>
      )}
    </div>
  );
};

export default FooBarForm;
