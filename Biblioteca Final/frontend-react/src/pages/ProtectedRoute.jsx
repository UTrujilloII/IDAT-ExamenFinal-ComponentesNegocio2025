import React from 'react'
import { Navigate } from 'react-router-dom'
import { getCurrentUser } from '../api/auth'

export default function ProtectedRoute({ children }) {
    const user = getCurrentUser()

    if (!user) {
        // Si no hay usuario logueado, redirige a login
        return <Navigate to="/login" replace />
    }

    // Si hay usuario, renderiza los children (ruta protegida)
    return children
}
