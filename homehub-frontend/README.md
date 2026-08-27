# HomeHub Frontend

React + TypeScript + Vite frontend for the current HomeHub Spring Boot API.

## Requirements

- Node.js 20+
- HomeHub backend running on `http://localhost:8080`

## Run

```bash
npm install
npm run dev
```

Open `http://localhost:5173`.

Vite proxies `/api/*` to `http://localhost:8080`, so CORS is not required during local development.

## Implemented backend endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/expenses`
- `GET /api/v1/expenses/sum?month=...`
- `POST /api/v1/expenses/create`
- `DELETE /api/v1/expenses/{id}` (shown only when JWT contains an ADMIN role)
- `POST /api/v1/income/create`

## Notes

The current `ExpenseResponseDTO` does not return `description`, so the table cannot display it.

The sum endpoint accepts only `month`, so the month selector is intentionally independent of the date-range filter.

If the backend is served from another host/port, change `target` in `vite.config.ts`.
