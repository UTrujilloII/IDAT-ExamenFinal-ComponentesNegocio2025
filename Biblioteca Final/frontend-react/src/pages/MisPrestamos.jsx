import React, { useEffect, useState } from 'react'
import api from '../api/axiosInstance'
import { getCurrentUser } from '../api/auth'

export default function MisPrestamos() {
    const [prestamos, setPrestamos] = useState([])
    const user = getCurrentUser()

    useEffect(() => {
        if (!user) return

        // 🔹 Usamos la ruta que ya funciona en Prestamos.jsx
        api.get("/prestamos/mios")
            .then(res => setPrestamos(res.data))
            .catch(err => console.error(err))
    }, [user])

    if (!user) {
        return <p>No estás autenticado.</p>
    }

    return (
        <div className="container mt-4">
            <h2>Mis préstamos</h2>

            {prestamos.length === 0 ? (
                <p>No tienes préstamos activos.</p>
            ) : (
                <table className="table table-striped table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Libro</th>
                            <th>Fecha Préstamo</th>
                            <th>Fecha Devolución Estimada</th>
                            <th>Fecha Devolución Real</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        {prestamos.map(p => (
                            <tr key={p.id}>
                                <td>{p.id}</td>
                                <td>{p.libro?.titulo || p.libro}</td>
                                <td>{new Date(p.fechaPrestamo).toLocaleDateString()}</td>
                                <td>{p.fechaDevolucionEstimada ? new Date(p.fechaDevolucionEstimada).toLocaleDateString() : "-"}</td>
                                <td>{p.fechaDevolucionReal ? new Date(p.fechaDevolucionReal).toLocaleDateString() : "-"}</td>
                                <td>{p.estado}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    )
}
