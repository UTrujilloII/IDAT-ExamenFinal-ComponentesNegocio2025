# Frontend React - Biblioteca


Proyecto React (Vite) para la app Biblioteca. Está pensado para conectarse a tu backend Spring Boot en `http://localhost:8080`.


## Comandos
- `npm install` -> instalar dependencias
- `npm run dev` -> levantar en desarrollo (http://localhost:5173)
- `npm run build` -> generar build para producción
- `npm run preview` -> previsualizar el build


## Notas importantes
- El `vite.config.js` contiene un proxy para `/api` hacia `http://localhost:8080`, por eso las llamadas usan `VITE_API_BASE_URL=/api`.
- Asegúrate de que tu backend permita CORS en `http://localhost:5173` (en desarrollo). En producción puedes servir el `dist/` desde Spring Boot.
- El login espera que el endpoint `POST /api/auth/login` devuelva JSON con `username`, `token` y `roles`.
- Los endpoints esperados por el frontend:
- `GET /api/libros` (admin)
- `GET /api/libros/activos` (todos los usuarios)
- `POST /api/libros` (admin)
- `PATCH /api/libros/{id}/activo` (admin)
- `DELETE /api/libros/{id}` (admin)
- `GET /api/prestamos` (admin)
- `GET /api/prestamos/usuario/{username}` (usuario)
- `POST /api/auth/register`
- `POST /api/auth/login`


## Integración con MySQL
- No hay cambios en el frontend por usar MySQL; tu backend Spring Boot seguirá gestionando la persistencia.




// End of files