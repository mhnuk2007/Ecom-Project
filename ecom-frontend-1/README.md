
# Ecom-Frontend-1 (React + Vite)

This is a simple e-commerce frontend built with React and Vite. It features product listing, theme toggling, and routing for adding products (feature coming soon).

## Project Structure

- `src/`
  - `App.jsx`: Main app component with routing
  - `components/`
    - `Home.jsx`: Fetches and displays products from backend
    - `AddProduct.jsx`: Placeholder for adding products
    - `Navbar.jsx`: Navigation bar with theme toggle
  - `axios.jsx`: Axios instance for API calls
  - `main.jsx`: Entry point
  - `assets/`: Static assets
- `public/`: Public files
- `vite.config.js`: Vite configuration

## Dependencies

- React 18
- Vite 7
- Axios
- Bootstrap
- React Router DOM

## Getting Started

1. Install dependencies:
   ```bash
   npm install
   ```
2. Start development server:
   ```bash
   npm run dev
   ```
3. Open [http://localhost:5173](http://localhost:5173) (or the port shown in terminal) in your browser.

## Features

- Product listing fetched from backend API (`http://localhost:8080/api/products`)
- Theme toggle (light/dark)
- Routing for home and add product page

## Notes

- Ensure the backend API is running at `http://localhost:8080` for product data to load.
- The "Add Product" feature is a placeholder and will be implemented soon.
