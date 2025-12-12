import React, { useEffect, useState } from 'react'
import api from '../api/axiosInstance'

export default function Libros() {
    const [libros, setLibros] = useState([])

    const [titulo, setTitulo] = useState('')
    const [autor, setAutor] = useState('')
    const [isbn, setIsbn] = useState('')
    const [totalEjemplares, setTotalEjemplares] = useState(1)
    const [disponibles, setDisponibles] = useState(1)

    const [editMode, setEditMode] = useState(false)
    const [currentId, setCurrentId] = useState(null)

    useEffect(() => {
        fetchLibros()
    }, [])

    const fetchLibros = () => {
        api.get('/libros')
            .then(res => setLibros(res.data))
            .catch(err => console.error(err))
    }

    const resetForm = () => {
        setTitulo('')
        setAutor('')
        setIsbn('')
        setTotalEjemplares(1)
        setDisponibles(1)
        setEditMode(false)
        setCurrentId(null)
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        try {
            if (editMode) {
                await api.put(`/libros/${currentId}`, { titulo, autor, isbn, totalEjemplares, disponibles })
            } else {
                await api.post('/libros', { titulo, autor, isbn, totalEjemplares, disponibles })
            }
            resetForm()
            fetchLibros()
        } catch (err) {
            alert(err.response?.data?.message || "Error al guardar libro")
        }
    }

    const handleEdit = (libro) => {
        setEditMode(true)
        setCurrentId(libro.id)
        setTitulo(libro.titulo)
        setAutor(libro.autor)
        setIsbn(libro.isbn)
        setTotalEjemplares(libro.totalEjemplares)
        setDisponibles(libro.disponibles)
    }

    const handleDeactivate = async (id) => {
        if (!confirm("¿Desactivar este libro?")) return
        try {
            await api.delete(`/libros/${id}`)
            fetchLibros()
        } catch (err) {
            alert(err.response?.data?.message || "Error al desactivar libro")
        }
    }

    const handleActivate = async (id) => {
        try {
            await api.put(`/libros/activar/${id}`)
            fetchLibros()
        } catch (err) {
            alert(err.response?.data?.message || "Error al activar libro")
        }
    }

    return (
        <div className="container mt-4">
            <h2 className="mb-4">Gestionar Libros (Admin)</h2>

            {/* FORMULARIO */}
            <form onSubmit={handleSubmit} className="card card-body mb-4">
                <h5>{editMode ? "Editar Libro" : "Crear Libro"}</h5>
                <div className="row g-3">
                    <div className="col-md-4">
                        <input type="text" className="form-control" placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} required />
                    </div>
                    <div className="col-md-4">
                        <input type="text" className="form-control" placeholder="Autor" value={autor} onChange={e => setAutor(e.target.value)} required />
                    </div>
                    <div className="col-md-4">
                        <input type="text" className="form-control" placeholder="ISBN" value={isbn} onChange={e => setIsbn(e.target.value)} required />
                    </div>
                    <div className="col-md-2">
                        <input type="number" className="form-control" placeholder="Total Ejemplares" value={totalEjemplares} onChange={e => setTotalEjemplares(parseInt(e.target.value))} min="1" required />
                    </div>
                    <div className="col-md-2">
                        <input type="number" className="form-control" placeholder="Disponibles" value={disponibles} onChange={e => setDisponibles(parseInt(e.target.value))} min="0" required />
                    </div>
                    <div className="col-md-8">
                        <button type="submit" className="btn btn-primary me-2">{editMode ? "Guardar Cambios" : "Crear Libro"}</button>
                        {editMode && <button type="button" className="btn btn-secondary" onClick={resetForm}>Cancelar</button>}
                    </div>
                </div>
            </form>

            {/* TABLA DE LIBROS */}
            <table className="table table-striped table-bordered">
                <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>ISBN</th>
                        <th>Disponibles / Total</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {libros.map(l => (
                        <tr key={l.id}>
                            <td>{l.id}</td>
                            <td>{l.titulo}</td>
                            <td>{l.autor}</td>
                            <td>{l.isbn}</td>
                            <td>{l.disponibles} / {l.totalEjemplares}</td>
                            <td>{l.activo}</td>
                            <td>
                                <button className="btn btn-sm btn-warning me-1" onClick={() => handleEdit(l)}>Editar</button>
                                {l.activo === "si esta activo" ? (
                                    <button className="btn btn-sm btn-danger" onClick={() => handleDeactivate(l.id)}>Desactivar</button>
                                ) : (
                                    <button className="btn btn-sm btn-success" onClick={() => handleActivate(l.id)}>Activar</button>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}
