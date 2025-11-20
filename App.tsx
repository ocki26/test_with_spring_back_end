
import React from 'react';
import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { DataProvider } from './contexts/DataContext';

import LoginPage from './pages/LoginPage';
import AdminLayout from './pages/admin/AdminLayout';
import Dashboard from './pages/admin/Dashboard';
import ManageUsers from './pages/admin/ManageUsers';
import ManageShows from './pages/admin/ManageShows';
import CompanyLayout from './pages/company/CompanyLayout';
import CompanyDashboard from './pages/company/CompanyDashboard';
import ManageArtists from './pages/company/ManageArtists';
import ProtectedRoute from './components/ProtectedRoute';

const App: React.FC = () => {
  return (
    <HashRouter>
      <AuthProvider>
        <DataProvider>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            
            <Route path="/admin" element={<ProtectedRoute role="ADMIN"><AdminLayout /></ProtectedRoute>}>
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="users" element={<ManageUsers />} />
              <Route path="shows" element={<ManageShows />} />
              <Route index element={<Navigate to="dashboard" replace />} />
            </Route>

            <Route path="/company" element={<ProtectedRoute role="COMPANY"><CompanyLayout /></ProtectedRoute>}>
              <Route path="dashboard" element={<CompanyDashboard />} />
              <Route path="artists" element={<ManageArtists />} />
              <Route index element={<Navigate to="dashboard" replace />} />
            </Route>

            <Route path="*" element={<AppRoutes />} />
          </Routes>
        </DataProvider>
      </AuthProvider>
    </HashRouter>
  );
};

const AppRoutes: React.FC = () => {
    const { user } = useAuth();
    if (!user) {
        return <Navigate to="/login" replace />;
    }
    switch (user.role) {
        case 'ADMIN':
            return <Navigate to="/admin" replace />;
        case 'COMPANY':
            return <Navigate to="/company" replace />;
        default:
            return <Navigate to="/login" replace />;
    }
};

export default App;