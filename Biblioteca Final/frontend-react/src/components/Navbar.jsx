import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { getCurrentUser, logout } from '../api/auth'
import "./Navbar.css"; // 👈 Usa tu CSS con glitch agresivo

export default function Navbar() {
    const user = getCurrentUser()
    const navigate = useNavigate()

    const roles = user?.roles || []

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <nav className="nav-container">
            
            {/* LOGO con glitch RGB */}
            <div className="nav-logo">
                <Link to="/" data-text="📚 Biblioteca">
                    📚 Biblioteca
                    {/* Capa azul extra necesaria para glitch */}
                    <span className="glitch-rgb"></span>
                </Link>
            </div>

            <div className="nav-links">
                {!user && (
                    <>
                        <Link className="nav-btn" to="/login">Iniciar sesión</Link>
                        <Link className="nav-btn" to="/register">Registrarse</Link>
                    </>
                )}

                {user && (
                    <>
                        <span className="nav-user">👤 {user.nombre}</span>

                        <Link className="nav-link" to="/dashboard">Dashboard</Link>
                        <Link className="nav-link" to="/catalogo">Catálogo</Link>

                        {roles.includes('ROLE_ADMIN') && (
                            <>
                                <Link className="nav-link" to="/libros">Libros</Link>
                                <Link className="nav-link" to="/prestamos">Préstamos</Link>
                                <Link className="nav-link" to="/usuarios">Usuarios</Link>
                            </>
                        )}

                        {roles.includes('ROLE_USUARIO') && (
                            <Link className="nav-link" to="/mis-prestamos">Mis Préstamos</Link>
                        )}

                        <button className="logout-btn" onClick={handleLogout}>
                            Cerrar sesión
                        </button>
                    </>
                )}
            </div>
        </nav>
    )
}
