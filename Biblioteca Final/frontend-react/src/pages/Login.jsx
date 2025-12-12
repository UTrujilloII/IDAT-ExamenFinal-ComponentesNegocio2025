import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../api/auth'

export default function Login() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)

        try {
            await login(email, password)
            navigate('/dashboard')
        } catch (err) {
            alert(err?.response?.data?.message || 'Error al iniciar sesión')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="card">
            <h2>Iniciar sesión</h2>

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Correo"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                />

                <button type="submit" disabled={loading}>
                    {loading ? 'Cargando...' : 'Entrar'}
                </button>
            </form>
        </div>
    )
}
