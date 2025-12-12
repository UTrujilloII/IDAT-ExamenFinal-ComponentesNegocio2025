import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Catalogo from './pages/Catalogo'
import Libros from './pages/Libros'
import Prestamos from './pages/Prestamos'
import MisPrestamos from './pages/MisPrestamos'
import Usuarios from './pages/Usuarios' // ✅ Página de gestión de usuarios
import PrivateRoute from './routes/PrivateRoute'
import "./styles/global.css";


export default function App() {
  return (
    <div>
      <Navbar />
      <main className="container">
        <Routes>
          {/* Redirección inicial */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />

          {/* Rutas públicas */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Dashboard (cualquier usuario logueado) */}
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />

          {/* Catálogo: accesible por usuarios y admins */}
          <Route
            path="/catalogo"
            element={
              <PrivateRoute roles={["ROLE_USUARIO", "ROLE_ADMIN"]}>
                <Catalogo />
              </PrivateRoute>
            }
          />

          {/* Mis préstamos: accesible por usuarios y admins */}
          <Route
            path="/mis-prestamos"
            element={
              <PrivateRoute roles={["ROLE_USUARIO", "ROLE_ADMIN"]}>
                <MisPrestamos />
              </PrivateRoute>
            }
          />

          {/* Todos los préstamos: solo admin */}
          <Route
            path="/prestamos"
            element={
              <PrivateRoute roles={["ROLE_ADMIN"]}>
                <Prestamos />
              </PrivateRoute>
            }
          />

          {/* Gestión de libros: solo admin */}
          <Route
            path="/libros"
            element={
              <PrivateRoute roles={["ROLE_ADMIN"]}>
                <Libros />
              </PrivateRoute>
            }
          />

          {/* Gestión de usuarios: solo admin */}
          <Route
            path="/usuarios"
            element={
              <PrivateRoute roles={["ROLE_ADMIN"]}>
                <Usuarios />
              </PrivateRoute>
            }
          />

          {/* Ruta comodín: redirige a dashboard si no existe */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </main>
    </div>
  )
}
