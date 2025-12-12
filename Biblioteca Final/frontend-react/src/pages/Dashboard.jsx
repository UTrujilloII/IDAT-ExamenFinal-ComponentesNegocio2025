import React from "react";
import { getCurrentUser } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
    const user = getCurrentUser();
    const navigate = useNavigate();

    // 🔥 Asegurar que roles siempre sea un array
    const roles = user?.roles ?? [];

    // Determinar el tipo de usuario
    let tipoUsuario = "Usuario";
    if (roles.includes("ROLE_ADMIN")) {
        tipoUsuario = "Admin";
    } else if (roles.includes("ROLE_USUARIO")) {
        tipoUsuario = "Usuario";
    }

    return (
        <div>
            <h2>Bienvenido, {tipoUsuario}</h2>

            <div className="grid">
                {/* Catálogo */}
                <div className="card small">
                    <h3>Catálogo</h3>
                    <p>Ver libros activos</p>
                    <button onClick={() => navigate("/catalogo")}>Ir</button>
                </div>

                {/* Mis préstamos */}
                {roles.includes("ROLE_USUARIO") && (
                    <div className="card small">
                        <h3>Mis préstamos</h3>
                        <p>Ver y gestionar tus préstamos</p>
                        <button onClick={() => navigate("/mis-prestamos")}>Ir</button>
                    </div>
                )}

                {/* Funciones solo para admin */}
                {roles.includes("ROLE_ADMIN") && (
                    <>
                        <div className="card small">
                            <h3>Gestionar Libros</h3>
                            <p>CRUD de libros</p>
                            <button onClick={() => navigate("/libros")}>Ir</button>
                        </div>

                        <div className="card small">
                            <h3>Todos los préstamos</h3>
                            <p>Ver y gestionar los préstamos de todos los usuarios</p>
                            <button onClick={() => navigate("/prestamos")}>Ir</button>
                        </div>

                        {/* Gestión de usuarios */}
                        <div className="card small">
                            <h3>Gestión de Usuarios</h3>
                            <p>Ver, buscar y gestionar todos los usuarios</p>
                            <button onClick={() => navigate("/usuarios")}>Ir</button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}
