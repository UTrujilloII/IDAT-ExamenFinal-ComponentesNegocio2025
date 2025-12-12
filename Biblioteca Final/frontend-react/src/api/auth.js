import api from './axiosInstance'

// Login
export const login = async (email, password) => {
    const res = await api.post('/auth/login', { email, password })

    // res.data = { token, nombre, roles }
    if (res.data?.token) {
        const userData = {
            token: res.data.token,
            nombre: res.data.nombre || email,  // 👈 GUARDAMOS EL NOMBRE REAL
            roles: res.data.roles || []
        }
        localStorage.setItem('user', JSON.stringify(userData))
    }

    return res.data
}

// Register
export const register = async (data) => {
    return api.post('/auth/register', data)
}

// Logout
export const logout = () => {
    localStorage.removeItem('user')
}

// Obtener usuario actual
export const getCurrentUser = () => {
    try {
        return JSON.parse(localStorage.getItem('user'))
    } catch (e) {
        return null
    }
}
