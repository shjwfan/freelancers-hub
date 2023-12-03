import {
  createBrowserRouter,
  createRoutesFromChildren,
  Route,
  RouterProvider,
} from 'react-router-dom';
import Navbar from './components/Navbar.tsx';
import LoginForm from './components/forms/login/LoginForm.tsx';
import AskPasswordResetThroughEmailForm from './components/forms/passwordreset/AskPasswordResetThroughEmailForm.tsx';
import ConfirmPasswordResetThroughEmailForm from './components/forms/passwordreset/ConfirmPasswordResetThroughEmailForm.tsx';
import DiscardPasswordResetThroughEmailForm from './components/forms/passwordreset/DiscardPasswordResetThroughEmailForm.tsx';
import PacksTable from './components/tables/PacksTable.tsx';
import ThemesTable from './components/tables/ThemesTable.tsx';
import WorksTable from './components/tables/WorksTable.tsx';

const router = createBrowserRouter(
  createRoutesFromChildren(
    <Route element={<Navbar />} path='/'>
      <Route element={<LoginForm />} path='/login' id='login' />
      <Route
        element={<AskPasswordResetThroughEmailForm />}
        path='/password-reset/email/ask'
        id='password-reset-through-email-ask'
      />
      <Route
        element={<ConfirmPasswordResetThroughEmailForm />}
        path='/password-reset/email/confirm'
        id='password-reset-through-email-confirm'
      />
      <Route
        element={<DiscardPasswordResetThroughEmailForm />}
        path='/password-reset/email/discard'
        id='password-reset-through-email-discard'
      />
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
