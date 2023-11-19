import {
  createBrowserRouter,
  createRoutesFromChildren,
  Route,
  RouterProvider,
} from 'react-router-dom';
import Navbar from './components/Navbar.tsx';
import LoginForm from './components/forms/LoginForm.tsx';
import PacksTable from './components/tables/PacksTable.tsx';
import WorksTable from './components/tables/WorksTable.tsx';
import ThemesTable from './components/tables/ThemesTable.tsx';

const router = createBrowserRouter(
  createRoutesFromChildren(
    <Route element={<Navbar />} path='/'>
      <Route element={<LoginForm />} path='/login' id='login' />
      <Route element={<PacksTable />} path='/packs' id='packs' />
      <Route element={<ThemesTable />} path='/themes' id='themes' />
      <Route element={<WorksTable />} path='/works' id='works' />
    </Route>,
  ),
  { basename: '/app' },
);

const App = () => {
  return <RouterProvider router={router} />;
};

export default App;
