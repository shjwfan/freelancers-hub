import { NavLink, Outlet } from 'react-router-dom';

const Navbar = () => {
  return (
    <>
      <nav className='navbar navbar-expand-lg navbar-dark bg-dark'>
        <div className='container'>
          <a className='navbar-brand' href='/app'>
            Freelancers Hub
          </a>
          <button
            className='navbar-toggler'
            type='button'
            data-bs-target='navbar'
            data-bs-toggle='collapse'
          >
            <span className='navbar-toggler-icon'></span>
          </button>
          <div className='collapse navbar-collapse' id='navbar'>
            <ul className='navbar-nav'>
              <li className='nav-item'>
                <NavLink
                  to='login'
                  className={({ isActive, isPending: isDisabled }) =>
                    `nav-link ${isActive ? 'active' : ''} ${
                      isDisabled ? 'disabled' : ''
                    }`
                  }
                >
                  Login
                </NavLink>
              </li>
              <li className='nav-item'>
                <NavLink
                  to='foo-bar'
                  className={({ isActive, isPending: isDisabled }) =>
                    `nav-link ${isActive ? 'active' : ''} ${
                      isDisabled ? 'disabled' : ''
                    }`
                  }
                >
                  Foo & Bar
                </NavLink>
              </li>
            </ul>
          </div>
        </div>
      </nav>
      <div className='container'>
        <Outlet />
      </div>
    </>
  );
};

export default Navbar;
