import React, { useState } from 'react'
import { register } from '../api/auth'
import { useNavigate } from 'react-router-dom'

export default function Register() {
    const [nombre, setNombre] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [rol, setRol] = useState('ROLE_USUARIO') // solo un rol
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)
        try {
            // Prepara el JSON que espera el backend
            const data = {
                nombre,
                email,
                password,
                roles: [rol], // siempre un array de strings
            }
            await register(data) // enviar correctamente
            alert('Usuario registrado con éxito')
            navigate('/login')
        } catch (err) {
            console.error(err.response?.data)
            alert(err?.response?.data?.message || 'Error al registrar')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="card p-4">
            <h2>Registro</h2>
            <form onSubmit={handleSubmit} className="d-flex flex-column gap-2">
                <input
                    placeholder="Nombre"
                    value={nombre}
                    onChange={e => setNombre(e.target.value)}
                    required
                />
                <input
                    type="email"
                    placeholder="Correo"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />

                {/* Selección de rol */}
                <div>
                    <label>
                        <input
                            type="radio"
                            name="rol"
                            value="ROLE_USUARIO"
                            checked={rol === 'ROLE_USUARIO'}
                            onChange={() => setRol('ROLE_USUARIO')}
                        />
                        ROLE_USUARIO
                    </label>
                    <label className="ms-2">
                        <input
                            type="radio"
                            name="rol"
                            value="ROLE_ADMIN"
                            checked={rol === 'ROLE_ADMIN'}
                            onChange={() => setRol('ROLE_ADMIN')}
                        />
                        ROLE_ADMIN
                    </label>
                </div>

                <button type="submit" disabled={loading}>
                    {loading ? 'Cargando...' : 'Registrar'}
                </button>
            </form>
        </div>
    )
}
