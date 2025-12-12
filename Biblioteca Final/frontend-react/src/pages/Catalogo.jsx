import React, { useEffect, useState } from 'react'
import api from '../api/axiosInstance'

export default function Catalogo() {
    const [libros, setLibros] = useState([])

    const placeholderImages = [
        'https://via.placeholder.com/150x200?text=Libro+1',
        'https://via.placeholder.com/150x200?text=Libro+2',
        'https://via.placeholder.com/150x200?text=Libro+3',
        'https://via.placeholder.com/150x200?text=Libro+4',
    ];

    const getRandomImage = () => {
        const index = Math.floor(Math.random() * placeholderImages.length);
        return placeholderImages[index];
    }

    useEffect(() => {
        api.get('/libros')
            .then(res => {
                // Asegurarnos de que cada libro tenga imagen válida
                const librosConImagen = res.data.map(l => ({
                    ...l,
                    imagen: l.imagen && l.imagen.trim() !== '' ? l.imagen : getRandomImage()
                }));
                setLibros(librosConImagen)
            })
            .catch(err => console.error("Error al cargar libros:", err))
    }, [])

    return (
        <div className="page-container">
            <h2 className="page-title">Catálogo de Libros Activos</h2>
            <div className="row">
                {libros.map(l => (
                    <div key={l.id} className="col-sm-6 col-md-4 col-lg-3 mb-4">
                        <div className="card h-100">
                            <img 
                                src={l.imagen} 
                                className="card-img-top" 
                                alt={l.titulo} 
                                style={{ objectFit: 'cover', height: '200px' }}
                                onError={(e) => {
                                    e.target.onerror = null
                                    e.target.src = getRandomImage()
                                }}
                            />
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title">{l.titulo}</h5>
                                <p className="card-text text-muted">{l.autor}</p>
                                <button className="btn btn-primary mt-auto">Ver más</button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}
