import React, { useEffect, useState } from "react";
import api from "../api/axiosInstance";
import { getCurrentUser } from "../api/auth";

export default function Prestamos() {
    const user = getCurrentUser();
    const isAdmin = user?.roles.includes("ROLE_ADMIN");

    const [prestamos, setPrestamos] = useState([]);
    const [usuarioId, setUsuarioId] = useState("");
    const [libroId, setLibroId] = useState("");
    const [fechaPrestamo, setFechaPrestamo] = useState("");
    const [fechaDevolucionEstimada, setFechaDevolucionEstimada] = useState("");

    // 🔹 Cargar préstamos según rol
    useEffect(() => {
        fetchPrestamos();
    }, []);

    const fetchPrestamos = () => {
        const url = isAdmin ? "/prestamos/admin" : "/prestamos/mios";
        api
            .get(url)
            .then((res) => setPrestamos(res.data))
            .catch((err) => console.error(err));
    };

    // 🔹 Registrar préstamo (solo admin)
    const handleCrearPrestamo = async (e) => {
        e.preventDefault();
        try {
            await api.post("/prestamos/admin", {
                usuarioId: parseInt(usuarioId),
                libroId: parseInt(libroId),
                fechaPrestamo,
                fechaDevolucionEstimada,
                fechaDevolucionReal: null,
                estado: "ACTIVO",
            });
            setUsuarioId("");
            setLibroId("");
            setFechaPrestamo("");
            setFechaDevolucionEstimada("");
            fetchPrestamos();
            alert("Préstamo registrado correctamente");
        } catch (err) {
            alert(err.response?.data?.message || "Error al registrar préstamo");
        }
    };

    // 🔹 Devolver préstamo (solo admin)
    const handleDevolver = async (id) => {
        if (!confirm("¿Marcar como devuelto?")) return;
        try {
            await api.put(`/prestamos/admin/${id}/devolver`);
            fetchPrestamos();
            alert("Préstamo devuelto");
        } catch (err) {
            alert(err.response?.data?.message || "Error al devolver préstamo");
        }
    };

    return (
        <div className="container mt-4">
            <h2>Gestión de Préstamos</h2>

            {/* FORMULARIO PARA ADMIN */}
            {isAdmin && (
                <form onSubmit={handleCrearPrestamo} className="card card-body mb-4">
                    <h5>Registrar Préstamo</h5>
                    <div className="row g-3">
                        <div className="col-md-3">
                            <input
                                type="number"
                                className="form-control"
                                placeholder="ID Usuario"
                                value={usuarioId}
                                min={1} // <-- evita valores menores que 1
                                onChange={(e) => {
                                    const value = parseInt(e.target.value);
                                    setUsuarioId(value >= 1 ? value : 1); // asegura que nunca baje de 1
                                }}
                                required
                            />
                        </div>
                        <div className="col-md-3">
                            <input
                                type="number"
                                className="form-control"
                                placeholder="ID Libro"
                                value={libroId}
                                min={1} // <-- evita valores menores que 1
                                onChange={(e) => {
                                    const value = parseInt(e.target.value);
                                    setLibroId(value >= 1 ? value : 1); // asegura que nunca baje de 1
                                }}
                                required
                            />
                        </div>

                        <div className="col-md-3">
                            <input
                                type="date"
                                className="form-control"
                                placeholder="Fecha Préstamo"
                                value={fechaPrestamo}
                                onChange={(e) => setFechaPrestamo(e.target.value)}
                                required
                            />
                        </div>
                        <div className="col-md-3">
                            <input
                                type="date"
                                className="form-control"
                                placeholder="Fecha Devolución Estimada"
                                value={fechaDevolucionEstimada}
                                onChange={(e) =>
                                    setFechaDevolucionEstimada(e.target.value)
                                }
                                required
                            />
                        </div>
                        <div className="col-md-12 mt-2">
                            <button type="submit" className="btn btn-primary w-100">
                                Registrar Préstamo
                            </button>
                        </div>
                    </div>
                </form>
            )}

            {/* TABLA DE PRÉSTAMOS */}
            <table className="table table-striped table-bordered">
                <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        {isAdmin && <th>Usuario</th>}
                        <th>Libro</th>
                        <th>Fecha Préstamo</th>
                        <th>Fecha Devolución Estimada</th>
                        <th>Fecha Devolución Real</th>
                        <th>Estado</th>
                        {isAdmin && <th>Acciones</th>}
                    </tr>
                </thead>
                <tbody>
                    {prestamos.map((p) => (
                        <tr key={p.id}>
                            <td>{p.id}</td>
                            {isAdmin && <td>{p.usuario}</td>}
                            <td>{p.libro}</td>
                            <td>{p.fechaPrestamo}</td>
                            <td>{p.fechaDevolucionEstimada}</td>
                            <td>{p.fechaDevolucionReal || "-"}</td>
                            <td>{p.estado}</td>
                            {isAdmin && (
                                <td>
                                    {p.estado === "PRESTADO" ? (
                                        <button
                                            className="btn btn-sm btn-success"
                                            onClick={() => handleDevolver(p.id)}
                                        >
                                            MARCAR COMO DEVUELTO
                                        </button>
                                    ) : (
                                        "MUCHAS GRACIAS POR DEVOLVER A TIEMPO :)"
                                    )}
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>

            </table>
        </div>
    );
}
