import React, { useState, useEffect } from 'react'
import api from '../api/axiosInstance'
import { getCurrentUser } from '../api/auth'

export default function Usuarios() {
    const user = getCurrentUser()
    const isAdmin = user?.roles.includes('ROLE_ADMIN')

    const [usuarios, setUsuarios] = useState([])
    const [searchId, setSearchId] = useState(1) // inicializamos en 1
    const [searchEmail, setSearchEmail] = useState('')
    const [resultado, setResultado] = useState(null)

    // Listar todos los usuarios al cargar (solo admin)
    useEffect(() => {
        if (!isAdmin) return
        fetchUsuarios()
    }, [isAdmin])

    const fetchUsuarios = async () => {
        try {
            const res = await api.get('/usuarios')
            setUsuarios(res.data)
        } catch (err) {
            console.error(err)
            alert('Error al cargar usuarios')
        }
    }

    const buscarPorId = async () => {
        if (searchId < 1) return alert('El ID debe ser mayor o igual a 1')
        try {
            const res = await api.get(`/usuarios/${searchId}`)
            setResultado(res.data)
        } catch (err) {
            console.error(err)
            alert('Usuario no encontrado')
            setResultado(null)
        }
    }

    const buscarPorEmail = async () => {
        if (!searchEmail) return
        try {
            const res = await api.get(`/usuarios/email/${encodeURIComponent(searchEmail)}`)
            setResultado(res.data)
        } catch (err) {
            console.error(err)
            alert('Usuario no encontrado')
            setResultado(null)
        }
    }

    return (
        <div className="container mt-4">
            <h2>Gestión de Usuarios</h2>

            {/* Búsqueda por ID */}
            <div className="mb-3 d-flex gap-2">
                <input
                    type="number"
                    placeholder="Buscar por ID"
                    value={searchId}
                    min={1} // no permite valores menores que 1
                    onChange={e => {
                        const value = parseInt(e.target.value)
                        setSearchId(value >= 1 ? value : 1)
                    }}
                />
                <button className="btn btn-primary" onClick={buscarPorId}>Buscar</button>
            </div>

            {/* Búsqueda por Email */}
            <div className="mb-3 d-flex gap-2">
                <input
                    type="email"
                    placeholder="Buscar por Email"
                    value={searchEmail}
                    onChange={e => setSearchEmail(e.target.value)}
                />
                <button className="btn btn-primary" onClick={buscarPorEmail}>Buscar</button>
            </div>

            {/* Resultado de búsqueda individual */}
            {resultado && (
                <div className="card mb-4 p-2">
                    <h5>Resultado de búsqueda</h5>
                    <p>ID: {resultado.id}</p>
                    <p>Nombre: {resultado.nombre}</p>
                    <p>Email: {resultado.email}</p>
                    <p>Roles: {resultado.roles.join(', ')}</p>
                </div>
            )}

            {/* Listado de todos los usuarios (solo admin) */}
            {isAdmin && (
                <div>
                    <h4>Todos los usuarios</h4>
                    <table className="table table-striped table-bordered">
                        <thead className="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Roles</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuarios.map(u => (
                                <tr key={u.id}>
                                    <td>{u.id}</td>
                                    <td>{u.nombre}</td>
                                    <td>{u.email}</td>
                                    <td>{u.roles.join(', ')}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    )
}
