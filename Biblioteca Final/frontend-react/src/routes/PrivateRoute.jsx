import React from 'react'
import { Navigate } from 'react-router-dom'
import { getCurrentUser } from '../api/auth'

export default function PrivateRoute({ children, roles }) {
    const user = getCurrentUser()

    // ❌ No logueado
    if (!user) return <Navigate to="/login" replace />

    // 🔥 PROTECCIÓN: asegurar array
    const userRoles = user.roles ?? []

    // 🔥 Si la ruta requiere roles, verificarlos correctamente
    if (roles?.length > 0) {
        const hasRole = roles.some(r => userRoles.includes(r))
        if (!hasRole) return <Navigate to="/dashboard" replace />
    }

    return children
}
