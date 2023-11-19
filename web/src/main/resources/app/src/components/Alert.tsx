import { RequestResult } from '../hooks/api';
import { PropsWithChildren } from 'react';

type AlertProps = {
  requestResult?: RequestResult;
  style?: 'secondary' | 'success' | 'danger';
};

const Alert = ({
  requestResult,
  style,
  children,
}: PropsWithChildren<AlertProps>) => {
  switch (requestResult) {
    case RequestResult.Loading:
      style = 'secondary';
      break;
    case RequestResult.Succeeded:
      style = 'success';
      break;
    case RequestResult.UnSucceeded:
      style = 'danger';
      break;
  }

  return (
    <div className={`alert ${style ? `alert-${style}` : ''} mt-3`} role='alert'>
      {children}
    </div>
  );
};

export default Alert;
