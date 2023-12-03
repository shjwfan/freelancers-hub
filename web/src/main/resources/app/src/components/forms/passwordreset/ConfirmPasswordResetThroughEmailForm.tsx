import { Link } from 'react-router-dom';

const ConfirmPasswordResetThroughEmailForm = () => {
  return (
    <div className='row'>
      <div className='col-md-3 m-auto mt-5 text-center'>
        <h2 className='permanent-marker text-success'>Password was reset</h2>
        <div className='text-secondary mt-2'>
          <Link to={'/login'}> Login.</Link>
        </div>
      </div>
    </div>
  );
};

export default ConfirmPasswordResetThroughEmailForm;
