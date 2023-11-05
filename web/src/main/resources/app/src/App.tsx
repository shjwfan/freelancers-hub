import {
  createBrowserRouter,
  createRoutesFromChildren,
  Route,
  RouterProvider,
} from 'react-router-dom';
import Navbar from './components/Navbar.tsx';
import LoginForm from './components/forms/LoginForm.tsx';
import FooBarForm from './components/forms/FooBarForm.tsx';

const router = createBrowserRouter(
  createRoutesFromChildren(
    <Route element={<Navbar />} path='/'>
      <Route element={<LoginForm />} path='/login' id='login' />
      <Route element={<FooBarForm />} path='/foo-bar' id='foo-bar' />
    </Route>,
  ),
  { basename: '/app' },
);

const App = () => {
  return <RouterProvider router={router} />;
};

export default App;
